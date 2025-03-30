import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";

const AddDoctor = () => {
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
      // On envoie les données du médecin au localhost:8080 pour l'ajout
      const response = await axios.post("http://localhost:8080/api/doctors/add", {
        accountname: formData.accountname,
        password: formData.password,
        firstName: formData.firstname, // facultatif selon vos besoins
        lastName: formData.lastname,
        email: formData.email,
      });

      if (response.status === 200) {
        alert("Médecin ajouté avec succès !");
        navigate("/admin");
      }
    } catch (error) {
      console.error("Erreur lors de l'ajout du médecin", error);
      alert("Échec de l'ajout du médecin.");
    }
  };

  return (
    <div className="add-doctor-container">
      <h2>Ajouter un Médecin</h2>
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
        <button type="submit" className="btn-add-doctor">
          Ajouter
        </button>
      </form>
    </div>
  );
};

export default AddDoctor;
