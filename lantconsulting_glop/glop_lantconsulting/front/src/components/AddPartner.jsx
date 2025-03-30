import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import axios from "axios";
import API_BASE_URL from "./api";

const AddPartner = () => {
  const [formData, setFormData] = useState({
    accountname: "",
    firstname: "",
    lastname: "",
    email: "",
    password: "",
    country: "",
    region: ""
  });

  const [countries, setCountries] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    axios.get("https://restcountries.com/v3.1/all")
      .then(response => {
        const countryList = response.data
          .filter(country => country.name && country.name.common && country.region)
          .map(country => ({
            name: country.name.common,
            region: country.region || "Autre"
          }));

        countryList.sort((a, b) => a.name.localeCompare(b.name));
        setCountries(countryList);
        setLoading(false);
      })
      .catch(error => {
        console.error("Erreur lors de la récupération des pays :", error);
        setLoading(false);
      });
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;

    if (name === "country") {
      const selectedCountry = countries.find(country => country.name === value);
      setFormData({
        ...formData,
        country: value,
        region: selectedCountry ? selectedCountry.region : ""
      });
    } else {
      setFormData({ ...formData, [name]: value });
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const response = await axios.post(`${API_BASE_URL}/partners/create`, {
        accountname: formData.accountname,
        password: formData.password,
        firstName: formData.firstname,
        lastName: formData.lastname,
        email: formData.email,
        country: formData.country,
        region: formData.region
      });

      if (response.status === 200) {
        alert("Partenaire ajouté avec succès !");
        navigate("/admin");
      }
    } catch (error) {
      console.error("Erreur lors de l'ajout du partenaire", error.response ? error.response.data : error.message);
      alert(`Échec de l'ajout du partenaire : ${error.response ? error.response.data : "Problème inconnu"}`);
    }
  };

  return (
    <div className="add-partner-container">
      <h2>Ajouter un Partenaire</h2>
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
        {loading ? (
          <p>Chargement des pays...</p>
        ) : (
          <select name="country" value={formData.country} onChange={handleChange} required>
            <option value="">Sélectionner un pays</option>
            {countries.map((country) => (
              <option key={country.name} value={country.name}>
                {country.name}
              </option>
            ))}
          </select>
        )}
        <input type="text" name="region" placeholder="Région" value={formData.region} readOnly />
        <button type="submit" className="btn-add-partner">
          Ajouter
        </button>
      </form>
    </div>
  );
};

export default AddPartner;
