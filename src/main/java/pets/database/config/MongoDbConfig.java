package pets.database.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.stereotype.Component;
import pets.database.service.GcpSecretManagerService;

import java.util.Map;

import static pets.database.utils.Constants.*;

@Component
public class MongoDbConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Autowired
    private GcpSecretManagerService gcpSecretManagerService;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    public MongoClient mongoClient() {
        Map<String, String> mongoConfig = gcpSecretManagerService.getMongodbConfig();
        String connectionString = String.format("mongodb+srv://%s:%s@%s.lwcmb.mongodb.net/%s?retryWrites=true&w=majority",
                mongoConfig.get(MONGODB_USR_NAME), mongoConfig.get(MONGODB_USR_PWD), mongoConfig.get(MONGODB_ACC_NAME), database);

        return MongoClients.create(MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .build());
    }
}
