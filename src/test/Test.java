package test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.UUID;

import org.springframework.beans.propertyeditors.UUIDEditor;

public class Test {

	public static void main(String[] args) throws IOException {
		checkDate(1403843280000L);
		if (1 == 1) {
			return;
		}
		getLongValueBefore3();
		generateToken();
		Enumeration<NetworkInterface> eni = NetworkInterface
				.getNetworkInterfaces();
		while (eni.hasMoreElements()) {
			NetworkInterface ni = eni.nextElement();
			Enumeration<InetAddress> eIa = ni.getInetAddresses();
			while (eIa.hasMoreElements()) {
				System.out.println(eIa.nextElement().getHostName());
			}
		}
	}

	public static void checkDate(long l) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(l);
		// c.add(Calendar.MINUTE, 5);
		System.out.println(c.getTimeInMillis());
		System.out.println("Current Value in DB:" + c.getTime());
		System.out.println();
	}

	public static void generateToken() {
		UUID uuid = UUID.randomUUID();
		long token = uuid.getMostSignificantBits();
		System.out.println(token);
	}

	public static void getLongValueBefore3() {
		Calendar c = Calendar.getInstance();
		long l1 = c.getTimeInMillis();
		System.out.println(c.getTime());
		c.add(Calendar.HOUR, -3);
		long l2 = c.getTimeInMillis();
		System.out.println("reuslt" + (l1 >= l2));
		System.out.println(c.getTime());
	}

	@org.junit.Test
	public void checkStringLength() {
		System.out.println(Long.MIN_VALUE);
		String s = "<RSAKeyValue><Modulus>jXF5n30r8wmlhXdnV46ogetFQBM9BC9xzRxPmgtFZ4pVI/5Zdml76KmVBwq+SXdBtkeScPumQcugbr6vJfS1wXz/KTXBXiWDdrrW8MZ6/CSw6QBi72+UL+hC47BhzNKYq02v+J3e+LokN/1HWvNEErjSJ/iTmj9f8llLvy7pDuE=</Modulus><Exponent>YfJNnbMpZxCsDYOFDa3dZLJObzOjUi2kqbvMJ8lcU3oIwXcV7pjXVGNyrsCyyt8Jozdm4lRxaiifPXFQioEuBviP5tf4aOQZsC5WbW2EEr+k0171ESAZCmtnvewRX5hofSYt7h9Xs9Emk3uoJCX1G/waFizttBiIUtbHc0VOvY0=</Exponent></RSAKeyValue>";
		System.out.println(s.length());
	}

	@org.junit.Test
	public void testObject() {

		A a1 = new A();
		a1.setS("123");
		System.out.println(a1.getS());

		B b1 = new B();
		b1.setT("abc");
		System.out.println(b1.getT());

		a1.setS(b1.getT());
		System.out.println(a1.getS());
	}
}

class A {
	private String s;

	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}
}

class B {
	private String t;

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}
}
