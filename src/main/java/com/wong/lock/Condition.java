package com.wong.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Condition
{
    public static void main(String[] args) throws InterruptedException {
        Lock testLock = new ReentrantLock();

        java.util.concurrent.locks.Condition condition = testLock.newCondition();
        new Thread(()->{
            testLock.lock();
            System.out.println("Thread 1 into wait status");

            try
            {
                condition.await();
            }catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            System.out.println("Thread 1 finish wait");
            testLock.unlock();

        },"1").start();

        Thread.sleep(100);

        new Thread(()->{
            testLock.lock();
            System.out.println("Thread 2 into wait status");
            condition.signal();
            System.out.println("Thread 2 finish wait");
            testLock.unlock();

        },"1").start();

    }
}