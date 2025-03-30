package com.example.lantconsulting;

import com.example.lantconsulting.entity.Offer;
import com.example.lantconsulting.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component("offerDataInitializer")
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private OfferRepository offerRepository;

    @Override
    public void run(String... args) throws Exception {
        if (offerRepository.count() == 0) {
            Offer vehicleOffer = new Offer();
            vehicleOffer.setTitle("Assurance Véhicule");
            vehicleOffer.setDescription(
                "Assurance couvrant les déplacements en cas de panne, ainsi que la réparation via nos partenaires."
            );
            vehicleOffer.setPrice(99.0);

            Offer personalTravelOffer = new Offer();
            personalTravelOffer.setTitle("Assurance Voyage Personnel");
            personalTravelOffer.setDescription(
                "Assurance garantissant un abri en cas de problème avec le vol ou l'hôtel."
            );
           personalTravelOffer.setPrice(130.0);

            Offer professionalTravelOffer = new Offer();
            professionalTravelOffer.setTitle("Assurance Voyage Professionnel");
            professionalTravelOffer.setDescription(
                "Assurance incluant la couverture du matériel de l'assuré lors de voyages professionnels."
            );
           professionalTravelOffer.setPrice(230.0);

            offerRepository.save(vehicleOffer);
            offerRepository.save(personalTravelOffer);
            offerRepository.save(professionalTravelOffer);

            System.out.println("Offres initialisées avec succès.");
        }
    }
}
