package net.celestiacraft.dcverify.mysql;

import net.celestiacraft.dcverify.DcVerify;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

public class Sqlgetter {

    private DcVerify plugin;

    public Sqlgetter(DcVerify plugin) {
        this.plugin = plugin;
    }


    public void createPlayer(ProxiedPlayer player) {

        Random randomGenerator = new Random();

        int randInt = randomGenerator.nextInt(900000) + 100000;
        int ergebnischeck = checkrand(randInt);

        while (ergebnischeck != 0) {
            randInt = randomGenerator.nextInt(900000) + 100000;
            ergebnischeck = checkrand(randInt);
        }

        try {
            UUID uuid = player.getUniqueId();
            if (!exists(uuid)) {
                PreparedStatement ps = plugin.MYSQL.getHikari().getConnection().prepareStatement("INSERT IGNORE INTO playerdata" + " (DCID,MCUUID,VERIFYID,VERIFYCHECK,DCNAME) VALUES (?,?,?,?,?)");
                ps.setString(1, null);
                ps.setString(2, uuid.toString());
                ps.setString(3, String.valueOf(randInt));
                ps.setBoolean(4, false);
                ps.setString(5, null);
                ps.executeUpdate();
                ps.close();
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(UUID uuid) {
        try {
            PreparedStatement ps = plugin.MYSQL.getHikari().getConnection().prepareStatement("SELECT * FROM playerdata WHERE MCUUID=?");
            ps.setString(1, uuid.toString());

            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return true;
            }
            ps.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Checks if id exits
     * @param {int} random - id
     * @return
     */
    public int checkrand(int random) {
        int randomid = 0;
        try {

            PreparedStatement ps = plugin.MYSQL.getHikari().getConnection().prepareStatement("SELECT `VERIFYID` FROM playerdata WHERE VERIFYID = " + random);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                randomid = result.getInt(1);
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return randomid;

    }

    public String selectverifycode(UUID uuid) {
        String fcode = "";
        try {

            PreparedStatement ps = plugin.MYSQL.getHikari().getConnection().prepareStatement("SELECT `VERIFYID` from playerdata WHERE MCUUID=?");
            ps.setString(1, ( uuid.toString() ));
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                fcode = result.getString("VERIFYID");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fcode;
    }

    public boolean checkverify(UUID uuid) {
        boolean effect = false;
        try {

            PreparedStatement ps = plugin.MYSQL.getHikari().getConnection().prepareStatement("SELECT `VERIFYCHECK` from playerdata WHERE MCUUID=?");
            ps.setString(1, ( uuid.toString() ));
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                effect = result.getBoolean("VERIFYCHECK");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return effect;
    }

    public String selectverifydiscoruser(UUID uuid) {
        String dcname = "";
        try {

            PreparedStatement ps = plugin.MYSQL.getHikari().getConnection().prepareStatement("SELECT `DCNAME` from playerdata WHERE MCUUID=?");
            ps.setString(1, ( uuid.toString() ));
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                dcname = result.getString("DCNAME");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dcname;
    }

    public void removeverify(UUID uuid) {
        try {
            PreparedStatement ps = plugin.MYSQL.getHikari().getConnection().prepareStatement("UPDATE playerdata SET DCNAME=?,VERIFYCHECK=?, DCID=? WHERE MCUUID=?");
            ps.setString(1, null);
            ps.setBoolean(2, false);
            ps.setString(3, null);
            ps.setString(4, uuid.toString());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void accepterify(UUID uuid) {
        try {
            PreparedStatement ps = plugin.MYSQL.getHikari().getConnection().prepareStatement("UPDATE playerdata SET VERIFYCHECK=? WHERE MCUUID=?");
            ps.setBoolean(1, true);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
