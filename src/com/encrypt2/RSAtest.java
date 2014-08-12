package com.encrypt2;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;

import javax.crypto.Cipher;

import org.junit.Test;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class RSAtest {

	@Test
	public void test() throws Exception {
		encryptLoginInfo(
				"111111",
				"jsWda7YVFLB7P+XDWhQY3lHFboP774xHNnEEjxNvciJFc8Vq/g/ZhYovoLUSUemc/Wkh1LNYMy64pDMyv/lkXHsYSe3n9iSTAI4Ob7Sh2USQDnt4blv/HwCFwFY6ZuwHDmy4q06E5CWkpcbFQ4zTpEtjQKQqiFRAzLJ03Vnvafs=",
				"AQAB");
	}

	public String encryptLoginInfo(String pwd, String module, String exponent)
			throws Exception {
		// 给密码加密
		byte[] modulusBytes = new BASE64Decoder().decodeBuffer(module);
		byte[] exponentBytes = new BASE64Decoder().decodeBuffer(exponent);
		BigInteger integreModulu = new BigInteger(1, modulusBytes);
		BigInteger integreExponent = new BigInteger(1, exponentBytes);

		RSAPublicKeySpec rsaPubKey = new RSAPublicKeySpec(integreModulu,
				integreExponent);
		KeyFactory fact = KeyFactory.getInstance("RSA");
		PublicKey pubKey = fact.generatePublic(rsaPubKey);

		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, pubKey);

		byte[] cipherData = cipher.doFinal(pwd.getBytes());
		String enPwd = new BASE64Encoder().encodeBuffer(cipherData);

		return enPwd;
	}
}
