package admin.pv.projects.mediasoft.com.abacus_admin.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import admin.pv.projects.mediasoft.com.abacus_admin.database.ClientHelper;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Client;


/**
 * Created by Mayi on 30/10/2015.
 */
public class ClientDAO extends DAOBase implements Crud<Client>{



    public ClientDAO(Context pContext) {
        super(pContext);
        this.mHandler = ClientHelper.getHelper(pContext, DATABASE, VERSION);
        open();
    }

    @Override
    public long add(Client client) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(ClientHelper.TABLE_KEY, client.getId());
        contentValues.put(ClientHelper.NOM, client.getNom());
        contentValues.put(ClientHelper.PRENOM, client.getPrenom());
        contentValues.put(ClientHelper.RAISONSOCIAL, client.getRaisonsocial());
        contentValues.put(ClientHelper.EMAIL, client.getEmail());
        contentValues.put(ClientHelper.TEL, client.getTel());
        contentValues.put(ClientHelper.ADRESSE, client.getAdresse());
        contentValues.put(ClientHelper.CREATED_AT, formatter.format(client.getCreated_at()));

        long  l = mDb.insert(ClientHelper.TABLE_NAME, null, contentValues);
        /*
        if (client.getId_externe()==0){

            client.setId_externe(l);
            update(client) ;
        }
        */
        return l;
    }

    @Override
    public int delete(long id) {
        return  mDb.delete(ClientHelper.TABLE_NAME,  ClientHelper.TABLE_KEY + " = ? ",new String[] { Long.toString(id)});
    }


    public int clean() {
        return  mDb.delete(ClientHelper.TABLE_NAME, null,null);
    }

    @Override
    public int update(Client client) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(ClientHelper.TABLE_KEY, client.getId());
        contentValues.put(ClientHelper.NOM, client.getNom());
        contentValues.put(ClientHelper.PRENOM, client.getPrenom());
        contentValues.put(ClientHelper.RAISONSOCIAL, client.getRaisonsocial());
        contentValues.put(ClientHelper.EMAIL, client.getEmail());
        contentValues.put(ClientHelper.TEL, client.getTel());
        contentValues.put(ClientHelper.ADRESSE, client.getAdresse());
        contentValues.put(ClientHelper.CREATED_AT, formatter.format(client.getCreated_at()));

        int i =mDb.update(ClientHelper.TABLE_NAME, contentValues,  ClientHelper.TABLE_KEY + " = ? ", new String[] { Long.toString(client.getId()) } );

        return i;
    }

    @Override
    public Client getOne(long id) {
        Client client = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ ClientHelper.TABLE_NAME +" where " +ClientHelper.TABLE_KEY + "  = "+id+"", null );

        if(res.moveToFirst()){
            client = new Client();

            client.setId(res.getLong(res.getColumnIndex(ClientHelper.TABLE_KEY)));
            client.setNom(res.getString(res.getColumnIndex(ClientHelper.NOM)));
            client.setPrenom(res.getString(res.getColumnIndex(ClientHelper.PRENOM)));
            client.setRaisonsocial(res.getString(res.getColumnIndex(ClientHelper.RAISONSOCIAL)));
            client.setEmail(res.getString(res.getColumnIndex(ClientHelper.EMAIL)));
            client.setTel(res.getString(res.getColumnIndex(ClientHelper.TEL)));
            client.setAdresse(res.getString(res.getColumnIndex(ClientHelper.ADRESSE)));

            try {
                client.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(ClientHelper.CREATED_AT))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

         };

        res.close();
        return client ;
    }

    public Client getLast() {
        Client client = null ;

        Cursor res =  mDb.rawQuery( "select * from "+ ClientHelper.TABLE_NAME +" order by " + ClientHelper.TABLE_KEY + " desc", null );

        if(res.moveToFirst()){
            client = new Client();

            client.setId(res.getLong(res.getColumnIndex(ClientHelper.TABLE_KEY)));
            client.setNom(res.getString(res.getColumnIndex(ClientHelper.NOM)));
            client.setPrenom(res.getString(res.getColumnIndex(ClientHelper.PRENOM)));
            client.setRaisonsocial(res.getString(res.getColumnIndex(ClientHelper.RAISONSOCIAL)));
            client.setEmail(res.getString(res.getColumnIndex(ClientHelper.EMAIL)));
            client.setTel(res.getString(res.getColumnIndex(ClientHelper.TEL)));
            client.setAdresse(res.getString(res.getColumnIndex(ClientHelper.ADRESSE)));

            try {
                client.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(ClientHelper.CREATED_AT))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

         };

        res.close();
        return client ;
    }

    @Override
    public ArrayList<Client> getAll() {
        ArrayList<Client> clients = new ArrayList<Client>();
        Client client = null;

        Cursor res = mDb.rawQuery("select * from " + ClientHelper.TABLE_NAME + " order by " + ClientHelper.TABLE_KEY + " desc ", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            client = new Client();


            client.setId(res.getLong(res.getColumnIndex(ClientHelper.TABLE_KEY)));
            client.setNom(res.getString(res.getColumnIndex(ClientHelper.NOM)));
            client.setPrenom(res.getString(res.getColumnIndex(ClientHelper.PRENOM)));
            client.setRaisonsocial(res.getString(res.getColumnIndex(ClientHelper.RAISONSOCIAL)));
            client.setEmail(res.getString(res.getColumnIndex(ClientHelper.EMAIL)));
            client.setTel(res.getString(res.getColumnIndex(ClientHelper.TEL)));
            client.setAdresse(res.getString(res.getColumnIndex(ClientHelper.ADRESSE)));

            try {
                client.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(ClientHelper.CREATED_AT))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            clients.add(client);
            res.moveToNext();
        }
        res.close();
        return clients;
    }

    public ArrayList<Client> getInterval(Date dateDebut, Date dateFin) {
        ArrayList<Client> clients = new ArrayList<Client>();
        Client client = null ;



        if (dateDebut==null) dateDebut=new Date("01/01/2015");
        if (dateFin==null) dateFin=new Date() ;

        Log.i("Debug", "SELECT * FROM " + ClientHelper.TABLE_NAME + " WHERE " + ClientHelper.CREATED_AT + "  BETWEEN '" + formatter.format(dateFin) + "' AND '" + formatter.format(dateDebut) + "' ORDER BY " +ClientHelper.TABLE_KEY + " DESC ") ;

        Cursor res =  mDb.rawQuery( "SELECT * FROM "+ClientHelper.TABLE_NAME + " WHERE "+ClientHelper.CREATED_AT + "  BETWEEN '" + formatter.format(dateDebut) + "' AND '" + formatter.format(dateFin) + "' ORDER BY " +ClientHelper.TABLE_KEY + " DESC ", null);
        res.moveToFirst();

        while(res.isAfterLast() == false){
            client = new Client();

            client.setId(res.getLong(res.getColumnIndex(ClientHelper.TABLE_KEY)));
            client.setNom(res.getString(res.getColumnIndex(ClientHelper.NOM)));
            client.setPrenom(res.getString(res.getColumnIndex(ClientHelper.PRENOM)));
            client.setRaisonsocial(res.getString(res.getColumnIndex(ClientHelper.RAISONSOCIAL)));
            client.setEmail(res.getString(res.getColumnIndex(ClientHelper.EMAIL)));
            client.setTel(res.getString(res.getColumnIndex(ClientHelper.TEL)));
            client.setAdresse(res.getString(res.getColumnIndex(ClientHelper.ADRESSE)));

            try {
                client.setCreated_at(DAOBase.formatter.parse(res.getString(res.getColumnIndex(ClientHelper.CREATED_AT))));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            clients.add(client);
            res.moveToNext();
        }
        res.close();
        Log.i("DEBUG",String.valueOf(clients.size()));
        return clients;
    }
}
