package com.wong.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

public class StampedLockDemo
{
    static int number = 37;
    static StampedLock stampedLock = new StampedLock();

    public static void main(String[] args)
    {

//        StampedLockDemo resource = new StampedLockDemo();
//        new Thread(()->{
//            resource.read();
//        },"readThread").start();
//
//        try{ TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e){ e.printStackTrace(); };
//
//        new Thread(()->{
//            System.out.println(Thread.currentThread().getName()+ "---- come in");
//            resource.write();
//        },"writeThread").start();

        StampedLockDemo resource = new StampedLockDemo();
        new Thread(()->{
            resource.tryOptimisticRead();
        },"readThread").start();

        try{ TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e){ e.printStackTrace(); };

        new Thread(()->{
            System.out.println(Thread.currentThread().getName() + " ----come in");
            resource.write();
        },"writeThread").start();
    }

    public void write()
    {
        long stamp = stampedLock.writeLock();
        System.out.println(Thread.currentThread().getName() + " ready write");
        try
        {
            number = number + 13;

        }
        finally
        {
            stampedLock.unlockWrite(stamp);
        }

        System.out.println(Thread.currentThread().getName() + " finish write");

    }

    // normal readlock get the lock, write lock cannot get the lock and block, this is pessimistic read
    public void read()
    {
        long stamp = stampedLock.readLock();
        System.out.println(Thread.currentThread().getName() + " come in readlock code block, after 4 seconds continue...");

        for (int i = 0; i < 4; i++) {
            try
            {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            };
            System.out.println(Thread.currentThread().getName() + " reading...");
        }

        try
        {
            int result = number;
            System.out.println(Thread.currentThread().getName() + " get member variable value result " + result);
            System.out.println("write thread didn't change success, when readlock lock, write lock cannot get in, traditional read write exlusion");
        }
        finally
        {
            stampedLock.unlockRead(stamp);
        }
    }

    // for optimistic read, when readlock getlock during read, write lock will not be block
    public void tryOptimisticRead()
    {
        long stamp = stampedLock.tryOptimisticRead();
        int result = number;

        // stop for 4 seconds, optimistic think that no other threads modify number value
        System.out.println("before 4 second stampedLock.validate method result (true got change or false dont have change): " + stampedLock.validate(stamp));
        for (int i = 0; i < 4; i++) {
            try{ TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e){ e.printStackTrace(); };
            System.out.println(Thread.currentThread().getName() + " reading... " +i+" second. Check validate result: " + stampedLock.validate(stamp) );
        }

        if(!stampedLock.validate(stamp))
        {
            System.out.println("There is modified------ got write operation");
            stamp = stampedLock.readLock();
            try
            {
                System.out.println("upgarde from optimistic read to pessimistic read");
                result = number;
                System.out.println("after pessimistic read result: "+result);
            }
            finally
            {
                stampedLock.unlockRead(stamp );
            }
        }

        System.out.println(Thread.currentThread().getName() + " final value: " + result);

    }
}
