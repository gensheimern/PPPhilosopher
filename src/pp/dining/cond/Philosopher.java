package pp.dining.cond;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Philosopher extends Thread {

	Lock tablex = new ReentrantLock();

	Condition isst = this.tablex.newCondition();
	boolean isstBoolean;

	Condition denkt = this.tablex.newCondition();
	boolean denktBoolean;

	private Philosopher left;
	private Philosopher right;
	private final Random random;
	private int eaten;

	private volatile boolean stop;

	public void stopPhilosopher() {
		System.out.println(this.getId() - 10 + " stopping");
		this.stop = true;
		this.interrupt();
	}

	public Philosopher() {
		this.stop = false;
		this.random = new Random();
		this.eaten = 0;
	}

	@Override
	public void run() {

		System.out.println(Thread.currentThread().getId() - 10 + " starting");

		while (!this.stop) {

			try {
				this.denktBoolean = true;
				Thread.sleep(this.random.nextInt(PhilosopherExperiment.MAX_THINKING_DURATION_MS));
				System.out.println(Thread.currentThread().getId() - 10 + " : MAX_THINKIND_DURATION");
			} catch (final InterruptedException e) {
				e.printStackTrace();
			} finally {
				this.denktBoolean = false;
			}

			try {
				Thread.sleep(this.random.nextInt(PhilosopherExperiment.MAX_TAKING_TIME_MS));
				System.out.println(Thread.currentThread().getId() - 10 + " : MAX_TAKING_TIME_MS");
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}

			// Thread is locking the table to eat. The Thread has to check if
			// the left or right space is available and no other Thread
			// is eating already
			System.out.println(Thread.currentThread().getId() - 10 + " : GETTING LOCK, PREPARES TO EAT");
			tablex.lock();

			/*
			 * Entry into the critical section. Here is checked if the right
			 * seat neighbor eats. In case of yes -> the actually running Thread
			 * has to wait
			 */
			try {

				System.out.println(Thread.currentThread().getId() - 10 + " : TRY TO TAKE THE LEFT SPACE");

				while (this.left.isstBoolean) {
					System.out.println(Thread.currentThread().getId() - 10 + " : IS EATING");
					this.isstBoolean = false;
					this.denktBoolean = false;
					isst.await();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				System.out.println(Thread.currentThread().getId() - 10 + " : PUTS THE LOCK BACK FOR NEXT FREE THREAD");
				this.isstBoolean = false;

				// to unlock the table. Must be in the finally Block to get
				// sure, that in both cases of the try-catch Block, the table
				// lock
				// are getting unlock for usage of the next free Thread.
				tablex.unlock();

			}

			System.out.println(Thread.currentThread().getId() - 10 + " : AQUIRED THE FREE LEFT SPACE");
			try {

				System.out.println(Thread.currentThread().getId() - 10 + " : MAX_TAKING_TIME_MS");
				Thread.sleep(this.random.nextInt(PhilosopherExperiment.MAX_TAKING_TIME_MS));

			} catch (final InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println(Thread.currentThread().getId() - 10 + " : GETTING LOCK, PREPARES TO EAT");
			tablex.lock();

			/*
			 * Entry into the critical section. Here is checked if the right
			 * seat neighbor eats. In case of yes -> the actually running Thread
			 * has to wait
			 */
			try {

				System.out.println(Thread.currentThread().getId() - 10 + " : TRY TO TAKE THE RIGHT SPACE");
				while (this.right.isstBoolean) {

					System.out.println(Thread.currentThread().getId() - 10 + " : IS EATING");
					this.isstBoolean = false;
					this.denktBoolean = false;
					isst.await();

				}
				this.isstBoolean = true;
				this.eaten++;

			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				System.out.println(Thread.currentThread().getId() - 10 + " : PUTS THE LOCK BACK FOR NEXT FREE THREAD");
				this.isstBoolean = false;
				tablex.unlock();
			}
			System.out.println(Thread.currentThread().getId() - 10 + " : AQUIRED THE FREE RIGHT SPACE");

			// is releasing the right Thread an after that, the left Thread
			System.out.println(Thread.currentThread().getId() - 10 + " : RIGHT THREAD IS RELEASED");
			try {

				System.out.println(Thread.currentThread().getId() - 10 + " : MAX_TAKING_TIME_MS");
				Thread.sleep(this.random.nextInt(PhilosopherExperiment.MAX_TAKING_TIME_MS));
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}

			System.out.println(Thread.currentThread().getId() - 10 + " : LEFT THREAD IS REALEASED");
			try {

				System.out.println(Thread.currentThread().getId() - 10 + " : MAX_TAKING_TIME_MS");
				Thread.sleep(this.random.nextInt(PhilosopherExperiment.MAX_TAKING_TIME_MS));

			} catch (final InterruptedException e) {
				e.printStackTrace();
			}

		}

		// if the variable for eating is true, the Thread will get wake up all
		// other Threads.
		if (this.isstBoolean == true) {

			System.out.println(Thread.currentThread().getId() - 10 + " : WAKING ALL THREADS UP WITH SIGNALLALL()");
			isst.signalAll();
		}

		System.out.println(Thread.currentThread().getId() - 10 + " STOPPED; THREAD EATS=" + this.eaten);

	}

	public void setLeft(Philosopher left) {
		this.left = left;
	}

	public void setRight(Philosopher right) {
		this.right = right;
	}

	public void setTable(Lock table) {
		this.tablex = table;
	}

}
