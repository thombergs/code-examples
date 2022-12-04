import Employee from '../models/employee.model.js';

const getEmployees = (_req, res) => {
    Employee.findAll({
        include: [{
            model: Employee,
            as: 'children',
            attributes: ['id', 'name', 'email', 'username', 'avatar'],
            required: true
        }],
        attributes: {
            exclude: ['managedBy']
        }
    })
      .then((data) => {
        res.send(data);
      })
      .catch((err) => {
        res.status(500).send({
          message:
            err.message
             || "There might be some error occurred while retrieving employees from database.",
        });
      });
  };

export default getEmployees;