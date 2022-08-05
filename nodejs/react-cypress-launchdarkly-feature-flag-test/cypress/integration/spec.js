/// <reference types="cypress" />

before(() => {
  expect(Cypress.env('launchDarklyApiAvailable'), 'LaunchDarkly').to.be.true
})

const featureFlagKey = 'testing-launch-darkly-control-from-cypress'
const userId = 'USER_1234'

it('shows the casual greeting', () => {
  // target the given user to receive the first variation of the feature flag
  cy.task('cypress-ld-control:setFeatureFlagForUser', {
    featureFlagKey,
    userId,
    variationIndex: 0,
  })
  cy.visit('/')
  cy.contains('h1', 'Hello, World').should('be.visible')
})

it('shows formal greeting', () => {
  cy.task('cypress-ld-control:setFeatureFlagForUser', {
    featureFlagKey,
    userId,
    variationIndex: 1,
  })
  cy.visit('/')
  cy.contains('h1', 'How are you doing, World').should('be.visible')
})

it('shows vacation greeting', () => {
  cy.task('cypress-ld-control:setFeatureFlagForUser', {
    featureFlagKey,
    userId,
    variationIndex: 2,
  })
  cy.visit('/')
  cy.contains('h1', 'Yippeeee, World').should('be.visible')

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
})

it('shows all greetings', () => {
  cy.visit('/')
  cy.task('cypress-ld-control:setFeatureFlagForUser', {
    featureFlagKey,
    userId,
    variationIndex: 0,
  })
  cy.contains('h1', 'Hello, World')
    .should('be.visible')
    .wait(1000)

  cy.task('cypress-ld-control:setFeatureFlagForUser', {
    featureFlagKey,
    userId,
    variationIndex: 1,
  })
  cy.contains('h1', 'How are you doing, World').should('be.visible').wait(1000)

  cy.task('cypress-ld-control:setFeatureFlagForUser', {
    featureFlagKey,
    userId,
    variationIndex: 2,
  })
  cy.contains('h1', 'Yippeeee, World').should('be.visible')
})

after(() => {
  cy.task('cypress-ld-control:removeUserTarget', { featureFlagKey, userId })
})
