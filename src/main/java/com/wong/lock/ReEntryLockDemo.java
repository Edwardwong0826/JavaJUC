package com.wong.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReEntryLockDemo
{

    static Lock lock = new ReentrantLock();

    public static void main(String[] args)
    {
        //reEntryM1();
        ReEntryLockDemo reEntryLockDemo = new ReEntryLockDemo();
        new Thread(()->{reEntryLockDemo.m1();},"a").start();

        new Thread(()->{

              lock.lock();

               try
               {
                   System.out.println(Thread.currentThread().getName()+"-----come in outside layer");
                   lock.lock();
                   try
                   {
                       System.out.println(Thread.currentThread().getName()+"-----come in inside layer");
                   }
                   finally
                   {
                       lock.unlock();
                   }
               }
               finally
               {
                   lock.unlock();

               }

            },"t1").start();

        // lock how many times, also unlock how many times, else second thread cannot get the lock, keep waiting
        new Thread(()->{

            lock.lock();

            try
            {
                System.out.println(Thread.currentThread().getName()+"-----come in outside layer");
                lock.lock();
            }
            finally
            {
                lock.unlock();

            }

        },"t2").start();
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
