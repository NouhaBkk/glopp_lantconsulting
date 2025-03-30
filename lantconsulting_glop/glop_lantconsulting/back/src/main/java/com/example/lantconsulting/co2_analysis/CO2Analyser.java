package com.example.lantconsulting.co2_analysis;

import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CO2Analyser {

    public static final String CARBON_API_KEY = "ILK3pLHW1X4wtNqVrv8rg";
    public static final String OPENROUTE_SERVICE_KEY = "5b3ce3597851110001cf6248e07a0020f0454073978af70dafbe393a";
    public static final String GOOGLE_API_KEY = "AIzaSyDMufDKGVuVz8HtrLnNqnSmelHeSrQjMLA";
    public static final String HEADER = "Authorization";
    public static final String HEADER_VALUE = "Bearer " + CARBON_API_KEY;

    public final RestTemplate restTemplate = new RestTemplate();

    public double calculateCarbonEmission(String make, String model, double distance) {
        String url = "https://www.carboninterface.com/api/v1/estimates";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER, HEADER_VALUE);
        headers.set("Content-Type", "application/json");

        JSONObject requestBody = new JSONObject();
        requestBody.put("type", "vehicle");
        requestBody.put("distance_unit", "km");
        requestBody.put("distance_value", distance);
        requestBody.put("vehicle_model_id", getVehicleModelId(make, model));

        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        JSONObject responseBody = new JSONObject(response.getBody());
        return responseBody.getJSONObject("data").getJSONObject("attributes").getDouble("carbon_kg");
    }

    public List<String> getAllVehiculesMakes() {
        String url = "https://www.carboninterface.com/api/v1/vehicle_makes";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER, HEADER_VALUE);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        JSONArray makesArray = new JSONArray(response.getBody());
        List<String> makes = new ArrayList<>();

        for (int i = 0; i < makesArray.length(); i++) {
            try {
                JSONObject makeWrapper = makesArray.getJSONObject(i);
                JSONObject data = makeWrapper.getJSONObject("data");
                JSONObject attributes = data.getJSONObject("attributes");
                makes.add(attributes.getString("name"));
            } catch (Exception e) {
                System.err.println("Erreur lors de l'analyse d'une marque : " + e.getMessage());
            }
        }

        return makes;
    }

    public String getMakeId(String make) {
        String url = "https://www.carboninterface.com/api/v1/vehicle_makes";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER, HEADER_VALUE);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        JSONArray makesArray = new JSONArray(response.getBody());

        for (int i = 0; i < makesArray.length(); i++) {
            JSONObject makeWrapper = makesArray.getJSONObject(i);
            JSONObject data = makeWrapper.getJSONObject("data");
            JSONObject attributes = data.getJSONObject("attributes");
            if (attributes.getString("name").equalsIgnoreCase(make)) {
                return data.getString("id");
            }
        }

        throw new IllegalArgumentException("Make not found: " + make);
    }

    public List<String> getAllVehiculesModelsForAMake(String make) {
        String makeId = getMakeId(make);
        String url = "https://www.carboninterface.com/api/v1/vehicle_makes/" + makeId + "/vehicle_models";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER, HEADER_VALUE);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        JSONArray modelsArray = new JSONArray(response.getBody());
        List<String> models = new ArrayList<>();

        for (int i = 0; i < modelsArray.length(); i++) {
            try {
                JSONObject wrapper = modelsArray.getJSONObject(i);
                JSONObject data = wrapper.getJSONObject("data");
                JSONObject attributes = data.getJSONObject("attributes");
                models.add(attributes.getString("name"));
            } catch (Exception e) {
                System.err.println("Erreur lors de l’analyse d’un modèle : " + e.getMessage());
            }
        }

        return new ArrayList<>(new HashSet<>(models)); // remove duplicates
    }

    public String getVehicleModelId(String make, String model) {
        String makeId = getMakeId(make);
        String url = "https://www.carboninterface.com/api/v1/vehicle_makes/" + makeId + "/vehicle_models";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER, HEADER_VALUE);
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        JSONArray modelsArray = new JSONArray(response.getBody());

        for (int i = 0; i < modelsArray.length(); i++) {
            JSONObject wrapper = modelsArray.getJSONObject(i);
            JSONObject data = wrapper.getJSONObject("data");
            JSONObject attributes = data.getJSONObject("attributes");

            if (attributes.getString("name").equalsIgnoreCase(model)) {
                return data.getString("id");
            }
        }

        throw new IllegalArgumentException("Model not found: " + model);
    }

    public double calculateRoute(String origin, String destination) {
        String url = String.format(
            "https://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&key=%s",
            origin, destination, GOOGLE_API_KEY);

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            JSONObject jsonResponse = new JSONObject(response.getBody());
            JSONArray routes = jsonResponse.getJSONArray("routes");

            if (routes.length() > 0) {
                JSONObject legs = routes.getJSONObject(0).getJSONArray("legs").getJSONObject(0);
                return legs.getJSONObject("distance").getDouble("value") / 1000.0;
            } else {
                throw new IllegalArgumentException("No route found.");
            }
        } else {
            throw new RuntimeException("Failed to fetch route from Google API. Status: " + response.getStatusCode());
        }
    }

    public String getCoordinates(String address) {
        String baseUrl = "https://nominatim.openstreetmap.org/search";
        String url = baseUrl + "?q=" + address.replace(" ", "+") + "&format=json&addressdetails=1";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            JSONArray jsonArray = new JSONArray(response.getBody());

            if (jsonArray.length() > 0) {
                JSONObject firstResult = jsonArray.getJSONObject(0);
                String lat = firstResult.getString("lat");
                String lon = firstResult.getString("lon");
                return lat + "," + lon;
            } else {
                throw new IllegalArgumentException("No coordinates found for address: " + address);
            }
        } else {
            throw new RuntimeException("Failed to fetch coordinates from Nominatim API. Status: " + response.getStatusCode());
        }
    }

    public double calculeReductionCO2(double totalCO2) {
        if (totalCO2 <= 10) return 30.0;
        else if (totalCO2 <= 30) return 25.0;
        else if (totalCO2 <= 50) return 20.0;
        else if (totalCO2 <= 70) return 15.0;
        else if (totalCO2 <= 100) return 10.0;
        else return 0.0;
    }

    private static final Map<String, Double> AVERAGE_EMISSIONS = Map.of(
        "car_thermic", 180.0,
        "car_hybrid", 110.0,
        "car_electric", 50.0,
        "train", 10.0,
        "plane_short", 280.0,
        "plane_long", 120.0,
        "bus", 90.0,
        "bike", 0.0,
        "walk", 0.0,
        "scooter", 35.0
    );

    public double estimateGenericCO2(String transportType, double distanceKm) {
        double gCO2PerKm = AVERAGE_EMISSIONS.getOrDefault(transportType.toLowerCase(), 0.0);
        return (gCO2PerKm * distanceKm) / 1000.0;
    }
}
