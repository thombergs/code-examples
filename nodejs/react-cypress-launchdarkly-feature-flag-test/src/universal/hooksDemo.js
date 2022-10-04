import React from 'react';
import styled from 'styled-components';
import { useFlags } from 'launchdarkly-react-client-sdk';

const Root = styled.div`
  color: #001b44;
`;
const Heading = styled.h1`
  color: #00449e;
`;
const theme = {
  blue: {
    default: "#3f51b5",
    hover: "#283593"
  }
};

const Button = styled.button`
    background-color: ${(props) => theme[props.theme].default};
    color: white;
    padding: 5px 15px;
    border-radius: 5px;
    outline: 0;
    text-transform: uppercase;
    margin: 10px 0px;
    cursor: pointer;
    box-shadow: 0px 2px 2px lightgray;
    transition: ease background-color 250ms;
    &:hover {
      background-color: ${(props) => theme[props.theme].hover};
    }
    &:disabled {
      cursor: default;
      opacity: 0.7;
    }
  `;

const clickMe = () => {
  alert("A new shiny feature pops up!");
};

const HooksDemo = () => {
  const { testGreetingFromCypress, showShinyNewFeature } = useFlags();

  return (
    <Root>
      <Heading>{testGreetingFromCypress}, World !!</Heading>
    <div>
      This is the equivalent LaunchDarkly React example project using hooks. The message above changes the greeting,
      based on the current feature flag variation.
    </div>
    <div>
    {showShinyNewFeature ? 
      <Button id='shiny-button' theme='blue' onClick={clickMe}>Shiny New Feature</Button>: ''}
    </div>
    <div>
      {showShinyNewFeature ? 'This button will show new shiny feature in UI on clicking it.': ''}
    </div>
    </Root>
  );
};

export default HooksDemo;
