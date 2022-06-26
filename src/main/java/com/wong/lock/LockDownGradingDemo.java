package com.wong.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * lock dwongrade: from write lock -> get read lock-> then release write lock sequence, write lock can downgrade to read lock
 * if a thread owns wrote lock, in not release write lock condition, it still can own the read lock, means write lock downgrade to read lock
 * when read is not finish write lock cannot get the lock, need to wait read lock finish read only have a chance to write
 * this is 悲观锁
 *
 *
 */
public class LockDownGradingDemo {
    public static void main(String[] args) {
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();


        // normal A B two thread
        // A
        //writeLock.lock();
        //System.out.println("----write");
        //writeLock.unlock();

        // B
        //readLock.lock();
        //System.out.println("----read");
        //readLock.unlock();


        //only one thread
        writeLock.lock();
        System.out.println("----write");

        readLock.lock();
        System.out.println("----read");

        writeLock.unlock();

        readLock.unlock();
    }
}
