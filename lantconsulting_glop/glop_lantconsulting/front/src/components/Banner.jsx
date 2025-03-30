import React from 'react';
import './Banner.css';
import bannerImage from './images/cover.jpeg';

const Banner = () => {
  return (
    <section className="mobisure-banner">
      <img src={bannerImage} alt="Bannière MobiSure" className="banner-img" />
      <div className="banner-overlay">
        <div className="banner-text">
          <h1>
            MobiSureMoinsDeCO2 vous accompagne partout avec des solutions
            d’assistance et d’assurance responsables.
          </h1>
        </div>
      </div>
    </section>
  );
};

export default Banner;
