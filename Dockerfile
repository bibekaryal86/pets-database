FROM adoptopenjdk/openjdk11:alpine
RUN addgroup -S springdocker && adduser -S springdocker -G springdocker
USER springdocker:springdocker
ARG JAR_FILE=app/build/libs/pets-database.jar
COPY ${JAR_FILE} pets-database.jar
ENTRYPOINT ["java","-jar", \
#"-Dspring.profiles.active=docker", \
#"-DBASIC_AUTH_PWD=some_password", \
#"-DBASIC_AUTH_USR=some_username", \
#"-DMONGODB_ACC_NAME=some_account_name", \
#"-DMONGODB_USR_NAME=another_username", \
#"-DMONGODB_USR_PWD=another_password", \
"/pets-database.jar"]
# provide environment variables in docker-compose
