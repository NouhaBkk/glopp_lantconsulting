import React, { useState, useEffect } from "react";
import "./CarbonForm.css"; // Un fichier CSS dédié pour ce composant

const CarbonForm = ({ offerId,  offerPrice, accountname, onClose, onSubscriptionSuccess,onReadyToPay }) => {
  const [formType, setFormType] = useState("vehicle");
  const [compensate, setCompensate] = useState(false);
const [treesToPlant, setTreesToPlant] = useState(null);
const [compensationCost, setCompensationCost] = useState(null);
const [availableMakes, setAvailableMakes] = useState([]);
const [availableModels, setAvailableModels] = useState([]);
  const [carbonFormData, setCarbonFormData] = useState({
    origine: "",
    destination: "",
    make: "",
    model: "",
    transportType: "",
  });
  const [carbonEmission, setCarbonEmission] = useState(null);
  const [reducedPrice, setReducedPrice] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const isCar = carbonFormData.transportType === "car";
  useEffect(() => {
    fetch("http://localhost:8080/api/co2/getAllMakes")
      .then((res) => res.json())
      .then((data) => {
        if (Array.isArray(data)) {
          setAvailableMakes(data);
        } else {
          console.error("Format de données inattendu:", data);
          setAvailableMakes([]);
        }
      })
      .catch((err) => console.error("Erreur marques:", err));
  }, []);

  useEffect(() => {
    if (isCar && carbonFormData.make) {
      fetch(`http://localhost:8080/api/co2/getAllModels?make=${carbonFormData.make}`)
        .then((res) => res.json())
        .then(data => {
          if (Array.isArray(data)) {
            setAvailableModels(data);
          } else {
            console.error("Réponse inattendue:", data);
            setAvailableModels([]);
          }
        })
        .catch((err) => console.error("Erreur modèles:", err));
    }
  }, [carbonFormData.make, isCar]);
  const handleFormChange = (e) => {
    const { name, value } = e.target;
    setCarbonFormData({
      ...carbonFormData,
      [name]: value,
    });
  };

  const handleFormTypeChange = (e) => {
    setFormType(e.target.value);
    setCarbonFormData({
      origine: "",
      destination: "",
      make: "",
      model: "",
      transportType: "",
    });
    setCarbonEmission(null);
    setReducedPrice(null);
    setError("");
  };

  const handleFormSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");

    const { origine, destination, make, model, transportType } = carbonFormData;

    try {
      let response;
      if (isCar) {
        if (!origine || !destination || !make || !model) {
          setError("Veuillez remplir tous les champs du véhicule.");
          setLoading(false);
          return;
        }
        const params = new URLSearchParams({ origin: origine, destination, make, model });
        response = await fetch(`http://localhost:8080/api/co2/routeEmission?${params}`, {
          method: "POST",
        });
      } else {
        if (!origine || !destination || !transportType) {
          setError("Veuillez remplir tous les champs du transport.");
          setLoading(false);
          return;
        }
        const params = new URLSearchParams({ origin: origine, destination, transportType });
        response = await fetch(`http://localhost:8080/api/co2/estimateCO2?${params}`, {
          method: "POST",
        });
      }

      if (response.ok) {
        const emission = await response.json();
        setCarbonEmission(emission);

        const reductionResponse = await fetch("http://localhost:8080/api/co2/getReducedPrice", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ co2_kg: emission, price: offerPrice }),
        });
        

        if (reductionResponse.ok) {
          const finalPrice = await reductionResponse.json();
          setReducedPrice(finalPrice);
          if (compensate) {
            const compensationResp = await fetch("http://localhost:8080/api/reforestation/estimate", {
              method: "POST",
              headers: { "Content-Type": "application/json" },
              body: JSON.stringify({ co2_kg: emission }),
            });
          
            if (compensationResp.ok) {
              const data = await compensationResp.json();
              setTreesToPlant(data.trees);
              setCompensationCost(data.compensationCost);
              setReducedPrice(prev => prev + data.compensationCost); // Ajoute la compensation au prix
            }
          }
        
        } else {
          setReducedPrice(null);
        }
      } else {
        setError("Erreur lors du calcul de l'empreinte carbone.");
      }
    } catch (err) {
      setError("Une erreur est survenue.");
    } finally {
      setLoading(false);
    }
  };



  return (
    <div className="modal">
      <div className="modal-content">
        <span className="close-btn" onClick={onClose}>&times;</span>
        <h2>Calculer l'empreinte carbone</h2>

        {error && <p className="error">{error}</p>}

        {!carbonEmission ? (
          <form onSubmit={handleFormSubmit}>
            <input type="text" name="origine" placeholder="Lieu de départ" value={carbonFormData.origine} onChange={handleFormChange} required />
            <input type="text" name="destination" placeholder="Lieu d'arrivée" value={carbonFormData.destination} onChange={handleFormChange} required />

            <select name="transportType" value={carbonFormData.transportType} onChange={handleFormChange} required>
               <option value="">-- Choisissez un transport --</option>
               <option value="train">Train</option>
               <option value="car">Voiture</option>
               <option value="plane_short">Avion (court-courrier)</option>
               <option value="plane_long">Avion (long-courrier)</option>
               <option value="bus">Bus</option>
               <option value="bike">Vélo</option>
               <option value="scooter">Trottinette</option>
               <option value="walk">Marche</option>
             </select>
             {isCar && (
              <>
                <select name="make" value={carbonFormData.make} onChange={handleFormChange} required>
                  <option value="">-- Sélectionner une marque --</option>
                  {availableMakes.map((make) => (
                    <option key={make} value={make}>{make}</option>
                  ))}
                </select>
                <select name="model" value={carbonFormData.model} onChange={handleFormChange} required>
                  <option value="">-- Sélectionner un modèle --</option>
                  {availableModels.map((model) => (
                    <option key={model} value={model}>{model}</option>
                  ))}
                </select>
              </>
            )}
          
           <label style={{ display: "block", marginTop: "10px" }}>
  <input
    type="checkbox"
    checked={compensate}
    onChange={(e) => setCompensate(e.target.checked)}
  />
  Je souhaite compenser mes émissions via la reforestation 🌱
</label>


           <button type="submit" disabled={loading}>
             {loading ? "Calcul..." : "Calculer l'empreinte carbone"}
           </button>
         </form>
       ) : (
         <div>
           <p>Votre voyage génère environ <strong>{carbonEmission.toFixed(2)} kg</strong> de CO2.</p>
           {compensate && treesToPlant !== null && (
  <>
    <p>🌳 Arbres à planter : <strong>{treesToPlant}</strong></p>
    <p>💰 Coût de la compensation : <strong>{compensationCost.toFixed(2)} €</strong></p>
  </>
)}

{reducedPrice !== null && (
  <p>Prix final après réduction : <strong>{reducedPrice.toFixed(2)} €</strong></p>
)}

<button
  onClick={() => {
    onReadyToPay({
      reducedPrice,
      compensationCost: compensationCost || 0,
    });
  }}
>
  Payer
</button>



<button onClick={onClose}>Annuler</button>

         </div>
       )}
     </div>
   </div>
 );



};

export default CarbonForm;
