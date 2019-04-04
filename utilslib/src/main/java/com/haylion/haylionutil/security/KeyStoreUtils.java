package com.haylion.haylionutil.security;

import android.content.Context;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;

/**
 * Author:wangjianming
 * Time:2018/11/19
 * Description:keyStore加密工具类
 */
public class KeyStoreUtils {
    static KeyStoreUtils encryUtilsInstance;
    KeyStore keyStore;

    public static KeyStoreUtils getInstance() {
        synchronized (KeyStoreUtils.class) {
            if (null == encryUtilsInstance) {
                encryUtilsInstance = new KeyStoreUtils();
            }
        }
        return encryUtilsInstance;
    }

    public KeyStoreUtils() {

    }

    private void initKeyStore(String alias, Context context) {
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            createNewKeys(alias, context);
        }
    }

    private void createNewKeys(String alias, Context context) {
        if (!"".equals(alias)) {
            try {
                if (!keyStore.containsAlias(alias)) {
                    Calendar start = Calendar.getInstance();
                    Calendar end = Calendar.getInstance();
                    end.add(Calendar.YEAR, 1);
                    KeyPairGeneratorSpec spec = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        spec = new KeyPairGeneratorSpec.Builder(context)
                                .setAlias(alias)
                                .setSubject(new X500Principal("CN=Sample Name, O=Android Authority"))
                                .setSerialNumber(BigInteger.ONE)
                                .setStartDate(start.getTime())
                                .setEndDate(end.getTime())
                                .build();
                    }
                    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                        generator.initialize(spec);
                    }

                    KeyPair keyPair = generator.generateKeyPair();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    /**
     * 加密方法
     *
     * @param needEncryptWord 　需要加密的字符串
     * @return
     */
    public String encryptString(String needEncryptWord, Context context) {
        String alias = context.getPackageName();
        if (!"".equals(alias) && !"".equals(needEncryptWord)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                initKeyStore(alias, context);
            }
            String encryptStr = "";
            byte[] vals = null;
            try {
                KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
                if (needEncryptWord.isEmpty()) {
                    return encryptStr;
                }

                Cipher inCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                inCipher.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                CipherOutputStream cipherOutputStream = new CipherOutputStream(
                        outputStream, inCipher);
                cipherOutputStream.write(needEncryptWord.getBytes("UTF-8"));
                cipherOutputStream.close();

                vals = outputStream.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
            String str = Base64.encodeToString(vals, Base64.DEFAULT);
            return str == null ? "" : str;
        }
        return "";
    }

    /**
     * 解密方法
     *
     * @param needDecryptString 需要解密的字符串
     * @param context
     * @return
     */
    public String decryptString(String needDecryptString, Context context) {
        String alias = context.getPackageName();
        if (!"".equals(alias) && !"".equals(needDecryptString)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                initKeyStore(alias, context);
            }
            String decryptStr = "";
            try {
                KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
                Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
                CipherInputStream cipherInputStream = new CipherInputStream(
                        new ByteArrayInputStream(Base64.decode(needDecryptString, Base64.DEFAULT)), output);
                ArrayList<Byte> values = new ArrayList<>();
                int nextByte;
                while ((nextByte = cipherInputStream.read()) != -1) {
                    values.add((byte) nextByte);
                }

                byte[] bytes = new byte[values.size()];
                for (int i = 0; i < bytes.length; i++) {
                    bytes[i] = values.get(i).byteValue();
                }

                decryptStr = new String(bytes, 0, bytes.length, "UTF-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return decryptStr;
        }
        return "";
    }
}