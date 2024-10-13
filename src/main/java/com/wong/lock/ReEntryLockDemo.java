package com.wong.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReEntryLockDemo
{
    // ReentrantLock kind of like synchronized both are re-entrant locks
    // also called recursive locks (可重入锁 也叫递归锁), mean that a thread can acquire its own internal lock again.
    // For example, if a thread acquires a lock on an object, and the object lock is not released yet
    // If it's a non-reentrant lock, it will cause a deadlock.

    // Another thing is ReentrantLock was interruptible lock and synchronized was non-interruptible lock
    // Interruptible lock - During the aquire lock process can be interrrupt or stop, not necessary need to keep waiting for get the lock only able to process other thing
    // Non-Interruptible lock - Once the thread start acquire the lock, need to wait unitl get the lock only able to process other thing

    // It has 3 main point
    // 1. Waiting for interruptible - A mechanism is provided to be able to interrupt a thread waiting for a lock, via lock.lockInterruptibly().
    //                                This means that a waiting thread can choose to quit waiting and do something else instead.
    // 2. Fair locking is possible - can choose fair sync or non-fair sync, reentrantLock default was non-fair sync,
    // 3. can selective notification (locks can be bound to multiple conditions) - need to depend on conidtion interface and newCondition method
    // static ReentrantLock lock = new ReentrantLock(true); // put true is fair lock, empty args and false for unfair lock
    static ReentrantLock lock = new ReentrantLock();

    // Reentrantlock have method to estimate queue length or determine current thread is it in the AQS CHL queue
    // getQueueLength()
    // hasQueuedThread()
    public static void main(String[] args)
    {
        //reEntryM1();
        ReEntryLockDemo reEntryLockDemo = new ReEntryLockDemo();
        //new Thread(()->{reEntryLockDemo.m1();},"a").start();

        Thread t1 = new Thread(() -> {

            lock.lock();

            try {
                System.out.println(Thread.currentThread().getName() + "-----come in outside layer");
                lock.lock();

                try{ TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e){ e.printStackTrace(); };

                try {

                    System.out.println(Thread.currentThread().getName() + "-----come in inside layer");

                } finally {
                    lock.unlock();
                }
            } finally {
                lock.unlock();

            }

        }, "t1");
        t1.start();

        // lock how many times, also unlock how many times, else second thread cannot get the lock, keep waiting
        Thread t2 = new Thread(() -> {

            lock.lock();

            try {
                System.out.println(Thread.currentThread().getName() + "-----come in outside layer");
                lock.lock();
            } finally {
                lock.unlock();

            }

        }, "t2");
        t2.start();

        System.out.println("current queue length: " + lock.getQueueLength());
        System.out.println(lock.hasQueuedThread(Thread.currentThread()));
    }

    public synchronized void m1()
    {
        System.out.println(Thread.currentThread().getName()+"----come in");
        m2();
        System.out.println(Thread.currentThread().getName()+"----end");
    }

    public synchronized void m2()
    {
        System.out.println(Thread.currentThread().getName()+"----come in");
    }


    private static void reEntryM1()
    {
        final Object object = new Object();
        new Thread(()->{
            synchronized (object){
                System.out.println(Thread.currentThread().getName()+"-----outside layer call");

                synchronized (object){
                    System.out.println(Thread.currentThread().getName()+"-----middle layer call");

                    synchronized (object){
                        System.out.println(Thread.currentThread().getName()+"-----inside layer call");

                    }
                }
            }
        },"t1").start();
    }


}
