package com.wong.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class ReEntryLockDemo
{

    static ReentrantLock lock = new ReentrantLock();

    // reentantlock have method to estimate queue length or determine current thread is it in the AQS CHL queue
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
