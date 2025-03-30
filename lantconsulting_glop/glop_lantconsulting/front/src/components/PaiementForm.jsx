import React, { useState } from "react";
import "./PaiementForm.css";

const PaiementForm = () => {
  const [formData, setFormData] = useState({
    name: "",
    card: "",
    exp: "",
    cvv: "",
  });
  const [success, setSuccess] = useState(false);
  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.id]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setSuccess(true);
  };

  return (
    <div className="paiement-page-container">
      <form className="payment-form" onSubmit={handleSubmit}>
        <h2>Paiement sécurisé 💳</h2>

        <label htmlFor="name">Nom sur la carte</label>
        <input
          type="text"
          id="name"
          value={formData.name}
          onChange={handleChange}
          required
        />

        <label htmlFor="card">Numéro de carte</label>
        <input
          type="text"
          id="card"
          maxLength="19"
          placeholder="0000 0000 0000 0000"
          value={formData.card}
          onChange={handleChange}
          required
        />

        <div className="row">
          <div>
            <label htmlFor="exp">Expiration</label>
            <input
              type="text"
              id="exp"
              placeholder="MM/YY"
              value={formData.exp}
              onChange={handleChange}
              required
            />
          </div>
          <div>
            <label htmlFor="cvv">CVV</label>
            <input
              type="text"
              id="cvv"
              maxLength="3"
              value={formData.cvv}
              onChange={handleChange}
              required
            />
          </div>
        </div>

        <button type="submit">💳 Payer maintenant</button>
        {success && (
          <div className="success-message">
            ✅ Paiement accepté avec succès ! Merci pour votre confiance.
          </div>)}
      </form>
    </div>
  );
};

export default PaiementForm;
