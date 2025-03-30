import { useState } from "react";

export default function UploadFile() {
  const [file, setFile] = useState(null);
  const [message, setMessage] = useState("");
  const [fileUrl, setFileUrl] = useState(""); 

  const handleFileChange = (event) => {
    setFile(event.target.files[0]);
  };

  const handleUpload = async () => {
    if (!file) {
      setMessage("Veuillez sélectionner un fichier.");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    try {
      const response = await fetch("http://backend/api/files/upload", {
        method: "POST",
        headers: {
          "Accept": "application/json",
        },
        body: formData,
      });

      if (response.ok) {
        const data = await response.json();
        setFileUrl(data.url);
        setMessage("Fichier uploadé avec succès !");
      } else {
        const errorData = await response.json();
        setMessage(`Erreur lors de l'upload : ${errorData.error || "Erreur inconnue"}`);
      }
    } catch (error) {
      setMessage("Erreur de connexion au serveur. Vérifiez si votre backend tourne.");
    }
  };

  return (
    <div style={{ textAlign: "center", padding: "20px" }}>
      <h2>Uploader un fichier sur Cloudinary</h2>
      <input type="file" onChange={handleFileChange} />
      <button onClick={handleUpload}>Uploader</button>
      <p>{message}</p>
      {fileUrl && (
        <div>
          <h3>Fichier uploadé :</h3>
          <a href={fileUrl} target="_blank" rel="noopener noreferrer">
            Voir le fichier
          </a>
        </div>
      )}
    </div>
  );
}
