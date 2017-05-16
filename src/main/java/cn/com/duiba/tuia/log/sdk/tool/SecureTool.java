package cn.com.duiba.tuia.log.sdk.tool;

import org.apache.commons.lang.StringUtils;

/**
 * The Class SecureTool.
 */
public class SecureTool {

    /**
     * Decrypt consumer cookie.
     *
     * @param data the data
     * @return the string
     */
    public static String decryptConsumerCookie(String data,String managerAccountEncryptKey) {

        if(StringUtils.isBlank(managerAccountEncryptKey)){
            return null;
        }

        return BlowfishUtils.decryptBlowfish(data, managerAccountEncryptKey);
    }
}
