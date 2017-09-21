package admin.pv.projects.mediasoft.com.abacus_admin.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import admin.pv.projects.mediasoft.com.abacus_admin.MainActivity;
import admin.pv.projects.mediasoft.com.abacus_admin.R;
import admin.pv.projects.mediasoft.com.abacus_admin.activities.AccueilActivity;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.BilletDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CaisseDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CategorieProduitDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.ClientDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CommercialDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CompteBanqueDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.DAOBase;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.ModePayementDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.MouvementDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.OperationDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.PartenaireDAO;

import admin.pv.projects.mediasoft.com.abacus_admin.dao.PointVenteDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.ProduitDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.TypeOperationDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Billet;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Caisse;
import admin.pv.projects.mediasoft.com.abacus_admin.model.CategorieProduit;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Client;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Commercial;
import admin.pv.projects.mediasoft.com.abacus_admin.model.CompteBanque;
import admin.pv.projects.mediasoft.com.abacus_admin.model.ModePayement;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Mouvement;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Operation;

import admin.pv.projects.mediasoft.com.abacus_admin.model.PointVente;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Produit;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.EtatsUtils;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.PrintPDA;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.PrinterUtils;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Url;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Utiles;
import okhttp3.FormBody;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OperationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OperationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OperationFragment extends Fragment {
    public static final String TAG = "OperationFragment";
    public static final String OPERATION_ID = "operation_id";
    public static final String HIDE = "hide";
    public static final String SHOW = "show";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView lv = null;
    ArrayList<Operation> operations = null;
    ListeOperationAdapter adapter = null;
    OperationDAO operationDAO = null;
    LinearLayout empty;
    LinearLayout full;
    LinearLayout progress;
    Button actualiser;
    private int type = 0;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    private LayoutInflater mInflater;
    private SharedPreferences preferences;
    private String bluetoothConfig = "imprimenteexterne";
    private AlertDialog.Builder detailBox;
    private AlertDialog dialogue;
    private AlertDialog dialogu;
    private String dateDebut = "2015-01-01";
    private String dateFin = DAOBase.formatter2.format(new Date());
    private SwipeRefreshLayout mSwipeRefreshLayout;

    TextView depot = null ;
    TextView retrait = null ;
    TextView total = null ;
    TextView nbre = null ;


    public OperationFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OperationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OperationFragment newInstance(String param1, String param2) {
        OperationFragment fragment = new OperationFragment();
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        refresh();
        //if (mParam2.equals("0")) showDateBox() ;
        //operations = new ArrayList<Operation>() ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_operation, container, false);
        operationDAO = new OperationDAO(getActivity());

        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(getActivity().getResources().getColor(R.color.my_primary));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mParam2.equals("0")) loadoperation() ;
                else mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        mInflater = inflater;


        depot = (TextView) v.findViewById(R.id.depot);
        retrait = (TextView) v.findViewById(R.id.retrait);
        total = (TextView) v.findViewById(R.id.total);
        nbre = (TextView) v.findViewById(R.id.nbre);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        empty = (LinearLayout) v.findViewById(R.id.vide);
        lv = (ListView) v.findViewById(R.id.liste);
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Operation operation = adapter.getItem(i);
                mListener.onFragmentInteraction(Uri.parse(String.valueOf(operation.getId_externe())));
                return true;
            }
        });


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Operation operation = adapter.getItem(i);
                showOpDetails(operation);
            }
        });


        dateDebut = preferences.getString("datedebut","2015-01-01") ;
        dateFin = preferences.getString("datefin",DAOBase.formatter2.format(new Date())) ;

        return v;
    }

    private void showOpDetails(final Operation operation) {
        detailBox = new AlertDialog.Builder(getActivity()) ;
        MouvementDAO mouvementDAO = new MouvementDAO(getActivity()) ;
        Operation parent = operationDAO.getOneExterne(operation.getOperation_id()) ;
        ArrayList<Mouvement> mouvements = null ;
        if (parent!=null) {
            mouvements = mouvementDAO.getMany(parent.getOperation_id()) ;
        }

        if ((mouvements!=null && mouvements.size()>0) || operation.getTypeOperation_id().equals(OperationDAO.VENTE) || operation.getTypeOperation_id().equals(OperationDAO.ACHAT) || operation.getTypeOperation_id().equals(OperationDAO.REGLEMENT) ){

            ScrollView sc = (ScrollView) getActivity().getLayoutInflater().inflate(R.layout.operationdetails,null);
            detailBox.setView(sc);

            TextView prixTotal = (TextView) sc.findViewById(R.id.prixTotalTV);
            TextView remise = (TextView) sc.findViewById(R.id.remiseTV);
            TextView recu = (TextView) sc.findViewById(R.id.recuTV);
            TextView mode = (TextView) sc.findViewById(R.id.mode);
            TextView client = (TextView) sc.findViewById(R.id.client);
            TextView banque = (TextView) sc.findViewById(R.id.banque);
            TextView rendu = (TextView) sc.findViewById(R.id.renduTV);
            TextView dateoperation = (TextView) sc.findViewById(R.id.dateVente);
            ImageButton imp = (ImageButton) sc.findViewById(R.id.imp);
            Button fermer = (Button) sc.findViewById(R.id.fermer);
            ListView v = (ListView) sc.findViewById(R.id.listeView);
            LinearLayout table = (LinearLayout) sc.findViewById(R.id.table);

            detailBox.setTitle(getString(R.string.notransact) + operation.getId()) ;
            if (operation.getTypeOperation_id().equals(OperationDAO.REGLEMENT))prixTotal.setText(getString(R.string.montantn) + Utiles.formatMtn(operation.getMontant()-operation.getRemise()));
            else prixTotal.setText(getString(R.string.netapayer) + Utiles.formatMtn(operation.getMontant()-operation.getRemise()));

            remise.setText(getString(R.string.remise) + Utiles.formatMtn(operation.getRemise()));
            recu.setText(getString(R.string.recu) + Utiles.formatMtn(operation.getRecu()));
            if (operation.getRecu()-operation.getMontant()+operation.getRemise() >0) rendu.setText(getString(R.string.rendu) + Utiles.formatMtn(operation.getRecu()-operation.getMontant()+operation.getRemise()));
            else rendu.setText(getString(R.string.rendu) + "0");

            dateoperation.setText(getString(R.string.dateoperation) + DAOBase.formatter.format(operation.getDateoperation()));
            client.setText("Client : " + operation.getClient());

            mode.setText(getString(R.string.mode) + operation.getModepayement());
            CompteBanque compteBanque = new CompteBanqueDAO(getActivity()).getOne(operation.getComptebanque_id()) ;
            if (compteBanque!=null)banque.setText(getString(R.string.bk) + compteBanque.getLibelle());

            if (operation.getTypeOperation_id().equals(OperationDAO.REGLEMENT)){
                parent = operationDAO.getOneExterne(operation.getOperation_id()) ;
                if (parent!=null) mouvements = mouvementDAO.getMany(parent.getId_externe());
                else mouvements = mouvementDAO.getMany(operation.getId_externe());
                if (mouvements.size()==0)  mouvements = mouvementDAO.getMany(operation.getId());
            }
            else {
                mouvements = mouvementDAO.getMany(operation.getId_externe());
                if (mouvements.size()==0)  mouvements = mouvementDAO.getMany(operation.getId());
            }

            if (mouvements.size()==0)  table.setVisibility(View.GONE);

            Log.e("MV SIZE", String.valueOf(mouvements.size())) ;
            if (!operation.getTypeOperation_id().startsWith(OperationDAO.PRODUIT) && !operation.getTypeOperation_id().startsWith(OperationDAO.CHARGE) && !operation.getTypeOperation_id().startsWith(OperationDAO.DEPENSE)) {

                VenteProduitAdapter vpa = new VenteProduitAdapter(mouvements) ;
                v.setAdapter(vpa);
            }
            else {
                v.setVisibility(View.GONE);
            }

            dialogue = detailBox.show();

            fermer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogue.dismiss();
                }
            });


            imp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (operation.getTypeOperation_id().equals(OperationDAO.VENTE) && operation.getAttente()==0)imprimeTicket(operation);
                    else if (operation.getTypeOperation_id().equals(OperationDAO.CMDCLNT))imprimeFacture(operation);
                    else if (operation.getTypeOperation_id().equals(OperationDAO.CMDFRNSS))imprimeFacture(operation);
                    else imprimeDoc(operation);
                }
            });


        }
        else {

            ScrollView sc = (ScrollView) getActivity().getLayoutInflater().inflate(R.layout.operationdetails1,null);
            detailBox.setView(sc);

            TextView description = (TextView) sc.findViewById(R.id.description);
            TextView montant = (TextView) sc.findViewById(R.id.montant);
            TextView mode = (TextView) sc.findViewById(R.id.mode);
            TextView client = (TextView) sc.findViewById(R.id.client);
            TextView banque = (TextView) sc.findViewById(R.id.banque);


            TextView dateoperation = (TextView) sc.findViewById(R.id.dateVente);
            ImageButton imp = (ImageButton) sc.findViewById(R.id.imp);
            Button fermer = (Button) sc.findViewById(R.id.fermer);

            description.setText(operation.getDescription());
            montant.setText(String.valueOf(operation.getMontant()));
            detailBox.setTitle(getString(R.string.notransact) + operation.getId()) ;

            mode.setText(getString(R.string.mode) + operation.getModepayement());
            CompteBanque compteBanque = new CompteBanqueDAO(getActivity()).getOne(operation.getComptebanque_id()) ;
            if (compteBanque!=null)banque.setText(getString(R.string.bk) + compteBanque.getLibelle());


            dateoperation.setText(getString(R.string.dateoperation) + DAOBase.formatter.format(operation.getDateoperation()));
            client.setText("Client : " + operation.getClient());

            fermer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogue.dismiss();
                }
            });

            dialogue = detailBox.show() ;

        }

    }

    private void imprimeFacture(final Operation operation) {

        final EditText edittext = new EditText(getActivity());
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setMessage(getString(R.string.docpdf));
        alert.setTitle(getString(R.string.ficname));

        alert.setView(edittext);
        edittext.setText(R.string.commande);

        alert.setPositiveButton(R.string.valider, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                String name = edittext.getText().toString();
                Utiles.createandDisplayOperationPdf(operation, name, getActivity(),false);
            }
        });

        alert.setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        if (operations.size()!=0)alert.show();
        else Toast.makeText(getActivity(), R.string.anyop, Toast.LENGTH_LONG).show();

    }

    private void imprimeDoc(final Operation operation) {
        final CharSequence[] items = { getString(R.string.pdf), getString(R.string.xls), getString(R.string.fermer) };

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        //builder.setTitle(getString(R.string.action));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                String libelle = " " ;
                if (operation.getTypeOperation_id().equals(OperationDAO.VENTE)) libelle = "" ;
                if (items[item].equals(getString(R.string.pdf))) {
                    imprimePDFDoc(libelle);
                } else if (items[item].equals(getString(R.string.xls))) {
                    imprimeExcelDoc(libelle);
                } else {
                    dialog.dismiss();
                }
            }
        });
        android.support.v7.app.AlertDialog alertDialog = builder.show();
    }

    private void operation_livre(final Operation operation) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity()) ;
        alertDialog.setTitle(R.string.app_name) ;
        if (operation.getPayer() == 0)alertDialog.setMessage(R.string.cmdpayer) ;
        else alertDialog.setMessage(R.string.livrecmd) ;
        alertDialog.setPositiveButton(R.string.livree, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogue.dismiss();
                /*
                if (operation.getTypeOperation_id().equals(OperationDAO.CMDCLNT)) operation.setTypeOperation_id(OperationDAO.VENTE);
                else if (operation.getTypeOperation_id().equals(OperationDAO.CMDFRNSS)) operation.setTypeOperation_id(OperationDAO.ACHAT);
                operation.setRecu(operation.getMontant());
                if (operationDAO.update(operation)>0) Toast.makeText(getActivity(), R.string.opupdate, Toast.LENGTH_SHORT).show();

                refresh();
                */
                Intent intent = new Intent(getActivity(), AccueilActivity.class) ;
                intent.putExtra("OPERATION",operation.getId()) ;
                //Toast.makeText(getActivity(),operation.getId() + "", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                getActivity().finish();
            }
        }) ;
        alertDialog.setNegativeButton(R.string.fermer, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }) ;
        final AlertDialog dialog = alertDialog.create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
            }
        }) ;
        dialog.show();
    }

    private void operation_annuler(final Operation operation) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity()) ;
        alertDialog.setTitle(R.string.app_name) ;
        alertDialog.setMessage(R.string.annulerop) ;
        alertDialog.setPositiveButton(R.string.annuler, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialogue!=null) dialogue.dismiss();
                operation.setAnnuler(1);
                if (operationDAO.update(operation)>0) Toast.makeText(getActivity(), R.string.opupdate, Toast.LENGTH_SHORT).show();
                refresh();
            }
        }) ;
        alertDialog.setNegativeButton(R.string.fermer, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }) ;
        final AlertDialog dialog = alertDialog.create();
        dialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));
                dialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
            }
        }) ;
        dialog.show();
    }


    private void imprimeTicket(final Operation operation) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity()) ;
        alertDialog.setTitle(R.string.app_name) ;
        alertDialog.setMessage(R.string.imprimermsg) ;
        alertDialog.setPositiveButton(R.string.imp, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                boolean imp = preferences.getBoolean(bluetoothConfig, false);
                if (imp) {
                    try {
                        PrinterUtils printerUtils = new PrinterUtils(getActivity());
                        printerUtils.printTicket(operation.getId_externe());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        PrintPDA printPDA = new PrintPDA(getActivity());
                        printPDA.printTicket(operation.getId_externe());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                Toast.makeText(getActivity(), R.string.imprimlancee, Toast.LENGTH_SHORT).show();
            }
        }) ;
        alertDialog.setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }) ;
        alertDialog.show() ;
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
        //mParent = (OperationActivity) getActivity();
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

    @Override
    public void onResume() {
        super.onResume();
    }

    public void interval(int i, String dateDebut, String dateFin) {
        if (getActivity() == null) return;
        if (operationDAO == null) operationDAO = new OperationDAO(getActivity());
        try {
            if (mParam2.equals("13")) operations = operationDAO.getOperationByPv(Integer.parseInt(mParam1), DAOBase.formatter2.parse(dateDebut), DAOBase.formatter2.parse(dateFin));
            else if (mParam2.equals("0"))  operations = operationDAO.getAll(0,  DAOBase.formatter2.parse(dateDebut), DAOBase.formatter2.parse(dateFin),0);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.e("SIZE", String.valueOf(operations.size()));
        if (operations != null) {
            adapter = new ListeOperationAdapter(operations);
            lv.setAdapter(adapter);
            //Toast.makeText(getActivity(), operations.size() + "", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        }
    }

    private void refresh() {
        if (operationDAO == null) operationDAO = new OperationDAO(getActivity());
        if (mParam2.equals("13")) operations = operationDAO.getOperationByPv(Integer.parseInt(mParam1),null,null);
        else if (mParam2.equals("0"))  operations = operationDAO.getAll();
        else  operations = operationDAO.getAll(0, null, new Date(),0);

        /*
        //Log.e("PARAM",mParam2) ;
        if (operations.size()==0 && mParam2.equals("0")) {
            LoadOperationDataTask loadOperationDataTask = new LoadOperationDataTask(dateFin,dateFin) ;
            loadOperationDataTask.execute(Url.getLoadOperationUrl(getActivity())) ;
        }
        */

        //Log.e("SIZE", String.valueOf(operations.size()));
        if (operations != null) {
            adapter = new ListeOperationAdapter(operations);
            lv.setAdapter(adapter);
            //Toast.makeText(getActivity(), operations.size() + "", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();

            double mtn1 = 0;;
            double mtn2 = 0;;
            for (int i = 0; i<operations.size();++i){
                if (operations.get(i).getAnnuler()==1 || operations.get(i).getPayer()==0) continue;
                if (operations.get(i).getEntree()==1) mtn1 += operations.get(i).getMontant()  - operations.get(i).getRemise();
                else mtn2 += operations.get(i).getMontant() - operations.get(i).getRemise() ;
            }
            depot.setText(Utiles.formatMtn(mtn1) + " F");
            retrait.setText(Utiles.formatMtn(mtn2) + " F");
            nbre.setText(String.valueOf(operations.size()));
        }

    }

    private void showDateBox() {
        android.support.v7.app.AlertDialog.Builder dateBox = new android.support.v7.app.AlertDialog.Builder(getActivity());
        ScrollView scrollView = (ScrollView) getActivity().getLayoutInflater().inflate(R.layout.dialogbox,null);
        if (((ViewGroup)scrollView.getParent())!=null)((ViewGroup)scrollView.getParent()).removeAllViews();
        dateBox.setView(scrollView);
        dateBox.setTitle(getString(R.string.datechoice));

        final DatePicker debut = (DatePicker) scrollView.findViewById(R.id.dateDebut);
        final DatePicker fin = (DatePicker) scrollView.findViewById(R.id.dateFin);
        final android.support.v7.app.AlertDialog alert = dateBox.show();
        Button button = (Button) scrollView.findViewById(R.id.valider);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dateDebut = String.valueOf(debut.getYear()) + "-" + String.valueOf(debut.getMonth() + 1) + "-" + String.valueOf(debut.getDayOfMonth());
                    dateFin = String.valueOf(fin.getYear()) + "-" + String.valueOf(fin.getMonth() + 1) + "-" + String.valueOf(fin.getDayOfMonth());

                    alert.dismiss();
                    GetOperationIntervalle getOperationIntervalle = new GetOperationIntervalle(dateDebut,dateFin) ;
                    getOperationIntervalle.execute() ;

                    //LoadOperationDataTask loadOperationDataTask = new LoadOperationDataTask(dateDebut,dateFin) ;
                    //loadOperationDataTask.execute(Url.getLoadOperationUrl(getActivity())) ;
            }
        });
    }

    public void imprimePDFDoc(final String titre) {
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
                if (mParam2.equals("3") || mParam2.equals("4")) Utiles.createandDisplayOperationPdf(operations, name, getActivity(),false,Integer.parseInt(mParam1));
                else Utiles.createandDisplayOperationPdf(operations, name, getActivity(),true,Integer.parseInt(mParam1));
            }
        });

        alert.setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        if (operations.size()!=0)alert.show();
        else Toast.makeText(getActivity(), R.string.anyop, Toast.LENGTH_LONG).show();
    }

    public void imprimeExcelDoc(final String titre) {
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
                if (mParam2.equals("3") || mParam2.equals("4"))Utiles.createandDisplayOperationExcel(operations, name, getActivity(),false);
                else Utiles.createandDisplayOperationExcel(operations, name, getActivity(),true);
            }
        });

        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        if (operations.size()!=0)alert.show();
        else Toast.makeText(getActivity(), R.string.anyop, Toast.LENGTH_LONG).show();
    }

    public void showInterval() {
        if (mParam2.equals("0")) showDateBox() ;
    }

    public void filtre() {
        final CharSequence[] items = {  getString(R.string.achat),  getString(R.string.creances), getString(R.string.depense),getString(R.string.emprunt),getString(R.string.vente),getString(R.string.rglmtc), getString(R.string.fermer) };
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.filtre));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                ArrayList<Operation> ops = null ;
                double mtn =  0 ;
                try {
                    if (items[item].equals(getString(R.string.vente))) {
                        ops = operationDAO.getAll(1, DAOBase.formatter2.parse(dateDebut), DAOBase.formatter2.parse(dateFin),0) ;
                        for (int i = 0; i<ops.size();++i){
                            if (ops.get(i).getAnnuler()==1) continue;
                            mtn += ops.get(i).getMontant() - ops.get(i).getRemise() ;
                        }
                        init() ;
                        depot.setText(Utiles.formatMtn(mtn) + " F");
                    } else if (items[item].equals(getString(R.string.achat))) {
                        ops = operationDAO.getAll(5, DAOBase.formatter2.parse(dateDebut), DAOBase.formatter2.parse(dateFin),0) ;
                        for (int i = 0; i<ops.size();++i){
                            if (ops.get(i).getAnnuler()==1) continue;
                            mtn += ops.get(i).getMontant() - ops.get(i).getRemise() ;
                        }
                        init() ;
                        retrait.setText(Utiles.formatMtn(mtn) + " F");
                    } else if (items[item].equals(getString(R.string.rglmtc))) {
                        ops = operationDAO.getAll(27, DAOBase.formatter2.parse(dateDebut), DAOBase.formatter2.parse(dateFin),0) ;
                        for (int i = 0; i<ops.size();++i){
                            if (ops.get(i).getAnnuler()==1) continue;
                            mtn += ops.get(i).getMontant() - ops.get(i).getRemise() ;
                        }
                        init() ;
                        retrait.setText(Utiles.formatMtn(mtn) + " F");
                    } else if (items[item].equals(getString(R.string.emprunt))) {
                        ops = operationDAO.getAll(24, DAOBase.formatter2.parse(dateDebut), DAOBase.formatter2.parse(dateFin),0) ;
                        for (int i = 0; i<ops.size();++i){
                            if (ops.get(i).getAnnuler()==1) continue;
                            mtn += ops.get(i).getMontant() - ops.get(i).getRemise() ;
                        }
                        init() ;
                        depot.setText(Utiles.formatMtn(mtn) + " F");
                    }  else if (items[item].equals(getString(R.string.creances))) {
                        ops = operationDAO.getAll(18, DAOBase.formatter2.parse(dateDebut), DAOBase.formatter2.parse(dateFin),0) ;
                        for (int i = 0; i<ops.size();++i){
                            if (ops.get(i).getAnnuler()==1) continue;
                            mtn += ops.get(i).getMontant() - ops.get(i).getRemise() ;
                        }
                        init() ;
                        depot.setText(Utiles.formatMtn(mtn) + " F");
                    } else if (items[item].equals(getString(R.string.depense))) {
                        ops = operationDAO.getAll(2, DAOBase.formatter2.parse(dateDebut), DAOBase.formatter2.parse(dateFin),0) ;
                        for (int i = 0; i<ops.size();++i){
                            if (ops.get(i).getAnnuler()==1) continue;
                            mtn += ops.get(i).getMontant() - ops.get(i).getRemise() ;
                        }
                        init() ;
                        retrait.setText(Utiles.formatMtn(mtn) + " F");
                    } else {
                        dialog.dismiss();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (ops != null) {
                    operations = ops ;
                    adapter = new ListeOperationAdapter(operations);
                    lv.setAdapter(adapter);
                    //Toast.makeText(getActivity(), operations.size() + "", Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    nbre.setText(String.valueOf(ops.size()));
                }
            }

            private void init() {
                retrait.setText("0 F");
                depot.setText("0 F");
                nbre.setText("0");
            }
        });

        android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void save() {
        imprimerEtat(2);
    }


    private void imprimerEtat(final int i) {
        final CharSequence[] items = { getString(R.string.pdf), getString(R.string.xls), getString(R.string.fermer) };
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
        //builder.setTitle(getString(R.string.action));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getString(R.string.pdf))) {
                    imprimePDFDoc(i,0);
                } else if (items[item].equals(getString(R.string.xls))) {
                    imprimeExcelDoc(i,0);
                } else {
                    dialog.dismiss();
                }
            }
        });

        android.support.v7.app.AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void imprimeExcelDoc(final int i, final long id) {
        final EditText edittext = new EditText(getActivity());
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity());

        alert.setMessage(getString(R.string.docexcel));
        alert.setTitle(getString(R.string.ficname));

        alert.setView(edittext);
        edittext.setText(R.string.listeopp);

        alert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                String name = edittext.getText().toString();
                Utiles.verifyStoragePermissions(getActivity());
                Utiles.createandDisplayOperationExcel(adapter.operations,name, getActivity(),false);
            }
        });

        alert.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });


        final android.support.v7.app.AlertDialog alertdialog = alert.create();
        alertdialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertdialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));
                alertdialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
            }
        }) ;
        alertdialog.show();
    }

    public void imprimePDFDoc(final int i, final long id) {
        final EditText edittext = new EditText(getActivity());
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity());

        alert.setMessage(getString(R.string.docpdf));
        alert.setTitle(getString(R.string.ficname));

        alert.setView(edittext);
        edittext.setText(R.string.listeopp);

        alert.setPositiveButton(R.string.valider, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                String name = edittext.getText().toString();
                Utiles.verifyStoragePermissions(getActivity());
                Utiles.createandDisplayOperationPdf(adapter.operations,name, getActivity(),false,0);
            }
        });

        alert.setNegativeButton(R.string.annuler, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        final android.support.v7.app.AlertDialog alertdialog = alert.create();
        alertdialog.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                alertdialog.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.my_accent));
                alertdialog.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.my_accent));
            }
        }) ;
        alertdialog.show();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    static class ViewHolder {
        TextView numcpt;
        TextView numpiece;
        TextView mte1;
        TextView t1;
        TextView t2;
        TextView mte2;
        TextView date;
        TextView type;
        TextView transaction;
        ImageView imageView = null;

        public ViewHolder(View v) {
            imageView = (ImageView) v.findViewById(R.id.imageview);
            date = (TextView) v.findViewById(R.id.date);
            t1 = (TextView) v.findViewById(R.id.t1);
            mte1 = (TextView) v.findViewById(R.id.mte1);
            t2 = (TextView) v.findViewById(R.id.t2);
            mte2 = (TextView) v.findViewById(R.id.mte2);
            numpiece = (TextView) v.findViewById(R.id.numpiece);
            numcpt = (TextView) v.findViewById(R.id.numcpt);
            type = (TextView) v.findViewById(R.id.type);
            transaction = (TextView) v.findViewById(R.id.transaction);
        }
    }

    public class ListeOperationAdapter extends BaseAdapter {

        ArrayList<Operation> operations = new ArrayList<Operation>();

        public ListeOperationAdapter(ArrayList<Operation> usg) {
            operations = usg;
            if (operations.size() <= 0) {
                empty.setVisibility(View.VISIBLE);
            } else {
                empty.setVisibility(View.GONE);
            }
        }

        @Override
        public int getCount() {
            if (operations == null) return 0;
            return operations.size();
        }

        public Operation getItem(int position) {
            return operations.get(position);
        }

        @Override
        public long getItemId(int position) {
            return operations.get(position).getId();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.operationitem, null);
                holder = new ViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Operation operation = (Operation) getItem(position);
            TypeOperationDAO typeOperationDAO = new TypeOperationDAO(getActivity()) ;
            PointVenteDAO pointVenteDAO = new PointVenteDAO(getActivity()) ;


            if (operation != null) {
                Log.e("CAISSE", String.valueOf(operation.getCaisse_id())) ;
                Caisse caisse = new CaisseDAO(getActivity()).getOne(operation.getCaisse_id()) ;
                PointVente pointVente = pointVenteDAO.getOne(caisse.getPointVente()) ;

                if (operation.getTypeOperation_id().startsWith(OperationDAO.DEPENSE)) {
                    holder.imageView.setImageResource(R.mipmap.ic_retrait);
                    holder.type.setText(operation.getDescription());
                    holder.mte2.setText(Utiles.formatMtn(operation.getMontant()));
                    holder.mte1.setText("0");
                    if (pointVente!=null) holder.type.setText(operation.getDescription() + " (" + pointVente.getLibelle() + ")");
                }

                else if (operation.getTypeOperation_id().startsWith(OperationDAO.PLACEMENT)) {
                    holder.imageView.setImageResource(R.mipmap.ic_retrait);
                    if (operation.getComptebanque_id()>0){
                        Log.e("COmpteBanque", String.valueOf(operation.getComptebanque_id())) ;
                        new CompteBanqueDAO(getActivity()).getAll() ;
                        CompteBanque compteBanque = new CompteBanqueDAO(getActivity()).getOneByIdExterne(operation.getComptebanque_id()) ;
                        if (compteBanque!=null) holder.type.setText(getString(R.string.placemnt) + compteBanque.getLibelle() + "(" + compteBanque.getCode() + ")");
                        else holder.type.setText(getString(R.string.placemnt));
                    }
                    else holder.type.setText(R.string.placemtcaisse);
                    holder.mte2.setText(Utiles.formatMtn(operation.getMontant()-operation.getRemise()));
                    holder.mte1.setText("0");
                }
                else if (operation.getTypeOperation_id().startsWith(OperationDAO.IMMOBILISATION)) {
                    holder.imageView.setImageResource(R.mipmap.ic_retrait);
                    holder.type.setText(operation.getDescription());
                    holder.mte2.setText(Utiles.formatMtn(operation.getMontant()));
                    holder.mte1.setText("0");
                }
                else if (operation.getTypeOperation_id().equals(OperationDAO.SORTIE_STOCK)) {
                    holder.imageView.setImageResource(R.mipmap.ic_retrait);
                    holder.type.setText(R.string.sortiestock);
                    holder.mte2.setText("0");
                    holder.mte1.setText("0");
                }
                else if (operation.getTypeOperation_id().equals(OperationDAO.VENTE)) {
                    holder.imageView.setImageResource(R.mipmap.ic_depot);
                    holder.type.setText(R.string.vente);
                    holder.mte1.setText(Utiles.formatMtn(operation.getMontant()-operation.getRemise()));
                    holder.mte2.setText("0");
                    if (pointVente!=null) holder.type.setText(getString(R.string.vente) + " (" + pointVente.getLibelle() + ")");
                }else if (operation.getTypeOperation_id().equals(OperationDAO.ACHAT)) {
                    holder.imageView.setImageResource(R.mipmap.ic_retrait);
                    holder.type.setText(R.string.achat);
                    holder.mte2.setText(Utiles.formatMtn(operation.getMontant()-operation.getRemise()));
                    holder.mte1.setText("0");
                    if (pointVente!=null) holder.type.setText(getString(R.string.achat)+ " (" + pointVente.getLibelle() + ")");
                }else if (operation.getTypeOperation_id().equals(OperationDAO.BQ)) {
                    if (operation.getEntree()==1){
                    holder.imageView.setImageResource(R.mipmap.ic_depot);
                    holder.mte1.setText(Utiles.formatMtn(operation.getMontant()));
                    holder.mte2.setText("0");
                }
                else{
                    holder.imageView.setImageResource(R.mipmap.ic_retrait);
                    holder.mte1.setText("0");
                    holder.mte2.setText(Utiles.formatMtn(operation.getMontant()));
                }
                    holder.type.setText(operation.getDescription());
                }else if (operation.getTypeOperation_id().equals(OperationDAO.CH_EXC)) {
                    holder.imageView.setImageResource(R.mipmap.ic_retrait);
                    holder.type.setText(operation.getDescription());
                    holder.mte2.setText(Utiles.formatMtn(operation.getMontant()));
                    holder.mte1.setText("0");
                    if (pointVente!=null) holder.type.setText(operation.getDescription() + " (" + pointVente.getLibelle() + ")");
                }else if (operation.getTypeOperation_id().equals(OperationDAO.CH_FN)) {
                    holder.imageView.setImageResource(R.mipmap.ic_retrait);
                    holder.type.setText(operation.getDescription());
                    holder.mte2.setText(Utiles.formatMtn(operation.getMontant()));
                    holder.mte1.setText("0");
                    if (pointVente!=null) holder.type.setText(operation.getDescription() + " (" + pointVente.getLibelle() + ")");
                }else if (operation.getTypeOperation_id().equals(OperationDAO.EMPRUNT)) {
                    holder.imageView.setImageResource(R.mipmap.ic_depot);
                    holder.type.setText(operation.getDescription());
                    holder.mte1.setText(Utiles.formatMtn(operation.getMontant()));
                    holder.mte2.setText("0");
                }
                else if (operation.getTypeOperation_id().equals(OperationDAO.REGLEMENT)) {
                    holder.imageView.setImageResource(R.mipmap.ic_depot);
                    holder.type.setText(R.string.rglmtc);
                    holder.mte1.setText(Utiles.formatMtn(operation.getMontant()-operation.getRemise()));
                    holder.mte2.setText("0");
                }else if (operation.getTypeOperation_id().equals(OperationDAO.PRODUIT_EXC)) {
                    holder.imageView.setImageResource(R.mipmap.ic_depot);
                    holder.type.setText(operation.getDescription());
                    holder.mte1.setText(Utiles.formatMtn(operation.getMontant()));
                    holder.mte2.setText("0");
                    if (pointVente!=null) holder.type.setText(operation.getDescription() + " (" + pointVente.getLibelle() + ")");
                }else if (operation.getTypeOperation_id().equals(OperationDAO.PRODUIT_FIN)) {
                    holder.imageView.setImageResource(R.mipmap.ic_depot);
                    holder.type.setText(operation.getDescription());
                    holder.mte1.setText(Utiles.formatMtn(operation.getMontant()));
                    holder.mte2.setText("0");
                    if (pointVente!=null) holder.type.setText(operation.getDescription() + " (" + pointVente.getLibelle() + ")");
                } else if (operation.getTypeOperation_id().equals(OperationDAO.CMDFRNSS)) {
                    holder.imageView.setImageResource(R.mipmap.ic_journal);
                    holder.type.setText(R.string.cmdfr);
                    holder.mte2.setText(Utiles.formatMtn(operation.getMontant()));
                    holder.mte1.setText("0");
                    if (pointVente!=null) holder.type.setText(getString(R.string.cmdfr) + " (" + pointVente.getLibelle() + ")");
                }else if (operation.getTypeOperation_id().equals(OperationDAO.CMDCLNT)) {
                    holder.imageView.setImageResource(R.mipmap.ic_journal);
                    holder.type.setText(R.string.cmdclt);
                    holder.mte1.setText(Utiles.formatMtn(operation.getMontant()));
                    holder.mte2.setText("0");
                    if (pointVente!=null) holder.type.setText(getString(R.string.cmdclt)+ " (" + pointVente.getLibelle() + ")");
                }
                else{
                    if (operation.getEntree()==1)  holder.imageView.setImageResource(R.mipmap.ic_depot);
                    else   holder.imageView.setImageResource(R.mipmap.ic_retrait);
                    holder.type.setText(operationDAO.getOpTitle(operation.getTypeOperation_id()));
                }


                if (operation.getAttente()==1)  {
                    holder.type.setText(operation.getClient());
                    holder.imageView.setImageResource(R.mipmap.ic_attente);
                }

                holder.transaction.setText(getResources().getString(R.string.numtrans) + operation.getToken()) ;

                /*
                if (operation.getPayer()==0 && operation.getTypeOperation_id().equals(OperationDAO.VENTE))  {
                    holder.type.setText(getActivity().getString(R.string.ventea) + operation.getClient());
                }
                */

                //Toast.makeText(getActivity(), operation.getAnnuler() + "", Toast.LENGTH_SHORT).show();
                if (operation.getAnnuler()==1)  holder.imageView.setImageResource(R.mipmap.ic_annuler);
                else if (operation.getPayer()==0) {
                    holder.mte1.setText(Utiles.formatMtn(operation.getMontant()-operation.getRemise()));
                    holder.mte2.setText("0");
                    if (operation.getTypeOperation_id().equals(OperationDAO.EMPRUNT))holder.imageView.setImageResource(R.mipmap.ic_dette);
                    else holder.imageView.setImageResource(R.mipmap.ic_acredit);
                    holder.t1.setText(R.string.mte1) ;
                    holder.t2.setText(R.string.payer) ;
                    /*
                    ArrayList<Operation> payements = operationDAO.getMany(operation.getId_externe());

                    double total =  0 ;
                    for (int i = 0; i < payements.size(); i++) {
                        total += payements.get(i).getMontant() ;
                    }
                    holder.mte1.setText(Utiles.formatMtn(operation.getMontant()-operation.getRemise()));
                    holder.mte2.setText(Utiles.formatMtn(total));
                    if (total==operation.getMontant()-operation.getRemise()){
                        operation.setPayer(1);
                        operation.setEtat(0);
                        operationDAO.update(operation) ;
                    }
                    */
                }
                holder.numpiece.setText(String.valueOf(operation.getId()));
                holder.numpiece.setVisibility(View.GONE);
                holder.date.setText(DAOBase.formatter1.format(operation.getDateoperation()));

                if (operation.getTypeOperation_id().equals(OperationDAO.BQ)) {
                    if (operation.getEntree() == 1)holder.numcpt.setText(R.string.bvc);
                    else holder.numcpt.setText(R.string.cvb);
                }
                else if (operation.getComptebanque_id()>0)  holder.numcpt.setText(R.string.bq);
                else holder.numcpt.setText(R.string.cs);

            }

            return convertView;
        }

        public void addData(ArrayList<Operation> usg) {
            if (usg == null) operations = new ArrayList<Operation>();
            operations = usg;

            if (this.operations.size() <= 0) empty.setVisibility(View.VISIBLE);
            else empty.setVisibility(View.GONE);
            notifyDataSetChanged();
        }


        public void update(ArrayList<Operation> operations) {
            if (operations.size() == 0) return;
            this.operations = operations;

            if (this.operations.size() <= 0) empty.setVisibility(View.VISIBLE);
            else empty.setVisibility(View.GONE);
            notifyDataSetChanged();
        }
    }





    public class VenteProduitAdapter extends BaseAdapter {

        ArrayList<Mouvement> mouvements = null ;

        public VenteProduitAdapter(ArrayList<Mouvement> pv){
            mouvements = pv ;
        }


        @Override
        public int getCount() {
            if (mouvements!=null)   return mouvements.size() ;
            else return 0;
        }

        @Override
        public Mouvement getItem(int position) {
            return mouvements.get(position) ;
        }

        @Override
        public long getItemId(int position) {
            return mouvements.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ProduitVenteViewHolder holder = null;

            if(convertView == null) {
                convertView  = mInflater.inflate(R.layout.mouvementitem, null);
                holder = new ProduitVenteViewHolder(convertView);

                convertView.setTag(holder);
            } else {
                holder = (ProduitVenteViewHolder)convertView.getTag();
            }
            Mouvement mouvement = (Mouvement) getItem(position);


            if(mouvement != null) {
                holder.produitTextView.setText(mouvement.getProduit());
                holder.quantiteTextView.setText(String.valueOf(mouvement.getQuantite()));
                if (mouvement.getEntree()==1){
                    holder.prixTextView.setText(Utiles.formatMtn(mouvement.getPrixV()));
                    holder.prixTotalTV.setText(Utiles.formatMtn(mouvement.getQuantite() * mouvement.getPrixV()));
                }
                else {
                    holder.prixTextView.setText(Utiles.formatMtn(mouvement.getPrixA()));
                    holder.prixTotalTV.setText(Utiles.formatMtn(mouvement.getQuantite() * mouvement.getPrixA()));
                }
            };
            return convertView;
        }

        public void addData(ArrayList<Mouvement> pvs) {
            mouvements = pvs ;
            notifyDataSetChanged();
        }

    }



    static class ProduitVenteViewHolder{
        TextView produitTextView ;
        TextView prixTextView ;
        TextView quantiteTextView ;
        TextView prixTotalTV ;

        public ProduitVenteViewHolder(View v) {
            produitTextView = (TextView)v.findViewById(R.id.produitTV);
            prixTextView = (TextView)v.findViewById(R.id.prixTv);
            quantiteTextView = (TextView)v.findViewById(R.id.quantiteTV);
            prixTotalTV = (TextView)v.findViewById(R.id.prixTotalTV);
        }
    }





    public class LoadOperationDataTask extends AsyncTask<String,Void,String> {
        Calendar calendar = Calendar.getInstance() ;
        private ProduitDAO produitDAO;
        String login = null ;
        String password = null ;
        private OperationDAO operationDAO;
        private MouvementDAO mouvementDAO;
        private ClientDAO clientDAO;
        private String res;
        private static final String MAUVAIS = "KO";
        private static final String BON = "OK";

        String dd = null ;
        String df = null ;

        public LoadOperationDataTask(){

        }

        public LoadOperationDataTask(String dateDebut, String dateFin) {
            dd = dateDebut ;
            df = dateFin ;
        }

        @Override
        protected String doInBackground(String... url) {
            FormBody.Builder formBuilder = new FormBody.Builder() ;
            formBuilder.add("client", String.valueOf(clientDAO.getLast().getId()));
            formBuilder.add("datedebut", dd);
            formBuilder.add("datefin", df);

            SharedPreferences.Editor editor = preferences.edit() ;
            editor.putString("datedebut",dd) ;
            editor.putString("datefin",df) ;
            editor.commit() ;

            Log.e("URL",url[0]) ;

            String result = " ";
            try {
                result = Utiles.POST(url[0], formBuilder.build());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.e("REPONSEEEEEEEEEEEEEEEE", result);

            int nbre = 0 ;
            int size = 0 ;
            try {

                JSONObject obj = new JSONObject(result);
                // list_annonces = null;
                if (obj != null) {
                    String reponse = obj.getString("reponse");

                    if (!reponse.equals("OK")) return reponse ;

                    operationDAO.clean() ;
                    mouvementDAO.clean() ;


                    JSONArray mouvementArr = obj.getJSONArray("mouvements");
                    size = mouvementArr.length() ;
                    JSONObject mouvementObj = null ;
                    Mouvement mouvement = null ;
                    for (int i = 0; i < size; i++) {
                        mouvementObj = mouvementArr.getJSONObject(i);
                        mouvement = new Mouvement();
                        mouvement.setId(mouvementObj.getLong("id"));
                        mouvement.setEntree(mouvementObj.getInt("entree"));
                        mouvement.setPrixA(mouvementObj.getDouble("prix_achat"));
                        mouvement.setPrixV(mouvementObj.getDouble("prix_vente"));
                        mouvement.setQuantite(mouvementObj.getInt("quantite"));
                        mouvement.setCmup(mouvementObj.getDouble("cmup"));
                        mouvement.setRestant(mouvementObj.getInt("restant"));
                        mouvement.setProduit_id(mouvementObj.getLong("produit_id"));
                        mouvement.setOperation_id(mouvementObj.getLong("operation_id"));
                        mouvement.setProduit(mouvementObj.getString("produit"));
                        try {
                            mouvement.setCreated_at(DAOBase.formatter.parse(mouvementObj.getString("created_at")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        mouvementDAO.add(mouvement) ;
                    }


                    JSONArray operationArr = obj.getJSONArray("operations");
                    size = operationArr.length() ;
                    JSONObject operationObj = null ;
                    Operation operation = null ;
                    for (int i = 0; i < size; i++) {
                        operationObj = operationArr.getJSONObject(i);
                        operation = new Operation();
                        operation.setId_externe(operationObj.getLong("id"));
                        operation.setId(operationObj.getLong("id"));
                        operation.setAnnuler(operationObj.getInt("annuler"));
                        operation.setAttente(0);
                        operation.setCaisse(operationObj.getLong("caisse_id"));
                        if (!operationObj.getString("operation_id").equals("null"))operation.setOperation_id(operationObj.getLong("operation_id"));
                        if (!operationObj.getString("compte_banque_id").equals("null"))operation.setComptebanque_id(operationObj.getLong("compte_banque_id"));
                        operation.setEntree(operationObj.getInt("entree"));
                        if (!operationObj.getString("commercial_id").equals("null"))operation.setCommercialid(operationObj.getLong("commercial_id"));
                        operation.setEntree(operationObj.getInt("entree"));
                        if (!operationObj.getString("partenaire_id").equals("null"))operation.setPartenaire_id(operationObj.getLong("partenaire_id"));
                        operation.setPayer(operationObj.getInt("payer"));
                        operation.setNbreproduit(operationObj.getInt("nbreproduit"));
                        operation.setTypeOperation_id(operationObj.getString("typeoperation"));
                        operation.setDescription(operationObj.getString("description"));
                        operation.setClient(operationObj.getString("client"));
                        operation.setMontant(operationObj.getDouble("montant"));
                        operation.setModepayement(operationObj.getString("mode_payement"));
                        operation.setRecu(operationObj.getDouble("recu"));
                        operation.setRemise(operationObj.getDouble("remise"));
                        try {
                            operation.setDateoperation(DAOBase.formatter.parse(operationObj.getString("created_at")));
                            operation.setCreated_at(DAOBase.formatter.parse(operationObj.getString("created_at")));
                            operation.setDateecheance(DAOBase.formatter2.parse(operationObj.getString("date_echeance")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        operationDAO.add(operation) ;
                    }

                    return BON ;

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return MAUVAIS ;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            getActivity().dismissDialog(AccueilActivity.PROGRESS_DIALOG_ID);
            if (result.equals(BON)) {
                mParam2="" ;
                refresh();
                if (operationDAO.getAll().size()==0) Toast.makeText(getActivity(), R.string.anyresult, Toast.LENGTH_SHORT).show();
            }
            else Toast.makeText(getActivity(), R.string.load_echec, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getActivity().showDialog(AccueilActivity.PROGRESS_DIALOG_ID);
            clientDAO = new ClientDAO(getActivity());
            mouvementDAO = new MouvementDAO(getActivity());
            operationDAO = new OperationDAO(getActivity());
            produitDAO = new ProduitDAO(getActivity());
        }
    }





    public class LoadOperationMoreTask extends AsyncTask<String,Void,String> {
        Calendar calendar = Calendar.getInstance() ;
        private ProduitDAO produitDAO;
        String login = null ;
        String password = null ;
        private OperationDAO operationDAO;
        private MouvementDAO mouvementDAO;
        private ClientDAO clientDAO;
        private String res;
        private static final String MAUVAIS = "KO";
        private static final String BON = "OK";

        String dd = null ;
        String df = null ;
        public LoadOperationMoreTask(){

        }

        public LoadOperationMoreTask(String dateDebut, String dateFin) {
            dd = dateDebut ;
            df = dateFin ;
        }

        @Override
        protected String doInBackground(String... url) {

            FormBody.Builder formBuilder = new FormBody.Builder() ;
            formBuilder.add("client", String.valueOf(clientDAO.getLast().getId()));
            if (operationDAO.getLast()!=null)formBuilder.add("operation", String.valueOf(operationDAO.getLast().getId()));
            else  formBuilder.add("operation", "0");

            Log.e("URL", Url.getLoadOperationsUrl(getActivity()));

            String result = " ";
            try {
                result = Utiles.POST(Url.getLoadOperationsUrl(getActivity()), formBuilder.build());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.e("REPONSEEEEEEEEEEEEEEEE", result);

            int nbre = 0 ;
            int size = 0 ;
            try {

                JSONObject obj = new JSONObject(result);
                // list_annonces = null;
                if (obj != null) {
                    String reponse = obj.getString("reponse");

                    if (!reponse.equals("OK")) return reponse ;

                    //operationDAO.clean() ;
                    //mouvementDAO.clean() ;

                    JSONArray mouvementArr = obj.getJSONArray("mouvements");
                    size = mouvementArr.length() ;
                    JSONObject mouvementObj = null ;
                    Mouvement mouvement = null ;
                    for (int i = 0; i < size; i++) {
                        mouvementObj = mouvementArr.getJSONObject(i);
                        mouvement = new Mouvement();
                        mouvement.setId(mouvementObj.getLong("id"));
                        mouvement.setEntree(mouvementObj.getInt("entree"));
                        mouvement.setPrixA(mouvementObj.getDouble("prix_achat"));
                        mouvement.setPrixV(mouvementObj.getDouble("prix_vente"));
                        mouvement.setQuantite(mouvementObj.getInt("quantite"));
                        if(!mouvementObj.getString("cmup").equals("null"))mouvement.setCmup(mouvementObj.getDouble("cmup"));
                        mouvement.setRestant(mouvementObj.getInt("restant"));
                        mouvement.setProduit_id(mouvementObj.getLong("produit_id"));
                        mouvement.setOperation_id(mouvementObj.getLong("operation_id"));
                        mouvement.setProduit(mouvementObj.getString("produit"));
                        try {
                            mouvement.setCreated_at(DAOBase.formatter.parse(mouvementObj.getString("created_at")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        mouvementDAO.add(mouvement) ;
                    }



                    JSONArray operationArr = obj.getJSONArray("operations");
                    size = operationArr.length() ;
                    JSONObject operationObj = null ;
                    Operation operation = null ;
                    for (int i = 0; i < size; i++) {
                        operationObj = operationArr.getJSONObject(i);
                        operation = new Operation();
                        operation.setId_externe(operationObj.getLong("id"));
                        operation.setId(operationObj.getLong("id"));
                        operation.setAnnuler(operationObj.getInt("annuler"));
                        operation.setAttente(0);
                        if (!operationObj.getString("operation_id").equals("null"))operation.setOperation_id(operationObj.getLong("operation_id"));
                        if (!operationObj.getString("compte_banque_id").equals("null"))operation.setComptebanque_id(operationObj.getLong("compte_banque_id"));
                        operation.setCaisse(operationObj.getLong("caisse_id"));
                        if (!operationObj.getString("commercial_id").equals("null"))operation.setCommercialid(operationObj.getLong("commercial_id"));
                        operation.setEntree(operationObj.getInt("entree"));
                        if (!operationObj.getString("partenaire_id").equals("null"))operation.setPartenaire_id(operationObj.getLong("partenaire_id"));
                        operation.setPayer(operationObj.getInt("payer"));
                        operation.setNbreproduit(operationObj.getInt("nbreproduit"));
                        operation.setTypeOperation_id(operationObj.getString("typeoperation"));
                        operation.setDescription(operationObj.getString("description"));
                        operation.setClient(operationObj.getString("client"));
                        operation.setMontant(operationObj.getDouble("montant"));
                        operation.setRecu(operationObj.getDouble("recu"));
                        operation.setRemise(operationObj.getDouble("remise"));
                        try {
                            operation.setDateoperation(DAOBase.formatter.parse(operationObj.getString("created_at")));
                            operation.setCreated_at(DAOBase.formatter.parse(operationObj.getString("created_at")));
                            operation.setDateecheance(DAOBase.formatter2.parse(operationObj.getString("date_echeance")));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        operationDAO.add(operation) ;
                    }

                    if (size>0)return BON ;

                }

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return MAUVAIS ;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            //getActivity().dismissDialog(AccueilActivity.PROGRESS_DIALOG_ID);
            if (result.equals(BON)) {
                mParam2="" ;
                refresh();
                if (operationDAO.getAll().size()==0) Toast.makeText(getActivity(), R.string.anyresult, Toast.LENGTH_SHORT).show();
            }
            else if (getActivity()!=null)Toast.makeText(getActivity(), R.string.anyop, Toast.LENGTH_SHORT).show();
            mSwipeRefreshLayout.setRefreshing(false);
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //getActivity().showDialog(AccueilActivity.PROGRESS_DIALOG_ID);
            clientDAO = new ClientDAO(getActivity());
            mouvementDAO = new MouvementDAO(getActivity());
            operationDAO = new OperationDAO(getActivity());
            produitDAO = new ProduitDAO(getActivity());
        }
    }


    public class GetOperationIntervalle extends AsyncTask<Void, Integer, Boolean> {
        Date dateDebut = null ;
        Date dateFin = null ;
        boolean data = false ;
        Calendar calendar = Calendar.getInstance() ;
        private double mtn1 = 0;
        private double mtn2 = 0;

        public GetOperationIntervalle(){

        }

        public GetOperationIntervalle(boolean d){
            data = d ;
        }

        public  GetOperationIntervalle(String dateDebut, String dateFin){
            data = true ;
            setDateDebut(dateDebut);
            setDateFin(dateFin);
        }

        public void setDateDebut(String dd) {
            try {
                dateDebut = DAOBase.formatter2.parse(dd);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //Log.e("TEST", formatter.format(this.dateDebut)) ;
        }

        public void setDateFin(String df) {
            try {
                dateFin = DAOBase.formatter2.parse(df) ;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //Log.e("TEST", formatter.format(this.dateFin)) ;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            operations = operationDAO.getAll(0,dateDebut,dateFin,0) ;
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

            adapter = new ListeOperationAdapter(operations);
            lv.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            for (int i = 0; i<operations.size();++i){
                if (operations.get(i).getAnnuler()==1 || operations.get(i).getPayer()==0) continue;
                if (operations.get(i).getEntree()==1) mtn1 += operations.get(i).getMontant()  - operations.get(i).getRemise();
                else mtn2 += operations.get(i).getMontant() - operations.get(i).getRemise() ;
            }
            depot.setText(Utiles.formatMtn(mtn1) + " F");
            retrait.setText(Utiles.formatMtn(mtn2) + " F");
            nbre.setText(String.valueOf(operations.size()));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            operationDAO = new OperationDAO(getActivity()) ;
        }
    }




    private void loadoperation() {
        LoadOperationMoreTask loadOperationMoreTask = new LoadOperationMoreTask() ;
        loadOperationMoreTask.execute() ;
    }
}
