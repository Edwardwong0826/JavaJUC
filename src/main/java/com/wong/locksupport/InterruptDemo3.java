package com.wong.locksupport;

import java.util.concurrent.TimeUnit;

public class InterruptDemo3 {
    public static void main(String[] args) {
        Thread t1 = new Thread(()->{
            while(true){
                if(Thread.currentThread().isInterrupted()){
                    System.out.println(Thread.currentThread().getName()+" is stop change to ture, program stop");
                    break;
                }

                // if when interrupt the thread is invoked sleep, join, wait method it will throw interrupted exception
                // it will clear the interrupt flag to false and cause infinite loop
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // need to call interrupt again in exception to stop program
                    e.printStackTrace();
                }

                System.out.println("t1--------hello interrupt");
            }
        }, "t1");
        t1.start();

        try{ TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e){ e.printStackTrace(); }

        Thread t2 = new Thread(()->{
            t1.interrupt();
        }, "t2");

        t2.start();
    }
}
