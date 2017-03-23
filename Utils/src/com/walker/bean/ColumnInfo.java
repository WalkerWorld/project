/**   
* @Title: TableInfo.java
* @Package com.walker.bean
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2017年3月21日 上午11:13:40
* @version V1.0   
*/
package com.walker.bean;

/**
 * @ClassName: TableInfo
 *	数据库表信息
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2017年3月21日 上午11:13:40
 * 
 */
public class ColumnInfo {
	String name;
	String type;
	String notnull;
	Object dflt_value;
	String pk;
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getNotnull() {
		return notnull;
	}
	
	public void setNotnull(String notnull) {
		this.notnull = notnull;
	}
	
	public Object getDflt_value() {
		return dflt_value;
	}
	
	public void setDflt_value(Object dflt_value) {
		this.dflt_value = dflt_value;
	}
	
	public String getPk() {
		return pk;
	}
	
	public void setPk(String pk) {
		this.pk = pk;
	}

	@Override
	public String toString() {
		return "ColumnInfo [name=" + name + ", type=" + type + ", notnull=" + notnull + ", dflt_value=" + dflt_value
				+ ", pk=" + pk + "]";
	}
	
}
