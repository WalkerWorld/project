/**   
* @Title: mainTest.java
* @Package com.walker.test
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2017年3月9日 下午6:56:36
* @version V1.0   
*/
package com.walker.test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

import com.walker.app.Config_Enum.FilePathEnum;
import com.walker.autils.FileUtils;
import com.walker.jutil.SqlUtil;
import com.walker.jutil.TestDao;

/** @ClassName: mainTest
 *
 * @Description: TODO
 *
 * @author walker
 *
 * @date 2017年3月9日 下午6:56:36
 * 
 */
public class mainTest {
	@Test
	public void test(){
		int date = getDate().getDate();
		 SimpleDateFormat dateFormat = new SimpleDateFormat("dd");  
	        long nowTime = System.currentTimeMillis();  
	        Calendar cal = Calendar.getInstance();  
		System.out.println("======" + dateFormat.format(System.currentTimeMillis()));
		System.out.println("" + FileUtils.getInstance().getPath(FilePathEnum.cache));
//		System.out.println("" + FileUtils.getInstance().getPath(FilePathEnum.database));
//		System.out.println("" + FileUtils.getInstance().getPath(FilePathEnum.download));
//		System.out.println("" + FileUtils.getInstance().getPath(FilePathEnum.icon));
//		System.out.println("" + FileUtils.getInstance().getPath(FilePathEnum.log));
	}
	public static long LOCAL_TIME = 0L;
	public static long DATABASE_TIME = 0L;

	public static Date getDate() {
		return new Date(new Date().getTime() - LOCAL_TIME + DATABASE_TIME);
	}

}
