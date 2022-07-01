package com.wong.juc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class BlockQueue
{
    public static void main(String[] args)
    {
        // ArrayBlockingQueue - with capacity limit queue, once full cannot put and will blocking
        // SynchronousQueue - like don't have capicty ArrayBlockingQueue, once put, need to take out element otherwise keep blocking
        // LinkedBlockingQueue - don't have capacity limit, can limit as well and blocking
        // PriorityBlockingQueue - can support priority blocking queue, get of element can/will base on priority to decide
        // Delay Queue - we can delay the timing to out queue, means if one element is put need to wait certain only can out queue
        // Above queue all will use reentrant lock and CAS(reentrant lock when TryAcquire will use CAS), only SynchronousQueue not use lock but only CAS operation instead
        BlockingQueue<Object> queue = new ArrayBlockingQueue<>(2);
        Runnable supplier = () -> {
          while(true)
          {
              try
              {
                  String name = Thread.currentThread().getName();
                  System.out.println(time() + " producer " + name + " perparing...");
                  try{ TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e){ e.printStackTrace(); };
                  System.out.println(time() + " producer " + name + " out");
                  queue.put(new Object());
              }
              catch(InterruptedException e)
              {
                  e.printStackTrace();
                  break;
              }
          }
        };

        Runnable consumer = () -> {
            while(true)
            {
                try
                {
                    String name = Thread.currentThread().getName();
                    System.out.println(time() + " consumer " + name + " waiting...");
                    queue.take();
                    System.out.println(time() + " consumer " + name + " get");
                    try{ TimeUnit.SECONDS.sleep(4); } catch (InterruptedException e){ e.printStackTrace(); };

                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                    break;
                }
            }
        };

        for (int i = 0; i < 2; i++) {
            new Thread(supplier,"Supplier-"+i).start();
        }
        for (int i = 0; i < 3; i++) {
            new Thread(consumer,"Consumer-"+i).start();
        }

    }

    private static String time()
    {
        SimpleDateFormat format = new SimpleDateFormat("HH:ms:ss");
        return "[" + format.format(new Date()) + "] ";
    }
}
