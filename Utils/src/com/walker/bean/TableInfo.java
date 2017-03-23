/**   
* @Title: TableInfo.java
* @Package com.walker.bean
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2017年3月21日 上午11:18:29
* @version V1.0   
*/
package com.walker.bean;

import java.util.ArrayList;

/** @ClassName: TableInfo
 *
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2017年3月21日 上午11:18:29
 * 
 */
public class TableInfo {
	/**类型：表（table）；索引（index）*/
	String type;
	/**对应表名称或索引名称*/
	String name;
	/**索引所属表的名称*/
	String tbl_name;
	String rootpage;
	/**建表语句*/
	String sql;
	/**包含字段信息集合*/
	ArrayList<ColumnInfo> columnInfos;
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getTbl_name() {
		return tbl_name;
	}
	
	public void setTbl_name(String tbl_name) {
		this.tbl_name = tbl_name;
	}
	
	public String getRootpage() {
		return rootpage;
	}
	
	public void setRootpage(String rootpage) {
		this.rootpage = rootpage;
	}
	
	public String getSql() {
		return sql;
	}
	
	public void setSql(String sql) {
		this.sql = sql;
	}

	public ArrayList<ColumnInfo> getColumnInfos() {
		return columnInfos;
	}
	

	public void setColumnInfos(ArrayList<ColumnInfo> columnInfos) {
		this.columnInfos = columnInfos;
	}
	

	@Override
	public String toString() {
		return "TableInfo [type=" + type + ", name=" + name + ", tbl_name=" + tbl_name + ", rootpage=" + rootpage
				+ ", sql=" + sql + ", columnInfos=" + columnInfos + "]";
	}
	
}
