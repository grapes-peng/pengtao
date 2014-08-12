package com.encrypt1;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

/**
 * @author cnchenhl Jul 8, 2011
 */
public class RSAMain {

	private static String exponentString = "AQAB";// 公钥Exponent

	private static String module = "AMG9EPNHtpOHzmHxoPt0L/Thy/Jz/eSnG5Ap1lArC7cLl3Hvx38z2D92Tq5LU/l4SKtVPzA7j9GGHw33sQqknHXQYFd0lFK9NI2aNeQOZwZ0mbXVXw3hOJl8xwggvK9eH5A7Yz5yyCf1nRap6n5YPW6McWxlnA9XdzzZtT7v1ckt";

	// 私钥Exponent
	private static String delement = "BpJSM95X3BM8NehLTJw5UDYkg9nZX9snPTGeGmQLZq8W+P9oJfFtQWHS82iMtaaV2HHwzcMHctT40rF3KORChlFLjvl1xlikrM0bRdhAC72WwyM3ROXUDcDUnDRPSj0LfHMv8xfybG2NEiabDlM7QaJonttWAwdSP5caiymbQaU=";

	private static String encryptString = "AHwoz6EWumorCn5FkPAnv5y91LLCLKnE6rbvLagYEb6gK/iCmlVu7nx1VTJLBPAA/O0lugfT9ps0/vTatSXBpVw6QLN1hF+hw2E0slz8viSHwT6/Np8jYFPZcCtbzbivqScC83z1NgU1D4VIT5pF5KpNz1jQM7jWsqaIkPaYNaua";

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {

//		System.out.println(Charset.defaultCharset());
		// generateRSAKeyPair();
//		if (1 == 1)
//			return;
//		byte[] en = encrypt();
//		 System.out.println(Base64.encode(en));

		byte[] enTest = null;
		byte[] removedTest = null;
		try {
			enTest = Base64.decode(encryptString);
			removedTest = removeMSZero(enTest);
		} catch (Base64DecodingException e) {
			e.printStackTrace();
		}
//		System.out.println("enTest" + enTest.length);
		System.out.println("removedTest" + enTest.length);

//		 System.out.println(en.length);
//		 System.out.println(new String(Dencrypt(en)));
		System.out.println(new String(Dencrypt(removedTest)));
	}

	public static byte[] encrypt() {
		try {
			byte[] modulusBytes = Base64.decode(module);
			byte[] exponentBytes = Base64.decode(exponentString);
			BigInteger modulus = new BigInteger(1, modulusBytes);
			BigInteger exponent = new BigInteger(1, exponentBytes);

			RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(modulus, exponent);
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

	public static byte[] Dencrypt(byte[] encrypted) {
		try {
			byte[] expBytes = Base64.decode(delement);
			byte[] modBytes = Base64.decode(module);

			BigInteger modules = new BigInteger(1, modBytes);
			BigInteger exponent = new BigInteger(1, expBytes);

			KeyFactory factory = KeyFactory.getInstance("RSA");
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");

			RSAPrivateKeySpec privSpec = new RSAPrivateKeySpec(modules,
					exponent);
			PrivateKey privKey = factory.generatePrivate(privSpec);
			cipher.init(Cipher.DECRYPT_MODE, privKey);
			byte[] decrypted = cipher.doFinal(encrypted);
			return decrypted;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static KeyPair generateRSAKeyPair() throws InterruptedException {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(1024);
			KeyPair keyPair = kpg.genKeyPair();
			RSAPrivateCrtKey privateKey = null;
			RSAPublicKey publicKey = null;
			publicKey = (RSAPublicKey) keyPair.getPublic();
			privateKey = (RSAPrivateCrtKey) keyPair.getPrivate();

			module = Base64.encode(privateKey.getModulus().toByteArray());
			// exponentString = Base64.encode(privateKey.getPrivateExponent()
			// .toByteArray());
			delement = Base64.encode(privateKey.getPrivateExponent()
					.toByteArray());

			Thread.sleep(500);
			System.err.println(" =========private base64 encode module:");
			System.out.println(Base64.encode(privateKey.getModulus()
					.toByteArray()));

			Thread.sleep(500);
			System.err.println(" =========private base64 encode expon:");
			System.out.println(Base64.encode(privateKey.getPrivateExponent()
					.toByteArray()));
			Thread.sleep(500);

			System.err.println("**********public base64 encode module:");
			System.out.println(Base64.encode(publicKey.getModulus()
					.toByteArray()));

			Thread.sleep(500);
			System.err.println("**********public base64 encode expon:");
			System.out.println(Base64.encode(publicKey.getPublicExponent()
					.toByteArray()));
			Thread.sleep(500);
			System.err.println("XML:");
			System.out.println(encodePublicKeyToXml(publicKey));

			return keyPair;
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

	/*
	 * java端公钥转换成C#公钥
	 */
	public static String encodePublicKeyToXml(PublicKey key) {
		if (!RSAPublicKey.class.isInstance(key)) {
			return null;
		}
		RSAPublicKey pubKey = (RSAPublicKey) key;
		StringBuilder sb = new StringBuilder();

		sb.append("<RSAKeyValue>");
		sb.append("<Modulus>")
				.append(Base64.encode(pubKey.getModulus().toByteArray()))
				.append("</Modulus>");
		sb.append("<Exponent>")
				.append(Base64.encode(pubKey.getPublicExponent().toByteArray()))
				.append("</Exponent>");
		sb.append("</RSAKeyValue>");
		return sb.toString();
	}

	private static byte[] removeMSZero(byte[] data) {
		byte[] data1;
		int len = data.length;
		if (data[0] == 0) {
			data1 = new byte[data.length - 1];
			System.arraycopy(data, 1, data1, 0, len - 1);
		} else
			data1 = data;

		return data1;
	}
}
