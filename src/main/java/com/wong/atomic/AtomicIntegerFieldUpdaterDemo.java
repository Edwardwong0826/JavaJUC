package com.wong.atomic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class AtomicIntegerFieldUpdaterDemo
{

    // AtomicIntegerFieldUpdater class allow us able use thread safe way to operate
    // non thread safe object certain fields
    public static void main(String[] args) throws InterruptedException {
        BankAccount bankAccount = new BankAccount();
        CountDownLatch countDownLatch = new CountDownLatch(10);

        for (int i = 1; i <= 10; i++) {
            new Thread(()->{
                try {
                    for (int j = 1; j <= 1000; j++) {
                        //bankAccount.add();
                        bankAccount.transMoney(bankAccount);
                    }
                } finally {
                    countDownLatch.countDown();
                }
            },String.valueOf(i)).start();

        }

        countDownLatch.await();
        System.out.println(Thread.currentThread().getName()+" result: "+bankAccount.money);
    }
}

class BankAccount
{
    String bankName = "CCB";

    // use AtomicIntegerFieldUpdater need to ensure the field that want to operate is public volatile
    // and use AtomicIntegerFieldUpdater static method newUpdater to reflection the field
    public volatile int money = 0;

    AtomicIntegerFieldUpdater<BankAccount> fieldUpdater = AtomicIntegerFieldUpdater.newUpdater(BankAccount.class,"money");

    public void transMoney(BankAccount bankAccount)
    {
        fieldUpdater.getAndIncrement(bankAccount);
    }

    public void add()
    {
        money++;
    }

}
