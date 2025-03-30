import React, { useEffect, useState, useCallback } from "react";
import API_BASE_URL from "./api";

const MesDossiers = () => {
  const [cases, setCases] = useState([]);
  const [selectedFiles, setSelectedFiles] = useState({});
  const user = JSON.parse(sessionStorage.getItem("user"));

  const loadCases = useCallback(() => {
    if (!user?.id) return;

    fetch(`${API_BASE_URL}/clientcases/by-user/${user.id}`)
      .then((res) => res.json())
      .then(setCases)
      .catch(console.error);
  }, [user?.id]);

  useEffect(() => {
    loadCases();
  }, [loadCases]);

  const handleFileChange = (e, caseId) => {
    setSelectedFiles((prev) => ({
      ...prev,
      [caseId]: e.target.files[0],
    }));
  };

  const handleFileUpload = (e, caseId) => {
    e.preventDefault();
    const file = selectedFiles[caseId];
    if (!file) return;

    const formData = new FormData();
    formData.append("file", file);
    formData.append("sender", "CLIENT");

    fetch(`${API_BASE_URL}/clientcases/${caseId}/documents`, {
      method: "POST",
      body: formData,
    })
      .then((res) => res.json())
      .then(() => {
        loadCases();
        setSelectedFiles((prev) => ({ ...prev, [caseId]: null }));
      })
      .catch(console.error);
  };

  const openDocument = (doc, caseId) => {
    const isPdf = doc.fileName.toLowerCase().endsWith(".pdf");
    const viewerUrl = isPdf
      ? `https://docs.google.com/viewer?url=${encodeURIComponent(doc.url)}&embedded=true`
      : doc.url;

    window.open(viewerUrl, "_blank");

    if (doc.sender === "PARTNER" && !doc.viewed) {
      fetch(`${API_BASE_URL}/clientcases/${caseId}/documents/mark-viewed`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ url: doc.url, viewerRole: "CLIENT" }),
      })
        .then(() => loadCases())
        .catch(console.error);
    }
  };

  const renderDocuments = (documents, caseId, senderType) => (
    <div style={{ marginBottom: 10 }}>
      {documents.length ? (
        <div style={{ display: "flex", flexDirection: "column", gap: "8px" }}>
          {documents.map((doc) => (
            <div
              key={`${caseId}-${doc.sender}-${doc.uploadedAt}-${doc.fileName}-${doc.url}`}
              style={{
                border: "1px solid #e2e8f0",
                borderLeft: "4px solid #1e3a8a",
                borderRadius: "6px",
                padding: "10px 12px",
                display: "flex",
                justifyContent: "space-between",
                alignItems: "center",
                backgroundColor: "#ffffff",
              }}
            >
              <div>
                <div
                  onClick={() => openDocument(doc, caseId)}
                  style={{
                    fontSize: "15px",
                    fontWeight: 600,
                    color: "#1e3a8a",
                    cursor: "pointer",
                  }}
                >
                  📎 {doc.fileName}
                </div>
                <div style={{ fontSize: "12px", color: "#64748b", marginTop: "2px" }}>
                  📅 {new Date(doc.uploadedAt).toLocaleString("fr-FR")}
                </div>
              </div>

              {senderType === "CLIENT" && doc.sender === "CLIENT" && (
                <span
                  style={{
                    fontSize: "12px",
                    fontWeight: 600,
                    color: doc.viewed ? "#15803d" : "#b91c1c",
                    backgroundColor: doc.viewed ? "#dcfce7" : "#fee2e2",
                    padding: "4px 8px",
                    borderRadius: "20px",
                  }}
                >
                  {doc.viewed ? "✅ Vu par le partenaire" : "❌ Non vu par le partenaire"}
                </span>
              )}
            </div>
          ))}
        </div>
      ) : (
        <p style={{ color: "#94a3b8", fontStyle: "italic" }}>Aucun document.</p>
      )}
    </div>
  );

  return (
    <div style={{
      padding: "20px",
      maxWidth: "1200px",
      margin: "auto",
      fontFamily: "'Segoe UI', sans-serif"
    }}>
      <h1 style={{
        color: "#0f172a",
        textAlign: "center",
        fontSize: "26px",
        marginBottom: "25px"
      }}>
        📂 Mes dossiers
      </h1>

      {cases.length === 0 ? (
        <p style={{ textAlign: "center", color: "#64748b" }}>Aucun dossier trouvé.</p>
      ) : (
        <div style={{
          display: "flex",
          flexWrap: "wrap",
          justifyContent: "center",
          gap: "20px"
        }}>
          {cases.map((c) => {
            const myDocs = c.documents.filter((doc) => doc.sender === "CLIENT");
            const partnerDocs = c.documents.filter((doc) => doc.sender === "PARTNER");

            return (
              <div
                key={c.id}
                style={{
                  width: "340px",
                  border: "1px solid #e2e8f0",
                  borderRadius: "10px",
                  padding: "16px",
                  backgroundColor: "#f9fafb",
                  boxShadow: "0 4px 8px rgba(0,0,0,.05)",
                }}
              >
                <h2 style={{ color: "#1e3a8a", fontSize: "18px", marginBottom: "10px" }}>
                  📌 Dossier #{c.id}
                </h2>
                <p><strong>Type :</strong> {c.assistanceType}</p>
                <p><strong>Description :</strong> {c.description}</p>
                <p>
                  <strong>Statut :</strong>{" "}
                  <span style={{
                    backgroundColor: c.status === "Clôturé" ? "#dbeafe" : "#dcfce7",
                    color: c.status === "Clôturé" ? "#1e40af" : "#15803d",
                    padding: "2px 8px",
                    borderRadius: "12px",
                    fontWeight: 600,
                    fontSize: "13px"
                  }}>
                    {c.status}
                  </span>
                </p>
                <p><strong>Partenaire :</strong> {c.partner?.accountname || "Aucun"}</p>
                <p><strong>Date :</strong> {new Date(c.creationDate).toLocaleDateString("fr-FR")}</p>

                <div style={{ marginTop: "10px" }}>
                  <h3 style={{ color: "#1e3a8a", fontSize: "15px", marginBottom: "6px" }}>📤 Mes documents</h3>
                  {renderDocuments(myDocs, c.id, "CLIENT")}

                  <h3 style={{ color: "#0f766e", fontSize: "15px", marginTop: "12px", marginBottom: "6px" }}>📥 Du partenaire</h3>
                  {renderDocuments(partnerDocs, c.id, "PARTNER")}
                </div>

                <form onSubmit={(e) => handleFileUpload(e, c.id)} style={{ marginTop: "10px" }}>
                  <label style={{ fontWeight: 600, color: "#0f172a", fontSize: "14px", display: "block", marginBottom: "5px" }}>
                    ➕ Ajouter un document :
                  </label>
                  <div style={{ display: "flex", gap: "8px", alignItems: "center" }}>
                    <input
                      type="file"
                      onChange={(e) => handleFileChange(e, c.id)}
                      style={{
                        flex: 1,
                        border: "1px solid #cbd5e1",
                        borderRadius: "6px",
                        padding: "5px",
                        fontSize: "12px",
                      }}
                    />
                    <button
                      type="submit"
                      style={{
                        backgroundColor: "#1e3a8a",
                        color: "white",
                        padding: "6px 10px",
                        borderRadius: "6px",
                        fontWeight: 600,
                        border: "none",
                        cursor: "pointer",
                        fontSize: "13px",
                      }}
                    >
                      📤 Envoyer
                    </button>
                  </div>
                </form>
              </div>
            );
          })}
        </div>
      )}
    </div>
  );
};

export default MesDossiers;
