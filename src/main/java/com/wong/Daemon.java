package com.wong;

import java.util.concurrent.TimeUnit;

public class Daemon {

    public static void main(String[] args) {
        Thread thread = new Thread(() ->{
            System.out.println(Thread.currentThread().getName() + " " +Thread.currentThread().isDaemon());
            while(true){

            }
        }, "t1");
        thread.setDaemon(true);
        thread.start();

        try{
            TimeUnit.SECONDS.sleep(1);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        System.out.println(Thread.currentThread().getName() + " end");

    }
}
