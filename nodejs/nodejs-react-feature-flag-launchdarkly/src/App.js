import React, { Component } from 'react';
import styled from "styled-components";
import './App.css';
import * as LDClient from 'launchdarkly-js-client-sdk';

const isNewer = (a, b) => Date.parse(a.added) - Date.parse(b.added);

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

class App extends Component {
  constructor() {
    super()
    this.state = {
      selectedSortOrder: null,
      users: [
        { name: 'John Doe', added: new Date('2022-7-27') },
        { name: 'Allen Witt', added: new Date('2022-6-30') },
        { name: 'Cheryl Strong', added: new Date('2022-7-02') },
        { name: 'Marty Byrde', added: new Date('2022-5-03') },
        { name: 'Wendy Byrde', added: new Date('2022-6-03') },
      ]
    }
  }
  componentDidMount() {
    const user = {
      // UI based logged-in user
      key: 'john_doe'
    }
    // SDK requires Client-side ID for UI call
    this.ldclient = LDClient.initialize('62e*********************', user);
    this.ldclient.on('ready', this.onLaunchDarklyUpdated.bind(this));
    this.ldclient.on('change', this.onLaunchDarklyUpdated.bind(this));
  }
  onLaunchDarklyUpdated() {
    this.setState({
      featureFlags: {
        defaultSortingType: this.ldclient.variation('sort-order', "natural"),
        showShinyNewFeature: this.ldclient.variation('show-shiny-new-feature', false)
      }
    })
  }
  render() {
    if (!this.state.featureFlags) {
      return <div className="App">Loading....</div>
    }

    let sorter;
    if (this.state.selectedSortOrder) {
      if (this.state.selectedSortOrder === 'timestamp') {
        sorter = isNewer
      } else if (this.state.selectedSortOrder === 'natural') {
        sorter = undefined
      }
    } else {
      if (this.state.featureFlags.defaultSortingType === 'timestamp') {
        sorter = isNewer
      } else {
        sorter = undefined
      }
    }
    return (
      <div className="App">
        <div>{ 
            this.state.featureFlags.showShinyNewFeature
              ? <Button theme='blue' onClick={clickMe}>Shiny New Feature</Button>
              : '' }
        </div>
        <div style ={{ fontWeight: 'bold' }}><h1>Users List</h1></div>
        <div
            style={{ fontWeight: sorter === undefined ? 'bold' : 'normal'}}
            onClick={() => this.setState({ selectedSortOrder: 'natural' })}>Natural sorting</div>
        <div
          style={{ fontWeight: sorter === isNewer ? 'bold' : 'normal'}}
          onClick={() => this.setState({ selectedSortOrder: 'timestamp' })}>Time sorting</div>
        <ul>
          {this.state.users.slice().sort(sorter).map(user =>
             <div>{ user.name }</div>
          )}
        </ul>
      </div>
    );
  }
}

export default App;