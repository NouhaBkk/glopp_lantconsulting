
// User.jsx
import React from 'react';

const User = ({ id, username }) => {
  return (
    <div>
      <h3>User Information</h3>
      <p><strong>ID:</strong> {id}</p>
      <p><strong>Username:</strong> {username}</p>
    </div>
  );
};

export default User;