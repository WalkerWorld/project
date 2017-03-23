/**   
* @Title: Config.java
* @Package com.walker.app
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2017年3月9日 下午9:01:57
* @version V1.0   
*/
package com.walker.app;

/** @ClassName: Config
 *
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2017年3月9日 下午9:01:57
 * 
 */
public class AppConfig {
	/**数据库存储路径*/
	private String db_path;
	/**数据库名称*/
	private String db_name = "ems.db";
	/**数据库版本号*/
	private int db_version=1;
	/**单例对象*/
	private static AppConfig config;
	/**应用是否处于开发状态：true 处于开发状态，false 应用处于线上状态*/
	private boolean isDebug = false;
	/**缓存文件存储天数:默认存储5天*/
	private int cacheDay = 5;

	private AppConfig(){}
	public static AppConfig getInstance(){
		if(config == null){
			config=new AppConfig();
		}
		return config;
	}
	public static AppConfig getConfig() {
		return getInstance();
	}
	/**
	 * Add by walker Date 2017年3月9日
	 * @Description: TODO
	 *	初始化应用配置信息
	 */
	public void init(){
		
	}
	/**获取指定数据库名称*/
	public String getDb_name() {
		return db_name;
	}
	/**设置数据库名称*/
	public void setDb_name(String db_name) {
		this.db_name = db_name;
	}
	/**获取数据库版本号*/
	public int getDb_version() {
		return db_version;
	}
	/**设置数据库版本号*/
	public void setDb_version(int db_version) {
		this.db_version = db_version;
	}
	/**获取数据库路径*/
	public String getDb_path() {
		return db_path;
	}
	/**设置数据库路径*/
	public void setDb_path(String db_path) {
		this.db_path = db_path;
	}
	/**返回是否为debug模式*/
	public boolean isDebug() {
		return isDebug;
	}
	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}
	
	public int getCacheDay() {
		return cacheDay;
	}
	public void setCacheDay(int cacheDay) {
		this.cacheDay = cacheDay;
	}
}
