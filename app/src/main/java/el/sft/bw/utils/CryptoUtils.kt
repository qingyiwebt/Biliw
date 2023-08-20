package el.sft.bw.utils

import java.math.BigInteger
import java.security.MessageDigest

object CryptoUtils {
    private val wbiMixKeyTable = intArrayOf(
        46, 47, 18, 2, 53,
        8, 23, 32, 15, 50,
        10, 31, 58, 3, 45,
        35, 27, 43, 5, 49,
        33, 9, 42, 19, 29,
        28, 14, 39, 12, 38,
        41, 13
    )

    fun getWbiKey(imgKey: String, subKey: String): String {
        val s = imgKey + subKey
        return wbiMixKeyTable
            .map { s[it] }
            .joinToString("")
    }

    /**
     * from https://stackoverflow.com/questions/64171624/how-to-generate-an-md5-hash-in-kotlin
     */
    fun md5(input: String): String {
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
    }
}