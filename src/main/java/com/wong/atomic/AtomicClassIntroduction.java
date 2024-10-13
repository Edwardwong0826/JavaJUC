package com.wong.atomic;

public class AtomicClassIntroduction {
    // Atomic classes are simply classes that feature atomic operations.
    // The Atomic class in the java.util.concurrent.atomic package provides a thread-safe way to manipulate individual variables.
    // The Atomic class relies on CAS (Compare-And-Swap) optimistic locks to guarantee the atomicity of its methods without the need to use traditional locks.
    // without the need to use traditional locking mechanisms such as synchronised blocks or ReentrantLock.

    // And CAS rely on Unsafe class to achieve atomic operation

}
