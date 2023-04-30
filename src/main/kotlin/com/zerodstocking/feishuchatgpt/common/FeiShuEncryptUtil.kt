package com.zerodstocking.feishuchatgpt.common

import java.security.MessageDigest
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import org.springframework.stereotype.Component

/**
 * @author JayCHou <a href="calljaychou@qq.com">Email</a>
 * @version 2.0.0
 * @date 2023/4/24 11:04
 */
@Component
class FeiShuEncryptUtil(val feiShuEnvironment: FeiShuEnvironment) {


    @Throws(Exception::class)
    fun decrypt(base64: String?): String? {
        val decode = Base64.getDecoder().decode(base64)
        val cipher = Cipher.getInstance("AES/CBC/NOPADDING")
        val iv = ByteArray(16)
        System.arraycopy(decode, 0, iv, 0, 16)
        val data = ByteArray(decode.size - 16)
        System.arraycopy(decode, 16, data, 0, data.size)
        cipher.init(
            Cipher.DECRYPT_MODE,
            SecretKeySpec(
                MessageDigest.getInstance("SHA-256").digest(feiShuEnvironment.encryptKey.toByteArray()),
                "AES"
            ),
            IvParameterSpec(iv)
        )
        var r = cipher.doFinal(data)
        if (r.isNotEmpty()) {
            var p = r.size - 1
            while (p >= 0 && r[p] <= 16) {
                p--
            }
            if (p != r.size - 1) {
                val rr = ByteArray(p + 1)
                System.arraycopy(r, 0, rr, 0, p + 1)
                r = rr
            }
        }
        return String(r)
    }
}