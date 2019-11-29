package hk.edu.polyu.comp2411.assignment.extension

import org.springframework.security.crypto.bcrypt.BCrypt
import java.security.MessageDigest

fun String.sha512() = hashString("SHA-512")

fun String.sha256() = hashString("SHA-256")

fun String.sha1() = hashString("SHA-1")

fun String.bcrypt(salt: String = BCrypt.gensalt()) = BCrypt.hashpw(this, salt)

fun String.bcryptCheck(targetKey: String) = BCrypt.checkpw(targetKey, this)

val HEX_CHARS = "0123456789ABCDEF"

fun String.hashString(type: String): String =
    MessageDigest
        .getInstance(type)
        .digest(toByteArray()).let { it ->
            val result = StringBuilder(it.size * 2)

            it.map { it2 -> it2.toInt() }.forEach { i ->
                result.append(HEX_CHARS[i shr 4 and 0x0f]).append(HEX_CHARS[i and 0x0f])
            }

            return result.toString()
        }