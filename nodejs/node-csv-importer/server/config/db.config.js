const HOST = "10.204.133.84";
const USER = "postgres";
const PASSWORD = "Welcome123";
const DB = "csvdb";
const dialect = "postgres";
const pool = {
    max: 5,
    min: 0,
    acquire: 30000,
    idle: 10000
};

export default {
    HOST,
    USER,
    PASSWORD,
    DB,
    dialect,
    pool
};