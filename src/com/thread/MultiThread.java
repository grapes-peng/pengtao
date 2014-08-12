package com.thread;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MultiThread {

	public static Map<String, String> cache = new ConcurrentHashMap<String, String>();
	static {
		cache.put("aaa", "");
		cache.put("", "123");
	}

	public static void main(String[] args) {
		MultiThread mt = new MultiThread();
		ClassB b = mt.new ClassB();
		mt.new AThread(b).start();
		mt.new AThread(b).start();
		mt.new AThread(b).start();
		mt.new AThread(b).start();
	}

	class AThread extends Thread {

		private ClassB b;

		public void run() {
			while (true) {
				try {
					b.print();
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		public AThread(ClassB b) {
			this.b = b;
		}
	}

	class ClassB {

		public synchronized void print() {
			String token = "aaa";
			String cacheToken = "";
			Iterator<String> s = cache.keySet().iterator();
			while (s.hasNext()) {
				String temp = s.next();
				if (temp.equals(token)) {
					cacheToken = temp;
					break;
				}
			}
			synchronized (cache) {
				System.out.println(cacheToken);
			}
		}
	}
}