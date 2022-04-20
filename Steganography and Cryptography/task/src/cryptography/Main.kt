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
            "hide" -> println("Hiding message in image.")
            "show" -> println("Obtaining message from image.")
            else -> println("Wrong task: $task")
        }
    }

    println("Bye!")
}
