package com.wong.atomic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;

// If we need to update multiple variables atomically, we can use reference atomic classes.
// AtomicReference class can achieve atomicity between referenced object
// By encapsulating multiple variables in a single object, we can use AtomicReference to perform CAS operations.
public class AtomicReferenceDemo {


    public static void main(String[] args) {

        AtomicReference<User> atomicReference = new AtomicReference<>();
        User lee = new User("Lee",22);
        User lim = new User("lim",28);

        atomicReference.set(lee);
        System.out.println(atomicReference.compareAndSet(lee,lim)+" "+atomicReference.get().toString());
        System.out.println(atomicReference.compareAndSet(lee,lim)+" "+atomicReference.get().toString());
    }
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class User
{
    String userName;
    int age;

}
