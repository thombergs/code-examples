import chalk from 'chalk';
import LaunchDarkly from 'launchdarkly-node-server-sdk';

const LD_SDK_KEY = 'sdk-d2432dc7-e56a-458b-9f93-0361af47d578';
const launchDarklyClient = LaunchDarkly.init( LD_SDK_KEY );
 
// We're just going to mock some traffic to a
// mock function that handles mock request /
// response structures mock all the things!
(async function sendMockHttpRequest() { 
	try {
		await launchDarklyClient.waitForInitialization(); 
		await mockHandleHttpRequest({
			ip: "192.168.100.40"
		}); 
	} catch ( error ) { 
		console.warn( "Error sending mock HTTP request." );
		console.error( error ); 
	} 
	setTimeout( sendMockHttpRequest, 1000 );
 
})();

async function mockHandleHttpRequest( request ) {
 
	// Get the collection of blocked IP-addresses. Internally, we've defined our feature
	// flag value as a JSON (JavaScript Object Notation) type. This means that
	// LaunchDarkly automatically handles the stringification, streaming, and parsing of
	// it for us. In other words, this .variation() call doesn't return a JSON payload -
	// it returns the original data structure that we provided in the LD dashboard.
	var ipBlacklist = await launchDarklyClient.variation(
		'ip-blacklisted-list',
		{
			key: 'mob-b9d6d4d4-4300-46fa-9b13-d9eac89f9794' // The static "user" for this task.
		},
		// Since the JSON type is automatically parsed by the LaunchDarkly client, our
		// default value should be the same type as the intended payload. In other words,
		// the default value is NOT a JSON STRING but, rather, AN ARRAY. In this case,
		// we're defaulting to the empty array so the site can FAIL OPEN.
		[]
	);
 
	// Check to see if the incoming request IP should be blocked.
	if ( ipBlacklist.includes( request.ip ) ) { 
		console.log( chalk.red( 'Blocking IP', chalk.bold( request.ip ) ) );
		console.log( chalk.red.italic( `... blocking one of ${ ipBlacklist.length }`
		+ ` known IP addresses.` ) ); 
	} else { 
		console.log( chalk.green( 'Allowing IP', chalk.bold( request.ip ) ) ); 
	} 
}