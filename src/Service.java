
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Service  {
    public String country;
    public String city;
    public Map<String,String> countries;

    public Service(String country){
        this.country=country;
        countries = getCode();
    }

    public String getWeather(String miasto)  {
        this.city=miasto;
        Locale [] locales = Locale.getAvailableLocales();
        String code = "";
        for (int i=0; i<locales.length; i++) {
            if(locales[i].getDisplayCountry(new Locale("GB")).equals(country)){
                code = locales[i].getCountry();
            }
        }

        URL url = null;
        try {
            miasto += "," + code;
            url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+miasto+"&APPID=7bb25ef0d88254dead906c201748e489");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String json = "";
        try(BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))){
            String line;
            while ((line = in.readLine()) != null) json+=line + "\n";
        } catch (IOException e) {
            e.printStackTrace();
        }


        return json;}

    public Double getRateFor(String kod_waluty){

        Currency base = Currency.getInstance(new Locale("pl",countries.get(country)));
        System.out.println(base);

        URL url = null;
        try {
            url = new URL("https://api.exchangerate.host/latest?base="+base+"&symbols=" + kod_waluty);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        String json = "";

        try(BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))){
            String line;
            while ((line = in.readLine()) != null) json+=line + "\n";
        } catch (IOException e) {
            e.printStackTrace();
        }

        JSONParser jsonParser = new JSONParser();
        try {
            String s = ((JSONObject)jsonParser.parse(json)).get("rates").toString();
            json = ((JSONObject)jsonParser.parse(s)).get(kod_waluty).toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(json);

        return  Double.valueOf(json);
    }

    public Double getNBPRate()  {

        if(country.equals("Poland")){
            return 1d;
        }

        URL url = null;
        String code = Currency.getInstance(new Locale("pl", countries.get(country))).getCurrencyCode();
        try {
            url = new URL("http://api.nbp.pl/api/exchangerates/rates/a/"+code+"/");
            HttpURLConnection huc = (HttpURLConnection) url.openConnection();
            int responseCode = huc.getResponseCode();

            if(responseCode==404){
                url = new URL("http://api.nbp.pl/api/exchangerates/rates/b/"+code+"/");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        String json = "";

        try(BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()))){
            String line;
            while ((line = in.readLine()) != null) json+=line + "\n";

            JSONParser jsonParser =new JSONParser();
            JSONObject jsonObject1 = (JSONObject) jsonParser.parse(json);
            String rates =  jsonObject1.get("rates").toString();

            JSONArray jsonArray = (JSONArray) jsonParser.parse(rates);
            json = ((JSONObject)jsonParser.parse(jsonArray.get(0).toString())).get("mid").toString();

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return Double.valueOf(json);}


    public JFXPanel getCity(){

        JFXPanel jfxPanel = new JFXPanel();


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                WebView webView = new WebView();
                WebEngine webEngine = webView.getEngine();
                webEngine.load("https://en.wikipedia.org/wiki/"+city);
                jfxPanel.setScene(new Scene(webView));
            }
        });

        return  jfxPanel;
    }

    public static Map<String,String> getCode(){
        Map<String,String> map = new HashMap<>();
        Locale[] locales = Locale.getAvailableLocales();

        for(Locale locale : locales){
            if(!locale.getCountry().equals("")){
                map.put(locale.getDisplayCountry(new Locale("GB")),locale.getCountry());
            }
        }
        return  map;
    }


}
