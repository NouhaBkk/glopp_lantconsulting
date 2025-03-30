// Contract.jsx
import React from 'react';

const Contract = ({ details, startDate, endDate }) => {
  return (
    <div>
      <h3>Contract Details</h3>
      <p><strong>Details:</strong> {details}</p>
      <p><strong>Start Date:</strong> {startDate}</p>
      <p><strong>End Date:</strong> {endDate}</p>
    </div>
  );
};

export default Contract;