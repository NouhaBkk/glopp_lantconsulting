import React, { useState, useEffect } from "react";
import "./ConseillerDashboard.css";
import { useNavigate } from "react-router-dom";
import API_BASE_URL from "./api";

const ConseillerDashboard = () => {
  const [conseiller, setConseiller] = useState(null);
  const navigate = useNavigate();
  const user = JSON.parse(sessionStorage.getItem("user"));

  useEffect(() => {
    if (user?.accountname) {
      fetch(`${API_BASE_URL}/api/conseillers/mon-profile?accountname=${user.accountname}`)
        .then((response) => response.json())
        .then((data) => {
          setConseiller(data);
        })
        .catch((error) => {
          console.error("Erreur lors du fetch du conseiller :", error);
        });
    }
  }, [user?.accountname]);

  if (!conseiller) {
    return <p>Chargement du tableau de bord...</p>;
  }

  return (
    <div className="conseiller-dashboard-container">
      <div className="conseiller-header">
        <h1>Bienvenue, {conseiller.nom} {conseiller.prenom}</h1>
      </div>

      <div className="conseiller-card">
        <h2>Vos Clients ({conseiller.clients?.length || 0})</h2>

        <div className="client-list">
          {conseiller.clients && conseiller.clients.length > 0 ? (
            conseiller.clients.map((client) => (
              <div key={client.id} className="client-card">
                <h3>{client.firstName} {client.lastName}</h3>
                <p>Email : {client.email}</p>
                <p>Téléphone : {client.phoneNumber}</p>
                <p>Adresse : {client.address}</p>
              </div>
            ))
          ) : (
            <p>Aucun client attribué pour l'instant.</p>
          )}
        </div>

        <button className="btn-refresh" onClick={() => window.location.reload()}>
          Rafraîchir
        </button>

        <button
          className="btn btn-primary"
          onClick={() => navigate("/declare-incident")}
        >
          Assistance Incident
        </button>
      </div>
    </div>
  );
};

export default ConseillerDashboard;
