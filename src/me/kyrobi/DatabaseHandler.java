package me.kyrobi;

import java.io.File;
import java.sql.*;
import java.util.Objects;

public class DatabaseHandler {


    File dbFile = new File("");
    public File folderDirectory = new File(dbFile.getAbsolutePath() + File.separator + "plugins" + File.separator + "JoinTime");
    String url = "jdbc:sqlite:" + folderDirectory + File.separator +"storage.db";

    //Creates a new database if not exist
    public void createNewDatabase(){

        System.out.println("VALUE FOR DIRECTORY: " + url);
        String command = "CREATE TABLE IF NOT EXISTS users ("
                + " 'uuid' VARCHAR(255) PRIMARY KEY,"
                + " 'time' INTEGER NOT NULL DEFAULT 0)";

        try(Connection conn = DriverManager.getConnection(url)){
            Class.forName("org.sqlite.JDBC");
            Statement stmt = conn.createStatement(); // Formulate the command to execute
            stmt.execute(command);  //Execute said command
        }
        catch (SQLException error){
            System.out.println(error.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Checks if a key already exists in the database.
    public boolean ifExist(String uuid){
        String selectfrom = "SELECT EXISTS(SELECT 1 FROM users WHERE uuid=?);";

        try(Connection conn = DriverManager.getConnection(url)){
            Class.forName("org.sqlite.JDBC");
            PreparedStatement stmt = conn.prepareStatement(selectfrom);

            stmt.setString(1, uuid);

            ResultSet rs = stmt.executeQuery(); // Execute the command

            int value = rs.getInt(1);
            if(value == 1) return true;
            else return false;
        }
        catch(SQLException e){
            e.printStackTrace();
            System.out.println("Error code: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Inserting values into the database
    public void insert(String uuid, long time){
        String command = "INSERT INTO users(uuid, time) VALUES(?,?)";

        try(Connection conn = DriverManager.getConnection(url)){
            System.out.println("Inserting");
            Class.forName("org.sqlite.JDBC");
            PreparedStatement stmt = conn.prepareStatement(command);
            stmt.setString(1, uuid); // The first column will contain the ID
            stmt.setLong(2, time); // The second column will contain the amount
            stmt.executeUpdate();
            conn.close();
        }
        catch(SQLException error){
            System.out.println(error.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    //Fetches the time from the database
    public long getTime(String uuid){
        //String command = "SELECT * FROM users WHERE uuid=" + uuid;
        String command = "SELECT * FROM users WHERE uuid= ?";
        long time = 0;

        try (Connection conn = DriverManager.getConnection(url)){
            Class.forName("org.sqlite.JDBC");
            PreparedStatement getTime = conn.prepareStatement(command);

            getTime.setString(1, uuid);

            ResultSet rs = getTime.executeQuery(); // Used with prepared statement

            time = rs.getLong("time");
            rs.close();
            conn.close();
        }
        catch(SQLException se){
            System.out.println(se.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return time;
    }

    public void update(String uuid, long time){
        //String command = "SELECT * FROM users WHERE uuid=" + uuid;
        String command = "UPDATE users SET time=? WHERE uuid= ?";

        try (Connection conn = DriverManager.getConnection(url)){
            Class.forName("org.sqlite.JDBC");
            PreparedStatement updateTime = conn.prepareStatement(command);

            updateTime.setLong(1, time);
            updateTime.setString(2, uuid);

            updateTime.executeUpdate();
            conn.close();
        }
        catch(SQLException se){
            System.out.println(se.getMessage());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

}
