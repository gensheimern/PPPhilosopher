package pp.dining.cond;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PhilosopherExperiment {
	static final int MAX_THINKING_DURATION_MS = 3000;
	static final int MAX_TAKING_TIME_MS = 100;
	static final int PHILOSOPHER_NUM = 5;
	static final int EXP_DURATION_MS = 20000;
	static IPhilosopher[] philosophers = new Philosopher[PhilosopherExperiment.PHILOSOPHER_NUM];

	public static void main(final String[] args) throws InterruptedException {
		final Lock table = new ReentrantLock();
		for (int i = 0; i < PhilosopherExperiment.PHILOSOPHER_NUM; i++) {
			PhilosopherExperiment.philosophers[i] = new Philosopher();
			PhilosopherExperiment.philosophers[i].setTable(table);
		}
		PhilosopherExperiment.philosophers[0]
				.setLeft(PhilosopherExperiment.philosophers[PhilosopherExperiment.PHILOSOPHER_NUM - 1]);
		PhilosopherExperiment.philosophers[0].setRight(PhilosopherExperiment.philosophers[1]);
		for (int i = 1; i < (PhilosopherExperiment.PHILOSOPHER_NUM - 1); i++) {
			PhilosopherExperiment.philosophers[i].setLeft(PhilosopherExperiment.philosophers[i - 1]);
			PhilosopherExperiment.philosophers[i].setRight(PhilosopherExperiment.philosophers[i + 1]);
		}
		PhilosopherExperiment.philosophers[PhilosopherExperiment.PHILOSOPHER_NUM - 1]
				.setLeft(PhilosopherExperiment.philosophers[PhilosopherExperiment.PHILOSOPHER_NUM - 2]);
		PhilosopherExperiment.philosophers[PhilosopherExperiment.PHILOSOPHER_NUM - 1]
				.setRight(PhilosopherExperiment.philosophers[0]);
		for (int i = 0; i < PhilosopherExperiment.PHILOSOPHER_NUM; i++) {
			PhilosopherExperiment.philosophers[i].start();
		}
		Thread.sleep(PhilosopherExperiment.EXP_DURATION_MS);
		for (int i = 0; i < PhilosopherExperiment.PHILOSOPHER_NUM; i++) {
			PhilosopherExperiment.philosophers[i].stopPhilosopher();
		}
	}
}
