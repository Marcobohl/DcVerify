package net.celestiacraft.dcverify;

import com.zaxxer.hikari.HikariDataSource;
import net.celestiacraft.dcverify.comands.Verify;
import net.celestiacraft.dcverify.listener.ConnectionListener;
import net.celestiacraft.dcverify.mysql.Mysql;
import net.celestiacraft.dcverify.mysql.Sqlgetter;
import net.md_5.bungee.api.plugin.Plugin;

public final class DcVerify extends Plugin {

    private static DcVerify main;
    public static Mysql MYSQL;
    public static Sqlgetter data;

    private static HikariDataSource hikari;

    @Override
    public void onEnable() {

        main = this;

        this.MYSQL = new Mysql();
        this.data = new Sqlgetter(this);

        MYSQL.connectToDatabase();

        getProxy().getPluginManager().registerCommand(this, new Verify(this));
        getProxy().getPluginManager().registerListener(this, new ConnectionListener(this));
    }

    @Override
    public void onDisable() {
        MYSQL.disconect();
    }
}
