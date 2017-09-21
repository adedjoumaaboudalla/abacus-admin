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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import admin.pv.projects.mediasoft.com.abacus_admin.fragment.OperationFragment;
import admin.pv.projects.mediasoft.com.abacus_admin.fragment.PointventeFragment;
import admin.pv.projects.mediasoft.com.abacus_admin.fragment.ProduitFragment;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Caisse;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Commercial;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Operation;
import admin.pv.projects.mediasoft.com.abacus_admin.model.PointVente;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.EtatsUtils;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Utiles;

public class PointVenteActivity extends AppCompatActivity implements  ViewPager.OnPageChangeListener ,  OperationFragment.OnFragmentInteractionListener, CaisseFragment.OnFragmentInteractionListener, CommercialFragment.OnFragmentInteractionListener, ProduitFragment.OnFragmentInteractionListener {

    CommercialFragment commercialFragment = null;
    CaisseFragment caisseFragment = null;
    OperationFragment operationFragment = null;
    ProduitFragment produitFragment = null;
    public static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.FRENCH) ;



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
        setContentView(R.layout.activity_point_vente);

        init();

        Intent intent = getIntent() ;
        id = intent.getLongExtra("ID",0) ;
        pointVente = pointVenteDAO.getOne(id) ;

        setTitle(pointVente.getLibelle());
        setupToolbar();
        setupFragments();
        setupTablayout();

        refresh(mPosition,pointVente.getId()) ;
    }

    private void refresh(int position, long pv) {
        DatePicker debut = new DatePicker(this) ;
        DatePicker fin = new DatePicker(this) ;
        if (dateDebut==null) dateDebut= DAOBase.formatter2.format(new Date("2015/01/01"));
        if (dateFin==null) dateFin= DAOBase.formatter2.format(new Date());

        ArrayList<Operation> operations = new ArrayList<Operation>() ;
        double mtn = 0 ;
        int nbr = 0 ;

        retrait.setText("0 F");
        depot.setText("0 F");

        if (position==0 || position==1){
            CaisseDAO caisseDAO = new CaisseDAO(this) ;
            try {
                ArrayList<Caisse> caisses = caisseDAO.getAllById(pointVente.getId());;
                for (int i = 0; i<caisses.size();++i){
                    mtn += EtatsUtils.chiffreAffaireCaisse(this,caisses.get(i).getId());
                    nbr++ ;
                }
                depot.setText(Utiles.formatMtn(mtn) + " F");
                nbr = caisses.size() ;
                nbre.setText(String.valueOf(nbr));

                Log.e("OPERATION SIZE", String.valueOf(operations.size()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (position==0 || position==2){
            CommercialDAO commercialDAO = new CommercialDAO(this) ;
            try {
                ArrayList<Commercial> commercials = commercialDAO.getCommercialByPv(pointVente.getId());;
                for (int i = 0; i<commercials.size();++i){
                    mtn += EtatsUtils.chiffreAffaireCommercial(this,commercials.get(i).getId());
                    nbr++ ;
                }
                depot.setText(Utiles.formatMtn(mtn) + " F");
                nbr = commercials.size() ;
                nbre.setText(String.valueOf(nbr));

                Log.e("OPERATION SIZE", String.valueOf(operations.size()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (position==0){
            mtn = 0 ;
            try {
                operations = operationDAO.getOperationByPv(pointVente.getId(),DAOBase.formatter2.parse(dateDebut),DAOBase.formatter2.parse(dateFin));
            } catch (Exception e) {
                e.printStackTrace();
            }

            double mtn1 = 0;
            double mtn2 = 0;
            for (int i = 0; i<operations.size();++i){
                Log.e("MTN",Utiles.formatMtn(mtn1));
                if (operations.get(i).getAnnuler()==1){
                    Log.e("MTNA",Utiles.formatMtn(mtn1));
                    continue;
                }
                if (operations.get(i).getEntree()==1){
                    Log.e("MTNE",Utiles.formatMtn(mtn1));
                    mtn1 += operations.get(i).getMontant()  - operations.get(i).getRemise();;
                }
                else mtn2 += operations.get(i).getMontant()  - operations.get(i).getRemise();;
            }

            Log.e("MTN",Utiles.formatMtn(mtn1));
            Log.e("MTN", ""+ mtn1);
            Log.e("MTN", ""+ mtn2);
            depot.setText(Utiles.formatMtn(mtn1) + " F");
            retrait.setText(Utiles.formatMtn(mtn2) + " F");
            nbr = operations.size() ;
            nbre.setText(String.valueOf(nbr));
        }
    }


    private void init() {
        pointVenteDAO = new PointVenteDAO(this) ;

        preferences = PreferenceManager.getDefaultSharedPreferences(PointVenteActivity.this) ;
        mInflater = LayoutInflater.from(this);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        depot = (TextView) findViewById(R.id.depot);
        retrait = (TextView) findViewById(R.id.retrait);
        //total = (TextView) findViewById(R.id.total);
        nbre = (TextView) findViewById(R.id.nbre);
        operationDAO = new OperationDAO(this) ;

        ArrayList<Operation> operations = operationDAO.getAll() ;
        float solde = 0 ;
        for (int i = 0; i<operations.size();++i){
            if (operations.get(i).getTypeOperation_id().equals(OperationDAO.VENTE)) solde += operations.get(i).getMontant()  - operations.get(i).getRemise();
            else  solde -= operations.get(i).getMontant()  - operations.get(i).getRemise();;
        }

        // si2.setText(Utiles.formatMtn(solde) + " F");
    }


    private void setupTablayout() {
        collectionAdapter = new CollectionAdapter(getSupportFragmentManager());
        viewPager.setAdapter(collectionAdapter);
        viewPager.addOnPageChangeListener(this);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void setupFragments() {
        final FragmentManager fm = getSupportFragmentManager();
        //this.operationFragment = (OperationFragment) fm.findFragmentByTag(OperationFragment.TAG);
        if (this.operationFragment == null) {
            this.operationFragment = OperationFragment.newInstance(String.valueOf(pointVente.getId()),"13");
        }

        if (this.caisseFragment == null) {
            this.caisseFragment = CaisseFragment.newInstance(null,String.valueOf(pointVente.getId()));
        }

        if (this.commercialFragment == null) {
            this.commercialFragment = CommercialFragment.newInstance(null,String.valueOf(pointVente.getId()));
        }

        if (this.produitFragment == null) {
            this.produitFragment = ProduitFragment.newInstance("2",null);
        }
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
        getMenuInflater().inflate(R.menu.menu_pointvente, menu);
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
                        dateDebut = String.valueOf(debut.getYear()) + "-" + String.valueOf(debut.getMonth() + 1) + "-" + String.valueOf(debut.getDayOfMonth());
                        dateFin = String.valueOf(fin.getYear()) + "-" + String.valueOf(fin.getMonth() + 1) + "-" + String.valueOf(fin.getDayOfMonth());
                        if (mPosition==0)operationFragment.interval(mPosition,dateDebut, dateFin);
                        //else if (mPosition==1)caisseFragment.interval(mPosition,dateDebut,dateFin);
                        //else if (mPosition==2) commercialFragment.interval(mPosition,dateDebut,dateFin);
                        //else if (mPosition==3)produitFragment.interval(dateDebut,dateFin);
                        else operationFragment.interval(mPosition,dateDebut,dateFin);
                        dateBox = null;
                        alert.dismiss();
                        refresh(mPosition,pointVente.getId());
                    }
                }
            });
            alert = dateBox.show();
        } else if (id == R.id.action_recap) {

            /*
            recapBox = new AlertDialog.Builder(this);
            ScrollView scrollView = (ScrollView) getLayoutInflater().inflate(R.layout.recapbox,null);
            if (((ViewGroup)scrollView.getParent())!=null)((ViewGroup)scrollView.getParent()).removeAllViews();
            recapBox.setView(scrollView);

            final TextView nbr1 = (TextView) scrollView.findViewById(R.id.nbr1);
            final TextView nbr2 = (TextView) scrollView.findViewById(R.id.nbr2);
            final TextView nbr3 = (TextView) scrollView.findViewById(R.id.nbr3);
            final TextView nbr4 = (TextView) scrollView.findViewById(R.id.nbr4);
            final TextView nbr5 = (TextView) scrollView.findViewById(R.id.nbr5);
            final TextView nbr6 = (TextView) scrollView.findViewById(R.id.nbr6);

            final TextView mtn1 = (TextView) scrollView.findViewById(R.id.mtn1);
            final TextView mtn2 = (TextView) scrollView.findViewById(R.id.mtn2);
            final TextView mtn3 = (TextView) scrollView.findViewById(R.id.mtn3);
            final TextView mtn4 = (TextView) scrollView.findViewById(R.id.mtn4);
            final TextView mtn5 = (TextView) scrollView.findViewById(R.id.mtn5);
            final TextView mtn6 = (TextView) scrollView.findViewById(R.id.mtn6);

            final TextView interval = (TextView) scrollView.findViewById(R.id.interval);

            DatePicker debut = new DatePicker(this) ;
            DatePicker fin = new DatePicker(this) ;
            recapBox.setTitle(getString(R.string.recap) + " du ");

            if (dateDebut==null)dateDebut = String.valueOf(debut.getYear()) + "-" + String.valueOf(debut.getMonth() + 1) + "-" + String.valueOf(debut.getDayOfMonth());
            if (dateFin==null)dateFin = String.valueOf(fin.getYear()) + "-" + String.valueOf(fin.getMonth() + 1) + "-" + String.valueOf(fin.getDayOfMonth());

            interval.setText(dateDebut + " au " + dateFin);

            float mtn = 0 ;
            ArrayList<Operation> operations = null;
            try {
                operations = operationDAO.getAll(1, DAOBase.formatter2.parse(dateDebut), DAOBase.formatter2.parse(dateFin));

                for (int i = 0; i<operations.size();++i){
                    mtn += operations.get(i).getMontant() ;
                }

                nbr1.setText(String.valueOf(operations.size()));
                mtn1.setText(Utiles.formatMtn(mtn) + " F");
            } catch (ParseException e) {
                e.printStackTrace();
            }


            try {
                operations = operationDAO.getAll(2,DAOBase.formatter2.parse(dateDebut), DAOBase.formatter2.parse(dateFin));
                mtn = 0 ;
                for (int i = 0; i<operations.size();++i){
                    mtn += operations.get(i).getMontant() ;
                }

                nbr2.setText(String.valueOf(operations.size()));
                mtn2.setText(Utiles.formatMtn(mtn) + " F");

            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                operations = operationDAO.getAll(5,DAOBase.formatter2.parse(dateDebut), DAOBase.formatter2.parse(dateFin));
                mtn = 0 ;
                for (int i = 0; i<operations.size();++i){
                    mtn += operations.get(i).getMontant() ;
                }

                nbr3.setText(String.valueOf(operations.size()));
                mtn3.setText(Utiles.formatMtn(mtn) + " F");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            try {
                operations = operationDAO.getAll(3,DAOBase.formatter2.parse(dateDebut), DAOBase.formatter2.parse(dateFin));
                mtn = 0 ;
                for (int i = 0; i<operations.size();++i){
                    mtn += operations.get(i).getMontant() ;
                }

                nbr3.setText(String.valueOf(operations.size()));
                mtn3.setText(Utiles.formatMtn(mtn) + " F");
            } catch (ParseException e) {
                e.printStackTrace();
            }


            try {
                operations = operationDAO.getAll(4,DAOBase.formatter2.parse(dateDebut), DAOBase.formatter2.parse(dateFin));
                mtn = 0 ;
                for (int i = 0; i<operations.size();++i){
                    mtn += operations.get(i).getMontant() ;
                }

                nbr4.setText(String.valueOf(operations.size()));
                mtn4.setText(Utiles.formatMtn(mtn) + " F");
            } catch (ParseException e) {
                e.printStackTrace();
            }


            try {
                operations = operationDAO.getAll(3,DAOBase.formatter2.parse(dateDebut), DAOBase.formatter2.parse(dateFin));
                mtn = 0 ;
                for (int i = 0; i<operations.size();++i){
                    mtn += operations.get(i).getMontant() ;
                }

                nbr5.setText(String.valueOf(operations.size()));
                mtn5.setText(Utiles.formatMtn(mtn) + " F");
            } catch (ParseException e) {
                e.printStackTrace();
            }


            try {
                operations = operationDAO.getAll(0,DAOBase.formatter2.parse(dateDebut), DAOBase.formatter2.parse(dateFin));
                mtn = 0 ;
                for (int i = 0; i<operations.size();++i){
                    mtn += operations.get(i).getMontant() ;
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
            button = (Button) scrollView.findViewById(R.id.close);
            Button impBtn = (Button) scrollView.findViewById(R.id.imp);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alert.dismiss();
                }
            });

            impBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean imp = preferences.getBoolean(bluetoothConfig, false) ;
                    if (imp){
                        try {
                            PrinterUtils printerUtils = new PrinterUtils(OperationActivity.this) ;
                            //printerUtils.printTicket(dateDebut,dateFin);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        try {
                            PrintPDA printPDA = new PrintPDA(OperationActivity.this) ;
                            //printPDA.printTicket(dateDebut,dateFin);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });


            alert = recapBox.show();
            */
        }
        else if (id==R.id.action_aide){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this) ;
            builder.setTitle(getString(R.string.app_name)) ;
            builder.setView(R.layout.legende) ;

            builder.setNegativeButton(getString(R.string.close), new DialogInterface.OnClickListener() {
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
        refresh(mPosition,pointVente.getId());
        if (mPosition==0)operationFragment.interval(mPosition,dateDebut, dateFin);
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
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0 : {
                    return operationFragment ;
                }
                case 1 : {
                    return caisseFragment ;
                }
                case 2 : {
                    return commercialFragment ;
                }
                case 3 : {
                    return produitFragment ;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return pages;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0 : {
                    return getString(R.string.op_rations) ;
                }
                case 1 : {
                    return getString(R.string.caisses) ;
                }
                case 2 : {
                    return getString(R.string.commercials) ;
                }
                case 3 : {
                    return getString(R.string.produits) ;
                }
            }
            return super.getPageTitle(position);
        }
    }

}
