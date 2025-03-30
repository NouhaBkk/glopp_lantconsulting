package com.example.lantconsulting.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.lantconsulting.co2_analysis.CO2Analyser;
import java.util.Map;

/**
 * REST controller for CO2 analysis operations.
 * Provides endpoints to calculate distances, get coordinates, make and model IDs, 
 * and calculate carbon emissions based on vehicle information and routes.
 */
@RestController
@RequestMapping("/api/co2")
public class CO2AnalyserController {


    @Autowired
    private CO2Analyser co2Analyser;



    /**
     * Calcule la distance entre deux points donnés.
     *
     * @param origin La ville ou l'adresse de départ.
     * @param destination La ville ou l'adresse d'arrivée.
     * @return La distance calculée entre l'origine et la destination.
     */
    @GetMapping("/distance")
    public double calculateDistance(@RequestParam String origin, @RequestParam String destination) {
        return co2Analyser.calculateRoute(origin, destination);
    }


    /**
     * Récupère les coordonnées géographiques pour une adresse donnée.
     *
     * @param address L'adresse pour laquelle obtenir les coordonnées.
     * @return Les coordonnées géographiques sous forme de chaîne de caractères.
     */
    @GetMapping("/coordinates")
    public String getCoordinates(@RequestParam String address) {
        return co2Analyser.getCoordinates(address);
    }


    /**
     * Récupère l'identifiant du fabricant en fonction du nom du fabricant fourni.
     *
     * @param make Le nom du fabricant pour lequel l'identifiant doit être récupéré.
     * @return L'identifiant du fabricant correspondant au nom fourni.
     */
    @GetMapping("/makeId")
    public String getMakeId(@RequestParam String make) {
        return co2Analyser.getMakeId(make);
    }


    /**
     * Récupère l'identifiant du modèle de véhicule en fonction de la marque et du modèle fournis.
     *
     * @param make La marque du véhicule.
     * @param model Le modèle du véhicule.
     * @return L'identifiant du modèle de véhicule.
     */
    @GetMapping("/modelId")
    public String getModelId(@RequestParam String make, @RequestParam String model) {
        return co2Analyser.getVehicleModelId(make, model);
    }


    /**
     * Calcule les émissions de carbone pour un véhicule donné en fonction de sa marque, 
     * de son modèle et de la distance parcourue.
     *
     * @param make La marque du véhicule.
     * @param model Le modèle du véhicule.
     * @param distance La distance parcourue en kilomètres.
     * @return Les émissions de carbone en grammes.
     */
    @PostMapping("/carbonEmission")
    public double calculateCarbonEmission(@RequestParam String make, @RequestParam String model, @RequestParam double distance) {
        return co2Analyser.calculateCarbonEmission(make, model, distance);
    }

    /**
     * Récupère toutes les marques de véhicules.
     *
     * @return une liste de chaînes de caractères représentant les marques de véhicules.
     */
    @GetMapping("/getAllMakes")
    public List<String> getAllMakes() {
        return co2Analyser.getAllVehiculesMakes();
    }

    /**
     * Récupère tous les modèles de véhicules pour une marque donnée.
     *
     * @param make la marque des véhicules pour laquelle récupérer les modèles
     * @return une liste de chaînes de caractères représentant les modèles de véhicules
     */
    @GetMapping("/getAllModels")
    public List<String> getAllModels(@RequestParam String make) {
        return co2Analyser.getAllVehiculesModelsForAMake(make);
    }


//    @PostMapping("/routeEmission")
//    public double calculateRouteAndEmission(@RequestParam String origin, @RequestParam String destination, @RequestParam String make, @RequestParam String model) {
//        double distance = co2Analyser.calculateRoute(origin, destination);
//        return co2Analyser.calculateCarbonEmission(make, model, distance);
//    }
    
    
    /**
     * Calcule les émissions de CO2 pour un trajet donné en fonction de l'origine, 
     * de la destination, de la marque et du modèle du véhicule.
     *
     * @param origin      La ville ou le point de départ du trajet.
     * @param destination La ville ou le point d'arrivée du trajet.
     * @param make        La marque du véhicule.
     * @param model       Le modèle du véhicule.
     * @return            Les émissions de CO2 pour le trajet spécifié.
     * @throws IllegalArgumentException Si l'origine, la destination, la marque ou le modèle ne sont pas fournis.
     * @throws IllegalStateException    Si la distance calculée est invalide.
     * @throws RuntimeException         En cas d'erreur lors du calcul des émissions de route.
     */
    @PostMapping("/routeEmission")
    public double calculateRouteAndEmission(@RequestParam String origin,
                                            @RequestParam String destination,
                                            @RequestParam String make,
                                            @RequestParam String model) {
        // Valider les paramètres
        if (origin == null || origin.isEmpty() || destination == null || destination.isEmpty()) {
            throw new IllegalArgumentException("Origin and destination must be provided.");
        }
        if (make == null || make.isEmpty() || model == null || model.isEmpty()) {
            throw new IllegalArgumentException("Make and model must be provided.");
        }

        try {
            double distance = co2Analyser.calculateRoute(origin, destination);
            if (distance <= 0) {
                throw new IllegalStateException("Calculated distance is invalid: " + distance);
            }
            return co2Analyser.calculateCarbonEmission(make, model, distance);
        } catch (Exception e) {
            throw new RuntimeException("Error calculating route emission: " + e.getMessage(), e);
        }
    }


    @PostMapping("/getReducedPrice")
    public double getReducedPrice(@RequestBody Map<String, Object> body) {
        double co2 = Double.parseDouble(body.get("co2_kg").toString());
        double price = Double.parseDouble(body.get("price").toString());
        return Math.max(price - co2 * 0.1, 0);
    }

    

    
    /**
     * Calcule les émissions de CO2 pour un trajet générique (train, avion, bus, etc.)
     *
     * @param origin        Ville de départ
     * @param destination   Ville d’arrivée
     * @param transportType Type de transport (ex: train, plane_short, bus, etc.)
     * @return              Émissions estimées en kg CO2
     */
    @PostMapping("/estimateCO2")
    public double estimateCO2(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam String transportType) {

        if (origin == null || destination == null || transportType == null ||
            origin.isEmpty() || destination.isEmpty() || transportType.isEmpty()) {
            throw new IllegalArgumentException("Tous les champs doivent être renseignés.");
        }

        double distance = co2Analyser.calculateRoute(origin, destination);
        return co2Analyser.estimateGenericCO2(transportType, distance);
    }

    
    
    
    
    
    
    
    
}