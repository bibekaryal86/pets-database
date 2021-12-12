# pets-database-layer

This REST API is implemented to execute CRUD functions in MongoDB. 
There are no business logic in this service, it is limited to model and CRUD functions only.

To run the app, we need to supply the following environment variables:
* Active Profile
  * spring.profiles.active (development, docker, production)
* Application security details:
  * BASIC_AUTH_USR (username to enforce spring security)
  * BASIC_AUTH_PWD (password to enforce spring security)
* MongoDB Database Details:
  * MONGODB_ACC_NAME (mongodb collection name)
  * MONGODB_USR_NAME (mongodb user name)
  * MONGODB_USR_PASSWORD (mongodb password)
* The final run command looks like this:
  * java -jar -D"spring.profiles.active=development" -DBASIC_AUTH_USR=some_username -DBASIC_AUTH_PWD=some_password -DMONGODB_ACC_NAME=account_name -DMONGODB_USR_NAME=another_username -DMONGODB_USR_PWD=another_password  JARFILE.jar

This app is one of the five apps that form the PETS (Personal Expenses Tracking System) application: 
* https://github.com/bibekaryal86/pets-database-layer (this)
* https://github.com/bibekaryal86/pets-service-layer
* https://github.com/bibekaryal86/pets-authenticate-layer
* https://github.com/bibekaryal86/pets-gateway-layer
* https://github.com/bibekaryal86/pets-ui-layer

This app is deployed in Google Cloud Project. The GCP configurations are found in the `gcp` folder in the project root.
To deploy to GCP, we need to copy the jar file to that folder and use gcloud app deploy terminal command.
* App Test Link: https://pets-database.appspot.com/pets-database/tests/ping
