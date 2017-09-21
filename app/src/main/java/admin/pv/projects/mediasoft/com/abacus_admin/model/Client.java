package admin.pv.projects.mediasoft.com.abacus_admin.model;

import java.util.Date;

/**
 * Created by Mayi on 02/10/2015.
 */
public class Client {

    long id = 0 ;
    String nom  = "" ;
    String prenom  = "" ;
    String raisonsocial  = "" ;
    String email  = "" ;
    String tel  = "" ;
    String adresse  = "" ;
    Date created_at = null ;

    public String getRaisonsocial() {
        return raisonsocial;
    }

    public void setRaisonsocial(String raisonsocial) {
        this.raisonsocial = raisonsocial;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Client(){
        created_at = new Date() ;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }


    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
