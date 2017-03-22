package cn.com.duiba.tuia.log.sdk.tool;

/**
 * The Class SecureTool.
 */
public class SecureTool
{
	
	/** The login key. */
	private static String loginKey;
	
	/** The admin key. */
	private static String adminKey;
	
	/** The manager account encrypt key. */
	private static String managerAccountEncryptKey;
	
	/** The app secret key. */
	private static String appSecretKey;
	
	/**
	 * The Constructor.
	 */
	private SecureTool(){
	    
	}
	
	/**
	 * Decrypt developer cookie.
	 *
	 * @param data the data
	 * @return the string
	 */
	public static String decryptDeveloperCookie(String data)
	{
		return BlowfishUtils.decryptBlowfish(data, loginKey);
	}
	
	/**
	 * Decrypt admin cookie.
	 *
	 * @param data the data
	 * @return the string
	 */
	public static String decryptAdminCookie(String data)
	{
		return BlowfishUtils.decryptBlowfish(data, adminKey);
	}
	
	/**
	 * Decrypt consumer cookie.
	 *
	 * @param data the data
	 * @return the string
	 */
	public static String decryptConsumerCookie(String data)
	{
		return BlowfishUtils.decryptBlowfish(data, managerAccountEncryptKey);
	}
	
	/**
	 * Encrypt app secret.
	 *
	 * @param data the data
	 * @return the string
	 */
	public static String encryptAppSecret(String data)
	{
		return BlowfishUtils.encryptBlowfish(data, appSecretKey);
	}
	
	/**
	 * Decrypt app secret code.
	 *
	 * @param data the data
	 * @return the string
	 */
	public static String decryptAppSecretCode(String data)
	{
		return BlowfishUtils.decryptBlowfish(data, appSecretKey);
	}
	
	/**
	 * Gets the login key.
	 *
	 * @return the login key
	 */
	public static String getLoginKey()
	{
		return loginKey;
	}
	
	/**
	 * Sets the login key.
	 *
	 * @param loginKey the login key
	 */
	public static void setLoginKey(String loginKey)
	{
		SecureTool.loginKey = loginKey;
	}
	
	/**
	 * Gets the manager account encrypt key.
	 *
	 * @return the manager account encrypt key
	 */
	public static String getManagerAccountEncryptKey()
	{
		return managerAccountEncryptKey;
	}
	
	/**
	 * Sets the manager account encrypt key.
	 *
	 * @param managerAccountEncryptKey the manager account encrypt key
	 */
	public static void setManagerAccountEncryptKey(String managerAccountEncryptKey)
	{
		SecureTool.managerAccountEncryptKey = managerAccountEncryptKey;
	}
	
}
