import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import "./DeclareIncident.css";
import API_BASE_URL from "./api";

const DeclareIncident = () => {
  const [clients, setClients] = useState([]);
  const [formData, setFormData] = useState({
    userId: "",
    country: "",
    assistanceType: "",
    description: "",
  });

  const location = useLocation();
  const navigate = useNavigate();
  const queryParams = new URLSearchParams(location.search);
  const userIdFromURL = queryParams.get("clientId");

  useEffect(() => {
    const user = JSON.parse(sessionStorage.getItem("user"));
    if (!user) return;

    fetch(`${API_BASE_URL}/conseillers/mon-profile?accountname=${user.accountname}`)
      .then((res) => res.json())
      .then((data) => {
        setClients(data.clients || []);
        if (userIdFromURL) {
          setFormData((prev) => ({ ...prev, userId: userIdFromURL }));
        }
      })
      .catch((err) => console.error("Erreur lors du chargement des clients :", err));
  }, [userIdFromURL]);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch(`${API_BASE_URL}/clientcases/create`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        alert("✅ Dossier d'incident créé avec succès !");
        navigate("/conseiller/dashboard");
      } else {
        const message = await response.text();
        alert("❌ Échec de la déclaration : " + message);
      }
    } catch (error) {
      console.error("Erreur lors de la déclaration :", error);
      alert("❌ Une erreur est survenue.");
    }
  };

  return (
    <div className="declare-incident-container">
      <div className="incident-card">
        <h2>Déclarer un Incident</h2>
        <form onSubmit={handleSubmit} className="incident-form">
          <label>Utilisateur concerné :</label>
          <select
            name="userId"
            value={formData.userId}
            onChange={handleChange}
            required
          >
            <option value="">Sélectionner un utilisateur</option>
            {clients.map((client) => (
              <option key={client.id} value={client.id}>
                {client.firstName} {client.lastName}
              </option>
            ))}
          </select>

          <label>Pays de l'incident :</label>
          <input
            type="text"
            name="country"
            value={formData.country}
            onChange={handleChange}
            placeholder="France, Tunisie, etc."
            required
          />

          <label>Type d’assistance :</label>
          <select
            name="assistanceType"
            value={formData.assistanceType}
            onChange={handleChange}
            required
          >
            <option value="">Choisissez un type</option>
            <option value="Dépannage">Dépannage</option>
            <option value="Remorquage">Remorquage</option>
            <option value="Accident">Accident</option>
            <option value="Hospitalisation">Hospitalisation</option>
            <option value="Bagage perdu">Bagage perdu</option>
          </select>

          <label>Description :</label>
          <textarea
            name="description"
            value={formData.description}
            onChange={handleChange}
            placeholder="Décrivez la situation"
            required
          />

          <button type="submit" className="btn-declare">
            Envoyer la demande
          </button>
        </form>
      </div>
    </div>
  );
};

export default DeclareIncident;
