package net.celestiacraft.dcverify;

import com.zaxxer.hikari.HikariDataSource;
import net.celestiacraft.dcverify.comands.Verify;
import net.celestiacraft.dcverify.listener.ConnectionListener;
import net.celestiacraft.dcverify.mysql.Mysql;
import net.celestiacraft.dcverify.mysql.Sqlgetter;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public final class DcVerify extends Plugin {

    private static DcVerify main;
    public static Mysql MYSQL;
    public static Sqlgetter data;

    private static HikariDataSource hikari;

    public File file = new File(getDataFolder(), "config.yml");



    @Override
    public void onEnable() {

        if (!getDataFolder().exists()) {
            getLogger().info("Created config folder: " + getDataFolder().mkdir());
        }

        if (!file.exists()) {
            try (InputStream in = getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
