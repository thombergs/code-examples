const { publishMessage } = require("../helper/pub-sub-config");
const topicName = "user_creation";

const welcome = (req, res) => {
  return res.status(200).json({
    success: true,
    message: "Welcome to User Profile Service:)",
  });
};

const createUser = async (req, res) => {
  console.log("hefd");
  let userObj = req.body;
  // create user profile logic goes here....

  let messageId = await publishMessage(topicName, userObj);
  return res.status(200).json({
    success: true,
    message: `Message ${messageId} published :)`,
  });
};
module.exports = { welcome, createUser };
