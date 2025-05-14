package numbers;

import java.util.Random;

import numbers.model.CustomTreadPoolGroupSum;
import numbers.model.ExecutorGroupSum;
import numbers.model.GroupSum;
import numbers.model.ParallelStreamGroupSum;
import numbers.model.ThreadGroupSum;
import numbers.test.GroupSumPerfomanceTest;

public class GroupSumAppl {
	private static final int N_GROUPS = 10_000;
	private static final int NUMBER_PER_GROUP = 10_000;
	private static final int[][] arr = new int[N_GROUPS][NUMBER_PER_GROUP];
	static Random random = new Random();

	public static void main(String[] args) {
		fillArray();
		GroupSum threadGroupSum = new ThreadGroupSum(arr);
		GroupSum executorGroupSum = new ExecutorGroupSum(arr);
		GroupSum streamGroupSum = new ParallelStreamGroupSum(arr);
		GroupSum customThreadPoolGroupSum = new CustomTreadPoolGroupSum(arr);

		new GroupSumPerfomanceTest("ThreadGroupSum", threadGroupSum).runTest();
		new GroupSumPerfomanceTest("ExecutorGroupSum", executorGroupSum).runTest();
		new GroupSumPerfomanceTest("ParallelStreamGroupSum", streamGroupSum).runTest();
		new GroupSumPerfomanceTest("CustomTreadPoolGroupSum", customThreadPoolGroupSum).runTest();

	}

	private static void fillArray() {
		for (int i = 0; i < arr.length; i++) {
			for (int j = 0; j < arr[i].length; j++) {
				arr[i][j] = random.nextInt();
			}
		}

	}

}
