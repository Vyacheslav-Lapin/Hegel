package com.hegel.core;


import com.hegel.core.functions.ExceptionalFunction;
import lombok.val;

import java.security.MessageDigest;

public interface StringEncryptUtil {

    String ALGORITHM = "MD5";

    @SuppressWarnings("SpellCheckingInspection")
    MessageDigest ENCRYPTOR = ExceptionalFunction.getOrThrowUnchecked(
            MessageDigest::getInstance, ALGORITHM);

    static String encrypt(String s) {

        ENCRYPTOR.reset();

        byte[] bs = ENCRYPTOR.digest(s.getBytes());

        val stringBuilder = new StringBuilder();

        //hex encode the digest
        for (byte b : bs) {
            String hexVal = Integer.toHexString(0xFF & b);
            if (hexVal.length() == 1)
                stringBuilder.append("0");
            stringBuilder.append(hexVal);
        }

        return stringBuilder.toString();
    }
}
