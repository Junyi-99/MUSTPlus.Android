package com.example.myapplication.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSAUtils {
    private static final int SEQUENCE_TAG = 0x30;
    private static final int BIT_STRING_TAG = 0x03;
    private static final byte[] NO_UNUSED_BITS = new byte[]{0x00};
    private static final byte[] RSA_ALGORITHM_IDENTIFIER_SEQUENCE =
            {(byte) 0x30, (byte) 0x0d,
                    (byte) 0x06, (byte) 0x09, (byte) 0x2a, (byte) 0x86, (byte) 0x48, (byte) 0x86, (byte) 0xf7, (byte) 0x0d, (byte) 0x01, (byte) 0x01, (byte) 0x01,
                    (byte) 0x05, (byte) 0x00};
    public static String PUBLIC_KEY = "MIIBCgKCAQEAr7vcGPzbCVDGUdhf7sh7jpsEG7FTbSkPpkuWqjCyB8CMb0PjiTVxUuxOco4zC5fDt/4kE26JZsyCfVw7quEZhEbh2aGcG5kV2j4klDj9UHVYA9OQlCuEXXpYeF8OOWX96HlvpsRYVv7kJZ04VjrVXRT74EVpCoPlJEVR/vf0VkI6Szdfedb2zZlL6xms0Vc8AqbyCy9l/E5W7PhJiwaWPKuWLKFhoNwqbOx9X3tBEWRudfAOxomSBBblAe2Td1V1qYwsgcfBEkD+8BvtvRNFnqoB05dZZgHsRxFm2hi9K7jkQQQj0ZSmISErMco1P+x59LZbKnIXhcybp/DfZl34xQIDAQAB";

    public static RSAPublicKey decodePKCS1PublicKey(byte[] pkcs1PublicKeyEncoding)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] subjectPublicKeyInfo2 = createSubjectPublicKeyInfoEncoding(pkcs1PublicKeyEncoding);
        KeyFactory rsaKeyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKey generatePublic = (RSAPublicKey) rsaKeyFactory.generatePublic(new X509EncodedKeySpec(subjectPublicKeyInfo2));
        return generatePublic;
    }

    public static byte[] createSubjectPublicKeyInfoEncoding(byte[] pkcs1PublicKeyEncoding) {
        byte[] subjectPublicKeyBitString = createDEREncoding(BIT_STRING_TAG, concat(NO_UNUSED_BITS, pkcs1PublicKeyEncoding));
        byte[] subjectPublicKeyInfoValue = concat(RSA_ALGORITHM_IDENTIFIER_SEQUENCE, subjectPublicKeyBitString);
        byte[] subjectPublicKeyInfoSequence = createDEREncoding(SEQUENCE_TAG, subjectPublicKeyInfoValue);

        return subjectPublicKeyInfoSequence;
    }

    private static byte[] concat(byte[]... bas) {
        int len = 0;
        for (int i = 0; i < bas.length; i++) {
            len += bas[i].length;
        }

        byte[] buf = new byte[len];
        int off = 0;
        for (int i = 0; i < bas.length; i++) {
            System.arraycopy(bas[i], 0, buf, off, bas[i].length);
            off += bas[i].length;
        }

        return buf;
    }

    private static byte[] createDEREncoding(int tag, byte[] value) {
        if (tag < 0 || tag >= 0xFF) {
            throw new IllegalArgumentException("Currently only single byte tags supported");
        }

        byte[] lengthEncoding = createDERLengthEncoding(value.length);

        int size = 1 + lengthEncoding.length + value.length;
        byte[] derEncodingBuf = new byte[size];

        int off = 0;
        derEncodingBuf[off++] = (byte) tag;
        System.arraycopy(lengthEncoding, 0, derEncodingBuf, off, lengthEncoding.length);
        off += lengthEncoding.length;
        System.arraycopy(value, 0, derEncodingBuf, off, value.length);

        return derEncodingBuf;
    }

    private static byte[] createDERLengthEncoding(int size) {
        if (size <= 0x7F) {
            // single byte length encoding
            return new byte[]{(byte) size};
        } else if (size <= 0xFF) {
            // double byte length encoding
            return new byte[]{(byte) 0x81, (byte) size};
        } else if (size <= 0xFFFF) {
            // triple byte length encoding
            return new byte[]{(byte) 0x82, (byte) (size >> Byte.SIZE), (byte) size};
        }
        throw new IllegalArgumentException("size too large, only up to 64KiB length encoding supported: " + size);
    }

    public static String encrypt(String txt) throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        byte[] pkcs1PublicKeyEncoding = Base64.decode(PUBLIC_KEY, Base64.DEFAULT);

        RSAPublicKey generatePublic = decodePKCS1PublicKey(pkcs1PublicKeyEncoding);
        System.out.println(generatePublic);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, generatePublic);
        byte[] content = txt.getBytes("UTF-8");
        byte[] output = cipher.doFinal(content);
        return Base64.encodeToString(output, Base64.DEFAULT);
    }
}
