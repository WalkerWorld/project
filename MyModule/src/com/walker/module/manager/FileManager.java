package com.walker.module.manager;

import java.io.File;
import java.io.InputStream;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseStream;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.walker.constant.ConstentStrings;
import com.walker.manager.ThreadPoolManager;
import com.walker.module.callback.downloadCallback;
import com.walker.utils.FileUtils;
import com.walker.utils.LogUtils;

/**
 * 文件管理器
 * @author walker
 *
 */
public class FileManager {
	/**
	 * 获取应用缓存文件流
	 * filename: 
	 * 		文件名
	 * isEncode：
	 * 		为true时对传入文件名编码后使用，当文件名包含特殊字符或需要隐藏文件名时使用
	 * @return
	 * 		返回缓存路径下的文件流对象
	 */
	public static File getCacheFile(String filename, boolean isEncode){
		String path;
		if(isEncode){//需要对文件名进行编码（隐藏文件名）
			path = FileUtils.getCacheDir() + FileUtils.getCachePathByURL(filename, ".zip");
		}else {
			path = FileUtils.getCacheDir() + filename;
		}
		LogUtils.e(ConstentStrings.LogMesg + path + "\n");
		File file = new File(path);
		
		return file;
	}
	
	/**
	 * 获取应用下载文件的文件流
	 * @return
	 */
	public static File getDownloadFile(){
		return null;
	}
	
	/**
	 * 判断是否已缓存
	 * @param url 被缓存video的url地址
	 * @return 文件已缓存返回true
	 */
	public static boolean isCashe(String url) {
		File file = FileManager.getCacheFile(url, true);//对文件url进行编码
//		FileManager.getCacheFile(url, false);
		LogUtils.e("该文件是否存在： " + file.exists());
		boolean isExists = file.exists();
		return file.exists();
//		return false;
	}

	/**
	 * 根据url将文件下载到缓存路径
	 * @param value 文件下载url
	 */
	public static void download2Cache(final String url, final downloadCallback callback) {
		
		ThreadPoolManager threadPoolManager = ThreadPoolManager.getInstance();
	
		Runnable myRunnable = new Runnable() {
			
			@Override
			public void run() {
				File file;
				ResponseStream responseStream;
				try {
					HttpUtils httpUtils = new HttpUtils();
					responseStream = httpUtils.sendSync(HttpMethod.GET, url);
					InputStream is = responseStream.getBaseStream();
					String path = FileUtils.getCacheDir() + FileUtils.getCachePathByURL(url, ".zip");
					if(FileUtils.writeFile(is, path, false)){
						LogUtils.e("文件缓存成功 \n" + path);
						callback.success();
					}
					else{//文件缓存失败则删除该文件
						file = new File(path);
						if(file != null && file.exists()){
							file.delete();
						}
						LogUtils.e("文件缓存失败");
						callback.failure();
					}
				} catch (HttpException e) {
					// TODO Auto-generated catch block
					LogUtils.e("网络访问失败");
					e.printStackTrace();
				}
			}
		};
		threadPoolManager.addTask(myRunnable);//将任务添加到线程池管理器中
	}

	/**
	 * 文件下载到cache目录
	 * @param url 文件下载路径
	 * @param fileName 保存的文件名
	 * @param downloadCallback 下载回调方法
	 */
	public static void download2Cache(final String url, final String fileName,
			final downloadCallback callback) {

		
		ThreadPoolManager threadPoolManager = ThreadPoolManager.getInstance();
	
		Runnable myRunnable = new Runnable() {
			
			@Override
			public void run() {
				File file;
				ResponseStream responseStream;
				try {
					HttpUtils httpUtils = new HttpUtils();
					responseStream = httpUtils.sendSync(HttpMethod.GET, url);
					InputStream is = responseStream.getBaseStream();
					String path = FileUtils.getCacheDir() + fileName;
					LogUtils.e("文件缓存路径" + path);
					if(FileUtils.writeFile(is, path, false)){
						LogUtils.e("文件缓存成功");
						callback.success();
					}
					else{//文件缓存失败则删除该文件
						file = new File(path);
						if(file != null && file.exists()){
							file.delete();
						}
						LogUtils.e("文件缓存失败");
						callback.failure();
					}
				} catch (HttpException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					LogUtils.e("网络访问失败");
				}
			}
		};
		threadPoolManager.addTask(myRunnable);//将任务添加到线程池管理器中
	}
}
