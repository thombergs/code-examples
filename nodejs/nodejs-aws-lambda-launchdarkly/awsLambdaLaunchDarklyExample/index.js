const LaunchDarkly = require("launchdarkly-node-server-sdk");
const client = LaunchDarkly.init("sdk-d2432dc7-e56a-458b-9f93-0361af47d578");

exports.handler = async (event) => {
  // place your S3 bucket URL here -- don't forget to add /site/
  let URL =
    "https://arpendu-nodejs-lambda-s3bucketforwebsitecontent-236bs71h1km3.s3.eu-north-1.amazonaws.com/site/";

  await client.waitForInitialization();
  let viewBetaSite = await client.variation(
    "rebrand",
    { key: event.Records[0].cf.request.clientIp },
    false
  );
  console.log(`LaunchDarkly returned ${viewBetaSite}`);

  if (viewBetaSite) URL += "beta/index.html";
  else URL += "index.html";
  return {
    status: "302",
    statusDescription: "Found",
    headers: {
      location: [
        {
          key: "Location",
          value: URL,
        },
      ],
    },
  };
};
