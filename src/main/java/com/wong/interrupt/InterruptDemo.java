package com.wong.interrupt;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class InterruptDemo {

    static volatile boolean isStop = false;
    static AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    public static void main(String[] args) {
        //volatileStop();
        //atomicBooleanStop();
        interruptStop();
    }

    public static void interruptStop(){
       Thread t1 = new Thread(()->{
            while(true){
                if(Thread.currentThread().isInterrupted()){
                    System.out.println(Thread.currentThread().getName()+" is stop change to ture, program stop");
                    break;
                }
                System.out.println("t1--------hello interrupt api");
            }
        }, "t1");
        t1.start();

        try{ TimeUnit.MILLISECONDS.sleep(20); } catch (InterruptedException e){ e.printStackTrace(); }

        // thread t2 request t1 to interrupt, set t1 interrupt flag to true and hope its stop
        Thread t2 = new Thread(()->{
            t1.interrupt();
        }, "t2");

        t2.start();
    }

    public static void atomicBooleanStop(){
        new Thread(()->{
            while(true){
                if(atomicBoolean.get()){
                    System.out.println(Thread.currentThread().getName()+" is stop change to ture, program stop");
                    break;
                }
                System.out.println("t1--------hello atomic");
            }
        }, "t1").start();


        try{ TimeUnit.MILLISECONDS.sleep(20); } catch (InterruptedException e){ e.printStackTrace(); }

        new Thread(()->{
            atomicBoolean.set(true);
        }, "t2").start();
    }

    public static void volatileStop(){

        new Thread(()->{
            while(true){
                if(isStop){
                    System.out.println(Thread.currentThread().getName()+" is stop change to ture, program stop");
                    break;
                }
                System.out.println("t1--------hello volatile");
            }
        }, "t1").start();


        try{ TimeUnit.MILLISECONDS.sleep(20); } catch (InterruptedException e){ e.printStackTrace(); }

        new Thread(()->{
            isStop = true;
        }, "t2").start();
    }
}
