package com.example.lantconsulting.controller;

import com.example.lantconsulting.entity.ReforestationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/reforestation")
public class ReforestationController {

    @Autowired
    private ReforestationService reforestationService;

    @PostMapping("/estimate")
    public Map<String, Object> estimate(@RequestBody Map<String, Object> body) {
        double co2 = Double.parseDouble(body.get("co2_kg").toString());

        int trees = reforestationService.calculateTreesToPlant(co2);
        double cost = reforestationService.calculateCompensationCost(co2);

        return Map.of(
            "trees", trees,
            "compensationCost", cost
        );
    }
}
