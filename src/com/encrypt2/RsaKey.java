package com.encrypt2;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class RsaKey {

	private final static String MODULUS_START_NODE = "<Modulus>";
	private final static String MODULUS_END_NODE = "</Modulus>";
	private final static String EXPONENT_START_NODE = "<Exponent>";
	private final static String EXPONENT_END_NODE = "</Exponent>";
	private final static String RSAKEYVALUE_START_NODE = "<RSAKeyValue>";
	private final static String RSAKEYVALUE_END_NODE = "</RSAKeyValue>";

	public static void main(String[] args) throws Exception {
		// 生成公私钥对
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		PublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		PrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

		// 将公钥和模进行Base64编码
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec publicSpec = keyFactory.getKeySpec(publicKey,
				RSAPublicKeySpec.class);
		BigInteger modulus = publicSpec.getModulus();
		BigInteger exponent = publicSpec.getPublicExponent();
		byte[] ary_m = modulus.toByteArray();// 注意：对公钥和模进行Base64编码时，不是对BigInteger对应的字符串编码，而是对其内部
												// 的字节数组进行编码
		byte[] ary_e = exponent.toByteArray();
		String str_m;
		String str_e;
		if (ary_m[0] == 0 && ary_m.length == 129)// 判断数组首元素是否为0，若是，则将其删除，保证模的位数是128
		{
			byte[] temp = new byte[ary_m.length - 1];
			for (int i = 1; i < ary_m.length; i++) {
				temp[i - 1] = ary_m[i];
			}
			str_m = (new BASE64Encoder()).encodeBuffer(temp);
		} else {
			str_m = (new BASE64Encoder()).encodeBuffer(ary_m);
		}

		str_e = (new BASE64Encoder()).encodeBuffer(ary_e);
		System.out.println("公钥为：" + str_e);
		System.out.println("模为：" + str_m);
		System.err.println(encodePublicKeyToXml((RSAPublicKey) (publicKey)));
		System.out.println("运行.NET程序，用所提供的公钥和模进行加密，然后将加密结果输入本程序进行解密：");
		Scanner sc = new Scanner(System.in);
		String str_en = "";
		String st = "";
		while (!(st = sc.nextLine()).equals("")) {
			str_en += st;
		}
		byte[] ary_en = (new BASE64Decoder()).decodeBuffer(str_en);
		// 解密
		// 注意Cipher初始化时的参数“RSA/ECB/PKCS1Padding”,代表和.NET用相同的填充算法，如果是标准RSA加密，则参数为“RSA”
		// Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		// cipher.init(Cipher.DECRYPT_MODE, privateKey);
		// byte[] deBytes = cipher.doFinal(ary_en);
		// String s = new String(deBytes);
		String s = Dencrypt(encodePrivateKeyToXml(privateKey), ary_en);
		System.out.println("解密结果为：" + s);
	}

	public static String encodePublicKeyToXml(PublicKey key)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		if (!RSAPublicKey.class.isInstance(key)) {
			return null;
		}
		RSAPublicKey pubKey = (RSAPublicKey) key;

		// 将公钥和模进行Base64编码
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPublicKeySpec publicSpec = keyFactory.getKeySpec(pubKey,
				RSAPublicKeySpec.class);
		BigInteger modulus = publicSpec.getModulus();
		BigInteger exponent = publicSpec.getPublicExponent();
		byte[] ary_m = modulus.toByteArray();// 注意：对公钥和模进行Base64编码时，不是对BigInteger对应的字符串编码，而是对其内部
												// 的字节数组进行编码
		byte[] ary_e = exponent.toByteArray();
		String str_m;
		String str_e;
		if (ary_m[0] == 0 && ary_m.length == 129)// 判断数组首元素是否为0，若是，则将其删除，保证模的位数是128
		{
			byte[] temp = new byte[ary_m.length - 1];
			for (int i = 1; i < ary_m.length; i++) {
				temp[i - 1] = ary_m[i];
			}
			str_m = (new BASE64Encoder()).encodeBuffer(temp);
		} else {
			str_m = (new BASE64Encoder()).encodeBuffer(ary_m);
		}

		str_e = (new BASE64Encoder()).encodeBuffer(ary_e);
		System.out.println("公钥为：" + str_e);
		System.out.println("模为：" + str_m);

		StringBuilder sb = new StringBuilder();

		sb.append(RSAKEYVALUE_START_NODE);
		sb.append(MODULUS_START_NODE).append(str_m).append(MODULUS_END_NODE);
		sb.append(EXPONENT_START_NODE).append(str_e).append(EXPONENT_END_NODE);
		sb.append(RSAKEYVALUE_END_NODE);
		return sb.toString();
	}

	/**
	 * 公钥加密
	 * 
	 * @param originalString
	 * @param publicKey
	 * @param platform
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 * @throws UnsupportedEncodingException
	 */
	public static String encrypt(String originalString, RSAPublicKey publicKey,
			String platform) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			IllegalBlockSizeException, BadPaddingException,
			UnsupportedEncodingException {

		byte[] ary_en = originalString.getBytes("UTF-8");
		// 解密
		// 注意Cipher初始化时的参数“RSA/ECB/PKCS1Padding”,代表和.NET用相同的填充算法，如果是标准RSA加密，则参数为“RSA”

		Cipher cipher = null;
		if (".net".equals(platform)) {
			cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		} else {
			cipher = Cipher.getInstance("RSA");

		}
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] deBytes = cipher.doFinal(ary_en);
		String result = new BASE64Encoder().encode(deBytes);
		System.out.println("加密结果为：" + result);

		return result;
	}

	/**
	 * 公钥加密
	 * 
	 * @param module
	 * @param exponent
	 * @return
	 */
	public static byte[] encrypt(String module, String exponent) {
		try {
			byte[] modulusBytes = new BASE64Decoder().decodeBuffer(module);
			byte[] exponentBytes = new BASE64Decoder().decodeBuffer(exponent);
			BigInteger integreModulu = new BigInteger(1, modulusBytes);
			BigInteger integreExponent = new BigInteger(1, exponentBytes);

			RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(integreModulu,
					integreExponent);
			KeyFactory fact = KeyFactory.getInstance("RSA/ECB/PKCS1Padding");
			PublicKey pubKey = fact.generatePublic(rsaPubKey);

			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);

			byte[] cipherData = cipher.doFinal(new String("chenhailong")
					.getBytes());
			return cipherData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * 私钥解密
	 * 
	 * @param module
	 * @param exponent
	 * @param encrypted
	 * @return
	 */
	public static String Dencrypt(String keyXml, byte[] encrypted) {
		try {

			String module = keyXml.substring(keyXml.indexOf(MODULUS_START_NODE)
					+ MODULUS_START_NODE.length(),
					keyXml.indexOf(MODULUS_END_NODE));
			String exponent = keyXml.substring(
					keyXml.indexOf(EXPONENT_START_NODE)
							+ EXPONENT_START_NODE.length(),
					keyXml.indexOf(EXPONENT_END_NODE));

			byte[] expBytes = new BASE64Decoder().decodeBuffer(exponent);
			byte[] modBytes = new BASE64Decoder().decodeBuffer(module);

			BigInteger integerModules = new BigInteger(1, modBytes);
			BigInteger integerExponent = new BigInteger(1, expBytes);

			KeyFactory factory = KeyFactory.getInstance("RSA");
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

			RSAPrivateKeySpec privSpec = new RSAPrivateKeySpec(integerModules,
					integerExponent);
			PrivateKey privKey = factory.generatePrivate(privSpec);
			cipher.init(Cipher.DECRYPT_MODE, privKey);
			byte[] decrypted = cipher.doFinal(encrypted);
			return new String(decrypted, "UTF-8");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String encodePrivateKeyToXml(PrivateKey key)
			throws NoSuchAlgorithmException, InvalidKeySpecException {
		if (!RSAPrivateKey.class.isInstance(key)) {
			return null;
		}
		RSAPrivateKey privateKey = (RSAPrivateKey) key;

		// 将公钥和模进行Base64编码
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		RSAPrivateKeySpec privateSpec = keyFactory.getKeySpec(privateKey,
				RSAPrivateKeySpec.class);
		BigInteger modulus = privateSpec.getModulus();
		BigInteger exponent = privateSpec.getPrivateExponent();
		byte[] ary_m = modulus.toByteArray();// 注意：对公钥和模进行Base64编码时，不是对BigInteger对应的字符串编码，而是对其内部
												// 的字节数组进行编码
		byte[] ary_e = exponent.toByteArray();
		String str_m;
		String str_e;
		if (ary_m[0] == 0 && ary_m.length == 129)// 判断数组首元素是否为0，若是，则将其删除，保证模的位数是128
		{
			byte[] temp = new byte[ary_m.length - 1];
			for (int i = 1; i < ary_m.length; i++) {
				temp[i - 1] = ary_m[i];
			}
			str_m = (new BASE64Encoder()).encodeBuffer(temp);
		} else {
			str_m = (new BASE64Encoder()).encodeBuffer(ary_m);
		}

		str_e = (new BASE64Encoder()).encodeBuffer(ary_e);
		System.out.println("私钥为：" + str_e);
		System.out.println("模为：" + str_m);

		StringBuilder sb = new StringBuilder();

		sb.append(RSAKEYVALUE_START_NODE);
		sb.append(MODULUS_START_NODE).append(str_m).append(MODULUS_END_NODE);
		sb.append(EXPONENT_START_NODE).append(str_e).append(EXPONENT_END_NODE);
		sb.append(RSAKEYVALUE_END_NODE);
		return sb.toString();
	}

	/*
	 * .net code fragment. using System; using System.Text; using
	 * System.Collections.Generic; using System.Linq; using
	 * Microsoft.VisualStudio.TestTools.UnitTesting; using
	 * System.Security.Cryptography;
	 * 
	 * 
	 * 
	 * namespace TestProject1 { class Class1 {
	 * 
	 * 
	 * static void Main(string[] args) { try { RSACryptoServiceProvider rsa =
	 * new RSACryptoServiceProvider(); RSAParameters para = new RSAParameters();
	 * //加密 Console.WriteLine("请输入公钥"); string publicKey = Console.ReadLine();
	 * Console.WriteLine("请输入模:"); string modulus = Console.ReadLine(); while
	 * (true) { string s = Console.ReadLine(); if (s == "") { break; } else {
	 * modulus += s; } } Console.WriteLine("请输入明文："); string m =
	 * Console.ReadLine(); para.Exponent = Convert.FromBase64String(publicKey);
	 * para.Modulus = Convert.FromBase64String(modulus);
	 * rsa.ImportParameters(para); byte[] enBytes =
	 * rsa.Encrypt(UTF8Encoding.UTF8.GetBytes(m), false);
	 * Console.WriteLine("密文为：" + Convert.ToBase64String(enBytes));
	 * Console.ReadLine(); } catch (Exception ex) {
	 * Console.WriteLine(ex.Message); Console.ReadLine(); } } } }
	 */

}
