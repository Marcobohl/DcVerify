package net.celestiacraft.dcverify.mysql;

import com.zaxxer.hikari.HikariDataSource;
import net.celestiacraft.dcverify.DcVerify;

import java.sql.Connection;
import java.sql.SQLException;

public class Mysql {

    private  HikariDataSource hikari;

    String address = DcVerify.config.getString("databaseip");
    String name = DcVerify.config.getString("databasename");
    String username = DcVerify.config.getString("username");
    String password = DcVerify.config.getString("password");


    public void connectToDatabase() {

        hikari = new HikariDataSource();

        hikari.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikari.setMaximumPoolSize(10);
        hikari.setJdbcUrl("jdbc:mysql://"+ address +"/" + name);
        hikari.setPassword(password);
        hikari.setUsername(username);
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
