package com.wong.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReentrantReadWriteLockDemo
{
    public static void main(String[] args)
    {
        MyResource myResource = new MyResource();

        for (int i = 1; i <= 10 ; i++) {
            int finalI = i;
            new Thread(()->{
                //myResource.write(finalI +"", finalI +"" );
                myResource.rwWrite(finalI +"", finalI +"" );
            },String.valueOf(i)).start();
        }

        for (int i = 1; i <= 10 ; i++) {
            int finalI = i;
            new Thread(()->{
                //myResource.read(finalI +"", finalI +"" );
                myResource.rwRead(finalI +"");
            },String.valueOf(i)).start();
        }

        try{ TimeUnit.MILLISECONDS.sleep(1); } catch (InterruptedException e){ e.printStackTrace(); };

        for (int i = 1; i <= 3 ; i++) {
            int finalI = i;
            new Thread(()->{
                myResource.rwWrite(finalI +"", finalI +"" );
            },"new write lock thread -> "+String.valueOf(i)).start();
        }

    }
}

class MyResource
{
    Map<String, String> map = new HashMap<>();

    //ReentrantLock kind like synchronized
    ReentrantLock lock = new ReentrantLock();

    // ReentrantReadWriteLock is read write mutual exclusion, read read share
    // it allows multi read thread to read the resource
    // but got two problem
    // 1. write lock straving problem
    // 2. lock downgrade

    ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();

    public void write(String key, String value)
    {
        lock.lock();
        try
        {
            System.out.println(Thread.currentThread().getName()+" write in");
            map.put(key,value);
            try{ TimeUnit.MILLISECONDS.sleep(500); } catch (InterruptedException e){ e.printStackTrace(); };
            System.out.println(Thread.currentThread().getName()+" finish write");

        }finally {
            lock.unlock();
        }
    }

    public void read(String key)
    {
        lock.lock();
        try
        {
            System.out.println(Thread.currentThread().getName()+" read in");
            String s = map.get(key);
            try{ TimeUnit.MILLISECONDS.sleep(200); } catch (InterruptedException e){ e.printStackTrace(); };
            System.out.println(Thread.currentThread().getName()+" finish read "+s);

        }finally {
            lock.unlock();
        }
    }

    public void rwWrite(String key, String value)
    {
        rwLock.writeLock().lock();
        try
        {
            System.out.println(Thread.currentThread().getName()+" write in");
            map.put(key,value);
            try{ TimeUnit.MILLISECONDS.sleep(500); } catch (InterruptedException e){ e.printStackTrace(); };
            System.out.println(Thread.currentThread().getName()+" finish write");

        }finally {
            rwLock.writeLock().unlock();
        }
    }

    public void rwRead(String key)
    {
        rwLock.readLock().lock();
        try
        {
            System.out.println(Thread.currentThread().getName()+" read in");
            String s = map.get(key);
            //try{ TimeUnit.MILLISECONDS.sleep(200); } catch (InterruptedException e){ e.printStackTrace(); };
            // stop 2000 ms, demostrate when read lock haven't finish, write lock cannot get lock
            try{ TimeUnit.MILLISECONDS.sleep(2000); } catch (InterruptedException e){ e.printStackTrace(); };
            System.out.println(Thread.currentThread().getName()+" finish read "+s);

        }finally {
            rwLock.readLock().unlock();
        }
    }
}
