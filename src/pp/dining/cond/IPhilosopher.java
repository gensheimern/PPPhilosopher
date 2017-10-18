package pp.dining.cond;

import java.util.concurrent.locks.Lock;

public interface IPhilosopher {

	void run();

	void setLeft(IPhilosopher left);

	void setRight(IPhilosopher right);

	void setTable(Lock table);

	void start();

	void stopPhilosopher();

}