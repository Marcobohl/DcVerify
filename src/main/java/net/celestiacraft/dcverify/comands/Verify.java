package net.celestiacraft.dcverify.comands;

import net.celestiacraft.dcverify.DcVerify;
import net.celestiacraft.dcverify.mysql.Sqlgetter;
import net.md_5.bungee.api.ChatColor;
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
    public static HashMap<ProxiedPlayer, Boolean> confimcommand = new HashMap<>();

    public Verify(DcVerify plugin) {
        super("verify");
        this.data = new Sqlgetter(plugin);
    }

    public static String verifycodemessage(String message, String placeholderreplace)
    {
        message = DcVerify.config.getString(message).replaceAll("%verifycode%", placeholderreplace);
        if (DcVerify.config.getBoolean("verify_prefix_use")) {
            message = ChatColor.translateAlternateColorCodes('&',DcVerify.config.getString("verify_prefix") + " " +  message);
        } else {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }
        return message;
    }

    public static String verifydcnameemessage(String message, String placeholderreplace)
    {
        message = DcVerify.config.getString(message).replaceAll("%verifydcname%", placeholderreplace);
        if (DcVerify.config.getBoolean("verify_prefix_use")) {
            message = ChatColor.translateAlternateColorCodes('&', DcVerify.config.getString("verify_prefix") + " " + message);
        } else {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }
        return message;

    }

    public static String message(String message) {
        message = DcVerify.config.getString(message);
        if (DcVerify.config.getBoolean("verify_prefix_use")) {
            message = ChatColor.translateAlternateColorCodes('&', DcVerify.config.getString("verify_prefix") + " " + message);
        } else {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }
        return message;
    }

        @Override
    public void execute(CommandSender sender, String[] args) {

        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent(message("verify_connsole_message")));
            return;
        }

        if (args.length == 0) {

            String verifycode = data.selectverifycode(((ProxiedPlayer) sender).getUniqueId());

            sender.sendMessage(new TextComponent(verifycodemessage("verify_code_message",verifycode)));

        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("check")) {
                boolean verifycode = data.checkverify(((ProxiedPlayer) sender).getUniqueId());
                if (!verifycode) {
                    sender.sendMessage(new TextComponent(message("verify_check_noverifykation")));
                } else {
                    String Verifydcname = data.selectverifydiscoruser(((ProxiedPlayer) sender).getUniqueId());
                    sender.sendMessage(new TextComponent(verifydcnameemessage("verify_check_verifycode", Verifydcname)));
                }
            } else if (args[0].equalsIgnoreCase("delete")) {
                boolean verifycode = data.checkverify(((ProxiedPlayer) sender).getUniqueId());
                if (!verifycode) {
                    sender.sendMessage(new TextComponent(message("verify_delete_nodelete")));
                } else {
                    String Verifydcname = data.selectverifydiscoruser(((ProxiedPlayer) sender).getUniqueId());
                    data.removeverify(((ProxiedPlayer) sender).getUniqueId());
                    sender.sendMessage(new TextComponent(verifydcnameemessage("verify_delete_verifykation", Verifydcname)));
                }
            } else if (args[0].equalsIgnoreCase("accept")) {
                boolean verifycode = data.checkverify(((ProxiedPlayer) sender).getUniqueId());
                String Verifydcname = data.selectverifydiscoruser(((ProxiedPlayer) sender).getUniqueId());
                if (!verifycode && Verifydcname == null) {
                    sender.sendMessage(new TextComponent(message("verify_accept_noaccept")));
                } else if (verifycode && Verifydcname != null)  {
                    sender.sendMessage(new TextComponent(verifydcnameemessage("verify_accept_existing",Verifydcname )));
                } else {

                    if (confimcommand.get(sender) == null) {
                        sender.sendMessage(new TextComponent(verifydcnameemessage("verify_accept_accepting", Verifydcname)));
                        confimcommand.put((ProxiedPlayer) sender, true);
                    } else {
                        data.accepterify(((ProxiedPlayer) sender).getUniqueId());
                        sender.sendMessage(new TextComponent(verifydcnameemessage("verify_accept_successful", Verifydcname)));
                        confimcommand.remove(sender);
                    }
                }
            }

            else {
                sender.sendMessage(new TextComponent(message("verify_help")));
            }
        } else {
            sender.sendMessage(new TextComponent(message("verify_help")));
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
