package com.wong.juc;

import java.util.concurrent.*;

public class ExecutorDemo
{
    public static void main(String[] args) throws ExecutionException, InterruptedException
    {

        ExecutorService executorService = Executors.newFixedThreadPool(10);

        executorService.submit(()-> System.out.println("Hello World!"));

        // we can submit runnable and to block, once finish can do the reuslt we want
        Future<String> future = executorService.submit(()->{
          try
          {
              TimeUnit.SECONDS.sleep(3);
          }
          catch (InterruptedException e)
          {
              e.printStackTrace();
          };
        },"I am string!");

        System.out.println(future.get());

        FutureTask<String> futureTask = new FutureTask<>(()->"I am future task 1 string");
        executorService.submit(futureTask);
        System.out.println(futureTask.get());
        System.out.println("Is future task 1 done: " + futureTask.isDone());



        FutureTask<String> futureTask2 = new FutureTask<>(()->"I am future task 2 string");
        executorService.submit(()-> {
            try{ TimeUnit.SECONDS.sleep(10); } catch (InterruptedException e){ e.printStackTrace(); };
            return futureTask2;
        });
        System.out.println(futureTask2.cancel(true));
        System.out.println("Is future task 2 cancel: " + futureTask2.isCancelled());

        executorService.shutdown();

    }
}
