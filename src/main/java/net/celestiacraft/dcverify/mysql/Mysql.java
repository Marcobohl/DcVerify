package net.celestiacraft.dcverify.mysql;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class Mysql {

    private  HikariDataSource hikari;

    String address = "127.0.0.1";
    String name = "dcverify";
    String username = "Celestiacraftmcserver";
    String password = "g8Rn5MCf5WHQTys";


    public void connectToDatabase() {

        hikari = new HikariDataSource();

        hikari.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikari.setMaximumPoolSize(10);
        hikari.setJdbcUrl("jdbc:mysql://localhost/dcverify");
        hikari.setPassword("g8Rn5MCf5WHQTys");
        hikari.setUsername("Celestiacraftmcserver");
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
