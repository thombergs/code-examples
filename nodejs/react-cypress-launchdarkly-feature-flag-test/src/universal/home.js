import React from 'react';
import PropTypes from 'prop-types';
import styled from 'styled-components';
import { withLDConsumer } from 'launchdarkly-react-client-sdk';

const Root = styled.div`
  color: #001b44;
`;
const Heading = styled.h1`
  color: #00449e;
`;
const Home = ({ flags }) => (
  <Root>
    <Heading>{flags.testingLaunchDarklyControlFromCypress}, World</Heading>
    <div>
      This is a LaunchDarkly React example project. The message above changes the greeting,
      based on the current feature flag variation.
    </div>
  </Root>
);

Home.propTypes = {
  flags: PropTypes.object.isRequired,
};

export default withLDConsumer()(Home);
