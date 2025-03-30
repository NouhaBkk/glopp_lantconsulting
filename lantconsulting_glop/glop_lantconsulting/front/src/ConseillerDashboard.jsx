import React, { useEffect, useState } from "react";
import axios from "axios";

const ConseillerDashboard = () => {
  const [clients, setClients] = useState([]);

  useEffect(() => {
    const fetchClients = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/conseillers/clients");
        setClients(response.data);
      } catch (error) {
        console.error("Erreur lors de la récupération des clients", error);
      }
    };

    fetchClients();
  }, []);

  return (
    <div className="conseiller-dashboard">
      <h2>Dashboard Conseiller</h2>
      <h3>Mes Clients</h3>
      <ul>
        {clients.length > 0 ? (
          clients.map((client) => (
            <li key={client.id}>
              {client.firstname} {client.lastname} - {client.email}
            </li>
          ))
        ) : (
          <p>Aucun client rattaché.</p>
        )}
      </ul>
    </div>
  );
};

export default ConseillerDashboard;
