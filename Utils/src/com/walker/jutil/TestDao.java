/**   
* @Title: TestDao.java
* @Package com.walker.reflect
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2017年1月24日 下午11:41:49
* @version V1.0   
*/
package com.walker.jutil;

import com.walker.jutil.Table.types;

/**
 * @ClassName: TestDao
 *	测试用实体类
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2017年2月4日 下午12:44:00
 *
 */
public class TestDao extends Object{
	@Table(isID=false)
	public String s;
	@Table(name="msgi", type = types.Boolean)
	private String msg;
	@Table(name = "ID", type = types.Double)
	 String id;
	public String getMsg() {
		return msg;
	}
	
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	
	
}
