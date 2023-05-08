import React from "react";

const PublisherList = ({ publishers, editEmployee, deleteEmployee }) => {
  return (
    <table className="table table-hover mt-3" align="center">
      <thead className="thead-light">
        <tr>
          <th scope="col">Nº</th>
          <th scope="col">Name</th>
          <th scope="col">Email</th>
          <th scope="col">Published</th>

          <th scope="col">Option</th>
        </tr>
      </thead>
      {publishers.map((employee, index) => {
        return (
          <tbody key={employee.id}>
            <tr>
              <th scope="row">{index + 1} </th>
              <td>{employee.name}</td>
              <td>{employee.email}</td>
              <td>{employee.published}</td>
              <td>
                <button
                  type="button"
                  className="btn btn-warning"
                  onClick={() => editEmployee(employee)}
                >
                  Edit
                </button>
                <button
                  type="button"
                  className="btn btn-danger mx-2"
                  onClick={() => deleteEmployee(employee.id)}
                >
                  Delete
                </button>
              </td>
            </tr>
          </tbody>
        );
      })}
    </table>
  );
};

export default PublisherList;
