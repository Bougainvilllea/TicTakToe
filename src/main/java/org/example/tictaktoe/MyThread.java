package org.example.tictaktoe;

import javafx.application.Platform;

public class MyThread extends Thread {
    private final Runnable preInit;
    private final Runnable loopedRunnable;
    private final double updateFrequency;

    public MyThread(Runnable loopedRunnable, double updateFrequency) {
        this.loopedRunnable = loopedRunnable;
        this.updateFrequency = updateFrequency;

        preInit = new Runnable() {
            @Override
            public void run() {

            }
        };
    }

    public MyThread(Runnable loopedRunnable, Runnable preInit, double updateFrequency) {
        this.loopedRunnable = loopedRunnable;
        this.updateFrequency = updateFrequency;
        this.preInit = preInit;
    }

    @Override
    public void run() {
        preInit.run();

        while (true) {
            try {
                Thread.sleep((int) Math.ceil(1000 / updateFrequency));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            Platform.runLater(loopedRunnable);
        }
    }
}