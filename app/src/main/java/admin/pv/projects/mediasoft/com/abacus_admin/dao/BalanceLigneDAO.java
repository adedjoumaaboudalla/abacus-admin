package admin.pv.projects.mediasoft.com.abacus_admin.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import admin.pv.projects.mediasoft.com.abacus_admin.database.BalanceHelper;
import admin.pv.projects.mediasoft.com.abacus_admin.database.BalanceLigneHelper;
import admin.pv.projects.mediasoft.com.abacus_admin.model.BalanceLigne;


/**
 * Created by Mayi on 18/12/2015.
 */
public class BalanceLigneDAO extends DAOBase implements Crud<BalanceLigne> {

    Context context = null ;

    public BalanceLigneDAO(Context pContext) {
        super(pContext);
        context = pContext ;
        this.mHandler = BalanceLigneHelper.getHelper(pContext, DATABASE, VERSION);
        open();
    }

    @Override
    public long add(BalanceLigne balanceLigne) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(BalanceLigneHelper.LIGNE_ID, balanceLigne.getLigne_id());
        contentValues.put(BalanceLigneHelper.BALANCE_ID, balanceLigne.getBalance_id());
        contentValues.put(BalanceLigneHelper.SOLDE_CLOTURE, balanceLigne.getSolde_cloture());
        contentValues.put(BalanceLigneHelper.SOLDE_REPORT, balanceLigne.getSolde_report());
        contentValues.put(BalanceLigneHelper.MOUVEMENT, balanceLigne.getMouvement());
        if (balanceLigne.getCreated_at()!=null) contentValues.put(BalanceLigneHelper.CREATED_AT, formatter.format(balanceLigne.getCreated_at()));
        if (balanceLigne.getUpdated_at()!=null) contentValues.put(BalanceLigneHelper.UPDATED_AT, formatter.format(balanceLigne.getUpdated_at()));

        Long l = mDb.insert(BalanceLigneHelper.TABLE_NAME, null, contentValues);

        Log.e("DEBUG", String.valueOf(l)) ;
        return l;
    }

    @Override
    public int delete(long id) {
        return  mDb.delete(BalanceLigneHelper.TABLE_NAME, BalanceLigneHelper.TABLE_KEY + " = ? ",new String[] { Long.toString(id)});
    }

    public int clean() {
        return  mDb.delete(BalanceLigneHelper.TABLE_NAME, null,null);
    }

    @Override
    public int update(BalanceLigne balanceLigne) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(BalanceLigneHelper.LIGNE_ID, balanceLigne.getLigne_id());
        contentValues.put(BalanceLigneHelper.BALANCE_ID, balanceLigne.getBalance_id());
        contentValues.put(BalanceLigneHelper.SOLDE_CLOTURE, balanceLigne.getSolde_cloture());
        contentValues.put(BalanceLigneHelper.SOLDE_REPORT, balanceLigne.getSolde_report());
        contentValues.put(BalanceLigneHelper.MOUVEMENT, balanceLigne.getMouvement());
        if (balanceLigne.getCreated_at()!=null) contentValues.put(BalanceLigneHelper.CREATED_AT, formatter.format(balanceLigne.getCreated_at()));
        if (balanceLigne.getUpdated_at()!=null) contentValues.put(BalanceLigneHelper.UPDATED_AT, formatter.format(balanceLigne.getUpdated_at()));

        int l = mDb.update(BalanceLigneHelper.TABLE_NAME, contentValues, BalanceLigneHelper.TABLE_KEY + " = ? ", new String[]{Long.toString(balanceLigne.getId())});

        Log.e("DEBUG", String.valueOf(l)) ;
        return l;
    }

    @Override
    public BalanceLigne getOne(long id) {
        BalanceLigne balanceLigne = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ BalanceLigneHelper.TABLE_NAME +" where " + BalanceLigneHelper.TABLE_KEY + "="+id+"", null );


        if (res.moveToFirst()) {
            balanceLigne = new BalanceLigne();

            balanceLigne.setId(res.getLong(res.getColumnIndex(BalanceLigneHelper.TABLE_KEY)));
            balanceLigne.setBalance_id(res.getLong(res.getColumnIndex(BalanceLigneHelper.BALANCE_ID)));
            balanceLigne.setLigne_id(res.getLong(res.getColumnIndex(BalanceLigneHelper.LIGNE_ID)));
            balanceLigne.setMouvement(res.getDouble(res.getColumnIndex(BalanceLigneHelper.MOUVEMENT)));
            balanceLigne.setSolde_cloture(res.getDouble(res.getColumnIndex(BalanceLigneHelper.SOLDE_CLOTURE)));
            balanceLigne.setSolde_report(res.getDouble(res.getColumnIndex(BalanceLigneHelper.SOLDE_REPORT)));
            try {
                balanceLigne.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(BalanceLigneHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                balanceLigne.setUpdated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(BalanceLigneHelper.UPDATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        res.close();
        return balanceLigne;
    }


    public BalanceLigne getOne(long id,long balance) {
        BalanceLigne balanceLigne = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ BalanceLigneHelper.TABLE_NAME +" where " + BalanceLigneHelper.LIGNE_ID + "="+id+" and " + BalanceLigneHelper.BALANCE_ID + " = " + balance, null );


        if (res.moveToFirst()) {
            balanceLigne = new BalanceLigne();

            balanceLigne.setId(res.getLong(res.getColumnIndex(BalanceLigneHelper.TABLE_KEY)));
            balanceLigne.setBalance_id(res.getLong(res.getColumnIndex(BalanceLigneHelper.BALANCE_ID)));
            balanceLigne.setLigne_id(res.getLong(res.getColumnIndex(BalanceLigneHelper.LIGNE_ID)));
            balanceLigne.setMouvement(res.getDouble(res.getColumnIndex(BalanceLigneHelper.MOUVEMENT)));
            balanceLigne.setSolde_cloture(res.getDouble(res.getColumnIndex(BalanceLigneHelper.SOLDE_CLOTURE)));
            balanceLigne.setSolde_report(res.getDouble(res.getColumnIndex(BalanceLigneHelper.SOLDE_REPORT)));
            try {
                balanceLigne.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(BalanceLigneHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                balanceLigne.setUpdated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(BalanceLigneHelper.UPDATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        res.close();
        return balanceLigne;
    }

    @Override
    public ArrayList<BalanceLigne> getAll() {
        ArrayList<BalanceLigne> balanceLignes = new ArrayList<BalanceLigne>();

        BalanceLigne balanceLigne = null ;
        Cursor res =  mDb.rawQuery( "select * from "+ BalanceLigneHelper.TABLE_NAME+ " order by "+ BalanceLigneHelper.TABLE_KEY + " asc ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            balanceLigne = new BalanceLigne();

            balanceLigne.setId(res.getLong(res.getColumnIndex(BalanceLigneHelper.TABLE_KEY)));
            balanceLigne.setBalance_id(res.getLong(res.getColumnIndex(BalanceLigneHelper.BALANCE_ID)));
            balanceLigne.setLigne_id(res.getLong(res.getColumnIndex(BalanceLigneHelper.LIGNE_ID)));
            balanceLigne.setMouvement(res.getDouble(res.getColumnIndex(BalanceLigneHelper.MOUVEMENT)));
            balanceLigne.setSolde_cloture(res.getDouble(res.getColumnIndex(BalanceLigneHelper.SOLDE_CLOTURE)));
            balanceLigne.setSolde_report(res.getDouble(res.getColumnIndex(BalanceLigneHelper.SOLDE_REPORT)));
            try {
                balanceLigne.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(BalanceLigneHelper.CREATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                balanceLigne.setUpdated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(BalanceLigneHelper.UPDATED_AT))));
            } catch (Exception e) {
                e.printStackTrace();
            }

            balanceLignes.add(balanceLigne) ;
            res.moveToNext();
        }
        res.close();
        return balanceLignes;
    }

}
