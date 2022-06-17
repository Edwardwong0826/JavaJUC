package com.wong.interrupt;

public class InterruptDemo4 {
    public static void main(String[] args) {

        System.out.println(Thread.currentThread().getName()+"\t"+Thread.interrupted());
        System.out.println(Thread.currentThread().getName()+"\t"+Thread.interrupted());
        System.out.println("-------1");
        Thread.currentThread().interrupt();
        System.out.println("-------2");
        // interrupted() first get the thread interrupt flag status , after that set the flag to false
        System.out.println(Thread.currentThread().getName()+"\t"+Thread.interrupted());
        System.out.println(Thread.currentThread().getName()+"\t"+Thread.interrupted());


        // below both call same native isInterrupted method, just that interrupted will pass true
        // isInterrupted wall pass false parameter
        Thread.interrupted();
        Thread.currentThread().isInterrupted();
    }
}
