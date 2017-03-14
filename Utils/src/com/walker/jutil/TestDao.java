/**   
* @Title: TestDao.java
* @Package com.walker.reflect
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2017年1月24日 下午11:41:49
* @version V1.0   
*/
package com.walker.jutil;

import com.walker.jutil.Column.columnType;

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
@Table(tableName="walker")
public class TestDao extends Object {
	@Column(columnType = columnType.TEXT)
	public String s;
	@Column(columnName = "msgi")
	private String msg;
	@Column
	int id;
	@Column
	boolean boolea;
	@Column
	Boolean boo;
	@Column(columnName="uselevel")
	int useLevel;
	@Column
	short shor;
	@Column
	Short sh;
	@Column
	long lon;
	@Column
	Long lo;
	@Column
	byte byt;
	@Column
	Byte by;
	@Column
	float floa;
	@Column
	Float fl;
	@Column
	double doub;
	@Column
	Double dou;
	@Column
	char ch;
	@Column
	Character chara;
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
