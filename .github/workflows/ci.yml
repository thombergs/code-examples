name: CI

on: 
  push:
    paths-ignore:
      - '**/*.md'
  pull_request:
    paths-ignore:
      - '**/*.md'

jobs:

  build:

    runs-on: ubuntu-latest
    strategy:
      matrix:
        # The MODULE environment variable is evaluated in build-all.sh to run a subset
        # of the builds. This way, multiple modules can be built in parallel.
        module: [ "module1", "module2", "module3", "module4", "module5", "module6", "module7" ]

    steps:

    - name: "Checkout sources"
      uses: actions/checkout@v1

    - name: "Setup Java"
      uses: actions/setup-java@v1
      with:
        java-version: 13

    - name: "Build module ${{ matrix.module }}"
      env:
        MODULE: ${{ matrix.module }}
        # We don't actually need AWS credentials in the tests, but LocalStack
        # complains if they're not there, so we add dummies to the environment.
        AWS_ACCESS_KEY_ID: dummy
        AWS_SECRET_ACCESS_KEY: dummy
        AWS_REGION: us-east-1
      run: |
        chmod 755 build-all.sh && ./build-all.sh $MODULE

    - name: "Zip build reports"
      if: failure()
      run: zip -r reports.zip **/**/build/reports

    - uses: actions/upload-artifact@v1
      name: "Upload build reports"
      if: failure()
      with:
        name: reports
        path: reports.zip
