/**
 * 
 */
package io.pratik.threadops;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.logging.Logger;

/**
 * @author pratikdas
 *
 */
public class ThreadMXBeanSample {
	private static final Logger logger = Logger.getLogger(ThreadMXBeanSample.class.getName());

	public static void main(String[] args) {
		startThreads();
		ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
		for (ThreadInfo ti : threadMxBean.dumpAllThreads(true, true)) {

			logger.info(ti.toString());
		}

		logger.info("\nGeneral Thread information");
		logger.info(("Number of live threads :" + threadMxBean.getThreadCount()));
		logger.info("Total CPU time for the current thread: " + threadMxBean.getCurrentThreadCpuTime());
		logger.info("Current number of live daemon threads:" + threadMxBean.getDaemonThreadCount());
		logger.info("Peak live thread count :" + threadMxBean.getPeakThreadCount());
		logger.info("Total number of threads created and started : " + threadMxBean.getTotalStartedThreadCount());
	}

	/**
	 * Starts two threads thread1 and thread2 and calls their synchronized methods
	 * in the run method resulting in a deadlock.
	 */
	private static void startThreads() {
		final ThreadSample thread1 = new ThreadSample();
		final ThreadSample thread2 = new ThreadSample();
		Thread t1 = new Thread("Thread1") {
			public void run() {
				thread1.executeMethod1(thread2);
			}
		};

		Thread t2 = new Thread("Thread2") {
			@Override
			public void run() {
				thread2.executeMethod2(thread1);
			}
		};

		t1.start();
		t2.start();
	}
}
