package com.walker.module.utils;

import android.annotation.TargetApi;
import android.os.Build;
import java.lang.ref.SoftReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MD5Util {
	private static final ConcurrentMap<String, String> cache = new ConcurrentHashMap<String, String>();

	private static final LinkedList<SoftReference<String>> cachelist = new LinkedList<SoftReference<String>>();

	private static final int sum = 1000000;

	private static final Lock lock = new ReentrantLock();

	public final static String md5s(String src) {
		if (src == null)
			throw new IllegalArgumentException("src can not be  null!");
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		if (md5 == null)
			throw new RuntimeException("md5 can not be initialized!");
		md5.update(src.getBytes());

		byte[] array = md5.digest();
		StringBuilder sb = new StringBuilder();
		for (int j = 0; j < array.length; ++j) {
			int b = array[j] & 0xFF;
			if (b < 0x10)
				sb.append('0');
			sb.append(Integer.toHexString(b));
		}
		return sb.toString();
	}

	/*
	 * @TargetApi(Build.VERSION_CODES.GINGERBREAD) public final static String
	 * md5(String src) { String s = cache.get(src); if (s == null) { s =
	 * md5s(src); lock.lock(); try{ if (cachelist.size()>=sum)
	 * cache.remove(cachelist.pop()); else cachelist.addLast(new
	 * SoftReference<String>(src)); }finally { lock.unlock(); } cache.put(src,
	 * s); } return s; }
	 */
	public final static void clear() {
		cache.clear();
		cachelist.clear();
	}

	public static void main(String[] args) {
		System.out.println(md5s("123456abc"));
	}

}
