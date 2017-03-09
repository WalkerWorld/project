/**   
* @Title: Table.java
* @Package com.walker.reflect
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2017年1月24日 下午11:38:48
* @version V1.0   
*/
package com.walker.jutil;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName: Table
 *
 * @Description: TODO
 *	数据库表注解声明
 * @author walker
 *
 * @date 2017年2月4日 下午12:43:10
 *
 */
@Documented
@Target (ElementType.FIELD)
@Retention (RetentionPolicy.RUNTIME)
public @interface Table {
	//字段名称注解
	public String name() default "";	
	public types type() default types.String;
	public boolean isID() default false;
	public boolean isPrimaryKey() default false;
	public enum types{String, Int, Double, Boolean}
	
}
