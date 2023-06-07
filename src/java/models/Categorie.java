package tp.javafx.models;

public class Categorie {
    private String nom_cat;
    private String couleur;


    public String getNom_cat() {
        return nom_cat;
    }


    public Categorie(String nom_cat, String couleur) {
        super();
        this.nom_cat = nom_cat;
        this.couleur = couleur;
    }


    public void setNom_cat(String nom_cat) {
        this.nom_cat = nom_cat;
    }


    public Categorie(String nom_cat) {
        super();
        this.nom_cat = nom_cat;
    }


    public String getCouleur() {
        return couleur;
    }


    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }


}
