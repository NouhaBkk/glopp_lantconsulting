package com.example.lantconsulting.entity;


import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.pdf.PdfWriter;



import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;

@Service
public class PdfService {

    public ByteArrayInputStream generateContractPdf(String offerTitle, String accountname, double price, String dateDebut, String dateFin) {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();

            // Logo
            Image logo = Image.getInstance("src/main/resources/static/logo.png");
            logo.scaleToFit(100, 100);
            logo.setAlignment(Element.ALIGN_CENTER);
            document.add(logo);

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20);
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14);
            Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

            document.add(new Paragraph("Contrat de Souscription d’Assurance", titleFont));
            document.add(new Paragraph("Généré le : " + LocalDate.now()));
            document.add(Chunk.NEWLINE);
            document.add(new LineSeparator());

            // Infos client
            document.add(new Paragraph("Informations du souscripteur", sectionFont));
            document.add(new Paragraph("Nom du souscripteur : " + accountname, textFont));
            document.add(new Paragraph("Identifiant : " + "2025-" + accountname.toUpperCase(), textFont));
            document.add(new Paragraph("Adresse : [à compléter]", textFont));
            document.add(Chunk.NEWLINE);

            // Offre
            document.add(new Paragraph("Détails de l’offre souscrite", sectionFont));
            document.add(new Paragraph("Offre choisie : " + offerTitle, textFont));
            document.add(new Paragraph("Prix mensuel : " + price + " €", textFont));
            document.add(new Paragraph("Période de couverture : du " + dateDebut + " au " + dateFin, textFont));
            document.add(new Paragraph("Réduction carbone : Oui / Non (si applicable)", textFont));
            document.add(Chunk.NEWLINE);

            // Conditions
            document.add(new Paragraph("Conditions générales", sectionFont));
            document.add(new Paragraph(
                    "Le présent contrat couvre les risques liés à l’utilisation d’un véhicule personnel dans les limites prévues par les conditions générales de vente. "
                            + "La garantie est effective pendant la durée mentionnée ci-dessus.", textFont));
            document.add(Chunk.NEWLINE);

            // Résiliation
            document.add(new Paragraph("Modalités de résiliation", sectionFont));
            document.add(new Paragraph(
                    "La résiliation est possible à tout moment après 12 mois, avec un préavis de 30 jours. "
                            + "Elle peut se faire en ligne ou par courrier recommandé avec accusé de réception.", textFont));
            document.add(Chunk.NEWLINE);

            // Engagements
            document.add(new Paragraph("Engagements", sectionFont));
            document.add(new Paragraph(
                    "L’assureur s’engage à fournir les services décrits dans l’offre choisie. "
                            + "Le souscripteur s’engage à respecter les conditions d’utilisation et à informer l’assureur de toute modification importante.", textFont));
            document.add(Chunk.NEWLINE);

            // Signature
            document.add(new Paragraph("Signatures", sectionFont));
            document.add(new Paragraph("Fait à : ____________", textFont));
            document.add(new Paragraph("Date : " + dateDebut, textFont));
            document.add(Chunk.NEWLINE);
            document.add(new Paragraph("Signature du souscripteur : __________________________", textFont));
            document.add(new Paragraph("Signature de l’assureur : ____________________________", textFont));

            document.close();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

}