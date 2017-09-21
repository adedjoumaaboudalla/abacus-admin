package admin.pv.projects.mediasoft.com.abacus_admin.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import admin.pv.projects.mediasoft.com.abacus_admin.R;
import admin.pv.projects.mediasoft.com.abacus_admin.database.CaisseHelper;
import admin.pv.projects.mediasoft.com.abacus_admin.database.MouvementHelper;
import admin.pv.projects.mediasoft.com.abacus_admin.database.OperationHelper;
import admin.pv.projects.mediasoft.com.abacus_admin.database.PointVenteHelper;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Caisse;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Commercial;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Mouvement;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Operation;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Partenaire;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Produit;

/**
 * Created by Mayi on 05/10/2015.
 */
public class OperationDAO extends DAOBase implements Crud<Operation> {
    public static final String DEPENSE = "DP_";
    public static final String PLACEMENT = "PLAC_";
    public static final String IMMOBILISATION = "IMMO_";
    private ArrayList<Operation> today;
    Context mContext = null ;


    public static final String VENTE = "VT";
    public static final String REGLEMENT = "RGLMT";
    public static final String DIVERS = "DV";
    public static final String CHARGE = "CH_";
    public static final String PRODUIT = "PD_";
    public static final String ACHAT = "ACH";
    public static final String CH_EXC = "CH_EXC";
    public static final String CH_FN = "CH_FN";
    public static final String AUTRES_DEP = "DP_AUTRES";
    public static final String EAU = "DP_EAU";
    public static final String ELECTRICITE = "DP_ELECT";
    public static final String FRAIS_PERSO = "DP_FRAIS_PERSO";
    public static final String IMPOT = "DP_IMP";
    public static final String LOYER = "DP_LOYER";
    public static final String TEL = "DP_TEL";
    public static final String TRANSPORT = "DP_TRSP";
    public static final String PRODUIT_EXC = "PD_EXC";
    public static final String PRODUIT_FIN = "PD_FN";
    public static final String CMDCLNT = "CMDCLNT";
    public static final String CMDFRNSS = "CMDFRNSS";
    public static final String CMD = "CMD";
    public static final String BQ = "BQ";

    public static final String SORTIE_STOCK = "SST";
    public static final String ENTREE_STOCK = "EST";

    public static final String PUB = "DP_PUB";
    public static final String MISSION = "DP_MIS";
    public static final String ENTRETIEN_REPARATION = "DP_ENT";
    public static final String DOTATION_AMOR = "DP_AMO";
    public static final String AUTRE_DETTE = "AUTRE_DET";
    public static final String EMPRUNT = "EMPRUNT";
    public static final String CAPITAL = "CAPITAL";
    public static final String PLACEMENT_AUTRE = "PLAC_AUT";
    public static final String PLACEMENT_TERME = "PLAC_TER";
    public static final String PLACEMENT_GARANTIE = "PLAC_GAR";
    public static final String AUTRE_CRE = "AUTRE_CRE";
    public static final String IMMO_CHARGE = "IMMO_CHA";
    public static final String IMMO_CORPORREL = "IMMO_COR";
    public static final String IMMO_INCORPOREL = "IMMO_INC";
    public static final String IMMO_FINANCIERE = "IMMO_FIN";

    // Lignes CODE
    public static final String CHA_IMMO = "CHA_IMMO";
    public static final String IMMO_CORP = "IMMO_CORP";
    public static final String IMMO_INCORP = "IMMO_INCORP";
    public static final String AUTR_CRE = "AUTR_CRE";
    public static final String AMOR_CORP = "AMOR_CORP";
    public static final String AMOR_INCORP = "AMOR_INCORP";
    public static final String AVAN_FOUR = "AVAN_FOUR";
    public static final String CLI = "CLI";
    public static final String MARCH = "MARCH";
    public static final String CAIS = "CAIS";
    public static final String BANQ_SFD = "BANQ_SFD";
    public static final String DEPO_GAR = "DEPO_GAR";
    public static final String DEPO_TERM = "DEPO_TERM";
    public static final String AUTR_PLA = "AUTR_PLA";
    public static final String RESERVE = "RESERVE";
    public static final String REP_NOUV = "REP_NOUV";
    public static final String RES_EXE = "RES_EXE";
    public static final String EMP_BANQ = "EMP_BANQ";
    public static final String CLI_AVAN = "CLI_AVAN";
    public static final String FOURN = "FOURN";
    public static final String AUTR_DET = "AUTR_DET";
    public static final String VAR_STOCK = "VAR_STOCK";
    public static final String TRANSP = "TRANSP";
    public static final String TELE = "TEL";
    public static final String AUTR_DEP = "AUTR_DEP";
    public static final String EAO = "EAU";
    public static final String ELEC = "ELEC";
    public static final String PUBL = "PUB";
    public static final String MISS_RECEP = "MISS_RECEP";
    public static final String ENTR_REP = "ENTR_REP";
    public static final String FRAIS_PERS = "FRAIS_PERS";
    public static final String DOT_AMOR = "DOT_AMOR";
    public static final String CHA_FIN = "CHA_FIN";
    public static final String IMP_TAXE = "IMP_TAXE";
    public static final String CHAR_EXCEP = "CHAR_EXCEP";
    public static final String PROD_FIN = "PROD_FIN";
    public static final String VENT = "VENTE";
    public static final String PROD_EXCEP = "PROD_EXCEP";

    MouvementDAO mouvementDAO = null ;

    public OperationDAO(Context pContext) {
        super(pContext);
        mContext = pContext ;
        this.mHandler = OperationHelper.getHelper(pContext, DATABASE, VERSION);
        mouvementDAO = new MouvementDAO(pContext) ;
        open();
    }

    @Override
    public long add(Operation operation) {
        
        ContentValues contentValues = new ContentValues();
        
        contentValues.put(OperationHelper.TABLE_KEY, operation.getId());
        contentValues.put(OperationHelper.ID_EXTERNE, operation.getId_externe());
        contentValues.put(OperationHelper.REMISE, operation.getRemise());
        contentValues.put(OperationHelper.MONTANT, operation.getMontant());
        contentValues.put(OperationHelper.MONTANT_AMORTI, operation.getMontant_ammor());
        contentValues.put(OperationHelper.CAISSE_ID, operation.getCaisse_id());
        contentValues.put(OperationHelper.PARTENAIRE_ID, operation.getPartenaire_id());
        contentValues.put(OperationHelper.COMMERCIAL_ID, operation.getCommercialid());
        contentValues.put(OperationHelper.TYPEOPERATION_ID, operation.getTypeOperation_id());
        contentValues.put(OperationHelper.OPERATION_ID, operation.getOperation_id());
        contentValues.put(OperationHelper.COMPTEBANQUE_ID, operation.getComptebanque_id());
        contentValues.put(OperationHelper.ETAT, operation.getEtat());
        contentValues.put(OperationHelper.MODEPAYEMENT, operation.getModepayement());
        contentValues.put(OperationHelper.NUMCHEQUE, operation.getNumcheque());
        contentValues.put(OperationHelper.RECU, operation.getRecu());
        contentValues.put(OperationHelper.DESCRIPTION, operation.getDescription());
        contentValues.put(OperationHelper.TOKEN, operation.getToken());
        contentValues.put(OperationHelper.ANNULER, operation.getAnnuler());
        contentValues.put(OperationHelper.ENTREE, operation.getEntree());
        contentValues.put(OperationHelper.ATTENTE, operation.getAttente());
        contentValues.put(OperationHelper.NBREPRODUIT, operation.getNbreproduit());
        contentValues.put(OperationHelper.CLIENT, operation.getClient());
        contentValues.put(OperationHelper.PAYER, operation.getPayer());
        if (operation.getDateecheance()!=null) contentValues.put(OperationHelper.DATE_ECHEANCE, formatter.format(operation.getDateecheance()));
        if (operation.getDateannulation()!=null) contentValues.put(OperationHelper.DATE_ANNULATION, formatter.format(operation.getDateannulation()));
        contentValues.put(OperationHelper.DATE_OPERATION, formatter.format(operation.getDateoperation()));
        contentValues.put(OperationHelper.CREATED_AT, formatter.format(operation.getCreated_at()));

        long l = mDb.insert(OperationHelper.TABLE_NAME, null, contentValues);

        //operation.setId(l);
        //operation.setId_externe(l);
        //update(operation) ;
        //mouvementDAO.updateMany(operation) ;

        Log.e("MOUV ID",String.valueOf(l)) ;
        return l;
    }

    @Override
    public int delete(long id) {
        return  mDb.delete(OperationHelper.TABLE_NAME, OperationHelper.TABLE_KEY + " = ? ",new String[] { Long.toString(id)});
    }


    public int clean() {
        return  mDb.delete(OperationHelper.TABLE_NAME, null,null);
    }

    @Override
    public int update(Operation operation) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(OperationHelper.ID_EXTERNE, operation.getId_externe());
        contentValues.put(OperationHelper.REMISE, operation.getRemise());
        contentValues.put(OperationHelper.MONTANT, operation.getMontant());
        contentValues.put(OperationHelper.MONTANT_AMORTI, operation.getMontant_ammor());
        contentValues.put(OperationHelper.CAISSE_ID, operation.getCaisse_id());
        contentValues.put(OperationHelper.PARTENAIRE_ID, operation.getPartenaire_id());
        contentValues.put(OperationHelper.COMMERCIAL_ID, operation.getCommercialid());
        contentValues.put(OperationHelper.TYPEOPERATION_ID, operation.getTypeOperation_id());
        contentValues.put(OperationHelper.COMPTEBANQUE_ID, operation.getComptebanque_id());
        contentValues.put(OperationHelper.OPERATION_ID, operation.getOperation_id());
        contentValues.put(OperationHelper.MODEPAYEMENT, operation.getModepayement());
        contentValues.put(OperationHelper.NUMCHEQUE, operation.getNumcheque());
        contentValues.put(OperationHelper.ETAT, operation.getEtat());
        contentValues.put(OperationHelper.RECU, operation.getRecu());
        contentValues.put(OperationHelper.DESCRIPTION, operation.getDescription());
        contentValues.put(OperationHelper.ANNULER, operation.getAnnuler());
        contentValues.put(OperationHelper.ENTREE, operation.getEntree());
        contentValues.put(OperationHelper.TOKEN, operation.getToken());
        contentValues.put(OperationHelper.ATTENTE, operation.getAttente());
        contentValues.put(OperationHelper.NBREPRODUIT, operation.getNbreproduit());
        contentValues.put(OperationHelper.CLIENT, operation.getClient());
        contentValues.put(OperationHelper.PAYER, operation.getPayer());
        if (operation.getDateecheance()!=null) contentValues.put(OperationHelper.DATE_ECHEANCE, formatter.format(operation.getDateecheance()));
        if (operation.getDateannulation()!=null) contentValues.put(OperationHelper.DATE_ANNULATION, formatter.format(operation.getDateannulation()));
        contentValues.put(OperationHelper.DATE_OPERATION, formatter.format(operation.getDateoperation()));
        contentValues.put(OperationHelper.CREATED_AT, formatter.format(operation.getCreated_at()));

        int i = mDb.update(OperationHelper.TABLE_NAME, contentValues, OperationHelper.TABLE_KEY + " = ? " , new String[] { Long.toString(operation.getId()) } );

        return i;
    }

    @Override
    public Operation getOne(long id) {
        Operation operation = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ OperationHelper.TABLE_NAME +" where "  + OperationHelper.TABLE_KEY  +  "="+id+"", null );
        if (res.moveToFirst()){
            operation = new Operation();
            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.TABLE_KEY)));
            operation.setId_externe(res.getInt(res.getColumnIndex(OperationHelper.ID_EXTERNE)));
            operation.setMontant(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setMontant_ammor(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT_AMORTI)));
            operation.setNbreproduit(res.getInt(res.getColumnIndex(OperationHelper.NBREPRODUIT)));
            operation.setRemise(res.getDouble(res.getColumnIndex(OperationHelper.REMISE)));
            operation.setCaisse_id(res.getLong(res.getColumnIndex(OperationHelper.CAISSE_ID)));
            operation.setPartenaire_id(res.getLong(res.getColumnIndex(OperationHelper.PARTENAIRE_ID)));
            operation.setCommercialid(res.getLong(res.getColumnIndex(OperationHelper.COMMERCIAL_ID)));
            operation.setTypeOperation_id(res.getString(res.getColumnIndex(OperationHelper.TYPEOPERATION_ID)));
            operation.setComptebanque_id(res.getLong(res.getColumnIndex(OperationHelper.COMPTEBANQUE_ID)));
            operation.setOperation_id(res.getLong(res.getColumnIndex(OperationHelper.OPERATION_ID)));
            operation.setEtat(res.getInt(res.getColumnIndex(OperationHelper.ETAT)));
            operation.setNumcheque(res.getString(res.getColumnIndex(OperationHelper.NUMCHEQUE)));
            operation.setModepayement(res.getString(res.getColumnIndex(OperationHelper.MODEPAYEMENT)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setAnnuler(res.getInt(res.getColumnIndex(OperationHelper.ANNULER)));
            operation.setEntree(res.getInt(res.getColumnIndex(OperationHelper.ENTREE)));
            operation.setAttente(res.getInt(res.getColumnIndex(OperationHelper.ATTENTE)));
            operation.setRecu(res.getDouble(res.getColumnIndex(OperationHelper.RECU)));
            operation.setClient(res.getString(res.getColumnIndex(OperationHelper.CLIENT)));
            operation.setDescription(res.getString(res.getColumnIndex(OperationHelper.DESCRIPTION)));
            operation.setPayer(res.getInt(res.getColumnIndex(OperationHelper.PAYER)));
            try {
                operation.setDateecheance(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ECHEANCE))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setDateoperation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_OPERATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setDateannulation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ANNULATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        res.close();

        return operation ;
    }


    public Operation getOneExterne(long id) {
        Operation operation = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ OperationHelper.TABLE_NAME +" where "  + OperationHelper.ID_EXTERNE  +  "="+id+"", null );
        if (res.moveToFirst()){
            operation = new Operation();
            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.TABLE_KEY)));
            operation.setId_externe(res.getInt(res.getColumnIndex(OperationHelper.ID_EXTERNE)));
            operation.setMontant(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setMontant_ammor(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT_AMORTI)));
            operation.setNbreproduit(res.getInt(res.getColumnIndex(OperationHelper.NBREPRODUIT)));
            operation.setRemise(res.getDouble(res.getColumnIndex(OperationHelper.REMISE)));
            operation.setCaisse_id(res.getLong(res.getColumnIndex(OperationHelper.CAISSE_ID)));
            operation.setPartenaire_id(res.getLong(res.getColumnIndex(OperationHelper.PARTENAIRE_ID)));
            operation.setCommercialid(res.getLong(res.getColumnIndex(OperationHelper.COMMERCIAL_ID)));
            operation.setTypeOperation_id(res.getString(res.getColumnIndex(OperationHelper.TYPEOPERATION_ID)));
            operation.setComptebanque_id(res.getLong(res.getColumnIndex(OperationHelper.COMPTEBANQUE_ID)));
            operation.setOperation_id(res.getLong(res.getColumnIndex(OperationHelper.OPERATION_ID)));
            operation.setEtat(res.getInt(res.getColumnIndex(OperationHelper.ETAT)));
            operation.setNumcheque(res.getString(res.getColumnIndex(OperationHelper.NUMCHEQUE)));
            operation.setModepayement(res.getString(res.getColumnIndex(OperationHelper.MODEPAYEMENT)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setAnnuler(res.getInt(res.getColumnIndex(OperationHelper.ANNULER)));
            operation.setEntree(res.getInt(res.getColumnIndex(OperationHelper.ENTREE)));
            operation.setAttente(res.getInt(res.getColumnIndex(OperationHelper.ATTENTE)));
            operation.setRecu(res.getDouble(res.getColumnIndex(OperationHelper.RECU)));
            operation.setClient(res.getString(res.getColumnIndex(OperationHelper.CLIENT)));
            operation.setDescription(res.getString(res.getColumnIndex(OperationHelper.DESCRIPTION)));
            operation.setPayer(res.getInt(res.getColumnIndex(OperationHelper.PAYER)));
            try {
                operation.setDateecheance(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ECHEANCE))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setDateoperation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_OPERATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setDateannulation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ANNULATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        res.close();

        return operation ;
    }

    public Operation getLast() {
        Operation operation = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ OperationHelper.TABLE_NAME +" order by " + OperationHelper.TABLE_KEY + " desc", null );
        if (res.moveToFirst()){
            operation = new Operation();
            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.TABLE_KEY)));
            operation.setId_externe(res.getLong(res.getColumnIndex(OperationHelper.ID_EXTERNE)));
            operation.setMontant(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setMontant_ammor(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT_AMORTI)));
            operation.setNbreproduit(res.getInt(res.getColumnIndex(OperationHelper.NBREPRODUIT)));
            operation.setRemise(res.getDouble(res.getColumnIndex(OperationHelper.REMISE)));
            operation.setAnnuler(res.getInt(res.getColumnIndex(OperationHelper.ANNULER)));
            operation.setCaisse_id(res.getLong(res.getColumnIndex(OperationHelper.CAISSE_ID)));
            operation.setPartenaire_id(res.getLong(res.getColumnIndex(OperationHelper.PARTENAIRE_ID)));
            operation.setCommercialid(res.getLong(res.getColumnIndex(OperationHelper.COMMERCIAL_ID)));
            operation.setTypeOperation_id(res.getString(res.getColumnIndex(OperationHelper.TYPEOPERATION_ID)));
            operation.setComptebanque_id(res.getLong(res.getColumnIndex(OperationHelper.COMPTEBANQUE_ID)));
            operation.setOperation_id(res.getLong(res.getColumnIndex(OperationHelper.OPERATION_ID)));
            operation.setEtat(res.getInt(res.getColumnIndex(OperationHelper.ETAT)));
            operation.setNumcheque(res.getString(res.getColumnIndex(OperationHelper.NUMCHEQUE)));
            operation.setModepayement(res.getString(res.getColumnIndex(OperationHelper.MODEPAYEMENT)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setRecu(res.getDouble(res.getColumnIndex(OperationHelper.RECU)));
            operation.setEntree(res.getInt(res.getColumnIndex(OperationHelper.ENTREE)));
            operation.setAttente(res.getInt(res.getColumnIndex(OperationHelper.ATTENTE)));
            operation.setClient(res.getString(res.getColumnIndex(OperationHelper.CLIENT)));
            operation.setDescription(res.getString(res.getColumnIndex(OperationHelper.DESCRIPTION)));
            operation.setPayer(res.getInt(res.getColumnIndex(OperationHelper.PAYER)));
            try {
                operation.setDateecheance(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ECHEANCE))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setDateoperation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_OPERATION))));
                operation.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.CREATED_AT))));
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            try {
                operation.setDateannulation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ANNULATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        res.close();
        return operation ;
    }

    @Override
    public ArrayList<Operation> getAll() {
        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;

        Cursor res =  mDb.rawQuery( "select * from "+OperationHelper.TABLE_NAME + " order by  " + OperationHelper.ID_EXTERNE + " desc", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operation = new Operation();


            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.TABLE_KEY)));
            operation.setId_externe(res.getLong(res.getColumnIndex(OperationHelper.ID_EXTERNE)));
            operation.setMontant(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setMontant_ammor(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT_AMORTI)));
            operation.setNbreproduit(res.getInt(res.getColumnIndex(OperationHelper.NBREPRODUIT)));
            operation.setRemise(res.getDouble(res.getColumnIndex(OperationHelper.REMISE)));
            operation.setAnnuler(res.getInt(res.getColumnIndex(OperationHelper.ANNULER)));
            operation.setCaisse_id(res.getLong(res.getColumnIndex(OperationHelper.CAISSE_ID)));
            operation.setPartenaire_id(res.getLong(res.getColumnIndex(OperationHelper.PARTENAIRE_ID)));
            operation.setCommercialid(res.getLong(res.getColumnIndex(OperationHelper.COMMERCIAL_ID)));
            operation.setTypeOperation_id(res.getString(res.getColumnIndex(OperationHelper.TYPEOPERATION_ID)));
            operation.setComptebanque_id(res.getLong(res.getColumnIndex(OperationHelper.COMPTEBANQUE_ID)));
            operation.setOperation_id(res.getLong(res.getColumnIndex(OperationHelper.OPERATION_ID)));
            operation.setEtat(res.getInt(res.getColumnIndex(OperationHelper.ETAT)));
            operation.setNumcheque(res.getString(res.getColumnIndex(OperationHelper.NUMCHEQUE)));
            operation.setModepayement(res.getString(res.getColumnIndex(OperationHelper.MODEPAYEMENT)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setRecu(res.getDouble(res.getColumnIndex(OperationHelper.RECU)));
            operation.setEntree(res.getInt(res.getColumnIndex(OperationHelper.ENTREE)));
            operation.setAttente(res.getInt(res.getColumnIndex(OperationHelper.ATTENTE)));
            operation.setClient(res.getString(res.getColumnIndex(OperationHelper.CLIENT)));
            operation.setDescription(res.getString(res.getColumnIndex(OperationHelper.DESCRIPTION)));
            operation.setPayer(res.getInt(res.getColumnIndex(OperationHelper.PAYER)));
            try {
                operation.setDateecheance(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ECHEANCE))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setDateoperation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_OPERATION))));
             } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                operation.setDateannulation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ANNULATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            operations.add(operation);
            res.moveToNext();
        }
        res.close();
        return operations;
    }



    public ArrayList<Operation> getReglement() {
        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;

        Cursor res =  mDb.rawQuery( "select * from "+OperationHelper.TABLE_NAME + " where " + OperationHelper.OPERATION_ID  + " > 0 order by " + OperationHelper.DATE_OPERATION + " desc", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operation = new Operation();


            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.TABLE_KEY)));
            operation.setId_externe(res.getLong(res.getColumnIndex(OperationHelper.ID_EXTERNE)));
            operation.setMontant(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setMontant_ammor(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT_AMORTI)));
            operation.setNbreproduit(res.getInt(res.getColumnIndex(OperationHelper.NBREPRODUIT)));
            operation.setRemise(res.getDouble(res.getColumnIndex(OperationHelper.REMISE)));
            operation.setAnnuler(res.getInt(res.getColumnIndex(OperationHelper.ANNULER)));
            operation.setCaisse_id(res.getLong(res.getColumnIndex(OperationHelper.CAISSE_ID)));
            operation.setPartenaire_id(res.getLong(res.getColumnIndex(OperationHelper.PARTENAIRE_ID)));
            operation.setCommercialid(res.getLong(res.getColumnIndex(OperationHelper.COMMERCIAL_ID)));
            operation.setTypeOperation_id(res.getString(res.getColumnIndex(OperationHelper.TYPEOPERATION_ID)));
            operation.setComptebanque_id(res.getLong(res.getColumnIndex(OperationHelper.COMPTEBANQUE_ID)));
            operation.setOperation_id(res.getLong(res.getColumnIndex(OperationHelper.OPERATION_ID)));
            operation.setEtat(res.getInt(res.getColumnIndex(OperationHelper.ETAT)));
            operation.setNumcheque(res.getString(res.getColumnIndex(OperationHelper.NUMCHEQUE)));
            operation.setModepayement(res.getString(res.getColumnIndex(OperationHelper.MODEPAYEMENT)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setRecu(res.getDouble(res.getColumnIndex(OperationHelper.RECU)));
            operation.setEntree(res.getInt(res.getColumnIndex(OperationHelper.ENTREE)));
            operation.setAttente(res.getInt(res.getColumnIndex(OperationHelper.ATTENTE)));
            operation.setClient(res.getString(res.getColumnIndex(OperationHelper.CLIENT)));
            operation.setDescription(res.getString(res.getColumnIndex(OperationHelper.DESCRIPTION)));
            operation.setPayer(res.getInt(res.getColumnIndex(OperationHelper.PAYER)));
            try {
                operation.setDateecheance(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ECHEANCE))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setDateoperation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_OPERATION))));
             } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                operation.setDateannulation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ANNULATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            operations.add(operation);
            res.moveToNext();
        }
        res.close();
        return operations;
    }



    public ArrayList<Operation> getCredit() {
        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;

        Cursor res =  mDb.rawQuery( "select * from "+OperationHelper.TABLE_NAME + " WHERE " + OperationHelper.PAYER + " = 0 order by " + OperationHelper.DATE_OPERATION + "  desc", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operation = new Operation();


            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.TABLE_KEY)));
            operation.setId_externe(res.getLong(res.getColumnIndex(OperationHelper.ID_EXTERNE)));
            operation.setMontant(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setMontant_ammor(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT_AMORTI)));
            operation.setNbreproduit(res.getInt(res.getColumnIndex(OperationHelper.NBREPRODUIT)));
            operation.setRemise(res.getDouble(res.getColumnIndex(OperationHelper.REMISE)));
            operation.setAnnuler(res.getInt(res.getColumnIndex(OperationHelper.ANNULER)));
            operation.setCaisse_id(res.getLong(res.getColumnIndex(OperationHelper.CAISSE_ID)));
            operation.setPartenaire_id(res.getLong(res.getColumnIndex(OperationHelper.PARTENAIRE_ID)));
            operation.setCommercialid(res.getLong(res.getColumnIndex(OperationHelper.COMMERCIAL_ID)));
            operation.setTypeOperation_id(res.getString(res.getColumnIndex(OperationHelper.TYPEOPERATION_ID)));
            operation.setComptebanque_id(res.getLong(res.getColumnIndex(OperationHelper.COMPTEBANQUE_ID)));
            operation.setOperation_id(res.getLong(res.getColumnIndex(OperationHelper.OPERATION_ID)));
            operation.setEtat(res.getInt(res.getColumnIndex(OperationHelper.ETAT)));
            operation.setNumcheque(res.getString(res.getColumnIndex(OperationHelper.NUMCHEQUE)));
            operation.setModepayement(res.getString(res.getColumnIndex(OperationHelper.MODEPAYEMENT)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setRecu(res.getDouble(res.getColumnIndex(OperationHelper.RECU)));
            operation.setEntree(res.getInt(res.getColumnIndex(OperationHelper.ENTREE)));
            operation.setAttente(res.getInt(res.getColumnIndex(OperationHelper.ATTENTE)));
            operation.setClient(res.getString(res.getColumnIndex(OperationHelper.CLIENT)));
            operation.setDescription(res.getString(res.getColumnIndex(OperationHelper.DESCRIPTION)));
            operation.setPayer(res.getInt(res.getColumnIndex(OperationHelper.PAYER)));
            try {
                operation.setDateecheance(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ECHEANCE))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setDateoperation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_OPERATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                operation.setDateannulation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ANNULATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            operations.add(operation);
            res.moveToNext();
        }
        res.close();
        return operations;
    }




    public ArrayList<Operation> getAllByCaisse(long id) {
        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;

        Cursor res =  mDb.rawQuery( "select * from "+OperationHelper.TABLE_NAME + " where " + OperationHelper.CAISSE_ID + " = " + id + " order by " + OperationHelper.DATE_OPERATION + " desc", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operation = new Operation();


            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.TABLE_KEY)));
            operation.setId_externe(res.getLong(res.getColumnIndex(OperationHelper.ID_EXTERNE)));
            operation.setMontant(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setMontant_ammor(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT_AMORTI)));
            operation.setNbreproduit(res.getInt(res.getColumnIndex(OperationHelper.NBREPRODUIT)));
            operation.setRemise(res.getDouble(res.getColumnIndex(OperationHelper.REMISE)));
            operation.setAnnuler(res.getInt(res.getColumnIndex(OperationHelper.ANNULER)));
            operation.setCaisse_id(res.getLong(res.getColumnIndex(OperationHelper.CAISSE_ID)));
            operation.setPartenaire_id(res.getLong(res.getColumnIndex(OperationHelper.PARTENAIRE_ID)));
            operation.setCommercialid(res.getLong(res.getColumnIndex(OperationHelper.COMMERCIAL_ID)));
            operation.setTypeOperation_id(res.getString(res.getColumnIndex(OperationHelper.TYPEOPERATION_ID)));
            operation.setComptebanque_id(res.getLong(res.getColumnIndex(OperationHelper.COMPTEBANQUE_ID)));
            operation.setOperation_id(res.getLong(res.getColumnIndex(OperationHelper.OPERATION_ID)));
            operation.setNumcheque(res.getString(res.getColumnIndex(OperationHelper.NUMCHEQUE)));
            operation.setModepayement(res.getString(res.getColumnIndex(OperationHelper.MODEPAYEMENT)));
            operation.setEtat(res.getInt(res.getColumnIndex(OperationHelper.ETAT)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setRecu(res.getDouble(res.getColumnIndex(OperationHelper.RECU)));
            operation.setEntree(res.getInt(res.getColumnIndex(OperationHelper.ENTREE)));
            operation.setAttente(res.getInt(res.getColumnIndex(OperationHelper.ATTENTE)));
            operation.setClient(res.getString(res.getColumnIndex(OperationHelper.CLIENT)));
            operation.setDescription(res.getString(res.getColumnIndex(OperationHelper.DESCRIPTION)));
            operation.setPayer(res.getInt(res.getColumnIndex(OperationHelper.PAYER)));
            try {
                operation.setDateecheance(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ECHEANCE))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setDateoperation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_OPERATION))));
             } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                operation.setDateannulation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ANNULATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            operations.add(operation);
            res.moveToNext();
        }
        res.close();
        return operations;
    }



    public ArrayList<Operation> getAllByCommercial(long id) {
        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;

        Cursor res =  mDb.rawQuery( "select * from "+OperationHelper.TABLE_NAME + " where " + OperationHelper.COMMERCIAL_ID + " = " + id, null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operation = new Operation();


            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.TABLE_KEY)));
            operation.setId_externe(res.getLong(res.getColumnIndex(OperationHelper.ID_EXTERNE)));
            operation.setMontant(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setMontant_ammor(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT_AMORTI)));
            operation.setNbreproduit(res.getInt(res.getColumnIndex(OperationHelper.NBREPRODUIT)));
            operation.setRemise(res.getDouble(res.getColumnIndex(OperationHelper.REMISE)));
            operation.setAnnuler(res.getInt(res.getColumnIndex(OperationHelper.ANNULER)));
            operation.setCaisse_id(res.getLong(res.getColumnIndex(OperationHelper.CAISSE_ID)));
            operation.setPartenaire_id(res.getLong(res.getColumnIndex(OperationHelper.PARTENAIRE_ID)));
            operation.setCommercialid(res.getLong(res.getColumnIndex(OperationHelper.COMMERCIAL_ID)));
            operation.setTypeOperation_id(res.getString(res.getColumnIndex(OperationHelper.TYPEOPERATION_ID)));
            operation.setComptebanque_id(res.getLong(res.getColumnIndex(OperationHelper.COMPTEBANQUE_ID)));
            operation.setOperation_id(res.getLong(res.getColumnIndex(OperationHelper.OPERATION_ID)));
            operation.setNumcheque(res.getString(res.getColumnIndex(OperationHelper.NUMCHEQUE)));
            operation.setModepayement(res.getString(res.getColumnIndex(OperationHelper.MODEPAYEMENT)));
            operation.setEtat(res.getInt(res.getColumnIndex(OperationHelper.ETAT)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setRecu(res.getDouble(res.getColumnIndex(OperationHelper.RECU)));
            operation.setEntree(res.getInt(res.getColumnIndex(OperationHelper.ENTREE)));
            operation.setAttente(res.getInt(res.getColumnIndex(OperationHelper.ATTENTE)));
            operation.setClient(res.getString(res.getColumnIndex(OperationHelper.CLIENT)));
            operation.setDescription(res.getString(res.getColumnIndex(OperationHelper.DESCRIPTION)));
            operation.setPayer(res.getInt(res.getColumnIndex(OperationHelper.PAYER)));
            try {
                operation.setDateecheance(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ECHEANCE))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setDateoperation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_OPERATION))));
             } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                operation.setDateannulation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ANNULATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            operations.add(operation);
            res.moveToNext();
        }
        res.close();
        return operations;
    }



    public ArrayList<Operation> getAllByPv(long pointvente) {
        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;

        CaisseDAO caisseDAO = new CaisseDAO(mContext) ;
        Cursor res =  mDb.rawQuery( "select * from "+OperationHelper.TABLE_NAME + " order by " + OperationHelper.DATE_OPERATION + " asc", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operation = new Operation();


            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.TABLE_KEY)));
            operation.setId_externe(res.getLong(res.getColumnIndex(OperationHelper.ID_EXTERNE)));
            operation.setMontant(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setMontant_ammor(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT_AMORTI)));
            operation.setNbreproduit(res.getInt(res.getColumnIndex(OperationHelper.NBREPRODUIT)));
            operation.setRemise(res.getDouble(res.getColumnIndex(OperationHelper.REMISE)));
            operation.setAnnuler(res.getInt(res.getColumnIndex(OperationHelper.ANNULER)));
            operation.setCaisse_id(res.getLong(res.getColumnIndex(OperationHelper.CAISSE_ID)));
            operation.setPartenaire_id(res.getLong(res.getColumnIndex(OperationHelper.PARTENAIRE_ID)));
            operation.setCommercialid(res.getLong(res.getColumnIndex(OperationHelper.COMMERCIAL_ID)));
            operation.setTypeOperation_id(res.getString(res.getColumnIndex(OperationHelper.TYPEOPERATION_ID)));
            operation.setComptebanque_id(res.getLong(res.getColumnIndex(OperationHelper.COMPTEBANQUE_ID)));
            operation.setOperation_id(res.getLong(res.getColumnIndex(OperationHelper.OPERATION_ID)));
            operation.setNumcheque(res.getString(res.getColumnIndex(OperationHelper.NUMCHEQUE)));
            operation.setModepayement(res.getString(res.getColumnIndex(OperationHelper.MODEPAYEMENT)));
            operation.setEtat(res.getInt(res.getColumnIndex(OperationHelper.ETAT)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setRecu(res.getDouble(res.getColumnIndex(OperationHelper.RECU)));
            operation.setEntree(res.getInt(res.getColumnIndex(OperationHelper.ENTREE)));
            operation.setAttente(res.getInt(res.getColumnIndex(OperationHelper.ATTENTE)));
            operation.setClient(res.getString(res.getColumnIndex(OperationHelper.CLIENT)));
            operation.setDescription(res.getString(res.getColumnIndex(OperationHelper.DESCRIPTION)));
            operation.setPayer(res.getInt(res.getColumnIndex(OperationHelper.PAYER)));
            try {
                operation.setDateecheance(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ECHEANCE))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setDateoperation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_OPERATION))));
             } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                operation.setDateannulation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ANNULATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }



            if (pointvente!=0){
                Caisse caisse = caisseDAO.getOne(operation.getCaisse_id()) ;
                if (caisse.getPointVente_id() == pointvente)  operations.add(operation);
                Log.e("CAISSE", String.valueOf(caisse.getPointVente_id())) ;
                Log.e("POINT VENTE", String.valueOf(pointvente)) ;
            }
            else  operations.add(operation);

            res.moveToNext();
        }
        res.close();
        return operations;
    }



    public ArrayList<Operation> getOperationByPv(long id, Date dateDebut, Date dateFin) {

        if (dateDebut==null) dateDebut=new Date("01/01/2015");
        if (dateFin==null) dateFin=new Date() ;

        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;
        String rqte ="select o.* from " + OperationHelper.TABLE_NAME + " o, " + CaisseHelper.TABLE_NAME + " c, " + PointVenteHelper.TABLE_NAME + " pv  where o." + OperationHelper.CAISSE_ID + " = c." + CaisseHelper.TABLE_KEY + " and pv." + PointVenteHelper.TABLE_KEY + " = c." + CaisseHelper.POINTVENTE_ID + " and pv." + PointVenteHelper.TABLE_KEY + " = " + id + " and o."+OperationHelper.DATE_OPERATION + "  BETWEEN '" + formatter2.format(dateDebut) + " 00:00:00' AND '" + formatter2.format(dateFin) + " 23:59:59' order by o._id desc " ;
        Log.e("RQT",rqte) ;
        Cursor res =  mDb.rawQuery(rqte, null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operation = new Operation();

            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.TABLE_KEY)));
            operation.setId_externe(res.getLong(res.getColumnIndex(OperationHelper.ID_EXTERNE)));
            operation.setMontant(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setMontant_ammor(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT_AMORTI)));
            operation.setNbreproduit(res.getInt(res.getColumnIndex(OperationHelper.NBREPRODUIT)));
            operation.setRemise(res.getDouble(res.getColumnIndex(OperationHelper.REMISE)));
            operation.setAnnuler(res.getInt(res.getColumnIndex(OperationHelper.ANNULER)));
            operation.setCaisse_id(res.getLong(res.getColumnIndex(OperationHelper.CAISSE_ID)));
            operation.setPartenaire_id(res.getLong(res.getColumnIndex(OperationHelper.PARTENAIRE_ID)));
            operation.setCommercialid(res.getLong(res.getColumnIndex(OperationHelper.COMMERCIAL_ID)));
            operation.setTypeOperation_id(res.getString(res.getColumnIndex(OperationHelper.TYPEOPERATION_ID)));
            operation.setComptebanque_id(res.getLong(res.getColumnIndex(OperationHelper.COMPTEBANQUE_ID)));
            operation.setOperation_id(res.getLong(res.getColumnIndex(OperationHelper.OPERATION_ID)));
            operation.setNumcheque(res.getString(res.getColumnIndex(OperationHelper.NUMCHEQUE)));
            operation.setModepayement(res.getString(res.getColumnIndex(OperationHelper.MODEPAYEMENT)));
            operation.setEtat(res.getInt(res.getColumnIndex(OperationHelper.ETAT)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setRecu(res.getDouble(res.getColumnIndex(OperationHelper.RECU)));
            operation.setEntree(res.getInt(res.getColumnIndex(OperationHelper.ENTREE)));
            operation.setAttente(res.getInt(res.getColumnIndex(OperationHelper.ATTENTE)));
            operation.setClient(res.getString(res.getColumnIndex(OperationHelper.CLIENT)));
            operation.setDescription(res.getString(res.getColumnIndex(OperationHelper.DESCRIPTION)));
            operation.setPayer(res.getInt(res.getColumnIndex(OperationHelper.PAYER)));
            try {
                operation.setDateecheance(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ECHEANCE))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setDateoperation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_OPERATION))));
             } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                operation.setDateannulation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ANNULATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            operations.add(operation);
            res.moveToNext();
        }
        res.close();
        return operations;
    }


    public ArrayList<Operation> getManyByPartenaire(long id) {
        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;

        Cursor res =  mDb.rawQuery( "select * from "+OperationHelper.TABLE_NAME + " where " + OperationHelper.PARTENAIRE_ID + " = " + id, null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operation = new Operation();


            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.TABLE_KEY)));
            operation.setId_externe(res.getLong(res.getColumnIndex(OperationHelper.ID_EXTERNE)));
            operation.setMontant(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setMontant_ammor(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT_AMORTI)));
            operation.setNbreproduit(res.getInt(res.getColumnIndex(OperationHelper.NBREPRODUIT)));
            operation.setRemise(res.getDouble(res.getColumnIndex(OperationHelper.REMISE)));
            operation.setAnnuler(res.getInt(res.getColumnIndex(OperationHelper.ANNULER)));
            operation.setCaisse_id(res.getLong(res.getColumnIndex(OperationHelper.CAISSE_ID)));
            operation.setPartenaire_id(res.getLong(res.getColumnIndex(OperationHelper.PARTENAIRE_ID)));
            operation.setCommercialid(res.getLong(res.getColumnIndex(OperationHelper.COMMERCIAL_ID)));
            operation.setTypeOperation_id(res.getString(res.getColumnIndex(OperationHelper.TYPEOPERATION_ID)));
            operation.setComptebanque_id(res.getLong(res.getColumnIndex(OperationHelper.COMPTEBANQUE_ID)));
            operation.setOperation_id(res.getLong(res.getColumnIndex(OperationHelper.OPERATION_ID)));
            operation.setEtat(res.getInt(res.getColumnIndex(OperationHelper.ETAT)));
            operation.setNumcheque(res.getString(res.getColumnIndex(OperationHelper.NUMCHEQUE)));
            operation.setModepayement(res.getString(res.getColumnIndex(OperationHelper.MODEPAYEMENT)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setRecu(res.getDouble(res.getColumnIndex(OperationHelper.RECU)));
            operation.setEntree(res.getInt(res.getColumnIndex(OperationHelper.ENTREE)));
            operation.setAttente(res.getInt(res.getColumnIndex(OperationHelper.ATTENTE)));
            operation.setClient(res.getString(res.getColumnIndex(OperationHelper.CLIENT)));
            operation.setDescription(res.getString(res.getColumnIndex(OperationHelper.DESCRIPTION)));
            operation.setPayer(res.getInt(res.getColumnIndex(OperationHelper.PAYER)));
            try {
                operation.setDateecheance(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ECHEANCE))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setDateoperation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_OPERATION))));
             } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                operation.setDateannulation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ANNULATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (operation.getPartenaire_id()!=0)operations.add(operation);
            res.moveToNext();
        }
        res.close();
        return operations;
    }



    public ArrayList<Operation> getManyByCommercial(long id) {
        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;

        Cursor res =  mDb.rawQuery( "select * from "+OperationHelper.TABLE_NAME + " where " + OperationHelper.COMMERCIAL_ID + " = " + id, null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operation = new Operation();


            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.TABLE_KEY)));
            operation.setId_externe(res.getLong(res.getColumnIndex(OperationHelper.ID_EXTERNE)));
            operation.setMontant(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setMontant_ammor(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT_AMORTI)));
            operation.setNbreproduit(res.getInt(res.getColumnIndex(OperationHelper.NBREPRODUIT)));
            operation.setRemise(res.getDouble(res.getColumnIndex(OperationHelper.REMISE)));
            operation.setAnnuler(res.getInt(res.getColumnIndex(OperationHelper.ANNULER)));
            operation.setCaisse_id(res.getLong(res.getColumnIndex(OperationHelper.CAISSE_ID)));
            operation.setPartenaire_id(res.getLong(res.getColumnIndex(OperationHelper.PARTENAIRE_ID)));
            operation.setCommercialid(res.getLong(res.getColumnIndex(OperationHelper.COMMERCIAL_ID)));
            operation.setTypeOperation_id(res.getString(res.getColumnIndex(OperationHelper.TYPEOPERATION_ID)));
            operation.setOperation_id(res.getLong(res.getColumnIndex(OperationHelper.OPERATION_ID)));
            operation.setComptebanque_id(res.getLong(res.getColumnIndex(OperationHelper.COMPTEBANQUE_ID)));
            operation.setEtat(res.getInt(res.getColumnIndex(OperationHelper.ETAT)));
            operation.setNumcheque(res.getString(res.getColumnIndex(OperationHelper.NUMCHEQUE)));
            operation.setModepayement(res.getString(res.getColumnIndex(OperationHelper.MODEPAYEMENT)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setRecu(res.getDouble(res.getColumnIndex(OperationHelper.RECU)));
            operation.setEntree(res.getInt(res.getColumnIndex(OperationHelper.ENTREE)));
            operation.setAttente(res.getInt(res.getColumnIndex(OperationHelper.ATTENTE)));
            operation.setClient(res.getString(res.getColumnIndex(OperationHelper.CLIENT)));
            operation.setDescription(res.getString(res.getColumnIndex(OperationHelper.DESCRIPTION)));
            operation.setPayer(res.getInt(res.getColumnIndex(OperationHelper.PAYER)));
            try {
                operation.setDateecheance(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ECHEANCE))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setDateoperation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_OPERATION))));
             } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                operation.setDateannulation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ANNULATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (operation.getPartenaire_id()!=0)operations.add(operation);
            res.moveToNext();
        }
        res.close();
        return operations;
    }





    public ArrayList<Operation> getManyByOperation(long id) {
        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;

        Cursor res =  mDb.rawQuery( "select * from "+OperationHelper.TABLE_NAME + " where " + OperationHelper.CAISSE_ID + " = " + id, null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operation = new Operation();


            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.TABLE_KEY)));
            operation.setId_externe(res.getLong(res.getColumnIndex(OperationHelper.ID_EXTERNE)));
            operation.setMontant(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setMontant_ammor(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT_AMORTI)));
            operation.setNbreproduit(res.getInt(res.getColumnIndex(OperationHelper.NBREPRODUIT)));
            operation.setRemise(res.getDouble(res.getColumnIndex(OperationHelper.REMISE)));
            operation.setAnnuler(res.getInt(res.getColumnIndex(OperationHelper.ANNULER)));
            operation.setCaisse_id(res.getLong(res.getColumnIndex(OperationHelper.CAISSE_ID)));
            operation.setPartenaire_id(res.getLong(res.getColumnIndex(OperationHelper.PARTENAIRE_ID)));
            operation.setCommercialid(res.getLong(res.getColumnIndex(OperationHelper.COMMERCIAL_ID)));
            operation.setTypeOperation_id(res.getString(res.getColumnIndex(OperationHelper.TYPEOPERATION_ID)));
            operation.setComptebanque_id(res.getLong(res.getColumnIndex(OperationHelper.COMPTEBANQUE_ID)));
            operation.setOperation_id(res.getLong(res.getColumnIndex(OperationHelper.OPERATION_ID)));
            operation.setNumcheque(res.getString(res.getColumnIndex(OperationHelper.NUMCHEQUE)));
            operation.setModepayement(res.getString(res.getColumnIndex(OperationHelper.MODEPAYEMENT)));
            operation.setEtat(res.getInt(res.getColumnIndex(OperationHelper.ETAT)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setRecu(res.getDouble(res.getColumnIndex(OperationHelper.RECU)));
            operation.setEntree(res.getInt(res.getColumnIndex(OperationHelper.ENTREE)));
            operation.setAttente(res.getInt(res.getColumnIndex(OperationHelper.ATTENTE)));
            operation.setClient(res.getString(res.getColumnIndex(OperationHelper.CLIENT)));
            operation.setDescription(res.getString(res.getColumnIndex(OperationHelper.DESCRIPTION)));
            operation.setPayer(res.getInt(res.getColumnIndex(OperationHelper.PAYER)));
            try {
                operation.setDateecheance(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ECHEANCE))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setDateoperation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_OPERATION))));
             } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                operation.setDateannulation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ANNULATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (operation.getPartenaire_id()!=0)operations.add(operation);
            res.moveToNext();
        }
        res.close();
        return operations;
    }

    public ArrayList<Operation> getInterval(Date dateDebut, Date dateFin) {
        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;

        Log.i("Debug","SELECT * FROM "+OperationHelper.TABLE_NAME + " WHERE "+OperationHelper.DATE_OPERATION + "  BETWEEN '" + formatter2.format(dateFin) + "' AND '" + formatter2.format(dateDebut) + "' ORDER BY _id DESC ") ;

        Cursor res =  mDb.rawQuery( "SELECT * FROM "+OperationHelper.TABLE_NAME + " WHERE "+OperationHelper.DATE_OPERATION + "  BETWEEN '" + formatter2.format(dateDebut) + "' AND '" + formatter2.format(dateFin) + " 23:59:00' ORDER BY _id DESC ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operation = new Operation();


            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.TABLE_KEY)));
            operation.setId_externe(res.getLong(res.getColumnIndex(OperationHelper.ID_EXTERNE)));
            operation.setMontant(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setMontant_ammor(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT_AMORTI)));
            operation.setNbreproduit(res.getInt(res.getColumnIndex(OperationHelper.NBREPRODUIT)));
            operation.setRemise(res.getDouble(res.getColumnIndex(OperationHelper.REMISE)));
            operation.setAnnuler(res.getInt(res.getColumnIndex(OperationHelper.ANNULER)));
            operation.setCaisse_id(res.getLong(res.getColumnIndex(OperationHelper.CAISSE_ID)));
            operation.setPartenaire_id(res.getLong(res.getColumnIndex(OperationHelper.PARTENAIRE_ID)));
            operation.setCommercialid(res.getLong(res.getColumnIndex(OperationHelper.COMMERCIAL_ID)));
            operation.setTypeOperation_id(res.getString(res.getColumnIndex(OperationHelper.TYPEOPERATION_ID)));
            operation.setComptebanque_id(res.getLong(res.getColumnIndex(OperationHelper.COMPTEBANQUE_ID)));
            operation.setOperation_id(res.getLong(res.getColumnIndex(OperationHelper.OPERATION_ID)));
            operation.setEtat(res.getInt(res.getColumnIndex(OperationHelper.ETAT)));
            operation.setNumcheque(res.getString(res.getColumnIndex(OperationHelper.NUMCHEQUE)));
            operation.setModepayement(res.getString(res.getColumnIndex(OperationHelper.MODEPAYEMENT)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setRecu(res.getDouble(res.getColumnIndex(OperationHelper.RECU)));
            operation.setEntree(res.getInt(res.getColumnIndex(OperationHelper.ENTREE)));
            operation.setAttente(res.getInt(res.getColumnIndex(OperationHelper.ATTENTE)));
            operation.setClient(res.getString(res.getColumnIndex(OperationHelper.CLIENT)));
            operation.setDescription(res.getString(res.getColumnIndex(OperationHelper.DESCRIPTION)));
            operation.setPayer(res.getInt(res.getColumnIndex(OperationHelper.PAYER)));
            try {
                operation.setDateecheance(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ECHEANCE))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setDateoperation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_OPERATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                operation.setDateannulation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ANNULATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            operations.add(operation);
            res.moveToNext();
        }
        res.close();
        return operations;
    }

    public ArrayList<Operation> getIntervalNonSync(Date dateDebut, Date dateFin) {
        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;


        if (dateDebut==null) dateDebut=new Date("01/01/2015");
        if (dateFin==null) dateFin=new Date() ;


        Log.e("Debug","SELECT * FROM "+OperationHelper.TABLE_NAME + " WHERE " + OperationHelper.ETAT + " = 0 AND "+OperationHelper.DATE_OPERATION + "  BETWEEN '" + formatter.format(dateFin) + "' AND '" + formatter.format(dateDebut) + "'  ORDER BY _id DESC ") ;

        Cursor res =  mDb.rawQuery( "SELECT * FROM "+OperationHelper.TABLE_NAME + " WHERE " + OperationHelper.ETAT + " < 2 AND " + OperationHelper.DATE_OPERATION + "  BETWEEN '" + formatter.format(dateDebut) + "' AND '" + formatter.format(dateFin) + "' ORDER BY _id DESC ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operation = new Operation();


            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.TABLE_KEY)));
            operation.setId_externe(res.getLong(res.getColumnIndex(OperationHelper.ID_EXTERNE)));
            operation.setMontant(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setMontant_ammor(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT_AMORTI)));
            operation.setNbreproduit(res.getInt(res.getColumnIndex(OperationHelper.NBREPRODUIT)));
            operation.setRemise(res.getDouble(res.getColumnIndex(OperationHelper.REMISE)));
            operation.setAnnuler(res.getInt(res.getColumnIndex(OperationHelper.ANNULER)));
            operation.setCaisse_id(res.getLong(res.getColumnIndex(OperationHelper.CAISSE_ID)));
            operation.setPartenaire_id(res.getLong(res.getColumnIndex(OperationHelper.PARTENAIRE_ID)));
            operation.setCommercialid(res.getLong(res.getColumnIndex(OperationHelper.COMMERCIAL_ID)));
            operation.setTypeOperation_id(res.getString(res.getColumnIndex(OperationHelper.TYPEOPERATION_ID)));
            operation.setComptebanque_id(res.getLong(res.getColumnIndex(OperationHelper.COMPTEBANQUE_ID)));
            operation.setOperation_id(res.getLong(res.getColumnIndex(OperationHelper.OPERATION_ID)));
            operation.setEtat(res.getInt(res.getColumnIndex(OperationHelper.ETAT)));
            operation.setNumcheque(res.getString(res.getColumnIndex(OperationHelper.NUMCHEQUE)));
            operation.setModepayement(res.getString(res.getColumnIndex(OperationHelper.MODEPAYEMENT)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setRecu(res.getDouble(res.getColumnIndex(OperationHelper.RECU)));
            operation.setEntree(res.getInt(res.getColumnIndex(OperationHelper.ENTREE)));
            operation.setAttente(res.getInt(res.getColumnIndex(OperationHelper.ATTENTE)));
            operation.setClient(res.getString(res.getColumnIndex(OperationHelper.CLIENT)));
            operation.setDescription(res.getString(res.getColumnIndex(OperationHelper.DESCRIPTION)));
            operation.setPayer(res.getInt(res.getColumnIndex(OperationHelper.PAYER)));
            try {
                operation.setDateecheance(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ECHEANCE))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setDateoperation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_OPERATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                operation.setDateannulation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ANNULATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (operation.getAttente()==0)operations.add(operation);
            res.moveToNext();
        }
        res.close();
        return operations;
    }



    public ArrayList<Operation> getNonSync() {
        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;


        Log.e("Debug","SELECT * FROM "+OperationHelper.TABLE_NAME + " WHERE " + OperationHelper.ETAT + " < 2  ORDER BY _id DESC ") ;

        Cursor res =  mDb.rawQuery( "SELECT * FROM "+OperationHelper.TABLE_NAME + " WHERE " + OperationHelper.ETAT + " < 2  ORDER BY _id DESC ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operation = new Operation();


            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.TABLE_KEY)));
            operation.setId_externe(res.getLong(res.getColumnIndex(OperationHelper.ID_EXTERNE)));
            operation.setMontant(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setMontant_ammor(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT_AMORTI)));
            operation.setNbreproduit(res.getInt(res.getColumnIndex(OperationHelper.NBREPRODUIT)));
            operation.setRemise(res.getDouble(res.getColumnIndex(OperationHelper.REMISE)));
            operation.setAnnuler(res.getInt(res.getColumnIndex(OperationHelper.ANNULER)));
            operation.setCaisse_id(res.getLong(res.getColumnIndex(OperationHelper.CAISSE_ID)));
            operation.setPartenaire_id(res.getLong(res.getColumnIndex(OperationHelper.PARTENAIRE_ID)));
            operation.setCommercialid(res.getLong(res.getColumnIndex(OperationHelper.COMMERCIAL_ID)));
            operation.setTypeOperation_id(res.getString(res.getColumnIndex(OperationHelper.TYPEOPERATION_ID)));
            operation.setComptebanque_id(res.getLong(res.getColumnIndex(OperationHelper.COMPTEBANQUE_ID)));
            operation.setOperation_id(res.getLong(res.getColumnIndex(OperationHelper.OPERATION_ID)));
            operation.setNumcheque(res.getString(res.getColumnIndex(OperationHelper.NUMCHEQUE)));
            operation.setModepayement(res.getString(res.getColumnIndex(OperationHelper.MODEPAYEMENT)));
            operation.setEtat(res.getInt(res.getColumnIndex(OperationHelper.ETAT)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setRecu(res.getDouble(res.getColumnIndex(OperationHelper.RECU)));
            operation.setEntree(res.getInt(res.getColumnIndex(OperationHelper.ENTREE)));
            operation.setAttente(res.getInt(res.getColumnIndex(OperationHelper.ATTENTE)));
            operation.setClient(res.getString(res.getColumnIndex(OperationHelper.CLIENT)));
            operation.setDescription(res.getString(res.getColumnIndex(OperationHelper.DESCRIPTION)));
            operation.setPayer(res.getInt(res.getColumnIndex(OperationHelper.PAYER)));
            try {
                operation.setDateecheance(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ECHEANCE))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setDateoperation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_OPERATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                operation.setDateannulation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ANNULATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (operation.getAttente()==0) {
                if (!operation.getTypeOperation_id().startsWith(OperationDAO.CMD))operations.add(operation);
            }
            res.moveToNext();
        }
        res.close();
        return operations;
    }


    public int getTotal() {
        Cursor res =  mDb.rawQuery( "select * from "+OperationHelper.TABLE_NAME+ " WHERE " + OperationHelper.ETAT + " <> 2  order by id desc ", null);
        res.moveToFirst();
        int total = 0 ;

        while(res.isAfterLast() == false){
            total += res.getDouble(res.getColumnIndex(OperationHelper.MONTANT));

            res.moveToNext();
        }
        return total;
    }




    public ArrayList<Operation> getAll(int type,Date datedebut,Date datefin,long pointvente) {


        if (datedebut==null) datedebut=new Date("2015/01/01");
        if (datefin==null) datefin=new Date() ;

        CaisseDAO caisseDAO = new CaisseDAO(mContext) ;

        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;
        String rqte = "select * from " + OperationHelper.TABLE_NAME + " where "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59'  order by " + OperationHelper.DATE_OPERATION + " desc" ;
        if (type == 1) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + VENTE + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59' and " + OperationHelper.ATTENTE + " = 0 order by _id desc " ;
        else if (type == 2) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " LIKE '" + DEPENSE +"%' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59' order by _id desc " ;
        else if (type == 3) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID +" = '" + CMDFRNSS + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59' order by _id desc " ;
        else if (type == 4) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + CMDCLNT + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59' order by _id desc " ;
        else if (type == 5) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + ACHAT + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59' order by _id desc " ;
        else if (type == 6) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + VENTE + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59'  order by _id desc " ;
        else if (type == 7) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + PRODUIT_FIN + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59' order by _id desc " ;
        else if (type == 8) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + CH_FN + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59' order by _id desc " ;
        else if (type == 9) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + PRODUIT_EXC + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59' order by _id desc " ;
        else if (type == 10) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + CH_EXC + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59' order by _id desc " ;
        else if (type == 11) rqte ="select * from " + OperationHelper.TABLE_NAME + " where (" + OperationHelper.TYPEOPERATION_ID + " LIKE 'PD_%' OR " + OperationHelper.TYPEOPERATION_ID + " LIKE 'CH_%' ) and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59' order by _id desc " ;
        else if (type == 12) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + VENTE + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59'  order by _id desc " ;
        else if (type == 13) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + BQ + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59'  order by _id desc " ;
        else if (type == 14) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + IMMO_CHARGE + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59'  order by _id desc " ;
        else if (type == 15) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + IMMO_CORPORREL + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59'   order by _id desc " ;
        else if (type == 16) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + IMMO_INCORPOREL + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59'  order by _id desc " ;
        else if (type == 17) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + IMMO_FINANCIERE + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59'  order by _id desc " ;
        else if (type == 18) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + VENTE + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59'  and " + OperationHelper.PAYER + " = 0 order by _id desc " ;
        else if (type == 19) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + AUTRE_CRE + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59'  order by _id desc " ;
        else if (type == 20) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + PLACEMENT_GARANTIE + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59'  order by _id desc " ;
        else if (type == 21) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + PLACEMENT_TERME + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59'  order by _id desc " ;
        else if (type == 22) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + PLACEMENT_AUTRE + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59'  order by _id desc " ;
        else if (type == 23) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + CAPITAL + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59'  order by _id desc " ;
        else if (type == 24) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + EMPRUNT + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59'   and " + OperationHelper.PAYER + " = 0  order by _id desc " ;
        else if (type == 25) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + ACHAT + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59'  and " + OperationHelper.PAYER + " = 0 order by _id desc " ;
        else if (type == 26) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + AUTRE_DETTE + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59'  and " + OperationHelper.PAYER + " = 0 order by _id desc " ;
        else if (type == 27) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + REGLEMENT + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59'  order by _id desc " ;
        else if (type == 28) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + AVAN_FOUR + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + " 00:00:00' and '" + formatter2.format(datefin) +  " 23:59:59'  order by _id desc " ;


        Log.e("SQL", rqte) ;
        Log.e("TYPE", type+"") ;

        Cursor res =  mDb.rawQuery(rqte, null);
        res.moveToFirst();
        ArrayList<Caisse> caisses = caisseDAO.getAllById(pointvente);

        Log.e("SIZE", String.valueOf(res.getCount())) ;
        while(res.isAfterLast() == false){
            operation = new Operation();


            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.TABLE_KEY)));
            operation.setId_externe(res.getLong(res.getColumnIndex(OperationHelper.ID_EXTERNE)));
            operation.setMontant(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setMontant_ammor(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT_AMORTI)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setNbreproduit(res.getInt(res.getColumnIndex(OperationHelper.NBREPRODUIT)));
            operation.setRemise(res.getDouble(res.getColumnIndex(OperationHelper.REMISE)));
            operation.setCaisse_id(res.getLong(res.getColumnIndex(OperationHelper.CAISSE_ID)));
            operation.setPartenaire_id(res.getLong(res.getColumnIndex(OperationHelper.PARTENAIRE_ID)));
            operation.setCommercialid(res.getLong(res.getColumnIndex(OperationHelper.COMMERCIAL_ID)));
            operation.setTypeOperation_id(res.getString(res.getColumnIndex(OperationHelper.TYPEOPERATION_ID)));
            operation.setComptebanque_id(res.getLong(res.getColumnIndex(OperationHelper.COMPTEBANQUE_ID)));
            operation.setOperation_id(res.getLong(res.getColumnIndex(OperationHelper.OPERATION_ID)));
            operation.setEtat(res.getInt(res.getColumnIndex(OperationHelper.ETAT)));
            operation.setNumcheque(res.getString(res.getColumnIndex(OperationHelper.NUMCHEQUE)));
            operation.setModepayement(res.getString(res.getColumnIndex(OperationHelper.MODEPAYEMENT)));
            operation.setAnnuler(res.getInt(res.getColumnIndex(OperationHelper.ANNULER)));
            operation.setEntree(res.getInt(res.getColumnIndex(OperationHelper.ENTREE)));
            operation.setAttente(res.getInt(res.getColumnIndex(OperationHelper.ATTENTE)));
            operation.setRecu(res.getDouble(res.getColumnIndex(OperationHelper.RECU)));
            operation.setClient(res.getString(res.getColumnIndex(OperationHelper.CLIENT)));
            operation.setDescription(res.getString(res.getColumnIndex(OperationHelper.DESCRIPTION)));
            operation.setPayer(res.getInt(res.getColumnIndex(OperationHelper.PAYER)));

            Log.e("CREATED AT", res.getString(res.getColumnIndex(OperationHelper.CREATED_AT))) ;
            Log.e("DATE OPERATION", res.getString(res.getColumnIndex(OperationHelper.DATE_OPERATION))) ;
            try {
                operation.setDateecheance(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ECHEANCE))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setDateoperation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_OPERATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                operation.setDateannulation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ANNULATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }
/*
            if (type != 6)operations.add(operation);
            else if (type==6 && payementDAO.getMany(operation.getId_externe()).size()>0) operations.add(operation);
            else if (type==6 && operation.getPayer()==0) operations.add(operation);
            */
            Log.e("SIZE", String.valueOf(operation.getCaisse_id())) ;


            if (pointvente!=0){
                //Caisse caisse = caisseDAO.getOne(operation.getCaisse_id()) ;
                for (int i = 0; i < caisses.size(); i++) {
                    if (caisses.get(i).getId() == operation.getCaisse_id())  operations.add(operation);
                    Log.e("CAISSE", String.valueOf(caisses.get(i).getPointVente_id())) ;
                    Log.e("POINT VENTE", String.valueOf(pointvente)) ;
                }
            }
            else  operations.add(operation);

            res.moveToNext();
        }
        res.close();
        return operations;
    }





    public double getTotal(int type,Date datedebut,Date datefin) {


        if (datedebut==null) datedebut=new Date("01/01/2015");
        if (datefin==null) datefin=new Date() ;

        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;
        String rqte = "select * from " + OperationHelper.TABLE_NAME + " where "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + "' and '" + formatter2.format(datefin) +  " 23:59:59'  order by _id desc " ;
        if (type == 1) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + VENTE + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + "' and '" + formatter2.format(datefin) +  " 23:59:59' order by _id desc " ;
        else if (type == 2) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + " LIKE 'DEP_%' and " + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + "' and '" + formatter2.format(datefin) +  " 23:59:59' order by _id desc " ;
        else if (type == 3) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID +" = '" + CMDFRNSS + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + "' and '" + formatter2.format(datefin) +  " 23:59:59' order by _id desc " ;
        else if (type == 4) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + CMDCLNT + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + "' and '" + formatter2.format(datefin) +  " 23:59:59' order by _id desc " ;
        else if (type == 5) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + ACHAT + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + formatter2.format(datedebut) + "' and '" + formatter2.format(datefin) +  " 23:59:59' order by _id desc " ;

        Cursor res =  mDb.rawQuery(rqte, null);
        res.moveToFirst();
        double total = 0 ;

        while(res.isAfterLast() == false){
            operation = new Operation();


            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.TABLE_KEY)));
            operation.setId_externe(res.getLong(res.getColumnIndex(OperationHelper.ID_EXTERNE)));
            operation.setMontant(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setMontant_ammor(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT_AMORTI)));

            total += operation.getMontant() ;

            res.moveToNext();
        }
        res.close();
        return total;
    }




    public double getAll(int type,String mois,String annee) {
        String datedebut = null;
        String datefin = null;
        if (datedebut==null) datedebut=  annee+ "-"+ mois +"-01" ;
        if (datefin==null)  datefin= annee+ "-"+ mois +"-31" ;

        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;
        String rqte = "" ;
        //"select * from " + OperationHelper.TABLE_NAME + " where "  + OperationHelper.DATE_OPERATION +  " between '" + datedebut + "' and '" + datefin +  " 23:59:59'  order by id desc " ;
        if (type == 1) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + VENTE + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + datedebut + "' and '" + datefin+  " 23:59:59'  " ;
        else if (type == 2) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + " LIKE 'DEP_%' and " + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + datedebut + "' and '" + datefin +  " 23:59:59'  " ;
        else if (type == 3) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID +" = '" + CMDFRNSS + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + datedebut + "' and '" + datefin +  " 23:59:59'  " ;
        else if (type == 4) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + CMDCLNT + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + datedebut + "' and '" + datefin +  " 23:59:59'  " ;
        else if (type == 5) rqte ="select * from " + OperationHelper.TABLE_NAME + " where " + OperationHelper.TYPEOPERATION_ID + " = '" + ACHAT + "' and "  + OperationHelper.DATE_OPERATION +  " between '" + datedebut + "' and '" + datefin +  " 23:59:59'  " ;

        Log.e("SQL",rqte) ;
        Cursor res =  mDb.rawQuery(rqte, null);
        res.moveToFirst();

        Log.e("SIZE",""+res.getCount()) ;
        double total = 0 ;
        while(res.isAfterLast() == false){
            operation = new Operation();


            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.TABLE_KEY)));
            operation.setId_externe(res.getLong(res.getColumnIndex(OperationHelper.ID_EXTERNE)));
            operation.setMontant(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setMontant_ammor(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT_AMORTI)));
            operation.setNbreproduit(res.getInt(res.getColumnIndex(OperationHelper.NBREPRODUIT)));
            operation.setRemise(res.getDouble(res.getColumnIndex(OperationHelper.REMISE)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setCaisse_id(res.getLong(res.getColumnIndex(OperationHelper.CAISSE_ID)));
            operation.setPartenaire_id(res.getLong(res.getColumnIndex(OperationHelper.PARTENAIRE_ID)));
            operation.setCommercialid(res.getLong(res.getColumnIndex(OperationHelper.COMMERCIAL_ID)));
            operation.setTypeOperation_id(res.getString(res.getColumnIndex(OperationHelper.TYPEOPERATION_ID)));
            operation.setComptebanque_id(res.getLong(res.getColumnIndex(OperationHelper.COMPTEBANQUE_ID)));
            operation.setOperation_id(res.getLong(res.getColumnIndex(OperationHelper.OPERATION_ID)));
            operation.setEtat(res.getInt(res.getColumnIndex(OperationHelper.ETAT)));
            operation.setAnnuler(res.getInt(res.getColumnIndex(OperationHelper.ANNULER)));
            operation.setNumcheque(res.getString(res.getColumnIndex(OperationHelper.NUMCHEQUE)));
            operation.setModepayement(res.getString(res.getColumnIndex(OperationHelper.MODEPAYEMENT)));
            operation.setRecu(res.getDouble(res.getColumnIndex(OperationHelper.RECU)));
            operation.setEntree(res.getInt(res.getColumnIndex(OperationHelper.ENTREE)));
            operation.setAttente(res.getInt(res.getColumnIndex(OperationHelper.ATTENTE)));
            operation.setClient(res.getString(res.getColumnIndex(OperationHelper.CLIENT)));
            operation.setDescription(res.getString(res.getColumnIndex(OperationHelper.DESCRIPTION)));
            operation.setPayer(res.getInt(res.getColumnIndex(OperationHelper.PAYER)));
            try {
                operation.setDateecheance(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ECHEANCE))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setDateoperation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_OPERATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                operation.setDateannulation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ANNULATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            operations.add(operation);
            total += operation.getMontant() ;
            res.moveToNext();
        }
        res.close();
        return total;
    }


    public ArrayList<Produit> getByProduitInterval(Date dateDebut, Date dateFin) {
        ArrayList<Produit> produits = new ArrayList<Produit>();
        ArrayList<Mouvement> mouvements = new ArrayList<Mouvement>();
        Mouvement mouvement = null ;
        ProduitDAO dao = new ProduitDAO(mContext) ;

        Log.e("DEBUG","select * from "+ MouvementHelper.TABLE_NAME + " Where  " + MouvementHelper.ETAT + " <> 2 ") ;
        Cursor res =  mDb.rawQuery( "select * from "+MouvementHelper.TABLE_NAME + " Where  " + MouvementHelper.CREATED_AT + "  BETWEEN '" + formatter.format(dateFin) + "' AND '" + formatter.format(dateDebut) + "'", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            mouvement = new Mouvement();


            mouvement.setId(res.getLong(res.getColumnIndex(MouvementHelper.TABLE_KEY)));
            mouvement.setPrixV(res.getDouble(res.getColumnIndex(MouvementHelper.PRIXV)));
            mouvement.setPrixA(res.getDouble(res.getColumnIndex(MouvementHelper.PRIXA)));
            mouvement.setEntree(res.getInt(res.getColumnIndex(MouvementHelper.ENTREE)));
            mouvement.setQuantite(res.getInt(res.getColumnIndex(MouvementHelper.QUANTITE)));
            mouvement.setProduit_id(res.getInt(res.getColumnIndex(MouvementHelper.PRODUIT_ID)));
            mouvement.setOperation_id(res.getInt(res.getColumnIndex(MouvementHelper.OPERATION_ID)));
            mouvement.setProduit(res.getString(res.getColumnIndex(MouvementHelper.PRODUIT)));
            mouvement.setEtat(res.getInt(res.getColumnIndex(MouvementHelper.ETAT)));
            try {
                mouvement.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.CREATED_AT))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            mouvements.add(mouvement);
            res.moveToNext();
        }
        Produit produit = null ;
        double n = 0 ;
        double mte = 0 ;

        for (int i = 0; i < mouvements.size() ; i++){
            produit = dao.getOne(mouvements.get(i).getProduit_id()) ;
            // Si le produit existe
            if (produits.contains(produit)){
                // on recupere le prix total deja existant
                mte = produits.get(produits.indexOf(produit)).getPrixV() ;
                // On calcule le nouvelle quantité
                n = mouvements.get(i).getQuantite() ;
                // Puis le nouveau montant
                mte += mouvements.get(i).getPrixV() * mouvements.get(i).getQuantite() ;
                // On affecte le tous
                produits.get(produits.indexOf(produit)).setPrixV(mte);
            }
            else if (produit!=null){
                // On affecte les quantite et produit du nouvel produit
                produit.setPrixV(mouvements.get(i).getPrixV() * mouvements.get(i).getQuantite());
                produits.add(produit) ;
            }
        }
        res.close();
        return  produits ;
    }


    public int updateMany(Partenaire partenaire) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(OperationHelper.PARTENAIRE_ID, partenaire.getId_externe());

        int i = mDb.update(OperationHelper.TABLE_NAME, contentValues, OperationHelper.PARTENAIRE_ID + " = ? ", new String[] {Long.toString(partenaire.getId())});
        Log.e("UPDATE",String.valueOf(i)) ;
        return i ;
    }


    public int updateMany(Commercial commercial) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(OperationHelper.COMMERCIAL_ID, commercial.getId_externe());

        int i = mDb.update(OperationHelper.TABLE_NAME, contentValues, OperationHelper.COMMERCIAL_ID + " = ? ", new String[] {Long.toString(commercial.getId())});
        Log.e("UPDATE",String.valueOf(i)) ;
        return i ;
    }

    public String getOpTitle(String typeOperation_id) {
        if (typeOperation_id.startsWith(PLACEMENT)) return mContext.getString(R.string.placm) ;
        else if (typeOperation_id.startsWith(IMMOBILISATION)) return mContext.getString(R.string.immo) ;
        else if (typeOperation_id.startsWith(CAPITAL)) return mContext.getString(R.string.capital) ;
        else if (typeOperation_id.equals(PUB)) return mContext.getString(R.string.pub) ;
        else if (typeOperation_id.equals(REGLEMENT)) return mContext.getString(R.string.rglmtc) ;
        else if (typeOperation_id.equals(MISSION)) return mContext.getString(R.string.miss) ;
        else if (typeOperation_id.equals(ENTRETIEN_REPARATION)) return mContext.getString(R.string.entretient) ;
        else if (typeOperation_id.equals(DOTATION_AMOR)) return mContext.getString(R.string.dotationamor) ;
        return "" ;
    }


    public ArrayList<Operation> getMany(long id) {
        ArrayList<Operation> operations = new ArrayList<Operation>();
        Operation operation = null ;

        Cursor res =  mDb.rawQuery( "select * from "+OperationHelper.TABLE_NAME + " WHERE " + OperationHelper.OPERATION_ID + " = " + id, null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            operation = new Operation();


            operation.setId(res.getLong(res.getColumnIndex(OperationHelper.TABLE_KEY)));
            operation.setId_externe(res.getLong(res.getColumnIndex(OperationHelper.ID_EXTERNE)));
            operation.setComptebanque_id(res.getLong(res.getColumnIndex(OperationHelper.COMPTEBANQUE_ID)));
            operation.setMontant(res.getDouble(res.getColumnIndex(OperationHelper.MONTANT)));
            operation.setNbreproduit(res.getInt(res.getColumnIndex(OperationHelper.NBREPRODUIT)));
            operation.setRemise(res.getDouble(res.getColumnIndex(OperationHelper.REMISE)));
            operation.setAnnuler(res.getInt(res.getColumnIndex(OperationHelper.ANNULER)));
            operation.setCaisse_id(res.getLong(res.getColumnIndex(OperationHelper.CAISSE_ID)));
            operation.setPartenaire_id(res.getLong(res.getColumnIndex(OperationHelper.PARTENAIRE_ID)));
            operation.setCommercialid(res.getLong(res.getColumnIndex(OperationHelper.COMMERCIAL_ID)));
            operation.setOperation_id(res.getLong(res.getColumnIndex(OperationHelper.OPERATION_ID)));
            operation.setTypeOperation_id(res.getString(res.getColumnIndex(OperationHelper.TYPEOPERATION_ID)));
            operation.setEtat(res.getInt(res.getColumnIndex(OperationHelper.ETAT)));
            operation.setRecu(res.getDouble(res.getColumnIndex(OperationHelper.RECU)));
            operation.setEntree(res.getInt(res.getColumnIndex(OperationHelper.ENTREE)));
            operation.setAttente(res.getInt(res.getColumnIndex(OperationHelper.ATTENTE)));
            operation.setClient(res.getString(res.getColumnIndex(OperationHelper.CLIENT)));
            operation.setNumcheque(res.getString(res.getColumnIndex(OperationHelper.NUMCHEQUE)));
            operation.setModepayement(res.getString(res.getColumnIndex(OperationHelper.MODEPAYEMENT)));
            operation.setDescription(res.getString(res.getColumnIndex(OperationHelper.DESCRIPTION)));
            operation.setToken(res.getString(res.getColumnIndex(OperationHelper.TOKEN)));
            operation.setPayer(res.getInt(res.getColumnIndex(OperationHelper.PAYER)));
            try {
                operation.setDateecheance(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ECHEANCE))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                operation.setDateoperation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_OPERATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                operation.setDateannulation(DAOBase.formatter.parse(res.getString(res.getColumnIndex(OperationHelper.DATE_ANNULATION))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            operations.add(operation);
            res.moveToNext();
        }
        res.close();
        return operations;
    }

}
