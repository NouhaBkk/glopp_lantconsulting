import React from 'react';

const UserList = ({ users }) => {
  return (
    <div className="table-container">
      <h3 className="text-center">Users</h3>
      <table className="table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Roles</th>
            <th>Contracts</th>
          </tr>
        </thead>
        <tbody>
          {users.map((user) => (
            <tr key={user.id}>
              <td>{user.id}</td>
              <td>{user.accountname}</td>
              <td>{user.roles ? user.roles.join(", ") : "No Roles"}</td>
              <td>
                {user.contracts && user.contracts.length > 0
                  ? user.contracts.map((contract) => (
                      <div key={contract.id}>
                        {contract.details} ({contract.startDate} - {contract.endDate})
                      </div>
                    ))
                  : "No Contracts"}
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default UserList;
