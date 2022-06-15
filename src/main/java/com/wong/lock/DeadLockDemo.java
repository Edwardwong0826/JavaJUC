package com.wong.lock;

import java.util.concurrent.TimeUnit;

public class DeadLockDemo {

    // if program stuck there, and we suspect was deadlock, can run below command to check
    // jps -l
    // jstack <pid>
    // jconsle - jdk GUI to show
    public static void main(String[] args) {
        final Object a = new Object();
        final Object b = new Object();

        new Thread(()->{
            synchronized (a){
                System.out.println(Thread.currentThread().getName()+" have lock a, want to get lock b");
                try{
                    TimeUnit.SECONDS.sleep(1);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                synchronized (b){
                    System.out.println(Thread.currentThread().getName()+"get lock b");
                }
            }
        },"a").start();

        new Thread(()->{
            synchronized (b){
                System.out.println(Thread.currentThread().getName()+" have lock b, want to get lock a");
                try{
                    TimeUnit.SECONDS.sleep(1);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                synchronized (a){
                    System.out.println(Thread.currentThread().getName()+"get lock a");
                }
            }
        },"b").start();
    }
}
