package com.wong.juc;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {

    // CountDownLatch
    // implemeted by using AQS CHL queue, and is fair sync
    // use shared lock concept
    // at the begining already lock by count amount, which is state = count
    // await() is shared lock, but state need be 0 only able to add lock success, otherwise according AQS mechanism , it will go into wait queue and block, add lock success exit the block
    // countDown() is minus the lock by 1
    public static void main(String[] args) throws InterruptedException
    {
        //latch1();
        latch2();

    }

    public static void latch1()
    {
        final CountDownLatch latch = new CountDownLatch(1);

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " Do some initial working.");
            try {
                Thread.sleep(1000);
                latch.await();
                System.out.println(Thread.currentThread().getName() + " Do other working.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " Do some initial working.");
            try {
                Thread.sleep(1000);
                latch.await();
                System.out.println(Thread.currentThread().getName() + " Do other working.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            System.out.println("asyn prepare for some data.");
            try {
                Thread.sleep(2000);
                System.out.println("Data prepare for done.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                latch.countDown();
            }
        }).start();
    }

    public static void latch2() throws InterruptedException {

        // CountDownLatch only can be used one time, need to create new one if want use again
        CountDownLatch latch = new CountDownLatch(20);

        for (int i = 0; i < 20; i++) {
            int finalI = i;
            new Thread(()->{

                try {
                    Thread.sleep((long)(2000*new Random().nextDouble()));
                    System.out.println("Subtask " + finalI + " execute completed!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }).start();
        }

        // wait all thread to complete, when count is 0, it will continue
        latch.await(); // this method can invoke by many thread, wait together
        System.out.println("all subtask completed!");

    }

}
