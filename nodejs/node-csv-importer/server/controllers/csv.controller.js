import Employee from '../models/employee.model.js';
import { createReadStream } from 'fs';
import { parse } from 'fast-csv';
import { Parser as CsvParser } from 'json2csv';

const upload = async (req, res) => {
  try {
    if (req.file == undefined) {
      return res.status(400).send("Please upload a CSV file!");
    }

    let employees = [];
    let path = "./resources/static/assets/uploads/" + req.file.filename;

    createReadStream(path)
      .pipe(parse({ headers: true }))
      .on("error", (error) => {
        throw error.message;
      })
      .on("data", (row) => {
        employees.push(row);
      })
      .on("end", () => {
        Employee.bulkCreate(employees)
          .then(() => {
            res.status(200).send({
              message: "The file: "
               + req.file.originalname
               + " got uploaded successfully!!",
            });
          })
          .catch((error) => {
            res.status(500).send({
              message: "Couldn't import data into database!",
              error: error.message,
            });
          });
      });
  } catch (error) {
    console.log(error);
    res.status(500).send({
      message: "Failed to upload the file: " + req.file.originalname,
    });
  }
};

const download = (_req, res) => {
  Employee.findAll().then((objs) => {
    let employees = [];

    objs.forEach((obj) => {
      const { id, name, email,
        username, dob, company,
        address, location, salary,
        about, role } = obj;
      employees.push({ id, name, email,
        username, dob, company,
        address, location, salary,
        about, role });
    });

    const csvFields = ['id', 'name', 'email',
                     'username', 'dob', 'company',
                     'address', 'location', 'salary',
                     'about', 'role'];
    const csvParser = new CsvParser({ csvFields });
    const csvData = csvParser.parse(employees);

    res.setHeader('Content-Type', 'text/csv');
    res.setHeader('Content-Disposition', 'attachment; filename=employees.csv');

    res.status(200).end(csvData);
  });
};

export default {
  upload,
  download,
};