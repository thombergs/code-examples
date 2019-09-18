chmod +x gradlew
./gradlew clean build --info

cd spring-boot/starter && {
  chmod +x gradlew
  ./gradlew clean build --info
  cd ../../
}

cd spring-boot/validation && {
  chmod +x gradlew
  ./gradlew clean build --info
  cd ../../
}

cd spring-boot/mocking && {
  chmod +x gradlew
  ./gradlew clean build --info
  cd ../../
}
