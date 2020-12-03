package pets.database.service;

import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersionName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static pets.database.utils.Constants.*;

@Service
public class GcpSecretManagerService {
    private static final Logger logger = LoggerFactory.getLogger(GcpSecretManagerService.class);

    private final String activeProfilesSpring;
    public GcpSecretManagerService(@Value("${spring.profiles.active}") String activeProfilesSpring) {
        this.activeProfilesSpring = activeProfilesSpring;
    }

    public Map<String, String> getMongodbConfig() {
        if ("production".equals(activeProfilesSpring)) {
            return getMongodbConfigProduction();
        } else if ("development".equals(activeProfilesSpring)) {
            return getMongodbConfigDevelopment();
        } else {
            return null;
        }
    }

    public Map<String, String> getAuthConfig() {
        if ("production".equals(activeProfilesSpring)) {
            return getAuthConfigProduction();
        } else if ("development".equals(activeProfilesSpring)) {
            return getAuthConfigDevelopment();
        } else {
            return null;
        }
    }

    private Map<String, String> getMongodbConfigDevelopment() {
        logger.info("Get MongoDB User Password Development: {}", activeProfilesSpring);
        Map<String, String> mongoConfigMap = new HashMap<>();
        mongoConfigMap.put(MONGODB_ACC_NAME, System.getProperty(MONGODB_ACC_NAME));
        mongoConfigMap.put(MONGODB_USR_NAME, System.getProperty(MONGODB_USR_NAME));
        mongoConfigMap.put(MONGODB_USR_PWD, System.getProperty(MONGODB_USR_PWD));
        return mongoConfigMap;
    }

    private Map<String, String> getMongodbConfigProduction() {
        logger.info("Get MongoDB User Password Production: {}", activeProfilesSpring);

        Map<String, String> mongoConfigMap = new HashMap<>();

        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            SecretVersionName secretVersionName = SecretVersionName.of(GCP_SECRET_MANAGER_PROJECT, MONGODB_ACC_NAME, SECRET_VERSION);
            AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);
            mongoConfigMap.put(MONGODB_ACC_NAME, response.getPayload().getData().toStringUtf8());

            secretVersionName = SecretVersionName.of(GCP_SECRET_MANAGER_PROJECT, MONGODB_USR_NAME, SECRET_VERSION);
            response = client.accessSecretVersion(secretVersionName);
            mongoConfigMap.put(MONGODB_USR_NAME, response.getPayload().getData().toStringUtf8());

            secretVersionName = SecretVersionName.of(GCP_SECRET_MANAGER_PROJECT, MONGODB_USR_PWD, SECRET_VERSION);
            response = client.accessSecretVersion(secretVersionName);
            mongoConfigMap.put(MONGODB_USR_PWD, response.getPayload().getData().toStringUtf8());
        } catch (Exception ex) {
            logger.error("Error retrieving DB User/Password: ", ex);
            mongoConfigMap = null;
        }

        return mongoConfigMap;
    }

    private Map<String, String> getAuthConfigDevelopment() {
        logger.info("Get Basic Auth User Password Development: {}", activeProfilesSpring);
        Map<String, String> authConfigMap = new HashMap<>();
        authConfigMap.put(BASIC_AUTH_USR, System.getProperty(BASIC_AUTH_USR));
        authConfigMap.put(BASIC_AUTH_PWD, System.getProperty(BASIC_AUTH_PWD));
        return authConfigMap;
    }

    private Map<String, String> getAuthConfigProduction() {
        logger.info("Get Basic Auth User Password Production: {}", activeProfilesSpring);

        Map<String, String> authConfigMap = new HashMap<>();

        try (SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            SecretVersionName secretVersionName = SecretVersionName.of(GCP_SECRET_MANAGER_PROJECT, BASIC_AUTH_USR, SECRET_VERSION);
            AccessSecretVersionResponse response = client.accessSecretVersion(secretVersionName);
            authConfigMap.put(BASIC_AUTH_USR, response.getPayload().getData().toStringUtf8());

            secretVersionName = SecretVersionName.of(GCP_SECRET_MANAGER_PROJECT, BASIC_AUTH_PWD, SECRET_VERSION);
            response = client.accessSecretVersion(secretVersionName);
            authConfigMap.put(BASIC_AUTH_PWD, response.getPayload().getData().toStringUtf8());
        } catch (Exception ex) {
            logger.error("Error retrieving DB User/Password: ", ex);
            authConfigMap = null;
        }

        return authConfigMap;
    }
}
