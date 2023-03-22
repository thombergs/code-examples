const gql = require("graphql-tag");

const typeDefs = gql`
  type Query {
    hello: String
    welcome(name: String): String
    students: [Student] #return array of students
    student(id: ID): Student #return student by id
  }
  type Student {
    id: ID
    firstName: String
    lastName: String
    age: Int
  }
  type Mutation {
    create(firstName: String, lastName: String, age: Int): Student
    update(id: ID, firstName: String, lastName: String, age: Int): Student
    delete(id: ID): Student
  }
`;

module.exports = { typeDefs };
