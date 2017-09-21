package admin.pv.projects.mediasoft.com.abacus_admin.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;

import admin.pv.projects.mediasoft.com.abacus_admin.database.CaisseHelper;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Caisse;


/**
 * Created by Mayi on 05/10/2015.
 */
public class CaisseDAO extends DAOBase implements Crud<Caisse> {


    public CaisseDAO(Context pContext) {
        super(pContext);
        this.mHandler = CaisseHelper.getHelper(pContext, DATABASE, VERSION);
        open();
    }

    @Override
    public long add(Caisse caisse) {
        
        ContentValues contentValues = new ContentValues();
        contentValues.put(CaisseHelper.TABLE_KEY, caisse.getId());
        contentValues.put(CaisseHelper.CODE, caisse.getCode());
        contentValues.put(CaisseHelper.SOLDE, caisse.getSolde());
        contentValues.put(CaisseHelper.LOGIN, caisse.getLogin());
        contentValues.put(CaisseHelper.IMEI, caisse.getImei());
        contentValues.put(CaisseHelper.POINTVENTE_ID, caisse.getPointVente());
        contentValues.put(CaisseHelper.CREATED_AT, formatter.format(caisse.getCreated_at()));

        long  l = mDb.insert(CaisseHelper.TABLE_NAME, null, contentValues);
        Log.v("CAISSE", String.valueOf(l)) ;

        return l;
    }

    @Override
    public int delete(long id) {
        return  mDb.delete(CaisseHelper.TABLE_NAME,  CaisseHelper.TABLE_KEY + " = ? ",new String[] { Long.toString(id)});
    }


    public int clean() {
        return  mDb.delete(CaisseHelper.TABLE_NAME,  null,null);
    }

    @Override
    public int update(Caisse caisse) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CaisseHelper.CODE, caisse.getCode());
        contentValues.put(CaisseHelper.SOLDE, caisse.getSolde());
        contentValues.put(CaisseHelper.LOGIN, caisse.getLogin());
        contentValues.put(CaisseHelper.IMEI, caisse.getImei());
        contentValues.put(CaisseHelper.POINTVENTE_ID, caisse.getPointVente());
        contentValues.put(CaisseHelper.CREATED_AT, formatter.format(caisse.getCreated_at()));

        int i = mDb.update(CaisseHelper.TABLE_NAME, contentValues, CaisseHelper.TABLE_KEY + " = ? ", new String[] { Long.toString(caisse.getId()) } );

        return i;
    }

    @Override
    public Caisse getOne(long id) {
        Caisse caisse = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ CaisseHelper.TABLE_NAME +" where " + CaisseHelper.TABLE_KEY + "="+id+"", null );

        if (res.moveToFirst()){

            caisse = new Caisse();

            caisse.setId(res.getLong(res.getColumnIndex(CaisseHelper.TABLE_KEY)));
            caisse.setCode(res.getString(res.getColumnIndex(CaisseHelper.CODE)));
            caisse.setSolde(res.getDouble(res.getColumnIndex(CaisseHelper.SOLDE)));
            caisse.setLogin(res.getString(res.getColumnIndex(CaisseHelper.LOGIN)));
            caisse.setImei(res.getString(res.getColumnIndex(CaisseHelper.IMEI)));
            caisse.setPointVente(res.getInt(res.getColumnIndex(CaisseHelper.POINTVENTE_ID)));


            try {
                caisse.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(CaisseHelper.CREATED_AT))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

         };
        res.close();
        return caisse ;
    }

    public Caisse getFirst() {
        Caisse caisse = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ CaisseHelper.TABLE_NAME +" order by " + CaisseHelper.TABLE_KEY  + " desc", null );

        if (res.moveToFirst()){

            caisse = new Caisse();

            caisse.setId(res.getLong(res.getColumnIndex(CaisseHelper.TABLE_KEY)));
            caisse.setCode(res.getString(res.getColumnIndex(CaisseHelper.CODE)));
            caisse.setSolde(res.getDouble(res.getColumnIndex(CaisseHelper.SOLDE)));
            caisse.setLogin(res.getString(res.getColumnIndex(CaisseHelper.LOGIN)));
            caisse.setImei(res.getString(res.getColumnIndex(CaisseHelper.IMEI)));
            caisse.setPointVente(res.getInt(res.getColumnIndex(CaisseHelper.POINTVENTE_ID)));


            try {
                caisse.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(CaisseHelper.CREATED_AT))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

         };

        res.close();
        return caisse ;
    }

    @Override
    public ArrayList<Caisse> getAll() {
        ArrayList<Caisse> caisses = new ArrayList<Caisse>();
        Caisse caisse = null ;

        Cursor res =  mDb.rawQuery( "select * from "+CaisseHelper.TABLE_NAME+ " order by " + CaisseHelper.TABLE_KEY + " desc ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            caisse = new Caisse();

            caisse.setId(res.getLong(res.getColumnIndex(CaisseHelper.TABLE_KEY)));
            caisse.setCode(res.getString(res.getColumnIndex(CaisseHelper.CODE)));
            caisse.setSolde(res.getDouble(res.getColumnIndex(CaisseHelper.SOLDE)));
            caisse.setLogin(res.getString(res.getColumnIndex(CaisseHelper.LOGIN)));
            caisse.setImei(res.getString(res.getColumnIndex(CaisseHelper.IMEI)));
            caisse.setPointVente(res.getInt(res.getColumnIndex(CaisseHelper.POINTVENTE_ID)));



            try {
                caisse.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(CaisseHelper.CREATED_AT))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            caisses.add(caisse);
            res.moveToNext();
        }
        res.close();
        return caisses;
    }


    public ArrayList<Caisse> getAllById(long id) {
        ArrayList<Caisse> caisses = new ArrayList<Caisse>();
        Caisse caisse = null ;

        String rqt =  "select * from "+CaisseHelper.TABLE_NAME+ " where " + CaisseHelper.POINTVENTE_ID + " = " + id + " order by " + CaisseHelper.TABLE_KEY + " desc " ;
        Cursor res =  mDb.rawQuery(rqt, null);
        res.moveToFirst();

        Log.e("RQT",rqt) ;

        while(res.isAfterLast() == false){
            caisse = new Caisse();

            caisse.setId(res.getLong(res.getColumnIndex(CaisseHelper.TABLE_KEY)));
            caisse.setCode(res.getString(res.getColumnIndex(CaisseHelper.CODE)));
            caisse.setSolde(res.getDouble(res.getColumnIndex(CaisseHelper.SOLDE)));
            caisse.setLogin(res.getString(res.getColumnIndex(CaisseHelper.LOGIN)));
            caisse.setImei(res.getString(res.getColumnIndex(CaisseHelper.IMEI)));
            caisse.setPointVente(res.getInt(res.getColumnIndex(CaisseHelper.POINTVENTE_ID)));



            try {
                caisse.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(CaisseHelper.CREATED_AT))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            caisses.add(caisse);
            res.moveToNext();
        }
        res.close();
        return caisses;
    }
}
