package admin.pv.projects.mediasoft.com.abacus_admin.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import admin.pv.projects.mediasoft.com.abacus_admin.R;
import admin.pv.projects.mediasoft.com.abacus_admin.activities.AccueilActivity;
import admin.pv.projects.mediasoft.com.abacus_admin.activities.PartenaireFormActivity;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.MouvementDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.OperationDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.PartenaireDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Partenaire;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Url;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Utiles;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PartenaireFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PartenaireFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PartenaireFragment extends Fragment {

    public static final String TAG = "PartenaireFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private LayoutInflater mInflater;
    ListePartenaireAdapter adapter = null ;
    ArrayList<Partenaire> partenaires = null;
    private PartenaireDAO partenaireDAO;
    ArrayList<Integer> selectedItems = null ;
    //private PartenaireActivity mParent = null;

    Partenaire partenaire = null ;

    LayoutInflater inflater = null ;
    public static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.FRENCH) ;
    private AccueilActivity mParent;
    private ListView lv;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VenteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PartenaireFragment newInstance(String param1, String param2) {
        PartenaireFragment fragment = new PartenaireFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PartenaireFragment() {
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
        View v = inflater.inflate(R.layout.fragment_partenaire, container, false);

        lv = (ListView) v.findViewById(R.id.list);
        partenaires = new ArrayList<Partenaire>() ;
        partenaireDAO = new PartenaireDAO(getActivity()) ;
        partenaires = partenaireDAO.getAll() ;
        adapter = new ListePartenaireAdapter(partenaires) ;
        lv.setAdapter(adapter);

        mInflater = inflater ;


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updatePartenaire(position) ;
            }
        });

        getActivity().setTitle(R.string.partenaires);
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
        mParent = (AccueilActivity) activity ;
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

    public Partenaire getPartenaire() {
        return partenaire;
    }

    public ArrayList<Partenaire> getPartenaires() {
        return  partenaires ;
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

    private void updatePartenaire(final int position) {
        final CharSequence[] items = {getString(R.string.modif), getString(R.string.delete), getString(R.string.annuler)};

        final OperationDAO operationDAO = new OperationDAO(getActivity()) ;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.action));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getString(R.string.modif))) {
                    Partenaire p = adapter.getItem(position) ;
                    Intent intent = new Intent(getActivity(), PartenaireFormActivity.class) ;
                    intent.putExtra("ID",p.getId());
                    startActivity(intent);
                    refresh();

                } else if (items[item].equals(getString(R.string.delete))) {
                    if (operationDAO.getManyByPartenaire(adapter.getItem(position).getId_externe()).size()==0)  deleteItem(position);
                    else Toast.makeText(getActivity(), R.string.partenairedelete, Toast.LENGTH_SHORT).show();
                }
                else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }




    public void deleteItem(final int position){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()) ;
        builder.setTitle(getString(R.string.app_name)) ;
        builder.setMessage(getString(R.string.delconfirm)) ;
        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
                Partenaire partenaire = adapter.getItem(position);

                //DelPartenaireTask task = new DelPartenaireTask(partenaires) ;
                //task.execute() ;
                partenaireDAO.delete(partenaire.getId()) ;
                refresh();
                Toast.makeText(getActivity(), R.string.partdelet, Toast.LENGTH_SHORT).show();
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



    public class ListePartenaireAdapter extends BaseAdapter {

        ArrayList<Partenaire> partenaires = new ArrayList<Partenaire>() ;

        public ListePartenaireAdapter(ArrayList<Partenaire> pv){
            partenaires = pv ;
        }

        @Override
        public int getCount() {
            return partenaires.size() ;
        }

        @Override
        public Partenaire getItem(int position) {
            return partenaires.get(position) ;
        }

        @Override
        public long getItemId(int position) {
            return partenaires.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if(convertView == null) {
                convertView  = mInflater.inflate(R.layout.partenaire_item, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            Partenaire partenaire = (Partenaire) getItem(position);


            if(partenaire != null) {
                holder.nomprenomTV.setText(partenaire.getRaisonsocial() + "" + partenaire.getNom() + " " + partenaire.getPrenom());
                holder.contactTV.setText(getString(R.string.cnt) + partenaire.getContact());
                holder.adresseTV.setText(getString(R.string.adr) + partenaire.getAdresse());

               /*
                if (partenaire.getImage()!=null) loadLocalImage(partenaire.getImage(), holder.imageView);
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
        TextView nomprenomTV ;
        TextView contactTV ;
        TextView adresseTV ;
        public ImageView imageView;

        public ViewHolder(View v) {
            nomprenomTV = (TextView)v.findViewById(R.id.nomprenom);
            contactTV = (TextView)v.findViewById(R.id.contact);
            adresseTV = (TextView)v.findViewById(R.id.adresse);
            imageView = (ImageView)v.findViewById(R.id.image);
        }
    }


    public void filtrer(ArrayList<Partenaire> partenaires, String query){
        Partenaire partenaire = null ;
        ArrayList<Partenaire> data = new ArrayList<Partenaire>() ;

        if(partenaires != null)
            for(int i = 0 ; i < partenaires.size() ; i++){
                partenaire = partenaires.get(i) ;
                if(partenaire.getNom().toLowerCase().contains(query)|| partenaire.getPrenom().toLowerCase().contains(query) || partenaire.getRaisonsocial().toLowerCase().contains(query) ) data.add(partenaire) ;
            }
        adapter = new ListePartenaireAdapter(data) ;
        lv.setAdapter(adapter) ;
        adapter.notifyDataSetChanged();
    }




    public class DelPartenaireTask extends AsyncTask<Void, Integer, Boolean> {
        ArrayList<Partenaire> mPartenaires = null ;

        public DelPartenaireTask(ArrayList<Partenaire> partenaires1){
            this.mPartenaires = partenaires1;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            for (int i = 0; i< mPartenaires.size(); i++) {
                partenaireDAO.delete(mPartenaires.get(i).getId()) ;
                mPartenaires.get(i) ;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                Toast.makeText(getActivity(), String.valueOf(mPartenaires.size()) + " " + getResources().getString(R.string.partenaire_suprimmer), Toast.LENGTH_LONG).show();
                refresh();
            }
            else
                Toast.makeText(getActivity(), getString(R.string.echec_del), Toast.LENGTH_LONG).show();
            getActivity().dismissDialog(AccueilActivity.PROGRESS_DIALOG_ID);
        }
    }

    private void refresh() {
        partenaires = partenaireDAO.getAll() ;
        adapter = new ListePartenaireAdapter(partenaires) ;
        lv.setAdapter(adapter);
    }



}
