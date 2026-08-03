package de.luiskun.apis;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.luiskun.log.Log;
import de.luiskun.weather.HourlyWeather;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.List;

public class WeatherAPI {


    public static String getHourlyWeatherString(String city) {

        HourlyWeather hw = getHourlyWeather(city);
        if (hw == null) return null;

        int minIndex = 0;
        int maxIndex = 0;

        for (int i = 0; i < hw.precipitation_probability.size(); i++) {
            if (hw.temperature2m.get(i) < hw.temperature2m.get(minIndex)) minIndex = i;
            if (hw.temperature2m.get(i) > hw.temperature2m.get(maxIndex)) maxIndex = i;
        }

        double maxRainProb = hw.precipitation_probability.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);

        String rainText;

        if (maxRainProb <= 10) {
            rainText = "Es wird voraussichtlich nicht regnen.";
        } else if (maxRainProb <= 30) {
            rainText = "Es besteht eine geringe Wahrscheinlichkeit für Regen.";
        } else if (maxRainProb <= 60) {
            rainText = "Es besteht eine mittlere Wahrscheinlichkeit für Regen.";
        } else {
            rainText = "Es besteht eine hohe Wahrscheinlichkeit für Regen.";
        }

        String minTime = hw.time.get(minIndex).substring(11, 16);
        String maxTime = hw.time.get(maxIndex).substring(11, 16);

        return String.format(
                "Heute haben wir die niedrigste Temperatur in %s mit %.1f °C um %s Uhr und die höchste Temperatur mit %.1f °C um %s Uhr. %s",
                city,
                hw.temperature2m.get(minIndex),
                minTime,
                hw.temperature2m.get(maxIndex),
                maxTime,
                rainText
        );

    }

    public static HourlyWeather getHourlyWeather(String city) {

        JsonObject posInfos = Requester.request(HTTPLinks.CITY_COORDS(city));
        if (posInfos == null) {
            return null;
        }
        JsonObject weatherInfos = Requester.request(
                HTTPLinks.WEATHER_BY_COORDS(
                        posInfos.getAsJsonArray("results").get(0).getAsJsonObject().get("latitude").getAsDouble(),
                        posInfos.getAsJsonArray("results").get(0).getAsJsonObject().get("longitude").getAsDouble()));
        if (weatherInfos == null) {
            return null;
        }

        HourlyWeather hourlyWeather = new HourlyWeather();
        JsonArray timeInfos = weatherInfos.getAsJsonObject("hourly").getAsJsonArray("time");
        JsonArray tempInfos = weatherInfos.getAsJsonObject("hourly").getAsJsonArray("temperature_2m");
        JsonArray rainInfos = weatherInfos.getAsJsonObject("hourly").getAsJsonArray("precipitation_probability");

        for (int i = 0; i < timeInfos.size(); i++) {
            hourlyWeather.time.add(timeInfos.get(i).getAsString());
            hourlyWeather.temperature2m.add(tempInfos.get(i).getAsDouble());
            hourlyWeather.precipitation_probability.add(rainInfos.get(i).getAsDouble());
        }

        return hourlyWeather;
    }

}
