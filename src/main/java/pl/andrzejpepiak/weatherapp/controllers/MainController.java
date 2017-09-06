package pl.andrzejpepiak.weatherapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import pl.andrzejpepiak.weatherapp.model.WeatherData;
import pl.andrzejpepiak.weatherapp.model.WeatherStat;
import pl.andrzejpepiak.weatherapp.model.dao.WeatherStatDao;
import pl.andrzejpepiak.weatherapp.model.dao.impl.WeatherStatDaoImpl;
import pl.andrzejpepiak.weatherapp.model.service.IWeatherObserver;
import pl.andrzejpepiak.weatherapp.model.service.WeatherService;
import pl.andrzejpepiak.weatherapp.model.utils.Connector;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements IWeatherObserver, Initializable {


    @FXML
    Button Button;

    @FXML
    TextArea Text;

    @FXML
    TextField CityName;

    @FXML
    ProgressIndicator Load;

    @FXML
    Button Stats;

    private String lastCityName;

    private WeatherService weatherService = WeatherService.getService();
    private Connector connector = Connector.getInstance();
    private WeatherStatDao weatherStatDao = new WeatherStatDaoImpl();

    @Override
    public void onWeatherUpdate(WeatherData data) {
        weatherStatDao.saveStat(new WeatherStat(lastCityName, (float) data.getTemp()));
        Text.setText("Temp: " + (data.getTemp() - 273.15) + "\nHumidity: " + data.getHumidity() + "\nPressure: " + data.getPressure() + "\nClouds: " + data.getClouds());
        Load.setVisible(false);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        weatherService.registerObserver(this);
        registerShowButtonAction();
        registerEnterListener();
        registerButtonStatsAction();
    }
    private void registerButtonStatsAction(){
        Stats.setOnMouseClicked(e -> {
            Stage stage = (Stage) Stats.getScene().getWindow();
            try {
                Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("stats.fxml"));
                stage.setScene(new Scene(root,600,400));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });
    }


    private void registerShowButtonAction (){
        Button.setOnMouseClicked( e -> prepareRequestAndClear());
    }
    private void  registerEnterListener(){
        CityName.setOnKeyPressed( e -> {
            if(e.getCode() == KeyCode.ENTER){
              prepareRequestAndClear();
            }
        });
    }
    private void prepareRequestAndClear(){
        lastCityName = CityName.getText();
        Load.setVisible(true);
        weatherService.init(CityName.getText());
        CityName.clear();
    }
}
