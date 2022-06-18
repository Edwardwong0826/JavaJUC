package com.wong.atomic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicReference;

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
