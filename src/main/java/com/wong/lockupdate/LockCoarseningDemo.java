package com.wong.lockupdate;

/**
 * if method there is close each other same lock object, JIT compiler will merge these synchronized block,
 * by coarsening and add range, one times lock, avoid keep create abd release lock to increase performance
 */
public class LockCoarseningDemo {

   static Object objectLock = new Object();

    public static void main(String[] args) {

        new Thread(()->{
            synchronized (objectLock)
            {
                System.out.println("1");
            }
            synchronized (objectLock)
            {
                System.out.println("2");
            }
            synchronized (objectLock)
            {
                System.out.println("3");
            }
            synchronized (objectLock)
            {
                System.out.println("4");
            }

        },"t1").start();
    }

}
