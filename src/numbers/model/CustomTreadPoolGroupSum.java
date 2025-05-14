package numbers.model;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;


public class CustomTreadPoolGroupSum extends GroupSum {

	public CustomTreadPoolGroupSum(int[][] numberGroups) {
		super(numberGroups);
	}

	@Override
	public int computeSum() {
		int numThreads = Runtime.getRuntime().availableProcessors();
		BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
		ThreadPoolExecutor executor = new ThreadPoolExecutor( numThreads, numThreads,
				60L, TimeUnit.SECONDS, taskQueue );

		List<Future<Integer>> futures = Arrays.stream(numberGroups)
				.map(group -> executor.submit(() -> Arrays.stream(group).sum()))
				.toList();

		int totalSum = futures.stream()
				.mapToInt(future -> {
					try {
						return future.get();
					} catch (InterruptedException | ExecutionException e) {
						return 0;
					}
				})
				.sum();

		executor.shutdown();
		return totalSum;
	}

}
