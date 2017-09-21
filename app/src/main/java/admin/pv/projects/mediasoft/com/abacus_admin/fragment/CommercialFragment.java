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
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import admin.pv.projects.mediasoft.com.abacus_admin.R;
import admin.pv.projects.mediasoft.com.abacus_admin.activities.AccueilActivity;
import admin.pv.projects.mediasoft.com.abacus_admin.activities.CommercialFormActivity;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.OperationDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CommercialDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Commercial;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.EtatsUtils;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Utiles;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CommercialFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CommercialFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommercialFragment extends Fragment {

    public static final String TAG = "CommercialFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private LayoutInflater mInflater;
    ListeCommercialAdapter adapter = null ;
    ArrayList<Commercial> commercials = null;
    private CommercialDAO commercialDAO;
    ArrayList<Integer> selectedItems = null ;
    //private CommercialActivity mParent = null;

    Commercial commercial = null ;

    LayoutInflater inflater = null ;
    public static final SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.FRENCH) ;
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
    public static CommercialFragment newInstance(String param1, String param2) {
        CommercialFragment fragment = new CommercialFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CommercialFragment() {
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
        View v = inflater.inflate(R.layout.fragment_commercial, container, false);

        lv = (ListView) v.findViewById(R.id.list);
        commercials = new ArrayList<Commercial>() ;
        commercialDAO = new CommercialDAO(getActivity()) ;
        commercials = commercialDAO.getAll() ;
        adapter = new ListeCommercialAdapter(commercials) ;
        lv.setAdapter(adapter);

        mInflater = inflater ;


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateCommercial(position) ;
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
        refresh() ;
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

    public Commercial getCommercial() {
        return commercial;
    }

    public ArrayList<Commercial> getCommercials() {
        return  commercials ;
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

    private void updateCommercial(final int position) {
        final CharSequence[] items = {getString(R.string.modif), getString(R.string.delete), getString(R.string.annuler)};

        final OperationDAO operationDAO = new OperationDAO(getActivity()) ;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.action));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getString(R.string.modif))) {
                    Commercial p = adapter.getItem(position) ;
                    Intent intent = new Intent(getActivity(), CommercialFormActivity.class) ;
                    intent.putExtra("ID",p.getId());
                    startActivity(intent);
                    refresh();

                } else if (items[item].equals(getString(R.string.delete))) {
                    if (operationDAO.getManyByCommercial(adapter.getItem(position).getId_externe()).size()==0)  deleteItem(position);
                    else Toast.makeText(getActivity(), R.string.commercialdelete, Toast.LENGTH_SHORT).show();
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
                Commercial commercial = adapter.getItem(position);

                //DelCommercialTask task = new DelCommercialTask(commercials) ;
                //task.execute() ;
                commercialDAO.delete(commercial.getId()) ;
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



    public class ListeCommercialAdapter extends BaseAdapter {

        ArrayList<Commercial> commercials = new ArrayList<Commercial>() ;

        public ListeCommercialAdapter(ArrayList<Commercial> pv){
            commercials = pv ;
        }

        @Override
        public int getCount() {
            return commercials.size() ;
        }

        @Override
        public Commercial getItem(int position) {
            return commercials.get(position) ;
        }

        @Override
        public long getItemId(int position) {
            return commercials.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if(convertView == null) {
                convertView  = mInflater.inflate(R.layout.commercial_item, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            Commercial commercial = (Commercial) getItem(position);


            if(commercial != null) {
                holder.nomprenomTV.setText(commercial.getNom() + " " + commercial.getPrenom());
                holder.contactTV.setText(getString(R.string.cnt) + commercial.getContact());
                holder.adresseTV.setText(getString(R.string.adr) + commercial.getAdresse());
                holder.caTV.setText("CA : "+ Utiles.formatMtn(EtatsUtils.chiffreAffaireCommercial(getActivity(),commercial.getId())) + " F");

               /*
                if (commercial.getImage()!=null) loadLocalImage(commercial.getImage(), holder.imageView);
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
        TextView caTV;

        public ViewHolder(View v) {
            caTV = (TextView)v.findViewById(R.id.ca);
            nomprenomTV = (TextView)v.findViewById(R.id.nomprenom);
            contactTV = (TextView)v.findViewById(R.id.contact);
            adresseTV = (TextView)v.findViewById(R.id.adresse);
            imageView = (ImageView)v.findViewById(R.id.image);
        }
    }


    public void filtrer(ArrayList<Commercial> commercials, String query){
        Commercial commercial = null ;
        ArrayList<Commercial> data = new ArrayList<Commercial>() ;

        if(commercials != null)
            for(int i = 0 ; i < commercials.size() ; i++){
                commercial = commercials.get(i) ;
                if(commercial.getNom().toLowerCase().contains(query)|| commercial.getPrenom().toLowerCase().contains(query)) data.add(commercial) ;
            }
        adapter = new ListeCommercialAdapter(data) ;
        lv.setAdapter(adapter) ;
        adapter.notifyDataSetChanged();
    }




    public class DelCommercialTask extends AsyncTask<Void, Integer, Boolean> {
        ArrayList<Commercial> mCommercials = null ;

        public DelCommercialTask(ArrayList<Commercial> commercials1){
            this.mCommercials = commercials1;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            for (int i = 0; i< mCommercials.size(); i++) {
                commercialDAO.delete(mCommercials.get(i).getId()) ;
                mCommercials.get(i) ;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                Toast.makeText(getActivity(), String.valueOf(mCommercials.size()) + " " + getResources().getString(R.string.commercial_suprimmer), Toast.LENGTH_LONG).show();
                refresh();
            }
            else
                Toast.makeText(getActivity(), getString(R.string.echec_del), Toast.LENGTH_LONG).show();
            getActivity().dismissDialog(AccueilActivity.PROGRESS_DIALOG_ID);
        }
    }

    private void refresh() {
        if (mParam2!=null)commercials = commercialDAO.getCommercialByPv(Long.parseLong(mParam2)) ;
        else commercials = commercialDAO.getAll() ;
        Log.e("SIZE", String.valueOf(commercials.size())) ;
        adapter = new ListeCommercialAdapter(commercials) ;
        lv.setAdapter(adapter);
    }
}
