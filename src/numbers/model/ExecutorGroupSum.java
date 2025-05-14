package numbers.model;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class ExecutorGroupSum extends GroupSum {

	public ExecutorGroupSum(int[][] numberGroups) {
		super(numberGroups);
	}

	@Override
	public int computeSum() {
		int numThreads = Runtime.getRuntime().availableProcessors();
		ExecutorService executor = Executors.newFixedThreadPool(numThreads);

		try {
			List<Future<Integer>> futures = Arrays.stream(numberGroups)
					.map(group -> executor.submit(() -> Arrays.stream(group).sum()))
					.toList();

			return futures.stream()
					.mapToInt(future -> {
						try {
							return future.get();
						} catch (InterruptedException | ExecutionException e) {
							return 0;
						}
					})
					.sum();
		} finally {
			executor.shutdown();
		}
	}
}
