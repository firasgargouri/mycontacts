package com.nsc.mycontacts;

import org.json.JSONObject;

public class Contact {
    private String nom,adresse,tel1,tel2,entreprise;
    private int id;

    public Contact(JSONObject jobject){
        this.id = jobject.optInt("id");
        this.nom = jobject.optString("nom");
        this.adresse = jobject.optString("adresse");
        this.tel1 = jobject.optString("tel1");
        this.tel2 = jobject.optString("tel2");
        this.entreprise = jobject.optString("entreprise");
    }

    public String getNom() {
        return nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getTel1() {
        return tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public String getEntreprise() {
        return entreprise;
    }

    public int getId() {
        return id;
    }
}
