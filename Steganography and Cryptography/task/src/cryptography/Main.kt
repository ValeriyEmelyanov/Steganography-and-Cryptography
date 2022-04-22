package cryptography

import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.experimental.xor

const val LEAST_BLUE_BIT_ON = 1
const val STOP_BYTES_LENGTH = 3
const val STOP_BYTE_1 = 0
const val STOP_BYTE_2 = 0
const val STOP_BYTE_3 = 3

fun main() {
    while (true) {
        println("Task (hide, show, exit):")
        val task = readLine()!!
        when (task) {
            "exit" -> break
            "hide" -> hideMessage()
            "show" -> showMessage()
            else -> println("Wrong task: $task")
        }
    }

    println("Bye!")
}

private fun hideMessage() {
    println("Input image file:")
    val inputFileName = readLine()!!
    println("Output image file:")
    val outputFileName = readLine()!!

    val image: BufferedImage
    try {
        val inputFile = File(inputFileName)
        image = ImageIO.read(inputFile)
    } catch (e: Exception) {
        println("Can't read input file!")
        return
    }

    println("Message to hide:")
    val message = readLine()!!
    println("Password:")
    val password = readLine()!!

    val bytes = stringToEncodedByteArrayWithStopBytes(message, password)
    val bits = byteArrayToBitArray(bytes)
    if (bits.size > image.width * image.height) {
        println("The input image is not large enough to hold this message.")
        return
    }

    val newImage = encodeImage(image, bits)

    try {
        val outputFile = File(outputFileName)
        ImageIO.write(newImage, "png", outputFile)
    } catch (e: Exception) {
        println("Can't write output file!")
        return
    }

    println("Message saved in $outputFileName image.")
}

private fun byteToBitArray(byte: Byte): IntArray {
    val bits = IntArray(8)
    var remainder = byte.toInt()
    for (i in 7 downTo 0) {
        bits[i] = remainder % 2
        remainder /= 2
    }
    return bits
}

private fun byteArrayToBitArray(bytes: ByteArray): IntArray {
    val bits = IntArray(bytes.size * 8)
    for (i in 0 until bytes.size) {
        System.arraycopy(byteToBitArray(bytes[i]), 0, bits, i * 8, 8)
    }
    return bits
}

private fun stringToEncodedByteArrayWithStopBytes(message: String, password: String): ByteArray {
    var bytes = message.toByteArray()
    encodeByteArray(bytes, password.toByteArray())
    bytes += byteArrayOf(STOP_BYTE_1.toByte(), STOP_BYTE_2.toByte(), STOP_BYTE_3.toByte())
    return bytes
}

private fun encodeByteArray(bytes: ByteArray, passwordBytes: ByteArray) {
    val passLen = passwordBytes.size
    var passInd = 0
    for (i in 0..bytes.lastIndex) {
        if (passInd >= passLen) passInd = 0
        bytes[i] = bytes[i].xor(passwordBytes[passInd])
        passInd++
    }
}

private fun encodeImage(image: BufferedImage, bits: IntArray): BufferedImage {
    val width = image.width
    val height = image.height
    val result = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

    var x = 0
    var y = 0

    for (i in 0 until bits.size) {
        x = i % width
        y = i / width
        val color = Color(image.getRGB(x, y))
        val newRgb = Color(color.red, color.green, color.blue.and(254).or(bits[i])).rgb
        result.setRGB(x, y, newRgb)
    }

    x++
    while (x < width) {
        result.setRGB(x, y, image.getRGB(x, y))
        x++
    }

    for (y1 in y + 1 until height) {
        for (x1 in 0 until width) {
            result.setRGB(x1, y1, image.getRGB(x1, y1))
        }
    }

    return result
}

private fun showMessage() {
    println("Input image file:")
    val fileName = readLine()!!

    val image: BufferedImage
    try {
        val inputFile = File(fileName)
        image = ImageIO.read(inputFile)
    } catch (e: Exception) {
        println("Can't read file!")
        return
    }

    println("Password:")
    val password = readLine()!!

    val message = decodeImage(image, password)

    println("Message:")
    println(message)
}

fun decodeImage(image: BufferedImage, password: String): String {
    val bits = IntArray(8)
    var bitCounter = 0
    val bytesList = mutableListOf<Int>()

    for (y in 0 until image.height) {
        for (x in 0 until image.width) {
            if (bitCounter >= 8) {
                bitCounter = 0
                bytesList.add(bitsToByte(bits))
            }
            if (bytesList.size >= STOP_BYTES_LENGTH && isStopBytes(bytesList)) {
                break
            }
            bits[bitCounter] = if (image.getRGB(x, y) and LEAST_BLUE_BIT_ON == LEAST_BLUE_BIT_ON) 1 else 0
            bitCounter++
        }
    }

    for (i in 1..STOP_BYTES_LENGTH) bytesList.removeLast()

    val bytes = bytesList.map { it.toByte() }.toByteArray()
    encodeByteArray(bytes, password.toByteArray())

    return String(bytes)
}

fun isStopBytes(bytesList: MutableList<Int>): Boolean {
    val lastIndex = bytesList.lastIndex
    return bytesList[lastIndex] == STOP_BYTE_3 &&
            bytesList[lastIndex - 1] == STOP_BYTE_2 &&
            bytesList[lastIndex - 2] == STOP_BYTE_1
}

fun bitsToByte(bits: IntArray): Int {
    var byte = bits[0]
    for (i in 1..7) {
        byte = (byte shl 1) or bits[i]
    }
    return byte
}
