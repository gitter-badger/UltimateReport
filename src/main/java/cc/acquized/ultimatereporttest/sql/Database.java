package cc.acquized.ultimatereporttest.sql;

import cc.acquized.ultimatereporttest.Main;

import java.util.logging.Level;

public class Database {

    public static void connect() {
        if(!Main.getInstance().firstRun) {

        } else {
            Main.getInstance().getLogger().log(Level.SEVERE, "It looks like this is the first time you run the Plugin. For that reason, i didn't connect to any database.");
        }
    }

}
