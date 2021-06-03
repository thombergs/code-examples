/**
 * 
 */
package io.pratik.threadops;

import java.util.logging.Logger;

/**
 * @author pratikdas
 *
 */
public class ThreadSample {
	private static final Logger logger = Logger.getLogger(ThreadSample.class.getName());

	synchronized void executeMethod1(final ThreadSample thread) {
		log("Thread " + Thread.currentThread().getName() + " is executing");
		thread.executeMethod2(this);
		log(Thread.currentThread().getName() + " has finished method execution");
	}

	synchronized void executeMethod2(final ThreadSample thread) {
		log("Thread " + Thread.currentThread().getName() + " is executing");
		thread.executeMethod1(this);
		log(Thread.currentThread().getName() + " has finished method execution");
	}
	
	private void log(final String message) {
		System.out.println(message);
		//logger.info(message);
	}

}
