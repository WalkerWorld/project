package com.walker.jutil;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpStatus;
import org.apache.http.protocol.HTTP;

import com.walker.bean.HttpRequest;
import com.walker.manager.ThreadPoolManager;

import android.app.DownloadManager.Request;
import android.os.Handler;
import android.os.Message;

public class HttpUtil {

	/** 转码格式 */
	private final String mEncoding = HTTP.UTF_8;
	private URL url;
	private HttpURLConnection mURLConnection;
	/** 连接超时时间 */
	private final int mConTimeout = 50 * 1000;
	/** 读取超时时间 */
	private final int mSoTimeout = 50 * 1000;
	/** 返回结果 */
	private ByteArrayOutputStream resultData = new ByteArrayOutputStream();

	public HttpUtil() {}

	/**
	 * 超时设置
	 * 
	 * @param urlConn
	 */
	private void setConPar(HttpURLConnection urlConn) {
		urlConn.setConnectTimeout(mConTimeout);
		urlConn.setReadTimeout(mSoTimeout);
	}
	
	Handler handler = new Handler(){
		public void dispatchMessage(android.os.Message msg) {
			
		};
	};
	
	public void doGetAsyn(final HttpRequest request, AsyncTaskListener listener){
		request.setAsyncTaskListener(listener);
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				doGet(request);
				Message msg = handler.obtainMessage();
				msg.obj = request;
				msg.what = 1;
				handler.sendMessage(msg);
			}
		};
		ThreadPoolManager.getInstance().addTask(runnable);
	}
	public void doPostStrAsyn(final HttpRequest request, AsyncTaskListener listener){
		request.setAsyncTaskListener(listener);
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				doPost(request);
				Message msg = handler.obtainMessage();
				msg.obj = request;
				msg.what = 1;
				handler.sendMessage(msg);
			}
		};
		ThreadPoolManager.getInstance().addTask(runnable);
	}
	public void doPostBytesAsyn(final HttpRequest request, AsyncTaskListener listener){
		request.setAsyncTaskListener(listener);
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				doPostByte(request);
				Message msg = handler.obtainMessage();
				msg.obj = request;
				msg.what = 1;
				handler.sendMessage(msg);
			}
		};
		ThreadPoolManager.getInstance().addTask(runnable);
	}

	/***
	 * 同步get方法
	 * 
	 * @param request 请求实体封装
	 * @return 返回当前请求的实体
	 */
	public HttpRequest doGet(HttpRequest request) {
		try {
			url = new URL(request.getUrl());
			mURLConnection = (HttpURLConnection) url.openConnection();
			InputStream is = mURLConnection.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String response = "";
			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				response = response + readLine;
			}
			is.close();
			br.close();
			mURLConnection.disconnect();
			request.setResStr(response);
			request.setResStatus(200);
		} catch (MalformedURLException e) {
			request.setResStatus(1001);
			request.setResStr(e.getMessage());
		} catch (IOException e) {
			request.setResStatus(1002);
			request.setResStr(e.getMessage());
		}
		return request;
	}

	/***
	 *	同步请求post，
	 * 
	 * @param request 请求实体封装
	 * @return 返回当前请求的实体
	 */
	public HttpRequest doPost(HttpRequest request) {
		try {
			url = new URL(request.getUrl());
			mURLConnection = (HttpURLConnection) url.openConnection();
			setConPar(mURLConnection);
			mURLConnection.setRequestMethod("POST");
			mURLConnection.setUseCaches(false);
			mURLConnection.setInstanceFollowRedirects(false);
			mURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			mURLConnection.setDoInput(true);
			if (!StringUtil.isEmpty(request.getParamStr())) {
				mURLConnection.setDoOutput(true);
					byte[] buf = (request.getParamStr()).getBytes(mEncoding);
					OutputStream out = mURLConnection.getOutputStream();
					out.write(buf);
					out.close();
			}
			mURLConnection.connect();
			int mStatusCode = mURLConnection.getResponseCode();
			request.setResStatus(mStatusCode);
			if (mStatusCode == HttpStatus.SC_OK) {
				InputStream inputstream = mURLConnection.getInputStream();
				resultData = new ByteArrayOutputStream();
				int ibyte;
				while (true) {
					ibyte = inputstream.read();
					if (ibyte == -1) {
						break;
					}
					resultData.write(ibyte);
				}
				inputstream.close();
				request.setResStr(new String(resultData.toByteArray(), mEncoding));
				resultData.close();
			}
			mURLConnection.disconnect();
		} catch (MalformedURLException e) {
			request.setResStatus(1001);
			request.setResStr(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			request.setResStatus(1002);
			request.setResStr(e.getMessage());
			e.printStackTrace();
		}
		return request;
	}

	/***
	 * post方法
	 * 
	 * @param postUrl
	 * @param postParam
	 * @return
	 */
	public HttpRequest doPostByte(HttpRequest request) {
		try {
			url = new URL(request.getUrl());
			mURLConnection = (HttpURLConnection) url.openConnection();
			setConPar(mURLConnection);
			mURLConnection.setRequestMethod("POST");
			mURLConnection.setUseCaches(false);
			mURLConnection.setInstanceFollowRedirects(false);
			mURLConnection.setRequestProperty("Content-Type", "multipart/form-data");
			mURLConnection.setDoInput(true);
			if (request.getParamBytes() != null) {
				mURLConnection.setDoOutput(true);
				OutputStream out = mURLConnection.getOutputStream();
				out.write(request.getParamBytes());
				out.close();
			}
			mURLConnection.connect();
			int mStatusCode = mURLConnection.getResponseCode();
			if (mStatusCode == HttpStatus.SC_OK) {
				InputStream inputstream = mURLConnection.getInputStream();
				resultData = new ByteArrayOutputStream();
				int ibyte;
				while (true) {
					ibyte = inputstream.read();
					if (ibyte == -1) {
						break;
					}
					resultData.write(ibyte);
				}
				inputstream.close();
				request.setResStr(new String(resultData.toByteArray(), mEncoding));
			}
			resultData.close();
			mURLConnection.disconnect();
		} catch (Exception e) {
			request.setResStatus(1000);
			request.setResStr(e.getMessage());
		}
		return request;
	}

	public interface AsyncTaskListener {
		public void onPostExecute(HttpRequest httpRequest);

		public void onPreExecute(HttpRequest httpRequest);
	}
}
