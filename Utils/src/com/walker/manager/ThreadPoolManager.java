package com.walker.manager;

import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池工具类
 * 
 * @author fada
 *
 */
public class ThreadPoolManager {
	private ThreadPoolManager() {
		num = Runtime.getRuntime().availableProcessors();
		/**
		 * 进行优先级处理
		 */
		Comparator<? super Runnable> comparator = new Comparator<Runnable>() {
			@Override
			public int compare(Runnable lhs, Runnable rhs) {
				return lhs.hashCode() > rhs.hashCode() ? 1 : -1;
			}
		};
		workQueue = new PriorityBlockingQueue<Runnable>(num * 100, comparator);
		executor = new ThreadPoolExecutor(num * 2, num * 2, 8, TimeUnit.SECONDS, workQueue,
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	private static ThreadPoolManager manager = new ThreadPoolManager();
	public int num;
	private ThreadPoolExecutor executor;
	private PriorityBlockingQueue<Runnable> workQueue;

	public ExecutorService getService() {
		return executor;
	}

	public synchronized static ThreadPoolManager getInstance() {

		if (manager == null) {
			manager = new ThreadPoolManager();
		}
		return manager;
	}

	/**
	 * 方法描述：关闭线程池不再接受新的任务
	 */
	public void stopReceiveTask() {
		if (!executor.isShutdown()) {
			executor.shutdown();
		}
	}

	/**
	 * 方法描述：停止所有线程,包括等待
	 */
	public void stopAllTask() {
		if (!executor.isShutdown()) {
			executor.shutdownNow();
		}
	}

	/**
	 * 添加一个新任务
	 * 
	 * @param runnable
	 *            任务的Runnable对象
	 */
	public void addTask(Runnable runnable) {
		if (executor.isShutdown()) {
			executor = new ThreadPoolExecutor(num * 2, num * 2, 8, TimeUnit.SECONDS, workQueue,
					new ThreadPoolExecutor.CallerRunsPolicy());
		}
		executor.execute(runnable);
	}
	
	/** 取消线程池中某个还未执行的任务 */
	public synchronized void cancel(Runnable run) {
		if (executor != null && (!executor.isShutdown() || executor.isTerminating())) {
			executor.getQueue().remove(run);
		}
	}
	/** 立刻关闭线程池，并且正在执行的任务也将会被中断 */
	public void stop() {
		if (executor != null && (!executor.isShutdown() || executor.isTerminating())) {
			executor.shutdownNow();
		}
	}

	/** 平缓关闭单任务线程池，但是会确保所有已经加入的任务都将会被执行完毕才关闭 */
	public synchronized void shutdown() {
		if (executor != null && (!executor.isShutdown() || executor.isTerminating())) {
			executor.shutdownNow();
		}
	}
}
