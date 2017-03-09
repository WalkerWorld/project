/**   
* @Title: Memory.java
* @Package com.walker.bean
* @Description: TODO
* @author A18ccms A18ccms_gmail_com   
* @date 2015年11月18日 下午3:11:18
* @version V1.0   
*/
package com.walker.bean;

/** @ClassName: Memory
 *
 * @Description: TODO
 *		手机存储情况实体类
 * @author walker
 *
 * @date 2015年11月18日 下午3:11:18
 * 
 */
public class PhoneMemory {
	/**手机外部存储空间： SD卡*/
	public long SDCarTotalSize;
	/**手机外部可用存储空间： SD卡*/
	public long SDCarAvailableSize;
	/**手机内部存储空间总大小*/
	public long ROMTotalSize;
	/**手机内部可用存储空间大小*/
	public long ROMAvailableSize;
	/**手机剩余存储空间*/
	public long MemoryAvailable;
	/**手机总内存*/
	public long MemoryTotal;
	
	static PhoneMemory phoneMemory;
	
	private PhoneMemory() {}
	
	public static PhoneMemory getInstance(){
		if(phoneMemory == null)
			phoneMemory = new PhoneMemory();
		return phoneMemory;
	}

	@Override
	public String toString() {
		return "PhoneMemory [SDCarTotalSize=" + SDCarTotalSize + ", SDCarAvailableSize=" + SDCarAvailableSize
				+ ", ROMTotalSize=" + ROMTotalSize + ", ROMAvailableSize=" + ROMAvailableSize + ", MemoryAvailable="
				+ MemoryAvailable + ", MemoryTotal=" + MemoryTotal + "]";
	}
	
}
