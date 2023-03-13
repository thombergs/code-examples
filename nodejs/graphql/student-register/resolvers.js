const { Student } = require("./models/Student.js");

// GraphQL Resolvers
const resolvers = {
  Query: {
    hello: () => "Hello from Reflectoring Blog",
    welcome: (parent, args) => `Hello ${args.name}`,
    students: async () => await Student.find({}), // return array of students
    student: async (parent, args) => await Student.findById(args.id), // return student by id
  },
  Mutation: {
    create: async (parent, args) => {
      const { firstName, lastName, age } = args;
      const newStudent = new Student({
        firstName,
        lastName,
        age,
      });
      await newStudent.save();
      return newStudent;
    },
    update: async (parent, args) => {
      const { id } = args;
      const updatedStudent = await Student.findByIdAndUpdate(id, args);
      if (!updatedStudent) {
        throw new Error(`Student with ID ${id} not found`);
      }
      return updatedStudent;
    },
    delete: async (parent, args) => {
      const { id } = args;
      const deletedStudent = await Student.findByIdAndDelete(id);
      if (!deletedStudent) {
        throw new Error(`Student with ID ${id} not found`);
      }
      return deletedStudent;
    },
  },
};

module.exports = { resolvers };
