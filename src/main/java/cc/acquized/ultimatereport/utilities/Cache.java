package cc.acquized.ultimatereport.utilities;

import cc.acquized.ultimatereport.Main;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.UUID;
import java.util.logging.Level;

public class Cache {

    public static UUID convertToUUID(String username) {
        try {
            URL url = new URL("https://eu.mc-api.net/v3/uuid/" + username + "/csv");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String[] args = reader.readLine().split(",");
            return UUID.fromString(args[3]); // Maybe i should use args[2] (UUID without dashes), i don't know how UUID#fromString works.
        } catch (IOException ex) {
            Main.getInstance().getLogger().log(Level.SEVERE, "[UltimateReport] Could not convert " + username + " to UUID. Stacktrace:");
            ex.printStackTrace();
            return UUID.randomUUID(); // Currently only returns a pseudo UUID
        }
    }

    public static String convertToUsername(UUID uuid) {
        try {
            URL url = new URL("https://eu.mc-api.net/v3/name/" + uuid.toString() + "/csv");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String[] args = reader.readLine().split(",");
            return args[1];
        } catch (IOException ex) {
            Main.getInstance().getLogger().log(Level.SEVERE, "[UltimateReport] Could not convert " + uuid.toString() + " to Username. Stacktrace:");
            ex.printStackTrace();
            return Arrays.toString(new Byte[2147483647]); // Currently only returns a pseudo String
        }
    }

}
