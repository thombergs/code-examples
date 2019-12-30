#!/bin/bash

MAIN_DIR=$PWD

build_gradle_module() {
  MODULE_PATH=$1
  echo ""
  echo "+++"
  echo "+++ BUILDING MODULE $MODULE_PATH"
  echo "+++"
  cd $MODULE_PATH && {
    chmod +x gradlew
    ./gradlew clean build --info --stacktrace
    if [ $? -ne 0 ]
    then
      echo ""
      echo "+++"
      echo "+++ BUILDING MODULE $MODULE_PATH FAILED"
      echo "+++"
      exit 1
    else
      echo ""
      echo "+++"
      echo "+++ BUILDING MODULE $MODULE_PATH SUCCESSFUL"
      echo "+++"
    fi
    cd $MAIN_DIR
  }
}

chmod +x gradlew

build_gradle_module "junit/assumptions"
build_gradle_module "logging"
build_gradle_module "pact/pact-feign-consumer"
# currently disabled since the consumer build won't run
# build_gradle_module "pact/pact-message-consumer"
# build_gradle_module "pact/pact-message-producer"
build_gradle_module "pact/pact-spring-provider"
build_gradle_module "patterns"
build_gradle_module "spring-boot/conditionals"
build_gradle_module "spring-boot/configuration"
build_gradle_module "spring-boot/mocking"
build_gradle_module "spring-boot/modular"
build_gradle_module "spring-boot/paging"
build_gradle_module "spring-boot/rabbitmq-event-brokering"
build_gradle_module "spring-boot/spring-boot-logging"
build_gradle_module "spring-boot/spring-boot-testing"
build_gradle_module "spring-boot/starter"
build_gradle_module "spring-boot/startup"
build_gradle_module "spring-boot/static"
build_gradle_module "spring-boot/validation"
build_gradle_module "spring-boot/profiles"
build_gradle_module "spring-cloud/feign-with-spring-data-rest"
build_gradle_module "spring-cloud/sleuth-downstream-service"
build_gradle_module "spring-cloud/sleuth-upstream-service"
build_gradle_module "spring-cloud/spring-cloud-contract-consumer"
build_gradle_module "spring-cloud/spring-cloud-contract-provider"
build_gradle_module "spring-data/spring-data-rest-associations"
build_gradle_module "spring-data/spring-data-rest-springfox"
build_gradle_module "tools/jacoco"

echo ""
echo "+++"
echo "+++ ALL MODULES SUCCESSFUL"
echo "+++"