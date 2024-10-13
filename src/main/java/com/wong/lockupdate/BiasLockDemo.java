package com.wong.lockupdate;

/**
 * java -XX:+PrintFlagsInitial |grep BiasedLock* - check bias lock parameter settings
 * -XX:+UseBiasedLocking - enable bias lock
 * -XX:BiasedLockingStartupDelay=0 - set bias lock start up delay time
 * -XX:-UseBiasedLocking - if close biased lock, program will directly go to lightweight lock
 *
 *
 *  Because biased locking - 偏向锁 adds complexity to the JVM, it also doesn't provide performance gains for all applications.
 *  Therefore, In Java 15, bias lock was default disable but still can be enabled back using command
 *  But in Java 18, bias lock it has been completely abandoned and unable to enabled back using command anymore
 */
public class BiasLockDemo {

    // when running the code, we will notice there will one thread get the lock more often there other threads
    // bias lock will prefer the first thread that get lock, if during next do not have other threads to ask lock
    // thread that hold bias lock no need trigger synchronized, also without CAS operation
    public static void main(String[] args)
    {
        Ticket2 ticket = new Ticket2();

        new Thread(()->{for(int i = 0; i <55; i++) ticket.sale();},"a").start();
        new Thread(()->{for(int i = 0; i <55; i++) ticket.sale();},"b").start();
        new Thread(()->{for(int i = 0; i <55; i++) ticket.sale();},"c").start();
    }
}

class Ticket2
{
    private int number = 50;
    Object lockObject = new Object();
    public void sale()
    {
        synchronized ((lockObject))
        {
            if(number>0)
            {
                // we will see one thread keep getting the lock and access the resource
                System.out.println(Thread.currentThread().getName()+" sale "+(number--)+" left "+number);

            }

        }
    }

}
