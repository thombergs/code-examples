import Sequelize from 'sequelize';
import dbConfig from '../config/db.config.js';

export const sequelize =  new Sequelize(dbConfig.DB, dbConfig.USER, dbConfig.PASSWORD, {
    host: dbConfig.HOST,
    dialect: dbConfig.dialect,
    pool: dbConfig.pool,
    logging: console.log
  }
);

sequelize.authenticate()
  .then(() => {
    console.log('Connection has been established successfully.');
    console.log('Creating tables ===================');
    sequelize.sync().then(() => {
        console.log('=============== Tables created per model');
    })
    .catch(err => {
        console.error('Unable to create tables:', err);    
    })
  })
  .catch(err => {
    console.error('Unable to connect to the database:', err);
});