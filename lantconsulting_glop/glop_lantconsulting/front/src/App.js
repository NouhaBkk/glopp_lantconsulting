import React, { useEffect, useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import Header from './components/Header'; 
import Footer from './components/Footer'; 
import Login from './components/Login';
import Offers from './components/Offers';
import ContractList from './components/ContractList';
import EspacePersonnel from './components/EspacePersonnel';
import AdminDashboard from './components/AdminDashboard';
import CreateAccount from './components/CreateAccount';
import UserForm from './components/UserForm';
import MainContent from './components/MainContent'; 
import ConseillerDashboard from "./components/ConseillerDashboard";
import AddConseiller from './components/AddConseiller';
import UploadFile from "./UploadFile";
import PartnerDashboard from "./components/PartnerDashboard";
import AddPartner from "./components/AddPartner";
import MesDossiers from "./components/MesDossiers";
import AddDoctor from './components/AddDoctor';
import DoctorDashboard from './components/DoctorDashboard';
import DeclareIncident from "./components/DeclareIncident";
import PaiementForm from "./components/PaiementForm";
import Viewer from "./components/Viewer"; 
import Banner from './components/Banner';

import API_BASE_URL from './components/api'; 

import './App.css';

function App() {
  const [user, setUser] = useState(JSON.parse(sessionStorage.getItem('user')) || null);

  useEffect(() => {
    const storedUser = sessionStorage.getItem("user");
    if (storedUser) {
      try {
        const parsedUser = JSON.parse(storedUser);
        setUser(parsedUser);
        console.log("🔄 User récupéré après rafraîchissement :", parsedUser);
      } catch (error) {
        console.error("❌ Erreur lors du parsing de sessionStorage :", error);
      }
    }
  }, []);

  const handleLogin = (loggedInUser) => {
    setUser(loggedInUser);
    sessionStorage.setItem('user', JSON.stringify(loggedInUser));
  };

  const handleLogout = () => {
    setUser(null);
    sessionStorage.removeItem('user');
    localStorage.removeItem('user');
  };

  const handleRegister = async (formData) => {
    try {
      const response = await fetch(`${API_BASE_URL}/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(formData),
      });

      if (response.ok) {
        alert('Compte créé avec succès ! Veuillez vous connecter.');
        return true;
      } else {
        const errorData = await response.json();
        alert(`Erreur lors de la création du compte : ${errorData.message}`);
        return false;
      }
    } catch (error) {
      console.error('Erreur lors de la création du compte:', error);
      alert('Une erreur est survenue. Veuillez réessayer.');
      return false;
    }
  };

  const handleSubscribe = async (offerId) => {
    if (!user) {
      alert('Vous devez être connecté pour souscrire à une offre.');
      return;
    }

    try {
      const response = await fetch(`${API_BASE_URL}/contracts/subscribe`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          offerId,
          accountname: user.accountname,
        }),
      });

      if (response.ok) {
        alert('Souscription réussie à l\'offre !');
      } else {
        alert('Échec de la souscription.');
      }
    } catch (error) {
      console.error('Erreur lors de la souscription :', error);
      alert('Une erreur est survenue. Veuillez réessayer.');
    }
  };

  return (
    <Router>
      <div className="app-container">
        <Header user={user} onLogout={handleLogout} />
        <Banner />
        <div className="main-content">
          <Routes>
            <Route path="/" element={
              user ? (
                user.roles.includes("ROLE_ADMIN") ? <AdminDashboard /> : <EspacePersonnel accountname={user.accountname} />
              ) : <Navigate to="/login" replace />
            } />
            <Route path="/login" element={<Login onLogin={handleLogin} />} />
            <Route path="/create-account" element={<CreateAccount onSubmit={handleRegister} />} />
            <Route path="/offers" element={user ? <Offers onSubscribe={handleSubscribe} /> : <Navigate to="/login" />} />
            <Route path="/admin" element={user?.roles.includes('ROLE_ADMIN') ? <AdminDashboard /> : <Navigate to="/login" />} />
            <Route path="/add-user" element={user?.roles.includes('ROLE_ADMIN') ? <UserForm /> : <Navigate to="/login" />} />
            <Route path="/espacepersonnel" element={user?.roles.includes('ROLE_USER') ? <EspacePersonnel accountname={user.accountname} /> : <Navigate to="/" />} />
            <Route path="/contracts" element={user ? <ContractList accountname={user.accountname} /> : <Navigate to="/login" />} />
            <Route path="/admin/add-conseiller" element={<AddConseiller />} />
            <Route path="/conseiller/dashboard" element={<ConseillerDashboard />} />
            <Route path="/upload-file" element={<UploadFile />} />
            <Route path="/upload/:caseId" element={<UploadFile />} />
            <Route path="/mes-dossiers" element={<MesDossiers />} />
            <Route path="/admin/add-partner" element={<AddPartner />} />
            <Route path="/partner/dashboard" element={<PartnerDashboard />} />
            <Route path="/admin/add-doctor" element={<AddDoctor />} />
            <Route path="/doctor/dashboard" element={user?.roles.includes('ROLE_DOCTOR') ? <DoctorDashboard doctorId={user.id} /> : <Navigate to="/login" />} />
            <Route path="/declare-incident" element={<DeclareIncident />} />
            <Route path="/paiement-form" element={<PaiementForm />} />
            <Route path="/viewer" element={<Viewer />} />
          </Routes>
        </div>
        <MainContent />
        <Footer />
      </div>
    </Router>
  );
}

export default App;
