package com.wong.juc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AQSDemo {

    // all these are use AQS, these class inside got sync class fields which is extends AbstractQueuedSynchronizer class
    // ReentrantLock
    // CountDownLatch
    // ReentrantReadWroteLock
    // CyclicBarrier
    // Semaphore

    // reentrant lock got Fair Sync and Nonfair Sync
    // when both sync invoke the lock(), different is that Nonfair Sync will have if else part for compareAndSetState(0, 1) part, Fair Sync only have acquire(1) code
    // this part means Nonfair Sync lock try to set the state to 1 get the lock instead of go to check state and queue if state is 1
    /**
     *             if (compareAndSetState(0, 1))
     *                 setExclusiveOwnerThread(Thread.currentThread());
     *             else
     *               acquire(1);
     */
    // compareAndSetState() is calling unSafe CAS method to update the state value
    /**
     *     protected final boolean compareAndSetState(int expect, int update) {
     *         // See below for intrinsics setup to support this
     *         return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
     *     }
     */
    // both Fair or NonfairSync acquire(1) is the same, tryAcquire is the method define by AQS, each sync will to implemented its own logic

    /**
     *         public final void acquire(int arg) {
     *             if (!tryAcquire(arg) &&
     *                     acquireQueued(addWaiter(AbstractQueuedSynchronizer.Node.EXCLUSIVE), arg))
     *                 selfInterrupt();
     *         }
     */
    // different is inside Fair Sync tryAcquire(), Fair Sync have !hasQueuedPredecessors() to check and queue, while NonfairSync TryAcquire() don't check this part
    /**
     *             xxxxxxxxxxx
     *             if (c == 0) {
     *                 if (!hasQueuedPredecessors() &&
     *                     compareAndSetState(0, acquires)) {
     *                     setExclusiveOwnerThread(current);
     *                     return true;
     *                 }
     *             }
     *             xxxxxxxxxxx
     */
    // if in tryAcuire() did get the lock, both sync will need to enqueues as node by call addWaiter(xxx, arg), and enq(node)
    // in the AQS CHL queue linked list, first node is virtual node, doesn't contain any information, only占位, only start from second node  will have data
    /**
     *     private Node addWaiter(Node mode) {
     *         Node node = new Node(Thread.currentThread(), mode);
     *         // Try the fast path of enq; backup to full enq on failure
     *         Node pred = tail;
     *         if (pred != null) {
     *             node.prev = pred;
     *             if (compareAndSetTail(pred, node)) {
     *                 pred.next = node;
     *                 return node;
     *             }
     *         }
     *         enq(node);
     *         return node;
     *     }
     */
    /**
     *     private Node enq(final Node node) {
     *         for (;;) {
     *             Node t = tail;
     *             if (t == null) { // Must initialize
     *                 if (compareAndSetHead(new Node()))
     *                     tail = head;
     *             } else {
     *                 node.prev = t;
     *                 if (compareAndSetTail(t, node)) {
     *                     t.next = node;
     *                     return t;
     *                 }
     *             }
     *         }
     *     }
     */
    // and then try to queue by invoke acquireQueued() in acquire(), there is infinite loop to keep run
    /**
     *     final boolean acquireQueued(final Node node, int arg) {
     *         boolean failed = true;
     *         try {
     *             boolean interrupted = false;
     *             for (;;) {
     *                 final Node p = node.predecessor();
     *                 if (p == head && tryAcquire(arg)) {
     *                     setHead(node);
     *                     p.next = null; // help GC
     *                     failed = false;
     *                     return interrupted;
     *                 }
     *                 if (shouldParkAfterFailedAcquire(p, node) &&
     *                     parkAndCheckInterrupt())
     *                     interrupted = true;
     *             }
     *         } finally {
     *             if (failed)
     *                 cancelAcquire(node);
     *         }
     *     }
     */
    // if thread(Node) couldn't success tryAcquire(), first time when enter check the ws=0 equal Node.SIGNAL -1 because not , will not execute if block
    // check if ws > 0, because not, it will go to else block use compareAndSetWaitStatus(pred, ws, Node.SIGNAL) set node waitStatus to -1 and return false,
    // loop again, if ws=-1 equal Node.SINGAL -1 return true
    // once shouldParkAfterFailedAcquire(p, node) is true go inside parkAndCheckInterrupt() use LockSupport.park(this); to block current thread
    /**
     *
     *    private static boolean shouldParkAfterFailedAcquire(Node pred, Node node) {
     *         int ws = pred.waitStatus;
     *         if (ws == Node.SIGNAL)
     *
     *              This node has already set status asking a release
     *              to signal it, so it can safely park.
     *
     *             return true;
     *         if (ws > 0) {
     *
     *              Predecessor was cancelled. Skip over predecessors and
     *              indicate retry.
     *
     *             do {
     *                 node.prev = pred = pred.prev;
     *             } while (pred.waitStatus > 0);
     *             pred.next = node;
     *         } else {
     *
     *              waitStatus must be 0 or PROPAGATE.  Indicate that we
     *              need a signal, but don't park yet.  Caller will need to
     *              retry to make sure it cannot acquire before parking.
     *
     *             compareAndSetWaitStatus(pred, ws, Node.SIGNAL);
     *         }
     *         return false;
     *     }
     *
     *         private final boolean parkAndCheckInterrupt() {
     *         LockSupport.park(this);
     *         return Thread.interrupted();
     *     }
     *
     */
    // when want to unlock, sync will call release and tryRelease(1) to unlock
    /**
     *     public final boolean release(int arg) {
     *         if (tryRelease(arg)) {
     *             Node h = head;
     *             if (h != null && h.waitStatus != 0)
     *                 unparkSuccessor(h);
     *             return true;
     *         }
     *         return false;
     *     }
     *
     *
     */
    // when call tryRelease(1), it will minus state value with 1, which is 1-1 = 0, and then set owner thread to null and state to 0;
    /**
     *         protected final boolean tryRelease(int releases) {
     *             int c = getState() - releases;
     *             if (Thread.currentThread() != getExclusiveOwnerThread())
     *                 throw new IllegalMonitorStateException();
     *             boolean free = false;
     *             if (c == 0) {
     *                 free = true;
     *                 setExclusiveOwnerThread(null);
     *             }
     *             setState(c);
     *             return free;
     *         }
     */
    // in compareAndSetWaitStatus(node, ws, 0), inside will set head node waitStatus to 0, check head next node == null or wait status > 0, and then unpark block thread
    // once unlock, in the acquireQueued there is infinite loop keep call tryAcquire() and try to get lock and set state, set owner thread as current thread
    /**
     *     private void unparkSuccessor(Node node) {
     *
     *          //If status is negative (i.e., possibly needing signal) try
     *          //to clear in anticipation of signalling.  It is OK if this
     *          //fails or if status is changed by waiting thread.
     *
     *         int ws = node.waitStatus;
     *         if (ws < 0)
     *             compareAndSetWaitStatus(node, ws, 0);
     *
     *
     *          //Thread to unpark is held in successor, which is normally
     *          //just the next node.  But if cancelled or apparently null,
     *          //traverse backwards from tail to find the actual
     *          non-cancelled successor.
     *
     *         Node s = node.next;
     *         if (s == null || s.waitStatus > 0) {
     *             s = null;
     *             for (Node t = tail; t != null && t != node; t = t.prev)
     *                 if (t.waitStatus <= 0)
     *                     s = t;
     *         }
     *         if (s != null)
     *             LockSupport.unpark(s.thread);
     *     }
     */
    // if thread dont want to wait anymore, it will call cancelAcquire() in acquireQueued() to leave AQS CHL queue

    /**
     *
     * @param args
     */
    public static void main(String[] args) {

        //new CountDownLatch(10);
        //new Semaphore(1);

        ReentrantLock reentrantLock = new ReentrantLock(); // default empty args or false is nonfair sync lock

        // Example, Simulate A B C, A first arrive, and state is 0, A get the lock first by set the state to 1
        // A occupied the lock very long
        new Thread(()->{
            reentrantLock.lock();
            try
            {
                System.out.println("--------come in A");
                try{TimeUnit.MINUTES.sleep(1); } catch (InterruptedException e){ e.printStackTrace(); };
            }finally {
                reentrantLock.unlock();
            }
        },"A").start();

        // B see A get the lock, only can go to queue by enter AQS queue, wait A to finish and try to acquire lock
        new Thread(()->{
            reentrantLock.lock();
            try
            {
                System.out.println("--------come in B");

            }finally {
                reentrantLock.unlock();
            }
        },"B").start();

        // C see A get the lock, only can go to queue by enter AQS queue, wait A to finish and try to acquire lock, in front is B, FIFO
        new Thread(()->{
            reentrantLock.lock();
            try
            {
                System.out.println("--------come in C");

            }finally {
                reentrantLock.unlock();
            }
        },"C").start();

    }
}
