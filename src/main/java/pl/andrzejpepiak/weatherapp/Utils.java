package pl.andrzejpepiak.weatherapp;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {
    public static String readWebsiteContext(String url){
        StringBuilder builder = new StringBuilder(); // string builder sluzy do łączenia stringow konkatenacja

        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
            InputStream i = httpURLConnection.getInputStream();
            int response = 0;
            while((response = i.read()) != -1){ //zwraca -1 wtedy kiedy nie ma juz co czytac
                builder.append((char)response);
            }

            i.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
}

