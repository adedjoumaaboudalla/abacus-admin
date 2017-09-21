package admin.pv.projects.mediasoft.com.abacus_admin.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import admin.pv.projects.mediasoft.com.abacus_admin.MainActivity;
import admin.pv.projects.mediasoft.com.abacus_admin.R;
import admin.pv.projects.mediasoft.com.abacus_admin.activities.PointVenteActivity;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CaisseDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.DAOBase;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.PointVenteDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.ProduitDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.Produit_pointventeDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.database.CaisseHelper;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Caisse;
import admin.pv.projects.mediasoft.com.abacus_admin.model.PointVente;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Produit;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Produit_pointvente;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Url;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Utiles;
import okhttp3.FormBody;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProduitAPointVenteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProduitAPointVenteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProduitAPointVenteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String TAG = "ProduitPreferenceFragment";
    public static final int NMBRE = 200;
    private static final int PROGRESS_DIALOG_ID = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Button suiv = null ;
    Button prec = null ;
    private LayoutInflater mInflater;
    GridView gv = null ;
    private ProduitDAO produitDAO;
    private Produit_pointventeDAO produit_pointventeDAO;
    private CostumAdapter mAdapter;
    ArrayList<Long> produitSelected =  new ArrayList<Long>() ;
    private SharedPreferences preferences;
    private CheckBox chooseall;
    private ArrayList<Produit> produit;
    private PointVente pointVente;
    private ArrayList<Produit_pointvente> produit_pointventes;


    public ProduitAPointVenteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FinalStepFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProduitAPointVenteFragment newInstance(String param1, String param2) {
        ProduitAPointVenteFragment fragment = new ProduitAPointVenteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_produit_apoint_vente, container, false);

        suiv = (Button) v.findViewById(R.id.suiv);
        mInflater = inflater ;
        mInflater = inflater ;
        gv = (GridView) v.findViewById(R.id.gridView);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity()) ;

        produit_pointventeDAO = new Produit_pointventeDAO(getActivity()) ;

        Intent intent = getActivity().getIntent() ;
        if (intent.hasExtra("ID")){
            pointVente = new PointVenteDAO(getActivity()).getOne(intent.getLongExtra("ID",0)) ;
        }
        if (pointVente==null) getActivity().finish();

        produitDAO = new ProduitDAO(getActivity()) ;
        produit = produitDAO.getAll() ;
        chooseall = (CheckBox) v.findViewById(R.id.all);
        chooseall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked)  {
                    produitSelected = new ArrayList<Long>() ;
                    selectAll();
                }
                else if (bon()) deselectionner();

                /*
                if (isChecked){
                    int size = produit.size() ;
                    for (int i = 0; i<size; ++i) produitSelected.add(new Long(produit.get(i).getDiff())) ;
                }
                else{
                    produitSelected.clear();
                }

                */

                //Toast.makeText(getActivity(), "Size"+categorieSelected.size(), Toast.LENGTH_SHORT).show();
                if (mAdapter!=null)mAdapter.notifyDataSetChanged() ;
            }
        });

        produit_pointventes = produit_pointventeDAO.getMany(pointVente.getId()) ;
        for (int i = 0 ; i < produit_pointventes.size(); ++i) {
            produitSelected.add(new Long(produit_pointventes.get(i).getProduit_id())) ;
        }

        mAdapter = new CostumAdapter(produitDAO.getAll()) ;
        gv.setAdapter(mAdapter);

        gv.setNumColumns(getActivity().getResources().getInteger(R.integer.numcolums)) ;
        suiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (produitSelected.size()==0){
                    Snackbar.make(getActivity().findViewById(R.id.drLayout), getActivity().getString(R.string.aucunproduit), Snackbar.LENGTH_LONG).show();
                    return;
                }

                int nbr =  produitSelected.size() ;
                Log.e("NBRE A SAVOIR ", String.valueOf(nbr));
                String p = "" ;

                Produit finalProduit = null ;

                for (int i = 0; i < produitSelected.size(); i++) {
                    finalProduit = produitDAO.getOneByIdExterne(produitSelected.get(i).longValue()) ;
                    if (i==0){
                        p +=  finalProduit.getId_externe() ;
                    }
                    else{
                        p += ";" + finalProduit.getId_externe() ;
                    }
                }

                if (Utiles.isConnected(getActivity())){
                    SendProduitAPointVenteTask sendProduitAPointVenteTask = new SendProduitAPointVenteTask();
                    sendProduitAPointVenteTask.execute(Url.getSendProduitPointvente(getActivity())) ;
                }
                else refresh();
                Log.e("PRODUITS",p) ;

            }
        });


        getActivity().setTitle(getString(R.string.chooseproduit));
        return v ;
    }


    public boolean bon() {
        final ArrayList<Produit> produits = new ProduitDAO(getActivity()).getAll();
        if (produitSelected.size()==produits.size()) return true;
        return false;
    }


    public void deselectionner() {
        produitSelected.clear();
        mAdapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            //mParent = (InscriptionActivity) getActivity() ;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }




    public class CostumAdapter extends BaseAdapter {

        ArrayList<Produit> produits = null;

        public CostumAdapter(ArrayList<Produit> produits){
            this.produits = produits ;
            Produit produit = null ;
            //Toast.makeText(getActivity(),"SIZE" + String.valueOf(userDAO.getFirst().getProduit_id()),Toast.LENGTH_LONG).show();
            for (int i = 0 ; i < produits.size(); ++i) {
                produit = this.produits.get(i) ;
            }

            if (produitSelected.size() == produits.size()) chooseall.setChecked(true);
            else chooseall.setChecked(false);
        }


        @Override
        public int getCount() {
            if (produits!=null) return produits.size() ;
            return 0;
        }

        @Override
        public Produit getItem(int position) {
            if (produits!=null) return produits.get(position) ;
            return null;
        }

        @Override
        public long getItemId(int position) {
            if (produits!=null) return produits.get(position).getId_externe() ;
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            final Produit produit = (Produit) getItem(position);

            if(convertView == null) {
                convertView  = mInflater.inflate(R.layout.choixproduititem, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            final ViewHolder finalHolder = holder;
            holder.libelleTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) ((LinearLayout) finalHolder.libelleTV.getParent()).getChildAt(1);
                    if (cb.isChecked())cb.setChecked(false);
                    else cb.setChecked(true);

                    boolean isChecked = cb.isChecked() ;
                    if (isChecked)  if (!produitSelected.contains(new Long(produit.getId_externe()))) produitSelected.add(new Long(produit.getId_externe())) ;

                    if (!isChecked) if (produitSelected.contains(new Long(produit.getId_externe()))) produitSelected.remove(new Long(produit.getId_externe())) ;


                    if (produitSelected.size() == produits.size()) {
                        if (!chooseall.isChecked())chooseall.setChecked(true);
                    }
                    else if (produitSelected.size() != produits.size()) chooseall.setChecked(false);

                }
            });

            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)  if (!produitSelected.contains(new Long(produit.getId_externe()))) produitSelected.add(new Long(produit.getId_externe())) ;

                    if (!isChecked) if (produitSelected.contains(new Long(produit.getId_externe()))) produitSelected.remove(new Long(produit.getId_externe())) ;


                    if (produitSelected.size() == produits.size()) {
                        if (!chooseall.isChecked())chooseall.setChecked(true);
                    }
                    else if (produitSelected.size() != produits.size()) chooseall.setChecked(false);

                }
            });

            if(produit != null) {
                Log.d("PRODUIT " + produit.getId_externe(),produit.getLibelle()) ;
                holder.libelleTV.setText(produit.getLibelle());
                if (produitSelected.contains(new Long(produit.getId_externe())))holder.checkBox.setChecked(true);
                else if (!produitSelected.contains(new Long(produit.getId_externe())))holder.checkBox.setChecked(false);
            }

            return convertView;
        }
    }

    private void selectAll() {
        for (int i = 0 ; i < produit.size(); ++i) {
            produitSelected.add(new Long(produit.get(i).getId_externe())) ;
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView libelleTV ;
        CheckBox checkBox ;

        RelativeLayout container;

        ViewHolder(View v) {
            super(v);

            checkBox = (CheckBox)v.findViewById(R.id.checkbox);
            libelleTV = (TextView)v.findViewById(R.id.label);
        }
    }



    public void refresh() {
        Intent intent = new Intent(getActivity(), MainActivity.class) ;
        startActivity(intent);
        getActivity().finish();
    }




    /*
        Chargement des annonce en fonction de l'onglet sous lequel nous sommes
     */



    public class SendProduitAPointVenteTask extends AsyncTask<String, Integer, String> {
        Caisse caisse ;
        ArrayList<Produit_pointvente> ppv ;
        public SendProduitAPointVenteTask() {
        }

        @Override
        protected String doInBackground(String... url) {
            Date d = new Date() ;
            ppv = new ArrayList<>() ;

            FormBody.Builder formBuilder = new FormBody.Builder() ;
            formBuilder.add("pointvente_id", String.valueOf(pointVente.getId()));
            formBuilder.add("nb", String.valueOf(produitSelected.size()));

            for (int i = 0; i < produitSelected.size(); i++) {
                formBuilder.add("produit_id"+i, String.valueOf(produitSelected.get(i).longValue()));
                ppv.add(new Produit_pointvente(produitSelected.get(i).longValue(),pointVente.getId()));
            }

            String result = " ";
            try {
                result = Utiles.POST(url[0], formBuilder.build());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (result.split(":").length == 2 && result.contains("OK")) {
                result = "OK" ;
            }
            else result = "KO" ;

            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("KO")) Toast.makeText(getActivity(),getString(R.string.echec_ajout),Toast.LENGTH_LONG).show();
            else if (result.equals("OK")){
                if (produit_pointventeDAO.deleteAllByPv(pointVente.getId()) == produit_pointventes.size()){
                    for (int i = 0; i < ppv.size(); i++) {
                        produit_pointventeDAO.add(ppv.get(i)) ;
                    }
                    produit_pointventes = ppv ;
                    Toast.makeText(getActivity(),getString(R.string.success_ajout),Toast.LENGTH_LONG).show();
                    getActivity().finish();
                };

            }
            getActivity().dismissDialog(PROGRESS_DIALOG_ID);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getActivity().showDialog(PROGRESS_DIALOG_ID);
        }
    }


}
