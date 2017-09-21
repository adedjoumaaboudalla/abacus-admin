package admin.pv.projects.mediasoft.com.abacus_admin;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import admin.pv.projects.mediasoft.com.abacus_admin.activities.AccueilActivity;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.BalanceDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.BalanceLigneDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.BilletDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CaisseDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CategorieProduitDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.ClientDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CommercialDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CompteBanqueDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.DAOBase;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.LigneDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.ModePayementDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.MouvementDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.OperationDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.PartenaireDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.PointVenteDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.ProduitDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.Produit_pointventeDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.TypeOperationDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.fragment.OperationFragment;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Balance;
import admin.pv.projects.mediasoft.com.abacus_admin.model.BalanceLigne;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Billet;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Caisse;
import admin.pv.projects.mediasoft.com.abacus_admin.model.CategorieProduit;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Client;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Commercial;
import admin.pv.projects.mediasoft.com.abacus_admin.model.CompteBanque;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Ligne;
import admin.pv.projects.mediasoft.com.abacus_admin.model.ModePayement;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Mouvement;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Operation;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Partenaire;
import admin.pv.projects.mediasoft.com.abacus_admin.model.PointVente;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Produit;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Produit_pointvente;
import admin.pv.projects.mediasoft.com.abacus_admin.model.TypeOperation;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Url;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Utiles;
import okhttp3.FormBody;

public class MainActivity extends AppCompatActivity {

    private static final String MAUVAIS = "KO";
    private static final String BON = "OK";
    Button quitter = null ;
    Button valider = null ;
    private ProgressDialog mProgressBar;
    final static int PROGRESS_DIALOG_ID = 0 ;
    private int MAX_SIZE;

    EditText login = null ;
    EditText password = null ;
    private Toolbar mToolbar;
    private CommercialDAO commercialDAO;
    private CompteBanqueDAO compteBanqueDAO;
    private ClientDAO clientDAO;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quitter = (Button) findViewById(R.id.annuler);
        valider = (Button) findViewById(R.id.valider);

        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);


        if (new ClientDAO(this).getLast()!=null && !getIntent().hasExtra("change")){
            startActivity(new Intent(MainActivity.this,AccueilActivity.class));
            finish();
        }

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Utiles.isConnected(MainActivity.this)){
                    Toast.makeText(MainActivity.this, R.string.noconnexion, Toast.LENGTH_SHORT).show();
                    return;
                }
                LoadInitDataTask loadInitDataTask = new LoadInitDataTask(login.getText().toString(),password.getText().toString()) ;
                loadInitDataTask.execute(Url.getCheckAndInitCaisse(MainActivity.this)) ;

            }
        });

        quitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId() ;
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        super.onCreateDialog(id);
        if (mProgressBar ==null){
            mProgressBar = new ProgressDialog(this) ;
            mProgressBar.setCancelable(false);
            mProgressBar.setTitle(getString(R.string.send_loding));
            mProgressBar.setMessage(getString(R.string.wait));
            mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressBar.setMax(MAX_SIZE);
        }

        return mProgressBar ;
    }


    public class LoadInitDataTask extends AsyncTask<String,Void,String> {
        Calendar calendar = Calendar.getInstance() ;
        private ProduitDAO produitDAO;
        String login = null ;
        String password = null ;
        private OperationDAO operationDAO;
        private CaisseDAO caisseDAO;
        private MouvementDAO mouvementDAO;
        private BilletDAO billetDAO;
        private ModePayementDAO modepayementDAO;
        private TypeOperationDAO typeOperationDAO;
        private Produit_pointventeDAO produit_pointventeDAO;
        private CategorieProduitDAO categorieproduitDAO;
        private PointVenteDAO pointventeDAO;
        private PartenaireDAO partenaireDAO;
        private LigneDAO ligneDAO;
        private BalanceDAO balanceDAO;
        private BalanceLigneDAO balanceLigneDAO;
        private String res;

        public LoadInitDataTask(String login, String password){
            this.login = login ;
            this.password = password ;
        }

        @Override
        protected String doInBackground(String... url) {
            FormBody.Builder formBuilder = new FormBody.Builder() ;
            formBuilder.add("login", login);
            formBuilder.add("password", password);

            Log.e("URL",url[0]) ;

            String result = " ";
            try {
                result = Utiles.POST(url[0], formBuilder.build());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.e("REPONSEEEEEEEEEEEEEEEE", result);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show();
                }
            });

            int nbre = 0 ;
            int size = 0 ;
            try {

                JSONObject obj = new JSONObject(result);
                // list_annonces = null;
                if (obj != null) {
                    String reponse = obj.getString("reponse");

                    if (!reponse.equals("OK")) return reponse ;


                    caisseDAO.clean() ;
                    mouvementDAO.clean() ;
                    clientDAO.clean() ;
                    produitDAO.clean() ;
                    produit_pointventeDAO.clean() ;
                    operationDAO.clean() ;
                    partenaireDAO.clean() ;
                    commercialDAO.clean() ;
                    compteBanqueDAO.clean() ;
                    categorieproduitDAO.clean() ;
                    typeOperationDAO.clean() ;
                    modepayementDAO.clean() ;
                    billetDAO.clean() ;
                    pointventeDAO.clean() ;
                    ligneDAO.clean() ;
                    balanceDAO.clean() ;
                    balanceLigneDAO.clean() ;

                    JSONObject clientObj = obj.getJSONObject("client");
                    size = clientObj.length() ;
                    if (size >0) {
                        Client client = new Client();
                        client.setId(clientObj.getLong("id"));
                        client.setNom(clientObj.getString("nom"));
                        client.setPrenom(clientObj.getString("prenom"));
                        client.setRaisonsocial(clientObj.getString("raisonsocial"));
                        client.setEmail(clientObj.getString("mail"));
                        client.setTel(clientObj.getString("tel"));
                        client.setAdresse(clientObj.getString("adresse"));
                        try {
                            client.setCreated_at(DAOBase.formatter.parse(clientObj.getString("created_at")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        clientDAO.add(client) ;
                    }


                    JSONArray caisseArr = obj.getJSONArray("caisses");
                    size = caisseArr.length() ;
                    JSONObject caisseObj = null ;
                    Caisse caisse = null ;
                    for (int i = 0; i < size; i++) {
                        caisseObj = caisseArr.getJSONObject(i);
                        caisse = new Caisse();
                        caisse.setId(caisseObj.getLong("id"));
                        caisse.setCode(caisseObj.getString("code"));
                        caisse.setLogin(caisseObj.getString("login"));
                        caisse.setImei(caisseObj.getString("imei"));
                        caisse.setPointVente(caisseObj.getLong("pointvente_id"));
                        try {
                            caisse.setCreated_at(DAOBase.formatter.parse(caisseObj.getString("created_at")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        caisseDAO.add(caisse) ;
                        Log.v("CAISSE", String.valueOf(caisse.getId())) ;
                    }


                    JSONArray compte_banqueArr = obj.getJSONArray("compte_banques");
                    size = compte_banqueArr.length() ;
                    JSONObject compte_banqueObj = null ;
                    CompteBanque compteBanque = null ;
                    for (int i = 0; i < size; i++) {
                        compte_banqueObj = compte_banqueArr.getJSONObject(i);
                        compteBanque = new CompteBanque();
                        compteBanque.setId_externe(compte_banqueObj.getLong("id"));
                        compteBanque.setCode(compte_banqueObj.getString("code"));
                        compteBanque.setLibelle(compte_banqueObj.getString("libelle"));
                        compteBanque.setUtilisateur_id(compte_banqueObj.getLong("client_id"));

                        compteBanqueDAO.add(compteBanque) ;
                        Log.v("CAISSE", String.valueOf(compteBanque.getId_externe())) ;
                    }

                    JSONArray pointventeArr = obj.getJSONArray("pointventes");
                    size = pointventeArr.length() ;
                    JSONObject pointventeObj = null ;
                    PointVente pointvente = null ;

                    // Si la base de données n'est pas vide et qu'il ya + de 1000 annonce on fait un clean

                    for (int i = 0; i < size; i++) {
                        pointventeObj = pointventeArr.getJSONObject(i);
                        pointvente = new PointVente();
                        pointvente.setId(pointventeObj.getLong("id"));
                        pointvente.setLibelle(pointventeObj.getString("libelle"));
                        //pointvente.setPays(pointventeObj.getString("pays"));
                        pointvente.setUtilisateur_id(pointventeObj.getLong("utilisateur_id"));
                        pointvente.setQuartier(pointventeObj.getString("quartier"));
                        pointvente.setTel(pointventeObj.getString("tel"));
                        pointvente.setVille(pointventeObj.getString("ville"));
                        try {
                            pointvente.setCreated_at(DAOBase.formatter.parse(pointventeObj.getString("created_at")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        pointventeDAO.add(pointvente) ;
                    }


                    JSONArray partenaireArr = obj.getJSONArray("partenaires");
                    size = partenaireArr.length() ;
                    JSONObject partenaireObj = null ;
                    Partenaire partenaire = null ;
                    for (int i = 0; i < size; i++) {
                        partenaireObj = partenaireArr.getJSONObject(i);
                        partenaire = new Partenaire() ;

                        partenaire.setId_externe(partenaireObj.getLong("id"));
                        partenaire.setRaisonsocial(partenaireObj.getString("raisonsocial"));
                        partenaire.setContact(partenaireObj.getString("contact"));
                        partenaire.setUtilisateur_id(partenaireObj.getLong("client"));
                        partenaire.setAdresse(partenaireObj.getString("adresse"));
                        partenaire.setTypepersonne(partenaireObj.getString("typepersonne"));
                        partenaire.setNom(partenaireObj.getString("nom"));
                        partenaire.setPrenom(partenaireObj.getString("prenom"));
                        partenaire.setSexe(partenaireObj.getString("sexe"));
                        partenaire.setPointvente_id(partenaireObj.getLong("pointvente_id"));
                        try {
                            partenaire.setCreated_at(DAOBase.formatter.parse(partenaireObj.getString("created_at")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        partenaireDAO.add(partenaire) ;
                    }



                    JSONArray commercialArr = obj.getJSONArray("commercials");
                    size = commercialArr.length() ;
                    JSONObject commercialObj = null ;
                    Commercial commercial = null ;
                    for (int i = 0; i < size; i++) {
                        commercialObj = commercialArr.getJSONObject(i);
                        commercial = new Commercial() ;

                        commercial.setId_externe(commercialObj.getLong("id"));
                        commercial.setContact(commercialObj.getString("contact"));
                        commercial.setAdresse(commercialObj.getString("adresse"));
                        commercial.setNom(commercialObj.getString("nom"));
                        commercial.setPrenom(commercialObj.getString("prenom"));
                        commercial.setSexe(commercialObj.getString("sexe"));
                        commercial.setPointvente_id(commercialObj.getLong("pointvente_id"));
                        try {
                            commercial.setCreated_at(DAOBase.formatter.parse(commercialObj.getString("created_at")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        commercialDAO.add(commercial) ;
                    }


                    JSONArray produitArr = obj.getJSONArray("produits");
                    size = produitArr.length() ;
                    JSONObject produitObj = null ;
                    Produit produit = null ;
                    for (int i = 0; i < size; i++) {
                        produitObj = produitArr.getJSONObject(i);
                        produit = new Produit() ;

                        produit.setId_externe(produitObj.getLong("id"));
                        produit.setUnite(produitObj.getString("unite"));
                        produit.setLibelle(produitObj.getString("libelle"));
                        produit.setUtilisateur_id(produitObj.getLong("client_id"));
                        if (!produitObj.getString("categorieproduit_id").equals("null"))produit.setCategorie_id(produitObj.getLong("categorieproduit_id"));
                        produit.setCode(produitObj.getString("code"));
                        produit.setImage(produitObj.getString("image"));
                        if (produitObj.getInt("prixmodifiable")!=0)produit.setModifiable(1);
                        else produit.setModifiable(0);
                        produit.setEtat(2);
                        produit.setPrixA(produitObj.getDouble("prixachat"));
                        produit.setPrixV(produitObj.getDouble("prixvente"));
                        //produit.setQuantite(produitObj.getInt("pointvente_id"));
                        try {
                            produit.setCreated_at(DAOBase.formatter.parse(produitObj.getString("created_at")));
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        produitDAO.add(produit) ;
                    }

                    JSONArray categorieProduitArr = obj.getJSONArray("categorieproduits");
                    size = categorieProduitArr.length() ;
                    JSONObject categorieProduitObj = null ;
                    CategorieProduit categorieProduit = null ;
                    for (int i = 0; i < size; i++) {
                        categorieProduitObj = categorieProduitArr.getJSONObject(i);
                        categorieProduit = new CategorieProduit() ;

                        categorieProduit.setId(categorieProduitObj.getLong("id"));
                        categorieProduit.setCode(categorieProduitObj.getString("code"));
                        categorieProduit.setEtat(1);
                        categorieProduit.setLibelle(categorieProduitObj.getString("libelle"));
                        Log.e("CP"+i,categorieProduit.getLibelle()) ;

                        try {
                            categorieProduit.setCreated_at(DAOBase.formatter.parse(categorieProduitObj.getString("created_at")));
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        categorieproduitDAO.add(categorieProduit) ;
                    }

                    JSONArray typeoperationArr = obj.getJSONArray("typeoperations");
                    size = typeoperationArr.length() ;
                    JSONObject typeoperationObj = null ;
                    TypeOperation typeoperation = null ;
                    for (int i = 0; i < size; i++) {
                        typeoperationObj = typeoperationArr.getJSONObject(i);
                        typeoperation = new TypeOperation() ;

                        typeoperation.setCode(typeoperationObj.getString("code"));
                        typeoperation.setLibelle(typeoperationObj.getString("libelle"));

                        try {
                            typeoperation.setCreated_at(DAOBase.formatter.parse(typeoperationObj.getString("created_at")));
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                        typeOperationDAO.add(typeoperation) ;
                    }



                    JSONArray produit_pointventeArr = obj.getJSONArray("produit_pointventes");
                    size = produit_pointventeArr.length() ;
                    JSONObject produit_pointventeObj = null ;
                    Produit_pointvente produit_pointvente = null ;
                    for (int i = 0; i < size; i++) {
                        produit_pointventeObj = produit_pointventeArr.getJSONObject(i);
                        produit_pointvente = new Produit_pointvente() ;

                        produit_pointvente.setPointvente_id(produit_pointventeObj.getLong("pointvente_id"));
                        produit_pointvente.setProduit_id(produit_pointventeObj.getLong("produit_id"));

                        produit_pointventeDAO.add(produit_pointvente) ;
                    }




                    JSONArray balancesArr = obj.getJSONArray("balances");
                    size = balancesArr.length() ;
                    JSONObject balancesObj = null ;
                    Balance balance = null ;
                    for (int i = 0; i < size; i++) {
                        balancesObj = balancesArr.getJSONObject(i);
                        balance = new Balance() ;

                        balance.setId(balancesObj.getLong("id"));
                        balance.setLibelle(balancesObj.getString("libelle"));
                        balance.setPointvente_id(balancesObj.getLong("pointvente_id"));

                        try {
                            balance.setDatefin(DAOBase.formatter.parse(balancesObj.getString("datefin")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            balance.setDatedebut(DAOBase.formatter.parse(balancesObj.getString("datedeb")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        balanceDAO.add(balance) ;
                    }




                    JSONArray lignesArr = obj.getJSONArray("lignes");
                    size = lignesArr.length() ;
                    JSONObject lignesObj = null ;
                    Ligne ligne = null ;
                    for (int i = 0; i < size; i++) {
                        lignesObj = lignesArr.getJSONObject(i);
                        ligne = new Ligne() ;

                        ligne.setId(lignesObj.getLong("id"));
                        ligne.setOrdre(lignesObj.getInt("ordre"));
                        ligne.setLibelle(lignesObj.getString("libelle"));
                        ligne.setCode(lignesObj.getString("code"));
                        ligne.setNature(lignesObj.getString("nature"));

                        try {
                            ligne.setUpdated_at(DAOBase.formatter.parse(lignesObj.getString("updated_at")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            ligne.setCreated_at(DAOBase.formatter.parse(lignesObj.getString("created_at")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        ligneDAO.add(ligne) ;
                    }






                    JSONArray balance_lignesArr = obj.getJSONArray("balance_lignes");
                    size = balance_lignesArr.length() ;
                    JSONObject balance_lignesObj = null ;
                    BalanceLigne balanceLigne = null ;
                    for (int i = 0; i < size; i++) {
                        balance_lignesObj = balance_lignesArr.getJSONObject(i);
                        balanceLigne = new BalanceLigne() ;

                        balanceLigne.setId(balance_lignesObj.getLong("id"));
                        balanceLigne.setLigne_id(balance_lignesObj.getLong("ligne_id"));
                        balanceLigne.setBalance_id(balance_lignesObj.getLong("balance_id"));
                        balanceLigne.setSolde_report(balance_lignesObj.getDouble("solde_report"));
                        balanceLigne.setSolde_cloture(balance_lignesObj.getDouble("solde_cloture"));
                        balanceLigne.setMouvement(balance_lignesObj.getDouble("mouvement"));

                        try {
                            balanceLigne.setUpdated_at(DAOBase.formatter.parse(balancesObj.getString("updated_at")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            balanceLigne.setCreated_at(DAOBase.formatter.parse(balancesObj.getString("created_at")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        balanceLigneDAO.add(balanceLigne) ;
                    }



                    JSONArray modepayementArr = obj.getJSONArray("modepayements");
                    size = modepayementArr.length() ;
                    JSONObject modepayementObj = null ;
                    ModePayement modepayement = null ;
                    for (int i = 0; i < size; i++) {
                        modepayementObj = modepayementArr.getJSONObject(i);
                        modepayement = new ModePayement() ;

                        modepayement.setCode(modepayementObj.getString("code"));
                        modepayement.setLibelle(modepayementObj.getString("libelle"));

                        try {
                            modepayement.setCreated_at(DAOBase.formatter.parse(modepayementObj.getString("created_at")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        modepayementDAO.add(modepayement) ;
                    }

                    JSONArray billetsArr = obj.getJSONArray("billets");
                    size = billetsArr.length() ;
                    JSONObject billetObj = null ;
                    Billet billet = null ;
                    for (int i = 0; i < size; i++) {
                        billetObj = billetsArr.getJSONObject(i);
                        billet = new Billet() ;

                        billet.setId(billetObj.getLong("id"));
                        billet.setLibelle(billetObj.getString("libelle"));
                        billet.setMontant(billetObj.getDouble("montant"));

                        billetDAO.add(billet) ;
                    }

                    return BON ;

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return MAUVAIS ;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            dismissDialog(PROGRESS_DIALOG_ID);
            if (result.equals(BON)) {
                Toast.makeText(MainActivity.this, R.string.load_success, Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                editor.putBoolean("FIRST",false) ;
                editor.commit();
                Intent intent = new Intent(MainActivity.this,AccueilActivity.class);
                startActivity(intent);
                finish();
            }
            else if (result.equals("error2")){
                Toast.makeText(MainActivity.this, R.string.aucunuser, Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(MainActivity.this, R.string.load_echec, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_DIALOG_ID);
            produitDAO = new ProduitDAO(MainActivity.this);
            commercialDAO = new CommercialDAO(MainActivity.this);
            compteBanqueDAO = new CompteBanqueDAO(MainActivity.this);
            partenaireDAO = new PartenaireDAO(MainActivity.this);
            pointventeDAO = new PointVenteDAO(MainActivity.this);
            caisseDAO = new CaisseDAO(MainActivity.this);
            mouvementDAO = new MouvementDAO(MainActivity.this);
            produit_pointventeDAO = new Produit_pointventeDAO(MainActivity.this);
            operationDAO = new OperationDAO(MainActivity.this);
            categorieproduitDAO = new CategorieProduitDAO(MainActivity.this);
            typeOperationDAO = new TypeOperationDAO(MainActivity.this);
            modepayementDAO = new ModePayementDAO(MainActivity.this);
            billetDAO = new BilletDAO(MainActivity.this);
            clientDAO = new ClientDAO(MainActivity.this);
            ligneDAO = new LigneDAO(MainActivity.this);
            balanceDAO = new BalanceDAO(MainActivity.this);
            balanceLigneDAO = new BalanceLigneDAO(MainActivity.this);
        }
    }




/*
    public class LoadOperationMoreTask extends AsyncTask<String,Void,String> {
        Calendar calendar = Calendar.getInstance() ;
        private ProduitDAO produitDAO;
        String login = null ;
        String password = null ;
        private OperationDAO operationDAO;
        private MouvementDAO mouvementDAO;
        private PayementDAO payementDAO;
        private ClientDAO clientDAO;
        private String res;
        private static final String MAUVAIS = "KO";
        private static final String BON = "OK";

        String dd = null ;
        String df = null ;
        public LoadOperationMoreTask(){

        }

        public LoadOperationMoreTask(String dateDebut, String dateFin) {
            dd = dateDebut ;
            df = dateFin ;
        }

        @Override
        protected String doInBackground(String... url) {

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            formBuilder.add("client", String.valueOf(clientDAO.getLast().getId())));
            if (operationDAO.getLast()!=null)formBuilder.add("operation", String.valueOf(operationDAO.getLast().getId())));
            else  formBuilder.add("operation", "0"));

            final String result = Utiles.POST(Url.getLoadOperationsUrl(MainActivity.this), nameValuePairs);

            Log.e("REPONSEEEEEEEEEEEEEEEE", result);

            int nbre = 0 ;
            int size = 0 ;
            try {

                JSONObject obj = new JSONObject(result);
                // list_annonces = null;
                if (obj != null) {
                    String reponse = obj.getString("reponse");

                    if (!reponse.equals("OK")) return reponse ;

                    //operationDAO.clean() ;
                    //mouvementDAO.clean() ;

                    JSONArray mouvementArr = obj.getJSONArray("mouvements");
                    size = mouvementArr.length() ;
                    JSONObject mouvementObj = null ;
                    Mouvement mouvement = null ;
                    for (int i = 0; i < size; i++) {
                        mouvementObj = mouvementArr.getJSONObject(i);
                        mouvement = new Mouvement();
                        mouvement.setId(mouvementObj.getLong("id"));
                        mouvement.setEntree(mouvementObj.getInt("entree"));
                        mouvement.setPrixA(mouvementObj.getDouble("prix_achat"));
                        mouvement.setPrixV(mouvementObj.getDouble("prix_vente"));
                        mouvement.setQuantite(mouvementObj.getInt("quantite"));
                        mouvement.setCmup(mouvementObj.getDouble("cmup"));
                        mouvement.setRestant(mouvementObj.getInt("restant"));
                        mouvement.setProduit_id(mouvementObj.getLong("produit_id"));
                        mouvement.setOperation_id(mouvementObj.getLong("operation_id"));
                        mouvement.setProduit(mouvementObj.getString("produit"));
                        try {
                            mouvement.setCreated_at(DAOBase.formatter.parse(mouvementObj.getString("created_at")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        mouvementDAO.add(mouvement) ;
                    }

                    JSONArray payementArr = obj.getJSONArray("payements");
                    size = payementArr.length() ;
                    JSONObject payementObj = null ;
                    Payement payement = null ;
                    for (int i = 0; i < size; i++) {
                        payementObj = payementArr.getJSONObject(i);
                        payement = new Payement();
                        payement.setId(payementObj.getLong("id"));
                        payement.setId_externe(payementObj.getLong("id"));
                        payement.setMontant(payementObj.getDouble("montant"));
                        payement.setModepayement(payementObj.getString("modepayement"));
                        payement.setOperation_id(payementObj.getLong("operation_id"));
                        try {
                            payement.setCreated_at(DAOBase.formatter.parse(payementObj.getString("created_at")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        payementDAO.add(payement) ;
                    }


                    JSONArray operationArr = obj.getJSONArray("operations");
                    size = operationArr.length() ;
                    JSONObject operationObj = null ;
                    Operation operation = null ;
                    for (int i = 0; i < size; i++) {
                        operationObj = operationArr.getJSONObject(i);
                        operation = new Operation();
                        operation.setId_externe(operationObj.getLong("id"));
                        operation.setId(operationObj.getLong("id"));
                        operation.setAnnuler(operationObj.getInt("annuler"));
                        operation.setAttente(0);
                        operation.setCaisse(operationObj.getLong("caisse_id"));
                        if (!operationObj.getString("commercial_id").equals("null"))operation.setCommercialid(operationObj.getLong("commercial_id"));
                        operation.setEntree(operationObj.getInt("entree"));
                        if (!operationObj.getString("partenaire_id").equals("null"))operation.setPartenaire_id(operationObj.getLong("partenaire_id"));
                        operation.setPayer(operationObj.getInt("payer"));
                        operation.setNbreproduit(operationObj.getInt("nbreproduit"));
                        operation.setTypeOperation_id(operationObj.getString("typeoperation"));
                        operation.setDescription(operationObj.getString("description"));
                        operation.setClient(operationObj.getString("client"));
                        operation.setMontant(operationObj.getDouble("montant"));
                        operation.setRecu(operationObj.getDouble("recu"));
                        operation.setRemise(operationObj.getDouble("remise"));
                        operation.setMontant_ammor(operationObj.getDouble("montant_amorti"));
                        operation.setToken(operationObj.getString("token"));
                        try {
                            operation.setDateoperation(DAOBase.formatter.parse(operationObj.getString("created_at")));
                            operation.setCreated_at(DAOBase.formatter.parse(operationObj.getString("created_at")));
                            operation.setDateecheance(DAOBase.formatter2.parse(operationObj.getString("date_echeance")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        operationDAO.add(operation) ;
                        /*
                            if (operation.getTypeOperation_id().equals(OperationDAO.ACHAT)|| operation.getTypeOperation_id().equals(OperationDAO.VENTE)) {
                                if (mouvementDAO.getMany(operation.getId_externe()).size() > 0) operationDAO.add(operation);
                            }
                            else operationDAO.add(operation) ;

                    }

                    if (size>0)return BON ;

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return MAUVAIS ;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //getActivity().dismissDialog(AccueilActivity.PROGRESS_DIALOG_ID);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //getActivity().showDialog(AccueilActivity.PROGRESS_DIALOG_ID);
            clientDAO = new ClientDAO(MainActivity.this);
            payementDAO = new PayementDAO(MainActivity.this);
            mouvementDAO = new MouvementDAO(MainActivity.this);
            operationDAO = new OperationDAO(MainActivity.this);
            produitDAO = new ProduitDAO(MainActivity.this);
        }
    }
    */


}
