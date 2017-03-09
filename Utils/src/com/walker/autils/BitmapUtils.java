package com.walker.autils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.walker.constant.ConstantFlag;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;


public class BitmapUtils {
	
	public static final int RAW_SIZE = 128;

	private static int sWidth = 128;
	private static int sHeight = 128;

	public static void setSize(int width, int height) {
		int size = width > height ? width : height;
		sWidth = size;
		sHeight = size;
	}

	public static void reset() {
		sWidth = RAW_SIZE;
		sHeight = RAW_SIZE;
	}

	/**
	 * 将字节流转换成Bitmap
	 * @param coverByte bitmap字节流
	 * @return 成功返回bitmap对象，失败返回null
	 */
	public static Bitmap getBitmap(byte[] coverByte) {
		try {
			if (coverByte == null)
				return null;

			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(coverByte, 0, coverByte.length, opts);
			opts.inSampleSize = BitmapUtils.computeSampleSize(opts, -1, sWidth
					* sHeight);

			opts.inJustDecodeBounds = false;
			return BitmapFactory.decodeByteArray(coverByte, 0,
					coverByte.length, opts);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 从输入流获取Bitmap对象
	 * @param is bitmap输入流
	 * @return 成功返回bitmap对象，失败返回null
	 */
	public static Bitmap getBitmap(InputStream is) {
		try {
			if (is == null)
				return null;

			int size = is.available();
			byte[] bs = new byte[size];
			is.read(bs, 0, bs.length);

			return getBitmap(bs);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}


	
	public static int computeSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength,
				maxNumOfPixels);
		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

	private static int computeInitialSampleSize(BitmapFactory.Options options,
			int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;

		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
				.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
				Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;

		}
	}

	/**
	 * 默认以jpg格式保存图片到指定路径
	 * 
	 * @param bm
	 * @param path
	 * @return
	 */
	public static boolean saveFile(Bitmap bm, String path) {
		if (bm == null || path == null)
			
			return false;
		File myCaptureFile = new File(path);
		if (myCaptureFile.exists()) {
			myCaptureFile.delete();
		}
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(myCaptureFile));
			bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 以指定格式保存图片到指定路径
	 * 
	 * @param bm 被保存的bitmap对象
	 * @param path 保存路径
	 * @param format 
	 * @return 存储成功返回true  
	 */
	public static boolean saveFile(Bitmap bm, String path,
			Bitmap.CompressFormat format) {
		if (bm == null || path == null)
			return false;
		File myCaptureFile = new File(path);
		if (myCaptureFile.exists()) {
			myCaptureFile.delete();
		}
		try {
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(myCaptureFile));
			bm.compress(format, 80, bos);
			bos.flush();
			bos.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 *  Bitmap转byte[]
	 * @param bm Bitmap对象
	 * @param format 图片格式
	 * @return 转换成功返回byte数组，否则返回null
	 */
	public static byte[] Bitmap2Bytes(Bitmap bm, Bitmap.CompressFormat format) {
		if (bm == null) {
			return null;
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(format, 100, baos);
		return baos.toByteArray();
	}

	/*
	 *  byte[]转Bitmap
	 */
	public static Bitmap Bytes2Bimap(byte[] b) {
		if (b.length != 0) {
			return BitmapFactory.decodeByteArray(b, 0, b.length);
		} else {
			return null;
		}
	}

	/**
	 * 转换成圆角bitmap
	 * @param bitmap 被转换图片
	 * @param round 转换后图片的角度
	 * @return
	 */
	public static Bitmap toRoundBitmap(Bitmap bitmap, int round) {
		if (bitmap == null)
			return null;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		float roundPx;
		float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
		if (width <= height) {
			if (round != 0)
				roundPx = round;
			else
				roundPx = width / 2;
			top = 0;
			bottom = width;
			left = 0;
			right = width;
			height = width;
			dst_left = 0;
			dst_top = 0;
			dst_right = width;
			dst_bottom = width;
		} else {
			if (round != 0)
				roundPx = round;
			else
				roundPx = height / 2;
			float clip = (width - height) / 2;
			left = clip;
			right = width - clip;
			top = 0;
			bottom = height;
			width = height;
			dst_left = 0;
			dst_top = 0;
			dst_right = height;
			dst_bottom = height;
		}

		Bitmap output = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();

		final Rect src = new Rect((int) left, (int) top, (int) right,
				(int) bottom);
		final Rect dst = new Rect((int) dst_left, (int) dst_top,
				(int) dst_right, (int) dst_bottom);
		final RectF rectF = new RectF(dst);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, src, dst, paint);
		return output;
	}

	/**
	 * 将图片压缩为指定宽高,压缩后的图片有可能变形
	 * @param bitMap 被压缩图片对象
	 * @param w 压缩后宽度
	 * @param h 压缩后高度
	 * @return 返回压缩后的图片
	 */
	public static Bitmap getBitmap(Bitmap bitMap, int w, int h) {
		int width = bitMap.getWidth();
		int height = bitMap.getHeight();
		// 设置想要的大小
		int newWidth = w;
		int newHeight = h;
		// 计算缩放比例
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		// 得到新的图片
		bitMap = Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
		
		return bitMap;
	}

	/**
	 * 按照屏幕缩放图片
	 * @param bitmap 图片bitmap对象
	 * @param screenWidth 手机屏幕宽度
	 * @param screenHeight 手机屏幕高度
	 * @return
	 */
	public static Bitmap scaleBitmap(Bitmap bitmap, int screenWidth, int screenHeight){
		
		float width = bitmap.getWidth();
		float height = bitmap.getHeight();
		
		float rlWidth = 0f;
		float rlHeight = 0f;
		// width/height = screenWidth/screenHeight
		
		if(width > screenWidth && height > screenHeight){//图片宽高全部超出屏幕尺寸
			if(width > height){//确保图片在手机屏幕中全部可见
				rlWidth = screenWidth;
				rlHeight = height*screenWidth/width;
				
				return getBitmap(bitmap, (int)rlWidth, (int)rlHeight);
			}else{
				rlHeight = screenHeight;
				rlWidth = width/height*rlHeight;
				
				return getBitmap(bitmap, (int)rlWidth, (int)rlHeight);
			}
		}else if(width > screenWidth){//仅图片宽度大于屏幕
			rlWidth = screenWidth;
			rlHeight = height*screenWidth/width;
			
			return getBitmap(bitmap, (int)rlWidth, (int)rlHeight);
		}else if(height > screenHeight){//仅图片高度大于屏幕
			rlHeight = screenHeight;
			rlWidth = width/height*rlHeight;
			
			return getBitmap(bitmap, (int)rlWidth, (int)rlHeight);
		}
		
		return bitmap;
	}
	
	/**
	 * 
	 * @Title: scaleBitmap1
	 *
	 * @Description: TODO
	 *	按照宽度或高度获取压缩后的图片bitmap对象
	 * @param @param bitmap 被压缩图拍呢对象
	 * @param @param width 压缩宽度（为NONE则代表按照高度压缩）
	 * @param @param height 压缩后高度（为NONE）
	 * @param @return
	 * @return Bitmap 
	 * @throws
	 */
	public Bitmap scaleBitmap1(Bitmap bitmap, int width, int height){
		float bWidth = bitmap.getWidth();
		float bHeight = bitmap.getHeight();
		
		float rlWidth = 0f;
		float rlHeight = 0f;
		
		if(bitmap == null){
			return bitmap;
		}
		
		if(width == ConstantFlag.NONE && height > 0){
			rlWidth = bWidth/height*height;
			return getBitmap(bitmap, (int)(bWidth*height/bHeight), height);
		}else if(height == ConstantFlag.NONE && width > 0){
			return getBitmap(bitmap, width, (int)(bWidth*width/bHeight));
		}
		return bitmap;
	}
}
