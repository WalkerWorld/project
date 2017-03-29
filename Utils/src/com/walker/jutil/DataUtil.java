/**   
* @Title: DataUtils.java
* @Package com.walker.utils
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2017年1月26日 下午11:29:25
* @version V1.0   
*/
package com.walker.jutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;


/** @ClassName: DataUtils
 *	数据信息处理工具类
 * @Description: TODO
 *	网络传输数据加密：数据实体以Base64编码
 *	数据库存储数据加密：数据信息以Base64进行编码，然后用以按照自定义算法排序加密
 * @author walker
 *
 * @date 2017年1月26日 下午11:29:25
 * 
 */
public class DataUtil {
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
			'E', 'F' };
	private DataUtil() {}
	static DataUtil dataUtils;
	/**单例获取数据加工类实体*/
	public static DataUtil getInstance(){
		if(dataUtils == null){
			dataUtils = new DataUtil();
		}
		return dataUtils;
	}
	/**
	 * Add by walker Date 2017年2月4日
	 * @Description: TODO
	 * 获取密码生成器 
	 *  @param pwd 用于获取密码种子
	 *  @param flag 0-获取加密器  其他－获取解密器
	 *  @return 返回密码生成器，异常时返回null
	 */
	public Cipher getCipherAES(String pwd, int flag) {
		try {
			// 创建Key构造器
			KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
			
			// 加密没关系，SecureRandom是生成安全随机数序列，password.getBytes()是种子，只要种子相同，序列就一样，所以解密只要有password就行
			 //防止linux下 随机生成key 
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );  
            secureRandom.setSeed(pwd.getBytes());  
			keyGenerator.init(128, secureRandom);
			// 根据用户密码生成秘钥
			SecretKey secretKey = keyGenerator.generateKey();
			// 返回基本编码格式的密钥，如果此密钥不支持编码，则返回null。
			byte[] enCodeFormat = secretKey.getEncoded();
			SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");// 转换为AES专用密钥
			// 创建密码器
			Cipher cipher = Cipher.getInstance("AES");
			if (flag == 0) {
				//初始化密码器为加密器
				cipher.init(Cipher.ENCRYPT_MODE, key);
			} else {
				//初始化密码器为解密器
				 cipher.init(Cipher.DECRYPT_MODE, key);
			}
			return cipher;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Add by walker Date 2017年2月4日
	 * @Description: TODO
	 *  加密字符串，获取密文字节数组
	 *  @param str
	 *  @param pwd
	 *  @return
	 */
	public byte[] encrypt(String str, String pwd){
		try {
			// 以utf－8方式编码字符串
			byte[] byteContent = str.getBytes("utf-8");
			// 获取加密器
			Cipher cipher = getCipherAES(pwd, 0);
			// 加密结果字节数组
			byte[] result = cipher.doFinal(byteContent);// 加密
			return result;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Add by walker Date 2017年2月4日
	 * @Description: TODO
	 *  加密字节数组，获取密文字节数组
	 *  @param str
	 *  @param pwd
	 *  @return
	 */
	public byte[] encrypt(byte[] bytes, String pwd){
		try {
			// 获取加密器
			Cipher cipher = getCipherAES(pwd, 0);
			// 加密结果字节数组
			byte[] result = cipher.doFinal(bytes);// 加密
			return result;
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * Add by walker Date 2017年2月4日
	 * @Description: TODO
	 *  解密密文
	 *  @param bytes 密文字节数组
	 *  @param pwd 解密密码
	 *  @return
	 */
	public byte[] decrypt(byte[] bytes, String pwd){
		   try {
			   Cipher cipher = getCipherAES(pwd, 1);
	            byte[] result = cipher.doFinal(bytes);  
	            return result; // 明文   
	        } catch (IllegalBlockSizeException e) {
	            e.printStackTrace();
	        } catch (BadPaddingException e) {
	            e.printStackTrace();
	        }
		return null;
	}


	/**
	 * 生成Md5
	 * 
	 * @param s
	 *            字符串
	 * @return 字符串的MD5值
	 */
	public static String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * @Description: TODO
	 *	字符串转 哈希值
	 *  @param b
	 *  @return
	 */
	public static String toHexString(byte[] b) {
		// String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	/**
	 * Add by walker Date 2017年3月27日
	 * @Description: TODO
	 * 字符串转base64编码 
	 *  @param str 被编码字符串
	 *  @return 返回编码后的字符串
	 */
	public String encodeBase64(String str){
		return Base64.encodeToString(str.getBytes(), Base64.NO_WRAP);
	}
	/**
	 * Add by walker Date 2017年3月27日
	 * @Description: TODO
	 * 字节数组转base64编码 
	 *  @param bytes 被编码字节数组
	 *  @return 返回编码后的字符串
	 */
	public String encodeBase64(byte[] bytes){
		return Base64.encodeToString(bytes, Base64.NO_WRAP);
	}
	
	/**
	 * Add by walker Date 2017年3月27日
	 * @Description: TODO
	 *  将Base64字符串解码
	 *  @param baseStr Base64编码字符串
	 *  @return 返回解码结果的字节数组
	 */
	public byte[] decodeBase64(String baseStr){
		return Base64.decode(baseStr, Base64.NO_WRAP);
	}
	
	public void getMd5ByFile(String fileName){
		 try {
	            File file = new File(fileName);
	            FileInputStream fis = new FileInputStream(file);
	            MessageDigest md = MessageDigest.getInstance("MD5");
	            byte[] buffer = new byte[1024];
	            int length = -1;
	            while ((length = fis.read(buffer, 0, 1024)) != -1) {
	                md.update(buffer, 0, length);
	            }
	            BigInteger bigInt = new BigInteger(1, md.digest());
	            System.out.println("文件md5值：" + bigInt.toString(16));
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (NoSuchAlgorithmException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
}
