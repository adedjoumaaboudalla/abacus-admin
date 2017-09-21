package admin.pv.projects.mediasoft.com.abacus_admin.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

import admin.pv.projects.mediasoft.com.abacus_admin.R;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.DAOBase;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.OperationDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.PointVenteDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Operation;
import admin.pv.projects.mediasoft.com.abacus_admin.model.PointVente;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.EtatsUtils;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Utiles;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EtatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EtatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EtatFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private SharedPreferences preferences = null;
    private Toolbar mToolbar;

    private ListView liste = null;
    private ParametresAdapter adapter = null;

    AlertDialog.Builder dateBox = null ;
    AlertDialog.Builder recapBox = null ;

    private String dateFin = null;
    private String dateDebut = null ;

    Button button = null ;
    private Dialog alert = null ;
    private LayoutInflater mInflater;
    private TextView textView;

    public EtatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EtatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EtatFragment newInstance(String param1, String param2) {
        EtatFragment fragment = new EtatFragment();
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
        View v = inflater.inflate(R.layout.fragment_etat, container, false);

        liste = (ListView) v.findViewById(R.id.liste);
        textView= (TextView) v.findViewById(R.id.date);
        ArrayList<Operation> op = new OperationDAO(getActivity()).getAll();
        if (op.size()==0) {
            Toast.makeText(getActivity(), R.string.noop, Toast.LENGTH_SHORT).show();
        }
        else {
            dateDebut = DAOBase.formatter2.format(op.get(op.size()-1).getCreated_at()) ;
            dateFin = DAOBase.formatter2.format(op.get(op.size()-1).getCreated_at()) ;
            textView.setText( DAOBase.formatterj.format(op.get(op.size()-1).getCreated_at())+ " - " + DAOBase.formatterj.format(op.get(0).getCreated_at()));
            initialisation();
        }

        mInflater = inflater ;

        getActivity().setTitle(R.string.etats );
        return v ;
    }



    private void initialisation() {
        adapter = new ParametresAdapter() ;
        liste.setAdapter(adapter);


        DatePicker debut = new DatePicker(getActivity()) ;
        DatePicker fin = new DatePicker(getActivity()) ;
        if (dateDebut==null) dateDebut= DAOBase.formatter2.format(new Date("2015/01/01"));
        if (dateFin==null) dateFin= DAOBase.formatter2.format(new Date());

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity()) ;

        liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imprimerEtat(position) ;
            }
        });
    }

    private void imprimerEtat(final int i) {
        final CharSequence[] items = { getString(R.string.pdf), getString(R.string.xls), getString(R.string.fermer) };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setTitle(getString(R.string.action));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getString(R.string.pdf))) {
                    choisirLepointventePDF(i) ;
                } else if (items[item].equals(getString(R.string.xls))) {
                    choisirLepointventeExcel(i) ;
                } else {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void choisirLepointventeExcel(final int i) {

        final PointVenteDAO pointVenteDAO = new PointVenteDAO(getActivity()) ;
        final ArrayList<PointVente> pvs = pointVenteDAO.getAll();
        int n = pvs.size()+2 ;
        final CharSequence[] items = new CharSequence[n];
        items[0] = getActivity().getResources().getString(R.string.tous) ;
        for (int k = 1; k <= n-2; k++) {
            items[k] = pvs.get(k-1).getLibelle() ;
        }
        items[n-1] = getActivity().getResources().getString(R.string.fermer) ;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.choixpv));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getString(R.string.fermer))) {
                    dialog.dismiss();
                }
                else if (items[item].equals(getString(R.string.tous))) {
                    imprimeExcelDoc(i,0);
                }
                else {
                    imprimeExcelDoc(i,pvs.get(item-1).getId());
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void choisirLepointventePDF(final int i) {

        final PointVenteDAO pointVenteDAO = new PointVenteDAO(getActivity()) ;
        final ArrayList<PointVente> pvs = pointVenteDAO.getAll();
        int n = pvs.size()+2 ;
        final CharSequence[] items = new CharSequence[n];
        items[0] = getActivity().getResources().getString(R.string.tous) ;
        for (int k = 1; k <= n-2; k++) {
            items[k] = pvs.get(k-1).getLibelle() ;
        }
        items[n-1] = getActivity().getResources().getString(R.string.fermer) ;

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.choixpv));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getString(R.string.fermer))) {
                    dialog.dismiss();
                }
                else if (items[item].equals(getString(R.string.tous))) {
                    imprimePDFDoc(i,0);
                }
                else {
                    imprimePDFDoc(i,pvs.get(item-1).getId());
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void imprimeExcelDoc(final int i, final long id) {
        final EditText edittext = new EditText(getActivity());
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setMessage(getString(R.string.docexcel));
        alert.setTitle(getString(R.string.ficname));

        alert.setView(edittext);
        edittext.setText(R.string.listeopp);

        alert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                String name = edittext.getText().toString();
                Utiles.verifyStoragePermissions(getActivity());
                EtatsUtils.createandDisplayExcelEtat(i,name,getActivity(), dateDebut,dateFin,id);
            }
        });

        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });


        final AlertDialog alertdialog = alert.create();
        alertdialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertdialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));
                alertdialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
            }
        }) ;
        alertdialog.show();
    }

    public void imprimePDFDoc(final int i, final long id) {
        final EditText edittext = new EditText(getActivity());
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setMessage(getString(R.string.docpdf));
        alert.setTitle(getString(R.string.ficname));

        alert.setView(edittext);
        edittext.setText(R.string.listeopp);

        alert.setPositiveButton(R.string.valider, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                String name = edittext.getText().toString();
                Utiles.verifyStoragePermissions(getActivity());
                EtatsUtils.createandDisplayPdfEtat(i,name,getActivity(),dateDebut,dateFin,id);
            }
        });

        alert.setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        final AlertDialog alertdialog = alert.create();
        alertdialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertdialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));
                alertdialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
            }
        }) ;
        alertdialog.show();
    }

    public void showInterval() {

        dateBox = new AlertDialog.Builder(getActivity());
        ScrollView scrollView = (ScrollView) getActivity().getLayoutInflater().inflate(R.layout.dialogbox,null);
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
                    dateBox = null;
                    textView.setText(String.valueOf(debut.getDayOfMonth()) + "-" + String.valueOf(debut.getMonth() + 1) + "-" + String.valueOf(debut.getYear()) + " - " + String.valueOf(fin.getDayOfMonth()) + "-" + String.valueOf(fin.getMonth() + 1) + "-" + String.valueOf(fin.getYear()));
                    alert.dismiss();
                }
            }
        });
        alert = dateBox.show();
    }


    public class ParametresAdapter extends BaseAdapter {

        int nbr = 10 ;

        public ParametresAdapter(){

        }

        @Override
        public int getCount() {
            return nbr ;
        }

        @Override
        public String getItem(int position) {
            return null ;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if(convertView == null) {
                convertView  = mInflater.inflate(R.layout.parametresitem, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder)convertView.getTag();
            }

            switch (position){
                case 0 : {
                    holder.libelle.setText(R.string.resultat);
                    //holder.icon.setImageResource(R.mipmap.ic_cat);
                } break;
                case 1 : {
                    holder.libelle.setText(R.string.chiffreaffaire);
                    //holder.icon.setImageResource(R.mipmap.ic_pays);
                } break;
                case 2 : {
                    holder.libelle.setText(R.string.liste_operations);
                    //holder.icon.setImageResource(R.mipmap.ic_pays);
                } break;
                case 3 : {
                    holder.libelle.setText(R.string.listeachat);
                    //holder.icon.setImageResource(R.mipmap.ic_pays);
                } break;
                case 4 : {
                    holder.libelle.setText(R.string.fichedestock);
                    //holder.icon.setImageResource(R.mipmap.ic_pays);
                } break;
                case 5 : {
                    holder.libelle.setText(R.string.fichedestockregroupe);
                    //holder.icon.setImageResource(R.mipmap.ic_pays);
                } break;
                case 6 : {
                    holder.libelle.setText(R.string.bilan);
                    //holder.icon.setImageResource(R.mipmap.ic_pays);
                } break;
                case 7 : {
                    holder.libelle.setText(R.string.balance);
                    //holder.icon.setImageResource(R.mipmap.ic_pays);
                } break;
                case 8 : {
                    holder.libelle.setText(R.string.listepartenaire);
                    //holder.icon.setImageResource(R.mipmap.ic_pays);
                } break;
                case 9 : {
                    holder.libelle.setText(R.string.tresorerie);
                    //holder.icon.setImageResource(R.mipmap.ic_pays);
                } break;
            }

            return convertView;
        }

    }

    static class ViewHolder{
        ImageView icon ;
        TextView libelle ;
        public ViewHolder(View v) {
            icon = (ImageView)v.findViewById(R.id.icon);
            libelle = (TextView)v.findViewById(R.id.libelleTV);
        }
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
}
