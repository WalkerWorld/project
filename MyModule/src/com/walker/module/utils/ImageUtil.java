package com.walker.module.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.example.mymodule.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.walker.module.BaseApplication;
import com.walker.utils.StringUtils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

/**
 * 图片工具类，对图片进行一些处理
 * 
 * @author renyangyang
 * 
 */
public class ImageUtil {

	public static Context context;
	
	/**
	 * @Description: TODO
	 *		读取资产目录下图片（drawable）
	 *  @param imageName 资产目录下图片名称
	 *  @return
	 *  Drawable 
	 *		返回资产目录下该文件名对应文件的drawable对象
	 */
	public static Drawable getDrawableFromAssets(String imageName){
		
		if(!StringUtils.isEmpty(imageName)){
			Bitmap bt = BaseApplication.getApplication().getImageFromAssetsFile(imageName);
			if(bt != null){
				return bitmapToDrawable(bt);
			}
		}
		return null;
	}
	
	/**
	 * Bitmap 转换成 byte 数组
	 * 
	 * @param b bitmap对象
	 * @return byte 数组
	 */
	public static byte[] bitmapToByte(Bitmap b) {
		if (b == null) {
			return null;
		}

		ByteArrayOutputStream o = new ByteArrayOutputStream();
		b.compress(Bitmap.CompressFormat.PNG, 100, o);
		return o.toByteArray();
	}

	/**
	 * byte 数组转换成 Bitmap
	 * 
	 * @param b byte字节数组
	 * @return byte字节数组转换成的bitmap对象
	 */
	public static Bitmap byteToBitmap(byte[] b) {
		return (b == null || b.length == 0) ? null : BitmapFactory
				.decodeByteArray(b, 0, b.length);
	
	}

	/**
	 * convert Drawable to Bitmap
	 * 
	 * @param d
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable d) {
		return d == null ? null : ((BitmapDrawable) d).getBitmap();
	}

	/**
	 * convert Bitmap to Drawable
	 * 
	 * @param b
	 * @return
	 */
	public static Drawable bitmapToDrawable(Bitmap b) {
		return b == null ? null : new BitmapDrawable(b);
	}

	/**
	 * convert Drawable to byte array
	 * 
	 * @param d
	 * @return
	 */
	public static byte[] drawableToByte(Drawable d) {
		return bitmapToByte(drawableToBitmap(d));
	}

	/**
	 * convert byte array to Drawable
	 * 
	 * @param b
	 * @return
	 */
	public static Drawable byteToDrawable(byte[] b) {
		return bitmapToDrawable(byteToBitmap(b));
	}

	/**
	 * 获取旋转后的图片
	 * 
	 * @param bmp 图片bitmap对象
	 * @param degree 旋转角度
	 * @return 旋转后的bitmap对象
	 */
	public static Bitmap postRotateBitamp(Bitmap bmp, float degree) {
		// 获得Bitmap的高和宽
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		// 产生resize后的Bitmap对象
		Matrix matrix = new Matrix();
		matrix.setRotate(degree);
		Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight,
				matrix, true);
		return resizeBmp;
	}

	/**
	 * 图片放大缩小
	 * 
	 */
	public static Bitmap postScaleBitamp(Bitmap bmp, float sx, float sy) {
		// 获得Bitmap的高和宽
		int bmpWidth = bmp.getWidth();
		int bmpHeight = bmp.getHeight();
		// System.out.println("before+w+h:::::::::::"+bmpWidth+","+bmpHeight);
		// 产生resize后的Bitmap对象
		Matrix matrix = new Matrix();
		matrix.setScale(sx, sy);
		Bitmap resizeBmp = Bitmap.createBitmap(bmp, 0, 0, bmpWidth, bmpHeight,
				matrix, true);
		// System.out.println("after+w+h:::::::::::"+resizeBmp.getWidth()+","+resizeBmp.getHeight());
		return resizeBmp;
	}

	/**
	 * 图片 亮度调整
	 * 
	 * @param bmp
	 *            huevalue亮度调整黑白
	 * @param sx
	 * @param sy
	 * @return
	 */
	public static Bitmap postColorRotateBitamp(int hueValue, int lumValue,
			Bitmap bm) {
		// 获得Bitmap的高和宽
		// System.out.println(bm.getWidth()+","+bm.getHeight()+"------before");
		Bitmap bmp = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(),
				Bitmap.Config.ARGB_8888);
		// 创建一个相同尺寸的可变的位图区,用于绘制调色后的图片
		Canvas canvas = new Canvas(bmp); // 得到画笔对象
		Paint paint = new Paint(); // 新建paint
		paint.setAntiAlias(true); // 设置抗锯齿,也即是边缘做平滑处理

		// 产生resize后的Bitmap对象
		ColorMatrix mAllMatrix = new ColorMatrix();
		ColorMatrix mLightnessMatrix = new ColorMatrix();
		ColorMatrix mSaturationMatrix = new ColorMatrix();
		ColorMatrix mHueMatrix = new ColorMatrix();

		float mHueValue = hueValue * 1.0F / 127; // 亮度
		mHueMatrix.reset();
		mHueMatrix.setScale(mHueValue, mHueValue, mHueValue, 1); // 红、绿、蓝三分量按相同的比例,最后一个参数1表示透明度不做变化，此函数详细说明参考

		float mSaturationValue = 127 * 1.0F / 127;// 饱和度
		mSaturationMatrix.reset();
		mSaturationMatrix.setSaturation(mSaturationValue);

		float mLumValue = (lumValue - 127) * 1.0F / 127 * 180; // 色相
		mLightnessMatrix.reset(); // 设为默认值
		mLightnessMatrix.setRotate(0, mLumValue); // 控制让红色区在色轮上旋转的角度
		mLightnessMatrix.setRotate(1, mLumValue); // 控制让绿红色区在色轮上旋转的角度
		mLightnessMatrix.setRotate(2, mLumValue); // 控制让蓝色区在色轮上旋转的角度

		mAllMatrix.reset();
		mAllMatrix.postConcat(mHueMatrix);
		mAllMatrix.postConcat(mSaturationMatrix); // 效果叠加
		mAllMatrix.postConcat(mLightnessMatrix); // 效果叠加

		paint.setColorFilter(new ColorMatrixColorFilter(mAllMatrix));// 设置颜色变换效果
		canvas.drawBitmap(bm, 0, 0, paint); // 将颜色变化后的图片输出到新创建的位图区
		// System.out.println(bmp.getWidth()+","+bmp.getHeight()+"------after");
		return bmp;
	}

	/**
	 * 读取资源图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 获取一个240x240的图片
	 * 
	 * @param path
	 * @return
	 */
	public static Bitmap parseHeadBitmapToLittle(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap bmp = BitmapFactory.decodeFile(path, options);

		int mWidth = 240;

		int max = options.outWidth > options.outHeight ? options.outWidth
				: options.outHeight;
		if (max > mWidth) {
			options.inSampleSize = max / mWidth;
			options.outWidth = 240;
			options.outHeight = 240;

		} else {
			options.inSampleSize = max / mWidth;
			options.outWidth = 240;
			options.outHeight = 240;
		}
		/* 这样才能真正的返回一个Bitmap给你 */
		options.inJustDecodeBounds = false;
		return getBitmapByPath(path, options);
	}

	/**
	 * 获取bitmap通过路径
	 * 
	 * @param filePath
	 * @return
	 */
	public static Bitmap getBitmapByPath(String filePath) {
		return getBitmapByPath(filePath, null);
	}

	public static Bitmap getBitmapByPath(String filePath,
			BitmapFactory.Options opts) {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			File file = new File(filePath);
			fis = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(fis, null, opts);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return bitmap;
	}

	/**
	 * 根据Uri获取文件的路径
	 * 
	 * @Title: getUriString
	 * @param uri
	 * @return
	 * @return String
	 * @date 2012-11-28 下午1:19:31
	 */
	public static String getUriString(Uri uri, ContentResolver cr) {
		String imgPath = null;
		if (uri != null) {
			String scheme = uri.getScheme();
			if (scheme.equalsIgnoreCase("file")) {
				imgPath = uri.getPath();
				return imgPath;
			} else if (scheme.equalsIgnoreCase("content")) {
				Cursor cursor = cr.query(uri, null, null, null, null);
				cursor.moveToFirst();
				imgPath = cursor.getString(1); // 图片文件路径
			}
			// String uriString = uri.toString();
			// 小米手机的适配问题，小米手机的uri以file开头，其他的手机都以content开头
			// 以content开头的uri表明图片插入数据库中了，而以file开头表示没有插入数据库
			// 所以就不能通过query来查询，否则获取的cursor会为null。
			/*
			 * if(uriString.startsWith("file")){
			 * //uri的格式为file:///mnt....,将前七个过滤掉获取路径 imgPath =
			 * uriString.substring(7, uriString.length()); return imgPath; }
			 * Cursor cursor = cr.query(uri, null, null,null, null);
			 * cursor.moveToFirst(); imgPath = cursor.getString(1); // 图片文件路径
			 */
		}
		return imgPath;
	}

	/**
	 * 写图片文件到SD卡
	 * 
	 * @throws IOException
	 */
	@SuppressLint("NewApi")
	public static void saveImageToSD(String filePath, Bitmap bitmap) {
		try {
			if (bitmap != null) {
				FileOutputStream fos = new FileOutputStream(filePath);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.PNG, 100, stream);
				byte[] bytes = stream.toByteArray();
				System.out.println("saveImageToSD图片大小：" + bytes.length + "\nbitmap大小： " + bitmap.getByteCount() );
				fos.write(bytes);
				fos.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 文件到SD卡
	 * 
	 * @throws IOException
	 */
	public static void saveFileToSD(String filePath, File file) {
		try {
			if (file != null) {
				FileOutputStream fos = new FileOutputStream(filePath);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				byte[] bytes = stream.toByteArray();
				fos.write(bytes);
				fos.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 把一个url的网络图片变成一个本地的BitMap,必须在子线程中执行
	public static Bitmap returnBitMap(String url) {
		URL myFileUrl = null;
		Bitmap bitmap = null;
		try {
			myFileUrl = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		try {
			HttpURLConnection conn = (HttpURLConnection) myFileUrl
					.openConnection();
			conn.setDoInput(true);
			conn.connect();
			InputStream is = conn.getInputStream();
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmap;
	}
	
	

	public static ImageLoader imageLoader;
	private static DisplayImageOptions options;
	private static boolean isImageLoaderInit = false;
	/**
	 * 
	 *@Description: TODO
	 *		初始化ImageLoader，在Appliacation中调用
	 *  void 
	 *
	 */
	public static void initImageLoader(){
		isImageLoaderInit = true;
		File cacheDir = StorageUtils.getOwnCacheDirectory(BaseApplication.getApplication(), "imageloader/Cache"); 
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration  
			    .Builder(BaseApplication.getApplication())  
			    .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽// Can slow ImageLoader, use it carefully (Better don't use it)/设置缓存的详细信息，最好不要设置这个  
			    .threadPoolSize(3)//线程池内加载的数量  
			    .threadPriority(Thread.NORM_PRIORITY - 2)  
			    .denyCacheImageMultipleSizesInMemory()  
			    .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024)) // You can pass your own memory cache implementation/你可以通过自己的内存缓存实现  
			    .memoryCacheSize(2 * 1024 * 1024)    
			    .discCacheSize(50 * 1024 * 1024)    
			    .discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密  
			    .tasksProcessingOrder(QueueProcessingType.LIFO)  
			    .discCache(new UnlimitedDiskCache(cacheDir))//自定义缓存路径  
			    .discCacheFileCount(100) //缓存的文件数量  
			    .defaultDisplayImageOptions(DisplayImageOptions.createSimple())  
			    .imageDownloader(new BaseImageDownloader(BaseApplication.getApplication(), 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间  
			    .writeDebugLogs() // Remove for release app  
			    .build();//开始构建  
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);
		
		options = new DisplayImageOptions.Builder()  
		 .showImageOnLoading(R.drawable.ic_error_page) //设置图片在下载期间显示的图片  
		 .showImageForEmptyUri(R.drawable.ic_error_page)//设置图片Uri为空或是错误的时候显示的图片  
		.showImageOnFail(R.drawable.ic_error_page)  //设置图片加载/解码过程中错误时候显示的图片
		.cacheInMemory(true)//设置下载的图片是否缓存在内存中  
		.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中  
		.considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
		.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示  
		.bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//  
		//设置图片的解码配置  
		//.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
		//设置图片加入缓存前，对bitmap进行设置  
		//.preProcessor(BitmapProcessor preProcessor)  
		.resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位  
		.displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少  
		.displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间  
		.build();//构建完成 
		
	}
	
	public static void showImage(ImageView iv, String uri){
		if(!isImageLoaderInit){
			initImageLoader();
		}
		imageLoader.displayImage(uri, iv, options);
	}
}
