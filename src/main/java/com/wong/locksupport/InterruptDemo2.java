package com.wong.locksupport;

import java.util.concurrent.TimeUnit;

public class InterruptDemo2 {
    public static void main(String[] args) {
        interruptStop();
    }

    public static void interruptStop(){
        Thread t1 = new Thread(()->{
            for(int i = 1; i <= 300; i++)
            {
                System.out.println("---------" + i);
            }
            System.out.println("T1 thread default interrupt flag: " + Thread.currentThread().isInterrupted());

        }, "t1");
        t1.start();
        System.out.println("T1 thread default interrupt flag: " + t1.isInterrupted());

        try{ TimeUnit.MILLISECONDS.sleep(2); } catch (InterruptedException e){ e.printStackTrace(); }

        // instance method interrupt() only set thread interrupt flag to true, won't stop thread
        t1.interrupt();
        System.out.println("T1 thread interrupt flag after call interrupt(): " + t1.isInterrupted()); // true
        try{ TimeUnit.MILLISECONDS.sleep(2); } catch (InterruptedException e){ e.printStackTrace(); }
        System.out.println("T1 thread interrupt flag after call interrupt(): " + t1.isInterrupted()); // false
    }
}
