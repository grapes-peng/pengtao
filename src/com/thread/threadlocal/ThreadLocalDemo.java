package com.thread.threadlocal;

import java.util.Random;

import org.apache.log4j.chainsaw.Main;

public class ThreadLocalDemo implements Runnable {

	private final static ThreadLocal studentLocal = new ThreadLocal();

	@Override
	public void run() {
		accessStudent();
	}

	private void accessStudent() {
		String currentThreadName = Thread.currentThread().getName();
		System.out.println(currentThreadName + "is running");
		Random random = new Random();
		int age = random.nextInt(100);
		System.out.println("thread" + currentThreadName + "set age to :" + age);
		Student student = getStudent();

	}

	private Student getStudent() {
		Student student = (Student) studentLocal.get();
		if (student == null) {
			student = new Student();
			studentLocal.set(student);
		}
		return student;
	}

	public static void main(String[] args) {
		ThreadLocalDemo td = new ThreadLocalDemo();
		new Thread(td, "a").start();
		new Thread(td, "b").start();
	}
}
