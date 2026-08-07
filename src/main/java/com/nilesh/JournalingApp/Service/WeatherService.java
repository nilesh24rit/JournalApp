package com.nilesh.JournalingApp.Service;

import com.nilesh.JournalingApp.Cache.AppCache;
import com.nilesh.JournalingApp.api.response.WeatherResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {
    @Value("${weather.api.key}")
    private  String api_key;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AppCache appCache;

    @Autowired
    private RedisService redisService;

    public WeatherResponse getWeather(String city){
        WeatherResponse weatherResponse = redisService.get("Weather_of_" + city, WeatherResponse.class);
        if (weatherResponse !=null){
            return weatherResponse;
        }else {

            String finalAPI = appCache.APP_CACHE.get("weather_api").replace("<city>", city).replace("<apikey>", api_key);
            ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
            WeatherResponse body = response.getBody();
            if (body !=null){
                redisService.set("Weather_of_" + city,body,300l);
            }
            return body;

        }

    }
}
