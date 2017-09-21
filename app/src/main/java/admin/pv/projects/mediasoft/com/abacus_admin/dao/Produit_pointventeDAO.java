package admin.pv.projects.mediasoft.com.abacus_admin.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import admin.pv.projects.mediasoft.com.abacus_admin.database.Produit_pointventeHelper;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Produit_pointvente;


/**
 * Created by Mayi on 18/12/2015.
 */
public class Produit_pointventeDAO extends DAOBase implements Crud<Produit_pointvente> {

    Context context = null ;

    public Produit_pointventeDAO(Context pContext) {
        super(pContext);
        context = pContext ;
        this.mHandler = Produit_pointventeHelper.getHelper(pContext, DATABASE, VERSION);
        open();
    }

    @Override
    public long add(Produit_pointvente produit_pointvente) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Produit_pointventeHelper.PRODUIT_ID, produit_pointvente.getProduit_id());
        contentValues.put(Produit_pointventeHelper.POINTVENTE_ID, produit_pointvente.getPointvente_id());

        Long l = mDb.insert(Produit_pointventeHelper.TABLE_NAME, null, contentValues);

        Log.e("DEBUG", String.valueOf(l)) ;
        return l;
    }

    @Override
    public int delete(long id) {
        return  mDb.delete(Produit_pointventeHelper.TABLE_NAME, Produit_pointventeHelper.TABLE_KEY + " = ? ",new String[] { Long.toString(id)});
    }

    public int deleteAllByPv(long id) {
        return  mDb.delete(Produit_pointventeHelper.TABLE_NAME, Produit_pointventeHelper.POINTVENTE_ID + " = ? ",new String[] { Long.toString(id)});
    }

    public int clean() {
        return  mDb.delete(Produit_pointventeHelper.TABLE_NAME, null,null);
    }

    @Override
    public int update(Produit_pointvente produit_pointvente) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(Produit_pointventeHelper.TABLE_KEY, produit_pointvente.getId());
        contentValues.put(Produit_pointventeHelper.PRODUIT_ID, produit_pointvente.getProduit_id());
        contentValues.put(Produit_pointventeHelper.POINTVENTE_ID, produit_pointvente.getPointvente_id());

        int l = mDb.update(Produit_pointventeHelper.TABLE_NAME, contentValues, Produit_pointventeHelper.TABLE_KEY + " = ? ", new String[]{Long.toString(produit_pointvente.getId())});

        Log.e("DEBUG", String.valueOf(l)) ;
        return l;
    }

    @Override
    public Produit_pointvente getOne(long id) {
        Produit_pointvente produit_pointvente = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ Produit_pointventeHelper.TABLE_NAME +" where " + Produit_pointventeHelper.TABLE_KEY + "="+id+"", null );


        if (res.moveToFirst()) {
            produit_pointvente = new Produit_pointvente();

            produit_pointvente.setId(res.getLong(res.getColumnIndex(Produit_pointventeHelper.TABLE_KEY)));
            produit_pointvente.setProduit_id(res.getLong(res.getColumnIndex(Produit_pointventeHelper.PRODUIT_ID)));
            produit_pointvente.setPointvente_id(res.getLong(res.getColumnIndex(Produit_pointventeHelper.POINTVENTE_ID)));
        }
        res.close();
        return produit_pointvente;
    }

    @Override
    public ArrayList<Produit_pointvente> getAll() {
        ArrayList<Produit_pointvente> produit_pointventes = new ArrayList<Produit_pointvente>();

        Produit_pointvente produit_pointvente = null ;
        Cursor res =  mDb.rawQuery( "select * from "+ Produit_pointventeHelper.TABLE_NAME+ " order by "+ Produit_pointventeHelper.TABLE_KEY + " asc ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            produit_pointvente = new Produit_pointvente();

            produit_pointvente.setId(res.getLong(res.getColumnIndex(Produit_pointventeHelper.TABLE_KEY)));
            produit_pointvente.setProduit_id(res.getLong(res.getColumnIndex(Produit_pointventeHelper.PRODUIT_ID)));
            produit_pointvente.setPointvente_id(res.getLong(res.getColumnIndex(Produit_pointventeHelper.POINTVENTE_ID)));

            produit_pointventes.add(produit_pointvente) ;
            res.moveToNext();
        }
        res.close();
        return produit_pointventes;
    }

    public ArrayList<Produit_pointvente> getMany(long id) {
        ArrayList<Produit_pointvente> produit_pointventes = new ArrayList<Produit_pointvente>();

        Produit_pointvente produit_pointvente = null ;
        Cursor res =  mDb.rawQuery( "select * from "+ Produit_pointventeHelper.TABLE_NAME+ " where " + Produit_pointventeHelper.POINTVENTE_ID + " = " + id + "  order by "+ Produit_pointventeHelper.TABLE_KEY + " asc ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            produit_pointvente = new Produit_pointvente();

            produit_pointvente.setId(res.getLong(res.getColumnIndex(Produit_pointventeHelper.TABLE_KEY)));
            produit_pointvente.setProduit_id(res.getLong(res.getColumnIndex(Produit_pointventeHelper.PRODUIT_ID)));
            produit_pointvente.setPointvente_id(res.getLong(res.getColumnIndex(Produit_pointventeHelper.POINTVENTE_ID)));

            produit_pointventes.add(produit_pointvente) ;
            res.moveToNext();
        }
        res.close();
        return produit_pointventes;
    }

}
