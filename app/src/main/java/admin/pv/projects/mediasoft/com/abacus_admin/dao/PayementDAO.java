package admin.pv.projects.mediasoft.com.abacus_admin.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;

import admin.pv.projects.mediasoft.com.abacus_admin.database.PartenaireHelper;
import admin.pv.projects.mediasoft.com.abacus_admin.database.PayementHelper;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Operation;



/**
 * Created by Mayi on 21/04/2011.
 */
/*
public class PayementDAO extends DAOBase implements Crud<Payement> {
    
    
    public PayementDAO(Context pContext) {
        super(pContext);
        this.mHandler = PayementHelper.getHelper(pContext, DATABASE, VERSION);
        open();
    }

    @Override
    public long add(Payement payement) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(PayementHelper.OPERATION_ID, payement.getOperation_id());
        contentValues.put(PayementHelper.IDEXTERNE, payement.getId_externe());
        contentValues.put(PayementHelper.ETAT, payement.getEtat());
        contentValues.put(PayementHelper.MONTANT, payement.getMontant());
        contentValues.put(PayementHelper.MODEPAYEMENT, payement.getModepayement());
        contentValues.put(PayementHelper.NUMCHEQUE, payement.getNumcheque());
        contentValues.put(PayementHelper.BANQUE_ID, payement.getBanque_id());
        contentValues.put(PayementHelper.CREATED_AT, formatter.format(payement.getCreated_at()));


        long  l = mDb.insert(PayementHelper.TABLE_NAME, null, contentValues);

        Log.e("DEBUG",String.valueOf(l)) ;
        return l;
    }

    @Override
    public int delete(long id) {
        return  mDb.delete(PayementHelper.TABLE_NAME, PayementHelper.TABLE_KEY + " = ? ",new String[] { Long.toString(id)});
    }


    public int clean() {
        return  mDb.delete(PayementHelper.TABLE_NAME, null,null);
    }


    public int deletePV(long id) {
        return  mDb.delete(PayementHelper.TABLE_NAME, PayementHelper.TABLE_KEY + " = ? ",new String[] { Long.toString(id)});
    }

    @Override
    public int update(Payement payement) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(PayementHelper.OPERATION_ID, payement.getOperation_id());
        contentValues.put(PayementHelper.IDEXTERNE, payement.getId_externe());
        contentValues.put(PayementHelper.ETAT, payement.getEtat());
        contentValues.put(PayementHelper.MONTANT, payement.getMontant());
        contentValues.put(PayementHelper.MODEPAYEMENT, payement.getModepayement());
        contentValues.put(PayementHelper.NUMCHEQUE, payement.getNumcheque());
        contentValues.put(PayementHelper.BANQUE_ID, payement.getBanque_id());
        contentValues.put(PayementHelper.CREATED_AT, formatter.format(payement.getCreated_at()));

        int i = mDb.update(PayementHelper.TABLE_NAME, contentValues, PartenaireHelper.TABLE_KEY  + " = ? ", new String[]{Long.toString(payement.getId())});

        return i;
    }


    @Override
    public Payement getOne(long id) {
        Payement payement = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ PayementHelper.TABLE_NAME +" where " + PayementHelper.TABLE_KEY +  "="+id+"", null );

        if (res.moveToFirst()){
            payement = new Payement();

            payement.setId(res.getLong(res.getColumnIndex(PayementHelper.TABLE_KEY)));
            payement.setId(res.getLong(res.getColumnIndex(PayementHelper.IDEXTERNE)));
            payement.setId(res.getInt(res.getColumnIndex(PayementHelper.ETAT)));
            payement.setMontant(res.getDouble(res.getColumnIndex(PayementHelper.MONTANT)));
            payement.setOperation_id(res.getInt(res.getColumnIndex(PayementHelper.OPERATION_ID)));
            payement.setModepayement(res.getString(res.getColumnIndex(PayementHelper.MODEPAYEMENT)));
            payement.setNumcheque(res.getString(res.getColumnIndex(PayementHelper.NUMCHEQUE)));
            payement.setBanque_id(res.getLong(res.getColumnIndex(PayementHelper.BANQUE_ID)));

            try {
                payement.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(PartenaireHelper.CREATED_AT))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        };

        res.close();

        return payement ;
    }

    @Override
    public ArrayList<Payement> getAll() {
        ArrayList<Payement> payements = new ArrayList<Payement>();
        Payement payement = null ;

        Cursor res =  mDb.rawQuery( "select * from "+PayementHelper.TABLE_NAME, null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            payement = new Payement();

            payement.setId(res.getLong(res.getColumnIndex(PayementHelper.TABLE_KEY)));
            payement.setId(res.getLong(res.getColumnIndex(PayementHelper.IDEXTERNE)));
            payement.setId(res.getInt(res.getColumnIndex(PayementHelper.ETAT)));
            payement.setMontant(res.getDouble(res.getColumnIndex(PayementHelper.MONTANT)));
            payement.setOperation_id(res.getInt(res.getColumnIndex(PayementHelper.OPERATION_ID)));
            payement.setModepayement(res.getString(res.getColumnIndex(PayementHelper.MODEPAYEMENT)));
            payement.setNumcheque(res.getString(res.getColumnIndex(PayementHelper.NUMCHEQUE)));
            payement.setBanque_id(res.getLong(res.getColumnIndex(PayementHelper.BANQUE_ID)));

            try {
                payement.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(PartenaireHelper.CREATED_AT))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            payements.add(payement);
            res.moveToNext();
        }
        res.close();
        return payements;
    }

    public ArrayList<Payement> getMany(long id) {
        ArrayList<Payement> payements = new ArrayList<Payement>();
        Payement payement = null ;
        Cursor res = null ;
        Log.e("DEBUG","select * from "+PayementHelper.TABLE_NAME + " Where " + PayementHelper.OPERATION_ID + " = " + id) ;
        if (id!=0)  res =  mDb.rawQuery( "select * from "+PayementHelper.TABLE_NAME + " Where " + PayementHelper.OPERATION_ID + " = " + id, null);
        else res =  mDb.rawQuery( "select * from "+PayementHelper.TABLE_NAME , null);
        res.moveToFirst();
        Log.e("TYPE", id+"") ;
        Log.e("SIZE", res.getCount()+"") ;

        while(res.isAfterLast() == false){
            payement = new Payement();

            payement.setId(res.getLong(res.getColumnIndex(PayementHelper.TABLE_KEY)));
            payement.setId(res.getLong(res.getColumnIndex(PayementHelper.IDEXTERNE)));
            payement.setId(res.getInt(res.getColumnIndex(PayementHelper.ETAT)));
            payement.setMontant(res.getDouble(res.getColumnIndex(PayementHelper.MONTANT)));
            payement.setOperation_id(res.getInt(res.getColumnIndex(PayementHelper.OPERATION_ID)));
            payement.setModepayement(res.getString(res.getColumnIndex(PayementHelper.MODEPAYEMENT)));
            payement.setNumcheque(res.getString(res.getColumnIndex(PayementHelper.NUMCHEQUE)));
            payement.setBanque_id(res.getLong(res.getColumnIndex(PayementHelper.BANQUE_ID)));

            try {
                payement.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(PartenaireHelper.CREATED_AT))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            payements.add(payement);
            res.moveToNext();
        }
        res.close();
        return payements;
    }




    public Payement getLastReglement(long id) {
        Payement payement = null ;
        Cursor res = null ;
        Log.e("DEBUG","select * from "+PayementHelper.TABLE_NAME + " Where " + PayementHelper.OPERATION_ID + " = " + id) ;
        res =  mDb.rawQuery( "select * from "+PayementHelper.TABLE_NAME + " Where " + PayementHelper.OPERATION_ID + " = " + id + " ORDER BY " + PayementHelper.IDEXTERNE + " DESC", null);
        res.moveToFirst();
        Log.e("TYPE", id+"") ;
        Log.e("SIZE", res.getCount()+"") ;

        if(res.isAfterLast() == false){
            payement = new Payement();

            payement.setId(res.getLong(res.getColumnIndex(PayementHelper.TABLE_KEY)));
            payement.setId(res.getLong(res.getColumnIndex(PayementHelper.IDEXTERNE)));
            payement.setId(res.getInt(res.getColumnIndex(PayementHelper.ETAT)));
            payement.setMontant(res.getDouble(res.getColumnIndex(PayementHelper.MONTANT)));
            payement.setOperation_id(res.getInt(res.getColumnIndex(PayementHelper.OPERATION_ID)));
            payement.setModepayement(res.getString(res.getColumnIndex(PayementHelper.MODEPAYEMENT)));
            payement.setNumcheque(res.getString(res.getColumnIndex(PayementHelper.NUMCHEQUE)));
            payement.setBanque_id(res.getLong(res.getColumnIndex(PayementHelper.BANQUE_ID)));

            try {
                payement.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(PartenaireHelper.CREATED_AT))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        res.close();
        return payement;
    }


    public ArrayList<Payement> getNonSync(long id) {
        ArrayList<Payement> payements = new ArrayList<Payement>();
        Payement payement = null ;

        Log.e("DEBUG","select * from "+PayementHelper.TABLE_NAME + " Where  " + PayementHelper.ETAT + " = 0 and " + PayementHelper.OPERATION_ID + " = " + id) ;
        Cursor res =  mDb.rawQuery( "select * from "+PayementHelper.TABLE_NAME + " Where  " + PayementHelper.ETAT + " = 0 and " + PayementHelper.OPERATION_ID + " = " + id, null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            payement = new Payement();
            payement.setId(res.getLong(res.getColumnIndex(PayementHelper.TABLE_KEY)));
            payement.setId(res.getLong(res.getColumnIndex(PayementHelper.IDEXTERNE)));
            payement.setId(res.getInt(res.getColumnIndex(PayementHelper.ETAT)));
            payement.setMontant(res.getDouble(res.getColumnIndex(PayementHelper.MONTANT)));
            payement.setOperation_id(res.getInt(res.getColumnIndex(PayementHelper.OPERATION_ID)));
            payement.setModepayement(res.getString(res.getColumnIndex(PayementHelper.MODEPAYEMENT)));
            payement.setNumcheque(res.getString(res.getColumnIndex(PayementHelper.NUMCHEQUE)));
            payement.setBanque_id(res.getLong(res.getColumnIndex(PayementHelper.BANQUE_ID)));

            try {
                payement.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(PartenaireHelper.CREATED_AT))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            payements.add(payement);
            res.moveToNext();
        }
        res.close();
        return payements;
    }

    public ArrayList<Payement> getNonSync() {
        ArrayList<Payement> payements = new ArrayList<Payement>();
        Payement payement = null ;

        Cursor res =  mDb.rawQuery( "select * from "+PayementHelper.TABLE_NAME + " Where  " + PayementHelper.ETAT + " <> 2  ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            payement = new Payement();

            payement.setId(res.getLong(res.getColumnIndex(PayementHelper.TABLE_KEY)));
            payement.setId(res.getLong(res.getColumnIndex(PayementHelper.IDEXTERNE)));
            payement.setId(res.getInt(res.getColumnIndex(PayementHelper.ETAT)));
            payement.setMontant(res.getDouble(res.getColumnIndex(PayementHelper.MONTANT)));
            payement.setOperation_id(res.getInt(res.getColumnIndex(PayementHelper.OPERATION_ID)));
            payement.setModepayement(res.getString(res.getColumnIndex(PayementHelper.MODEPAYEMENT)));
            payement.setNumcheque(res.getString(res.getColumnIndex(PayementHelper.NUMCHEQUE)));
            payement.setBanque_id(res.getLong(res.getColumnIndex(PayementHelper.BANQUE_ID)));
            try {
                payement.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(PartenaireHelper.CREATED_AT))));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            payements.add(payement);
            res.moveToNext();
        }
        res.close();
        return payements;
    }

    public int updateMany(Operation vente) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(PayementHelper.OPERATION_ID, vente.getId_externe());

       int i = mDb.update(PayementHelper.TABLE_NAME, contentValues, PayementHelper.TABLE_KEY + " = ? ", new String[] { Long.toString(vente.getId()) } );
        Log.e("UPDATE",String.valueOf(i)) ;
        return i ;
    }


}
        */