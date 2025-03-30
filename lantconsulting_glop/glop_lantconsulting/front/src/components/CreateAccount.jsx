import React, { useState } from 'react';
import './CreateAccount.css';
import { useNavigate } from 'react-router-dom';

const CreateAccount = ({ onSubmit }) => {
  const [formData, setFormData] = useState({
    accountname: '', // Champ ajouté pour le nom d'utilisateur
    civility: 'M.',
    lastName: '',
    firstName: '',
    birthDate: '',
    email: '',
    address: '',
    phoneNumber: '',
    maritalStatus: 'célibataire',
    motorized: false,
    vehicleName: '',
    annualTravelPercentage: 0,
    password: '', // Champ mot de passe
    children: [],
  });

  const [newChild, setNewChild] = useState({
    firstName: '',
    lastName: '',
    gender: 'M',
    age: '',
  });
  
  const navigate = useNavigate(); 
  const handleInputChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData({
      ...formData,
      [name]: type === 'checkbox' ? checked : value,
    });
  };

  const handleChildInputChange = (e) => {
    const { name, value } = e.target;
    setNewChild({
      ...newChild,
      [name]: value,
    });
  };

  const addChild = () => {
    if (newChild.firstName && newChild.lastName && newChild.age) {
      setFormData({
        ...formData,
        children: [...formData.children, newChild],
      });
      setNewChild({ firstName: '', lastName: '', gender: 'M', age: '' });
    } else {
      alert('Veuillez remplir tous les champs pour ajouter un enfant.');
    }
  };

  const removeChild = (index) => {
    setFormData({
      ...formData,
      children: formData.children.filter((_, i) => i !== index),
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!formData.accountname) {
      alert("Le nom d'utilisateur est requis.");
      return;
    }
    if (!formData.password) {
      alert('Le mot de passe est requis.');
      return;
    }
    const success = await onSubmit(formData); // Assurez-vous que `onSubmit` retourne un booléen
    if (success) {
      navigate('/login'); // Redirige vers la page de connexion
    }
  };

  return (
    <form onSubmit={handleSubmit} className="create-account-form">
      <h2>Créer un Compte Client</h2>
      <div className="form-group">
        <label>Nom d'utilisateur :</label>
        <input
          type="text"
          name="accountname"
          value={formData.accountname}
          onChange={handleInputChange}
          required
        />
      </div>
      <div className="form-group">
        <label>Civilité :</label>
        <select name="civility" value={formData.civility} onChange={handleInputChange}>
          <option value="M.">M.</option>
          <option value="Mme">Mme</option>
        </select>
      </div>
      <div className="form-group">
        <label>Nom :</label>
        <input
          type="text"
          name="lastName"
          value={formData.lastName}
          onChange={handleInputChange}
          required
        />
      </div>
      <div className="form-group">
        <label>Prénom :</label>
        <input
          type="text"
          name="firstName"
          value={formData.firstName}
          onChange={handleInputChange}
          required
        />
      </div>
      <div className="form-group">
        <label>Date de Naissance :</label>
        <input
          type="date"
          name="birthDate"
          value={formData.birthDate}
          onChange={handleInputChange}
          required
        />
      </div>
      <div className="form-group">
        <label>Email :</label>
        <input
          type="email"
          name="email"
          value={formData.email}
          onChange={handleInputChange}
          required
        />
      </div>
      <div className="form-group">
        <label>Adresse Postale :</label>
        <input
          type="text"
          name="address"
          value={formData.address}
          onChange={handleInputChange}
          required
        />
      </div>
      <div className="form-group">
        <label>Téléphone :</label>
        <input
          type="tel"
          name="phoneNumber"
          value={formData.phoneNumber}
          onChange={handleInputChange}
          required
        />
      </div>
      <div className="form-group">
        <label>Mot de Passe :</label>
        <input
          type="password"
          name="password"
          value={formData.password}
          onChange={handleInputChange}
          required
        />
      </div>
      <div className="form-group">
        <label>État Civil :</label>
        <select name="maritalStatus" value={formData.maritalStatus} onChange={handleInputChange}>
          <option value="célibataire">Célibataire</option>
          <option value="marié">Marié</option>
        </select>
      </div>
      <div className="form-group">
        <label>Êtes-vous motorisé ?</label>
        <input
          type="checkbox"
          name="motorized"
          checked={formData.motorized}
          onChange={handleInputChange}
        />
      </div>
      {formData.motorized && (
        <>
          <div className="form-group">
            <label>Nom du véhicule :</label>
            <input
              type="text"
              name="vehicleName"
              value={formData.vehicleName}
              onChange={handleInputChange}
              required
            />
          </div>
          <div className="form-group">
            <label>Pourcentage de voyage annuel :</label>
            <input
              type="range"
              name="annualTravelPercentage"
              min="0"
              max="100"
              value={formData.annualTravelPercentage}
              onChange={handleInputChange}
            />
            <span>{formData.annualTravelPercentage} %</span>
          </div>
        </>
      )}
      <h3>Ajouter des Enfants</h3>
      {formData.children.map((child, index) => (
        <div key={index} className="child-entry">
          <p>
            {child.firstName} {child.lastName}, {child.gender}, {child.age} ans
          </p>
          <button type="button" onClick={() => removeChild(index)}>
            Supprimer
          </button>
        </div>
      ))}
      <div className="form-group">
        <label>Prénom de l'enfant :</label>
        <input
          type="text"
          name="firstName"
          value={newChild.firstName}
          onChange={handleChildInputChange}
        />
        <label>Nom de l'enfant :</label>
        <input
          type="text"
          name="lastName"
          value={newChild.lastName}
          onChange={handleChildInputChange}
        />
        <label>Sexe :</label>
        <select name="gender" value={newChild.gender} onChange={handleChildInputChange}>
          <option value="M">M</option>
          <option value="F">F</option>
        </select>
        <label>Âge :</label>
        <input
          type="number"
          name="age"
          value={newChild.age}
          onChange={handleChildInputChange}
        />
        <button type="button" onClick={addChild}>
          Ajouter Enfant
        </button>
      </div>
      <button type="submit" className="btn-submit">
        Créer le Compte
      </button>
    </form>
  );
};

export default CreateAccount;
