package cryptography

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main() {
    while (true) {
        println("Task (hide, show, exit):")
        val task = readLine()!!
        when (task) {
            "exit" -> break
            "hide" -> hideMessage()
            "show" -> println("Obtaining message from image.")
            else -> println("Wrong task: $task")
        }
    }

    println("Bye!")
}

const val LEAST_ONE_BITS = 65793

fun hideMessage() {
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

    println("Input Image: $inputFileName")
    println("Output Image: $outputFileName")

    for (x in 0 until image.width) {
        for (y in 0 until image.height) {
            val rgb = image.getRGB(x, y)
            image.setRGB(x, y, rgb or LEAST_ONE_BITS)
        }
    }

    try {
        val outputFile = File(outputFileName)
        ImageIO.write(image, "png", outputFile)
    } catch (e: Exception) {
        println("Can't write output file!")
        return
    }

    println("Image $outputFileName is saved.")
}
