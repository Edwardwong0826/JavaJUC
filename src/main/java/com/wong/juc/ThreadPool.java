package com.wong.juc;

import java.util.concurrent.*;

public class ThreadPool
{

    public static void main(String[] args)
    {
        threadPoolBlockingQueue();
        System.out.println();
        //threadPoolSynchronousQueue();
    }

    public static void threadPoolBlockingQueue()
    {
        // once core thread is full, all remaing task will go to blocking queue to wait
        // if core thread and queue is full, the thread pool will create up to maximum pool size allowed to pick up task
        // keep alive time is the time for non-core thread alive , non-core thread will destroy if no request coming
        // can pass in custom thread factory to define own thread creation
        // if the thread throw exception, thread pool will destroy it
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 4, 3, TimeUnit.SECONDS, new ArrayBlockingQueue<>(1), new ThreadFactory() {
            int counter = 0;
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "Custom thread: " + counter);
            }
        }, new ThreadPoolExecutor.DiscardOldestPolicy());

        for (int i = 0; i <6 ; i++) {
            int finalI = i;
            threadPoolExecutor.execute(()->{
                System.out.println(Thread.currentThread().getName() + " start execution! " + finalI);
                try{ TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e){ e.printStackTrace(); };
                System.out.println(Thread.currentThread().getName() + " finish " + finalI);
            });
        }

        try{ TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e){ e.printStackTrace(); };
        System.out.println("thread pool thread amount: " + threadPoolExecutor.getPoolSize());
        try{ TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e){ e.printStackTrace(); };
        System.out.println("thread pool thread amount: " + threadPoolExecutor.getPoolSize());

        threadPoolExecutor.shutdownNow(); // it will cancel all request in blocking queue and try to intrreput task that are running, once shutdown cannot submit task
    }

    public static void threadPoolSynchronousQueue()
    {

        // SynchronousQueue don't have waiting capacity, it will reject based on thread pool refuse policy
        // AbortPolicy - default, throw exception
        // CallerRunsPolicy - let submit task thread execute, example main thread
        // DiscardOldestPolicy - throw queue most recent task, switch with current task
        // DiscardPolicy - do nothing
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 4, 3, TimeUnit.SECONDS, new SynchronousQueue<>(), new ThreadPoolExecutor.CallerRunsPolicy());

        for (int i = 0; i <6 ; i++) {
            int finalI = i;
            threadPoolExecutor.execute(()->{
                System.out.println(Thread.currentThread().getName() + " start execution! " + finalI);
                try{ TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e){ e.printStackTrace(); };
                System.out.println(Thread.currentThread().getName() + " finish " + finalI);
            });
        }

        try{ TimeUnit.SECONDS.sleep(1); } catch (InterruptedException e){ e.printStackTrace(); };
        System.out.println("thread pool thread amount: " + threadPoolExecutor.getPoolSize());
        try{ TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e){ e.printStackTrace(); };
        System.out.println("thread pool thread amount: " + threadPoolExecutor.getPoolSize());

        threadPoolExecutor.shutdownNow(); // it will cancel all request in blocking queue and try to intrreput task that are running, once shutdown cannot submit task
    }
}
