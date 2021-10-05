package app.database.migrations.partials;

import app.helpers.SHA512;
import app.database.migrations.Migrator;
import app.helpers.dateParser;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.mongodb.client.model.Filters.eq;

public class GenerateUserMigration extends Migrator {

    private final String collectionName = "users";
    private final dateParser parser = new dateParser();
    private final Date date = new Date(System.currentTimeMillis());

    public GenerateUserMigration() throws ParseException {

        Date date = new Date(System.currentTimeMillis());

        List<Document> users = new ArrayList<>();
        for(int i = 1; i < 100; i++){
            users.add(generateDocument());
        }

        this.database.insertMany(users, this.collectionName);
    }

    private Document generateDocument(){
        String[] type = {"Employee", "Service_desk"};

        return new Document("firstName", generateRandomString(ThreadLocalRandom.current().nextInt(5, 14)))
                .append("lastName", generateRandomString(ThreadLocalRandom.current().nextInt(5, 14)))
                .append("type", selectRandomFromArray(type))
                .append("email", generateRandomString(ThreadLocalRandom.current().nextInt(5, 14))+"@example.com")
                .append("phonenumber", "0687264563")
                .append("location_id", this.database.findOne(eq("location", "Amsterdam"),"locations").get("_id"))
                .append("password", SHA512.encryptThisString("password"))
                .append("created_at", dateParser.toString(date))
                .append("updated_at", dateParser.toString(date));
    }
}
