/**
 * Java program to print Odd & Even integer numbers using 2 threads.
 * Odd thread & Even threads are scheduled correctly so that the output
 * is prodcued correctly as follows:-
 * 
 * Odd Thread - 1
 * Even Thread - 2
 * Odd Thread - 3
 * Even Thread - 4 and so on..

 * @author jaamit 02/10/2016
 *
 */
import java.util.concurrent.Semaphore;


public class EvenOdd {
	private final static String ODD = "ODD";
	private final static String EVEN = "EVEN";
	private final static int MAX_ITERATIONS = 10;
	
	public static class EvenOddThread implements Runnable {		
		private String mType;
		private int  mNum;
		private Semaphore mMySema;
		private Semaphore mOtherSema;
		
		public EvenOddThread(String str, Semaphore mine, Semaphore other) {
			mType = str;
			mMySema = mine;//new Semaphore(1); // start out as unlocked
			mOtherSema = other;//new Semaphore(0);
			if(str.equals(ODD)) {
				mNum = 1;
			}
			else {
				mNum = 2;
			}
		}
		
		@Override
		public void run() {			
				
				for (int i = 0; i < MAX_ITERATIONS; i++) {
					mMySema.acquireUninterruptibly();
					if (mType.equals(ODD)) {
						System.out.println("Odd Thread - " + mNum);
					} else {
						System.out.println("Even Thread - " + mNum);
					}
					mNum += 2;
					mOtherSema.release();
				}			
		}
		
}
		
		public static void main(String[] args) throws InterruptedException {
			Semaphore odd = new Semaphore(1);
			Semaphore even = new Semaphore(0);
			
			System.out.println("Start!!!");
			System.out.println();
			
			Thread tOdd = new Thread(new EvenOddThread(ODD, 
					                 odd, 
					                 even));
			Thread tEven = new Thread(new EvenOddThread(EVEN, 
					                 even, 
					                 odd));
			
			tOdd.start();
			tEven.start();
			
			tOdd.join();
			tEven.join();
			
			System.out.println();
			System.out.println("Done!!!");
		}
		
		
	
	
}
	/*
	@Override
	public void run() {
		String str = Thread.getName();
		System.out.println("Hello main");
	}
	public static void main(String[] args) {
		for(int i = 0; i<10;i++) {
			System.out.println();
		System.out.println("Hello Thread");
		
		//Thread t = new Thread(new EvenOdd());
		new Thread(
				new EvenOdd(), "EVEN").start();
		new Thread(
				new EvenOdd(), "ODD").start();
		//t.start();
		System.out.println("Hello Thread");
		}
	}
*/

