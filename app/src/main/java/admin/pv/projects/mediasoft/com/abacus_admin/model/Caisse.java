package admin.pv.projects.mediasoft.com.abacus_admin.model;

import java.util.Date;

/**
 * Created by Mayi on 02/10/2015.
 */
public class Caisse {

    long id ;
    String code  = null ;
    String login  = null ;
    String imei  = null ;
    String password  = null ;
    double solde = 0 ;
    long pointVente_id ;
    Date created_at = null ;

    public Caisse(){
        created_at = new Date() ;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public void setPointVente(long pointVente) {
        this.pointVente_id = pointVente;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public long getPointVente_id() {
        return pointVente_id;
    }

    public void setPointVente_id(long pointVente_id) {
        this.pointVente_id = pointVente_id;
    }

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getImei() {
        return imei;
    }

    public long getPointVente() {
        return pointVente_id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public double getSolde() {
        return solde;
    }

    public void setSolde(double solde) {
        this.solde = solde;
    }
}
