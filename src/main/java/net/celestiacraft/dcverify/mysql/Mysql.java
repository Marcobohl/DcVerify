package net.celestiacraft.dcverify.mysql;

import com.zaxxer.hikari.HikariDataSource;
import net.celestiacraft.dcverify.DcVerify;

public class Mysql {

    private  HikariDataSource hikari;
    private final DcVerify plugin;

    public Mysql(DcVerify plugin) {
        this.plugin = plugin;
    }


    public void connectToDatabase() {

        hikari = new HikariDataSource();

        hikari.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikari.setMaximumPoolSize(10);
        hikari.setJdbcUrl("jdbc:mysql://"+ plugin.config.getString("databaseip") +"/" + plugin.config.getString("databasename"));
        hikari.setPassword(plugin.config.getString("password"));
        hikari.setUsername(plugin.config.getString("username"));
        hikari.addDataSourceProperty("cachePrepStmts", "true");
        hikari.addDataSourceProperty("prepStmtCacheSize", "250");
        hikari.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

    }

    public HikariDataSource getHikari() {
        return hikari;
    }

    public void disconect() {
        hikari.close();
    }
}
