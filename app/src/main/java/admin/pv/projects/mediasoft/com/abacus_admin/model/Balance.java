package admin.pv.projects.mediasoft.com.abacus_admin.model;

import java.util.Date;

/**
 * Created by mediasoft on 01/06/2017.
 */

public class Balance {
    long id = 0 ;
    String libelle = null ;
    Date datedebut = null ;
    Date datefin = null ;
    long pointvente_id = 0 ;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Date getDatedebut() {
        return datedebut;
    }

    public void setDatedebut(Date datedebut) {
        this.datedebut = datedebut;
    }

    public Date getDatefin() {
        return datefin;
    }

    public void setDatefin(Date datefin) {
        this.datefin = datefin;
    }

    public long getPointvente_id() {
        return pointvente_id;
    }

    public void setPointvente_id(long pointvente_id) {
        this.pointvente_id = pointvente_id;
    }
}
