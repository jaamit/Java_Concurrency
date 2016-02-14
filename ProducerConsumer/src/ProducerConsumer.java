
/**
 * @author jaamit
 *
 */

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;


public class ProducerConsumer {
	
	// number of items to store
	private final static int SIZE = 10;
	private final static int CONSUMERS = 3;
	private final static int PRODUCERS = 3;
	
	private List<Integer> items = new ArrayList<>(SIZE);
	private final ReentrantLock lock = new ReentrantLock();
	private final Condition hasSpaces = lock.newCondition();
	private final Condition hasItems = lock.newCondition();
		
	// Producer
	private void put(int item) {
		lock.lock();
		try{
			// check if list is FULL
			while(items.size() >= SIZE) {
				try {
					// wait for the list to have some space
					hasSpaces.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// list has space - add item to tail of list
			System.out.println("Producer: " + item);
			items.add(item);
			if(items.size() > SIZE) {
				System.err.println("Over capacity: " + items.size());					
			}
			// signal a waiting consumer that list has some items now
			hasItems.signal();
		} finally {
			lock.unlock();
		}
		//System.out.println("PUT HERE");
	}
	
	// consumer
	private Integer get() {
		lock.lock();
		try{
			// check if list is Empty before removing items
			while(items.isEmpty()) {
				try {
					// wait for list to have some items
					hasItems.await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			Integer item = null;
			try {
				// remove from head of the list
				item = items.remove(0);
				// signal to waiting producer that list has space now
				hasSpaces.signal();
			} catch(IndexOutOfBoundsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			return item;						
		} finally {
			lock.unlock();
		}

		//System.out.println("GET HERE");
	}
	
	public static void main(String[] args) {
		new ProducerConsumer().doMain();
	}
	
	private void doMain() {
		for(int i = 0; i < PRODUCERS; ++i) {
			new Thread(new Producer()).start();
		}
		for(int i = 0; i < CONSUMERS; ++i) { 
			new Thread(new Consumer()).start();
		}
		
	}
	
	private class Producer implements Runnable {		
		@Override
		public void run() {
			int i = 0;
			while(i < 10){
				put(i);
				i++;
			}			
		}
	}
	
	private class Consumer implements Runnable {
		@Override
		public void run() {
			int i = 0;
			while(i < 10) {
				System.out.println("Consumer: " + get());
				i++;
			}
		}
		
	}
}