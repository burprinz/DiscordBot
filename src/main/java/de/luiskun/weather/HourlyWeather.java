package de.luiskun.weather;

import java.util.ArrayList;
import java.util.List;

public class HourlyWeather {

    public List<String> time;
    public List<Double> temperature2m;
    public List<Double> precipitation_probability;

    public HourlyWeather() {
        time = new ArrayList<>();
        temperature2m = new ArrayList<>();
        precipitation_probability = new ArrayList<>();
    }

}
