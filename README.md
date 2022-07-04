# Steganography and Cryptography

Using encryption to send secret messages,
hiding message in an image. 
Kotlin training project from JetBrains Academy.

### In project
* Kotlin
* Bitwise and bit-shift operations
* Working with images
* ByteArray

### Encrypt the message
* The program reads the **password** string and converts it to a Bytes Array.
* The **message** should be converted to an array of bytes.
  Then, 3 bytes with the values 0, 0, 3 should be added at the end of the array.
* The program should check that the image size is adequate for holding the Bytes array.
  The image can hold up to [image width]*[image height] bits.
* The first message byte will be XOR encrypted using the first password byte, 
  the second message byte will be XOR encrypted with the second password byte, 
  and so on. 
  If the password is shorter than the message, 
  then after the last byte of the password, the first byte of the password should be used again.
* Each bit of this Bytes Array will be saved at the position of the least significant bit 
  of the blue color of each pixel.
