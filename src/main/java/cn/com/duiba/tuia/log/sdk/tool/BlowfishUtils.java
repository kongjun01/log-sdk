package cn.com.duiba.tuia.log.sdk.tool;


import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.HashMap;

/**
 * Blowfish加密解密的方法.
 *
 * @link http://www.schneier.com/blowfish.html
 * @author zhoufang
 */
public class BlowfishUtils {

    /** The Constant CIPHER_NAME. */
    private static final String                                      CIPHER_NAME   = "Blowfish/CFB8/NoPadding";

    /** The Constant KEY_SPEC_NAME. */
    private static final String                                      KEY_SPEC_NAME = "Blowfish";

    /** The Constant pool. */
    private static final ThreadLocal<HashMap<String, BlowfishUtils>> pool          = new ThreadLocal<>();

    /** The en cipher. */
    private Cipher                                                   enCipher;

    /** The de cipher. */
    private Cipher                                                   deCipher;

    /** The key. */
    private String                                                   key;

    /**
     * The Constructor.
     *
     * @param key the key
     */
    private BlowfishUtils(String key) {
        try {
            this.key = key;
            String iv = StringUtils.substring(DigestUtils.md5Hex(key), 0, 8);
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), KEY_SPEC_NAME);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv.getBytes());
            enCipher = Cipher.getInstance(CIPHER_NAME);
            deCipher = Cipher.getInstance(CIPHER_NAME);
            enCipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            deCipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Encrypt blowfish.
     *
     * @param s the s
     * @param key the key
     * @return the string
     */
    public static String encryptBlowfish(String s, String key) {
        return getInstance(key).encrypt(s);
    }

    /**
     * Decrypt blowfish.
     *
     * @param s the s
     * @param key the key
     * @return the string
     */
    public static String decryptBlowfish(String s, String key) {
        return getInstance(key).decrypt(s);
    }

    /**
     * Encrypt blowfish.
     *
     * @param s the s
     * @return the string
     */
    public static String encryptBlowfish(String s) {
        return encryptBlowfish(s, "abc");
    }

    /**
     * Decrypt blowfish.
     *
     * @param s the s
     * @return the string
     */
    public static String decryptBlowfish(String s) {
        return decryptBlowfish(s, "abc");
    }

    /**
     * Gets the instance.
     *
     * @param key the key
     * @return the instance
     */
    private static BlowfishUtils getInstance(String key) {
        HashMap<String, BlowfishUtils> keyMap = pool.get();
        if (keyMap == null || keyMap.isEmpty()) {
            keyMap = new HashMap<>();
            pool.set(keyMap);
        }
        BlowfishUtils instance = keyMap.get(key);
        if (instance == null || !StringUtils.equals(instance.key, key)) {
            instance = new BlowfishUtils(key);
            keyMap.put(key, instance);
        }
        return instance;
    }

    /**
     * 加密.
     *
     * @param s the s
     * @return the string
     */
    private String encrypt(String s) {
        String result = null;
        if (StringUtils.isNotBlank(s)) {
            try {
                byte[] encrypted = enCipher.doFinal(s.getBytes());
                result = Base58.encode(encrypted);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * 解密.
     *
     * @param s the s
     * @return the string
     */
    private String decrypt(String s) {
        String result = null;
        if (StringUtils.isNotBlank(s)) {
            try {
                byte[] decrypted = Base58.decode(s);
                result = new String(deCipher.doFinal(decrypted));
            } catch (Exception e) {
                resetInstance();
                throw new RuntimeException(e);
            }
        }
        return result;
    }

    /**
     * Reset instance.
     */
    private void resetInstance() {
        pool.set(null);
    }
}
