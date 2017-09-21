package admin.pv.projects.mediasoft.com.abacus_admin.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import admin.pv.projects.mediasoft.com.abacus_admin.R;
import admin.pv.projects.mediasoft.com.abacus_admin.activities.AccueilActivity;
import admin.pv.projects.mediasoft.com.abacus_admin.activities.CategorieProduitActivity;
import admin.pv.projects.mediasoft.com.abacus_admin.activities.ProduitPredefiniActivity;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CategorieProduitDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CategorieProduitDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.OperationDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.PointVenteDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.model.CategorieProduit;
import admin.pv.projects.mediasoft.com.abacus_admin.model.CategorieProduit;
import admin.pv.projects.mediasoft.com.abacus_admin.model.PointVente;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.EtatsUtils;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Utiles;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategorieFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategorieFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategorieFragment extends Fragment {
    public static final String TAG = "CategorieProduitFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private LayoutInflater mInflater;
    ListeCategorieProduitAdapter adapter = null ;
    ArrayList<CategorieProduit> categorieProduits = null;
    private CategorieProduitDAO categorieProduitDAO;
    ArrayList<Integer> selectedItems = null ;
    //private CategorieProduitFormActivity mParent = null;

    CategorieProduit categorieProduit = null ;

    LayoutInflater inflater = null ;
    public static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.FRENCH) ;
    private AccueilActivity mParent;
    private ListView lv;
    private LinearLayout vide;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VenteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategorieFragment newInstance(String param1, String param2) {
        CategorieFragment fragment = new CategorieFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CategorieFragment() {
        // Required empty public constructor
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_categorie, container, false);

        lv = (ListView) v.findViewById(R.id.list);
        categorieProduits = new ArrayList<CategorieProduit>() ;
        categorieProduitDAO = new CategorieProduitDAO(getActivity()) ;
        categorieProduits = categorieProduitDAO.getAll() ;
        adapter = new ListeCategorieProduitAdapter(categorieProduits) ;
        lv.setAdapter(adapter);

        vide = (LinearLayout) v.findViewById(R.id.vide);
        mInflater = inflater ;

        if (categorieProduits.size() != 0) vide.setVisibility(View.GONE);
        Log.e("DEBUG","ICI") ;

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categorieProduit = categorieProduits.get(position) ;
                Intent intent = new Intent(getActivity(), ProduitPredefiniActivity.class) ;
                intent.putExtra("categorie_id",categorieProduit.getId()) ;
                startActivity(intent);
            }
        });

        return  v ;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (adapter.getSelectedCount()==0){
                    adapter.toggleSelection(position);
                    mListener.onFragmentInteraction(adapter.getSelectedCount());
                }
                return true;
            }
        });*/

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(int uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mParent = null ;
        mListener = null;
    }

    public CategorieProduit getCategorieProduit() {
        return categorieProduit;
    }

    public ArrayList<CategorieProduit> getCategorieProduits() {
        return  categorieProduits ;
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
        public void onFragmentInteraction(int uri);
    }

    public class ListeCategorieProduitAdapter extends BaseAdapter {

        ArrayList<CategorieProduit> CategorieProduits = new ArrayList<CategorieProduit>() ;

        public ListeCategorieProduitAdapter(ArrayList<CategorieProduit> pv){
            CategorieProduits = pv ;
        }

        @Override
        public int getCount() {
            return CategorieProduits.size() ;
        }

        @Override
        public CategorieProduit getItem(int position) {
            return CategorieProduits.get(position) ;
        }

        @Override
        public long getItemId(int position) {
            return CategorieProduits.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if(convertView == null) {
                convertView  = mInflater.inflate(R.layout.categorieproduit_item, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            CategorieProduit categorieProduit = (CategorieProduit) getItem(position);


            if(categorieProduit != null) {
                holder.libelleTV.setText(categorieProduit.getLibelle());
            };

            return convertView;
        }

    }



    static class ViewHolder{
        TextView libelleTV ;

        public ViewHolder(View v) {
            libelleTV = (TextView)v.findViewById(R.id.libelleTV);
        }
    }


    public void filtrer(ArrayList<CategorieProduit> CategorieProduits, String query){
        CategorieProduit categorieProduit = null ;
        ArrayList<CategorieProduit> data = new ArrayList<CategorieProduit>() ;

        if(categorieProduit != null)
            for(int i = 0 ; i < CategorieProduits.size() ; i++){
                categorieProduit = CategorieProduits.get(i) ;
                if(categorieProduit.getLibelle().toLowerCase().contains(query)) data.add(categorieProduit) ;
            }
        adapter = new ListeCategorieProduitAdapter(data) ;
        lv.setAdapter(adapter) ;
        adapter.notifyDataSetChanged();
    }


}
