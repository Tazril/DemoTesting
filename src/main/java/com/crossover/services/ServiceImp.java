package com.crossover.services;

import java.util.concurrent.TimeUnit;

public class ServiceImp implements NotificationService {

    @Override
    public void notify(int rating) {
        try {
            TimeUnit.MILLISECONDS.sleep(30);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
