package com.wong.juc;

import java.util.concurrent.TimeUnit;

public class VolatileSeeDemo {

    // every thread will have its own working memory, by using volatile, it can ensure main memory value is refresh and notify others thread
    // volatile ensure other threads read from this memory and know the changes and get the latest value
    // when generate machine code from byte code, if notice got volatile ACC_VOLATILE flag,
    // it will add memory barrier according JMM spec
    static volatile boolean flag = true;

    public static void main(String[] args) {
        new Thread(()->{

            System.out.println(Thread.currentThread().getName()+"--------- come in");

            while(flag)
            {

            }
            System.out.println(Thread.currentThread().getName()+"--------- flag set to false, program stop");

        }, "t1").start();

        try{ TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e){ e.printStackTrace(); }

        flag = false;

        System.out.println(Thread.currentThread().getName()+" change flag complete");
    }
}
