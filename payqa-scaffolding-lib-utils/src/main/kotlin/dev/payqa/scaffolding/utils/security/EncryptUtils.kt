package dev.payqa.scaffolding.utils.security

import org.apache.commons.codec.binary.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class EncryptUtils(private val seed: Int) {

    private val encrypter = Sequence(AES())

    fun encrypt(textToEncrypt: String) = encrypter.encrypt(textToEncrypt, seed)

    fun decrypt(textToDecrypt: String) = encrypter.decrypt(textToDecrypt, seed)

    private inner class AES {

        private val password = "9827637890298371".toByteArray()
        private val vector = "1738920987367289".toByteArray()

        private val iv = IvParameterSpec(vector)
        private val keySpec = SecretKeySpec(password, "AES")
        private val cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING")

        fun encrypt(value: String): String {
            cipher!!.init(Cipher.ENCRYPT_MODE, keySpec, iv)
            val encrypted = cipher.doFinal(value.toByteArray())
            return Base64.encodeBase64String(encrypted)
        }

        fun decrypt(value: String): String {
            cipher!!.init(Cipher.DECRYPT_MODE, keySpec, iv)
            val original = cipher.doFinal(Base64.decodeBase64(value))
            return String(original)
        }

    }

    private inner class Sequence(private val aes: AES) {

        private fun encodeListNumber(value: String): String? {
            val textToEncrypt = value.split(",").toTypedArray()
            val length = arrayOfNulls<Int>(textToEncrypt.size)
            var output = ""
            for (i in textToEncrypt.indices) {
                length[i] = textToEncrypt[i].toInt()
                output = String.format("%s%s", output, (length[i] as Int).toChar())
            }
            return output.substring(0, output.length)
        }

        private fun decodeListNumber(value: String): Array<Int?> {
            val length = value.length
            val output = arrayOfNulls<Int>(length)
            for (i in 0 until length) {
                output[i] = value[i].toInt()
            }
            return output
        }

        private fun createSequence(point: Int, length: Int): Array<Int?> {
            val number = point * 2 % 102
            val pow = Math.pow(2.0, 10.0).toInt()
            var aux = point
            val output = arrayOfNulls<Int>(length)
            for (i in 0 until length) {
                aux = (aux * aux + number) % pow
                output[i] = aux
            }
            return output
        }

        private fun generateArray(seed: Int, length: Int): Array<String?> {
            val output = arrayOfNulls<String>(length)
            var array = arrayOfNulls<Int>(length)
            array = createSequence(seed, length)
            for (i in array.indices) {
                if (array[i]!! % 3 != 0) {
                    output[i] = "-"
                } else {
                    output[i] = "+"
                }
            }
            return output
        }

        fun encrypt(value: String, seed: Int): String {
            val encryptedValue = aes.encrypt(value)
            val numbers = decodeListNumber(encryptedValue)
            val arrayOperators = generateArray(seed, numbers.size)
            val arraySequence = createSequence(seed, numbers.size)
            val transformedNumbers = arrayOfNulls<Int>(numbers.size)
            var output = ""
            for (i in numbers.indices) {
                when (arrayOperators[i]) {
                    "+" -> transformedNumbers[i] = numbers[i].toString().toInt() - arraySequence[i]!!
                    "-" -> transformedNumbers[i] = numbers[i].toString().toInt() + arraySequence[i]!!
                }
            }
            for (i in numbers.indices) {
                output = String.format("%s%s%s", output, transformedNumbers[i], ",")
            }
            output = String.format("%s%s", output, transformedNumbers[transformedNumbers.size - 1])
            return output
        }

        fun decrypt(value: String, seed: Int): String {
            val splited = value.split(",").toTypedArray()
            var length = splited.size
            val arrayOperators = generateArray(seed, length)
            val arraySequence = createSequence(seed, length)
            val transformedNumbers = arrayOfNulls<Int>(length)
            var item = 0
            var aux = ""
            for (i in 0 until length) {
                item = splited[i].toInt()
                when (arrayOperators[i]) {
                    "+" -> transformedNumbers[i] = item + arraySequence[i]!!
                    "-" -> transformedNumbers[i] = item - arraySequence[i]!!
                }
            }
            length = transformedNumbers.size
            for (i in 0 until length) {
                aux = String.format("%s%s%s", aux, transformedNumbers[i], ",")
            }
            aux = String.format("%s%s", aux, transformedNumbers[length - 1])
            return aes.decrypt(encodeListNumber(aux)!!)
        }

    }

}