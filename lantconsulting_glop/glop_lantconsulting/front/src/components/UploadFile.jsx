import React, { useState } from "react";
import { useParams, Link } from "react-router-dom";
import "./UploadFile.css";
import API_BASE_URL from "./api"; // ✅ Ajout de l'import

const UploadFile = () => {
  const { caseId } = useParams();
  const [file, setFile] = useState(null);
  const [message, setMessage] = useState("");
  const [fileUrl, setFileUrl] = useState("");

  const handleFileChange = (event) => {
    setFile(event.target.files[0]);
  };

  const handleUpload = async () => {
    if (!file) {
      setMessage("Sélectionne un fichier !");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch(`${API_BASE_URL}/clientcases/${caseId}/upload`, {
        method: "POST",
        body: formData,
      });

      if (response.ok) {
        const data = await response.json();
        setFileUrl(data.url);
        setMessage("Fichier uploadé avec succès !");
      } else {
        setMessage("Erreur lors de l'upload.");
      }
    } catch (error) {
      setMessage("Erreur de connexion au serveur.");
    }
  };

  return (
    <div className="upload-container">
      <h1>📤 Ajouter un document au dossier {caseId}</h1>
      <input type="file" onChange={handleFileChange} />
      <button onClick={handleUpload}>Uploader</button>
      <p>{message}</p>
      {fileUrl && (
        <a href={fileUrl} target="_blank" rel="noopener noreferrer">
          📄 Voir le fichier
        </a>
      )}
      <br />
      <Link to="/mes-dossiers" className="btn-retour">
        ⬅ Retour aux Dossiers
      </Link>
    </div>
  );
};

export default UploadFile;
