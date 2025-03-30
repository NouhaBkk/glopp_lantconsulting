import React, { useEffect, useState } from "react";
import API_BASE_URL from "./api";

const ClientDashboard = () => {
  const [cases, setCases] = useState([]);

  useEffect(() => {
    fetch(`${API_BASE_URL}/api/clientcases/all`)
      .then((res) => res.json())
      .then((data) => setCases(data))
      .catch((error) => console.error("Erreur:", error));
  }, []);

  return (
    <div className="dashboard-container">
      <h2>Tableau de Bord Client</h2>
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>Statut</th>
            <th>Envoyer Document</th>
          </tr>
        </thead>
        <tbody>
          {cases.map((c) => (
            <tr key={c.id}>
              <td>{c.id}</td>
              <td>{c.status}</td>
              <td>
                <a href={`/upload/${c.id}`} className="upload-link">
                  📄 Ajouter un Document
                </a>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ClientDashboard;
