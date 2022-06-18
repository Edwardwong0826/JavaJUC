package com.wong.juc;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CycleBarriersDemo {

    public static void main(String[] args) {
        CyclicBarriers();
    }

    public static void CyclicBarriers()
    {
        // if use thread pool, make sure thread pool larger or equal to CyclicBarrier value
        // else will hang , result dead lock

        System.out.println("CyclicBarriers-------------------------");
        ExecutorService es = Executors.newFixedThreadPool(4);
        CycleBarriersDemo cbd = new CycleBarriersDemo();
        CyclicBarrier c1 = new CyclicBarrier(4);
        CyclicBarrier c2 = new CyclicBarrier(4, () -> System.out.println("Finished!"));

        for (int i = 0; i < 4; i++) {
            es.submit(() -> cbd.performTask(c1, c2));
        }

        es.shutdown();

    }

    public void performTask(CyclicBarrier c1, CyclicBarrier c2)
    {

        // CyclicBarrier can enforce all the thread at certain point to wait and must wait all
        // thread finish to release the barrier before continue next execution

        try
        {
            remove(); // CyclicBarrier take constructor of value to indicating the number of thread to wait
            c1.await(); // only all thread called await(), the barrier is released to continue next task
            clean();
            c2.await();
            add();
        } catch (InterruptedException | BrokenBarrierException ignored)
        {

        }

    }

    private void remove() {
        System.out.println("Removing");
    }

    private void clean() {
        System.out.println("Cleaning");
    }

    private void add() {
        System.out.println("Adding");
    }
}
