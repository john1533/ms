package com.mobcent.lowest.android.ui.module.weather.observer;

import java.util.Observable;
import java.util.Observer;

public class WeatherObserver extends Observable {
    private static WeatherObserver observer = null;

    public static WeatherObserver getInstance() {
        if (observer == null) {
            observer = new WeatherObserver();
        }
        return observer;
    }

    public void addObserver(Observer observer) {
        super.addObserver(observer);
    }

    public synchronized void deleteObserver(Observer observer) {
        super.deleteObserver(observer);
    }

    public void notifyObservers(Object data) {
        setChanged();
        super.notifyObservers(data);
    }
}
