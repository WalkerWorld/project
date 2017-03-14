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
	private String db_name;
	private int db_version;
	static AppConfig config;
	
	
	private AppConfig(){}
	public static AppConfig getInstance(){
		if(config == null){
			config=new AppConfig();
		}
		return config;
	}
	
	
	public String getDb_name() {
		return db_name;
	}
	
	public void setDb_name(String db_name) {
		this.db_name = db_name;
	}
	
	public int getDb_version() {
		return db_version;
	}
	
	public void setDb_version(int db_version) {
		this.db_version = db_version;
	}
	
	public static AppConfig getConfig() {
		return config;
	}
	
	public static void setConfig(AppConfig config) {
		AppConfig.config = config;
	}
	
	/**
	 * Add by walker Date 2017年3月9日
	 * @Description: TODO
	 *	初始化应用配置信息
	 */
	public void init(){
		
	}
	
	public String getDb_path() {
		return db_path;
	}
	

	public void setDb_path(String db_path) {
		this.db_path = db_path;
	}
	

	
}
