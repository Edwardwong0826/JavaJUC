package com.wong.juc;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  better to remove ThreadLocal variable, in the thread pool scenario, the thread will repeat use
 *  as might cause memory leak issue or affect after business logic, use try-finally to remove
 */
public class ThreadLocalDemo2
{
    public static void main(String[] args)
    {

        Mydata mydata = new Mydata();
        ExecutorService threadPool = Executors.newFixedThreadPool(3);

        ThreadLocal<String> tl = new ThreadLocal<>();
        tl.set("xxx@gmail.com");
        tl.get();
        tl.remove(); // in thread pool use scenario, need to use remove 

        // example in Thread, got ThreadLocal.ThreadLocalMap = null, ThreadLocalMap stored entry key WeakReference<ThreadLocal<?>>
        // when GC collect, this key WeakReference<ThreadLocal<?>> will remove and key will become null
        // in the thread pool scenario, Thread will reuse, thus even the key is null but still got value and have a reference to the thread
        // GC Collector cannot remove, thus it may cause series of problem
        // by calling remove(), to ensure entry value also become null, then can be GC to solve the problem
        // set, getEntry, remove method will invoke expungeStaleEntry, cleanSomeSlots, replaceStaleEntry to clean dirty entry key of null
        new HashMap<>().put(null,"xxx@gmail.com");

        try
        {
            for (int i = 0; i < 10; i++) {
                threadPool.submit(()->{
                    try {
                        Integer beforeInt = mydata.field.get();
                        mydata.add();
                        Integer afterInt = mydata.field.get();
                        System.out.println(Thread.currentThread().getName()+" beforeInt: "+beforeInt+" afterInt: "+afterInt);
                    } finally {
                        mydata.field.remove();
                    }

                });
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }finally
        {
            threadPool.shutdown();
        }
    }
}

class Mydata
{
    ThreadLocal<Integer> field = ThreadLocal.withInitial(()->0);

    public void add()
    {
        field.set(1 + field.get());
    }
}
