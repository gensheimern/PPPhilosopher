package pp.dining.cond;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Philosopher extends Thread {
	ReentrantLock tisch = new ReentrantLock();
	Condition isst = tisch.newCondition();
	Condition denkt = tisch.newCondition();
	private Philosopher leftPhilosoph;
	private Philosopher rightPhilosoph;
	//private final Chopstick left;
	//private final Chopstick right;
	private final Random random;
	private int eaten;

	private volatile boolean stop;
	

	public void stopPhilosopher() {
		System.out.println(this.getId() + " stopping");
		this.stop = true;
		this.interrupt();
	}

	public Philosopher(Lock table) {
		this.stop = false;
		this.random = new Random();
		this.eaten = 0;
	}



	@Override
	public void run() {
		System.out.println(Thread.currentThread().getId() + " starting");
		while (!this.stop) {
			try {
				Thread.sleep(this.random.nextInt(PhilosopherExperiment.MAX_THINKING_DURATION_MS));
			} catch (final InterruptedException e) {
				// empty
			}
			try {
				Thread.sleep(this.random.nextInt(PhilosopherExperiment.MAX_TAKING_TIME_MS));
			} catch (final InterruptedException e) {
				// empty
			}
			System.out.println(Thread.currentThread().getId() + " try taking left");
			synchronized (this.left) {
				System.out.println(Thread.currentThread().getId() + " left acquired");
				try {
					Thread.sleep(this.random.nextInt(PhilosopherExperiment.MAX_TAKING_TIME_MS));
				} catch (final InterruptedException e) {
					// empty
				}
				System.out.println(Thread.currentThread().getId() + " try taking right");
				synchronized (this.right) {
					System.out.println(Thread.currentThread().getId() + " right acquired");
					System.out.println(Thread.currentThread().getId() + " eating");
					this.eaten++;
				}
				System.out.println(Thread.currentThread().getId() + " right released");
				try {
					Thread.sleep(this.random.nextInt(PhilosopherExperiment.MAX_TAKING_TIME_MS));
				} catch (final InterruptedException e) {
					// empty
				}
			}
			System.out.println(Thread.currentThread().getId() + " left released");
			try {
				Thread.sleep(this.random.nextInt(PhilosopherExperiment.MAX_TAKING_TIME_MS));
			} catch (final InterruptedException e) {
				// empty
			}
		}
		System.out.println(Thread.currentThread().getId() + " stopped; eaten=" + this.eaten);
	}

	public void setLeft(Philosopher philosopher) {
		this.leftPhilosoph = philosopher;
	}

	public void setRight(Philosopher philosopher) {
		this.rightPhilosoph = philosopher;
	}

	
}
