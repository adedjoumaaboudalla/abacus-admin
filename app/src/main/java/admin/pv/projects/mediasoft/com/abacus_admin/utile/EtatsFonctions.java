package admin.pv.projects.mediasoft.com.abacus_admin.utile;

import android.content.Context;
import android.util.Log;

import com.itextpdf.text.Element;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import admin.pv.projects.mediasoft.com.abacus_admin.dao.BalanceDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.BalanceLigneDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CaisseDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.DAOBase;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.LigneDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.MouvementDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.OperationDAO;

import admin.pv.projects.mediasoft.com.abacus_admin.dao.ProduitDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Balance;
import admin.pv.projects.mediasoft.com.abacus_admin.model.BalanceLigne;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Caisse;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Ligne;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Mouvement;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Operation;

import admin.pv.projects.mediasoft.com.abacus_admin.model.Produit;

/**
 * Created by mediasoft on 19/06/2017.
 */

public class EtatsFonctions {

    private static ArrayList<Operation> payements;
    private static ArrayList<Operation> operations;
    private final String datedebut;
    private final String datefin;
    private final LigneDAO ligneDAO;
    private final BalanceLigneDAO balanceLigneDAO;
    private final BalanceDAO balanceDAO;
    private final ProduitDAO produitDAO;
    OperationDAO operationDAO = null ;
    MouvementDAO mouvementDAO = null ;
    CaisseDAO caisseDAO = null ;
    private double valeur = 0;
    private ArrayList<Mouvement> mouvements;
    private Ligne ligne;
    Date date = null ;
    Calendar c = null ;

    public EtatsFonctions(Context context, String datedebut, String datefin) {
        caisseDAO = new CaisseDAO(context) ;
        mouvementDAO = new MouvementDAO(context) ;
        operationDAO = new OperationDAO(context) ;
        produitDAO = new ProduitDAO(context) ;
        balanceDAO = new BalanceDAO(context) ;
        ligneDAO = new LigneDAO(context) ;
        balanceLigneDAO = new BalanceLigneDAO(context) ;
        this.datedebut = datedebut ;
        this.datefin = datefin ;
        this.date = new Date() ;
        c = Calendar.getInstance() ;
    }

    public double getClientAvanceVersées(long pv) {

        try {
            operations = operationDAO.getAll(1, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        Operation operation = null ;
        ArrayList<Operation> payements = null ;

        for (int i = 0; i < operations.size(); i++) {
            operation = operations.get(i) ;
            if (operation.getAnnuler()==1  && operation.getDateannulation().before(date) && operation.getDateannulation().before(date)) continue;
            if (operation.getPayer() == 0){
                payements = operationDAO.getMany(operation.getId_externe()) ;
                for (int j = 0; j < payements.size(); j++) {
                    valeur += payements.get(j).getMontant() ;
                }
            }
        }

        return valeur ;
    }


    public double getFournisseurAvanceVersees(long pv) {

        try {
            operations = operationDAO.getAll(28, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        Operation operation = null ;
        ArrayList<Operation> payements = null ;

        for (int i = 0; i < operations.size(); i++) {
            operation = operations.get(i) ;
            if (operation.getAnnuler()==1  && operation.getDateannulation().before(date)  && operation.getDateannulation().before(date)) continue;
            if (operation.getPayer() == 0){
                payements = operationDAO.getMany(operation.getId_externe()) ;
                for (int j = 0; j < payements.size(); j++) {
                    valeur += payements.get(j).getMontant() ;
                }
            }
        }

        return valeur ;
    }


    public double getVenteCredit(long pv, String dd, String df) {

        try {
            if (dd!=null && df!=null)operations = operationDAO.getAll(18, DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(18, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        Operation operation = null ;
        ArrayList<Operation> payements = null ;

        for (int i = 0; i < operations.size(); i++) {
            operation = operations.get(i) ;
            if (operation.getAnnuler()==1  && operation.getDateannulation().before(date)  && operation.getDateannulation().before(date)) continue;
            valeur += operation.getMontant()-operation.getRemise() ;
            payements = operationDAO.getMany(operation.getId_externe()) ;
            for (int j = 0; j < payements.size(); j++) {
                if (operation.getAnnuler()==1  && operation.getDateannulation().before(date)  && operation.getDateannulation().before(date)) continue;
                valeur -= payements.get(j).getMontant() ;
            }
        }

        return valeur ;
    }






    public double getVente(long pv, String dd, String df) {

        try {
            if (dd!=null && df!=null)operations = operationDAO.getAll(1, DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(1, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        Operation operation = null ;
        ArrayList<Operation> payements = null ;

        for (int i = 0; i < operations.size(); i++) {
            operation = operations.get(i) ;
            if (operation.getAnnuler()==1  && operation.getDateannulation().before(date)  && operation.getDateannulation().before(date)) continue;
            valeur += operation.getMontant()-operation.getRemise() ;
        }

        return valeur ;
    }



    public double getAchatCredit(long pv,String dd, String df) {

        try {
            if (dd!=null && df!=null)operations = operationDAO.getAll(25, DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(25, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        Operation operation = null ;
        ArrayList<Operation> payements = null ;

        for (int i = 0; i < operations.size(); i++) {
            operation = operations.get(i) ;
            if (operation.getAnnuler()==1  && operation.getDateannulation().before(date)  && operation.getDateannulation().before(date)) continue;
            if (operation.getPayer() == 0){
                valeur += operation.getMontant()-operation.getRemise() ;
                payements = operationDAO.getMany(operation.getId_externe()) ;
                for (int j = 0; j < payements.size(); j++) {
                    valeur -= payements.get(j).getMontant() ;
                }
            }
        }

        return valeur ;
    }


    public double getEmprunt(long pv, String dd, String df) {

        try {
            if (dd!=null && df!=null)operations = operationDAO.getAll(24, DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(24, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        Operation operation = null ;
        ArrayList<Operation> payements = null ;

        for (int i = 0; i < operations.size(); i++) {
            operation = operations.get(i) ;
            if (operation.getAnnuler()==1  && operation.getDateannulation().before(date)  && operation.getDateannulation().before(date)) continue;
            if (operation.getPayer() == 0){
                valeur += operation.getMontant()-operation.getRemise() ;
                payements = operationDAO.getMany(operation.getId_externe()) ;
                for (int j = 0; j < payements.size(); j++) {
                    valeur -= payements.get(j).getMontant() ;
                }
            }
        }

        return valeur ;
    }




    public double getAutreDette(long pv, String dd, String df) {

        try {
            if (dd!=null && df!=null)operations = operationDAO.getAll(26, DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(26, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        Operation operation = null ;
        ArrayList<Operation> payements = null ;

        for (int i = 0; i < operations.size(); i++) {
            operation = operations.get(i) ;
            if (operation.getAnnuler()==1  && operation.getDateannulation().before(date)) continue;
            if (operation.getPayer() == 0){
                valeur += operation.getMontant()-operation.getRemise() ;
                payements = operationDAO.getMany(operation.getId_externe()) ;
                for (int j = 0; j < payements.size(); j++) {
                    valeur -= payements.get(j).getMontant() ;
                }
            }
        }

        return valeur ;
    }

    public double getAchat(long pv, String dd, String df) {

        try {
            if (dd!=null && df!=null)operations = operationDAO.getAll(5,DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(5,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1  && operations.get(i).getDateannulation().before(date)) continue;
            valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
        }

        return  valeur ;
    }



    public double getImmoFinanciere(long pv, String dd, String df) {

        operations = null ;
        try {
            if (dd != null && df != null)operations = operationDAO.getAll(17, DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(17, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1  && operations.get(i).getDateannulation().before(date)) continue;
            valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
        }
        return valeur ;
    }

    public double getImmoIncorp(long pv,String dd, String df) {
        operations = null ;
        try {
            if (dd!=null && df!=null)operations = operationDAO.getAll(16, DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(16, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1  && operations.get(i).getDateannulation().before(date)) continue;
            valeur += operations.get(i).getMontant()-operations.get(i).getRemise() - operations.get(i).getMontant_ammor() ;
        }
        return  valeur ;
    }

    public double getImmoCorp(long pv,String dd, String df) {
        operations = null ;
        try {
            if (dd!=null && df!=null)operations = operationDAO.getAll(15, DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(15, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1  && operations.get(i).getDateannulation().before(date)) continue;
            valeur += operations.get(i).getMontant()-operations.get(i).getRemise() - operations.get(i).getMontant_ammor() ;
        }

        return  valeur ;
    }



    public double getAmmortCorp(long pv,String dd, String df) {
        operations = null ;
        try {
            if (dd != null && df != null)operations = operationDAO.getAll(15, DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(15, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1  && operations.get(i).getDateannulation().before(date)) continue;
            valeur += operations.get(i).getMontant_ammor();
        }

        return  valeur ;
    }



    public double getAmmortInCorp(long pv, String dd, String df) {
        operations = null ;
        try {
            if (dd!=null && df!=null) operations = operationDAO.getAll(16, DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(16, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1  && operations.get(i).getDateannulation().before(date)) continue;
            valeur += operations.get(i).getMontant_ammor();
        }

        return  valeur ;
    }



    public double getChargeImmo(long pv,String dd, String df) {

        ArrayList<Operation> operations = null ;
        try {
            if (dd!=null && df!=null) operations = operationDAO.getAll(14, DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(14, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1  && operations.get(i).getDateannulation().before(date)) continue;
            valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
        }

        return valeur ;
    }



    public double getReportChargeImmo(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.CHA_IMMO);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur + getChargeImmo(pv,c.get(Calendar.YEAR)+"-01-01",datedebut) ;
    }




    public double getReportImmoCorp(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.IMMO_CORP);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur + getImmoCorp(pv,c.get(Calendar.YEAR)+"-01-01",datedebut) ;
    }



    public double getReportImmoInCorp(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.IMMO_INCORP);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur + getImmoIncorp(pv,c.get(Calendar.YEAR)+"-01-01",datedebut);
    }



    public double getReportImmoFin(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.IMMO_FINANCIERE);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur + getImmoIncorp(pv,c.get(Calendar.YEAR)+"-01-01",datedebut);
    }



    public double getReportAmmoCorp(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.AMOR_CORP);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur + getAmmortCorp(pv,c.get(Calendar.YEAR)+"-01-01",datedebut) ;
    }


    public double getReportAmmoInCorp(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.AMOR_INCORP);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur + getAmmortInCorp(pv,c.get(Calendar.YEAR)+"-01-01",datedebut);
    }



    public double getReportAchat(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.MARCH);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur + getAchat(pv,c.get(Calendar.YEAR)+"-01-01",datedebut);
    }




    public double getReportFournAvanceVerse(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.AVAN_FOUR);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur ;
    }


    public double getReportVenteCredit(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.CLI);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur + getVenteCredit(pv,c.get(Calendar.YEAR)+"-01-01",datedebut) ;
    }



    public double getReportAutreCreance(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.AUTR_CRE);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur + getAutreCreance(pv,c.get(Calendar.YEAR)+"-01-01",datedebut) ;
    }


    public double getReportCaisse(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.CAIS);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur + getCaisseDebit(pv,c.get(Calendar.YEAR)+"-01-01",datedebut) - getCaisseCredit(pv,c.get(Calendar.YEAR)+"-01-01",datedebut);
    }


    public double getReportFraisPersonnel(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.FRAIS_PERS);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur ;
    }



    public double getReportDotationAmort(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.DOT_AMOR);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur ;
    }



    public double getReportChargeFin(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.CHA_FIN);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur + getChargeFin(pv,c.get(Calendar.YEAR)+"-01-01",datedebut);
    }



    public double getReportBQSFD(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.BANQ_SFD);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur  + getBanqueOuSfdDebit(pv,c.get(Calendar.YEAR)+"-01-01",datedebut) - getBanqueOuSfdCredit(pv,c.get(Calendar.YEAR)+"-01-01",datedebut);
    }




    public double getReportDepotGarantie(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.DEPO_GAR);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur + getDepotGarantie(pv,c.get(Calendar.YEAR)+"-01-01",datedebut);
    }



    public double getReportDepotAterme(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.DEPO_TERM);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur + getDepotTerme(pv,c.get(Calendar.YEAR)+"-01-01",datedebut);
    }



    public double getReportAutrePlacement(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.AUTR_PLA);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur  + getDepotAutre(pv,c.get(Calendar.YEAR)+"-01-01",datedebut);
    }



    public double getReportCapital(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.CAPITAL);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur ;
    }



    public double getReportReserve(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.RESERVE);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur ;
    }




    public double getReportNouveau(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.REP_NOUV);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur ;
    }




    public double getReportResultat(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.RES_EXE);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur ;
    }




    public double getReportEmprunt(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.EMP_BANQ);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur + getEmprunt(pv,c.get(Calendar.YEAR)+"-01-01",datedebut);
    }



    public double getReportAvanceVerse(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.CLI_AVAN);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur ;
    }


    public double getReportAchatCredit(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.FOURN);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur + getAchatCredit(pv,c.get(Calendar.YEAR)+"-01-01",datedebut);
    }


    public double getReportAutreDette(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.AUTR_DET);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur + getAutreDette(pv,c.get(Calendar.YEAR)+"-01-01",datedebut);
    }




    public double getReportVariationStock(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.VAR_STOCK);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur ;
    }




    public double getReportTransport(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.TRANSP);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur ;
    }




    public double getReportTel(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.TELE);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur ;
    }



    public double getReportAutreDep(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.AUTR_DEP);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur ;
    }




    public double getReportLoyer(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.LOYER);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null && ligne!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur ;
    }



    public double getReportImpotTaxe(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.IMP_TAXE);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur ;
    }




    public double getReportChargeExcep(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.CHAR_EXCEP);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur + getChargeExcep(pv,c.get(Calendar.YEAR)+"-01-01",datedebut);
    }





    public double getReportProdFin(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.PROD_FIN);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur + getProduitFinancier(pv,c.get(Calendar.YEAR)+"-01-01",datedebut);
    }




    public double getReportVente(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.VENT);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur + getVente(pv,c.get(Calendar.YEAR)+"-01-01",datedebut);
    }




    public double getReportProdExcept(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.PROD_EXCEP);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur + getProduitExcep(pv,c.get(Calendar.YEAR)+"-01-01",datedebut);
    }



    public double getReportEau(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.EAO);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur ;
    }


    public double getReportElectricite(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.ELEC);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur ;
    }


    public double getReportPub(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.PUBL);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur ;
    }



    public double getReportMission(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.MISS_RECEP);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur ;
    }



    public double getReportEntretient(long pv) {
        ligne = ligneDAO.getOneByCode(OperationDAO.ENTR_REP);
        Balance balance = balanceDAO.getOneByPv(pv) ;
        valeur = 0 ;
        if (balance!=null){
            BalanceLigne balanceLigne = balanceLigneDAO.getOne(ligne.getId(),balance.getId()) ;
            if (balanceLigne!=null) valeur = balanceLigne.getSolde_cloture() ;
        }
        return valeur ;
    }



    public double getBanqueOuSfdDebit(long pv, String dd, String df) {

        ArrayList<Operation> operations = null ;
        try {
            if (dd!=null && df!=null)operations = operationDAO.getAll(0, DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(0, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            // Si lop est annuler on saute
            if (operations.get(i).getAnnuler()==1  && operations.get(i).getDateannulation().before(date)) continue;
            // Si lop nest pas lié à la banque on saute
            if (operations.get(i).getComptebanque_id()==0) continue;
            // Si lop est un virement banque caisse
            if (operations.get(i).getTypeOperation_id().equals(OperationDAO.BQ)){
                // Si c'est une sortie d'argent de la caisse vers la banque on comptabilise
                if (operations.get(i).getEntree()==0)valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }
            // Sinon on comptabilise la valeur de lop
            else if (operations.get(i).getEntree()==1)valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
        }

        ArrayList<Operation> payements = operationDAO.getReglement();
        for (int j = 0; j < payements.size(); j++) {
            // Si le reglement est lié à la banque
            if (payements.get(j).getComptebanque_id()>0) {
                Operation operation = operationDAO.getOne(payements.get(j).getOperation_id()) ;
                // Si cest une entree dargent à la banque
                if (operation.getEntree()==1){
                    // Si c'est un emprunt on saute car le reglement d'un emprunt est une charge
                    if (operation.getTypeOperation_id().equals(OperationDAO.EMPRUNT))continue;
                    else  {
                        // Sinon on comptabilise
                        valeur += payements.get(j).getMontant();
                        Log.e("PAYEMENT", String.valueOf(payements.get(j).getMontant())) ;
                    }
                }
            }
        }

        return valeur ;
    }



    public double getBanqueOuSfdCredit(long pv, String dd, String df) {

        ArrayList<Operation> operations = null ;
        try {
            if (dd!=null && df!=null)operations = operationDAO.getAll(0, DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(0, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            // Si lop est annuler on saute
            if (operations.get(i).getAnnuler()==1  && operations.get(i).getDateannulation().before(date)) continue;
            // Si lop nest pas lié à la banque on saute
            if (operations.get(i).getComptebanque_id()==0) continue;
            // Si lop est un virement banque caisse
            if (operations.get(i).getTypeOperation_id().equals(OperationDAO.BQ)){
                // Si c'est une sortie d'argent de la banque vers la caisse on comptabilise
                if (operations.get(i).getEntree()==1)valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }
            // Sinon on comptabilise la valeur de lop
            else if (operations.get(i).getEntree()==0) valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
        }


        ArrayList<Operation> payements = operationDAO.getReglement();
        for (int j = 0; j < payements.size(); j++) {
            // Si le reglement est lié à la banque
            if (payements.get(j).getComptebanque_id()>0) {
                Operation operation = operationDAO.getOne(payements.get(j).getOperation_id()) ;
                // Si cest une entree dargent à la banque
                if (operation.getEntree()==0)valeur += payements.get(j).getMontant();
                    // Si c'est un emprunt on comptabilise le reglement
                else if (operation.getTypeOperation_id().equals(OperationDAO.EMPRUNT)) valeur += payements.get(j).getMontant();
                Log.e("PAYEMENT", String.valueOf(payements.get(j).getMontant())) ;
            }
        }


        return valeur ;
    }



    public double getDepotTerme(long pv, String dd, String df) {

        ArrayList<Operation> operations = null ;
        try {
            if (dd!=null && df!=null)operations = operationDAO.getAll(21, DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(21, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1  && operations.get(i).getDateannulation().before(date)) continue;
            valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
        }

        return valeur ;
    }



    public double getDepotAutre(long pv,String dd, String df) {

        ArrayList<Operation> operations = null ;
        try {
            if (dd!=null && df!=null)operations = operationDAO.getAll(22, DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(22, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1  && operations.get(i).getDateannulation().before(date)) continue;
            valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
        }

        return valeur ;
    }



    public double getDepotGarantie(long pv, String dd, String df) {

        ArrayList<Operation> operations = null ;
        try {
            if (dd!=null && df!=null)operations = operationDAO.getAll(20, DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(20, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        valeur = 0 ;

        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1  && operations.get(i).getDateannulation().before(date)) continue;
            valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
        }

        return valeur ;
    }



    public double getAvoirPerso(long pv) {

        valeur = 0 ;

        return valeur ;
    }




    public double getDepense(long pv) {

        try {
            operations = operationDAO.getAll(2,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1  && operations.get(i).getDateannulation().before(date)) continue;
            if (operations.get(i).getTypeOperation_id().equals(OperationDAO.TRANSPORT))valeur += operations.get(i).getMontant() ;
        }

        return  valeur ;
    }

    public double getAutreCreance(long pv, String dd, String df) {

        try {
            if (dd!=null && df!=null)operations = operationDAO.getAll(19,DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(19,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1  && operations.get(i).getDateannulation().before(date)) continue;
            valeur += operations.get(i).getMontant() ;
        }

        return  valeur ;
    }

    public double getProduitFinancier(long pv,String dd, String df) {

        try {
            if (dd!=null && df!=null)operations = operationDAO.getAll(7,DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(7,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1  && operations.get(i).getDateannulation().before(date)) continue;
            valeur += operations.get(i).getMontant() ;
        }

        return  valeur ;
    }

    public double getChargeFin(long pv, String dd, String df) {

        try {
            if (dd!=null && df!=null)operations = operationDAO.getAll(8,DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(8,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1  && operations.get(i).getDateannulation().before(date)) continue;
            valeur += operations.get(i).getMontant() ;
        }

        return  valeur ;
    }


    public double getProduitExcep(long pv,String dd, String df) {

        try {
            if (dd!=null && df!=null)operations = operationDAO.getAll(9,DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(9,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1  && operations.get(i).getDateannulation().before(date)) continue;
            valeur += operations.get(i).getMontant() ;
        }

        return  valeur ;
    }



    public double getChargeExcep(long pv, String dd, String df) {

        try {
            if (dd!=null && df!=null)operations = operationDAO.getAll(10,DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(10,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1  && operations.get(i).getDateannulation().before(date)) continue;
            valeur += operations.get(i).getMontant() ;
        }

        return  valeur ;
    }



    public double getImpotTaxe(long pv) {


        try {
            operations = operationDAO.getAll(2,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),0);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1  && operations.get(i).getDateannulation().before(date)) continue;
            if (operations.get(i).getTypeOperation_id().equals(OperationDAO.IMPOT))valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
        }

        return  valeur ;
    }


    public double getCaisse(long pv) {
        valeur = 0 ;
        ArrayList<Caisse> caisses = caisseDAO.getAllById(pv);
        for (int i = 0; i <caisses.size(); i++) {
            valeur += caisses.get(i).getSolde() ;
        }

        return valeur ;
    }


    public double getCaisseDebit(long pv, String dd, String df) {

        try {
            if (dd!=null && df!=null)operations = operationDAO.getAll(0,DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(0,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            Operation operation = operations.get(i) ;
            // Si lop est annulé on saute
            if (operation.getAnnuler()==1  && operation.getDateannulation().before(date)) continue;
            // Si lop nest pas un virement comptebanque on continue mais est lié à la banque on saute
            if (operation.getComptebanque_id()>0  && !operation.getTypeOperation_id().equals(OperationDAO.BQ)) continue;
            // Si lop est une entré dargent
            if (operation.getEntree()==1){
                // Si lop nes pas payer
                if (operation.getPayer()==0) {
                    // Si lop est un emprunt
                    if (operation.getTypeOperation_id().equals(OperationDAO.EMPRUNT)) {
                        valeur += operation.getMontant() - operation.getRemise();
                    }
                    else {
                        // on comptabilise les reglement
                        ArrayList<Operation> payements = operationDAO.getMany(operation.getId_externe());
                        for (int j = 0; j < payements.size(); j++) {
                            if (payements.get(j).getComptebanque_id()>0) continue;
                            valeur += payements.get(j).getMontant();
                        }
                    }
                }
                // Sinon on comptabilise la valeur de lop
                else {
                    valeur += operations.get(i).getMontant() - operations.get(i).getRemise();
                }
            }
        }

        return valeur ;
    }


    public double getCaisseCredit(long pv, String dd, String df) {

        try {
            if (dd!=null && df!=null)operations = operationDAO.getAll(0,DAOBase.formatter2.parse(dd),DAOBase.formatter2.parse(df),pv);
            else operations = operationDAO.getAll(0,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            Operation operation = operations.get(i) ;
            // Si lop est annulé on saute
            if (operation.getAnnuler()==1  && operation.getDateannulation().before(date)) continue;
            // Si lop nest pas un virement comptebanque on continue mais est lié à la banque on saute
            if (operation.getComptebanque_id()>0  && !operation.getTypeOperation_id().equals(OperationDAO.BQ)) continue;
            // Si lop est une entré dargent
            if (operation.getEntree()==0){
                // Si lop nes pas payer
                if (operation.getPayer()==0) {
                    // on comptabilise les reglement
                    ArrayList<Operation> payements = operationDAO.getMany(operation.getId_externe());
                    for (int j = 0; j < payements.size(); j++) {
                        if (payements.get(j).getComptebanque_id()>0) continue;
                        valeur += payements.get(j).getMontant();
                    }
                }
                // Si lop est payer entiererment on comptabilise son montant
                else {
                    valeur += operations.get(i).getMontant() - operations.get(i).getRemise();
                }
            }
            // Si lop est un emprunt
            else if (operation.getTypeOperation_id().equals(OperationDAO.EMPRUNT)){
                // On comptabilise les reglement de lemprunt
                ArrayList<Operation> payements = operationDAO.getMany(operation.getId_externe());
                for (int j = 0; j < payements.size(); j++) {
                    if (payements.get(j).getComptebanque_id()>0) continue;
                    valeur += payements.get(j).getMontant();
                }
            }
        }

        return valeur ;
    }

    public double getVariationStock(long pv) {


        try {
            mouvements = mouvementDAO.getManyByInterval(DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        Operation operation = null ;
        for (int i = 0; i < mouvements.size(); i++) {
            operation = operationDAO.getOne(mouvements.get(i).getOperation_id()) ;
            if (operation.getAnnuler()==1  && operation.getDateannulation().before(date)) continue;
            if (mouvements.get(i).getEntree() == 0)valeur+= mouvements.get(i).getCmup() * mouvements.get(i).getQuantite() ;
            else valeur-= mouvements.get(i).getCmup() * mouvements.get(i).getQuantite() ;
        }

        return  valeur ;
    }

    public double getImmo(long pv) {
        valeur = 0 ;
        valeur = getImmoCorp(pv,null,null) + getImmoFinanciere(pv,null,null) + getImmoIncorp(pv,null,null) + getChargeImmo(pv,null,null) ;
        return  valeur ;
    }

    public double getStockFinal(long pv) {

        //just some random data to fill
        float entree = 0 ;
        float sortie = 0 ;

        ArrayList<Produit> produits = null ;
        if (pv==0) produits = produitDAO.getAll();
        else produits = produitDAO.getAllByPv(pv) ;

        ArrayList<Mouvement> mouvements = null;

        double valeur = 0 ;
        double total = 0 ;
        double quantite = 0 ;
        double cmpu = 0 ;
        double restant = 0 ;
        double qsortie = 0 ;
        double qentree = 0 ;
        // Vente par produit
        Mouvement mouvement = null ;
        Operation operation = null ;

        for (int i = 0; i < produits.size(); i++) {
            try {
                mouvements = mouvementDAO.getManyByProductInterval(produits.get(i).getId_externe(),DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin)) ;
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (mouvements != null){
                valeur = 0 ;
                quantite = 0 ;
                restant = 0 ;
                qentree = 0 ;
                cmpu = 0 ;
                qsortie = 0 ;

                for (int j = 0; j < mouvements.size(); j++) {
                    mouvement = mouvements.get(j) ;
                    operation = operationDAO.getOne(mouvement.getOperation_id()) ;
                    if (operation==null || (operation.getAnnuler()==1 && operation.getDateannulation().before(new Date())) || operation.getTypeOperation_id().startsWith(OperationDAO.CMD)) continue;

                    //if (pv!=0 && caisseDAO.getOne(operation.getCaisse_id()).getPointVente_id()!=pv) continue;
                    if (j==0)
                        if (mouvement.getRestant()==mouvement.getQuantite() && mouvement.getEntree()==0){
                            restant = 0 ;
                        }
                        else restant = mouvement.getRestant() ;

                    if (mouvement.getEntree()==0) {
                        valeur -= mouvement.getPrixA() * mouvement.getQuantite() ;
                        qentree += mouvement.getQuantite() ;
                        cmpu = mouvement.getPrixA() ;
                    }
                    else {
                        valeur += mouvement.getCmup() * mouvement.getQuantite() ;
                        qsortie += mouvement.getQuantite() ;
                    }
                        /*
                        if (restant!=0) cmpu = valeur / restant ;
                        else cmpu = mouvement.getCmup() ;
                        */
                }

                quantite = restant + qentree - qsortie ;
                total+=Math.abs(valeur) ;
            }
        }

        return total;
    }
}
