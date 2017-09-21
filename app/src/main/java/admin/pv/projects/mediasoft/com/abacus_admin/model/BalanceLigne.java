package admin.pv.projects.mediasoft.com.abacus_admin.model;

import java.util.Date;

/**
 * Created by mediasoft on 01/06/2017.
 */

public class BalanceLigne {
    long id = 0 ;
    long balance_id = 0 ;
    long ligne_id = 0 ;
    double solde_report = 0 ;
    double solde_cloture = 0 ;
    double mouvement = 0 ;
    Date created_at = null ;
    Date updated_at = null ;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBalance_id() {
        return balance_id;
    }

    public void setBalance_id(long balance_id) {
        this.balance_id = balance_id;
    }

    public long getLigne_id() {
        return ligne_id;
    }

    public void setLigne_id(long ligne_id) {
        this.ligne_id = ligne_id;
    }

    public double getSolde_report() {
        return solde_report;
    }

    public void setSolde_report(double solde_report) {
        this.solde_report = solde_report;
    }

    public double getSolde_cloture() {
        return solde_cloture;
    }

    public void setSolde_cloture(double solde_cloture) {
        this.solde_cloture = solde_cloture;
    }

    public double getMouvement() {
        return mouvement;
    }

    public void setMouvement(double mouvement) {
        this.mouvement = mouvement;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
}
