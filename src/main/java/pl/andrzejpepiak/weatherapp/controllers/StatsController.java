package pl.andrzejpepiak.weatherapp.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import pl.andrzejpepiak.weatherapp.model.WeatherData;
import pl.andrzejpepiak.weatherapp.model.WeatherStat;
import pl.andrzejpepiak.weatherapp.model.dao.WeatherStatDao;
import pl.andrzejpepiak.weatherapp.model.dao.impl.WeatherStatDaoImpl;
import pl.andrzejpepiak.weatherapp.model.utils.Connector;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StatsController implements Initializable{
    @FXML
    BarChart<String, Number > ChartWeather;

    @FXML
    ListView ListOfCities;

    @FXML
    Button backButton;

    @FXML
    Label AvgTemp;

    private String lastCityName;

    private Connector connector = Connector.getInstance();
    private WeatherStatDao weatherStatDao = new WeatherStatDaoImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registerButtonBackAction();
        loadCityNames();
        registerClickItemOnList();
        avgTemp();
    }

    private void registerClickItemOnList(){
        ListOfCities.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            loadChart((String) newValue);
        });
    }

    private void loadChart(String city){
        XYChart.Series series = new XYChart.Series();
        series.setName(city);
        int counter = 1;
        for(WeatherStat weatherStat : weatherStatDao.getLastSixStats(city)){
            series.getData().add(new XYChart.Data(""  + counter, weatherStat.getTemp()));
            counter++;
        }
        ChartWeather.getData().clear();
        ChartWeather.getData().add(series);
    }

    private void loadCityNames(){
        ListOfCities.setItems((FXCollections.observableList(weatherStatDao.getAllCities())));
    }
    private void registerButtonBackAction(){
        backButton.setOnMouseClicked(e -> {
            Stage stage = (Stage) backButton.getScene().getWindow();
            try {
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
                stage.setScene(new Scene(root,600,400));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }
    private void avgTemp(){

        ListOfCities.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            avgTempCities((String) newValue);
        });
    }

    private void avgTempCities (String newValue) {
        AvgTemp.setText(String.valueOf(weatherStatDao.countAvgTemp(newValue)));
    }
}
