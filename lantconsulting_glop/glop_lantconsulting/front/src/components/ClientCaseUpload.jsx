import React, { useState } from "react";
import API_BASE_URL from "./api";

const ClientCaseUpload = () => {
  const [file, setFile] = useState(null);
  const [caseId, setCaseId] = useState("");
  const [message, setMessage] = useState("");

  const handleFileChange = (event) => {
    setFile(event.target.files[0]);
  };

  const handleUpload = async () => {
    if (!file || !caseId) {
      setMessage("⚠️ Sélectionnez un fichier et entrez un ID de dossier !");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch(`${API_BASE_URL}/api/clientcases/${caseId}/upload`, {
        method: "POST",
        body: formData,
      });

      if (response.ok) {
        setMessage("✅ Fichier uploadé avec succès !");
      } else {
        setMessage("❌ Erreur lors de l'upload.");
      }
    } catch (error) {
      setMessage("❌ Erreur de connexion au serveur.");
    }
  };

  return (
    <div className="upload-container">
      <h2>Envoyer un Document</h2>
      <input type="text" placeholder="ID du dossier" value={caseId} onChange={(e) => setCaseId(e.target.value)} />
      <input type="file" onChange={handleFileChange} />
      <button onClick={handleUpload}>📤 Envoyer</button>
      <p>{message}</p>
    </div>
  );
};

export default ClientCaseUpload;
