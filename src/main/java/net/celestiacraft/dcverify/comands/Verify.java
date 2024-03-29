package net.celestiacraft.dcverify.comands;

import net.celestiacraft.dcverify.DcVerify;
import net.celestiacraft.dcverify.mysql.Sqlgetter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Verify extends Command implements TabExecutor {

    private final Sqlgetter data;

    private final DcVerify plugin;

    public static HashMap<ProxiedPlayer, Boolean> confimcommand = new HashMap<>();

    public Verify(DcVerify plugin) {
        super("verify");
        this.data = new Sqlgetter(plugin);
        this.plugin = plugin;
    }

    public String verifycodemessage(String message, String placeholderreplace) {
        message = plugin.config.getString(message).replaceAll("%verifycode%", placeholderreplace);
        if (plugin.config.getBoolean("verify_prefix_use")) {
            message = ChatColor.translateAlternateColorCodes('&', plugin.config.getString("verify_prefix") + " " + message);
        } else {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }
        return message;
    }

    public String verifydcnameemessage(String message, String placeholderreplace) {
        message = plugin.config.getString(message).replaceAll("%verifydcname%", placeholderreplace);
        if (plugin.config.getBoolean("verify_prefix_use")) {
            message = ChatColor.translateAlternateColorCodes('&', plugin.config.getString("verify_prefix") + " " + message);
        } else {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }
        return message;

    }

    public String verifydeletmessage(String message, String placeholderreplace) {
        message = plugin.config.getString(message).replaceAll("%verifyplayer%", placeholderreplace);
        if (plugin.config.getBoolean("verify_prefix_use")) {
            message = ChatColor.translateAlternateColorCodes('&', plugin.config.getString("verify_prefix") + " " + message);
        } else {
            message = ChatColor.translateAlternateColorCodes('&', message);
        }
        return message;

    }

    public String message(String message) {
        message = plugin.config.getString(message);
        if (plugin.config.getBoolean("verify_prefix_use")) {
            message = ChatColor.translateAlternateColorCodes('&', plugin.config.getString("verify_prefix") + " " + message);
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

            if (sender.hasPermission("dcverify.user.code") || sender.hasPermission("dcverify.user.*")) {

                String verifycode = data.selectverifycode(((ProxiedPlayer) sender).getUniqueId());

                sender.sendMessage(new TextComponent(verifycodemessage("verify_code_message", verifycode)));

            } else {
                sender.sendMessage(new TextComponent(message("verify_nopermissions")));
            }

        } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase("check")) {

                if (sender.hasPermission("dcverify.user.check") || sender.hasPermission("dcverify.user.*")) {

                    boolean verifycode = data.checkverify(((ProxiedPlayer) sender).getUniqueId());
                    if (!verifycode) {
                        sender.sendMessage(new TextComponent(message("verify_check_noverifykation")));
                    } else {
                        String Verifydcname = data.selectverifydiscoruser(((ProxiedPlayer) sender).getUniqueId());
                        sender.sendMessage(new TextComponent(verifydcnameemessage("verify_check_verifycode", Verifydcname)));
                    }

                } else {
                    sender.sendMessage(new TextComponent(message("verify_nopermissions")));
                }

            } else if (args[0].equalsIgnoreCase("delete")) {

                if (sender.hasPermission("dcverify.user.delete") || sender.hasPermission("dcverify.user.*")) {

                    boolean verifycode = data.checkverify(((ProxiedPlayer) sender).getUniqueId());
                    if (!verifycode) {
                        sender.sendMessage(new TextComponent(message("verify_delete_nodelete")));
                    } else {
                        String Verifydcname = data.selectverifydiscoruser(((ProxiedPlayer) sender).getUniqueId());
                        data.removeverify(((ProxiedPlayer) sender).getUniqueId());
                        sender.sendMessage(new TextComponent(verifydcnameemessage("verify_delete_verifykation", Verifydcname)));
                    }

                } else {
                    sender.sendMessage(new TextComponent(message("verify_nopermissions")));
                }

            } else if (args[0].equalsIgnoreCase("accept")) {

                if (sender.hasPermission("dcverify.user.accept") || sender.hasPermission("dcverify.user.*")) {

                    boolean verifycode = data.checkverify(((ProxiedPlayer) sender).getUniqueId());
                    String Verifydcname = data.selectverifydiscoruser(((ProxiedPlayer) sender).getUniqueId());
                    if (!verifycode && Verifydcname == null) {
                        sender.sendMessage(new TextComponent(message("verify_accept_noaccept")));
                    } else if (verifycode && Verifydcname != null) {
                        sender.sendMessage(new TextComponent(verifydcnameemessage("verify_accept_existing", Verifydcname)));
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

                } else {
                    sender.sendMessage(new TextComponent(message("verify_nopermissions")));
                }
            } else if (args[0].equalsIgnoreCase("reload")) {
                if (sender.hasPermission("dcverify.admin.reload")) {

                    if (plugin.reload()) {
                        sender.sendMessage(new TextComponent(message("verify_reload")));
                    } else {
                        sender.sendMessage(new TextComponent(message("verify_reload_error")));
                    }
                } else {
                    sender.sendMessage(new TextComponent(message("verify_nopermissions")));
                }
            } else {
                sender.sendMessage(new TextComponent(message("verify_help")));
            }
        } else if (args.length == 2) {

            if (args[1].equalsIgnoreCase("delete")) {
                if (sender.hasPermission("dcverify.admin.delete")) {


                } else {
                    sender.sendMessage(new TextComponent(message("verify_nopermissions")));
                }

            } else {
                sender.sendMessage(new TextComponent(message("verify_help")));
            }

        } else {
            sender.sendMessage(new TextComponent(message("verify_help")));
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] args) {
        if (args.length == 1) {
            List<String> servers;
            if (commandSender.hasPermission("dcverify.admin.reload")) {
                servers = List.of("accept", "check", "delete", "reload");
            } else {
                servers = List.of("accept", "check", "delete");
            }
            return servers;
        } else {
            return new ArrayList<>();
        }
    }
}
