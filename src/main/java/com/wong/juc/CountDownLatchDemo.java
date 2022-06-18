package com.wong.juc;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {

    public static void main(String[] args) throws InterruptedException {
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

}
