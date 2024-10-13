package com.wong.multithread;

import java.util.concurrent.TimeUnit;
class Phone
{

    /**
     * How to use synchronized
     * 1. Use in instance method
     *  synchronized void method() {
     *      //业务代码
     *  }
     *
     * 2. Use in static method
     *
     *  synchronized static void method() {
     *      //业务代码
     *  }
     *
     * 3. Use in code block {}
     *  synchronized(object) - means before enter the synchronisation code base need to acquire this given object lock
     *  synchronized(class) -  means before enter the synchronisation code base need to acquire this given class lock
     *
     *  synchronized(this) {
     *     //业务代码
     *  }
     *
     */

    // The synchronized keyword, when added to static methods and synchronized(class) blocks, locks the class;
    // The synchronized keyword added to an instance method locks the object instance;

    public static synchronized void sendEmail()
    {
        try
        {
            TimeUnit.SECONDS.sleep(3);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        System.out.println("--------send email");
    }

    public static synchronized void sendSMS()
    {
        System.out.println("--------send SMS");
    }

    public void doLongTimeTask()
    {
        System.out.println(Thread.currentThread().getName() + " start");

        synchronized (this)
        {
            try
            {
                TimeUnit.SECONDS.sleep(3);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " do long time task");
        }

        System.out.println(Thread.currentThread().getName() + " finish");
    }

    public void doSecondTimeTask()
    {
        System.out.println(Thread.currentThread().getName() + " start");

        synchronized(this)
        {
            System.out.println(Thread.currentThread().getName() + " do second time task");
        }

        System.out.println(Thread.currentThread().getName() + " finish");
    }

    public void hello()
    {
        System.out.println("--------hello");
    }
}

/**
 * 8 case example
 *  1.stand access have a b two thread, print email or sms
 *  2.sendEmail add stop 3 seconds, print email or sms
 *  3.add normal hello, print email or hello
 *  4.two phone, print email or sms
 *  5.two static synchronized method one phone, print email or sms
 *  6.two static synchronized method two phone, print email or sms
 *  7.one static synchronized method, one normal synchronized method, one phone, print email or sms
 *  8.one static synchronized method, one normal synchronized method, two phone, print email or sms
 *
 *
 *   note
 *   Question 1-2
 *      一个对象里面如果有对个synchronized方法, at certain time,只要有一个 thread调用其中一个synchronized方法了
 *      other threads only can wait, other words, 只能有唯一的thread去访问这些synchronized方法
 *      锁的是当前对象this（对象锁）, 锁定后, 其他thread都不能进入当前对象的其他的synchronized方法
 *   Question 3-4
 *      一个a线程调用synchronized方法拿了锁,另外一个b调用普通方法，普通方法没有锁，所以b可以直接用，
 *      两个线程call不同的对象synchronized方法，没有竞争,不是同一把锁
 *   Question 5-6
 *       三种synchronized锁的内容差别：
 *       对于普通synchronized方法,锁得是当前实例对象，通常指this,所有的于普通synchronized方法得是同一把锁->实例对象本身
 *       对于static synchronized方法, 锁得是当前类的Class对象（锁类），example Phone.class
 *       对于synchronized代码块, 锁得括号内的对象而已
 *
 *       
 *         -synchronized(o)
 *          { }
 *    Question 7-8
 *       当一个线程试图访问synchronized方法获取锁后代码他必须得到锁，正常退出或者抛出异常必须释放锁
 *       具体实例对象this和唯一模板Class, 这两把锁是两个不同的对象，所以static synchronized方法和普通synchronized方法没有竞态条件
 *       但是一旦一个static synchronized方法获取锁后，其他的static synchronized方法都必须等待该方法释放锁后才能获得锁
 *
 */
public class EightLockTest
{

    // https://zhuanlan.zhihu.com/p/39896563 高性能，高并发概念：排队和各种锁
    // https://zhuanlan.zhihu.com/p/40791268 Java concurrent原理
    public static void main(String[] args)
    {
        Phone phone = new Phone();
        Phone phone2 = new Phone();

        new Thread(()->{
            phone.doLongTimeTask();
        }, "a").start();

//        try
//        {
//            TimeUnit.SECONDS.sleep(2);
//        }
//        catch (InterruptedException e)
//        {
//            e.printStackTrace();
//        }

        new Thread(()->{
            phone.doSecondTimeTask();
            //phone.hello();
            //phone2.sendSMS();
        }, "b").start();
    }
}

