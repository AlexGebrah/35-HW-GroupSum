package numbers.model;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class ThreadGroupSum extends GroupSum {

	public ThreadGroupSum(int[][] numberGroups) {
		super(numberGroups);
	}

	@Override
	public int computeSum() {
		int[] pSums = new int[numberGroups.length];

		Thread[] threads = IntStream.range(0, numberGroups.length)
				.mapToObj(index -> new Thread(() -> {
					pSums[index] = Arrays.stream(numberGroups[index]).sum();
				}))
				.peek(Thread::start)
				.toArray(Thread[]::new);

		Stream.of(threads).forEach(thread -> {
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		return Arrays.stream(pSums).sum();
	}

}
