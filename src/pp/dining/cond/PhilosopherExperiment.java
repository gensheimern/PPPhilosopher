package pp.dining.cond;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PhilosopherExperiment {
	static final int MAX_THINKING_DURATION_MS = 3000;
	static final int MAX_TAKING_TIME_MS = 100;
	static final int PHILOSOPHER_NUM = 5;
	static final int EXP_DURATION_MS = 20000;
	static Philosopher[] philosophers = new Philosopher[PhilosopherExperiment.PHILOSOPHER_NUM];

	public static void main(final String[] args) throws InterruptedException {
		final Lock table = new ReentrantLock();
		for (int i = 0; i < PhilosopherExperiment.PHILOSOPHER_NUM; i++) {
			philosophers[i] = new Philosopher(table);
		}
		philosophers[0].setLeft(philosophers[PhilosopherExperiment.PHILOSOPHER_NUM - 1]);
		philosophers[0].setRight(philosophers[1]);
		for (int i = 1; i < (PhilosopherExperiment.PHILOSOPHER_NUM - 1); i++) {
			philosophers[i].setLeft(philosophers[i - 1]);
			philosophers[i].setRight(philosophers[i + 1]);
		}
		philosophers[PhilosopherExperiment.PHILOSOPHER_NUM - 1]
				.setLeft(philosophers[PhilosopherExperiment.PHILOSOPHER_NUM - 2]);
		philosophers[PhilosopherExperiment.PHILOSOPHER_NUM - 1].setRight(philosophers[0]);
		for (int i = 0; i < PhilosopherExperiment.PHILOSOPHER_NUM; i++) {
			philosophers[i].start();
		}
		Thread.sleep(PhilosopherExperiment.EXP_DURATION_MS);
		for (int i = 0; i < PhilosopherExperiment.PHILOSOPHER_NUM; i++) {
			philosophers[i].stopPhilosopher();
		}
	}
}
