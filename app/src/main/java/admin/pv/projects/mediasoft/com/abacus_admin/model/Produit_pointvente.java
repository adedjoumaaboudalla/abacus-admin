package admin.pv.projects.mediasoft.com.abacus_admin.model;

/**
 * Created by Mayi on 08/01/2016.
 */
public class Produit_pointvente {
    long id = 0 ;
    long produit_id = 0 ;
    long pointvente_id = 0 ;

    public Produit_pointvente(long produit_id, long pointvente_id) {
        this.produit_id = produit_id;
        this.pointvente_id = pointvente_id;
    }

    public Produit_pointvente() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProduit_id() {
        return produit_id;
    }

    public void setProduit_id(long produit_id) {
        this.produit_id = produit_id;
    }

    public long getPointvente_id() {
        return pointvente_id;
    }

    public void setPointvente_id(long pointvente_id) {
        this.pointvente_id = pointvente_id;
    }
}
