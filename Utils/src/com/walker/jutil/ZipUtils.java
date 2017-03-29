package com.walker.jutil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.http.protocol.HTTP;

public class ZipUtils {
	private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte

	/**
	 * Gzip字符串压缩
	 * 
	 * @param String str 被压缩字符串
	 * @throws IOException
	 *             IO错误
	 */
	public static byte[] compress(String str) {
		byte[] resBytes = null;
		try {
			if (str == null || str.length() == 0) {
				return null;
			}
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			GZIPOutputStream gzip;
			gzip = new GZIPOutputStream(out);
			gzip.write(str.getBytes());
			gzip.close();
			resBytes = out.toByteArray();
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resBytes;
	}

	/**
	 * Gzip字符串解压
	 * 
	 * @param byte[] buf
	 *            解压流
	 * @throws IOException
	 *             IO错误
	 */
	public static byte[] uncompress(byte[] buf) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ByteArrayInputStream in = new ByteArrayInputStream(buf);
		GZIPInputStream gunzip = new GZIPInputStream(in);
		byte[] buffer = new byte[256];
		int n;
		while ((n = gunzip.read(buffer)) >= 0) {
			out.write(buffer, 0, n);
		}
		return out.toByteArray();
	}

	/**
	 * Gzip文件压缩
	 * 
	 * @param resFileList
     *
	 * @param zipFile
	 *            压缩文件
	 * @throws IOException
	 *             IO错误
	 */
	public static void zipFiles(Collection<File> resFileList, File zipFile)
			throws IOException {

		ZipOutputStream zipout = null;
		try {
			zipout = new ZipOutputStream(new BufferedOutputStream(
					new FileOutputStream(zipFile), BUFF_SIZE));
			for (File resFile : resFileList) {
				zipFile(resFile, zipout, "");
			}
		} finally {
			if (zipout != null)
				zipout.close();
		}
	}

	/**
	 *  Gzip文件压缩
	 * 
	 * @param resFileList
	 *           
	 * @param zipFile
	 *            
	 * @param comment
	 *            
	 * @throws IOException
	 *             
	 */
	public static void zipFiles(Collection<File> resFileList, File zipFile,
			String comment) throws IOException {
		ZipOutputStream zipout = null;
		try {
			zipout = new ZipOutputStream(new BufferedOutputStream(
					new FileOutputStream(zipFile), BUFF_SIZE));
			for (File resFile : resFileList) {
				zipFile(resFile, zipout, "");
			}
			zipout.setComment(comment);

		} finally {
			if (zipout != null)
				zipout.close();
		}
	}

	/**
	 * Gzip文件解压
	 * 
	 * @param zipFile
	 *            
	 * @param folderPath
	 *           
	 * @throws IOException
	 *             
	 */
	public static void upZipFile(File zipFile, String folderPath)
			throws ZipException, IOException {
		File desDir = new File(folderPath);
		if (!desDir.exists()) {
			desDir.mkdirs();
		}
		ZipFile zf = new ZipFile(zipFile);
		InputStream in = null;
		OutputStream out = null;
		try {
			for (Enumeration<?> entries = zf.entries(); entries
					.hasMoreElements();) {
				ZipEntry entry = ((ZipEntry) entries.nextElement());
				in = zf.getInputStream(entry);
				String str = folderPath + File.separator + entry.getName();
				str = new String(str.getBytes("8859_1"), HTTP.UTF_8);
				File desFile = new File(str);
				if (!desFile.exists()) {
					File fileParentDir = desFile.getParentFile();
					if (!fileParentDir.exists()) {
						fileParentDir.mkdirs();
					}
					desFile.createNewFile();
				}
				out = new FileOutputStream(desFile);
				byte buffer[] = new byte[BUFF_SIZE];
				int realLength;
				while ((realLength = in.read(buffer)) > 0) {
					out.write(buffer, 0, realLength);
				}

			}
		} finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}
	}

	/**
	 * Gzip文件解压
	 * 
	 * @param zipFile
	 *            
	 * @param folderPath
	 *            
	 * @param nameContains
	 *           
	 * @throws ZipException
	 *             
	 * @throws IOException
	 *             
	 */
	public static ArrayList<File> upZipSelectedFile(File zipFile,
			String folderPath, String nameContains) throws ZipException,
			IOException {
		ArrayList<File> fileList = new ArrayList<File>();

		File desDir = new File(folderPath);
		if (!desDir.exists()) {
			desDir.mkdir();
		}

		ZipFile zf = new ZipFile(zipFile);
		InputStream in = null;
		OutputStream out = null;
		try {
			for (Enumeration<?> entries = zf.entries(); entries
					.hasMoreElements();) {
				ZipEntry entry = ((ZipEntry) entries.nextElement());
				if (entry.getName().contains(nameContains)) {
					in = zf.getInputStream(entry);
					String str = folderPath + File.separator + entry.getName();
					str = new String(str.getBytes("8859_1"), HTTP.UTF_8);
					// str.getBytes(AppConstans.UTF_8),"8859_1" ���
					// str.getBytes("8859_1"),AppConstans.UTF_8 ����
					File desFile = new File(str);
					if (!desFile.exists()) {
						File fileParentDir = desFile.getParentFile();
						if (!fileParentDir.exists()) {
							fileParentDir.mkdirs();
						}
						desFile.createNewFile();
					}
					out = new FileOutputStream(desFile);
					byte buffer[] = new byte[BUFF_SIZE];
					int realLength;
					while ((realLength = in.read(buffer)) > 0) {
						out.write(buffer, 0, realLength);
					}

					fileList.add(desFile);
				}
			}
		} finally {
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		}
		return fileList;
	}

	/**
	 * 类型转化
	 * 
	 * @param zipFile
	 *            
	 * @return 
	 * @throws ZipException
	 *             
	 * @throws IOException
	 *             
	 */
	public static ArrayList<String> getEntriesNames(File zipFile)
			throws ZipException, IOException {
		ArrayList<String> entryNames = new ArrayList<String>();
		Enumeration<?> entries = getEntriesEnumeration(zipFile);
		while (entries.hasMoreElements()) {
			ZipEntry entry = ((ZipEntry) entries.nextElement());
			entryNames.add(new String(getEntryName(entry).getBytes(HTTP.UTF_8),
					"8859_1"));
		}
		return entryNames;
	}

	/**
	 * 类型转化
	 * 
	 * @param zipFile
	 *            
	 * @return
	 * @throws ZipException
	 *            
	 * @throws IOException
	 *             
	 */
	public static Enumeration<?> getEntriesEnumeration(File zipFile)
			throws ZipException, IOException {
		ZipFile zf = new ZipFile(zipFile);
		return zf.entries();

	}

	/**
	 * 类型转化
	 * 
	 * @param entry
	 *            
	 * @return 
	 * @throws UnsupportedEncodingException
	 */
	public static String getEntryComment(ZipEntry entry)
			throws UnsupportedEncodingException {
		return new String(entry.getComment().getBytes(HTTP.UTF_8), "8859_1");
	}

	/**
	 * 类型转化
	 * 
	 * @param entry
	 *           
	 * @return 
	 * @throws UnsupportedEncodingException
	 */
	public static String getEntryName(ZipEntry entry)
			throws UnsupportedEncodingException {
		return new String(entry.getName().getBytes(HTTP.UTF_8), "8859_1");
	}

	/**
	 * 文件压缩
	 * 
	 * @param resFile
	 *            
	 * @param zipout
	 *            
	 * @param rootpath
	 *            
	 * @throws FileNotFoundException
	 *             
	 * @throws IOException
	 *             
	 */
	private static void zipFile(File resFile, ZipOutputStream zipout,
			String rootpath) throws FileNotFoundException, IOException {
		rootpath = rootpath
				+ (rootpath.trim().length() == 0 ? "" : File.separator)
				+ resFile.getName();
		rootpath = new String(rootpath.getBytes("8859_1"), HTTP.UTF_8);
		BufferedInputStream in = null;
		try {
			if (resFile.isDirectory()) {
				File[] fileList = resFile.listFiles();
				for (File file : fileList) {
					zipFile(file, zipout, rootpath);
				}
			} else {
				byte buffer[] = new byte[BUFF_SIZE];
				in = new BufferedInputStream(new FileInputStream(resFile),
						BUFF_SIZE);
				zipout.putNextEntry(new ZipEntry(rootpath));
				int realLength;
				while ((realLength = in.read(buffer)) != -1) {
					zipout.write(buffer, 0, realLength);
				}
				in.close();
				zipout.flush();
				zipout.closeEntry();
			}
		} finally {
			if (in != null)
				in.close();
			// if (zipout != null);
			// zipout.close();
		}
	}
}