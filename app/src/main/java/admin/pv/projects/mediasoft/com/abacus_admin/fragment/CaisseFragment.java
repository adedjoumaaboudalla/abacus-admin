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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import admin.pv.projects.mediasoft.com.abacus_admin.R;
import admin.pv.projects.mediasoft.com.abacus_admin.activities.AccueilActivity;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.OperationDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CaisseDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.PointVenteDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Caisse;
import admin.pv.projects.mediasoft.com.abacus_admin.model.PointVente;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.EtatsUtils;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.PrinterUtils;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Utiles;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CaisseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CaisseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CaisseFragment extends Fragment {
    public static final String TAG = "CaisseFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private LayoutInflater mInflater;
    ListeCaisseAdapter adapter = null ;
    ArrayList<Caisse> caisses = null;
    private CaisseDAO caisseDAO;
    ArrayList<Integer> selectedItems = null ;
    //private CaisseFormActivity mParent = null;

    Caisse caisse = null ;

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
    public static CaisseFragment newInstance(String param1, String param2) {
        CaisseFragment fragment = new CaisseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CaisseFragment() {
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
        View v = inflater.inflate(R.layout.fragment_caisse, container, false);

        lv = (ListView) v.findViewById(R.id.list);
        caisses = new ArrayList<Caisse>() ;
        caisseDAO = new CaisseDAO(getActivity()) ;
        caisses = caisseDAO.getAll() ;
        adapter = new ListeCaisseAdapter(caisses) ;
        lv.setAdapter(adapter);

        mInflater = inflater ;

        Log.e("DEBUG","ICI") ;

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateCaisse(position) ;
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

        refresh();
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

    public Caisse getCaisse() {
        return caisse;
    }

    public ArrayList<Caisse> getCaisses() {
        return  caisses ;
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

    private void updateCaisse(final int position) {
        final CharSequence[] items = {getString(R.string.afficher),getString(R.string.modif), getString(R.string.delete), getString(R.string.annuler)};

        final OperationDAO operationDAO = new OperationDAO(getActivity()) ;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.action));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getString(R.string.modif))) {
                    /*
                    Caisse p = adapter.getItem(position) ;
                    Intent intent = new Intent(getActivity(), CaisseFormActivity.class) ;
                    intent.putExtra("ID",p.getId());
                    startActivity(intent);
                    */
                    refresh();

                } else if (items[item].equals(getString(R.string.delete))) {
                    deleteItem(position);
                } else if (items[item].equals(getString(R.string.afficher))) {
                    /*
                    Caisse p = adapter.getItem(position) ;
                    Intent intent = new Intent(getActivity(), CaisseFormActivity.class) ;
                    intent.putExtra("ID",p.getId());
                    startActivity(intent);
                    */
                }
                else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
        Log.e("DEBUG","CLICK1") ;
    }




    public void deleteItem(final int position){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()) ;
        builder.setTitle(getString(R.string.app_name)) ;
        builder.setMessage(getString(R.string.delconfirm)) ;
        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
                Caisse caisse = adapter.getItem(position);

                DelCaisseTask task = new DelCaisseTask(caisses) ;
                task.execute() ;
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



    public class ListeCaisseAdapter extends BaseAdapter {

        ArrayList<Caisse> caisses = new ArrayList<Caisse>() ;

        public ListeCaisseAdapter(ArrayList<Caisse> pv){
            caisses = pv ;
        }

        @Override
        public int getCount() {
            return caisses.size() ;
        }

        @Override
        public Caisse getItem(int position) {
            return caisses.get(position) ;
        }

        @Override
        public long getItemId(int position) {
            return caisses.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if(convertView == null) {
                convertView  = mInflater.inflate(R.layout.caisse_item, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            Caisse caisse = (Caisse) getItem(position);


            if(caisse != null) {
                PointVenteDAO pointVenteDAO = new PointVenteDAO(getActivity()) ;
                PointVente pointVente = pointVenteDAO.getOne(caisse.getPointVente()) ;
                holder.libelleTV.setText(caisse.getLogin());
                holder.caTV.setText("CA : " + Utiles.formatMtn(EtatsUtils.chiffreAffaireCaisse(getActivity(),caisse.getId())) + " F");
                holder.villeTV.setText(pointVente.getVille() + " " + pointVente.getQuartier());

               /*
                if (caisse.getImage()!=null) loadLocalImage(caisse.getImage(), holder.imageView);
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
        TextView caTV ;

        public ViewHolder(View v) {
            libelleTV = (TextView)v.findViewById(R.id.libelle);
            villeTV = (TextView)v.findViewById(R.id.ville);
            caTV = (TextView)v.findViewById(R.id.ca);
        }
    }


    public void filtrer(ArrayList<Caisse> caisses, String query){
        Caisse caisse = null ;
        ArrayList<Caisse> data = new ArrayList<Caisse>() ;

        if(caisses != null)
            for(int i = 0 ; i < caisses.size() ; i++){
                caisse = caisses.get(i) ;
                if(caisse.getCode().toLowerCase().contains(query)) data.add(caisse) ;
            }
        adapter = new ListeCaisseAdapter(data) ;
        lv.setAdapter(adapter) ;
        adapter.notifyDataSetChanged();
    }




    public class DelCaisseTask extends AsyncTask<Void, Integer, Boolean> {
        ArrayList<Caisse> mCaisses = null ;

        public DelCaisseTask(ArrayList<Caisse> caisses1){
            this.mCaisses = caisses1;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                Toast.makeText(getActivity(), String.valueOf(mCaisses.size()) + " " + getResources().getString(R.string.caisse_suprimmer), Toast.LENGTH_LONG).show();
                refresh();
            }
            else
                Toast.makeText(getActivity(), getString(R.string.echec_del), Toast.LENGTH_LONG).show();
            getActivity().dismissDialog(AccueilActivity.PROGRESS_DIALOG_ID);
        }
    }

    private void refresh() {
        caisses = caisseDAO.getAllById(Long.parseLong(mParam2)) ;
        adapter = new ListeCaisseAdapter(caisses) ;
        lv.setAdapter(adapter);
    }


}
