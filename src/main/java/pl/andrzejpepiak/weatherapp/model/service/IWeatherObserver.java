package pl.andrzejpepiak.weatherapp.model.service;

import pl.andrzejpepiak.weatherapp.model.WeatherData;

public interface IWeatherObserver {
    void onWeatherUpdate(WeatherData data);
}
