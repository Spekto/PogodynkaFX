package pl.andrzejpepiak.weatherapp.model.service;

import org.json.JSONObject;

import pl.andrzejpepiak.weatherapp.Config;
import pl.andrzejpepiak.weatherapp.Utils;
import pl.andrzejpepiak.weatherapp.model.WeatherData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class WeatherService {
    private static  WeatherService INSTANCE = new WeatherService();// deklaracja w cesze , projekt tworzy sie przy deklaracji (odrazu)// jedna instancja na caly projekt
    public static WeatherService getService() {// singleton , jedna instancja klasy jest per caly projekt
        return INSTANCE;// zwracamy utwozona przez nas instancje, te trzy linijki to jest caly wzorzec singleton
    }

    private List<IWeatherObserver> observerList; // lista po to zeby przechowywac obserwatorów, zeby ich powiadomic
    private ExecutorService executorService;


    private WeatherService(){//deklaracja w konsruktorze, tworzy sie dopiero kiedy metoda zostnie przynajmniej raz wygenerowana
        executorService = Executors.newFixedThreadPool(2);//thread pool dwu wątkowy ( wielo wątkowość)
        observerList = new ArrayList<>();// implementacja listy
    }

    public void registerObserver(IWeatherObserver observer){//metoda obsługująca liste
        observerList.add(observer);
    }

    private void notifyObservers(WeatherData data){
        for (IWeatherObserver iWeatherObserver: observerList) {//ta metoda powiadamia wszystkich obserwatorów, foreach dlatego zeby powiadomic kazdego obserwatora
            iWeatherObserver.onWeatherUpdate(data); // to pobiera w glownej pogodzie, onWeatherUpdate to skalowalna paczka ( łatwiej w przypadku rozwoju aplikacji)
        }
    }

    public void init(final String city){
        Runnable taskinit = new Runnable() {

            public void run() {
                String text = Utils.readWebsiteContext(Config.API_URL+city+"&appid="+Config.APP_KEY);
                parseJsonFromString(text);//zapisanie tresci strony do zmiennej text// po to dodawalismy wielo watkowosc zeby JSON odpalał sie w innym atku niz wątek główny
            }
        };
       executorService.execute(taskinit);// po to dodawalismy wielo watkowosc zeby JSON odpalał sie w innym atku niz wątek glowny
    }

    private void parseJsonFromString(String text) {//na końcu tej metody nasza pogoda zostanie obsluzona
        WeatherData data = new WeatherData();

        JSONObject root = new JSONObject(text);// komenda odczytujaca JSONa
        JSONObject main = root.getJSONObject("main"); // root to jest caly tekst JSONa

        data.setTemp(main.getDouble("temp"));
        data.setHumidity(main.getInt("humidity"));
        data.setPressure(main.getInt("pressure"));

        JSONObject cloudsObject = root.getJSONObject("clouds");
        data.setClouds(cloudsObject.getInt("all"));

            notifyObservers(data);//implementacja wzorca observator

    }
}
