import React from 'react';
import './MainContent.css';
import voitureIcon from './images/co2-voiture.png';
import reforestationIcon from './images/reforestation.webp';
import comparateurIcon from './images/comparateur.png';
import imgQui from './images/qui-sommes-nous.webp';
import imgCo2 from './images/mobisure-co2.jpg';
import imgMission from './images/mission-assistance.jpeg';

const MainContent = () => {
  return (
    <>
 <section className="zigzag-section">
      {/* Bloc 1 */}
      <div className="zigzag-block left">
        <div className="zigzag-image">
          <img src={imgQui} alt="Qui sommes-nous" />
        </div>
        <div className="zigzag-text">
          <h3>Qui sommes-nous ?</h3>
          <p>
            <b>LANT Consulting</b> est une société spécialisée dans la conception de solutions digitales sur mesure dans les domaines de l’assurance, de la mobilité et de l’assistance.
          </p>
        </div>
      </div>

      {/* Bloc 2 */}
      <div className="zigzag-block right">
        <div className="zigzag-text">
          <h3>MobiSureMoinsDeCO2 en quelques mots</h3>
          <p>
            <b>MobiSureMoinsDeCO2</b> est une initiative d’<b>AssurMob</b> qui réinvente l’assistance pour les voyageurs, en tenant compte des enjeux environnementaux.
          </p>
        </div>
        <div className="zigzag-image">
          <img src={imgCo2} alt="MobiSureMoinsDeCO2" />
        </div>
      </div>

      {/* Bloc 3 */}
      <div className="zigzag-block left">
        <div className="zigzag-image">
          <img src={imgMission} alt="Notre mission" />
        </div>
        <div className="zigzag-text">
          <h3>Notre Mission</h3>
          <p>
            Chez <b>LANT Consulting</b>, notre mission est de simplifier et optimiser la vie de nos clients avec des solutions digitales durables et innovantes.
          </p>
        </div>
      </div>
    </section>

      {/* ✅ SECTION VISUELLE AVEC ICONES */}
      <section className="visuel-section">
        <div className="visuel-card">
          <img src={voitureIcon} alt="Voiture moins de CO2" />
          <div className="visuel-title">Réduire vos émissions CO₂</div>
          <div className="visuel-description">
            Choisissez une assurance qui valorise les trajets écoresponsables.
          </div>
        </div>

        <div className="visuel-card">
          <img src={reforestationIcon} alt="Reforestation" />
          <div className="visuel-title">Compensation carbone intégrée</div>
          <div className="visuel-description">
            Une partie de votre cotisation finance des projets de reforestation.
          </div>
        </div>

        <div className="visuel-card">
          <img src={comparateurIcon} alt="Comparateur de transport" />
          <div className="visuel-title">Comparez vos trajets</div>
          <div className="visuel-description">
            Visualisez l’impact CO₂ de vos moyens de transport en un clic.
          </div>
        </div>
      </section>
    </>
  );
};

export default MainContent;
