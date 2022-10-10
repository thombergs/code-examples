import LaunchDarkly from 'launchdarkly-node-server-sdk';
import express from 'express';

const app = express();

app.get("/", async (req, res) => {
  const flags = await init();
  res.send(flags);
});
app.listen(8080);

const LD_SDK_KEY = 'sdk-********-****-****-****-************';
const userName = 'admin';
let client;

async function init() {
  if (!client) {
    client = LaunchDarkly.init(LD_SDK_KEY);
    await client.waitForInitialization();
  }

  const user = {
    key: userName
  };
  const allFlagsState = await client.allFlagsState(user);
  const flags = allFlagsState.allValues();
  return flags;
}
