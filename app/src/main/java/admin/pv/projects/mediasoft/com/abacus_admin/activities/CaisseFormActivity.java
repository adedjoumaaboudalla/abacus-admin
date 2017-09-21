package admin.pv.projects.mediasoft.com.abacus_admin.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import admin.pv.projects.mediasoft.com.abacus_admin.R;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.ClientDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.DAOBase;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CaisseDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.PointVenteDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.database.CaisseHelper;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Client;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Caisse;
import admin.pv.projects.mediasoft.com.abacus_admin.model.PointVente;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Url;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Utiles;
import okhttp3.FormBody;

public class CaisseFormActivity extends AppCompatActivity {

    private static final int MAX_SIZE = 0;
    private static final int PROGRESS_DIALOG_ID = 1;

    private CaisseDAO caisseDAO;
    private Caisse caisse;
    private ProgressDialog mProgressBar;
    private Button valider;
    private AddCaisseTask addCaisseTask;
    private ImageButton pvBtn;
    private ArrayList<PointVente> pv;
    private EditText code;
    private EditText login;
    private EditText password;
    private EditText confirm;
    private EditText pointvente;
    private ListePointVenteAdapter listePointVenteAdapter;
    private PointVenteDAO pointVenteDAO;
    private PointVente pointVente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caisse);

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
                caisse = caisseDAO.getOne(id);
                if (caisse != null) {
                    init(caisse);
                    //if (produit.getImage() != null && !produit.getImage().equals("null") && !produit.getImage().equals("")) loadLocalImage(produit.getImage(),imageV);
                    //imageV.setImageBitmap(Utiles.loadImageFromExternalStorage(this,produit.getImage(),dossier));
                }
                else Toast.makeText(getApplicationContext(),  getString(R.string.echec), Toast.LENGTH_SHORT).show();
            }
        }

        pvBtn = (ImageButton) findViewById(R.id.typeBtn);
        pvBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout taxesLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.taxeliste,null);
                ListView lv = (ListView) taxesLayout.findViewById(R.id.listeView);

                listePointVenteAdapter = new ListePointVenteAdapter(pv) ;
                lv.setAdapter(listePointVenteAdapter);
                final AlertDialog.Builder builder = new AlertDialog.Builder(CaisseFormActivity.this) ;
                builder.setTitle(getString(R.string.choosepv)) ;
                builder.setView(taxesLayout) ;
                final AlertDialog alertdialog = builder.create();
                alertdialog.show();
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        pointvente.setText((String) pv.get(position).getLibelle());
                        pointVente = pv.get(position) ;
                        alertdialog.dismiss();
                    }
                });
            }
        });
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

        pointVenteDAO = new PointVenteDAO(CaisseFormActivity.this) ;
        pv = pointVenteDAO.getAll();
        pointVente = null ;

        code = (EditText) findViewById(R.id.code);
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);
        confirm = (EditText) findViewById(R.id.confirm);
        pointvente = (EditText) findViewById(R.id.pv);


        valider = (Button) findViewById(R.id.valider);

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCaisse();
            }
        });

    }


    private void init(Caisse caisse) {
        code.setText(caisse.getCode());
        login.setText(caisse.getLogin());
        pointvente.setText(pointVenteDAO.getOne(caisse.getId()).getLibelle());
    }





    public class ListePointVenteAdapter extends BaseAdapter {

        ArrayList<PointVente> pointVentes = new ArrayList<PointVente>() ;

        public ListePointVenteAdapter(ArrayList<PointVente> pv){
            pointVentes = pv ;
        }

        @Override
        public int getCount() {
            return pointVentes.size() ;
        }

        @Override
        public PointVente getItem(int position) {
            return pointVentes.get(position) ;
        }

        @Override
        public long getItemId(int position) {
            return pointVentes.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if(convertView == null) {
                convertView  = getLayoutInflater().inflate(R.layout.pointvente_item1, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            PointVente pointVente = (PointVente) getItem(position);


            if(pointVente != null) {
                holder.libelleTV.setText(pointVente.getLibelle());
                holder.contactTV.setText(getString(R.string.cnt) + pointVente.getTel());
                holder.villeTV.setText(pointVente.getVille() + " " + pointVente.getQuartier());

               /*
                if (pointVente.getImage()!=null) loadLocalImage(pointVente.getImage(), holder.imageView);
                else holder.imageView.setImageBitmap(null);
                 */
            };

            return convertView;
        }



        public void removeSelection() {
            notifyDataSetChanged();
        }
    }



    static class ViewHolder{
        TextView libelleTV ;
        TextView villeTV ;
        TextView contactTV ;

        public ViewHolder(View v) {
            libelleTV = (TextView)v.findViewById(R.id.libelle);
            villeTV = (TextView)v.findViewById(R.id.ville);
            contactTV = (TextView)v.findViewById(R.id.contact);
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_caisse_form, menu);
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
                        addCaisseTask.cancel(true);
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

    private void addCaisse() {
        if (isValid()){
            if (caisse==null)caisse = new Caisse() ;
            caisse.setLogin(login.getText().toString());
            caisse.setCode(code.getText().toString());
            caisse.setPassword(password.getText().toString());
            caisse.setPointVente(pointVente.getId());

            addCaisseTask = new AddCaisseTask(caisse) ;
            addCaisseTask.execute() ;
        }
        else {
            Toast.makeText(getApplicationContext(), getString(R.string.data_errors1), Toast.LENGTH_LONG).show();
        }
    }

    private boolean isValid() {
        if (code.getText().length() <= 0) return false ;
        if (login.getText().length() < 6) return false ;
        if (password.getText().length() < 6) return false ;
        if (!password.getText().toString().equals(confirm.getText().toString())) return false ;
        if (pointVente == null) return false ;

        return true;
    }

    public class AddCaisseTask extends AsyncTask<Void, Integer, String> {
        Caisse caisse ;

        public AddCaisseTask(Caisse p) {
            caisse = p ;
        }

        @Override
        protected String doInBackground(Void... params) {
            Date d = new Date() ;

            FormBody.Builder formBuilder = new FormBody.Builder() ;
           formBuilder.add(CaisseHelper.TABLE_KEY, String.valueOf(caisse.getId()));
           formBuilder.add(CaisseHelper.LOGIN, String.valueOf(caisse.getLogin()));
           formBuilder.add("password", String.valueOf(caisse.getPassword()));
           formBuilder.add(CaisseHelper.CODE, String.valueOf(caisse.getCode()));
           formBuilder.add(CaisseHelper.POINTVENTE_ID, String.valueOf(caisse.getPointVente_id()));
           formBuilder.add(CaisseHelper.IMEI, String.valueOf(caisse.getImei()));
           formBuilder.add(CaisseHelper.CREATED_AT, DAOBase.formatter.format(caisse.getCreated_at()));
            //nameValuePairs.add(new BasicNameValuePair("caisse_id", String.valueOf(caisseDAO.getLast().getId())));

            String result = "";
            try {
                result = Utiles.POST(Url.getPostCaisseUrl(CaisseFormActivity.this), formBuilder.build());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("URL", Url.getPostCaisseUrl(CaisseFormActivity.this));

            Log.e("Reponse",result) ;

            if (result.split(":").length == 2 && result.contains("OK")) {
                // Si le caisse est un caisse qui ne se trouve pas sur le serveur
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
                if (caisse.getId() == 0) {
                    caisse.setId(Long.valueOf(result.split(":")[1]));
                    caisseDAO.add(caisse);
                    Toast.makeText(getApplicationContext(),getString(R.string.caissecreer),Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(getApplicationContext(),getString(R.string.caisseupdate),Toast.LENGTH_LONG).show();
                    caisseDAO.update(caisse);
                }
            }

            dismissDialog(PROGRESS_DIALOG_ID);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG_ID);
            if (caisseDAO==null)caisseDAO = new CaisseDAO(getApplicationContext()) ;
        }
    }

    private void clean() {
        code.setText("");
        login.setText("");
        password.setText("");
        confirm.setText("");
        pointvente.setText("");
        pointVente = null ;
    }


}
