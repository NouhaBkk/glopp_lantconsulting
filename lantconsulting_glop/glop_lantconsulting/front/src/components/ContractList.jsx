//import React, { useState } from 'react';
import React, { useState, useEffect } from 'react';

const ContractList = () => {
  //const [contracts] = useState([]);
  const [contracts, setContracts] = useState([]);

  useEffect(() => {
    fetch("http://127.0.0.1:8080/api/contracts/allcontracts")
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
