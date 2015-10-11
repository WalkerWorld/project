package com.walker.module.bean;

/**
 * @类名: DownloadBean
 * 
 * @描述: 记录下载相关的数据
 * 
 */
public class DownloadBean
{

	public String	downloadUrl;			// 网络下载地址
	public String	savePath;				// 存储的路径
	public long		size;					// 应用的总长度
	public String	name;					// 应用的名称
	public int		downloadState;			// 下载状态
	public String	packageName;			// 应用的包名
	public long		currenDownloadLength;	// 当前下载的长度
}
