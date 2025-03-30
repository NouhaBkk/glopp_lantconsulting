import React from 'react';
import './Footer.css';

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-container">
        <div className="footer-item">
          <i className="fas fa-map-marker-alt"></i>
          <h4>Adresse</h4>
          <p>5 rue LantConsulting, Lille France</p>
        </div>
        <div className="footer-item">
          <i className="fas fa-phone-alt"></i>
          <h4>Téléphone</h4>
          <p>06 58 71 99 30</p>
        </div>
        <div className="footer-item">
          <i className="fas fa-envelope"></i>
          <h4>E-mail</h4>
          <p>contact@lant-consulting.com</p>
        </div>
      </div>
    </footer>
  );
};

export default Footer;