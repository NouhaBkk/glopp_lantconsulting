import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import "./EspacePersonnel.css";
import CarbonForm from "./CarbonForm";
import { useNavigate } from "react-router-dom"; 


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
  const [carbonEmission, setCarbonEmission] = useState(null); // Empreinte carbone calculée
  const [showForm, setShowForm] = useState(false); // Affiche la modale pour le formulaire
  const navigate = useNavigate();
  const [showConfirmation, setShowConfirmation] = useState(false); // Confirmation avant souscription
  const [doctors, setDoctors] = useState([]);
  const [selectedDoctor, setSelectedDoctor] = useState("");
  const [showDoctorForm, setShowDoctorForm] = useState(false); 
  const [message, setMessage] = useState("");
  const storedUser = JSON.parse(localStorage.getItem("loggedInUser"));
  const [user, setUser] = useState(storedUser || null);
  const [reducedPrice, setReducedPrice] = useState(null);
  const [showSubscriptions, setShowSubscriptions] = useState(false);
  const [showPayment, setShowPayment] = useState(false);
  const [compensationCost, setCompensationCost] = useState(0);
  useEffect(() => {
    console.log(" Chargement de EspacePersonnel...");

    const storedUser = sessionStorage.getItem("user");
    const parsedUser = storedUser ? JSON.parse(storedUser) : null;
    
    console.log(" Utilisateur récupéré dans `EspacePersonnel` :", parsedUser);

    if (!parsedUser || !parsedUser.id) {
        console.error("Aucun utilisateur trouvé !");
        alert("Erreur : Impossible de récupérer votre ID utilisateur.");
        setUser(null);
    } else {
        console.log(" Utilisateur valide !");
        setUser(parsedUser);
    }
    // Récupérer les offres disponibles
    fetch("http://localhost:8080/api/offers")
      .then((response) => response.json())
      .then((data) => setOffers(data))
      .catch((error) => console.error("Erreur lors de la récupération des offres :", error));

    // Récupérer les contrats de l'utilisateur
    if (parsedUser && parsedUser.id) {
      fetch(`http://localhost:8080/api/contracts/user/${parsedUser.id}`)
        .then((response) => response.json())
        .then((data) => setSubscribedOffers(data))
        .catch((error) =>
          console.error("Erreur lors de la récupération des contrats de l'utilisateur :", error)
        );
    }
    
      fetch("http://localhost:8080/api/doctors")
      .then((response) => response.json())
      .then((data) => {
        console.log("Médecins récupérés :", data); // 🔍 Vérification ici
        setDoctors(data);
      })
      .catch((error) => console.error("Erreur lors du chargement des médecins :", error));
  }, [accountname]);
  

  const handleSubscribeClick = (offer) => {
    setSelectedOffer(offer);
    setShowForm(true); // Ouvre la modale
    setCarbonEmission(null); // Réinitialiser l'empreinte carbone
    setReducedPrice(null);
  };

  const handleFormChange = (e) => {
    const { name, value } = e.target;
    setCarbonFormData({
      ...carbonFormData,
      [name]: value,
    });
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();

    if (!carbonFormData.origine || !carbonFormData.destination || !carbonFormData.make || !carbonFormData.model) {
      alert("Veuillez remplir tous les champs.");
      return;
    }

    const queryParams = new URLSearchParams({
      origin: carbonFormData.origine.trim(),
      destination: carbonFormData.destination.trim(),
      make: carbonFormData.make.trim(),
      model: carbonFormData.model.trim(),
    });

    try {
      const response = await fetch(
        `http://localhost:8080/api/co2/routeEmission?${queryParams.toString()}`,
        { method: "POST" }
      );

      if (response.ok) {
        const emission = await response.json();
        setCarbonEmission(emission);
      
        const treesToPlant = Math.ceil(emission / 20);
        const compensation = treesToPlant * 2;
        setCompensationCost(compensation);
      
        const reduction = emission * 0.01;
        const finalPrice = selectedOffer.price - reduction;
        setReducedPrice(finalPrice);
      
        // On passe à l'étape paiement
        setShowConfirmation(false);
        setShowForm(false);
        setShowPayment(true);
      
      
      } else {
        alert("Erreur lors du calcul de l'empreinte carbone.");
      }
    } catch (error) {
      console.error("Erreur lors du calcul de l'empreinte carbone :", error);
      alert("Une erreur est survenue.");
    }
  };
  const handlePaiement = async () => {
    // Vérifie si l'utilisateur a déjà cette offre
    const alreadySubscribed = subscribedOffers.some(
      (contract) => contract.offer.id === selectedOffer.id
    );
  
    if (alreadySubscribed) {
      alert("Vous avez déjà souscrit à cette offre.");
      setShowPayment(false);
      return;
    }
  
    try {
      const response = await fetch("http://localhost:8080/api/contracts/subscribe", {
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
  
  
  const handleSubscribeConfirm = async () => {
    try {
      const response = await fetch("http://localhost:8080/api/contracts/subscribe", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ offerId: selectedOffer, accountname: accountname }),
      });

      if (response.ok) {
        const newContract = await response.json();
        setSubscribedOffers([...subscribedOffers, newContract]);
        //alert("Souscription réussie !");
        setShowForm(false);
        setShowConfirmation(false);
        setCarbonEmission(null);
        //setCarbonFormData({ origine: "", destination: "", make: "", model: "" });
      } else {
        alert("Erreur lors de la souscription.");
      }
    } catch (error) {
      console.error("Erreur lors de la souscription :", error);
      alert("Une erreur est survenue.");
    }
  };

  const handleCancelConfirmation = () => {
    setShowConfirmation(false);
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

    if (!user || !user.id) {
        alert("Erreur : Impossible de récupérer votre ID utilisateur.");
        return;
    }

    const requestData = {
        userId: user.id, 
        doctorId: selectedDoctor,
        diagnosis: "Consultation initiale",
        treatmentPlan: "En attente du médecin",
        message: message,
      };

    try {
        const response = await fetch("http://localhost:8080/api/medical-cases/request", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(requestData),
        });

        if (response.ok) {
            alert("Votre demande a été envoyée avec succès !");
            setMessage("");
            setSelectedDoctor("");
            setShowDoctorForm(false);
        } else {
            alert("Erreur lors de l'envoi de la demande.");
        }
    } catch (error) {
        console.error("Erreur lors de l'envoi :", error);
    }
};
  return (
    <div className="espace-personnel-container">

      <h1>Bienvenue dans votre espace personnel</h1>

      <nav className="navbar-espace">
  <ul>
    <li><Link to="/mes-dossiers">📁 Mes Dossiers</Link></li>
    <li>
      <a href="#contact-medecin" onClick={(e) => {
        e.preventDefault();
        setShowDoctorForm(true);
      }}>
        🩺 Contacter un Médecin
      </a>
    </li>
    <li>
      <a href="#souscriptions" onClick={(e) => {
        e.preventDefault();
        setShowSubscriptions(!showSubscriptions);
      }}>
        📄 Mes Souscriptions
      </a>
    </li>
  </ul>
</nav>

      {/* Liste des offres disponibles */}
      <h2>Offres Disponibles</h2>
      <div className="offers-list">
      {offers.length > 0 ? (
  offers.map((offer) => {
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
  })
) : (
  <p>Aucune offre disponible pour le moment.</p>
)}

      </div>

      {/* Modale pour le formulaire */}
      {showForm && selectedOffer && (
        <CarbonForm
        offerId={selectedOffer.id}
        offerPrice={selectedOffer.price}
        accountname={accountname}
        onClose={() => setShowForm(false)}
        onSubscriptionSuccess={(newContract) =>
          setSubscribedOffers([...subscribedOffers, newContract])
        }
        onReadyToPay={handleReadyToPay}
      />
      
      )}
{/* Formulaire pour contacter un médecin */}
{showDoctorForm && (
        <div className="modal" id="contact-medecin">
          <div className="modal-content">
            <span className="close-btn" onClick={() => setShowDoctorForm(false)}>
              &times;
            </span>
            <h2>Contacter un Médecin</h2>
            <form onSubmit={handleContactDoctor}>
              <label>Médecin :</label>
              <select value={selectedDoctor} onChange={(e) => setSelectedDoctor(e.target.value)} required>
                <option value="">Sélectionner un médecin</option>
                {doctors.map((doctor) => (
                  <option key={doctor.id} value={doctor.id}>
                 {doctor.accountname} {/* Remplace doctor.firstName et doctor.lastName par doctor.accountname */}
                  </option>
                ))}
              </select>

              <label>Message :</label>
              <textarea value={message} onChange={(e) => setMessage(e.target.value)} placeholder="Expliquez votre problème..." required></textarea>

              <button type="submit">Envoyer la demande</button>
            </form>
          </div>
        </div> )}
       

      {/* Confirmation de souscription */}
      {showConfirmation && carbonEmission !== null && (
        <div className="modal">
          <div className="modal-content">
            <span className="close-btn" onClick={handleCancelConfirmation}>&times;</span>
            <h2>Votre empreinte carbone</h2>
            <p>Votre voyage génère environ <strong>{carbonEmission} kg</strong> de CO2.</p>
            <p>Prix final après réduction : <strong>{reducedPrice.toFixed(2)} €</strong></p>
            <button onClick={handleSubscribeConfirm} className="confirm-btn">Souscrire</button>
            <button onClick={handleCancelConfirmation} className="cancel-btn">Annuler</button>
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

         {/* Liste des souscriptions */}
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
                      fetch(`http://localhost:8080/api/contracts/${contract.id}/pdf`)
                        .then((response) => {
                          if (!response.ok) throw new Error("Erreur lors du téléchargement du PDF");
                          return response.blob();
                        })
                        .then((blob) => {
                          const url = window.URL.createObjectURL(blob);
                          const a = document.createElement("a");
                          a.href = url;
                          a.download = `Contrat_Assurance_${contract.offer.title}_${contract.id}.pdf`;
                          document.body.appendChild(a);
                          a.click();
                          a.remove();
                        })
                        .catch((err) => {
                          console.error(err);
                          alert("Impossible de télécharger le contrat.");
                        });
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
