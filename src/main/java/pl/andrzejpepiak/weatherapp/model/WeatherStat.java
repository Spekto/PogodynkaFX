package pl.andrzejpepiak.weatherapp.model;

public class WeatherStat {
    private String city;
    private float temp;

    public WeatherStat(String city, float temp) {
        this.city = city;
        this.temp = temp;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public float getTemp() {
        return temp;
    }

    public void setTemp(float temp) {
        this.temp = temp;
    }
}
