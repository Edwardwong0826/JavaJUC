package com.wong.juc;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ThreadLocalDemo
{
    public static void main(String[] args) throws  InterruptedException
    {
        House house = new House();

        for (int i = 1; i <=5 ; i++) {
            new Thread(()->{
                int size = new Random().nextInt(5)+1;
                try {
                    for (int j = 1; j <= size ; j++) {
                        house.saleHouse();
                        house.SaleVolumeByThreadLocal();
                    }
                    System.out.println(Thread.currentThread().getName()+"\t"+"no sale "+house.saleVolume.get());
                } finally {
                    house.saleVolume.remove(); // better to remove ThreadLocal variable as might cause memory leak issue or affect after business logic
                }
            },String.valueOf(i)).start();
        }
        try{ TimeUnit.MILLISECONDS.sleep(10); } catch (InterruptedException e){ e.printStackTrace(); };

        System.out.println(Thread.currentThread().getName()+" total sale how many house: " +house.saleCount);
    }
}

class House
{
    int saleCount = 0;
    // when initial value not recommend use anonymous class
    // use supplier instead
    ThreadLocal<Integer> saleVolume = ThreadLocal.withInitial(()->0);

    public synchronized void saleHouse()
    {
        ++saleCount;
    }

    public void SaleVolumeByThreadLocal()
    {
        saleVolume.set(1+saleVolume.get());
    }
}