package com.example.myapplication.utils;

import android.util.Base64;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

public class RSAUtils {


    /**
     * 公钥加密过程
     *
     * @param pubKey    公钥
     * @param plainText 明文数据
     * @return
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String pubKey, String plainText) throws Exception {
        System.out.println(pubKey);
        pubKey = pubKey.replaceAll("\\n", "").replace("-----BEGIN RSA PUBLIC KEY-----", "").replace("-----END RSA PUBLIC KEY-----", "");
        System.out.println(pubKey);

        byte[] buffer = Base64.decode(pubKey, Base64.DEFAULT);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(buffer), new BigInteger(plainText.getBytes(StandardCharsets.UTF_8)));
        RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(pubKeySpec);


        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");// 此处如果写成"RSA"加密出来的信息JAVA服务器无法解析
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] output = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        // 必须先encode成 byte[]，再转成encodeToString，否则服务器解密会失败 (未验证)
        byte[] encode = Base64.encode(output, Base64.DEFAULT);
        return Base64.encodeToString(encode, Base64.DEFAULT);
    }

}
