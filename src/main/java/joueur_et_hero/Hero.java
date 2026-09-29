package joueur_et_hero;

import java.util.UUID;

public class Hero {
    UUID id;
    UUID JoueurId;
    NomHeros nom;
    Espece espece;
    ClassHeros classHeros;
    Caracteristique caracteristique;
    PointDeVie pointDeVie;
    ReserveMagie magie;
    Progression progression;
    Inventaire inventaire;
    Equipement equipement;
    GestionEffets effets;
    public Hero() {}
}

public class NomHero {
    String nom;
    public NomHero(String nom) {
        this.nom = nom;
    }
    public String getNom() {
        return nom;
    }
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
