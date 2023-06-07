  DROP TABLE Tache;
  DROP TABLE Creneau;
  DROP TABLE Jour;
  DROP TABLE Projet;
  DROP TABLE Categorie;
  DROP TABLE Utilisateur;

  CREATE TABLE Utilisateur (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    pseudo TEXT UNIQUE,
    password TEXT,
    dateDebut TEXT,
    dateFin TEXT,
    nbTaches INTEGER,
    badgeGood INTEGER,
    badgeVeryGood INTEGER,
    badgeExcellent INTEGER
);

CREATE TABLE Categorie (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    utilisateur_id INTEGER NOT NULL,
    nom_cat TEXT,
    couleur TEXT,
    FOREIGN KEY (utilisateur_id) REFERENCES utilisateur(id)
);
CREATE TABLE Projet (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    utilisateur_id INTEGER NOT NULL,
    nom TEXT UNIQUE,
    description TEXT,
    FOREIGN KEY (utilisateur_id) REFERENCES Utilisateur(id)
);
CREATE TABLE Jour (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    utilisateur_id INTEGER NOT NULL,
    date TEXT,
    nombreMinimumTaches INTEGER,
    FOREIGN KEY (utilisateur_id) REFERENCES Utilisateur(id)
);
CREATE TABLE Creneau (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    jour_id INTEGER NOT NULL,
    debut TEXT,
    fin TEXT,
    libre BOOLEAN,
    dureeMinimale TEXT,
    FOREIGN KEY (jour_id) REFERENCES Jour(id)
);
 CREATE TABLE Tache (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    creneau_id INTEGER NOT NULL,
    categorie_id INTEGER NOT NULL,
    projet_id INTEGER,
    type INTEGER,
    nom TEXT,
    duree TEXT,
    prio TEXT,
    deadline TEXT,
    etat TEXT,
    FOREIGN KEY (categorie_id) REFERENCES Categorie(id),
    FOREIGN KEY (creneau_id) REFERENCES Creneau(id),
    FOREIGN KEY (projet_id) REFERENCES Projet(id)
);






