const {
  listenForPullMessages,
  listenForPushMessages,
} = require("../helper/pub-sub-config");
const subscriptionName = "email_subscription_pull";
const timeout = 60;

const welcome = (req, res) => {
  return res.status(200).json({
    success: true,
    message: "Welcome to Email Service:)",
  });
};

const pullEmail = async (req, res) => {
  try {
    await listenForPullMessages(subscriptionName, timeout);
    return res.status(200).json({
      success: true,
      message: "Pull message received successfully :",
    });
  } catch (error) {
    return res.status(500).json({
      success: false,
      message: "Couldn't receive pull message :(",
      data: error.message,
    });
  }
};

const pushEmail = async (req, res) => {
  try {
    let messageResponse = await listenForPushMessages(req.body.message.data);
    return res.status(200).json({
      success: true,
      message: "Push Message received successfully :)",
      data: messageResponse,
    });
  } catch (error) {
    return res.status(500).json({
      success: false,
      message: "Couldn't receive push message :(",
      data: error,
    });
  }
};

module.exports = { welcome, pullEmail, pushEmail };
