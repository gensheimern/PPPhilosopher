package pp.dining.cond;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Philosopher extends Thread{
	Lock table = new ReentrantLock();
	Condition isst = table.newCondition();
	Condition denkt = table.newCondition();
	private Philosopher left;
	private Philosopher right;
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
//			synchronized (this.table) {
			/*
			 * Eintritt in den Kritischen Abschnitt.
			 * Hier wird überprüft ob der link Sitznachbar isst.
			 * Falls ja ->> Soll das Aktuelle Objekt "warten".
			 */
			table.lock();
				try {
					while(left.stop= false){
						isst.await();
					}
				} catch (InterruptedException e) {
					System.out.println("Exception1");
					e.printStackTrace();
				}finally{
					table.unlock();
				}
				System.out.println(Thread.currentThread().getId() + " left acquired");
				try {
					Thread.sleep(this.random.nextInt(PhilosopherExperiment.MAX_TAKING_TIME_MS));
				} catch (final InterruptedException e) {
					// empty
				}
				System.out.println(Thread.currentThread().getId() + " try taking right");
				/*
				 * Eintritt in den Kritischen Abschnitt.
				 * Hier wird überprüft ob der rechte Sitznachbar isst.
				 * Falls ja ->> Soll das Aktuelle Objekt "warten".
				 */
				table.lock();
				try{
					while(right.stop=false){
						isst.await();
					}
				} catch (InterruptedException e) {
					System.out.println("Exception2");
					e.printStackTrace();
				}finally{
					table.unlock();
				}
//				synchronized (this.right) {
					System.out.println(Thread.currentThread().getId() + " right acquired");
					System.out.println(Thread.currentThread().getId() + " eating");
					this.eaten++;
//				}
				System.out.println(Thread.currentThread().getId() + " right released");
				try {
					Thread.sleep(this.random.nextInt(PhilosopherExperiment.MAX_TAKING_TIME_MS));
				} catch (final InterruptedException e) {
					// empty
				}
//			}
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
		this.left = philosopher;
	}

	public void setRight(Philosopher philosopher) {
		this.right = philosopher;
	}
	public void setTable(Lock table){
		this.table = table;
	}

	
}
