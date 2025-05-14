package utils.pool;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadPool implements Executor {
	private Queue<Runnable> workQueue = new ConcurrentLinkedDeque<>();
	private Thread[] threads;
	private AtomicBoolean isRunning = new AtomicBoolean(true);

	public ThreadPool(int nThreads) {
		threads = new Thread[nThreads];
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Thread(new TaskWorker());
			threads[i].start();
		}
	}

	public void shutDown() {
		isRunning.set(false);
	}

	public void joinToPool() throws InterruptedException {
		for (int i = 0; i < threads.length; i++) {
			threads[i].join();
		}
	}

	@Override
	public void execute(Runnable command) {
		if (isRunning.get()) {
			workQueue.add(command);
		} else {
			throw new RejectedExecutionException();
		}
	}

	private class TaskWorker implements Runnable {

		@Override
		public void run() {
			while (isRunning.get() || !workQueue.isEmpty()) {
				Runnable task = workQueue.poll();
				if (task != null) {
					task.run();
				}
			}

		}

	}

}
