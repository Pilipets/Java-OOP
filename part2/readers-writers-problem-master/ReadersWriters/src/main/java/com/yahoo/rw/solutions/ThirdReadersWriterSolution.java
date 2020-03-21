package com.yahoo.rw.solutions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

/*
 * 
 * Name : ThirdReadersWriterSolution.java
 * Author : knar@
 * Description : Overrides the reader and writer abstract methods of ReadersWriterInterface
 * Refer to src/main/resources for more information on the input and output
 * 
 */

public class ThirdReadersWriterSolution implements ReadersWriterInterface {

	private Semaphore serviceQueue;
	private Semaphore readCountAccess;
	private Semaphore write;
	private int readCount;

	private Map<String, Integer> schedules;

	public ThirdReadersWriterSolution() {
		readCount = 0;
		serviceQueue = new Semaphore(1, true);
		readCountAccess = new Semaphore(1, true);
		write = new Semaphore(1, true);
		schedules = new HashMap<String, Integer>();
		fillMapWithInitialSchedules();
	}

	public void writer(String flight, Integer time) throws InterruptedException {
		serviceQueue.acquire();
		try {
			write.acquire();

			if (serviceQueue.hasQueuedThreads()) {
				System.out
						.println("There are "
								+ serviceQueue.getQueueLength()
								+ " waiting Threads on the orderMutex semaphore during writing ");
			}
		} finally {
			serviceQueue.release();
		}
		// Writing/Updating new/existing schedules

		try {
			Thread.sleep(1000);

			setMapValue(flight, time);

			System.out.println("Set for " + flight + " time: " + time
					+ " by Writer Thread: " + Thread.currentThread().getName());

			// Writing done
		} finally {
			write.release();
		}
	}

	public void reader(String flight) throws InterruptedException {

		serviceQueue.acquire();
		readCountAccess.acquire();
		try {
			if (readCount == 0) {
				write.acquire();
			}
			readCount++;
			if (serviceQueue.hasQueuedThreads()) {
				System.out
						.println("There are "
								+ serviceQueue.getQueueLength()
								+ " waiting Threads on the orderMutex semaphore during reading ");
			}
		} finally {
			serviceQueue.release();
			readCountAccess.release();
		}

		// Read the schedules...

		Thread.sleep(1000);

		System.out.println("Schedule for " + flight + " is :"
				+ schedules.get(flight) + " read by "
				+ Thread.currentThread().getName());

		// Reading done

		readCountAccess.acquire();
		try {
			readCount--;
			if (readCount == 0) {
				write.release();
			}
		} finally {
			readCountAccess.release();
		}
	}

	public Integer getMapValue(ReadersWriterInterface r, String s) {
		return schedules.get(s);
	}

	public void setMapValue(String flght, Integer time) {
		this.schedules.put(flght, time);
	}

	public void fillMapWithInitialSchedules() {
		this.schedules.put("Flight-1", 1);
		this.schedules.put("Flight-2", 4);
		this.schedules.put("Flight-3", 6);
		this.schedules.put("Flight-4", 9);
		this.schedules.put("Flight-5", 10);
		this.schedules.put("Flight-6", 13);
		this.schedules.put("Flight-7", 15);
		this.schedules.put("Flight-8", 17);
		this.schedules.put("Flight-9", 20);
		this.schedules.put("Flight-10", 23);

	}

}
