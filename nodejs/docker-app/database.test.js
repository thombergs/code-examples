const db = require("./database");

beforeAll(async () => {
  await db.sequelize.sync({ force: false });
});

test("Add new User", async () => {
  expect.assertions(1);
  const user = await db.Users.create({
    id: 1234,
    name: "Denver Mike",
    age: 18,
  });
  expect(user.id).toEqual(1234);
});

test("Get a User", async () => {
  expect.assertions(2);
  const user = await db.Users.findByPk(1234);
  expect(user.name).toEqual("Denver Mike");
  expect(user.age).toEqual(18);
});

test("Delete state", async () => {
  expect.assertions(1);
  await db.Users.destroy({
    where: {
      id: 1234,
    },
  });
  const user = await db.Users.findByPk(1234);
  expect(user).toBeNull();
});

afterAll(async () => {
  await db.sequelize.close();
});
