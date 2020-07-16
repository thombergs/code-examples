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
    ./gradlew clean build
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

run_gradle_task() {
  MODULE_PATH=$1
  TASK_NAME=$2
  echo ""
  echo "+++"
  echo "+++ RUNNING GRADLE TASK $MODULE_PATH : $TASK_NAME"
  echo "+++"
  cd $MODULE_PATH && {
    chmod +x gradlew
    ./gradlew $TASK_NAME
    if [ $? -ne 0 ]
    then
      echo ""
      echo "+++"
      echo "+++ GRADLE TASK $MODULE_PATH : $TASK_NAME FAILED"
      echo "+++"
      exit 1
    else
      echo ""
      echo "+++"
      echo "+++ GRADLE TASK $MODULE_PATH : $TASK_NAME SUCCESSFUL"
      echo "+++"
    fi
    cd $MAIN_DIR
  }
}

build_maven_module() {
  MODULE_PATH=$1
  echo ""
  echo "+++"
  echo "+++ BUILDING MODULE $MODULE_PATH"
  echo "+++"
  cd $MODULE_PATH && {
    chmod +x mvnw
    ./mvnw clean package
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

build_maven_module "resilience4j/retry"
build_maven_module "solid/lsp"
run_gradle_task "spring-boot/thymeleaf-vue" "npmInstall"
build_gradle_module "spring-boot/thymeleaf-vue"
build_gradle_module "spring-boot/spring-boot-springdoc"
build_maven_module "spring-boot/dependency-injection"
build_maven_module "spring-boot/spring-boot-openapi"
build_maven_module "spring-boot/data-migration/liquibase"
build_gradle_module "spring-boot/boundaries"
build_gradle_module "spring-boot/argumentresolver"
build_gradle_module "spring-data/spring-data-jdbc-converter"
build_gradle_module "solid"
build_gradle_module "spring-boot/data-migration/flyway"
build_gradle_module "reactive"
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
build_gradle_module "spring-boot/password-encoding"
build_gradle_module "spring-boot/testcontainers"
build_gradle_module "spring-boot/hazelcast/hazelcast-embedded-cache"
build_gradle_module "spring-boot/hazelcast/hazelcast-client-server"
build_gradle_module "spring-boot/cache"
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
