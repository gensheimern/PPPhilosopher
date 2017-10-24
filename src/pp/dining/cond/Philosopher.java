package pp.dining.cond;

import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Philosopher extends Thread {

	Lock tablex = new ReentrantLock() ;

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
		System.out.println(this.getId()-10 + " stopping");
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

		System.out.println(Thread.currentThread().getId()-10 + " starting");

		while (!this.stop) {
		
//			Think Abschnitt
			try {
				this.denktBoolean = true;
				Thread.sleep(this.random.nextInt(PhilosopherExperiment.MAX_THINKING_DURATION_MS));
				System.out.println(Thread.currentThread().getId()-10+" ....MAX_Think");
			} catch (final InterruptedException e) {
				// empty
			}finally{
				this.denktBoolean = false;
			}

			try {
				Thread.sleep(this.random.nextInt(PhilosopherExperiment.MAX_TAKING_TIME_MS));
				System.out.println(Thread.currentThread().getId()-10+" ....MAX_Take");
			} catch (final InterruptedException e) {
				// empty
			}

			System.out.println(Thread.currentThread().getId()-10 + " try taking left");
//			Ende von think
			
			
			//			synchronized (this.table) {
			/*
			 * Eintritt in den Kritischen Abschnitt.
			 * Hier wird überprüft ob der link Sitznachbar isst.
			 * Falls ja ->> Soll das Aktuelle Objekt "warten".
			 */
			System.out.println(Thread.currentThread().getId()-10+" ....getting lock 1");
			
			
			tablex.lock();
			
			
			try {
//				changed HERE
				while(this.left.isstBoolean){
					System.out.println(Thread.currentThread().getId()-10+" ....getting isst.await -> sleep");
					this.isstBoolean = false;
					this.denktBoolean = false;
					isst.await();
				}
			} catch (InterruptedException e) {
				System.out.println("Exception1");
				e.printStackTrace();
			}finally{
				System.out.println(Thread.currentThread().getId()-10+" ....  giving lock back -> finally-Block");
				this.isstBoolean = false;
				tablex.unlock();
			}


			System.out.println(Thread.currentThread().getId()-10 + " left acquired");

			//				try2
			try {
				System.out.println(Thread.currentThread().getId()-10+" ....  taking time in try 2");
				Thread.sleep(this.random.nextInt(PhilosopherExperiment.MAX_TAKING_TIME_MS));
			} catch (final InterruptedException e) {
				// empty
			}

			System.out.println(Thread.currentThread().getId()-10+ " try taking right");
			/*
			 * Eintritt in den Kritischen Abschnitt.
			 * Hier wird überprüft ob der rechte Sitznachbar isst.
			 * Falls ja ->> Soll das Aktuelle Objekt "warten".
			 */
			System.out.println(Thread.currentThread().getId()-10+" ....  getting lock 2");
			
			
			tablex.lock();
		
			try{
				while(this.right.isstBoolean){
					System.out.println(Thread.currentThread().getId()-10+" .... going essen await 2 in right.Alive");
					this.isstBoolean = false;
					this.denktBoolean = false;
					isst.await();
				}
				
				System.out.println(Thread.currentThread().getId()-10 + " right acquired");
				System.out.println(Thread.currentThread().getId()-10 + " eating");
				this.isstBoolean = true;

				this.eaten++;
				
			} catch (InterruptedException e) {
				System.out.println("Exception2");
				e.printStackTrace();
			}finally{
				System.out.println(Thread.currentThread().getId()-10+" .... giving lock 2 back in finally");
				this.isstBoolean = false;
				tablex.unlock();
			}
			//				synchronized (this.right) {
			
			//				}
			System.out.println(Thread.currentThread().getId()-10 + " right released");
			try {
				//					max_taking 2
				System.out.println(Thread.currentThread().getId()-10+" .... sleeping bei max_taking 2");
				Thread.sleep(this.random.nextInt(PhilosopherExperiment.MAX_TAKING_TIME_MS));
			} catch (final InterruptedException e) {
				// empty
			}
			//			}
			System.out.println(Thread.currentThread().getId()-10 + " left released");
			try {
				//				max_taking 3
				System.out.println(Thread.currentThread().getId()-10+" .... sleeping bei max_taking 3");
				Thread.sleep(this.random.nextInt(PhilosopherExperiment.MAX_TAKING_TIME_MS));
			} catch (final InterruptedException e) {
				System.out.println("Exception");
				e.printStackTrace();
			}

		}
		
//		if(this.left.denktBoolean){
//			if(this.right.denktBoolean){
//				try {
//					System.out.println("LAST TIME SLEEPING");
//					isst.await();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
//		System.out.println("LAST STEPP FPR SIGNAL ALL");
//		isst.signal();
		
		if(this.isstBoolean == true){
			System.out.println(Thread.currentThread().getId()-10+" ....using isst.signalAll()");
			isst.signalAll();
		}
		
		System.out.println(Thread.currentThread().getId()-10 + " stopped; eaten=" + this.eaten);

	}

	public void setLeft(Philosopher left) {
		this.left = left;
	}

	public void setRight(Philosopher right) {
		this.right =  right;
	}
	public void setTable(Lock table){
		this.tablex = table;
	}




}
