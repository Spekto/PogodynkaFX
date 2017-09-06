package pl.andrzejpepiak.weatherapp.model.dao.impl;

import pl.andrzejpepiak.weatherapp.model.WeatherStat;
import pl.andrzejpepiak.weatherapp.model.dao.WeatherStatDao;
import pl.andrzejpepiak.weatherapp.model.utils.Connector;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class WeatherStatDaoImpl implements WeatherStatDao{

    private Connector connector = Connector.getInstance();
    @Override
    public void saveStat(WeatherStat weatherStat) {
        PreparedStatement preparedStatement = connector.getNewPreparedStatement(
                "INSERT INTO weather VALUES (?,?,?);"
        );
        try {
            preparedStatement.setInt(1,0);
            preparedStatement.setString(2,weatherStat.getCity());
            preparedStatement.setFloat(3,weatherStat.getTemp()- 273.15f);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<WeatherStat> getLastSixStats(String city) {
        List<WeatherStat> weatherStatList = new ArrayList<>();
        PreparedStatement preparedStatement = connector.getNewPreparedStatement(
                "SELECT * FROM weather WHERE city = ? ORDER BY id DESC LIMIT 6"
        );

        try {
            WeatherStat weatherStat;
            preparedStatement.setString(1, city);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                weatherStat = new WeatherStat(resultSet.getString("city"), resultSet.getInt("temp"));
                weatherStatList.add(weatherStat);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return weatherStatList;
    }


    public List<String> getAllCities() {
        List<String> cities = new ArrayList<>();
        PreparedStatement preparedStatement = connector.getNewPreparedStatement(
                "SELECT DISTINCT city FROM weather"
        );
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                cities.add(resultSet.getString("city"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cities;
    }
    public double countAvgTemp(String city) {
        List<Double> allTemp = new ArrayList<>();
        double avg = 0;
        PreparedStatement preparedStatement = connector.getNewPreparedStatement(
                "SELECT temp FROM weather WHERE city = ? ");
        try {
            preparedStatement.setString(1,city);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                allTemp.add(resultSet.getDouble("temp"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        double sum = 0;
        for (Double temp : allTemp) {
            sum+=temp;
        }
        avg = sum/allTemp.size();
        return avg;
    }

}
