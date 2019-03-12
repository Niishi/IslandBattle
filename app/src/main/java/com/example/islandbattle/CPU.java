package com.example.islandbattle;

import android.util.Log;

import java.util.ArrayList;

public class CPU {

    ArrayList<City> cities = new ArrayList<>();
    public int id;
    float wheat = 50;
    float money = 50;

    private final static int MAX_FORCE_COUNT = 20;
    private final static int COOL_TIME = 1000;
    private int time = COOL_TIME + 1;

    CPU(City homeCity, int id) {
        cities.add(homeCity);
        this.id = id;
    }


    void turn(City[] allCities) {
        time++;
        int forceCount = getTotalForce();
        if(time > COOL_TIME && forceCount < MAX_FORCE_COUNT) {
            float totalSoldierCount = getTotalSoldierCount();
            City target = getTarget(allCities);
            if (target.soldier.getCount() < totalSoldierCount / 2 &&
                    wheat > target.soldier.getCount()) {

                for (City city : cities) {
                    city.attack(target);
                }
                time = 0;
            }
        }
    }

    private City getTarget(City[] allCities) {
        float minCount = Float.MAX_VALUE;
        City minCity = null;
        for (int i = allCities.length - 1; i >= 0; i--) {
            City city = allCities[i];
            if (city.getState() != id) {
                if (city.soldier.getCount() < minCount) {
                    minCount = city.soldier.getCount();
                    minCity = city;
                }
            }
        }
        return minCity;
    }

    private float getTotalSoldierCount() {
        float result = 0;
        for (City city : cities) {
            result += city.soldier.getCount();
        }
        return result;
    }

    private int getTotalForce(){
        int result = 0;
        for(City city : cities){
            result += city.soldiers.size();
        }
        return result;
    }

}
