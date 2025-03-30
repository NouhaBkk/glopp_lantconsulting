package com.example.lantconsulting.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class CountryRegionService {

    private static final String BASE_URL = "https://restcountries.com/v3.1/name/";

    public String getRegionFromCountry(String countryName) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            String url = UriComponentsBuilder
                    .fromHttpUrl(BASE_URL + countryName)
                    .queryParam("fields", "region")
                    .toUriString();

            String response = restTemplate.getForObject(url, String.class);
            JSONArray jsonArray = new JSONArray(response);

            if (!jsonArray.isEmpty()) {
                JSONObject countryObj = jsonArray.getJSONObject(0);
                return countryObj.optString("region", "Autre");
            }

            return "Autre"; // fallback
        } catch (Exception e) {
            System.err.println("Erreur lors du fetch de la région pour " + countryName + " : " + e.getMessage());
            return "Autre";
        }
    }
}
