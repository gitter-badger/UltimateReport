package cc.acquized.ultimatereport.framework;

import cc.acquized.ultimatereport.Main;
import cc.acquized.ultimatereport.file.Config;
import cc.acquized.ultimatereport.file.Messages;
import cc.acquized.ultimatereport.sql.DataStorage;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import javax.xml.crypto.Data;
import java.util.logging.Level;

@SuppressWarnings("deprecation")
public class Utils {

    public static Report createReport(ProxiedPlayer victim, ProxiedPlayer reporter, String reason) {
        return new Report(reporter.getName(), victim.getName(), reason);
    }

    public static void proceedReport(Report r) {
        boolean staffOnline = false;
        for(ProxiedPlayer o : ProxyServer.getInstance().getPlayers()) {
            if(o.hasPermission("ultimatereport.reports.receive")) {
                staffOnline = true;
            }
        }
        if(staffOnline) {
            DataStorage.getInstance().addToHistory(r);
            ProxiedPlayer reporter = ProxyServer.getInstance().getPlayer(r.getReporter());
            if(reporter != null) {
                reporter.sendMessage(Messages.getMessage("General.Report.Reported"));
            }
            Title title = ProxyServer.getInstance().createTitle();
            String[] msg = Messages.getBundle().getString("General.Report.Staff.Title").split("%nl%");
            title.title(new TextComponent(color(msg[0])));
            if(msg.length == 2) {
                title.subTitle(new TextComponent(color(msg[1])));
            }
            title.fadeIn(Config.getConfig().getInt("Management.TitleFadeIn"));
            title.stay(Config.getConfig().getInt("Management.TitleStay"));
            title.fadeOut(Config.getConfig().getInt("Management.TitleFadeOut"));
            for(ProxiedPlayer o : ProxyServer.getInstance().getPlayers()) {
                if(o.hasPermission("ultimatereport.reports.receive")) {
                    if(Config.getConfig().getString("Management.Mode").equals("CHAT")) {
                        o.sendMessage(Messages.getMessage("General.Report.Staff.Chat").replaceAll("%victim%", r.getVictim()).replaceAll("%reporter%", r.getReporter())
                                .replaceAll("%reason%", r.getReason()).replaceAll("%server%", ProxyServer.getInstance().getPlayer(r.getVictim()) != null ?
                                        ProxyServer.getInstance().getPlayer(r.getVictim()).getServer().getInfo().getName() : Messages.getMessage("Info.Server.Unknown")));
                    } else if(Config.getConfig().getString("Management.Mode").equals("TITLE")) {
                        title.send(o);
                    } else {
                        Main.getInstance().getLogger().log(Level.SEVERE, "[UltimateReport] An error occured because you have set a invalid Option in config.yml. " +
                                "Reason: 'Unknown Value in Management.Mode: " + Config.getConfig().getString("Management.Mode") + "'.");
                    }
                }
            }
        } else {
            DataStorage.getInstance().addToWaitingQueue(r);
            ProxiedPlayer reporter = ProxyServer.getInstance().getPlayer(r.getReporter());
            if(reporter != null) {
                reporter.sendMessage(Messages.getMessage("General.Report.NoStaff"));
            }
        }
    }

    public static void displayReminder(ProxiedPlayer p) {
        Report[] reports = DataStorage.getInstance().getWaitingQueueEntries();
        if(reports.length >= 1) {
            p.sendMessage(Messages.getMessage("Info.Staff.Join.NewReports").replaceAll("%count%", String.valueOf(reports.length)));
        } else {
            p.sendMessage(Messages.getMessage("Info.Staff.Join.NoNewReports"));
        }
    }

    public static void displayWaitingReports(ProxiedPlayer p) {
        Report[] reports = DataStorage.getInstance().getWaitingQueueEntries();
        p.sendMessage(Messages.getMessage("General.Report.Waiting.Header").replaceAll("%count%", String.valueOf(reports.length)));
        for(Report r : reports) {
            p.sendMessage(Messages.getMessage("General.Report.Waiting.Display").replaceAll("%victim%", r.getVictim()).replaceAll("%reporter%", r.getReporter())
            .replaceAll("%reason%", r.getReason()));
        }
        p.sendMessage(Messages.getMessage("General.Report.Waiting.Footer"));
    }

    public static void clearWaitingReports(ProxiedPlayer p) {
        DataStorage.getInstance().clearWaitingQueueAndAddToHistory();
        p.sendMessage(Messages.getMessage("General.Report.Waiting.Moved"));
    }

    public static void displayReportHistory(ProxiedPlayer p) {
        Report[] reports = DataStorage.getInstance().getHistoryEntries();
        p.sendMessage(Messages.getMessage("General.Report.History.Header").replaceAll("%count%", String.valueOf(reports.length)));
        for(Report r : reports) {
            p.sendMessage(Messages.getMessage("General.Report.History.Display").replaceAll("%victim%", r.getVictim()).replaceAll("%reporter%", r.getReporter())
                    .replaceAll("%reason%", r.getReason()));
        }
        p.sendMessage(Messages.getMessage("General.Report.History.Footer"));
    }

    public static void showLatest5Reports(ProxiedPlayer p) {
        int count = -1;
        Report[] reports = DataStorage.getInstance().getHistoryEntries();
        p.sendMessage(Messages.getMessage("General.Report.Last5.Header"));
        for(Report r : reports) {
            if(count < 5) {
                p.sendMessage(Messages.getMessage("General.Report.Last5.Display").replaceAll("%victim%", r.getVictim()).replaceAll("%reporter%", r.getReporter())
                        .replaceAll("%reason%", r.getReason()));
                count++;
            }
        }
        p.sendMessage(Messages.getMessage("General.Report.Last5.Footer"));
    }

    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

}
