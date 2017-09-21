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
import android.support.v4.widget.PopupMenuCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import admin.pv.projects.mediasoft.com.abacus_admin.R;
import admin.pv.projects.mediasoft.com.abacus_admin.activities.AccueilActivity;
import admin.pv.projects.mediasoft.com.abacus_admin.activities.PointVenteActivity;
import admin.pv.projects.mediasoft.com.abacus_admin.activities.PointventeFormActivity;
import admin.pv.projects.mediasoft.com.abacus_admin.activities.ProduitPointventeActivity;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.OperationDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.PointVenteDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.PointVenteDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.database.PointVenteHelper;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Operation;
import admin.pv.projects.mediasoft.com.abacus_admin.model.PointVente;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.EtatsUtils;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Url;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Utiles;
import okhttp3.FormBody;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PointventeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PointventeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PointventeFragment extends Fragment {
    public static final String TAG = "PointVenteFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private LayoutInflater mInflater;
    ListePointVenteAdapter adapter = null ;
    ArrayList<PointVente> pointVentes = null;
    private PointVenteDAO pointVenteDAO;
    ArrayList<Integer> selectedItems = null ;
    //private PointVenteActivity mParent = null;

    PointVente pointVente = null ;

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
    public static PointventeFragment newInstance(String param1, String param2) {
        PointventeFragment fragment = new PointventeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PointventeFragment() {
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
        View v = inflater.inflate(R.layout.fragment_pointvente, container, false);

        lv = (ListView) v.findViewById(R.id.list);
        pointVentes = new ArrayList<PointVente>() ;
        pointVenteDAO = new PointVenteDAO(getActivity()) ;
        pointVentes = pointVenteDAO.getAll() ;
        adapter = new ListePointVenteAdapter(pointVentes) ;
        lv.setAdapter(adapter);



        mInflater = inflater ;


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.e("DEBUG","ICI") ;
            }
        });

        getActivity().setTitle(R.string.pointVentes);
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

    public PointVente getPointVente() {
        return pointVente;
    }

    public ArrayList<PointVente> getPointVentes() {
        return  pointVentes ;
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


    public void deleteItem(final int position){
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()) ;
        builder.setTitle(getString(R.string.app_name)) ;
        builder.setMessage(getString(R.string.delconfirm)) ;
        builder.setPositiveButton(getString(android.R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //finish();
                PointVente pointVente = adapter.getItem(position);

                DelPointVenteTask task = new DelPointVenteTask(pointVente) ;
                task.execute() ;
                //Toast.makeText(getActivity(), R.string.partdelet, Toast.LENGTH_SHORT).show();
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



    public class ListePointVenteAdapter extends BaseAdapter {

        ArrayList<PointVente> pointVentes = new ArrayList<PointVente>() ;

        public ListePointVenteAdapter(ArrayList<PointVente> pv){
            pointVentes = pv ;
        }

        @Override
        public int getCount() {
            return pointVentes.size() ;
        }

        @Override
        public PointVente getItem(int position) {
            return pointVentes.get(position) ;
        }

        @Override
        public long getItemId(int position) {
            return pointVentes.get(position).getId();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if(convertView == null) {
                convertView  = mInflater.inflate(R.layout.pointvente_item, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }
            final PointVente pointVente = (PointVente) getItem(position);


            if(pointVente != null) {
                holder.libelleTV.setText(pointVente.getLibelle());
                holder.chiffreAffaire.setText("CA : " + Utiles.formatMtn(EtatsUtils.chiffreAffaire(getActivity(),pointVente.getId())) + " F");
                holder.contactTV.setText(getString(R.string.cnt) + pointVente.getTel());
                holder.villeTV.setText(pointVente.getVille() + " " + pointVente.getQuartier());

               /*
                if (pointVente.getImage()!=null) loadLocalImage(pointVente.getImage(), holder.imageView);
                else holder.imageView.setImageBitmap(null);
                 */
                holder.popupmenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popupMenu = new PopupMenu(getActivity(),v) ;
                        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                int id = item.getItemId() ;
                                if (id == R.id.modif) {
                                    Intent intent = new Intent(getActivity(), PointventeFormActivity.class) ;
                                    intent.putExtra("ID",pointVente.getId());
                                    startActivity(intent);

                                    refresh();

                                }
                                else if (id == R.id.affecterproduit) {
                                    Intent intent = new Intent(getActivity(), ProduitPointventeActivity.class) ;
                                    intent.putExtra("ID",pointVente.getId());
                                    startActivity(intent);
                                    refresh();

                                } else
                                if (id == R.id.delete) {
                                    deleteItem(position);
                                } else
                                if (id == R.id.contacter) {
                                    String te = pointVente.getTel() ;
                                    if (te!=null && !te.equals("null")  && !te.equals("") ){
                                        Intent appel = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+te));
                                        startActivity(appel);
                                    }
                                    else
                                        Toast.makeText(getActivity(), R.string.anycontact, Toast.LENGTH_SHORT).show();
                                }
                                return true;
                            }
                        });
                        popupMenu.getMenuInflater().inflate(R.menu.menu_choix_pointvente,popupMenu.getMenu());
                        popupMenu.show();
                    }
                });

                holder.card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PointVente p = adapter.getItem(position) ;
                        Intent intent = new Intent(getActivity(), PointVenteActivity.class) ;
                        intent.putExtra("ID",p.getId());
                        startActivity(intent);
                    }
                });

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
        TextView contactTV ;
        TextView chiffreAffaire ;
        ImageButton popupmenu;
        CardView card;

        public ViewHolder(View v) {
            libelleTV = (TextView)v.findViewById(R.id.libelle);
            villeTV = (TextView)v.findViewById(R.id.ville);
            contactTV = (TextView)v.findViewById(R.id.contact);
            chiffreAffaire = (TextView)v.findViewById(R.id.ca);
            popupmenu = (ImageButton) v.findViewById(R.id.popupmenu);
            card = (CardView) v.findViewById(R.id.card);
        }
    }


    public void filtrer(ArrayList<PointVente> pointVentes, String query){
        PointVente pointVente = null ;
        ArrayList<PointVente> data = new ArrayList<PointVente>() ;

        if(pointVentes != null)
            for(int i = 0 ; i < pointVentes.size() ; i++){
                pointVente = pointVentes.get(i) ;
                if(pointVente.getLibelle().toLowerCase().contains(query)|| pointVente.getPays().toLowerCase().contains(query) || pointVente.getQuartier().toLowerCase().contains(query) ) data.add(pointVente) ;
            }
        adapter = new ListePointVenteAdapter(data) ;
        lv.setAdapter(adapter) ;
        adapter.notifyDataSetChanged();
    }




    public class DelPointVenteTask extends AsyncTask<Void, Integer, Boolean> {
        PointVente mPointVente = null ;

        public DelPointVenteTask(PointVente pointVente) {
            mPointVente = pointVente ;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            FormBody.Builder formBuilder = new FormBody.Builder() ;
            formBuilder.add(PointVenteHelper.TABLE_KEY, String.valueOf(mPointVente.getId()));

            String result = "";
            try {
                result = Utiles.POST(Url.getDeletePointVenteUrl(getActivity()), formBuilder.build());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("URL", Url.getDeletePointVenteUrl(getActivity()));

            Log.e("Reponse",result) ;

            if (result.split(":").length == 2 && result.contains("OK")) {
                // Si le pointVente est un pointVente qui ne se trouve pas sur le serveur
                int i = pointVenteDAO.delete(mPointVente.getId()) ;
                if (i<=0) return false ;
            }
            else return false ;

            return true ;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                Toast.makeText(getActivity(),getResources().getString(R.string.pointVente_suprimmer), Toast.LENGTH_LONG).show();
                refresh();
            }
            else
                Toast.makeText(getActivity(), getString(R.string.echec_del), Toast.LENGTH_LONG).show();
            getActivity().dismissDialog(AccueilActivity.PROGRESS_DIALOG_ID);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pointVenteDAO = new PointVenteDAO(getActivity());
            getActivity().showDialog(AccueilActivity.PROGRESS_DIALOG_ID);
        }
    }

    private void refresh() {
        pointVentes = pointVenteDAO.getAll() ;
        adapter = new ListePointVenteAdapter(pointVentes) ;
        lv.setAdapter(adapter);
    }


}
