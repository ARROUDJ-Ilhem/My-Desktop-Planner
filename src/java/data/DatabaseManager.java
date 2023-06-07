package tp.javafx.data;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String JDBC_DRIVER = "org.sqlite.JDBC";
    String dbFilePath = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "database.db";

    File dbFile = new File(dbFilePath);

    private Connection connection;

    public DatabaseManager() {
        if (!dbFile.exists()) {
            try {
                // Create the file in the Documents folder
                dbFile.createNewFile();

                // Copy the template database file to the newly created file
                // (optional if you don't have a template database)
                // Path templatePath = Path.of("path/to/your/template/database.db");
                // Files.copy(templatePath, dbFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

                // Initialize the database by executing SQL queries
                try {
                    connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
                     Statement stmt = connection.createStatement();
                    // Execute your SQL queries to create tables, insert data, etc.
                    stmt.executeUpdate("" +
                            "CREATE TABLE Utilisateur (\n" +
                            "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                            "    pseudo TEXT UNIQUE,\n" +
                            "    password TEXT,\n" +
                            "    dateDebut TEXT,\n" +
                            "    dateFin TEXT,\n" +
                            "    nbTaches INTEGER,\n" +
                            "    badgeGood INTEGER,\n" +
                            "    badgeVeryGood INTEGER,\n" +
                            "    badgeExcellent INTEGER\n" +
                            ");\n" +
                            "\n" +
                            "CREATE TABLE Categorie (\n" +
                            "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                            "    utilisateur_id INTEGER NOT NULL,\n" +
                            "    nom_cat TEXT,\n" +
                            "    couleur TEXT,\n" +
                            "    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id)\n" +
                            ");\n" +
                            "CREATE TABLE Projet (\n" +
                            "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                            "    utilisateur_id INTEGER NOT NULL,\n" +
                            "    nom TEXT UNIQUE,\n" +
                            "    description TEXT,\n" +
                            "    FOREIGN KEY (utilisateur_id) REFERENCES Utilisateur(id)\n" +
                            ");\n" +
                            "CREATE TABLE Jour (\n" +
                            "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                            "    utilisateur_id INTEGER NOT NULL,\n" +
                            "    date TEXT,\n" +
                            "    nombreMinimumTaches INTEGER,\n" +
                            "    FOREIGN KEY (utilisateur_id) REFERENCES Utilisateur(id)\n" +
                            ");\n" +
                            "CREATE TABLE Creneau (\n" +
                            "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                            "    jour_id INTEGER NOT NULL,\n" +
                            "    debut TEXT,\n" +
                            "    fin TEXT,\n" +
                            "    libre BOOLEAN,\n" +
                            "    dureeMinimale TEXT,\n" +
                            "    FOREIGN KEY (jour_id) REFERENCES Jour(id)\n" +
                            ");\n" +
                            " CREATE TABLE Tache (\n" +
                            "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                            "    creneau_id INTEGER NOT NULL,\n" +
                            "    categorie_id INTEGER NOT NULL,\n" +
                            "    projet_id INTEGER,\n" +
                            "    type INTEGER,\n" +
                            "    nom TEXT,\n" +
                            "    duree TEXT,\n" +
                            "    prio TEXT,\n" +
                            "    deadline TEXT,\n" +
                            "    etat TEXT,\n" +
                            "    FOREIGN KEY (categorie_id) REFERENCES Categorie(id),\n" +
                            "    FOREIGN KEY (creneau_id) REFERENCES Creneau(id),\n" +
                            "    FOREIGN KEY (projet_id) REFERENCES Projet(id)\n" +
                            ");\n");
                } catch (SQLException e){
                        System.out.println("Error initializing the database: " + e.getMessage());
                    }

                System.out.println("Database file created and initialized successfully.");
            } catch (IOException e) {
                System.out.println("Error creating database file: " + e.getMessage());
            }
        } else {
            try {
                // Connect to the existing database
                try {
                    Class.forName(JDBC_DRIVER);
                    this.connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile.getAbsolutePath());
                } catch (SQLException e) {
                    System.out.println("Error connecting to the database: " + e.getMessage());
                }
            } catch (Exception e) {
                System.out.println("Error accessing the database file: " + e.getMessage());
            }
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
