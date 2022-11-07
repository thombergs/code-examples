/// <reference types="cypress" />

before(() => {
  expect(Cypress.env('launchDarklyApiAvailable'), 'LaunchDarkly').to.be.true
});

const featureFlagKey = 'test-greeting-from-cypress';
const userId = 'CYPRESS_TEST_1234';

it('shows a casual greeting', () => {
  // target the given user to receive the first variation of the feature flag
  cy.task('cypress-ld-control:setFeatureFlagForUser', {
    featureFlagKey,
    userId,
    variationIndex: 0,
  })
  cy.visit('/')
  cy.contains('h1', 'Hello, World !!').should('be.visible')
});

it('shows a formal greeting', () => {
  cy.task('cypress-ld-control:setFeatureFlagForUser', {
    featureFlagKey,
    userId,
    variationIndex: 1,
  })
  cy.visit('/')
  cy.contains('h1', 'Good Morning, World !!').should('be.visible')
});

it('shows a vacation greeting', () => {
  cy.task('cypress-ld-control:setFeatureFlagForUser', {
    featureFlagKey,
    userId,
    variationIndex: 2,
  })
  cy.visit('/')
  cy.contains('h1', 'Hurrayyyyy, World').should('be.visible')

  // print the current state of the feature flag and its variations
  cy.task('cypress-ld-control:getFeatureFlag', featureFlagKey)
    .then(console.log)
    // let's print the variations to the Command Log side panel
    .its('variations')
    .then((variations) => {
      variations.forEach((v, k) => {
        cy.log(`${k}: ${v.name} is ${v.value}`)
      })
    })
});

it('shows all greetings', () => {
  cy.visit('/')
  cy.task('cypress-ld-control:setFeatureFlagForUser', {
    featureFlagKey,
    userId,
    variationIndex: 0,
  })
  cy.contains('h1', 'Hello, World !!')
    .should('be.visible')
    .wait(1000)

  cy.task('cypress-ld-control:setFeatureFlagForUser', {
    featureFlagKey,
    userId,
    variationIndex: 1,
  })
  cy.contains('h1', 'Good Morning, World !!').should('be.visible').wait(1000)

  cy.task('cypress-ld-control:setFeatureFlagForUser', {
    featureFlagKey,
    userId,
    variationIndex: 2,
  })
  cy.contains('h1', 'Hurrayyyyy, World !!').should('be.visible')
});

it('click a button', () => {
  cy.task('cypress-ld-control:setFeatureFlagForUser', {
    featureFlagKey: 'show-shiny-new-feature',
    userId: 'john_doe',
    variationIndex: 0,
  })
  cy.visit('/');
  var alerted = false;
  cy.on('window:alert', msg => alerted = msg);

  cy.get('#shiny-button').should('be.visible').click().then(
    () => expect(alerted).to.match(/A new shiny feature pops up!/));
});

after(() => {
  cy.task('cypress-ld-control:removeUserTarget', { featureFlagKey, userId })
});
