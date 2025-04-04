import React, { useState, useEffect } from "react";
import API_BASE_URL from "./api";

const DoctorDashboard = ({ doctorId }) => {
  const [patients, setPatients] = useState([]);
  const [selectedPatient, setSelectedPatient] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [diagnosis, setDiagnosis] = useState("");
  const [treatmentPlan, setTreatmentPlan] = useState("");
  const [status, setStatus] = useState("EN_ATTENTE");
  const [loading, setLoading] = useState(true);
  const [message, setMessage] = useState("");

  useEffect(() => {
    if (!doctorId) return;

    fetch(`${API_BASE_URL}/medical-cases/doctor/${doctorId}`)
      .then((response) => response.json())
      .then((data) => {
        setPatients(Array.isArray(data) ? data : []);
      })
      .catch((error) => {
        console.error("Erreur lors du chargement des patients :", error);
        setPatients([]);
      })
      .finally(() => setLoading(false));
  }, [doctorId]);

  const handleAddMedicalCaseClick = (patient) => {
    setSelectedPatient(patient);
    setShowForm(true);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!selectedPatient) return;

    const clientId = selectedPatient.clientId;
    if (!clientId) {
      alert("Erreur : Aucun utilisateur associé !");
      return;
    }

    const medicalCase = {
      client: { id: clientId },
      doctor: { id: doctorId },
      diagnosis,
      treatmentPlan,
      status,
      createdDate: new Date().toISOString().split("T")[0],
      message,
    };

    try {
      const response = await fetch(`${API_BASE_URL}/medical-cases/create`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(medicalCase),
      });

      if (response.ok) {
        alert("Dossier médical ajouté !");
        fetch(`${API_BASE_URL}/medical-cases/doctor/${doctorId}`)
          .then((response) => response.json())
          .then((newData) => {
            setPatients(Array.isArray(newData) ? newData : []);
          });
        setDiagnosis("");
        setTreatmentPlan("");
        setStatus("EN_ATTENTE");
        setMessage("");
      } else {
        alert("Erreur lors de l'ajout.");
      }
    } catch (error) {
      console.error("Erreur :", error);
    }
  };

  const groupByClient = (cases) => {
    const grouped = {};
    cases.forEach((c) => {
      if (!grouped[c.clientId]) {
        grouped[c.clientId] = {
          clientName: `${c.firstName} ${c.lastName}`,
          cases: [],
        };
      }
      grouped[c.clientId].cases.push(c);
    });
    return grouped;
  };

  return (
    <div className="doctor-dashboard" style={{ padding: "20px", fontFamily: "Arial, sans-serif" }}>
      <h2 style={{ color: "#0056b3" }}>🩺 Dashboard Médecin</h2>
      <h3>📌 Liste des Patients</h3>

      {loading ? (
        <p>Chargement des patients...</p>
      ) : patients.length === 0 ? (
        <p>Aucun patient en attente.</p>
      ) : (
        Object.entries(groupByClient(patients)).map(([clientId, group]) => (
          <div key={clientId} style={{ marginBottom: "40px" }}>
            <h3 style={{ color: "#333" }}>👤 {group.clientName}</h3>
            <table border="1" style={{ width: "100%", textAlign: "left", borderCollapse: "collapse", marginTop: "10px" }}>
              <thead>
                <tr style={{ backgroundColor: "#f2f2f2" }}>
                  <th>Diagnostic</th>
                  <th>Statut</th>
                  <th>Message</th>
                  <th>Date de Création</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                {group.cases.map((patient, index) => (
                  <tr key={patient.id ? `case-${patient.id}` : `index-${index}`}>
                    <td>{patient.diagnosis || "Aucun diagnostic"}</td>
                    <td>{patient.status}</td>
                    <td>{patient.message?.trim() !== "" ? patient.message : "Aucun message"}</td>
                    <td>{patient.createdDate || "Non disponible"}</td>
                    <td>
                      <button
                        onClick={() => handleAddMedicalCaseClick(patient)}
                        style={{
                          backgroundColor: "#28a745",
                          color: "white",
                          padding: "5px",
                          border: "none",
                          cursor: "pointer",
                        }}
                      >
                        ➕ Ajouter un Dossier
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ))
      )}

      {showForm && selectedPatient && (
        <div className="modal">
          <div className="modal-content">
            <span className="close-btn" onClick={() => setShowForm(false)}>&times;</span>
            <h3>➕ Ajouter un Dossier Médical pour {selectedPatient.client?.firstName || selectedPatient.firstName} {selectedPatient.client?.lastName || selectedPatient.lastName}</h3>
            <form onSubmit={handleSubmit} style={{ display: "flex", flexDirection: "column", gap: "10px" }}>
              <label>Diagnostic :</label>
              <textarea value={diagnosis} onChange={(e) => setDiagnosis(e.target.value)} required style={{ width: "100%", padding: "5px" }}></textarea>

              <label>Plan de Traitement :</label>
              <textarea value={treatmentPlan} onChange={(e) => setTreatmentPlan(e.target.value)} required style={{ width: "100%", padding: "5px" }}></textarea>

              <label>Statut :</label>
              <select value={status} onChange={(e) => setStatus(e.target.value)} style={{ width: "100%", padding: "5px" }}>
                <option value="EN_ATTENTE">EN_ATTENTE</option>
                <option value="EN_COURS_DE_TRAITEMENT">EN_COURS_DE_TRAITEMENT</option>
                <option value="TERMINE">TERMINE</option>
              </select>

              <label>Message :</label>
              <textarea value={message} onChange={(e) => setMessage(e.target.value)} placeholder="Ajouter un message optionnel..." style={{ width: "100%", padding: "5px" }}></textarea>

              <button type="submit" style={{ backgroundColor: "#0056b3", color: "white", padding: "10px", border: "none", cursor: "pointer" }}>
                Ajouter Dossier
              </button>
            </form>
          </div>
        </div>
      )}
    </div>
  );
};

export default DoctorDashboard;
