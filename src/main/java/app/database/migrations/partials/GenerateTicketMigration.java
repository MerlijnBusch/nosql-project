package app.database.migrations.partials;

import app.database.migrations.Migrator;
import app.helpers.dateParser;
import com.mongodb.client.model.Filters;
import org.bson.Document;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class GenerateTicketMigration extends Migrator {

    private final String collectionName = "Tickets";
    private final dateParser parser = new dateParser();
    private final Date date = new Date(System.currentTimeMillis());

    public GenerateTicketMigration() throws ParseException {

        List<Document> tickets = new ArrayList<>();
        for(int i = 1; i < 100; i++){
            tickets.add(generateDocument());
        }

        this.database.insertMany(tickets, this.collectionName);
    }

    private Document generateDocument(){
        String[] priority = {"High", "Low", "Normal"};
        String[] type = {"Service", "Hardware", "Software"};
        String[] status = {"Closed", "Normal", "Escalated"};

        return new Document("Reported", parser.toString(date))
                .append("incident", generateRandomString( ThreadLocalRandom.current().nextInt(15, 30)))
                .append("type", selectRandomFromArray(type))
                .append("user_id", database.findOne(Filters.eq("type", "Service_desk"), "users").get("_id").toString())
                .append("employee_id", database.findOne(Filters.eq("type", "Employee"), "users").get("_id").toString())
                .append("priority", selectRandomFromArray(priority))
                .append("deadline", parser.toString(date))
                .append("description", generateRandomString( ThreadLocalRandom.current().nextInt(60, 200)))
                .append("status", selectRandomFromArray(status));
    }


}
