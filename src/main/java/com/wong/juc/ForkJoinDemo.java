package com.wong.juc;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ForkJoinDemo
{
    // fork Join will split the bigger task to smaller task for different thread to execute
    // once finish each thread will join the result into big task to return
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        System.out.println(forkJoinPool.submit(new SubTask(1, 1000)).get());
    }

    // need to inherit from RecursiveTask only can submit to ForkJoinPool as a task
    private static class SubTask extends RecursiveTask<Integer>
    {

        private int start;
        private int end;

        public SubTask(int start, int end)
        {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute()
        {
            // split the task
           if(end-start > 125)
           {
               SubTask subTask1 = new SubTask(start, (end + start) / 2);
               subTask1.fork(); // continue split sub task
               SubTask subTask2 = new SubTask((end + start) / 2+1, end);
               subTask2.fork();
               return subTask1.join() + subTask2.join();
           }
           else
           {
               System.out.println(Thread.currentThread().getName() + " start calculate " + start + "-" + end + " value! ");
               int res = 0;
               for (int i = start; i <= end ; i++) {
                   res += i;
               }

               return res; // return will be join result
           }
        }
    }
}
