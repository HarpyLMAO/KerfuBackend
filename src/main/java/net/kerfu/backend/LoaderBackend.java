package net.kerfu.backend;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import spark.utils.IOUtils;

import java.io.*;
import java.net.URL;

import static com.mongodb.client.model.Filters.eq;
import static spark.Spark.get;
import static spark.Spark.port;

public class LoaderBackend {

    @SuppressWarnings("all")
    public static void main(String[] args) {
        MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb+srv://harpylmao:1234asdf@privatedb.u5zb8.mongodb.net/?retryWrites=true&w=majority"));
        MongoDatabase mongoDatabase = mongoClient.getDatabase("terminal");
        MongoCollection<Document> ipsCollection = mongoDatabase.getCollection("ips");

        port(8165);

        get("/plugins", (request, response) -> {
            String plugin = request.queryParams("plugin");

            URL url = new URL("http://checkip.amazonaws.com/");
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

            Bson filter = eq("_id", "34.118.86.66");
            FindIterable<Document> iterable = ipsCollection.find(filter);

            if (iterable.first() != null) {
                response.status(200);
                switch (plugin) {
                    case "meetup":
                        response.header("Content-Type", "application/octet-stream");
                        response.header("Content-disposition", "attachment; filename=meetup.jar;");
                        File meetupFile = new File("/downloads/meetup.jar");
                        try (InputStream in = new DataInputStream(new FileInputStream(meetupFile));
                             OutputStream out = response.raw().getOutputStream()) {

                            IOUtils.copy(in,out);
                            in.close();
                            out.close();
                            return response;
                        }
                    case "hub":
                        response.header("Content-Type", "application/octet-stream");
                        response.header("Content-disposition", "attachment; filename=hub.jar;");
                        File hubFile = new File("/downloads/hub.jar");
                        try (InputStream in = new DataInputStream(new FileInputStream(hubFile));
                             OutputStream out = response.raw().getOutputStream()) {

                            IOUtils.copy(in,out);
                            in.close();
                            out.close();
                            return response;
                        }
                    case "gkits":
                        response.header("Content-Type", "application/octet-stream");
                        response.header("Content-disposition", "attachment; filename=gkits.jar;");
                        File gkitsFile = new File("/downloads/gkits.jar");
                        try (InputStream in = new DataInputStream(new FileInputStream(gkitsFile));
                                OutputStream out = response.raw().getOutputStream()) {

                            IOUtils.copy(in,out);
                            in.close();
                            out.close();
                            return response;
                        }
                }
            } else {
                response.status(404);
            }
            return null;
        });
    }
}
