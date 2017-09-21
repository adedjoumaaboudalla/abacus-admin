package admin.pv.projects.mediasoft.com.abacus_admin.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import admin.pv.projects.mediasoft.com.abacus_admin.R;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.ClientDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.DAOBase;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.OperationDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.PointVenteDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.database.PointVenteHelper;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Client;
import admin.pv.projects.mediasoft.com.abacus_admin.model.PointVente;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Url;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Utiles;
import okhttp3.FormBody;

public class PointventeFormActivity extends AppCompatActivity {

    private static final int MAX_SIZE = 0;
    private static final int PROGRESS_DIALOG_ID = 1;
    private EditText libelle;
    private EditText pays;
    private EditText quartier;
    private EditText ville;
    private EditText contact;
    private PointVenteDAO pointventeDAO;
    private PointVente pointvente;
    private ProgressDialog mProgressBar;
    private Button valider;
    private AddPointventeTask addPointventeTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pointvente_form);

        setupToolbar() ;
        initialisation() ;


        Intent intent = null ;
        intent = getIntent() ;
        if (intent!=null){
            long id = intent.getLongExtra("ID", 0) ;
            //int type = intent.getIntExtra("TYPE",0) ;
            //if (type==0) dossier = Utiles.FLUFF_PHOTO_PROFILS ;
            Log.e("DEBUG", String.valueOf(id)) ;
            if (id != 0){
                pointvente = pointventeDAO.getOne(id);
                if (pointvente != null) {
                    init(pointvente);
                    //if (produit.getImage() != null && !produit.getImage().equals("null") && !produit.getImage().equals("")) loadLocalImage(produit.getImage(),imageV);
                    //imageV.setImageBitmap(Utiles.loadImageFromExternalStorage(this,produit.getImage(),dossier));
                }
                else Toast.makeText(getApplicationContext(),  getString(R.string.echec), Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_back);
        ab.setDisplayHomeAsUpEnabled(true);
    }


    private void initialisation() {
        ville = (EditText) findViewById(R.id.ville);
        libelle = (EditText) findViewById(R.id.libelle);
        quartier = (EditText) findViewById(R.id.quartier);
        contact = (EditText) findViewById(R.id.tel);
        pays = (EditText) findViewById(R.id.pays);
        pointventeDAO = new PointVenteDAO(this) ;


        valider = (Button) findViewById(R.id.valider);

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPointvente();
            }
        });

    }


    private void init(PointVente pointvente) {
        libelle.setText(pointvente.getLibelle());
        pays.setText(pointvente.getPays());
        quartier.setText(pointvente.getQuartier());
        contact.setText(pointvente.getTel());
        ville.setText(pointvente.getVille());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pointvente_form, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        super.onCreateDialog(id);
        if (mProgressBar ==null){
            mProgressBar = new ProgressDialog(this) ;
            mProgressBar.setCancelable(true);
            mProgressBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    try {
                        addPointventeTask.cancel(true);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            mProgressBar.setTitle(getString(R.string.send_loding));
            mProgressBar.setMessage(getString(R.string.wait));
            mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressBar.setMax(MAX_SIZE);
        }

        return mProgressBar ;
    }

    private void addPointvente() {
        if (isValid()){
            if (pointvente==null)pointvente = new PointVente() ;
            Client client = new ClientDAO(this).getLast() ;
            pointvente.setPays(pays.getText().toString());
            pointvente.setLibelle(libelle.getText().toString());
            pointvente.setQuartier(quartier.getText().toString());
            pointvente.setVille(ville.getText().toString());
            pointvente.setTel(contact.getText().toString());
            pointvente.setUtilisateur_id(client.getId());

            addPointventeTask = new AddPointventeTask(pointvente) ;
            addPointventeTask.execute() ;
        }
        else {
            Toast.makeText(getApplicationContext(), getString(R.string.data_errors1), Toast.LENGTH_LONG).show();
        }
    }

    private boolean isValid() {
        if (libelle.getText().length() <= 0) return false ;
        if (pays.getText().length() <= 0) return false ;
        if (ville.getText().length() <= 0) return false ;
        if (contact.getText().length() <= 0) return false ;

        return true;
    }

    public class AddPointventeTask extends AsyncTask<Void, Integer, String> {
        PointVente pointvente ;

        public AddPointventeTask(PointVente p) {
            pointvente = p ;
        }

        @Override
        protected String doInBackground(Void... params) {
            Date d = new Date() ;

            FormBody.Builder formBuilder = new FormBody.Builder() ;
            formBuilder.add(PointVenteHelper.LIBELLE, pointvente.getLibelle());
            formBuilder.add(PointVenteHelper.TABLE_KEY, String.valueOf(pointvente.getId()));
            formBuilder.add(PointVenteHelper.VILLE, String.valueOf(pointvente.getVille()));
            formBuilder.add(PointVenteHelper.QUARTIER, String.valueOf(pointvente.getQuartier()));
            formBuilder.add(PointVenteHelper.PAYS, String.valueOf(pointvente.getPays()));
            formBuilder.add(PointVenteHelper.TEL, String.valueOf(pointvente.getTel()));
            formBuilder.add(PointVenteHelper.UTILISATEUR_ID, String.valueOf(pointvente.getUtilisateur()));
            formBuilder.add(PointVenteHelper.CREATED_AT, DAOBase.formatter.format(pointvente.getCreated_at()));
             //formBuilder.add("pointvente_id", String.valueOf(pointVenteDAO.getLast().getId())));

            String result = " ";
            try {
                result = Utiles.POST(Url.getPostPointVenteUrl(PointventeFormActivity.this), formBuilder.build());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.e("URL", Url.getPostPointVenteUrl(PointventeFormActivity.this));

            Log.e("Reponse",result) ;

            if (result.split(":").length == 2 && result.contains("OK")) {
                // Si le pointVente est un pointVente qui ne se trouve pas sur le serveur
            }
            else result = "KO" ;

            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("KO")) Toast.makeText(getApplicationContext(),getString(R.string.echec_ajout),Toast.LENGTH_LONG).show();
            else{
                clean();
                if (pointvente.getId() == 0) {
                    pointvente.setId(Long.valueOf(result.split(":")[1]));
                    pointventeDAO.add(pointvente);
                    Toast.makeText(getApplicationContext(),getString(R.string.pointventecreer),Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(getApplicationContext(),getString(R.string.pointventeupdate),Toast.LENGTH_LONG).show();
                    pointventeDAO.update(pointvente);
                }
            }

            dismissDialog(PROGRESS_DIALOG_ID);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG_ID);
            if (pointventeDAO==null)pointventeDAO = new PointVenteDAO(getApplicationContext()) ;
        }
    }

    private void clean() {
        libelle.setText("");
        quartier.setText("");
        ville.setText("");
        contact.setText("");
        pays.setText("");
        pointvente = null ;
    }


}
