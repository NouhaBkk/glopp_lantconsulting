import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import API_BASE_URL from "./api"; 

const AddConseiller = () => {
  const [formData, setFormData] = useState({
    accountname: "",
    firstname: "",
    lastname: "",
    email: "",
    password: "",
  });

  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(`${API_BASE_URL}/conseillers/add`, {
        accountname: formData.accountname,
        password: formData.password,
        firstName: formData.firstname,
        lastName: formData.lastname,
        email: formData.email,
      });

      if (response.status === 200) {
        alert("Conseiller ajouté avec succès !");
        navigate("/admin");
      }
    } catch (error) {
      console.error("Erreur lors de l'ajout du conseiller", error);
      alert("Échec de l'ajout du conseiller.");
    }
  };

  return (
    <div className="add-conseiller-container">
      <h2>Ajouter un Conseiller</h2>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          name="accountname"
          placeholder="Nom d'utilisateur"
          value={formData.accountname}
          onChange={handleChange}
          required
        />
        <input
          type="text"
          name="firstname"
          placeholder="Prénom"
          value={formData.firstname}
          onChange={handleChange}
          required
        />
        <input
          type="text"
          name="lastname"
          placeholder="Nom"
          value={formData.lastname}
          onChange={handleChange}
          required
        />
        <input
          type="email"
          name="email"
          placeholder="Email"
          value={formData.email}
          onChange={handleChange}
          required
        />
        <input
          type="password"
          name="password"
          placeholder="Mot de passe"
          value={formData.password}
          onChange={handleChange}
          required
        />
        <button type="submit" className="btn-add-conseiller">
          Ajouter
        </button>
      </form>
    </div>
  );
};

export default AddConseiller;
