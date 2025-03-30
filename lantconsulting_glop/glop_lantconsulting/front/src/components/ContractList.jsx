import React, { useState, useEffect } from 'react';
import API_BASE_URL from './api';

const ContractList = () => {
  const [contracts, setContracts] = useState([]);

  useEffect(() => {
    fetch(`${API_BASE_URL}/api/contracts/allcontracts`)
      .then((response) => response.json())
      .then((data) => setContracts(data))
      .catch((error) => console.error("Error fetching contracts:", error));
  }, []);

  return (
    <div className="table-container">
      <h3 className="text-center">Contracts</h3>
      <table className="table">
        <thead>
          <tr>
            <th>Details</th>
            <th>Start Date</th>
            <th>End Date</th>
          </tr>
        </thead>
        <tbody>
          {contracts.map((contract) => (
            <tr key={contract.id}>
              <td>{contract.details}</td>
              <td>{contract.startDate}</td>
              <td>{contract.endDate}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default ContractList;
