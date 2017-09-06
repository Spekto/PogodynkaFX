package pl.andrzejpepiak.weatherapp.model.dao;

import pl.andrzejpepiak.weatherapp.model.WeatherStat;

import java.util.List;

public interface WeatherStatDao {

    void saveStat(WeatherStat weatherStat);
    List<WeatherStat>getLastSixStats(String city);
    List<String> getAllCities();
    double countAvgTemp(String city);
}
