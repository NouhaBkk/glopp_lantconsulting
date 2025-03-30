import React, { useState, useEffect } from 'react';
import './AdminDashboard.css';
import coverImage from './images/cover.jpeg';
import { useNavigate } from 'react-router-dom';
import API_BASE_URL from './api';

const AdminDashboard = () => {
  const [view, setView] = useState('');
  const [offers, setOffers] = useState([]);
  const [newOffer, setNewOffer] = useState({ title: '', description: '', price: '' });
  const [editingOffer, setEditingOffer] = useState(null);
  const [updatedOffer, setUpdatedOffer] = useState({ title: '', description: '', price: '' });

  const fetchOffers = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/offers`);
      const data = await response.json();
      setOffers(data);
    } catch (error) {
      console.error('Error fetching offers:', error);
    }
  };

  const navigate = useNavigate();

  const handleAddOffer = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/offers`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newOffer),
      });

      if (response.ok) {
        alert('Offre ajoutée avec succès');
        setNewOffer({ title: '', description: '', price: '' });
        fetchOffers();
      } else {
        alert('Erreur lors de l’ajout de l’offre');
      }
    } catch (error) {
      console.error('Error adding offer:', error);
    }
  };

  const handleUpdateOffer = async () => {
    if (!editingOffer) return;

    try {
      const response = await fetch(`${API_BASE_URL}/offers/${editingOffer.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updatedOffer),
      });

      if (response.ok) {
        alert('Offre mise à jour avec succès');
        setEditingOffer(null);
        fetchOffers();
      } else {
        alert('Erreur lors de la mise à jour de l’offre');
      }
    } catch (error) {
      console.error('Error updating offer:', error);
    }
  };

  const handleDeleteOffer = async (id) => {
    if (window.confirm('Voulez-vous vraiment supprimer cette offre ?')) {
      try {
        await fetch(`${API_BASE_URL}/offers/${id}`, { method: 'DELETE' });
        alert('Offre supprimée avec succès');
        fetchOffers();
      } catch (error) {
        console.error('Error deleting offer:', error);
      }
    }
  };

  useEffect(() => {
    if (view === 'offers') fetchOffers();
  }, [view]);

  return (
    <div className="background-overlay">
      {view === '' && (
        <>
          <h1>Mobisur moins CO2</h1>
          <div className="dashboard-button-group">
            <button onClick={() => setView('offers')} className="btn-admin">Nos Offres</button>
            <button onClick={() => navigate('/admin/add-conseiller')} className="btn-admin">Ajouter un Conseiller</button>
            <button onClick={() => navigate('/admin/add-partner')} className="btn-admin">Ajouter un Partenaire</button>
            <button onClick={() => navigate('/admin/add-doctor')} className="btn-admin">Ajouter un Médecin</button>
          </div>
        </>
      )}

      {view === 'offers' && (
        <div>
          <h2>Gestion des Offres</h2>
          <div className="offers-cards">
            {offers.map((offer) => (
              <div key={offer.id} className="offer-card">
                <div className="item-head">
                  <h5 className="item-title"><b>{offer.title}</b></h5>
                  <h6 className="item-subtitle"><b>{offer.price} €</b>/par mois</h6>
                </div>
                <div className="item-content">
                  <p className="item-description">{offer.description}</p>
                </div>
                <div className="item-footer">
                  <button className="btn-edit" onClick={() => { setEditingOffer(offer); setUpdatedOffer({ title: offer.title, description: offer.description, price: offer.price }); }}>Modifier</button>
                  <button className="btn-danger" onClick={() => handleDeleteOffer(offer.id)}>Supprimer</button>
                </div>
              </div>
            ))}
          </div>

          <h3>Ajouter une Offre</h3>
          <form className="add-offer-form" onSubmit={(e) => { e.preventDefault(); handleAddOffer(); }}>
            <input type="text" placeholder="Titre" value={newOffer.title} onChange={(e) => setNewOffer({ ...newOffer, title: e.target.value })} required />
            <textarea placeholder="Description" value={newOffer.description} onChange={(e) => setNewOffer({ ...newOffer, description: e.target.value })} required />
            <input type="number" placeholder="Prix" value={newOffer.price} onChange={(e) => setNewOffer({ ...newOffer, price: e.target.value })} required />
            <button type="submit" className="btn-add-offer">Ajouter</button>
          </form>
        </div>
      )}

      {editingOffer && (
        <div className="edit-offer-modal">
          <h3>Modifier l'Offre</h3>
          <form className="edit-offer-form" onSubmit={(e) => { e.preventDefault(); handleUpdateOffer(); }}>
            <input type="text" placeholder="Titre" value={updatedOffer.title} onChange={(e) => setUpdatedOffer({ ...updatedOffer, title: e.target.value })} required />
            <textarea placeholder="Description" value={updatedOffer.description} onChange={(e) => setUpdatedOffer({ ...updatedOffer, description: e.target.value })} required />
            <input type="number" placeholder="Prix" value={updatedOffer.price} onChange={(e) => setUpdatedOffer({ ...updatedOffer, price: e.target.value })} required />
            <button type="submit" className="btn-update-offer">Enregistrer</button>
            <button type="button" className="btn-cancel" onClick={() => setEditingOffer(null)}>Annuler</button>
          </form>
        </div>
      )}
    </div>
  );
};

export default AdminDashboard;
