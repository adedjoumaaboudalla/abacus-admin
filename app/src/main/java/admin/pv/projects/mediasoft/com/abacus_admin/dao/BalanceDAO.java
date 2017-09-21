package admin.pv.projects.mediasoft.com.abacus_admin.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;

import admin.pv.projects.mediasoft.com.abacus_admin.database.BalanceHelper;
import admin.pv.projects.mediasoft.com.abacus_admin.database.OperationHelper;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Balance;


/**
 * Created by Mayi on 18/12/2015.
 */
public class BalanceDAO extends DAOBase implements Crud<Balance> {

    Context context = null ;

    public BalanceDAO(Context pContext) {
        super(pContext);
        context = pContext ;
        this.mHandler = BalanceHelper.getHelper(pContext, DATABASE, VERSION);
        open();
    }

    @Override
    public long add(Balance balance) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(BalanceHelper.TABLE_KEY, balance.getId());
        contentValues.put(BalanceHelper.LIBELLE, balance.getLibelle());
        if (balance.getDatedebut()!=null) contentValues.put(BalanceHelper.DATE_DEBUT, formatter.format(balance.getDatedebut()));
        if (balance.getDatefin()!=null) contentValues.put(BalanceHelper.DATE_FIN, formatter.format(balance.getDatefin()));
        contentValues.put(BalanceHelper.POINTVENTE_ID, balance.getPointvente_id());

        Long l = mDb.insert(BalanceHelper.TABLE_NAME, null, contentValues);

        Log.e("DEBUG", String.valueOf(l)) ;
        return l;
    }

    @Override
    public int delete(long id) {
        return  mDb.delete(BalanceHelper.TABLE_NAME, BalanceHelper.TABLE_KEY + " = ? ",new String[] { Long.toString(id)});
    }

    public int clean() {
        return  mDb.delete(BalanceHelper.TABLE_NAME, null,null);
    }

    @Override
    public int update(Balance balance) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(BalanceHelper.TABLE_KEY, balance.getId());
        contentValues.put(BalanceHelper.LIBELLE, balance.getLibelle());
        if (balance.getDatedebut()!=null) contentValues.put(BalanceHelper.DATE_DEBUT, formatter.format(balance.getDatedebut()));
        if (balance.getDatefin()!=null) contentValues.put(BalanceHelper.DATE_FIN, formatter.format(balance.getDatefin()));
        contentValues.put(BalanceHelper.POINTVENTE_ID, balance.getPointvente_id());

        int l = mDb.update(BalanceHelper.TABLE_NAME, contentValues, BalanceHelper.TABLE_KEY + " = ? ", new String[]{Long.toString(balance.getId())});

        Log.e("DEBUG", String.valueOf(l)) ;
        return l;
    }

    @Override
    public Balance getOne(long id) {
        Balance balance = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ BalanceHelper.TABLE_NAME +" where " + BalanceHelper.TABLE_KEY + "="+id+"", null );


        if (res.moveToFirst()) {
            balance = new Balance();

            balance.setId(res.getLong(res.getColumnIndex(BalanceHelper.TABLE_KEY)));
            balance.setLibelle(res.getString(res.getColumnIndex(BalanceHelper.LIBELLE)));
            balance.setPointvente_id(res.getLong(res.getColumnIndex(BalanceHelper.POINTVENTE_ID)));
            try {
                balance.setDatefin(DAOBase.formatter.parse(res.getString(res.getColumnIndex(BalanceHelper.DATE_FIN))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                balance.setDatedebut(DAOBase.formatter.parse(res.getString(res.getColumnIndex(BalanceHelper.DATE_DEBUT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        res.close();
        return balance;
    }


    public Balance getOneByPv(long id) {
        Balance balance = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ BalanceHelper.TABLE_NAME +" where " + BalanceHelper.POINTVENTE_ID + "="+id+"", null );


        if (res.moveToFirst()) {
            balance = new Balance();

            balance.setId(res.getLong(res.getColumnIndex(BalanceHelper.TABLE_KEY)));
            balance.setLibelle(res.getString(res.getColumnIndex(BalanceHelper.LIBELLE)));
            balance.setPointvente_id(res.getLong(res.getColumnIndex(BalanceHelper.POINTVENTE_ID)));
            try {
                balance.setDatefin(DAOBase.formatter.parse(res.getString(res.getColumnIndex(BalanceHelper.DATE_FIN))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                balance.setDatedebut(DAOBase.formatter.parse(res.getString(res.getColumnIndex(BalanceHelper.DATE_DEBUT))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        res.close();
        return balance;
    }

    @Override
    public ArrayList<Balance> getAll() {
        ArrayList<Balance> balances = new ArrayList<Balance>();

        Balance balance = null ;
        Cursor res =  mDb.rawQuery( "select * from "+ BalanceHelper.TABLE_NAME+ " order by "+ BalanceHelper.TABLE_KEY + " asc ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            balance = new Balance();

            balance.setId(res.getLong(res.getColumnIndex(BalanceHelper.TABLE_KEY)));
            balance.setLibelle(res.getString(res.getColumnIndex(BalanceHelper.LIBELLE)));
            balance.setPointvente_id(res.getLong(res.getColumnIndex(BalanceHelper.POINTVENTE_ID)));
            try {
                balance.setDatefin(DAOBase.formatter.parse(res.getString(res.getColumnIndex(BalanceHelper.DATE_FIN))));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                balance.setDatedebut(DAOBase.formatter.parse(res.getString(res.getColumnIndex(BalanceHelper.DATE_DEBUT))));
            } catch (Exception e) {
                e.printStackTrace();
            }

            balances.add(balance) ;
            res.moveToNext();
        }
        res.close();
        return balances;
    }

}
