package com.wong.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

//  This demo is use Java unsafe class to achieve CAS operation
public class UnsafeCASDemo {
    private volatile int a;
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        UnsafeCASDemo casTest=new UnsafeCASDemo();

        new Thread(()->{
            for (int i = 1; i < 5; i++) {
                casTest.increment(i);
                System.out.print(casTest.a+" ");
            }
        }).start();
        new Thread(()->{
            for (int i = 5 ; i <10 ; i++) {
                casTest.increment(i);
                System.out.print(casTest.a+" ");
            }
        }).start();
    }

    private void increment(int x){

        Unsafe unsafe = UnsafeCASDemo.getUnsafe();

        while (true){
            try {
                // fieldOffset is the variable a memory offset address, so that use CAS to perform update via atomic operation
                long fieldOffset = unsafe.objectFieldOffset(UnsafeCASDemo.class.getDeclaredField("a"));
                if (unsafe.compareAndSwapInt(this,fieldOffset,x-1,x))
                    break;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }

    // How to get Unsafe instance
    // 1. java -Xbootclasspath/a: ${path}   // 其中path为调用Unsafe相关方法的类所在jar包路径
    // 2. Get Unsafe singaleton instance by using reflection like below
    public static Unsafe getUnsafe() {
        try {
            Field f = Unsafe.class.getDeclaredField("theUnsafe");
            f.setAccessible(true);
            return (Unsafe)f.get(null);
        } catch (Exception e) {
            return null;
        }
    }
}
