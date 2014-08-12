package com.encrypt;

import java.security.Key;
import java.security.interfaces.RSAPrivateKey;
import java.util.Map;

public class RSATester {

	static String publicKey;
	static String privateKey;

	static {
		try {
			Map<String, Object> keyMap = RSAUtils.genKeyPair();
			publicKey = RSAUtils.getPublicKey(keyMap);
			privateKey = RSAUtils.getPrivateKey(keyMap);
			System.err.println("公钥: \n\r" + publicKey);
			System.err.println("私钥： \n\r" + privateKey);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
//		 test();
		 testSign();
//		getPrivateKey();
//		testP();
	}

	/**
	 * MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIy8CPfS66uq9C57dfYegHDHnCtyCtuU7jtjXBr5sn8hQuaHOkq8mdrWWWhT2XX6K
	 * +GDFJ9RVUikOKQ7QYR+yGP5WIrzXAv94PWSKbMrxEKta0ib3vd5+r9sTGK6idrs8LnXJjx/
	 * TeSUivP6dgV/mI1+VfTLoUgDX7lIneOE+fb7AgMBAAECgYBXyTMO41J+
	 * UrTeCcUbMQuNTfXsY8bCEbRI90GM0hS1mFnS0qdZ1ythhjNfEG6lWNfmY42Jk4JPPaDXUN8ddrgoNlgUCwUuVSugMXXJN6Ghvrah2
	 * +IHsBImaWOczm8SNo0Qocq51ak8dBkim71Sx4bNBsG/
	 * VsGrRQpuXdvVcR6wAQJBANLBZmrFVFBeUesY423RMCFKiATxHFaXSltGCFxbrfOXXxYcvUvpU6nSihuHIKlNfTrl1bct9bQZEP50NtfiyoECQQCq8nTFWYofxW
	 * +rVi4ke2FMjSkT8vGRpHGWbDaV0Ja+1
	 * trPsdpgXImE4vRpNCNt1E2ZNpImx6xxm4B1yMO9fyt7AkEAtiNKHbAX47T
	 * /2suRkVCd5ceBsbOmJ/
	 * dr3WGyzA63ULNbC8MenUIyeatoi83GHkrHlWUXw81GcLTRCPbqhryLAQJAKbdbKaHNJC2xL5WrUvnM57p28MhTjSAdHAEyXOL3NoEGCrMzQFq0sHoV
	 * /OpdTvsFKMN5bP9DJMI22gdxRPgYqwJAEk9v2+x21THQpT+
	 * h4BelAIQVPNjeupwtlSlLvQoovYX5V1V3W1CZpN5HF7ZbXVEvFL5af3KoE7xzu0OTqqqotg==
	 * 
	 * @throws Exception
	 */
	public static void getPrivateKey() throws Exception {

		RSAPrivateKey key = (RSAPrivateKey) RSAUtils
				.getPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIy8CPfS66uq9C57dfYegHDHnCtyCtuU7jtjXBr5sn8hQuaHOkq8mdrWWWhT2XX6K+GDFJ9RVUikOKQ7QYR+yGP5WIrzXAv94PWSKbMrxEKta0ib3vd5+r9sTGK6idrs8LnXJjx/TeSUivP6dgV/mI1+VfTLoUgDX7lIneOE+fb7AgMBAAECgYBXyTMO41J+UrTeCcUbMQuNTfXsY8bCEbRI90GM0hS1mFnS0qdZ1ythhjNfEG6lWNfmY42Jk4JPPaDXUN8ddrgoNlgUCwUuVSugMXXJN6Ghvrah2+IHsBImaWOczm8SNo0Qocq51ak8dBkim71Sx4bNBsG/VsGrRQpuXdvVcR6wAQJBANLBZmrFVFBeUesY423RMCFKiATxHFaXSltGCFxbrfOXXxYcvUvpU6nSihuHIKlNfTrl1bct9bQZEP50NtfiyoECQQCq8nTFWYofxW+rVi4ke2FMjSkT8vGRpHGWbDaV0Ja+1trPsdpgXImE4vRpNCNt1E2ZNpImx6xxm4B1yMO9fyt7AkEAtiNKHbAX47T/2suRkVCd5ceBsbOmJ/dr3WGyzA63ULNbC8MenUIyeatoi83GHkrHlWUXw81GcLTRCPbqhryLAQJAKbdbKaHNJC2xL5WrUvnM57p28MhTjSAdHAEyXOL3NoEGCrMzQFq0sHoV/OpdTvsFKMN5bP9DJMI22gdxRPgYqwJAEk9v2+x21THQpT+h4BelAIQVPNjeupwtlSlLvQoovYX5V1V3W1CZpN5HF7ZbXVEvFL5af3KoE7xzu0OTqqqotg==");
		System.out.println("privateKey encode==:"
				+ Base64Utils.encode(((Key) key).getEncoded()));
		// [B@78b5f53a
	}

	static void test() throws Exception {
		System.err.println("公钥加密——私钥解密");
		String source = "这是一行没有任何意义的文字，你看完了等于没看，不是吗？";
		System.out.println("\r加密前文字：\r\n" + source);
		byte[] data = source.getBytes();
		byte[] encodedData = RSAUtils.encryptByPublicKey(data, publicKey);
		System.out.println("加密后文字：\r\n" + new String(encodedData));
		byte[] decodedData = RSAUtils.decryptByPrivateKey(encodedData,
				privateKey);
		String target = new String(decodedData);
		System.out.println("解密后文字: \r\n" + target);
	}

	static void testP() throws Exception {

		// String soure1 =
		// "ADsWGrL63aZ8DLidin0rC40F4llthiWF+iS9ciinEwm8qdSn1/TRCoGEBYSW1juN5cLWWCvboihcajvUf8PhvXJfGjVH6xUF5PPQJeS5NN8ypktPKxlJQeeAzMP29z5lLCFVaMsUwiCMYGmJFhWU575cBhsnw2s/STVOsVvW4FvZ";
		String privateKey1 = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIy8CPfS66uq9C57dfYegHDHnCtyCtuU7jtjXBr5sn8hQuaHOkq8mdrWWWhT2XX6K+GDFJ9RVUikOKQ7QYR+yGP5WIrzXAv94PWSKbMrxEKta0ib3vd5+r9sTGK6idrs8LnXJjx/TeSUivP6dgV/mI1+VfTLoUgDX7lIneOE+fb7AgMBAAECgYBXyTMO41J+UrTeCcUbMQuNTfXsY8bCEbRI90GM0hS1mFnS0qdZ1ythhjNfEG6lWNfmY42Jk4JPPaDXUN8ddrgoNlgUCwUuVSugMXXJN6Ghvrah2+IHsBImaWOczm8SNo0Qocq51ak8dBkim71Sx4bNBsG/VsGrRQpuXdvVcR6wAQJBANLBZmrFVFBeUesY423RMCFKiATxHFaXSltGCFxbrfOXXxYcvUvpU6nSihuHIKlNfTrl1bct9bQZEP50NtfiyoECQQCq8nTFWYofxW+rVi4ke2FMjSkT8vGRpHGWbDaV0Ja+1trPsdpgXImE4vRpNCNt1E2ZNpImx6xxm4B1yMO9fyt7AkEAtiNKHbAX47T/2suRkVCd5ceBsbOmJ/dr3WGyzA63ULNbC8MenUIyeatoi83GHkrHlWUXw81GcLTRCPbqhryLAQJAKbdbKaHNJC2xL5WrUvnM57p28MhTjSAdHAEyXOL3NoEGCrMzQFq0sHoV/OpdTvsFKMN5bP9DJMI22gdxRPgYqwJAEk9v2+x21THQpT+h4BelAIQVPNjeupwtlSlLvQoovYX5V1V3W1CZpN5HF7ZbXVEvFL5af3KoE7xzu0OTqqqotg==";
		String publicKey1 = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDduJiqjyXBigujBTpiPNv5D2rP/jLhq4lAVJGqs76HVQcee1qQsobhiiWLqvUfhnkC4uOG8mGke6R1Ugw1cbTz7QrpweLAyLd5NNXX94mokTQbXkABLzbTUauMRygVRIpoCbdsazYWzcGgNQTZZIHYf4yFVCvqCI2zAikAkzRmgwIDAQAB";

		System.err.println("私钥加密——公钥解密:");
		String source = "这是一行测试RSA数字加密解密的文字";
		System.out.println("原文字：\r\n" + source);
		byte[] data = source.getBytes();
		byte[] encodedData = RSAUtils.encryptByPrivateKey(data, privateKey1);
		System.out.println("加密后：\r\n" + new String(encodedData));

		byte[] decodedData = RSAUtils.decryptByPublicKey(encodedData,
				publicKey1);
		String target = new String(decodedData);
		System.out.println("解密后: \r\n" + target);

	}

	static void testSign() throws Exception {
		System.err.println("私钥加密——公钥解密");
		String source = "这是一行测试RSA数字签名的无意义文字";
		System.out.println("原文字：\r\n" + source);
		byte[] data = source.getBytes();
		byte[] encodedData = RSAUtils.encryptByPrivateKey(data, privateKey);
		System.out.println("加密后：\r\n" + new String(encodedData));
		byte[] decodedData = RSAUtils
				.decryptByPublicKey(encodedData, publicKey);
		String target = new String(decodedData);
		System.out.println("解密后: \r\n" + target);
		System.err.println("私钥签名——公钥验证签名");
		String sign = RSAUtils.sign(encodedData, privateKey);
		System.err.println("签名:\r" + sign);
		boolean status = RSAUtils.verify(encodedData, publicKey, sign);
		System.err.println("验证结果:\r" + status);
	}

}
