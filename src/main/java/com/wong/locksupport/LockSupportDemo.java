package com.wong.locksupport;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class LockSupportDemo {

    // when use Object monitor and condition wait/await method, thread need to get lock first
    // and wait/await after notify/signal only can wake up
    public static void main(String[] args) {
        //syncWaitNotify();
        //syncAwaitSignal();
        syncParkUnpark();
    }

    public static void syncParkUnpark(){

        // LocKSupport will create will already have the lock
        // use park and unpark method, the order doesn't matter, the thread can still be wake up
        Thread t1 = new Thread(()->{

            try{ TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e){ e.printStackTrace(); }
            System.out.println(Thread.currentThread().getName() + "\t" + "----come in");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "\t" + "----being wake up");
        }, "t1");

        t1.start();

        //try{ TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e){ e.printStackTrace(); }

        new Thread(()->{

            LockSupport.unpark(t1);
            System.out.println(Thread.currentThread().getName() + "\t" + "----send notice");

        }, "t2").start();
    }

    public static void syncAwaitSignal(){
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(()->{
            lock.lock();

            try
            {
                // await and signal method need to use in lock/unlock inside
                // need to await after signal only can wake up
                System.out.println(Thread.currentThread().getName() + "\t" + "----come in");
                condition.await();
                System.out.println(Thread.currentThread().getName() + "\t" + "----being awake");

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally
            {
                lock.unlock();
            }


        }, "t1").start();

        try{ TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e){ e.printStackTrace(); }

        new Thread(()->{
            lock.lock();
            try
            {
                condition.signal();
                System.out.println(Thread.currentThread().getName() + "\t" + "----send notice");

            }
            finally
            {
                lock.unlock();
            }

        }, "t2").start();
    }

    public static void syncWaitNotify(){
        Object objectLock = new Object();

        new Thread(()->{
            synchronized (objectLock){
                System.out.println(Thread.currentThread().getName() + "\t" + "----come in");

                // notice wait method will release lock, only can use in synchronized block
                // other words own the object monitor
                try {
                    objectLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + "\t" + "----being wake up");
            }
        }, "t1").start();


        try{ TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e){ e.printStackTrace(); }

        new Thread(()->{
            synchronized (objectLock){

                objectLock.notify();
                System.out.println(Thread.currentThread().getName() + "\t" + "----send notice");

            }
        }, "t2").start();
    }

}
