import React, { useState, useEffect } from 'react';
import API_BASE_URL from './api'; // ✅ Import ajouté

const UserForm = ({ onSubmit }) => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState('ROLE_USER');
  const [contracts, setContracts] = useState([]);
  const [selectedContractId, setSelectedContractId] = useState('');

  useEffect(() => {
    fetch(`${API_BASE_URL}/contracts/allcontracts`) // ✅ Utilisation d'API_BASE_URL
      .then((response) => response.json())
      .then((data) => setContracts(data))
      .catch((error) => console.error("Erreur lors de la récupération des contrats:", error));
  }, []);

  const handleSubmit = (e) => {
    e.preventDefault();
    const user = {
      accountname: username,
      password,
      roles: [role],
      contracts: selectedContractId ? [{ id: selectedContractId }] : [],
    };
    onSubmit(user);
    setUsername('');
    setPassword('');
    setRole('ROLE_USER');
    setSelectedContractId('');
  };

  return (
    <form onSubmit={handleSubmit}>
      <div className="mb-3">
        <label className="form-label">Username:</label>
        <input
          type="text"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          className="form-control"
          required
        />
      </div>
      <div className="mb-3">
        <label className="form-label">Password:</label>
        <input
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="form-control"
          required
        />
      </div>
      <div className="mb-3">
        <label className="form-label">Role:</label>
        <select
          value={role}
          onChange={(e) => setRole(e.target.value)}
          className="form-control"
        >
          <option value="ROLE_USER">User</option>
          <option value="ROLE_ADMIN">Admin</option>
        </select>
      </div>
      <div className="mb-3">
        <label className="form-label">Associated Contract:</label>
        <select
          value={selectedContractId}
          onChange={(e) => setSelectedContractId(e.target.value)}
          className="form-control"
        >
          <option value="">Select Contract</option>
          {contracts.map((contract) => (
            <option key={contract.id} value={contract.id}>
              {contract.details} ({contract.startDate} - {contract.endDate})
            </option>
          ))}
        </select>
      </div>
      <button type="submit" className="btn btn-primary">Add User</button>
    </form>
  );
};

export default UserForm;
