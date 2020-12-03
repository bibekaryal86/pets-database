package pets.database.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static pets.database.utils.Constants.*;

@Component
public class MongoDbConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    public MongoClient mongoClient() {
        Map<String, String> mongoConfig = getMongodbConfig();
        String connectionString = String.format("mongodb+srv://%s:%s@%s.lwcmb.mongodb.net/%s?retryWrites=true&w=majority",
                mongoConfig.get(MONGODB_USR_NAME), mongoConfig.get(MONGODB_USR_PWD), mongoConfig.get(MONGODB_ACC_NAME), database);

        return MongoClients.create(MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .build());
    }

    private Map<String, String> getMongodbConfig() {

        Map<String, String> mongoConfigMap = new HashMap<>();

        if (System.getProperty(MONGODB_ACC_NAME) != null) {
            // for running locally
            mongoConfigMap.put(MONGODB_ACC_NAME, System.getProperty(MONGODB_ACC_NAME));
            mongoConfigMap.put(MONGODB_USR_NAME, System.getProperty(MONGODB_USR_NAME));
            mongoConfigMap.put(MONGODB_USR_PWD, System.getProperty(MONGODB_USR_PWD));
        } else if (System.getenv(MONGODB_ACC_NAME) != null) {
            // for GCP
            mongoConfigMap.put(MONGODB_ACC_NAME, System.getenv(MONGODB_ACC_NAME));
            mongoConfigMap.put(MONGODB_USR_NAME, System.getenv(MONGODB_USR_NAME));
            mongoConfigMap.put(MONGODB_USR_PWD, System.getenv(MONGODB_USR_PWD));
        }

        return mongoConfigMap;
    }
}
