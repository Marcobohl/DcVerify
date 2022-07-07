package net.celestiacraft.dcverify;

import com.zaxxer.hikari.HikariDataSource;
import net.celestiacraft.dcverify.comands.Verify;
import net.celestiacraft.dcverify.listener.ConnectionListener;
import net.celestiacraft.dcverify.mysql.Mysql;
import net.celestiacraft.dcverify.mysql.Sqlgetter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

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
    public static Configuration config;


    @Override
    public void onEnable() {

        main = this;

        this.data = new Sqlgetter(this);
        this.config = loadConfig("config.yml");

        this.MYSQL = new Mysql();

        MYSQL.connectToDatabase();


        getProxy().getPluginManager().registerCommand(this, new Verify(this));
        getProxy().getPluginManager().registerListener(this, new ConnectionListener(this));
    }

    private Configuration loadConfig(String name) {
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        File file = new File(getDataFolder(), name);
        if (!file.exists())
            try (InputStream in = getResourceAsStream(name)) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        try {
            Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
            return config;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

            @Override
    public void onDisable() {

        MYSQL.disconect();
    }
}
