package com.wong.juc;

public class ObjectHeaderDemo
{
    public static void main(String[] args)
    {
        // new one object will take how much space? in 64 bit hotspot jvm, mark word take 16 bytes
        // mark work 8 byte
        // klass pointer 8 byte
        Object o = new Object();

        System.out.println(o.hashCode()); //hashcode stored in object header mark word

        synchronized (o){

        }

        System.gc();

        // Object header klass pointer point to metaspace Customer.class metaobject
        // and JVM look at this to know this object is which class instance
        Customer c1 = new Customer();

    }
}


class Customer
{
    // those field in class, its field(include parent field) information will put in object layout instance data part
    int id;
    boolean flag;
}
