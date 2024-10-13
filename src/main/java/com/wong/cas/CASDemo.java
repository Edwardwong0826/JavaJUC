package com.wong.cas;

import java.util.concurrent.atomic.AtomicInteger;

// Primitive atmoic class work well on single shared variable, but not good when need to operate with mutliple share viarables
// Instaed we can use AtomicReference class for mutliple share viarables
public class CASDemo {
    public static void main(String[] args) {
        AtomicInteger atomicInteger = new AtomicInteger(5);

        System.out.println(atomicInteger.compareAndSet(5,2022)+"\t"+atomicInteger.get());
        System.out.println(atomicInteger.compareAndSet(5,2022)+"\t"+atomicInteger.get());

   }
}
