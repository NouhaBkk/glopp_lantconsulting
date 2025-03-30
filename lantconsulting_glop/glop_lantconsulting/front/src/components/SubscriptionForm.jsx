import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import "./SubscriptionForm.css";
import API_BASE_URL from "./api"; // ✅ Import de la base URL

const SubscriptionForm = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const offerId = location.state?.offerId;

  const [formData, setFormData] = useState({
    origine: "",
    destination: "",
    make: "",
    model: "",
  });

  const [carbonEmission, setCarbonEmission] = useState(null);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const queryParams = new URLSearchParams({
      origin: formData.origine.trim(),
      destination: formData.destination.trim(),
      make: formData.make.trim(),
      model: formData.model.trim(),
    }).toString();

    try {
      const response = await fetch(`${API_BASE_URL}/co2/carbonEmission?${queryParams}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json",
        },
      });

      if (response.ok) {
        const emission = await response.json();
        setCarbonEmission(emission);
      } else {
        const errorText = await response.text();
        alert(`Erreur lors du calcul de l'empreinte carbone: ${errorText}`);
      }
    } catch (error) {
      console.error("Erreur de requête:", error);
      alert("Une erreur est survenue lors de la requête.");
    }
  };

  const handleConfirmSubscription = async () => {
    if (!offerId) {
      alert("Aucune offre sélectionnée.");
      return;
    }

    try {
      const response = await fetch(`${API_BASE_URL}/contracts/subscribe`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({ offerId }),
      });

      if (response.ok) {
        alert("Souscription réussie !");
        navigate("/");
      } else {
        alert("Échec de la souscription.");
      }
    } catch (error) {
      console.error("Erreur lors de la souscription:", error);
      alert("Une erreur est survenue lors de la souscription.");
    }
  };

  return (
    <div className="subscription-container">
      <h2>Informations sur votre voyage</h2>
      <form onSubmit={handleSubmit} className="subscription-form">
        <label>
          Lieu de départ:
          <input type="text" name="origine" value={formData.origine} onChange={handleChange} required />
        </label>
        <label>
          Lieu d'arrivée:
          <input type="text" name="destination" value={formData.destination} onChange={handleChange} required />
        </label>
        <label>
          Marque du véhicule:
          <input type="text" name="make" value={formData.make} onChange={handleChange} required />
        </label>
        <label>
          Modèle du véhicule:
          <input type="text" name="model" value={formData.model} onChange={handleChange} required />
        </label>
        <button type="submit" className="calculate-btn">Calculer l'empreinte carbone</button>
      </form>

      {carbonEmission !== null && (
        <div className="carbon-result">
          <p>Votre trajet émet environ <strong>{carbonEmission} kg</strong> de CO2.</p>
          <button onClick={handleConfirmSubscription} className="confirm-btn">Souscrire</button>
          <button onClick={() => navigate("/")} className="cancel-btn">Annuler</button>
        </div>
      )}
    </div>
  );
};

export default SubscriptionForm;
