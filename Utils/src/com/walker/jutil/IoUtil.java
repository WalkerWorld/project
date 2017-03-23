/**   
* @Title: IoUtil.java
* @Package com.walker.jutil
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2017年3月20日 下午1:27:05
* @version V1.0   
*/
package com.walker.jutil;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/** @ClassName: IoUtil
 *
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2017年3月20日 下午1:27:05
 * 
 */
public class IoUtil {
	/**
	 * Add by walker Date 2017年3月20日
	 * @Description: TODO
	 * 关闭读取流 
	 *  @param reader
	 */
	public static void close(Reader reader){
		try {
			if(reader != null && reader.ready()){
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Add by walker Date 2017年3月20日
	 * @Description: TODO
	 * 关闭写入流 
	 *  @param writer 写入流
	 */
	public static void close(Writer writer){
		try {
			if(writer != null){
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
