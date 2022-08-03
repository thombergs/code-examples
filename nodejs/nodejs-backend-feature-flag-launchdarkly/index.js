import LaunchDarkly from 'launchdarkly-node-server-sdk';
import express from 'express';

const app = express();

app.get("/", async (req, res) => {
  const flags = await init();
  res.send(flags);
});
app.listen(8080);

const sdkKey = 'sdk-d2432dc7-e56a-458b-9f93-0361af47d578';
const buyerKey = 'mob-b9d6d4d4-4300-46fa-9b13-d9eac89f9794';
let client;

async function init() {
  if (!client) {
    client = LaunchDarkly.init(sdkKey);
    await client.waitForInitialization();
  }

  const user = {
    key: buyerKey
  };
  const allFlagsState = await client.allFlagsState(user);
  const flags = allFlagsState.allValues();
  return flags;
}
