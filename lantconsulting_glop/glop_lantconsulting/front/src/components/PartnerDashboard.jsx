import React, { useEffect, useState } from "react";
import API_BASE_URL from "./api"; 

const PartnerDashboard = () => {
  const [groupedCases, setGroupedCases] = useState({});
  const [accountname, setAccountname] = useState("");
  const [selectedFiles, setSelectedFiles] = useState({});

  useEffect(() => {
    const user = JSON.parse(sessionStorage.getItem("user"));
    if (!user?.accountname) return;

    setAccountname(user.accountname);
    fetch(`${API_BASE_URL}/partners/cases/by-accountname?accountname=${user.accountname}`)
      .then((res) => res.json())
      .then((data) => {
        const grouped = {};
        data.forEach((c) => {
          const client = c.user?.accountname || "Client inconnu";
          if (!grouped[client]) grouped[client] = [];
          grouped[client].push(c);
        });
        setGroupedCases(grouped);
      })
      .catch(console.error);
  }, []);

  const handleFileChange = (e, caseId) => {
    setSelectedFiles((prev) => ({
      ...prev,
      [caseId]: e.target.files[0],
    }));
  };

  const handleUpload = (e, caseId) => {
    e.preventDefault();
    const file = selectedFiles[caseId];
    if (!file) return;

    const formData = new FormData();
    formData.append("file", file);
    formData.append("sender", "PARTNER");

    fetch(`${API_BASE_URL}/clientcases/${caseId}/documents`, {
      method: "POST",
      body: formData,
    })
      .then((res) => res.json())
      .then((doc) => {
        if (doc.url) {
          setGroupedCases((prev) => {
            const updated = { ...prev };
            Object.keys(updated).forEach((client) => {
              updated[client] = updated[client].map((c) =>
                c.id === caseId
                  ? { ...c, documents: [...(c.documents || []), { ...doc, viewed: false, uploadedAt: new Date() }] }
                  : c
              );
            });
            return updated;
          });
        }
      })
      .catch(console.error);
  };

  const openDocument = (doc) => {
    const isPdf = doc.fileName.toLowerCase().endsWith(".pdf");
    const viewerUrl = isPdf
      ? `https://docs.google.com/viewer?url=${encodeURIComponent(doc.url)}&embedded=true`
      : doc.url;
    window.open(viewerUrl, "_blank");
  };

  const updateStatus = (caseId, newStatus) => {
    fetch(`${API_BASE_URL}/partners/clientcases/update/${caseId}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ status: newStatus }),
    })
      .then(() => {
        setGroupedCases((prev) => {
          const updated = { ...prev };
          Object.keys(updated).forEach((client) => {
            updated[client] = updated[client].map((c) =>
              c.id === caseId ? { ...c, status: newStatus } : c
            );
          });
          return updated;
        });
      })
      .catch(console.error);
  };

  const renderDocuments = (docs, caseId, role) => (
    <div style={{ display: "flex", flexDirection: "column", gap: "8px", marginTop: "8px" }}>
      {docs.map((doc) => (
        <div
          key={`${caseId}-${doc.fileName}-${doc.uploadedAt}`}
          style={{
            border: "1px solid #e2e8f0",
            borderLeft: "4px solid #1e3a8a",
            borderRadius: "6px",
            padding: "10px 12px",
            backgroundColor: "#ffffff",
            display: "flex",
            justifyContent: "space-between",
            alignItems: "center",
          }}
        >
          <div>
            <div
              onClick={() => openDocument(doc)}
              style={{
                fontSize: "15px",
                fontWeight: 600,
                color: "#1e3a8a",
                cursor: "pointer",
                transition: "all 0.2s",
              }}
              onMouseEnter={(e) => {
                e.currentTarget.style.textDecoration = "underline";
                e.currentTarget.style.backgroundColor = "#f1f5f9";
                e.currentTarget.style.padding = "2px 6px";
                e.currentTarget.style.borderRadius = "6px";
              }}
              onMouseLeave={(e) => {
                e.currentTarget.style.textDecoration = "none";
                e.currentTarget.style.backgroundColor = "transparent";
                e.currentTarget.style.padding = "0";
              }}
            >
              {doc.fileName}
            </div>
            <div style={{ fontSize: "12px", color: "#64748b", marginTop: "2px" }}>
              📅 {new Date(doc.uploadedAt).toLocaleString("fr-FR")}
            </div>
          </div>
          {role === "PARTNER" && doc.sender === "PARTNER" && (
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
              {doc.viewed ? "✅ Vu " : "❌ Non vu "}
            </span>
          )}
        </div>
      ))}
    </div>
  );

  return (
    <div style={styles.container}>
      <h1 style={styles.welcome}>Bonjour {accountname} 👋</h1>
      <h2 style={styles.title}>🌍 Dossiers groupés par client</h2>

      {Object.keys(groupedCases).length === 0 ? (
        <p style={styles.empty}>Aucun dossier trouvé.</p>
      ) : (
        Object.entries(groupedCases).map(([client, cases]) => (
          <div key={client} style={styles.clientBlock}>
            <h3 style={styles.clientName}>👤 {client}</h3>
            <div style={styles.cardGrid}>
              {cases.map((c) => {
                const myDocs = c.documents.filter((doc) => doc.sender === "PARTNER");
                const clientDocs = c.documents.filter((doc) => doc.sender === "CLIENT");

                return (
                  <div key={c.id} style={styles.card}>
                    <h4 style={styles.cardTitle}>Dossier #{c.id}</h4>
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
                    <button
                      style={{
                        ...styles.button,
                        backgroundColor: c.status === "Clôturé" ? "#9ca3af" : "#64748b",
                        cursor: c.status === "Clôturé" ? "not-allowed" : "pointer",
                      }}
                      onClick={() => updateStatus(c.id, "Clôturé")}
                      disabled={c.status === "Clôturé"}
                    >
                      Clôturer le dossier
                    </button>

                    <div style={{ marginTop: 15 }}>
                      <strong>📁 Documents envoyés par moi :</strong>
                      {renderDocuments(myDocs, c.id, "PARTNER")}

                      <strong style={{ marginTop: 12, display: "block" }}>📁 Documents envoyés par le client :</strong>
                      {renderDocuments(clientDocs, c.id, "CLIENT")}
                    </div>

                    <form onSubmit={(e) => handleUpload(e, c.id)} style={{ marginTop: 15 }}>
                      <div style={{ display: "flex", gap: "10px", alignItems: "center" }}>
                        <label style={{
                          backgroundColor: "#64748b",
                          color: "white",
                          padding: "8px 12px",
                          borderRadius: "6px",
                          fontWeight: 600,
                          cursor: "pointer",
                          border: "none",
                          fontSize: "14px",
                          display: "inline-block"
                        }}>
                          Sélectionner un fichier
                          <input
                            type="file"
                            onChange={(e) => handleFileChange(e, c.id)}
                            style={{ display: "none" }}
                          />
                        </label>
                        <span style={{ fontSize: "13px", color: "#334155" }}>
                          {selectedFiles[c.id]?.name || "Aucun fichier"}
                        </span>
                        <button type="submit" style={styles.button}>Envoyer</button>
                      </div>
                    </form>
                  </div>
                );
              })}
            </div>
          </div>
        ))
      )}
    </div>
  );
};

const styles = {
  container: { padding: "20px", background: "#f1f5f9", fontFamily: "Segoe UI, sans-serif" },
  welcome: { fontSize: "24px", textAlign: "center", color: "#0f172a" },
  title: { textAlign: "center", color: "#2563eb", fontSize: "28px" },
  empty: { textAlign: "center", color: "#555" },
  clientBlock: { marginBottom: "40px" },
  clientName: { fontSize: "22px", color: "#0f172a" },
  cardGrid: { display: "grid", gridTemplateColumns: "repeat(auto-fit, minmax(320px, 1fr))", gap: 20 },
  card: { background: "#fff", borderRadius: 12, padding: 20, boxShadow: "0 4px 8px rgba(0,0,0,0.05)" },
  cardTitle: { color: "#1e3a8a" },
  button: {
    marginTop: 10,
    color: "white",
    padding: "8px 12px",
    borderRadius: 6,
    border: "none",
    fontWeight: 600,
    cursor: "pointer"
  },
};

export default PartnerDashboard;
