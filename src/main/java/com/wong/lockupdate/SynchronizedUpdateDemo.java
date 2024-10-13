package com.wong.lockupdate;

import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

public class SynchronizedUpdateDemo {

    private static Object objectLock = new Object();
    public static void main(String[] args)
    {
        noLock();
        biasLock();
        lightweightLock();
        heavyLock();
        countHashCode();

    }

    public static void countHashCode()
    {
        try{ TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e){ e.printStackTrace(); };
        Object o = new Object();
        Object o2 = new Object();

        System.out.println("should be bias lock");
        System.out.println(ClassLayout.parseInstance(o).toPrintable());

        o.hashCode();

        synchronized (o){
            System.out.println("should be bias lock, but after count identity hash code, will directly upgrade to lightweight lock");
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }

        synchronized (o2){
            o.hashCode();
            System.out.println("bias lock encounter identity hash code request, will remove current lock, and inflate to heavy lock");
            System.out.println("should be bias lock, but after count identity hash code, will directly upgrade to lightweight lock");
            System.out.println(ClassLayout.parseInstance(o).toPrintable());
        }

    }

    public static void heavyLock()
    {

        new Thread(()->{
            synchronized (objectLock){
                System.out.println(ClassLayout.parseInstance(objectLock).toPrintable());
            }
        },"t1").start();

        new Thread(()->{
            synchronized (objectLock){
                System.out.println(ClassLayout.parseInstance(objectLock).toPrintable());
            }
        },"t2").start();

    }

    // use jvm command disabled bias lock to directly enter lightweight lock
    // after java 6, it will use Adaptive Spinning lock 适应性自旋锁, the spin times might reference according result of last successful spin times
    // if less spin success, next times will reduce spin times or don't spin at all avoid CPU resources
    public static void lightweightLock()
    {
        Object o = new Object();
        new Thread(()->{
            synchronized (o){
                System.out.println(ClassLayout.parseInstance(o).toPrintable());
            }
        },"t1").start();
    }

    /**
     * java -XX:+PrintFlagsInitial |grep BiasedLock* - check in linus bias lock parameter settings
     * -XX:+UseBiasedLocking - enable bias lock
     * -XX:BiasedLockingStartupDelay=0 - set bias lock start up delay time, default JVM delay 4 seconds
     * -XX:-UseBiasedLocking - if close biased lock, program will directly go to lightweight lock 轻量级锁
     */
    public static void biasLock()
    {
        // in mark word last 3 bits , 101 means bias lock flag
        Object o = new Object();

        // this will print mark word without thread pointers data
        System.out.println(ClassLayout.parseInstance(o).toPrintable());

        // this will print mark word with thread pointers data
        new Thread(()->{
            synchronized (o) {
                System.out.println(ClassLayout.parseInstance(o).toPrintable());
            }
        },"t1").start();
    }

    public static void noLock()
    {
        Object o = new Object();

        // mark word value look from end
        System.out.println("decimal: "+o.hashCode());
        System.out.println("hex: "+Integer.toHexString(o.hashCode()));
        System.out.println("binary: "+Integer.toBinaryString(o.hashCode()));
        System.out.println(ClassLayout.parseInstance(o).toPrintable());

    }
}
