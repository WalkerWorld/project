package com.walker.manager;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;

/**
 * @类名: ThreadPoolProxy
 * @作者: 肖琦
 * 
 * @描述: 线程池
 * 
 */
public class ThreadPoolProxy
{
	private ThreadPoolExecutor	mPoolExecutor;
	private int					mCorePoolSize	= 0;
	private int					mMaximumPoolSize	= 0;
	private long				mKeepAliveTime	= 0;

	public ThreadPoolProxy(int corePoolSize, int maximumPoolSize, long keepAliveTime) {
		this.mCorePoolSize = corePoolSize;
		this.mMaximumPoolSize = maximumPoolSize;
		this.mKeepAliveTime = keepAliveTime;
	}

	public Future<?> execute(Runnable task)
	{

		if (task == null) { return null; }

		if (mPoolExecutor == null || mPoolExecutor.isShutdown() || mPoolExecutor.isTerminated())
		{

			TimeUnit unit = TimeUnit.SECONDS;
			// BlockingQueue<Runnable> workQueue = new
			// ArrayBlockingQueue<Runnable>(capacity);//固定大小的队列，FIFO
			BlockingQueue<Runnable> workQueue = new
												LinkedBlockingQueue<Runnable>();// 不固定大小，FIFO
			// BlockingQueue<Runnable> workQueue = new
			// PriorityBlockingQueue<Runnable>();// 优先级的队列，FIFO
			// BlockingQueue<Runnable> workQueue = new
			// SynchronousQueue<Runnable>();// 同步队列，FIFO

			ThreadFactory threadFactory = Executors.defaultThreadFactory();
			// RejectedExecutionHandler handler = new
			// ThreadPoolExecutor.AbortPolicy();// 将异常抛出
			// RejectedExecutionHandler handler = new
			// ThreadPoolExecutor.CallerRunsPolicy();// 如果有异常，直接执行加入的任务
			// RejectedExecutionHandler handler = new
			// ThreadPoolExecutor.DiscardOldestPolicy();// 移除第一个任务，执行加入的任务
			RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();// 不做处理

			mPoolExecutor = new ThreadPoolExecutor(mCorePoolSize,// 工作线程的个数
												mMaximumPoolSize,// 最多有多少个工作线程
												mKeepAliveTime,//
												unit,// 时间参数，是keepAliveTime
												workQueue,// 任务队列
												threadFactory,// 线程工厂
												handler);//
		}

		// mExecutor.execute(task);

		return mPoolExecutor.submit(task);
	}

	public void remove(Runnable task)
	{
		mPoolExecutor.getQueue().remove(task);
	}


/*	private ThreadPoolExecutor mPool;
	private int mCorePoolSize;
	private int mMaximumPoolSize;
	private long mKeepAliveTime;*/

	/*private ThreadPoolProxy(int corePoolSize, int maximumPoolSize,
			long keepAliveTime) {
		mCorePoolSize = corePoolSize;
		mMaximumPoolSize = maximumPoolSize;
		mKeepAliveTime = keepAliveTime;
	}
*/
	/** 执行任务，当线程池处于关闭，将会重新创建新的线程池 */
	/*public synchronized void execute(Runnable run) {
		if (run == null) {
			return;
		}
		if (mPool == null || mPool.isShutdown()) {
			// 参数说明
			// 当线程池中的线程小于mCorePoolSize，直接创建新的线程加入线程池执行任务
			// 当线程池中的线程数目等于mCorePoolSize，将会把任务放入任务队列BlockingQueue中
			// 当BlockingQueue中的任务放满了，将会创建新的线程去执行，
			// 但是当总线程数大于mMaximumPoolSize时，将会抛出异常，交给RejectedExecutionHandler处理
			// mKeepAliveTime是线程执行完任务后，且队列中没有可以执行的任务，存活的时间，后面的参数是时间单位
			// ThreadFactory是每次创建新的线程工厂
			mPool = new ThreadPoolExecutor(mCorePoolSize, mMaximumPoolSize,
					mKeepAliveTime, TimeUnit.MILLISECONDS,
					new LinkedBlockingQueue<Runnable>(),
					Executors.defaultThreadFactory(), new AbortPolicy());
		}
		mPool.execute(run);
	}*/

	/** 取消线程池中某个还未执行的任务 */
	public synchronized void cancel(Runnable run) {
		if (mPoolExecutor != null && (!mPoolExecutor.isShutdown() || mPoolExecutor.isTerminating())) {
			mPoolExecutor.getQueue().remove(run);
		}
	}

	/** 取消线程池中某个还未执行的任务 */
	public synchronized boolean contains(Runnable run) {
		if (mPoolExecutor != null && (!mPoolExecutor.isShutdown() || mPoolExecutor.isTerminating())) {
			return mPoolExecutor.getQueue().contains(run);
		} else {
			return false;
		}
	}

	/** 立刻关闭线程池，并且正在执行的任务也将会被中断 */
	public void stop() {
		if (mPoolExecutor != null && (!mPoolExecutor.isShutdown() || mPoolExecutor.isTerminating())) {
			mPoolExecutor.shutdownNow();
		}
	}

	/** 平缓关闭单任务线程池，但是会确保所有已经加入的任务都将会被执行完毕才关闭 */
	public synchronized void shutdown() {
		if (mPoolExecutor != null && (!mPoolExecutor.isShutdown() || mPoolExecutor.isTerminating())) {
			mPoolExecutor.shutdownNow();
		}
	}

}
