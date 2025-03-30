import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import "./EspacePersonnel.css";
import CarbonForm from "./CarbonForm";
import API_BASE_URL from "./api";

const EspacePersonnel = ({ accountname }) => {
  const [offers, setOffers] = useState([]);
  const [subscribedOffers, setSubscribedOffers] = useState([]);
  const [selectedOffer, setSelectedOffer] = useState(null);
  const [carbonFormData, setCarbonFormData] = useState({
    origine: "",
    destination: "",
    make: "",
    model: "",
    transportType: "",
  });
  const [carbonEmission, setCarbonEmission] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [showConfirmation, setShowConfirmation] = useState(false);
  const [doctors, setDoctors] = useState([]);
  const [selectedDoctor, setSelectedDoctor] = useState("");
  const [showDoctorForm, setShowDoctorForm] = useState(false);
  const [message, setMessage] = useState("");
  const [user, setUser] = useState(JSON.parse(sessionStorage.getItem("user")) || null);
  const [reducedPrice, setReducedPrice] = useState(null);
  const [showSubscriptions, setShowSubscriptions] = useState(false);
  const [showPayment, setShowPayment] = useState(false);
  const [compensationCost, setCompensationCost] = useState(0);
  const navigate = useNavigate();

  useEffect(() => {
    const storedUser = sessionStorage.getItem("user");
    const parsedUser = storedUser ? JSON.parse(storedUser) : null;

    if (parsedUser && parsedUser.id) {
      setUser(parsedUser);
      fetch(`${API_BASE_URL}/contracts/user/${parsedUser.id}`)
        .then((response) => response.json())
        .then((data) => setSubscribedOffers(data))
        .catch((error) =>
          console.error("Erreur récupération contrats utilisateur :", error)
        );
    } else {
      alert("Erreur : Impossible de récupérer votre ID utilisateur.");
    }

    fetch(`${API_BASE_URL}/offers`)
      .then((response) => response.json())
      .then((data) => setOffers(data))
      .catch((error) => console.error("Erreur récupération des offres :", error));

    fetch(`${API_BASE_URL}/doctors`)
      .then((response) => response.json())
      .then((data) => setDoctors(data))
      .catch((error) => console.error("Erreur chargement des médecins :", error));
  }, [accountname]);

  const handleSubscribeClick = (offer) => {
    setSelectedOffer(offer);
    setShowForm(true);
    setCarbonEmission(null);
    setReducedPrice(null);
  };

  const handlePaiement = async () => {
    const alreadySubscribed = subscribedOffers.some(
      (contract) => contract.offer.id === selectedOffer.id
    );

    if (alreadySubscribed) {
      alert("Vous avez déjà souscrit à cette offre.");
      setShowPayment(false);
      return;
    }

    try {
      const response = await fetch(`${API_BASE_URL}/contracts/subscribe`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ offerId: selectedOffer.id, accountname }),
      });

      if (response.ok) {
        const newContract = await response.json();
        setSubscribedOffers((prev) => [...prev, newContract]);
        setShowPayment(false);
        navigate("/paiement-form");
      } else if (response.status === 400) {
        const errorMessage = await response.text();
        alert(errorMessage);
        setShowPayment(false);
      } else {
        alert("Erreur lors de la souscription.");
      }
    } catch (err) {
      console.error(err);
      alert("Erreur réseau !");
    }
  };

  const handleReadyToPay = ({ reducedPrice, compensationCost }) => {
    setReducedPrice(reducedPrice);
    setCompensationCost(compensationCost);
    setShowForm(false);
    setShowPayment(true);
  };

  const handleCloseForm = () => {
    setShowForm(false);
    setCarbonEmission(null);
  };

  const handleContactDoctor = async (e) => {
    e.preventDefault();

    if (!user?.id) {
      alert("Erreur : Impossible de récupérer votre ID utilisateur.");
      return;
    }

    const requestData = {
      userId: user.id,
      doctorId: selectedDoctor,
      diagnosis: "Consultation initiale",
      treatmentPlan: "En attente du médecin",
      message,
    };

    try {
      const response = await fetch(`${API_BASE_URL}/medical-cases/request`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(requestData),
      });

      if (response.ok) {
        alert("Votre demande a été envoyée !");
        setMessage("");
        setSelectedDoctor("");
        setShowDoctorForm(false);
      } else {
        alert("Erreur lors de l'envoi de la demande.");
      }
    } catch (error) {
      console.error("Erreur envoi médecin :", error);
    }
  };

  return (
    <div className="espace-personnel-container">
      <h1>Bienvenue dans votre espace personnel</h1>

      <nav className="navbar-espace">
        <ul>
          <li><Link to="/mes-dossiers">📁 Mes Dossiers</Link></li>
          <li><a href="#contact-medecin" onClick={(e) => { e.preventDefault(); setShowDoctorForm(true); }}>🩺 Contacter un Médecin</a></li>
          <li><a href="#souscriptions" onClick={(e) => { e.preventDefault(); setShowSubscriptions(!showSubscriptions); }}>📄 Mes Souscriptions</a></li>
        </ul>
      </nav>

      <h2>Offres Disponibles</h2>
      <div className="offers-list">
        {offers.length > 0 ? offers.map((offer) => {
          const isAlreadySubscribed = subscribedOffers.some(
            (contract) => contract.offer.id === offer.id
          );
          return (
            <div key={offer.id} className="offer-card">
              <h5>{offer.title}</h5>
              <p>{offer.description}</p>
              <p><strong>Prix : {offer.price} € / mois</strong></p>
              <button
                onClick={() => handleSubscribeClick(offer)}
                className="item-btn"
                disabled={isAlreadySubscribed}
              >
                {isAlreadySubscribed ? "Déjà souscrite" : "Souscrire"}
              </button>
            </div>
          );
        }) : <p>Aucune offre disponible pour le moment.</p>}
      </div>

      {showForm && selectedOffer && (
        <CarbonForm
          offerId={selectedOffer.id}
          offerPrice={selectedOffer.price}
          accountname={accountname}
          onClose={handleCloseForm}
          onSubscriptionSuccess={(newContract) =>
            setSubscribedOffers([...subscribedOffers, newContract])
          }
          onReadyToPay={handleReadyToPay}
        />
      )}

      {showDoctorForm && (
        <div className="modal" id="contact-medecin">
          <div className="modal-content">
            <span className="close-btn" onClick={() => setShowDoctorForm(false)}>&times;</span>
            <h2>Contacter un Médecin</h2>
            <form onSubmit={handleContactDoctor}>
              <label>Médecin :</label>
              <select value={selectedDoctor} onChange={(e) => setSelectedDoctor(e.target.value)} required>
                <option value="">Sélectionner un médecin</option>
                {doctors.map((doctor) => (
                  <option key={doctor.id} value={doctor.id}>
                    {doctor.accountname}
                  </option>
                ))}
              </select>
              <label>Message :</label>
              <textarea value={message} onChange={(e) => setMessage(e.target.value)} placeholder="Expliquez votre problème..." required />
              <button type="submit">Envoyer la demande</button>
            </form>
          </div>
        </div>
      )}

      {showPayment && reducedPrice !== null && compensationCost !== null && (
        <div className="modal">
          <div className="modal-content">
            <span className="close-btn" onClick={() => setShowPayment(false)}>&times;</span>
            <h2>🧾 Paiement</h2>
            <p>Montant offre après réduction : <strong>{reducedPrice.toFixed(2)} €</strong></p>
            <p>Frais de compensation carbone : <strong>{compensationCost.toFixed(2)} €</strong></p>
            <p><strong>Total à payer : {(reducedPrice + compensationCost).toFixed(2)} €</strong></p>
            <button className="pay-btn" onClick={handlePaiement}>💳 Payer</button>
          </div>
        </div>
      )}

      {showSubscriptions && (
        <>
          <h2 className="section-title">Vos Souscriptions</h2>
          <div className="contracts-table-container">
            {subscribedOffers.length > 0 ? (
              <table className="contracts-table">
                <thead>
                  <tr>
                    <th>Offre</th>
                    <th>Début</th>
                    <th>Fin</th>
                    <th>Contrat</th>
                  </tr>
                </thead>
                <tbody>
                  {subscribedOffers.map((contract) => (
                    <tr key={contract.id}>
                      <td>{contract.offer.title}</td>
                      <td>{contract.startDate}</td>
                      <td>{contract.endDate}</td>
                      <td>
                        <button
                          onClick={() => {
                            fetch(`${API_BASE_URL}/contracts/${contract.id}/pdf`)
                              .then((response) => {
                                if (!response.ok) throw new Error("Erreur lors du téléchargement");
                                return response.blob();
                              })
                              .then((blob) => {
                                const url = window.URL.createObjectURL(blob);
                                const a = document.createElement("a");
                                a.href = url;
                                a.download = `Contrat_${contract.offer.title}_${contract.id}.pdf`;
                                document.body.appendChild(a);
                                a.click();
                                a.remove();
                              })
                              .catch(() => alert("Erreur téléchargement du contrat."));
                          }}
                          className="download-btn"
                        >
                          📄 Télécharger PDF
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            ) : (
              <p>Aucune souscription en cours.</p>
            )}
          </div>
        </>
      )}
    </div>
  );
};

export default EspacePersonnel;
