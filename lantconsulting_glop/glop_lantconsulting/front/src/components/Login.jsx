import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import './Login.css';
import API_BASE_URL from './api';

const Login = ({ onLogin }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch(`${API_BASE_URL}/auth/authenticate`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ accountname: username, password }),
        credentials: 'include',
      });

      const data = await response.json();
      console.log("Réponse du backend :", data);

      if (response.ok) {
        if (!data.id) {
          alert("ERREUR : L'ID de l'utilisateur est manquant !");
          return;
        }

        const loggedInUser = {
          id: data.id,
          accountname: data.username,
          roles: data.role ? [data.role] : [],
        };

        sessionStorage.setItem("user", JSON.stringify(loggedInUser));
        console.log("Utilisateur stocké dans sessionStorage :", loggedInUser);
        onLogin(loggedInUser);

        if (loggedInUser.roles.includes('ROLE_CONSEILLER') || loggedInUser.roles.includes('ROLE_ADVISOR')) {
          localStorage.setItem("id", data.id);
          localStorage.setItem("accountname", data.username);
          localStorage.setItem("role", data.role);
          navigate('/conseiller/dashboard');
        } else if (loggedInUser.roles.includes('ROLE_ADMIN')) {
          navigate('/admin');
        } else if (loggedInUser.roles.includes('ROLE_PARTNER')) {
          navigate('/partner/dashboard');
        } else if (loggedInUser.roles.includes('ROLE_DOCTOR')) {
          navigate('/doctor/dashboard');
        } else {
          navigate('/espacepersonnel');
        }
      } else {
        alert(data.message || 'Échec de l’authentification');
      }
    } catch (error) {
      console.error('Erreur lors de la connexion :', error);
      alert('Une erreur est survenue lors de la connexion.');
    }
  };

  return (
    <div className="login-container">
      <form onSubmit={handleLogin}>
        <h2>Connexion</h2>
        <div className="form-group">
          <label>Nom d'utilisateur :</label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            className="form-control"
            placeholder="Entrez votre nom d'utilisateur"
            required
          />
        </div>
        <div className="form-group">
          <label>Mot de passe :</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="form-control"
            placeholder="Entrez votre mot de passe"
            required
          />
        </div>
        <button type="submit" className="btn btn-primary">
          Se connecter
        </button>
      </form>
      <div className="mt-3">
        <p>Pas encore de compte ?</p>
        <Link to="/create-account" className="btn btn-link">
          Créer un compte
        </Link>
      </div>
    </div>
  );
};

export default Login;
