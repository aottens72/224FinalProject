import java.sql.*;

public class DatabaseHelper {
    static final String DATABASE_NAME_PREF = "databaseMinesweeper.db";
    static final String DATABASE_NAME_STATS = "databaseStats.db";
    static final String CONNECTION_URL_PREF = "jdbc:sqlite:databases/" + DATABASE_NAME_PREF;
    static final String CONNECTION_URL_STATS = "jdbc:sqlite:databases/" + DATABASE_NAME_STATS;

    static final String TABLE_PREFERENCES = "tablePreferences";
    static final String ID = "id";
    static final String DIMENSIONS = "dimensions";
    static final String NUM_BOMBS = "numBombs";

    static final String TABLE_STATS = "tableStats";
    static final String GAMES_PLAYED = "gamesPlayed";
    static final String GAMES_WON = "gamesWon";
    static final String WIN_PERCENTAGE = "winPercentage";
    static final String FASTEST_TIME = "fastestTime";

    Connection connection_pref;
    Connection connection_stats;

    public DatabaseHelper(){
        getConnectionPref();
        getConnectionStats();
        createPreferenceTable();
        createStatsTable();
    }

    public void getConnectionPref(){
        try{
            connection_pref = DriverManager.getConnection(CONNECTION_URL_PREF);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void closeConnectionPref(){
        if(connection_pref != null){
            try{
                connection_pref.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    public void getConnectionStats(){
        try{
            connection_stats = DriverManager.getConnection(CONNECTION_URL_STATS);
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void closeConnectionStats(){
        if(connection_stats != null){
            try {
                connection_stats.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public void createPreferenceTable(){
        String SQLCreate = "CREATE TABLE " + TABLE_PREFERENCES + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DIMENSIONS + " INTEGER, " +
                NUM_BOMBS + " INTEGER)";

        if(connection_pref != null){
            try{
                Statement statement = connection_pref.createStatement();
                statement.execute(SQLCreate);
            }catch(SQLException e){
               // e.printStackTrace();
            }
        }
    }

    public void createStatsTable(){
        String SQLCreate = "CREATE TABLE " + TABLE_STATS + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GAMES_PLAYED + " INTEGER, " +
                GAMES_WON + " INTEGER, " +
                WIN_PERCENTAGE + " DOUBLE, " +
                FASTEST_TIME + " INTEGER)";

        if(connection_stats != null) {
            try {
                Statement statement = connection_stats.createStatement();
                statement.execute(SQLCreate);
            }catch(SQLException e){
              //  e.printStackTrace();
            }
        }
    }

    public void insertPreferences(int dims, int bombs){
        String SQLInsert = "INSERT INTO " + TABLE_PREFERENCES + " VALUES(null, " +
                dims + ", " +
                bombs + ")";
        if(connection_pref != null){
            try{
                Statement statement = connection_pref.createStatement();
                statement.execute(SQLInsert);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public void insertStats(int gamesPlayed, int gamesWon, double winPercent, int fastestTime){
        String SQLInsert = "INSERT INTO " + TABLE_STATS + " VALUES(null, " +
                gamesPlayed + ", " +
                gamesWon + ", " +
                winPercent + ", " +
                fastestTime + ")";
        if(connection_pref != null){
            try{
                Statement statement = connection_pref.createStatement();
                statement.execute(SQLInsert);
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    public Preference getPreferences(){
        Preference p = new Preference();
        String sqlSelect = "SELECT * FROM " + TABLE_PREFERENCES;
        if(connection_pref != null){
            try{
                Statement statement = connection_pref.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlSelect);
                resultSet.next();
                int dim = resultSet.getInt(DIMENSIONS);
                int numBombs = resultSet.getInt(NUM_BOMBS);
                p = new Preference(dim,numBombs);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return p;
    }

    public Stats getStats(){
        Stats s = new Stats();
        String sqlSelect = "SELECT * FROM " + TABLE_STATS;
        if(connection_stats != null){
            try{
                Statement statement = connection_stats.createStatement();
                ResultSet resultSet = statement.executeQuery(sqlSelect);
                resultSet.next();
                int gamesPlayed = resultSet.getInt(GAMES_PLAYED);
                int gamesWon = resultSet.getInt(GAMES_WON);
                double winPercentage = resultSet.getDouble(WIN_PERCENTAGE);
                int fastestTime = resultSet.getInt(FASTEST_TIME);
                s = new Stats(gamesPlayed, gamesWon, winPercentage, fastestTime);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return s;
    }

    public void updatePreferences(int id, int dims, int bombs){
        String sqlUpdate = "UPDATE " + TABLE_PREFERENCES + " SET " +
                DIMENSIONS + "=" + dims + ", " +
                NUM_BOMBS + "=" + bombs +
                " WHERE " + ID + "=" + id;
        if(connection_pref != null){
            try{
                Statement statement = connection_pref.createStatement();
                statement.execute(sqlUpdate);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public void updateStats(int id, int gamesPlayed, int gamesWon, double winPercent, int fastestTime){
        String sqlUpdate = "UPDATE " + TABLE_STATS + " SET " +
                GAMES_PLAYED + "=" + gamesPlayed + ", " +
                GAMES_WON + "=" + gamesWon + ", " +
                WIN_PERCENTAGE + "=" + winPercent + ", " +
                FASTEST_TIME + "=" + fastestTime +
                " WHERE " + ID + "=" + id;
        if(connection_stats != null){
            try{
                Statement statement = connection_stats.createStatement();
                statement.execute(sqlUpdate);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public void deletePreferences(){
        String sqlDelete = "DELETE FROM " + TABLE_PREFERENCES;
        if(connection_pref != null){
            try{
                Statement statement = connection_pref.createStatement();
                statement.execute(sqlDelete);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    public void deleteStats(){
        String sqlDelete = "DELETE FROM " + TABLE_STATS;
        if(connection_stats != null){
          try {
              Statement statement = connection_stats.createStatement();
              statement.execute(sqlDelete);
          }catch (SQLException e){
              e.printStackTrace();
          }
        }
    }
}
