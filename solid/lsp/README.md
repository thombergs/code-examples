# Liskov Substition Principle

There are 5 programs under com/reflectoring/examples/lsp/paymentexample.

Run in this order to see class design evolution:
1. InitialPaymentRequirementsMainApp
2. LSPViolationMainApp
3. ForceFitStillBrokenMainApp 
4. ForceFitAndConditionalCodeMainApp 
5. RedesignedMainApp

Packages are organized similarly: violation, forcefit, forcefitandconditional, 
and redesigned have specific classes modified at that point in the evolution.

Common code is in common, common/exceptions, common/external and 
common/instruments.

## Blog posts 

* [The Liskov Substitution Principle Explained](https://reflectoring.io/lsp-explained/) 