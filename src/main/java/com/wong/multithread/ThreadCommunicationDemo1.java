package com.wong.multithread;

// first step create resource class, define fields and method
public class ThreadCommunicationDemo1
{
    // third step, create multiply threads, invoke resource class method
    // this also called produer and consumer pattern
    public static void main(String[] args)
    {
        Share share = new Share();

        new Thread(()->{
            for (int i = 0; i <= 10 ; i++) {
                try {
                    share.incr();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"AA").start();

        new Thread(()->{
            for (int i = 0; i <= 10 ; i++) {
                try {
                    share.decr();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"BB").start();

        new Thread(()->{
            for (int i = 0; i <= 10 ; i++) {
                try {
                    share.incr();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"CC").start();

        new Thread(()->{
            for (int i = 0; i <= 10 ; i++) {
                try {
                    share.decr();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"DD").start();
    }
}

class Share
{
    private int number = 0;

    public synchronized void incr() throws InterruptedException {

        // step four prevent spurious wakeup problem
        // when use wait need to put in loop while, else will have虚假唤醒problem
        // because wait() will release lock
        // example here is thread B sleep, and then other thread notifyAll (spurious wakeup) and release lock
        // then thread B this round being awake up and get the lock, if execute from last line of code
        // causing if part no check at all
        // in order to solve this, need to use wait() in while
        while(number != 0)
        {
            this.wait(); //wait() is thread sleep at where, also wake up at where to continue
        }
        number++;

        System.out.println(Thread.currentThread().getName() + " : " + number);
        // notify other threads
        this.notifyAll();
    }

    // second step determine, work, notification
    public synchronized void decr() throws InterruptedException {

        while(number != 1)
        {
            this.wait();
        }
        number--;

        System.out.println(Thread.currentThread().getName() + " : " + number);

        this.notifyAll();
    }
}
