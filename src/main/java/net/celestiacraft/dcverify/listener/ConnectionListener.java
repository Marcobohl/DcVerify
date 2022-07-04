package net.celestiacraft.dcverify.listener;

import net.celestiacraft.dcverify.DcVerify;
import net.celestiacraft.dcverify.mysql.Mysql;
import net.celestiacraft.dcverify.mysql.Sqlgetter;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.awt.*;

public class ConnectionListener implements Listener {

    public static Sqlgetter data;
    public static Mysql mysql;

    public ConnectionListener(DcVerify plugin) {;
        this.data = new Sqlgetter(plugin);
    }


    @EventHandler
    public void onPostLogin(PostLoginEvent event) {

        ProxiedPlayer player = event.getPlayer();

        data.createPlayer(player);

    }
}
