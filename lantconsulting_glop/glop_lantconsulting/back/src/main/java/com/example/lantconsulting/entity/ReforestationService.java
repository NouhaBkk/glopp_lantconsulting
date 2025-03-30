package com.example.lantconsulting.entity;

import org.springframework.stereotype.Service;

@Service
public class ReforestationService {

    private static final double CO2_ABSORBED_PER_TREE_KG = 20.0;
    private static final double COST_PER_TREE_EURO = 2.0;

    public int calculateTreesToPlant(double co2Kg) {
        return (int) Math.ceil(co2Kg / CO2_ABSORBED_PER_TREE_KG);
    }

    public double calculateCompensationCost(double co2Kg) {
        int trees = calculateTreesToPlant(co2Kg);
        return trees * COST_PER_TREE_EURO;
    }
}