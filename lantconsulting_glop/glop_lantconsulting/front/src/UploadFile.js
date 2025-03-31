import { useState } from "react";
import API_BASE_URL from "./components/api"; 

export default function UploadFile() {
  const [file, setFile] = useState(null);
  const [message, setMessage] = useState("");
  const [messageType, setMessageType] = useState("info"); // "success" | "error"
  const [fileUrl, setFileUrl] = useState(""); 

  const handleFileChange = (event) => {
    setFile(event.target.files[0]);
    setMessage("");
    setFileUrl("");
  };

  const handleUpload = async () => {
    if (!file) {
      setMessage("Veuillez sélectionner un fichier.");
      setMessageType("error");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch(`${API_BASE_URL}/files/upload`, {
        method: "POST",
        headers: {
          Accept: "application/json",
        },
        body: formData,
      });

      if (response.ok) {
        const data = await response.json();
        setFileUrl(data.url);
        setMessage("✅ Fichier uploadé avec succès !");
        setMessageType("success");
      } else {
        const errorData = await response.json();
        setMessage(`❌ Erreur lors de l'upload : ${errorData.error || "Erreur inconnue"}`);
        setMessageType("error");
      }
    } catch (error) {
      setMessage("❌ Erreur de connexion au serveur. Vérifiez si votre backend tourne.");
      setMessageType("error");
    }
  };

  return (
    <div style={{ textAlign: "center", padding: "20px", fontFamily: "Segoe UI, sans-serif" }}>
      <h2 style={{ color: "#1e3a8a" }}>📤 Uploader un fichier sur Cloudinary</h2>
      <input type="file" onChange={handleFileChange} style={{ marginBottom: "10px" }} />
      <br />
      <button
        onClick={handleUpload}
        style={{
          padding: "10px 20px",
          backgroundColor: "#1e3a8a",
          color: "#fff",
          border: "none",
          borderRadius: "5px",
          cursor: "pointer",
          marginTop: "10px"
        }}
      >
        Uploader
      </button>

      {message && (
        <p style={{
          marginTop: "15px",
          color: messageType === "success" ? "#15803d" : "#b91c1c",
          fontWeight: 500
        }}>
          {message}
        </p>
      )}

      {fileUrl && (
        <div style={{ marginTop: "20px" }}>
          <h3>📎 Fichier disponible :</h3>
          <a href={fileUrl} target="_blank" rel="noopener noreferrer" style={{ color: "#1e40af", fontWeight: 600 }}>
            Voir le fichier
          </a>
        </div>
      )}
    </div>
  );
}
