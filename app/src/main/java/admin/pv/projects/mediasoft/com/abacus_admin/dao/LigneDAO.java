package admin.pv.projects.mediasoft.com.abacus_admin.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import admin.pv.projects.mediasoft.com.abacus_admin.database.LigneHelper;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Ligne;


/**
 * Created by Mayi on 18/12/2015.
 */
public class LigneDAO extends DAOBase implements Crud<Ligne> {

    Context context = null ;

    public LigneDAO(Context pContext) {
        super(pContext);
        context = pContext ;
        this.mHandler = LigneHelper.getHelper(pContext, DATABASE, VERSION);
        open();
    }

    @Override
    public long add(Ligne ligne) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(LigneHelper.TABLE_KEY, ligne.getId());
        contentValues.put(LigneHelper.CODE, ligne.getCode());
        contentValues.put(LigneHelper.LIBELLE, ligne.getLibelle());
        contentValues.put(LigneHelper.NATURE, ligne.getNature());
        contentValues.put(LigneHelper.ORDRE, ligne.getOrdre());
        if (ligne.getCreated_at()!=null) contentValues.put(LigneHelper.CREATED_AT, formatter.format(ligne.getCreated_at()));
        if (ligne.getUpdated_at()!=null) contentValues.put(LigneHelper.UPDATED_AT, formatter.format(ligne.getUpdated_at()));

        Long l = mDb.insert(LigneHelper.TABLE_NAME, null, contentValues);

        Log.e("DEBUG", String.valueOf(l)) ;
        return l;
    }

    @Override
    public int delete(long id) {
        return  mDb.delete(LigneHelper.TABLE_NAME, LigneHelper.TABLE_KEY + " = ? ",new String[] { Long.toString(id)});
    }

    public int clean() {
        return  mDb.delete(LigneHelper.TABLE_NAME, null,null);
    }

    @Override
    public int update(Ligne ligne) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(LigneHelper.TABLE_KEY, ligne.getId());
        contentValues.put(LigneHelper.CODE, ligne.getCode());
        contentValues.put(LigneHelper.LIBELLE, ligne.getLibelle());
        contentValues.put(LigneHelper.NATURE, ligne.getNature());
        contentValues.put(LigneHelper.ORDRE, ligne.getOrdre());
        if (ligne.getCreated_at()!=null) contentValues.put(LigneHelper.CREATED_AT, formatter.format(ligne.getCreated_at()));
        if (ligne.getUpdated_at()!=null) contentValues.put(LigneHelper.UPDATED_AT, formatter.format(ligne.getUpdated_at()));

        int l = mDb.update(LigneHelper.TABLE_NAME, contentValues, LigneHelper.TABLE_KEY + " = ? ", new String[]{Long.toString(ligne.getId())});

        Log.e("DEBUG", String.valueOf(l)) ;
        return l;
    }

    @Override
    public Ligne getOne(long id) {
        Ligne ligne = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ LigneHelper.TABLE_NAME +" where " + LigneHelper.TABLE_KEY + "="+id+"", null );


        if (res.moveToFirst()) {
            ligne = new Ligne();

            ligne.setId(res.getLong(res.getColumnIndex(LigneHelper.TABLE_KEY)));
            ligne.setLibelle(res.getString(res.getColumnIndex(LigneHelper.LIBELLE)));
            ligne.setCode(res.getString(res.getColumnIndex(LigneHelper.CODE)));
            ligne.setNature(res.getString(res.getColumnIndex(LigneHelper.NATURE)));
            ligne.setOrdre(res.getInt(res.getColumnIndex(LigneHelper.ORDRE)));
            try {
                ligne.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(LigneHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                ligne.setUpdated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(LigneHelper.UPDATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        res.close();
        return ligne;
    }


    public Ligne getOneByCode(String code) {
        Ligne ligne = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ LigneHelper.TABLE_NAME +" where " + LigneHelper.CODE + "='"+code+"'", null );


        if (res.moveToFirst()) {
            ligne = new Ligne();

            ligne.setId(res.getLong(res.getColumnIndex(LigneHelper.TABLE_KEY)));
            ligne.setLibelle(res.getString(res.getColumnIndex(LigneHelper.LIBELLE)));
            ligne.setCode(res.getString(res.getColumnIndex(LigneHelper.CODE)));
            ligne.setNature(res.getString(res.getColumnIndex(LigneHelper.NATURE)));
            ligne.setOrdre(res.getInt(res.getColumnIndex(LigneHelper.ORDRE)));
            try {
                ligne.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(LigneHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                ligne.setUpdated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(LigneHelper.UPDATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        res.close();
        return ligne;
    }

    @Override
    public ArrayList<Ligne> getAll() {
        ArrayList<Ligne> lignes = new ArrayList<Ligne>();

        Ligne ligne = null ;
        Cursor res =  mDb.rawQuery( "select * from "+ LigneHelper.TABLE_NAME+ " order by "+ LigneHelper.TABLE_KEY + " asc ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            ligne = new Ligne();

            ligne.setId(res.getLong(res.getColumnIndex(LigneHelper.TABLE_KEY)));
            ligne.setLibelle(res.getString(res.getColumnIndex(LigneHelper.LIBELLE)));
            ligne.setCode(res.getString(res.getColumnIndex(LigneHelper.CODE)));
            ligne.setNature(res.getString(res.getColumnIndex(LigneHelper.NATURE)));
            ligne.setOrdre(res.getInt(res.getColumnIndex(LigneHelper.ORDRE)));
            try {
                ligne.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(LigneHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                ligne.setUpdated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(LigneHelper.UPDATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }

            lignes.add(ligne) ;
            res.moveToNext();
        }
        res.close();
        return lignes;
    }

}
