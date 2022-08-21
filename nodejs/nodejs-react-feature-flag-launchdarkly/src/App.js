import React, { Component } from 'react';
import './App.css';
import * as LDClient from 'launchdarkly-js-client-sdk';

const isNewer = (a, b) => Date.parse(a.added) - Date.parse(b.added);

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
      // UI based user
      key: 'user_a'
    }
    // SDK requires Client-side ID for UI call
    this.ldclient = LDClient.initialize('62e9289ade464c10d842c2b3', user);
    this.ldclient.on('ready', this.onLaunchDarklyUpdated.bind(this));
    this.ldclient.on('change', this.onLaunchDarklyUpdated.bind(this));
  }
  onLaunchDarklyUpdated() {
    this.setState({
      featureFlags: {
        defaultSortingIsAdded: this.ldclient.variation('user-list-default-sorting-check', true),
        hideUser: this.ldclient.variation('hidden-user', '')
      }
    })
  }
  render() {
    if (!this.state.featureFlags) {
      return <div className="App">Loading....</div>
    }

    let sorter;
    console.log('Checking the environment variables: ', this.state.featureFlags);
    if (this.state.selectedSortOrder) {
      if (this.state.selectedSortOrder === 'added') {
        sorter = isNewer
      } else if (this.state.selectedSortOrder === 'natural') {
        sorter = undefined
      }
    } else {
      if (this.state.featureFlags.defaultSortingIsAdded) {
        sorter = isNewer
      } else {
        sorter = undefined
      }
    }
    return (
      <div className="App">
        <div style ={{ fontWeight: 'bold' }}><h1>Users List</h1></div>
        <div
            style={{ fontWeight: sorter === undefined ? 'bold' : 'normal'}}
            onClick={() => this.setState({ selectedSortOrder: 'natural' })}>Natural sorting</div>
        <div
          style={{ fontWeight: sorter === isNewer ? 'bold' : 'normal'}}
          onClick={() => this.setState({ selectedSortOrder: 'added' })}>Time sorting</div>
        <ul>
          {this.state.users.slice().sort(sorter).map(user =>
             <div>{ this.state.featureFlags.hideUser === 'John Doe'
              && user.name === 'John Doe' ? '*********' : user.name }</div>
          )}
        </ul>
      </div>
    );
  }
}

export default App;
