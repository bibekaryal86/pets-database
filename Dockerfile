FROM openjdk:11-jre-slim-bullseye
RUN adduser --system --group springdocker
USER springdocker:springdocker
ARG JAR_FILE=app/build/libs/pets-database.jar
COPY ${JAR_FILE} pets-database.jar
ENTRYPOINT ["java","-jar", \
#"-DSPRING_PROFILES_ACTIVE=docker", \
#"-DMONGODB_ACC_NAME=some_account_name", \
#"-DMONGODB_USR_NAME=some_username", \
#"-DMONGODB_USR_PWD=some_password", \
#"-DBASIC_AUTH_USR=another_username", \
#"-DBASIC_AUTH_PWD=another_password", \
"/pets-database.jar"]
