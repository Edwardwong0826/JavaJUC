package com.wong.juc;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SemaphoreDemo
{

    // Semaphore allow us to set some reousrce can maximun access by how many threads
    // other words, it is exlusion lock thst can be accquired by N threads(also support fair sync and non fair sync)
    // when other thread cannot get the permit, it will be block
    // internal using CAS(underlying is Unsafe.compareAndSwapXXX(xxx xxx)) to implemented
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Semaphore semaphore = new Semaphore(2);

        for (int i = 0; i < 3; i++) {
            new Thread(()->{
                try {
                    semaphore.acquire();
                    System.out.println("get the permit!");
                    try{ TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e){ e.printStackTrace(); };
                    semaphore.release();
                    System.out.println("Return permit!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }).start();
        }

        System.out.println("Available permits: "+semaphore.availablePermits());
        System.out.println("Currently has queued threads: "+semaphore.hasQueuedThreads());
        System.out.println("Queue length: " + semaphore.getQueueLength());
        System.out.println("return back all permits: "+semaphore.drainPermits()); // get back available permits
    }

}
