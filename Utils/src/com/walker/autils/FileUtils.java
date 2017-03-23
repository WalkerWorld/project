package com.walker.autils;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.walker.app.AppConfig;
import com.walker.app.BaseApplication;
import com.walker.app.Config_Enum.FilePathEnum;
import com.walker.jutil.DataUtil;
import com.walker.jutil.DateUtil;
import com.walker.jutil.StringUtil;
import com.walker.jutil.TypeUtil;

import android.content.Context;
import android.content.SyncStatusObserver;
import android.os.Environment;
import android.text.format.Formatter;

/**
 * 写文件的工具类
 * 
 */
public class FileUtils {

	// public final String ROOT_DIR = "Android/data/";//缓存的根路径，在当前应用的包名路径下
	public final String ROOT_DIR = "Android/data/" + UIUtils.getPackageName();// 缓存的根路径，在当前应用的包名路径下
	public final String DOWNLOAD_DIR = "download";// 文件下载路径
	public final String CACHE_DIR = "cache";// 缓存路径
	public final String ICON_DIR = "icon";// 图标缓存路径
	public final String CONFIG_DIR = "config";

//	/** 获取下载目录 */
//	public String getDownloadDir() {
//		return getDir(DOWNLOAD_DIR);
//	}
//
//	/** 获取缓存目录 */
//	public String getCacheDir() {
//		return getDir(CACHE_DIR);
//	}
//
//	/** 获取icon目录 */
//	public String getIconDir() {
//		return getDir(ICON_DIR);
//	}

	/** 创建文件夹 */
	public boolean createDirs(String dirPath) {
		File file = new File(dirPath);
		if (!file.exists() || !file.isDirectory()) {
			return file.mkdirs();
		}
		return true;
	}

	/**
	 * Add by walker Date 2017年3月22日
	 * @Description: TODO
	 *  获取指定配置目录
	 *  @param path 应用配置目录枚举
	 *  @return 返回用户指定路径
	 */
	public String getPath(FilePathEnum path) {
		StringBuilder sb = new StringBuilder();
		sb.append(getDir("Android/data/"));
		sb.append(BaseApplication.getInstance().getPackageName() + "/");
		if (path == FilePathEnum.cache) {
			// 缓存文件：非必要文件
			sb.append("cache");
		} else if (path == FilePathEnum.database) {
			// 数据库存储路径
			sb.append("database");
		} else if (path == FilePathEnum.download) {
			// 下载文件存储
			sb.append("download");
		} else if (path == FilePathEnum.icon) {
			// 图标文件
			sb.append("icon");
		} else if (path == FilePathEnum.log) {
			sb.append("cache/LOG");
		}
		return sb + "";
	}

	/** 获取应用目录，当SD卡存在时，获取SD卡上的目录，当SD卡不存在时，获取应用的cache目录 */
	public String getDir(String name) {
		StringBuilder sb = new StringBuilder();
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			sb.append(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator);
		} else {
			sb.append(getCachePath());
		}
		sb.append(name);
		sb.append(File.separator);
		String path = sb.toString();
		System.out.println("路径：" + path);
		if (createDirs(path)) {
			return path;
		} else {
			return null;
		}
	}

	/** 获取SD下的应用目录 */
	private String getExternalStoragePath() {
		StringBuilder sb = new StringBuilder();
		sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
		sb.append(File.separator);
		sb.append(ROOT_DIR);
		sb.append(File.separator);
		return sb.toString();
	}

	/** 获取应用的cache目录 */
	private String getCachePath() {
		File f = UIUtils.getContext().getCacheDir();
		if (null == f) {
			return null;
		} else {
			return f.getAbsolutePath() + "/";
		}
	}

	/** 单例对象 */
	static FileUtils fileUtils;

	public static FileUtils getInstance() {
		if (fileUtils == null) {
			fileUtils = new FileUtils();
			fileUtils.init();
		}
		return fileUtils;
	}

	/**
	 * Add by walker Date 2017年3月16日
	 * 
	 * @Description: TODO 初始化应用文件环境：各个路径建立
	 */
	public void init() {
		if (fileMap == null) {
			fileMap = new HashMap<String, File>();
		}
	}

	/**
	 * 复制文件夹
	 * 
	 * @param sourceDir
	 * @param targetDir
	 * @throws IOException
	 */
	public void copyDirectiory(String sourceDir, String targetDir) throws IOException {
		// 新建目标目录
		(new File(targetDir)).mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
				copyFile(sourceFile, targetFile, false);
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + "/" + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + "/" + file[i].getName();
				copyDirectiory(dir1, dir2);
			}
		}
	}

	/**
	 * 删除一个目录下的所有文件
	 * 
	 * @param filepath
	 *            目录路径
	 * @throws IOException
	 */
	public void del(String filepath) throws IOException {
		File f = new File(filepath);// 定义文件路径
		if (f.exists() && f.isDirectory()) {// 判断是文件还是目录
			if (f.listFiles().length == 0) {// 若目录下没有文件则直接删除
				f.delete();
			} else {// 若有则把文件放进数组，并判断是否有下级目录
				File delFile[] = f.listFiles();
				int i = f.listFiles().length;
				for (int j = 0; j < i; j++) {
					if (delFile[j].isDirectory()) {
						del(delFile[j].getAbsolutePath());// 递归调用del方法并取得子目录路径
					}
					delFile[j].delete();// 删除文件
				}
			}
		}
	}

	/** 复制文件，可以选择是否删除源文件 */
	public boolean copyFile(String srcPath, String destPath, boolean deleteSrc) {
		File srcFile = new File(srcPath);
		File destFile = new File(destPath);
		return copyFile(srcFile, destFile, deleteSrc);
	}

	/** 复制文件，可以选择是否删除源文件 */
	public boolean copyFile(File srcFile, File destFile, boolean deleteSrc) {
		if (!srcFile.exists() || !srcFile.isFile()) {
			return false;
		}
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new FileInputStream(srcFile);
			out = new FileOutputStream(destFile);
			byte[] buffer = new byte[1024];
			int i = -1;
			while ((i = in.read(buffer)) > 0) {
				out.write(buffer, 0, i);
				out.flush();
			}
			if (deleteSrc) {
				srcFile.delete();
			}
		} catch (Exception e) {
			LogUtil.e(e);
			return false;
		} finally {
			close(out);
			close(in);
		}
		return true;
	}

	/**
	 * 复制文件通过流
	 * 
	 * @param inBuff
	 * @param targetFile
	 * @throws IOException
	 */
	public void copyFileByIO(InputStream inBuff, File targetFile) throws IOException {
		BufferedOutputStream outBuff = null;
		try {
			// 新建文件输出流并对它进行缓冲
			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

			// 缓冲数组
			byte[] b = new byte[1024 * 5];
			int len;
			while ((len = inBuff.read(b)) != -1) {
				outBuff.write(b, 0, len);
			}
			// 刷新此缓冲的输出流
			outBuff.flush();
		} finally {
			// 关闭流
			if (inBuff != null)
				inBuff.close();
			if (outBuff != null)
				outBuff.close();
		}
	}

	/**
	 * 资产目录复制入项目filesDir目录
	 * 
	 * @param context
	 *            上下文
	 * @param name
	 *            文件名
	 */
	public void copyAssetsToFilesDir(Context context, String name) {
		InputStream myInput = null;
		try {
			myInput = context.getAssets().open(name);
			File dataFolder = context.getFilesDir();
			File jar = new File(dataFolder.getAbsolutePath() + "/" + name);
			copyFileByIO(myInput, jar);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 判断文件是否可写 */
	public boolean isWriteable(String path) {
		try {
			if (StringUtil.isEmpty(path)) {
				return false;
			}
			File f = new File(path);
			return f.exists() && f.canWrite();
		} catch (Exception e) {
			LogUtil.e(e);
			return false;
		}
	}

	/** 修改文件的权限,例如"777"等 */
	public void chmod(String path, String mode) {
		try {
			String command = "chmod " + mode + " " + path;
			Runtime runtime = Runtime.getRuntime();
			runtime.exec(command);
		} catch (Exception e) {
			LogUtil.e(e);
		}
	}

	/**文件对象缓存*/
	HashMap<String, File> fileMap;
	/**
	 * Add by walker Date 2017年3月12日
	 * @Description: TODO
	 * 	向指定文件中输出信息 
	 *  @param msg 写入内容信息
	 *  @param fileName 写入文件名称
	 */
	public void writeStr(String msg, String fileName){
		try {
			init();
			String path = getPath(FilePathEnum.cache)+ fileName.toUpperCase()+DateUtil.formatDate(System.currentTimeMillis())+".txt";
			FileWriter fw;
			File file = fileMap.get(path);
			if(file == null){
				file = new File(path);
				fileMap.put(path, file);
			}
			if (!file.exists()) {
				file.createNewFile();
			} 
			fw = new FileWriter(file, true);
			fw.write(msg + "\n");
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 把数据写入文件
	 * 
	 * @param is
	 *            数据流
	 * @param path
	 *            文件路径
	 * @param recreate
	 *            如果文件存在，是否需要删除重建
	 * @return 是否写入成功
	 */
	public boolean writeFile(InputStream is, String path, boolean recreate) {
		boolean res = false;
		File f = new File(path);
		FileOutputStream fos = null;
		try {
			if (recreate && f.exists()) {
				f.delete();
			}
			if (!f.exists() && null != is) {
				File parentFile = new File(f.getParent());
				parentFile.mkdirs();
				int count = -1;
				byte[] buffer = new byte[1024];
				fos = new FileOutputStream(f);
				while ((count = is.read(buffer)) != -1) {
					fos.write(buffer, 0, count);
				}
				res = true;
			}
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			close(fos);
			close(is);
		}
		return res;
	}

	/**
	 * 把字符串数据写入文件
	 * 
	 * @param content
	 *            需要写入的字符串
	 * @param path
	 *            文件路径名称
	 * @param append
	 *            是否以添加的模式写入
	 * @return 是否写入成功
	 */
	public boolean writeFile(byte[] content, String path, boolean append) {
		boolean res = false;
		File f = new File(path);
		RandomAccessFile raf = null;
		try {
			if (f.exists()) {
				if (!append) {
					f.delete();
					f.createNewFile();
				}
			} else {
				f.createNewFile();
			}
			if (f.canWrite()) {
				raf = new RandomAccessFile(f, "rw");
				raf.seek(raf.length());
				raf.write(content);
				res = true;
			}
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			close(raf);
		}
		return res;
	}

	/**
	 * 把字符串数据写入文件
	 * 
	 * @param content
	 *            需要写入的字符串
	 * @param path
	 *            文件路径名称
	 * @param append
	 *            是否以添加的模式写入
	 * @return 是否写入成功
	 */
	public boolean writeFile(String content, String path, boolean append) {
		LogUtil.d("文件写入路径： " + path);
		return writeFile(content.getBytes(), path, append);
	}

	/**
	 * 把键值对写入文件
	 * 
	 * @param filePath
	 *            文件路径
	 * @param key
	 *            键
	 * @param value
	 *            值
	 * @param comment
	 *            该键值对的注释
	 */
	public void writeProperties(String filePath, String key, String value, String comment) {
		if (StringUtil.isEmpty(key) || StringUtil.isEmpty(filePath)) {
			return;
		}
		FileInputStream fis = null;
		FileOutputStream fos = null;
		File f = new File(filePath);
		try {
			if (!f.exists() || !f.isFile()) {
				f.createNewFile();
			}
			fis = new FileInputStream(f);
			Properties p = new Properties();
			p.load(fis);// 先读取文件，再把键值对追加到后面
			p.setProperty(key, value);
			fos = new FileOutputStream(f);
			p.store(fos, comment);
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			close(fis);
			close(fos);
		}
	}

	public boolean close(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
				LogUtil.e(e);
			}
		}
		return true;
	}

	/** 根据值读取 */
	public String readProperties(String filePath, String key, String defaultValue) {
		if (StringUtil.isEmpty(key) || StringUtil.isEmpty(filePath)) {
			return null;
		}
		String value = null;
		FileInputStream fis = null;
		File f = new File(filePath);
		try {
			if (!f.exists() || !f.isFile()) {
				f.createNewFile();
			}
			fis = new FileInputStream(f);
			Properties p = new Properties();
			p.load(fis);
			value = p.getProperty(key, defaultValue);
		} catch (IOException e) {
			LogUtil.e(e);
		} finally {
			close(fis);
		}
		return value;
	}

	/** 把字符串键值对的map写入文件 */
	public void writeMap(String filePath, Map<String, String> map, boolean append, String comment) {
		if (map == null || map.size() == 0 || StringUtil.isEmpty(filePath)) {
			return;
		}
		FileInputStream fis = null;
		FileOutputStream fos = null;
		File f = new File(filePath);
		try {
			if (!f.exists() || !f.isFile()) {
				f.createNewFile();
			}
			Properties p = new Properties();
			if (append) {
				fis = new FileInputStream(f);
				p.load(fis);// 先读取文件，再把键值对追加到后面
			}
			p.putAll(map);
			fos = new FileOutputStream(f);
			p.store(fos, comment);
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			close(fis);
			close(fos);
		}
	}

	/** 把字符串键值对的文件读入map */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, String> readMap(String filePath, String defaultValue) {
		if (StringUtil.isEmpty(filePath)) {
			return null;
		}
		Map<String, String> map = null;
		FileInputStream fis = null;
		File f = new File(filePath);
		try {
			if (!f.exists() || !f.isFile()) {
				f.createNewFile();
			}
			fis = new FileInputStream(f);
			Properties p = new Properties();
			p.load(fis);
			map = new HashMap<String, String>((Map) p);// 因为properties继承了map，所以直接通过p来构造一个map
		} catch (Exception e) {
			LogUtil.e(e);
		} finally {
			close(fis);
		}
		return map;
	}

	/** 改名 */
	public boolean copy(String src, String des, boolean delete) {
		File file = new File(src);
		if (!file.exists()) {
			return false;// 源文件不存在，返回false
		}
		File desFile = new File(des);
		FileInputStream in = null;
		FileOutputStream out = null;
		try {
			in = new FileInputStream(file);
			out = new FileOutputStream(desFile);
			byte[] buffer = new byte[1024];
			int count = -1;
			while ((count = in.read(buffer)) != -1) {
				out.write(buffer, 0, count);
				out.flush();
			}
		} catch (Exception e) {
			LogUtil.e(e);
			return false;
		} finally {
			close(in);
			close(out);
		}
		if (delete) {
			file.delete();
		}
		return true;
	}

	/**
	 * 文件转成字节数组
	 * 
	 * @param path
	 * @return byte[]
	 * @throws IOException
	 */
	public byte[] readFileToBytes(String path) throws IOException {
		byte[] b = null;
		InputStream is = null;
		File f = new File(path);
		try {
			is = new FileInputStream(f);
			b = new byte[(int) f.length()];
			is.read(b);
		} finally {
			if (is != null)
				is.close();
		}
		return b;
	}

	/**
	 * 将byte写入文件中
	 * 
	 * @param fileByte
	 * @param filePath
	 * @throws IOException
	 */
	public void byteToFile(byte[] fileByte, String filePath) throws IOException {
		OutputStream os = null;
		try {
			os = new FileOutputStream(new File(filePath));
			os.write(fileByte);
			os.flush();
		} finally {
			if (os != null)
				os.close();
		}
	}

	/**
	 * 折分数组
	 * 
	 * @param ary
	 * @param subSize
	 * @return 数组
	 */
	public List<List<Object>> splitAry(Object[] ary, int subSize) {
		int count = ary.length % subSize == 0 ? ary.length / subSize : ary.length / subSize + 1;

		List<List<Object>> subAryList = new ArrayList<List<Object>>();

		for (int i = 0; i < count; i++) {
			int index = i * subSize;

			List<Object> list = new ArrayList<Object>();
			int j = 0;
			while (j < subSize && index < ary.length) {
				list.add(ary[index++]);
				j++;
			}

			subAryList.add(list);
		}

		return subAryList;
	}

	/**
	 * 数据转字符串
	 * 
	 * @param mobile
	 *            数据
	 * @return 转换好的字符串
	 */
	public String ArrayToString(Object[] mobile) {
		String destId = "";
		for (Object phone : mobile) {
			destId += " " + (String) phone;
		}
		return destId.trim();
	}

	/** 格式化文件大小，不保留末尾的0 */
	public String formatFileSize(long len) {
		return formatFileSize(len, false);
	}

	/** 格式化文件大小，保留末尾的0，达到长度一致 */
	public String formatFileSize(long len, boolean keepZero) {
		String size;
		DecimalFormat formatKeepTwoZero = new DecimalFormat("#.00");
		DecimalFormat formatKeepOneZero = new DecimalFormat("#.0");
		if (len < 1024) {
			size = String.valueOf(len + "B");
		} else if (len < 10 * 1024) {
			// [0, 10KB)，保留两位小数
			size = String.valueOf(len * 100 / 1024 / (float) 100) + "KB";
		} else if (len < 100 * 1024) {
			// [10KB, 100KB)，保留一位小数
			size = String.valueOf(len * 10 / 1024 / (float) 10) + "KB";
		} else if (len < 1024 * 1024) {
			// [100KB, 1MB)，个位四舍五入
			size = String.valueOf(len / 1024) + "KB";
		} else if (len < 10 * 1024 * 1024) {
			// [1MB, 10MB)，保留两位小数
			if (keepZero) {
				size = String.valueOf(formatKeepTwoZero.format(len * 100 / 1024 / 1024 / (float) 100)) + "MB";
			} else {
				size = String.valueOf(len * 100 / 1024 / 1024 / (float) 100) + "MB";
			}
		} else if (len < 100 * 1024 * 1024) {
			// [10MB, 100MB)，保留一位小数
			if (keepZero) {
				size = String.valueOf(formatKeepOneZero.format(len * 10 / 1024 / 1024 / (float) 10)) + "MB";
			} else {
				size = String.valueOf(len * 10 / 1024 / 1024 / (float) 10) + "MB";
			}
		} else if (len < 1024 * 1024 * 1024) {
			// [100MB, 1GB)，个位四舍五入
			size = String.valueOf(len / 1024 / 1024) + "MB";
		} else {
			// [1GB, ...)，保留两位小数
			size = String.valueOf(len * 100 / 1024 / 1024 / 1024 / (float) 100) + "GB";
		}
		return size;
	}

	/**
	 * 方法描述：给文件加时间戳
	 */
	public void updateFileTime(String dir, String fileName) {
		File file = new File(dir, fileName);
		long newModifiedTime = System.currentTimeMillis();
		file.setLastModified(newModifiedTime);
	}

	/**
	 * 将long值大小格式化为有单位的KB MB GB
	 * 
	 * @param context
	 * @param size
	 * @return 格式化后的KB MB GB
	 */
	public String formatFileSize(Context context, long size) {
		return Formatter.formatFileSize(context, size);
	}

	/**
	 * url路径编码为文件名
	 * 
	 * @param url
	 *            文件网络请求url
	 * @return url对应的文件名
	 */
	public String url2FileName(String url) {
		String md5Url = DataUtil.md5(url);
		String path = new File(getPath(FilePathEnum.cache) + URLEncoder.encode(md5Url) + ".json").getAbsolutePath();
		return path;
	}

	/**
	 * 根据文件url获取本地缓存文件名
	 * 
	 * @param url
	 *            需要编码的文件名
	 * @param suffix
	 *            文件后缀名
	 * @return
	 */
	public String getCachePathByURL(String url, String suffix) {
		LogUtil.d("缓存url： " + url);
		String md5Url = DataUtil.md5(url);
		String path = new File(getPath(FilePathEnum.cache) + URLEncoder.encode(md5Url) + suffix).getAbsolutePath();
		return path;
	}
	
	/**
	 * Add by walker Date 2017年3月12日
	 * @Description: TODO
	 *	清空无效缓存信息
	 */
	public  void deleteHistory() {
		try {
			init();
			File file = new File(getPath(FilePathEnum.cache));
			if(file.isDirectory() && !file.exists()){
				file.mkdirs();
				return;
			}
			for (File fileChild : file.listFiles()) {
				int difData = DateUtil.formatDay(System.currentTimeMillis()-fileChild.lastModified());
				if (difData > AppConfig.getInstance().getCacheDay()) {
					LogUtil.eToFile("删除日志：" +fileChild.getName() + "\t占用时间：" + DateUtil.formatDay(System.currentTimeMillis()-fileChild.lastModified())+ "\t生效时间：" + AppConfig.getInstance().getCacheDay());
					fileChild.delete();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 根据文件路径获取文件类型
	 * 
	 * @param path
	 *            文件路径
	 * @return 文件后缀名
	 */
	public static String getFileType(String path) {
		return path.substring(path.lastIndexOf("."));
	}
}
