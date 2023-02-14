import React from 'react';
import { Switch, Route, Redirect } from 'react-router-dom';
import { withLDProvider } from 'launchdarkly-react-client-sdk';
import SiteNav from './siteNav';
import Home from './home';
import HooksDemo from './hooksDemo';

const App = () => (
  <div>
    <SiteNav />
    <main>
      <Switch>
        <Route exact path="/" component={Home} />
        <Route path="/home">
          <Redirect to="/" />
        </Route>
        <Route path="/hooks" component={HooksDemo} />
      </Switch>
    </main>
  </div>
);

// Set clientSideID to your own Client-side ID. You can find this in
// your LaunchDarkly portal under Account settings / Projects
// https://docs.launchdarkly.com/sdk/client-side/javascript#initializing-the-client
const user = {
  key: 'CYPRESS_TEST_1234'
};
export default withLDProvider({ clientSideID: '63**********************', user })(App);
