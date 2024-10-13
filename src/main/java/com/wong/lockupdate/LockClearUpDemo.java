package com.wong.lockupdate;


/**
 * this lock object never shared to other lock to use
 * there is no machine code for this lock object, thus remove the use of lock - 锁消除
 */
public class LockClearUpDemo {

    static Object objectLock = new Object();

    public void m1()
    {


        // lock remove problem, JIT compile will ignore it, because everytime create a new object lock
        Object o = new Object();

        synchronized (o)
        {
            System.out.println("----hello LockClearUpDemo"+" "+o.hashCode()+" "+objectLock.hashCode());
        }
    }

    public static void main(String[] args) {

        LockClearUpDemo lockClearUpDemo = new LockClearUpDemo();

        for (int i = 1; i <= 10 ; i++) {
            new Thread(()->{
                lockClearUpDemo.m1();
            },String.valueOf(i)).start();
        }

    }
}
