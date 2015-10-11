package com.walker.module.constant;

/**
 * @类名: Constants
 * @描述: 全局常量
 * 
 */
public interface Constants
{

	/**
	 * 服务器地址
	 */
	String	SERVER_URL			= "http://10.0.2.2:8080/GooglePlayServer/";

	/**
	 * 图片基本访问地址
	 */
	String	BASE_IMAGE_URL		= SERVER_URL + "image?name=";

	String	BASE_DOWNLOAD_URL	= SERVER_URL + "download";

	int		PAGE_SIZE			= 20;
	
//	public static final String SERVER_URL="http://http://192.168.56.1:8080/RedBabyServer/";
	/**
	 * 联网失败
	 */
	public static final int NET_FAILED=0;
	/**
	 * 联网成功
	 */
	public static final int NET_SUCCESS=1;
	/**
	 * 获取数据成功
	 */
	public static final int SUCCESS=200;
	/**
	 * 获取数据失败
	 */
	public static final int FAILED=-1;
	/**
	 * 打印Log增加标记字符串
	 */
	public static final String LOG_FLAG = "*********************########当前工程错误#######**********************\n";

}
