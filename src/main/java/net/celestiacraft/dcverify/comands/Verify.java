package net.celestiacraft.dcverify.comands;

import net.celestiacraft.dcverify.DcVerify;
import net.celestiacraft.dcverify.mysql.Mysql;
import net.celestiacraft.dcverify.mysql.Sqlgetter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class Verify extends Command implements TabExecutor {

    public static Sqlgetter data;
    public static Mysql mysql;
    public static HashMap<ProxiedPlayer, Boolean> confimcommand = new HashMap<>();

    public Verify(DcVerify plugin) {
        super("verify");
        this.data = new Sqlgetter(plugin);
    }
    boolean fistaccept = true;

    @Override
    public void execute(CommandSender sender, String[] args) {


        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent("§6[§3Celestiacraft§6]§7 Du Musst ein Spieler Sein."));
            return;
        }

        if (args.length == 0) {

            String verifycode = data.selectverifycode(((ProxiedPlayer) sender).getUniqueId());

            sender.sendMessage(new TextComponent("§6[§3Celestiacraft§6]§7 Dein Verify Code Lautet: §a" + verifycode + "§7."));

        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("check")) {
                boolean verifycode = data.checkverify(((ProxiedPlayer) sender).getUniqueId());
                if (!verifycode) {
                    sender.sendMessage(new TextComponent("§6[§3Celestiacraft§6]§7 Aktuell bist du mit §akeinem Discored User§7 Verknüpft."));
                } else {
                    String Verifydcname = data.selectverifydiscoruser(((ProxiedPlayer) sender).getUniqueId());
                    sender.sendMessage(new TextComponent("§6[§3Celestiacraft§6]§7 Dein Verknüpfter Discord Account Lautet:§a " + Verifydcname + "§7."));
                }
            } else if (args[0].equalsIgnoreCase("delete")) {
                boolean verifycode = data.checkverify(((ProxiedPlayer) sender).getUniqueId());
                if (!verifycode) {
                    sender.sendMessage(new TextComponent("§6[§3Celestiacraft§6]§7 Die Verknüpfung konnte nicht gelöscht werden da du mit §akeinem Discored User§7 Verknüpft bist."));
                } else {
                    String Verifydcname = data.selectverifydiscoruser(((ProxiedPlayer) sender).getUniqueId());
                    data.removeverify(((ProxiedPlayer) sender).getUniqueId());
                    sender.sendMessage(new TextComponent("§6[§3Celestiacraft§6]§7 Deine Verknüpfung mit:§a " + Verifydcname + "§7 wurde gelöscht."));
                }
            } else if (args[0].equalsIgnoreCase("accept")) {
                boolean verifycode = data.checkverify(((ProxiedPlayer) sender).getUniqueId());
                String Verifydcname = data.selectverifydiscoruser(((ProxiedPlayer) sender).getUniqueId());
                if (!verifycode && Verifydcname == null) {
                    sender.sendMessage(new TextComponent("§6[§3Celestiacraft§6]§7 Aktuell kannst du §akeine Verknüpfung bestätigen§7 da du keine anfrage hast."));
                } else if (verifycode && Verifydcname != null)  {
                    sender.sendMessage(new TextComponent("§6[§3Celestiacraft§6]§7 Aktuell bist du mit dem User:§a " + Verifydcname + "§7 Verknüpft du kannst die Verknüpfung jederzeit mit §a/Verify delete §7löschen."));
                } else {

                    if (confimcommand.get(sender) == null) {
                        sender.sendMessage(new TextComponent("§6[§3Celestiacraft§6]§7 Gebe §a/Verify Accept§7 erneut ein um die Verknüpfung mit: §a" + Verifydcname + "§7 zu bestätigt."));
                        confimcommand.put((ProxiedPlayer) sender, true);
                    } else {
                        data.accepterify(((ProxiedPlayer) sender).getUniqueId());
                        sender.sendMessage(new TextComponent("§6[§3Celestiacraft§6]§7 Deine Verknüpfung mit: §a" + Verifydcname + "§7 wurde hirmit bestätigt."));
                        confimcommand.remove(sender);
                    }
                }
            }


            else {
                sender.sendMessage(new TextComponent("§6[§3Celestiacraft§6]§7 Bitte gebe §a/Verify §7<§aCheck§7/§aDelete§7/§aAccept§7> ein."));
            }
        } else {
            sender.sendMessage(new TextComponent("§6[§3Celestiacraft§6]§7 Bitte gebe §a/Verify §7<§aCheck§7/§aDelete§7/§aAccept§7> ein."));
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            List<String> servers = new ArrayList<>(Arrays.asList("accept", "check", "delete"));
            return servers;
        } else {
            return new ArrayList<>();
        }
    }
}
