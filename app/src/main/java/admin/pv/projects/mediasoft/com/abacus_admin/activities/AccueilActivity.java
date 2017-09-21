package admin.pv.projects.mediasoft.com.abacus_admin.activities;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import admin.pv.projects.mediasoft.com.abacus_admin.MainActivity;
import admin.pv.projects.mediasoft.com.abacus_admin.R;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.BilletDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CaisseDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CategorieProduitDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.ClientDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CommercialDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.ModePayementDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.MouvementDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.OperationDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.PartenaireDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.PointVenteDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.ProduitDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.Produit_pointventeDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.TypeOperationDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.fragment.AccueilFragment;
import admin.pv.projects.mediasoft.com.abacus_admin.fragment.CategorieFragment;
import admin.pv.projects.mediasoft.com.abacus_admin.fragment.CommercialFragment;
import admin.pv.projects.mediasoft.com.abacus_admin.fragment.EtatFragment;
import admin.pv.projects.mediasoft.com.abacus_admin.fragment.OperationFragment;
import admin.pv.projects.mediasoft.com.abacus_admin.fragment.ParametreFragment;
import admin.pv.projects.mediasoft.com.abacus_admin.fragment.PartenaireFragment;
import admin.pv.projects.mediasoft.com.abacus_admin.fragment.PointventeFragment;
import admin.pv.projects.mediasoft.com.abacus_admin.fragment.ProduitFragment;
import admin.pv.projects.mediasoft.com.abacus_admin.fragment.ProfilFragment;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Partenaire;

public class AccueilActivity extends AppCompatActivity  implements  AccueilFragment.OnFragmentInteractionListener ,  EtatFragment.OnFragmentInteractionListener ,   OperationFragment.OnFragmentInteractionListener, ProduitFragment.OnFragmentInteractionListener, CategorieFragment.OnFragmentInteractionListener, CommercialFragment.OnFragmentInteractionListener, PartenaireFragment.OnFragmentInteractionListener, ProfilFragment.OnFragmentInteractionListener,ParametreFragment.OnFragmentInteractionListener,PointventeFragment.OnFragmentInteractionListener{

    public static final int PROGRESS_DIALOG_ID = 0;
    public static final String CODEBAR = "codebare";
    private ProgressDialog mProgressBar;
    private int MAX_SIZE;

    private Toolbar mToolbar;
    FrameLayout frameLayout1 = null ;
    FrameLayout frameLayout2 = null ;

    AccueilFragment acceuilFragment = null ;
    PointventeFragment pointventeFragment = null ;
    OperationFragment operationFragment = null ;
    ProduitFragment produitFragment = null ;
    CommercialFragment commercialFragment = null ;
    CategorieFragment categorieFragment = null ;
    PartenaireFragment partenaireFragment = null ;
    ProfilFragment profilFragment = null ;
    ParametreFragment parametreFragment = null ;
    EtatFragment etatFragment = null ;
    private int start = 0;

    private CommercialDAO commercialDAO;
    private ProduitDAO produitDAO;
    private OperationDAO operationDAO;
    private CaisseDAO caisseDAO;
    private BilletDAO billetDAO;
    private ModePayementDAO modepayementDAO;
    private TypeOperationDAO typeOperationDAO;
    private CategorieProduitDAO categorieproduitDAO;
    private PointVenteDAO pointventeDAO;
    private PartenaireDAO partenaireDAO;
    private Uri uriactuel = null;
    private MenuItem dateinterval;
    private MenuItem menu_add;
    private MenuItem menu_filtre;



    private static final String AUTHORITY_OPERATION = "admin.pv.projects.mediasoft.com.abacus_admin.Operation";
    private static final int MINUTE = 60;
    public static String ACCOUNT_TYPE = "";
    Account mAccount;
    // The account name
    public static final String ACCOUNT = "abacus account";
    // Sync interval constants
    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 2L;
    public static final long SYNC_INTERVAL = SYNC_INTERVAL_IN_MINUTES * SECONDS_PER_MINUTE;
    private ContentResolver mResolver;
    private ClientDAO clientDAO;
    private Produit_pointventeDAO produit_pointventeDAO;
    private MouvementDAO mouvementDAO;
    private MenuItem menu_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);

        initialisation() ;

        setupToolBar();
        setupFragments();


        if (getResources().getInteger(R.integer.panel_number)!=1){
            showFragment(pointventeFragment,1);
        }


    }

    private void initialisation() {

        produitDAO = new ProduitDAO(AccueilActivity.this);
        commercialDAO = new CommercialDAO(AccueilActivity.this);
        partenaireDAO = new PartenaireDAO(AccueilActivity.this);
        pointventeDAO = new PointVenteDAO(AccueilActivity.this);
        caisseDAO = new CaisseDAO(AccueilActivity.this);
        operationDAO = new OperationDAO(AccueilActivity.this);
        categorieproduitDAO = new CategorieProduitDAO(AccueilActivity.this);
        typeOperationDAO = new TypeOperationDAO(AccueilActivity.this);
        modepayementDAO = new ModePayementDAO(AccueilActivity.this);
        billetDAO = new BilletDAO(AccueilActivity.this);
        clientDAO = new ClientDAO(AccueilActivity.this);
        mouvementDAO = new MouvementDAO(AccueilActivity.this);
        produit_pointventeDAO = new Produit_pointventeDAO(AccueilActivity.this);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuaccueil, menu);
        dateinterval = menu.findItem(R.id.action_interval) ;
        menu_save = menu.findItem(R.id.menu_save) ;
        menu_add = menu.findItem(R.id.nouveau) ;
        menu_filtre = menu.findItem(R.id.menu_filtre) ;
        //dateinterval.setVisible(false) ;
        return true;
    }

    private void setupToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.mipmap.ic_back);
        ab.setDisplayHomeAsUpEnabled(true);
    }



    private void setupFragments() {
        final FragmentManager fm = getSupportFragmentManager();
        acceuilFragment = AccueilFragment.newInstance(null,null) ;

        final FragmentTransaction ft = fm.beginTransaction();
        //ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        ft.replace(R.id.frame1, acceuilFragment);
        ft.commit();

        pointventeFragment = PointventeFragment.newInstance(null,null) ;
        operationFragment = OperationFragment.newInstance(null,"0") ;
        produitFragment = ProduitFragment.newInstance("1",null) ;
        commercialFragment = CommercialFragment.newInstance(null,null) ;
        categorieFragment = CategorieFragment.newInstance(null,null) ;
        partenaireFragment = PartenaireFragment.newInstance(null,null) ;
        profilFragment = ProfilFragment.newInstance(null,null) ;
        parametreFragment = ParametreFragment.newInstance(null,null) ;
        etatFragment = EtatFragment.newInstance(null,null) ;
        //showFragment(profilFragment);
    }




    private void showFragment(final Fragment fragment, int sens) {
        if (fragment == null)         return;
        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();

        if (sens == 0)ft.setCustomAnimations(R.anim.left_to_right_in, R.anim.left_to_right_out);
        else ft.setCustomAnimations(R.anim.right_to_left_in, R.anim.right_to_left_out);

        if (getResources().getInteger(R.integer.panel_number)==1){
            if (fragment instanceof AccueilFragment)start = 0 ;
            else start = 1 ;

            ft.replace(R.id.frame1, fragment);
            if (menu_add != null){
                if (start==1)menu_add.setVisible(false) ;
                else menu_add.setVisible(true) ;
            }
        }
        else {
            ft.replace(R.id.frame2, fragment);
        }

        ft.commit();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId() ;
        if (id == android.R.id.home){
            quitter() ;
        }
        else if (id == R.id.nouveau){
            nouveau() ;
        }

        else if (id == R.id.menu_filtre){
            operationFragment.filtre() ;
        }

        else if (id == R.id.menu_save){
            operationFragment.save() ;
        }
        else if (id == R.id.action_interval){
            try {
                operationFragment.showInterval() ;
            }catch (Exception e){

            }
            try {
                etatFragment.showInterval() ;
            }catch (Exception e){

            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void nouveau() {
        //final CharSequence[] items = {getString(R.string.pointvente),getString(R.string.caisse),getString(R.string.produit), getString(R.string.commercial), getString(R.string.partenaire)};
        final CharSequence[] items = {getString(R.string.pointvente),getString(R.string.caisse),getString(R.string.produit)};

        final OperationDAO operationDAO = new OperationDAO(this) ;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.nouveau));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(getString(R.string.pointvente))) {
                    Intent intent =new Intent(AccueilActivity.this,PointventeFormActivity.class);
                    startActivity(intent);
                } else if (items[item].equals(getString(R.string.caisse))) {
                    Intent intent =new Intent(AccueilActivity.this,CaisseFormActivity.class);
                    startActivity(intent);
                } else if (items[item].equals(getString(R.string.produit))) {
                    Intent intent =new Intent(AccueilActivity.this,ProduitFormActivity.class);
                    startActivity(intent);
                }else if (items[item].equals(getString(R.string.commercial))) {

                } else if (items[item].equals(getString(R.string.partenaire))) {

                }
                else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent() ;

        if (intent!=null && intent.getIntExtra("POS",0) ==1) {
            onFragmentInteraction(Uri.parse(AccueilFragment.OPERATION_URI));
        }


        lanceSyncAdapter(this);

    }



    private void lanceSyncAdapter(Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context) ;
        clientDAO = new ClientDAO(AccueilActivity.this);
        if (clientDAO.getLast()==null) return;
        ACCOUNT_TYPE = context.getString(R.string.accounttype) ;
        // Get the content resolver for your app
        mResolver = context.getContentResolver();
        /*
         * Turn on periodic syncing
         */

        mAccount = CreateSyncAccount(context);
        // Time en second



        // Pass the settings flags by inserting them in a bundle
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(
                ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        ContentResolver.setSyncAutomatically(mAccount,AUTHORITY_OPERATION, true);
        ContentResolver.addPeriodicSync(mAccount,AUTHORITY_OPERATION, Bundle.EMPTY, Long.valueOf(preferences.getString("interval", "5")) * SECONDS_PER_MINUTE);

        /*
         * Request the sync for the default account, authority, and
         * manual sync settings
         */
        ContentResolver.requestSync(mAccount, AUTHORITY_OPERATION, settingsBundle);
    }



    /**
     * Create a new dummy account for the sync adapter
     *
     * @param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager = (AccountManager) context.getSystemService(context.ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }

        return newAccount ;
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


    @Override
    public void onFragmentInteraction(Uri uri) {
        uriactuel = uri ;
        if (dateinterval!=null)dateinterval.setVisible(false) ;
        if (menu_save!=null)menu_save.setVisible(false) ;
        if (menu_filtre!=null)menu_filtre.setVisible(false) ;

        if (uri.equals(Uri.parse(AccueilFragment.PV_URI))) showFragment(pointventeFragment,1);
        else if (uri.equals(Uri.parse(AccueilFragment.OPERATION_URI))) {
            setTitle(getString(R.string.op_rations));
            showFragment(operationFragment,1);
            if (dateinterval!=null)  dateinterval.setVisible(true) ;
            if (menu_save!=null)  menu_save.setVisible(true) ;
            if (menu_filtre!=null)  menu_filtre.setVisible(true) ;
        }
        else if (uri.equals(Uri.parse(AccueilFragment.PRODUIT_URI))) {
            setTitle(R.string.produits);
            showFragment(produitFragment,1);
        }
        else if (uri.equals(Uri.parse(AccueilFragment.COMMERCIAU_URI))) {
            setTitle(R.string.commercials);
            showFragment(commercialFragment,1);
        }
        else if (uri.equals(Uri.parse(AccueilFragment.PREDEF_URI))) {
            setTitle(R.string.categorie);
            showFragment(categorieFragment,1);
        }
        else if (uri.equals(Uri.parse(AccueilFragment.PARTENAIRE_URI))) showFragment(partenaireFragment,1);
        else if (uri.equals(Uri.parse(AccueilFragment.PROFIL_URI))) showFragment(profilFragment,1);
        else if (uri.equals(Uri.parse(AccueilFragment.PARAMETRE_URI))) showFragment(parametreFragment,1);
        else if (uri.equals(Uri.parse(AccueilFragment.ETAT_URI))){
            if (dateinterval!=null)  dateinterval.setVisible(true) ;
            if (menu_save!=null)  menu_save.setVisible(true) ;
            if (menu_filtre!=null)  menu_filtre.setVisible(false) ;
            showFragment(etatFragment,1);
        }
        else if (uri.equals(Uri.parse(AccueilFragment.EXIT_URI))) deconnecter();
    }

    private void deconnecter() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this) ;
        builder.setTitle(getString(R.string.deconnexion)) ;
        builder.setMessage(getString(R.string.deconnecter_votre_compte_de_ce_t_l_phone)) ;
        builder.setPositiveButton(getString(R.string.deconnexion), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                caisseDAO.clean() ;
                clientDAO.clean() ;
                produitDAO.clean() ;
                operationDAO.clean() ;
                partenaireDAO.clean() ;
                commercialDAO.clean() ;
                categorieproduitDAO.clean() ;
                typeOperationDAO.clean() ;
                modepayementDAO.clean() ;
                mouvementDAO.clean() ;
                produit_pointventeDAO.clean() ;
                billetDAO.clean() ;
                pointventeDAO.clean() ;
                finish();
            }
        }) ;
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }) ;
        final AlertDialog alertdialog = builder.create();
        alertdialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertdialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));
                alertdialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
            }
        }) ;
        alertdialog.show();

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            quitter();
            return true ;
        }
        return super.onKeyDown(keyCode, event);
    }




    private void quitter() {
        if (menu_save!=null)menu_save.setVisible(false) ;
        if (dateinterval!=null)dateinterval.setVisible(false) ;
        if (menu_filtre!=null)menu_filtre.setVisible(false) ;
        if (start==0) {
            //Toast.makeText(AccueilActivity.this, "Fermer", Toast.LENGTH_SHORT).show();


            final AlertDialog.Builder builder = new AlertDialog.Builder(this) ;
            builder.setTitle(getString(R.string.app_name)) ;
            builder.setMessage(getString(R.string.exit_confirm)) ;
            builder.setPositiveButton(getString(R.string.quiter), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            }) ;
            builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }) ;
            final AlertDialog alertdialog = builder.create();
            alertdialog.setOnShowListener( new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    alertdialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));
                    alertdialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
                }
            }) ;
            alertdialog.show();
        }
        else {
            start = 0 ;
            setTitle(getString(R.string.accueil));
            showFragment(acceuilFragment,0);
        }
    }

    @Override
    public void onFragmentInteraction(int uri) {

    }
}
