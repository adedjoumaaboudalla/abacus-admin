package admin.pv.projects.mediasoft.com.abacus_admin.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import admin.pv.projects.mediasoft.com.abacus_admin.R;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CaisseDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CommercialDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.DAOBase;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.OperationDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.PointVenteDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.fragment.CaisseFragment;
import admin.pv.projects.mediasoft.com.abacus_admin.fragment.CommercialFragment;
import admin.pv.projects.mediasoft.com.abacus_admin.fragment.DashbordFragment;
import admin.pv.projects.mediasoft.com.abacus_admin.fragment.DashbordFragment;
import admin.pv.projects.mediasoft.com.abacus_admin.fragment.ProduitFragment;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Caisse;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Commercial;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Operation;
import admin.pv.projects.mediasoft.com.abacus_admin.model.PointVente;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.EtatsUtils;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Utiles;

public class DashboardActivity extends AppCompatActivity implements  ViewPager.OnPageChangeListener ,  DashbordFragment.OnFragmentInteractionListener, CaisseFragment.OnFragmentInteractionListener, CommercialFragment.OnFragmentInteractionListener, ProduitFragment.OnFragmentInteractionListener {

    CommercialFragment commercialFragment = null;
    CaisseFragment caisseFragment = null;
    ArrayList<DashbordFragment> dashbordFragments = null;
    ProduitFragment produitFragment = null;
    public static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.FRENCH) ;


    private ArrayList<PointVente> pointVentes = null;

    private String bluetoothConfig = "imprimenteexterne";
    AlertDialog.Builder dateBox = null ;
    AlertDialog.Builder recapBox = null ;
    Button button = null ;
    private LayoutInflater mInflater= null ;
    private TabLayout tabLayout= null ;
    private ViewPager viewPager= null ;


    TextView depot = null ;
    TextView retrait = null ;
    TextView total = null ;
    TextView nbre = null ;
    TextView si1 = null ;
    TextView si2 = null ;
    final static int PROGRESS_DIALOG_ID = 0 ;

    private ProgressDialog mProgressBar;
    private int MAX_SIZE;
    private CollectionAdapter collectionAdapter;
    private int mPosition = 0 ;
    private AlertDialog alert;
    OperationDAO operationDAO = null ;
    private String dateFin = null;
    private String dateDebut = null ;
    private SharedPreferences preferences;
    private PointVenteDAO pointVenteDAO;
    private long id;
    private PointVente pointVente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        init();

        setupToolbar();
        setupTablayout();
    }

    private void init() {
        pointVenteDAO = new PointVenteDAO(this) ;
        pointVentes = pointVenteDAO.getAll() ;

        preferences = PreferenceManager.getDefaultSharedPreferences(DashboardActivity.this) ;
        mInflater = LayoutInflater.from(this);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        depot = (TextView) findViewById(R.id.depot);
        retrait = (TextView) findViewById(R.id.retrait);
        //total = (TextView) findViewById(R.id.total);
        nbre = (TextView) findViewById(R.id.nbre);
        operationDAO = new OperationDAO(this) ;

        ArrayList<Operation> operations = operationDAO.getAll() ;
        // si2.setText(Utiles.formatMtn(solde) + " F");

        //dateDebut = preferences.getString("datedebut","2015-01-01") ;
        //dateFin = preferences.getString("datefin",DAOBase.formatter2.format(new Date())) ;

        Calendar d1 = Calendar.getInstance() ;
        int year = d1.get(Calendar.YEAR);
        int month = d1.get(Calendar.MONTH) + 1;
        int daylast = d1.getActualMaximum(Calendar.DAY_OF_MONTH);
        int daysfirst = d1.getActualMinimum(Calendar.DAY_OF_MONTH);


        dateDebut = year + "-" + (month<10?("0"+month):(month)) + "-" + daysfirst ;
        dateFin = year + "-" + (month<10?("0"+month):(month)) + "-" + daylast ;

     }


    private void setupTablayout() {
        collectionAdapter = new CollectionAdapter(getSupportFragmentManager());
        viewPager.setAdapter(collectionAdapter);
        viewPager.addOnPageChangeListener(this);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_back);
        ab.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashbord, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_interval) {
            dateBox = new AlertDialog.Builder(this);
            ScrollView scrollView = (ScrollView) getLayoutInflater().inflate(R.layout.dialogbox,null);
            if (((ViewGroup)scrollView.getParent())!=null)((ViewGroup)scrollView.getParent()).removeAllViews();
            dateBox.setView(scrollView);
            dateBox.setTitle(getString(R.string.datechoice));

            final DatePicker debut = (DatePicker) scrollView.findViewById(R.id.dateDebut);
            final DatePicker fin = (DatePicker) scrollView.findViewById(R.id.dateFin);
            button = (Button) scrollView.findViewById(R.id.valider);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (dateBox != null) {

                        int year = debut.getYear();
                        int month = debut.getMonth() + 1;
                        int day = debut.getDayOfMonth() ;
                        dateDebut = year + "-" + (month<10?("0"+month):(month)) + "-" +  (day<10?("0"+day):(day)) ;

                        year = fin.getYear();
                        month = fin.getMonth() + 1;
                        day = fin.getDayOfMonth() ;
                        dateFin = year + "-" + (month<10?("0"+month):(month)) + "-" + (day<10?("0"+day):(day)) ;

                        dashbordFragments.get(mPosition).interval(dateDebut, dateFin);
                        dateBox = null;
                        alert.dismiss();
                        //refresh(mPosition,pointVente.getId());
                    }
                }
            });
            alert = dateBox.show();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        super.onCreateDialog(id);
        if (mProgressBar == null) {
            mProgressBar = new ProgressDialog(this);
            mProgressBar.setCancelable(false);
            mProgressBar.setTitle(getString(R.string.send_loding));
            mProgressBar.setMessage(getString(R.string.wait));
            mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressBar.setMax(MAX_SIZE);
        }

        return mProgressBar;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    /**
     * This method will be invoked when the current page is scrolled, either as part
     * of a programmatically initiated smooth scroll or a user initiated touch scroll.
     *
     * @param position             Position index of the first page currently being displayed.
     *                             Page position+1 will be visible if positionOffset is nonzero.
     * @param positionOffset       Value from [0, 1) indicating the offset from the page at position.
     * @param positionOffsetPixels Value in pixels indicating the offset from position.
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    @Override
    public void onPageSelected(int position) {
        mPosition = position ;
        dashbordFragments.get(mPosition).interval(dateDebut, dateFin);
        //refresh(mPosition,pointVente.getId());
        //if (mPosition==0)dashbordFragment.interval(mPosition,dateDebut, dateFin);
        //else if (mPosition==1) caisseFragment.interval(mPosition,dateDebut,dateFin);
        //else if (mPosition==2)commercialFragment.interval(mPosition,dateDebut,dateFin);
        //else if (mPosition==3)produitFragment.interval(mPosition,dateDebut,dateFin);
    }

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onFragmentInteraction(int uri) {

    }


    private class  CollectionAdapter extends FragmentPagerAdapter {
        int pages = 3 ;


        public CollectionAdapter(FragmentManager fm) {
            super(fm);
            pages = pointVentes.size() + 1 ;
            dashbordFragments = new ArrayList<DashbordFragment>(pages) ;
        }

        @Override
        public Fragment getItem(int position) {
            long id = 0 ;
            if (position==0) id=0 ;
            else id = pointVentes.get(position-1).getId() ;
            DashbordFragment dashbordFragment = DashbordFragment.newInstance(String.valueOf(position), String.valueOf(id));
            if (dashbordFragments.size()>position)dashbordFragments.set(position, dashbordFragment) ;
            else dashbordFragments.add(dashbordFragment) ;
            dashbordFragments.get(position).setDate(dateDebut,dateFin);
            return dashbordFragments.get(position) ;
        }

        @Override
        public int getCount() {
            return pages;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0 : {
                    return getString(R.string.generale) ;
                }
                default: return pointVentes.get(position-1).getLibelle() ;
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }
}
