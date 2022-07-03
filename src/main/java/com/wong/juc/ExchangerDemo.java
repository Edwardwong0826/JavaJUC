package com.wong.juc;

import java.util.concurrent.Exchanger;

public class ExchangerDemo
{
    // exchanger will allow thread change data, if dont have other thread change it will block, need to be same exchanger object
    public static void main(String[] args) throws InterruptedException {
        Exchanger<String> exchanger = new Exchanger<>();
        new Thread(()->{
            try
            {
                System.out.println("Get main thread exchange data: " + exchanger.exchange("AAA"));
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }).start();

        System.out.println("Get sub thread exchange data: " + exchanger.exchange("BBB"));

    }
}
