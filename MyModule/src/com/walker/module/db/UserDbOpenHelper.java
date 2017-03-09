/**   
* @Title: UserDbOpenHelper.java
* @Package com.walker.module.db
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2015年9月14日 下午10:06:37
* @version V1.0   
*/
package com.walker.module.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @ClassName: UserDbOpenHelper
 *
 * @Description: TODO
 *		创建指定数据库文件
 * @author walker
 *
 * @date 2015年9月14日 下午10:06:37
 * 
 */
public class UserDbOpenHelper extends SQLiteOpenHelper {

	/**
	 * @param context 上下文环境：应用包名、路径
	 * @param name  数据库文件名称
	 * @param factory 游标
	 * @param version 数据库版本号 从1开始
	 */
	public UserDbOpenHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		System.out.println("初始化Openhelper实例： " + name);
	}

	/* (非 Javadoc)
	 * Description: 数据库第一次创建时调用
	 * @param db
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("数据库被创建： " + db.getPath());
	}

	/* (非 Javadoc)
	 * Description: 数据库版本更新时调用方法
	 * @param db
	 * @param oldVersion
	 * @param newVersion
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		System.out.println("数据库更新： " + db.getPath());
	}

}
