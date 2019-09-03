buildDir=$(pwd)
cd $(buildDir) && ./gradlew clean build --info
cd $(buildDir)/spring-boot/starter && ./gradlew clean build --info
cd $(buildDir)/spring-boot/validation && ./gradlew clean build --info