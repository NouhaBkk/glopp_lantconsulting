import React from "react";
import { Link, useNavigate } from "react-router-dom";
import "./Header.css";
import logo from "./images/logo.png"; // Importation de votre logo

const Header = ({ user, onLogout }) => {
  const navigate = useNavigate(); // Hook pour naviguer vers une autre route

  const handleLogout = () => {
    onLogout(); // Appeler la fonction de déconnexion passée en props
    navigate("/"); // Rediriger vers la page d'accueil
  };

  const handleLoginRedirect = () => {
    navigate("/login"); // Rediriger vers la page de connexion
  };

  const handleHomeRedirect = () => {
    navigate("/"); // Rediriger vers la page d'accueil
  };
  const userRole = user?.roles?.[0]
  
  return (
    <header className="app-header">
      <div className="header-left" onClick={handleHomeRedirect} style={{ cursor: "pointer" }}>
        <img
          src={logo} // Utilisation de la variable d'importation
          alt="Logo"
          className="logo"
        />
        <span className="company-name">LantConsulting</span>
      </div>
      <nav className="header-nav">
        {user ? (
          userRole === "ROLE_ADMIN" ? (
            <>
              <Link to="/admin">Espace administrateur</Link>
              <Link to="/offers">Offres</Link>
              <Link to="/contact">Contact</Link>
            </>
            ) : userRole === "ROLE_DOCTOR" ? (
              <>
                <Link to="/doctor/dashboard">Espace Médecin</Link>
                <Link to="/contact">Contact</Link>
              </>
           
          ) : (

            <>
              <Link to="/espacepersonnel">Espace utilisateur</Link>
              <Link to="/offers">Offres</Link>
              <Link to="/contact">Contact</Link>
            </>
          )
        ) : (
          <>
            <Link to="/offers">Offres</Link>
            <Link to="/contact">Contact</Link>
          </>
        )}
      </nav>
      <div className="header-right">
        {user ? (
          <button onClick={handleLogout} className="btn btn-logout">
            Déconnexion
          </button>
        ) : (
          <button onClick={handleLoginRedirect} className="btn btn-login">
            Se connecter
          </button>
        )}
      </div>
    </header>
  );
};

export default Header;