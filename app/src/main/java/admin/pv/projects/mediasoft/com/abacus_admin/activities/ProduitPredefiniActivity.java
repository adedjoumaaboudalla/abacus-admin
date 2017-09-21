package admin.pv.projects.mediasoft.com.abacus_admin.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;

import admin.pv.projects.mediasoft.com.abacus_admin.R;
import admin.pv.projects.mediasoft.com.abacus_admin.fragment.ProduitPredefiniFragment;

public class ProduitPredefiniActivity extends AppCompatActivity implements ProduitPredefiniFragment.OnFragmentInteractionListener {


    private Toolbar mToolbar;
    public final static int PROGRESS_DIALOG_ID = 0 ;
    private ProgressDialog mProgressBar;
    private int MAX_SIZE;
    private SharedPreferences preferences = null ;
    private ProduitPredefiniFragment produitPredefiniFragment = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produit_predefini);
        preferences = PreferenceManager.getDefaultSharedPreferences(this) ;
        initialisation() ;
        setupToolBar() ;
    }


    private void setupToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        // Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_back);
        ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initialisation() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        preferences = PreferenceManager.getDefaultSharedPreferences(this) ;
        final FragmentManager fm = getSupportFragmentManager();


        this.produitPredefiniFragment = (ProduitPredefiniFragment) fm.findFragmentByTag(ProduitPredefiniFragment.TAG);
        if (this.produitPredefiniFragment == null) {
            this.produitPredefiniFragment = new ProduitPredefiniFragment();
        }

        showFragment(this.produitPredefiniFragment);
    }

    private void showFragment(final Fragment fragment) {
        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
// We can also animate the changing of fragment
        //ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        ft.replace(R.id.frame, fragment);
        ft.commit();
    }




    @Override
    protected Dialog onCreateDialog(int id) {
        super.onCreateDialog(id);
        if (mProgressBar == null){
            mProgressBar = new ProgressDialog(this) ;
            mProgressBar.setCancelable(true);
            mProgressBar.setTitle(getString(R.string.app_name));
            mProgressBar.setMessage(getString(R.string.wait));
            mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressBar.setMax(MAX_SIZE);
        }
        return mProgressBar ;
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            return true ;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
