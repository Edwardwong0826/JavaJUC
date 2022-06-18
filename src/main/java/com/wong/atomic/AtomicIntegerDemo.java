package com.wong.atomic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerDemo {

    public static final int SIZE = 50;

    public static void main(String[] args) throws InterruptedException {
        MyNumber myNumber = new MyNumber();
        CountDownLatch countDownLatch = new CountDownLatch(SIZE);

        for (int i = 1; i <= SIZE; i++) {
            new Thread(()->{
                try {
                    for (int j = 1; j <= 1000 ; j++) {
                        myNumber.addPlusPlus();
                    }
                } finally {
                    countDownLatch.countDown();
                }
            },String.valueOf(i)).start();
        }

        // need to wait above 50 thread compute complete, and get the final value

        countDownLatch.await();
        System.out.println(Thread.currentThread().getName()+" result " + myNumber.atomicInteger.get());
    }
}

class MyNumber
{
    AtomicInteger atomicInteger = new AtomicInteger();

    public void addPlusPlus()
    {
        atomicInteger.getAndIncrement();
    }
}
