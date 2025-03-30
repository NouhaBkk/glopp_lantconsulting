import React from "react";
import { useLocation } from "react-router-dom";

const Viewer = () => {
  const query = new URLSearchParams(useLocation().search);
  const url = query.get("url");

  return (
    <div style={{ padding: "20px" }}>
      <h2 style={{ textAlign: "center", marginBottom: "20px" }}>🖼️ Visualisation du document</h2>
      {url ? (
        <iframe
          src={`https://docs.google.com/gview?embedded=true&url=${encodeURIComponent(url)}`}
          title="Visualisation du PDF"
          width="100%"
          height="800px"
          style={{ border: "1px solid #ccc", borderRadius: "8px" }}
        ></iframe>
      ) : (
        <p>URL invalide ou manquante</p>
      )}
    </div>
  );
};

export default Viewer;
