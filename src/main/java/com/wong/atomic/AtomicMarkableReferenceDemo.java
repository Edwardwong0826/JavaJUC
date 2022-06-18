package com.wong.atomic;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;


public class AtomicMarkableReferenceDemo {

    // CAS-------Unsafe------do while + ABA
    // AtomicMarkableReference, test one time touch, false
    // this solve is it modify before

    static AtomicMarkableReference markableReference = new AtomicMarkableReference(100,false);
    public static void main(String[] args) {

        new Thread(()->{
            boolean marked = markableReference.isMarked();

            System.out.println(Thread.currentThread().getName()+" default mark: "+marked);
            try{ TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e){ e.printStackTrace(); };

            markableReference.compareAndSet(100,1000,marked,!marked);

        },"t1").start();

        new Thread(()->{
            boolean marked = markableReference.isMarked();

            System.out.println(Thread.currentThread().getName()+" default mark: "+marked);

            try{ TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e){ e.printStackTrace(); };
            boolean b = markableReference.compareAndSet(100, 2000, marked, !marked);
            System.out.println(Thread.currentThread().getName()+" t2 thread CAS result: "+b);
            System.out.println(Thread.currentThread().getName()+" "+markableReference.isMarked());
            System.out.println(Thread.currentThread().getName()+" "+markableReference.getReference());
        },"t2").start();
    }

}
