import React, { useState, useEffect } from 'react';
import './Offers.css';


const Offers = ({ onSubscribe }) => {
  const [offers, setOffers] = useState([]);
  // eslint-disable-next-line no-unused-vars
const [selectedOffer, setSelectedOffer] = useState(null);

// eslint-disable-next-line no-unused-vars
const [subscriptionMessage, setSubscriptionMessage] = useState('');

  useEffect(() => {
    // Récupérer les offres dynamiquement via l'API
    fetch('http://localhost:8080/api/offers')
      .then((response) => {
        if (!response.ok) {
          throw new Error('Failed to fetch offers');
        }
        return response.json();
      })
      .then((data) => setOffers(data))
      .catch((error) => console.error('Error fetching offers:', error));
  }, []);

  // const handleSubscribe = (offer) => {
  //   setSelectedOffer(offer); // Assigner l'offre sélectionnée
  //   setSubscriptionMessage(`Vous êtes inscrit à l'offre : ${offer.name}`);

  //   // Appel de la fonction de souscription si nécessaire
  //   if (onSubscribe) {
  //     onSubscribe(offer.id);
  //   }
  // };

  return (
    <div className="offers-container"
      
    >
      <h1>Nos Offres d'Assurance</h1>
      <div className="offers-list">
        {offers.length > 0 ? (
          offers.map((offer) => (
            <div key={offer.id} className="offer-card">
<h2>{offer.title}</h2>
<div className="price">
  Prix : <span>{offer.price} € / mois</span>
</div>


              <div className="item-content">
                <p className="item-description">{offer.description}</p>
              </div>
              <div className="item-footer">
                {/* <button
                  className="btn item-btn"
                  onClick={() => handleSubscribe(offer)}
                >
                  Souscrire
                </button> */}
              </div>
            </div>
          ))
        ) : (
          <p>Aucune offre disponible pour le moment.</p>
        )}
      </div>
      {subscriptionMessage && (
        <div className="subscription-message">
          <p>{subscriptionMessage}</p>
        </div>
      )}
      {selectedOffer && (
        <div className="selected-offer-details">
          <h2>Détails de l'Offre Sélectionnée :</h2>
          <p><strong>Nom : </strong>{selectedOffer.name}</p>
          <p><strong>Description : </strong>{selectedOffer.description}</p>
        </div>
      )}
    </div>
  );
};

export default Offers;