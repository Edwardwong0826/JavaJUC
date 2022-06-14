package com.wong.multithread;

/**
 *  open .class file dir
 *  javap -c LockSync.class
 *  javap -v LockSync.class
 */
public class LockSync {

    Object object = new Object();

    public void m1()
    {
        synchronized (object)
        {
            System.out.println("-------hello synchronized code block");
        }
    }
    public synchronized void m2()
    {
        System.out.println("-------hello synchronized code block");
    }

    public static void main(String[] args)
    {

    }
}

class book extends Object
{

}
