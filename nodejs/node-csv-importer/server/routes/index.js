import { Router } from 'express';
import csvController from '../controllers/csv.controller.js';
import getEmployees from '../controllers/employee.controller.js';
import uploadFile from '../middleware/upload.js';

const router = Router();

let routes = (app) => {
  // CSV
  router.post('/csv/upload', uploadFile.single('file'), csvController.upload);
  router.get('/csv/download', csvController.download);

  // Employees
  router.get('/employees', getEmployees);

  app.use("/api", router);
};

export default routes;