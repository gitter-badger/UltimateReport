package cc.acquized.ultimatereport.sql;

import cc.acquized.ultimatereport.Main;
import cc.acquized.ultimatereport.file.Config;
import cc.acquized.ultimatereport.framework.Report;
import cc.acquized.ultimatereport.utilities.Cache;
import cc.acquized.ultimatereport.utilities.Note;

import javax.xml.crypto.Data;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class DataStorage {

    private static DataStorage instance;
    public String prefix = Config.getConfig().getString("Database.TablePrefix");

    @Note("Creates the Table. Maybe i should use a StringBuilder")
    public void createTables() {
        try {
            Statement s = Database.getConnection().createStatement();
            s.executeUpdate("CREATE TABLE IF NOT EXISTS `" + prefix + "waiting` (`victim` VARCHAR(36), `reporter` VARCHAR(36), `reason` VARCHAR(64));");
            s.close();
        } catch (SQLException ex) {
            Main.getInstance().getLogger().log(Level.SEVERE, "[UltimateReport] An error in MySQL occured. Message: " + ex.getMessage());
        }
        try {
            Statement s = Database.getConnection().createStatement();
            s.executeUpdate("CREATE TABLE IF NOT EXISTS `" + prefix + "history` (`victim` VARCHAR(36), `reporter` VARCHAR(36), `reason` VARCHAR(64));");
            s.close();
        } catch (SQLException ex) {
            Main.getInstance().getLogger().log(Level.SEVERE, "[UltimateReport] An error in MySQL occured. Message: " + ex.getMessage());
        }
    }

    // ---------- WAITING QUEUE ---------- \\

    @Note("Adds a entry to the Waiting Queue. It will be displayed as soon a staff joins.")
    public void addToWaitingQueue(Report r) {
        try {
            Statement s = Database.getConnection().createStatement();
            s.executeUpdate("INSERT INTO `" + prefix + "waiting` (`victim`, `reporter`, `reason`) VALUES ('" + Cache.convertToUUID(r.getVictim()) + "', '" + Cache.convertToUUID(r.getReporter()) + "', '" + r.getReason() + "');");
            s.close();
        } catch (SQLException ex) {
            Main.getInstance().getLogger().log(Level.SEVERE, "[UltimateReport] An error in MySQL occured. Message: " + ex.getMessage());
        }
    }

    @Note("Gets all entrys from the Waiting Queue.")
    public Report[] getWaitingQueueEntries() {
        List<Report> reports = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs;
        try {
            ps = Database.getConnection().prepareStatement("SELECT `victim`, `reporter`, `reason` FROM `" + prefix + "waiting`;");
            rs = ps.executeQuery();
            while((rs != null) && (rs.next())) {
                reports.add(new Report(Cache.convertToUsername(UUID.fromString(rs.getString("reporter"))), Cache.convertToUsername(UUID.fromString(rs.getString("victim"))), rs.getString("reason")));
            }
        } catch (SQLException ex) {
            Main.getInstance().getLogger().log(Level.SEVERE, "[UltimateReport] An error in MySQL occured. Message: " + ex.getMessage());
            return new Report[]{new Report("AN", "ERROR", "OCCURED")};
        } finally {
            if(ps != null) {
                try {
                    ps.close();
                } catch (SQLException ignored) {}
            }
        }
        return (Report[]) reports.toArray();
    }

    @Note("Clears the Waiting Queue and adds all Entries to the History")
    public void clearWaitingQueueAndAddToHistory() {
        try {
            PreparedStatement ps = Database.getConnection().prepareStatement("INSERT INTO `" + prefix + "history` SELECT * FROM `" + prefix + "waiting`;");
            ps.executeUpdate();
            ps.close();
        } catch (SQLException ex) {
            Main.getInstance().getLogger().log(Level.SEVERE, "[UltimateReport] An error in MySQL occured. Message: " + ex.getMessage());
        }
        try {
            Statement s = Database.getConnection().createStatement();
            s.executeUpdate("DELETE * FROM `" + prefix + "waiting`;");
            s.close();
        } catch (SQLException ex) {
            Main.getInstance().getLogger().log(Level.SEVERE, "[UltimateReport] An error in MySQL occured. Message: " + ex.getMessage());
        }
    }

    // ---------- HISTORY ---------- \\

    @Note("Gets all Entries in the History Table")
    public Report[] getHistoryEntries() {
        List<Report> reports = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs;
        try {
            ps = Database.getConnection().prepareStatement("SELECT `victim`, `reporter`, `reason` FROM `" + prefix + "history`;");
            rs = ps.executeQuery();
            while((rs != null) && (rs.next())) {
                reports.add(new Report(Cache.convertToUsername(UUID.fromString(rs.getString("reporter"))), Cache.convertToUsername(UUID.fromString(rs.getString("victim"))), rs.getString("reason")));
            }
        } catch (SQLException ex) {
            Main.getInstance().getLogger().log(Level.SEVERE, "[UltimateReport] An error in MySQL occured. Message: " + ex.getMessage());
            return new Report[]{new Report("AN", "ERROR", "OCCURED")};
        } finally {
            if(ps != null) {
                try {
                    ps.close();
                } catch (SQLException ignored) {}
            }
        }
        return (Report[]) reports.toArray();
    }

    @Note("Adds a entry")
    public void addToHistory(Report r) {
        try {
            Statement s = Database.getConnection().createStatement();
            s.executeUpdate("INSERT INTO `" + prefix + "history` (`victim`, `reporter`, `reason`) VALUES ('" + Cache.convertToUUID(r.getVictim()) + "', '" + Cache.convertToUUID(r.getReporter()) + "', '" + r.getReason() + "');");
            s.close();
        } catch (SQLException ex) {
            Main.getInstance().getLogger().log(Level.SEVERE, "[UltimateReport] An error in MySQL occured. Message: " + ex.getMessage());
        }
    }

    @Note("Clears the History Table")
    public void clearHistory() {
        try {
            Statement s = Database.getConnection().createStatement();
            s.executeUpdate("DELETE * FROM `" + prefix + "history`;");
            s.close();
        } catch (SQLException ex) {
            Main.getInstance().getLogger().log(Level.SEVERE, "[UltimateReport] An error in MySQL occured. Message: " + ex.getMessage());
        }
    }

    public static DataStorage getInstance() {
        if(instance == null) {
            instance = new DataStorage();
        }
        return instance;
    }

}
