package com.wong.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * spin lock advantage: use CAS dont need to like thread wait and block
 */
public class SpinLockDemo
{

    AtomicReference<Thread> atomicReference = new AtomicReference<>();

    public void lock()
    {
        Thread thread = Thread.currentThread();
        System.out.println(Thread.currentThread().getName()+"-------come in");
        while(!atomicReference.compareAndSet(null,thread))
        {

        }
    }

    public void unlock()
    {
        Thread thread = Thread.currentThread();
        atomicReference.compareAndSet(thread,null);
        System.out.println(Thread.currentThread().getName()+"-------task over, unlock");
    }

    // CAS got two major two problem
    // CAS will keep attempt when it failed, if CAS long time not success, will bring much overhead to CPU
    // CAS will also produce ABA problem
    public static void main(String[] args)
    {
        SpinLockDemo spinLockDemo = new SpinLockDemo();

        new Thread(()-> {
            spinLockDemo.lock();
            try{ TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e){ e.printStackTrace(); }
            spinLockDemo.unlock();
        }, "A").start();

        try{ TimeUnit.MILLISECONDS.sleep(500); } catch (InterruptedException e){ e.printStackTrace(); }

        new Thread(()-> {
            spinLockDemo.lock();
            spinLockDemo.unlock();
        }, "B").start();


    }
}
