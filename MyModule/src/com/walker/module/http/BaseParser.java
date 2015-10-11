package com.walker.module.http;

import android.os.Handler;


/**
 * 解析工具抽象类
 * @author fada
 *
 * @param <T> 返回的对象类型
 */
public abstract class BaseParser<T> {
	/**
	 * 
	 * @param str json字符串
	 * @return json对应的对象
	 */
	 public abstract T parseJSON(String str);
}
