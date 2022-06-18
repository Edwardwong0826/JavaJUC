package com.wong.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class AtomicReferenceFieldUpdaterDemo
{
    public static void main(String[] args) {
        MyVar myVar = new MyVar();

        for (int i = 5; i <=5; i++) {
            new Thread(()->{
                myVar.init(myVar);
            },String.valueOf(i)).start();
        }
    }
}

class MyVar
{
    public volatile Boolean isInit = Boolean.FALSE;

    AtomicReferenceFieldUpdater<MyVar,Boolean> referenceFieldUpdater= AtomicReferenceFieldUpdater.newUpdater(MyVar.class,Boolean.class,"isInit");

    public void init(MyVar myvar)
    {
        if(referenceFieldUpdater.compareAndSet(myvar,Boolean.FALSE,Boolean.TRUE))
        {
            System.out.println(Thread.currentThread().getName()+"----start init, need 3 seconds");
            try{ TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e){ e.printStackTrace(); };
            System.out.println(Thread.currentThread().getName()+"----over init");
        }
        else
        {
            System.out.println(Thread.currentThread().getName()+"----already init by other thread");
        }
    }

}
