# pets-database

** Migrated to Public Repository after removing credentials. **

This REST API is implemented to execute CRUD functions in MongoDB

THERE ARE NO BUSINESS LOGIC IN THIS SERVICE, IT IS LIMITED TO CRUD FUNCTIONS ONLY

The @NonNull annotation is kept as is in the Request Models to identify 
which ones should have been passed with values

To run the App:

(1) From IDE, gradlew bootrun 

(2) From Jar, java -jar JAR_NAME (provide environment variables)
For example:
java -jar -Dspring.profiles.active=development -DMONGODB_ACC_NAME=abcd -DMONGODB_USR_NAME=efgh -DMONGODB_USR_PWD=ijkl JAR_NAME

The App is deployed to Google App Engine. The app.yaml configuration file can be found on src/main/appengine folder.
The app.yaml includes another app-mongodb.yaml file which includes mongodb credentials, which is not committed.
To deploy the app to GCP, copy the app jar from build/libs folder to the appengine folder, and from the appengine folder
use gcloud SDK commands: (1) gcloud init (2) gcloud app deploy app.yaml.

The GCP specific configurations are based on active spring profile of production
mainly in application-production.yml and logback.yml

Instead of environment variables, the app can also use GCP Secret Manager (preferred/recommended) to access DB
credentials when running on Google App Engine. See branch google-secret-manager. One caveat of using Secret Manager is
that increases the jar file (32MB -> 60MB) and JVM memory usage (290MB -> 390MB). So maybe not preferable for Free tier.

Three apps from the following repos need to be running together:
* https://github.com/bibekaryal86/pets-database
* https://github.com/bibekaryal86/pets-service
* https://github.com/bibekaryal86/pets-ui-mpa

Deployed to:
* GCP: https://pets-database.appspot.com/pets-database/tests/ping
