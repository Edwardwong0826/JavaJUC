package com.wong;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ThreadBase {

    public static void main(String[] args) {
        Thread t1 = new Thread(()->{}, "t1");
        t1.start();

        // every object also can be get the lock act as monitor
        Object o = new Object();

        Thread t2 = new Thread(()->{
            synchronized (o){
                System.out.println(o);
            }
        }, "t2");
    }

}
