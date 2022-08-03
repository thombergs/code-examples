import React from 'react';
import styled from 'styled-components';
import { useFlags } from 'launchdarkly-react-client-sdk';

const Root = styled.div`
  color: #001b44;
`;
const Heading = styled.h1`
  color: #00449e;
`;

const HooksDemo = () => {
  const { testingLaunchDarklyControlFromCypress } = useFlags();

  return (
    <Root>
      <Heading>{testingLaunchDarklyControlFromCypress}, World</Heading>
      <div>
        This is the equivalent LaunchDarkly demo app using hooks.
      </div>
    </Root>
  );
};

export default HooksDemo;
