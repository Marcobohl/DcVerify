package net.celestiacraft.dcverify.mysql;

import net.celestiacraft.dcverify.DcVerify;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

public class Sqlgetter {

    private final DcVerify plugin;
    Connection connection = null;
    PreparedStatement ps = null;

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
            connection = DcVerify.MYSQL.getHikari().getConnection();

            UUID uuid = player.getUniqueId();
            if (!exists(uuid)) {
                ps = connection.prepareStatement("INSERT IGNORE INTO playerdata" + " (DCID,MCUUID,VERIFYID,VERIFYCHECK,DCNAME) VALUES (?,?,?,?,?)");
                ps.setString(1, null);
                ps.setString(2, uuid.toString());
                ps.setString(3, String.valueOf(randInt));
                ps.setBoolean(4, false);
                ps.setString(5, null);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeconnection(connection, ps);
        }
    }

    public boolean exists(UUID uuid) {
        try {
            connection = DcVerify.MYSQL.getHikari().getConnection();

            ps = connection.prepareStatement("SELECT * FROM playerdata WHERE MCUUID=?");
            ps.setString(1, uuid.toString());

            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return true;
            }
            ps.close();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeconnection(connection, ps);
        }
        return false;
    }
    
    public int checkrand(int random) {
        int randomid = 0;
        try {
            connection = DcVerify.MYSQL.getHikari().getConnection();

            ps = connection.prepareStatement("SELECT `VERIFYID` FROM playerdata WHERE VERIFYID = " + random);
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                randomid = result.getInt(1);
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeconnection(connection, ps);
        }
        return randomid;

    }

    public String selectverifycode(UUID uuid) {
        String fcode = "";
        try {
            connection = DcVerify.MYSQL.getHikari().getConnection();

            ps = connection.prepareStatement("SELECT `VERIFYID` from playerdata WHERE MCUUID=?");
            ps.setString(1, ( uuid.toString() ));
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                fcode = result.getString("VERIFYID");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeconnection(connection, ps);
        }
        return fcode;
    }

    public boolean checkverify(UUID uuid) {
        boolean effect = false;
        try {
            connection = plugin.MYSQL.getHikari().getConnection();

            ps = connection.prepareStatement("SELECT `VERIFYCHECK` from playerdata WHERE MCUUID=?");
            ps.setString(1, ( uuid.toString() ));
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                effect = result.getBoolean("VERIFYCHECK");
            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeconnection(connection, ps);
        }
        return effect;
    }

    public String selectverifydiscoruser(UUID uuid) {
        String dcname = "";
        try {
            connection = DcVerify.MYSQL.getHikari().getConnection();

            ps = connection.prepareStatement("SELECT `DCNAME` from playerdata WHERE MCUUID=?");
            ps.setString(1, ( uuid.toString() ));
            ResultSet result = ps.executeQuery();
            while (result.next()) {
                dcname = result.getString("DCNAME");

            }
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeconnection(connection, ps);
        }
        return dcname;
    }

    public void removeverify(UUID uuid) {
        try {
            connection = DcVerify.MYSQL.getHikari().getConnection();

            ps = connection.prepareStatement("UPDATE playerdata SET DCNAME=?,VERIFYCHECK=?, DCID=? WHERE MCUUID=?");
            ps.setString(1, null);
            ps.setBoolean(2, false);
            ps.setString(3, null);
            ps.setString(4, uuid.toString());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeconnection(connection, ps);
        }
    }

    public void accepterify(UUID uuid) {
        try {
            connection = DcVerify.MYSQL.getHikari().getConnection();

            ps = connection.prepareStatement("UPDATE playerdata SET VERIFYCHECK=? WHERE MCUUID=?");
            ps.setBoolean(1, true);
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeconnection(connection, ps);
        }
    }

    public void closeconnection(Connection connection, PreparedStatement ps) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
