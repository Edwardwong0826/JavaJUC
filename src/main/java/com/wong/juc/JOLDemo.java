package com.wong.juc;

import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;

/**
 * java -XX:+PrintCommandLineFlags -version - can check is it using compressed class pointers
 */
public class JOLDemo
{
    public static void main(String[] args)
    {

        //System.out.println(VM.current().details());
        //System.out.println(VM.current().objectAlignment());

//        Object o = new Object();
//        System.out.println(ClassLayout.parseInstance(o).toPrintable());

        Customer2 c1 = new Customer2();
        System.out.println(ClassLayout.parseInstance(c1).toPrintable());

    }

}

// when only one object header instance object, 16 bytes(ignore compress pointer)+4bytes+1byte=21bytes----> padding(will padding to 8 multiply) to 24bytes,
// by default, JVM enabled -XX:+UseCompressedClassPointers, so 8 bytes class pointers -> 4 bytes class pointers
class Customer2
{
    // first scenario, only object header without instance data 16 bytes
    // second scenario, got object header and instance data and filling with padding 24 bytes
//    int id;
//    boolean flag = false;
}
