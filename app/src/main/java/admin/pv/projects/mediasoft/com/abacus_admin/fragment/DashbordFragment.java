package admin.pv.projects.mediasoft.com.abacus_admin.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import admin.pv.projects.mediasoft.com.abacus_admin.R;
import admin.pv.projects.mediasoft.com.abacus_admin.activities.AccueilActivity;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.DAOBase;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.OperationDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Caisse;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Operation;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Utiles;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashbordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashbordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashbordFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView nbreop = null;
    TextView catotal = null;
    TextView capparjour = null;
    TextView deptotal = null;
    TextView depparjour = null;
    TextView dateinterval = null;
    TextView pf = null;
    TextView cf = null;
    TextView pe = null;
    TextView ce = null;
    TextView opannuler = null;
    TextView opcredit = null;

    private OnFragmentInteractionListener mListener;
    private OperationDAO operationDAO;
    private String datedebut;
    private String datefin;
    private LoadStatTask loadStatTask;

    public DashbordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashbordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashbordFragment newInstance(String param1, String param2) {
        DashbordFragment fragment = new DashbordFragment();
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
        View v = inflater.inflate(R.layout.fragment_dashbord, container, false);

        nbreop = (TextView) v.findViewById(R.id.nbreop);
        dateinterval = (TextView) v.findViewById(R.id.dateinterval);
        catotal = (TextView) v.findViewById(R.id.catotal);
        capparjour = (TextView) v.findViewById(R.id.caparjour);
        deptotal = (TextView) v.findViewById(R.id.deptotal);
        depparjour = (TextView) v.findViewById(R.id.depparjour);
        pf = (TextView) v.findViewById(R.id.pf);
        cf = (TextView) v.findViewById(R.id.cf);
        pe = (TextView) v.findViewById(R.id.pe);
        ce = (TextView) v.findViewById(R.id.ce);
        opannuler = (TextView) v.findViewById(R.id.opannuler);
        opcredit = (TextView) v.findViewById(R.id.opcredit);
        operationDAO = new OperationDAO(getActivity()) ;

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity()) ;
        //datedebut = preferences.getString("datedebut","2015-01-01") ;
        //datefin = preferences.getString("datefin",DAOBase.formatter2.format(new Date())) ;
        return v ;
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


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        interval(datedebut,datefin);
    }

    public void interval(String dateDebut, String dateFin) {
        datedebut = dateDebut ;
        this.datefin = dateFin ;
        loadStatTask = new LoadStatTask();
        loadStatTask.execute() ;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void setDate(String dateDebut, String dateFin) {
        datedebut = dateDebut ;
        datefin = dateFin ;
    }



    public void refresh() {
        Log.e("INTERVAL",datedebut + " - " + datefin) ;
        dateinterval.post(new Runnable() {
            @Override
            public void run() {
                dateinterval.setText(datedebut + " - " + datefin);
                //dateinterval.setText(String.valueOf(System.currentTimeMillis()));
            }
        });

        ArrayList<Operation> operations = null ;
        try {
            operations = operationDAO.getAll(1, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),Long.valueOf(mParam2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        double valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1) continue;
            valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
        }
        int nbre = 0 ;
        try {
            nbre = Utiles.daysBetween(DAOBase.formatter2.parse(datedebut).getTime(),DAOBase.formatter2.parse(datefin).getTime()) ;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        final ArrayList<Operation> finalOperations = operations;
        final int finalNbre = nbre;
        final double finalValeur = valeur;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                nbreop.post(new Runnable() {
                    @Override
                    public void run() {
                        nbreop.setText(finalOperations.size() + " opérations en " + finalNbre + " jours");
                    }
                }) ;
                capparjour.post(new Runnable() {
                    @Override
                    public void run() {
                        if (finalValeur!=0)capparjour.setText(Utiles.formatMtn(finalValeur/finalNbre) + " F");
                        else capparjour.setText(Utiles.formatMtn(finalValeur) + " F");
                    }
                }) ;
                catotal.post(new Runnable() {
                    @Override
                    public void run() {
                        catotal.setText(Utiles.formatMtn(finalValeur) + " F");
                    }
                }) ;
            }
        });

        try {
            operations = operationDAO.getAll(0,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),Long.valueOf(mParam2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        double annuler = 0 ;
        double credit = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1)  annuler += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            else if (operations.get(i).getPayer()==0)  credit += operations.get(i).getMontant()-operations.get(i).getRemise() ;
        }

        final double finalAnnuler = annuler;
        final double finalCredit = credit;

        opannuler.post(new Runnable() {
            @Override
            public void run() {
                opannuler.setText(Utiles.formatMtn(finalAnnuler) + " F");
            }
        });
        opcredit.post(new Runnable() {
            @Override
            public void run() {
                opcredit.setText(Utiles.formatMtn(finalCredit) + " F");
            }
        });
        try {
            operations = operationDAO.getAll(2,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),Long.valueOf(mParam2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1) continue;
            valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
        }
        nbre = 0 ;
        if (operations.size()>0){
            nbre = Utiles.daysBetween(operations.get(operations.size()-1).getDateoperation().getTime(),operations.get(0).getDateoperation().getTime()) ;
        }

        try {
            operations = operationDAO.getAll(5, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),Long.valueOf(mParam2));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1) continue;
            valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
        }
        if (operations.size()>0){
            nbre += Utiles.daysBetween(operations.get(operations.size()-1).getDateoperation().getTime(),operations.get(0).getDateoperation().getTime()) ;
        }
        final double finalValeur1 = valeur;
        final int finalNbre1 = nbre;

        deptotal.post(new Runnable() {
            @Override
            public void run() {
                deptotal.setText(Utiles.formatMtn(finalValeur1) + " F");
            }
        }) ;

        depparjour.post(new Runnable() {
            @Override
            public void run() {
                if (finalValeur1 !=0 && finalNbre1!=0){
                    float val = (int)finalValeur1 / finalNbre1 ;
                    depparjour.setText(Utiles.formatMtn(val) + " F");
                }
                else depparjour.setText(Utiles.formatMtn(finalValeur1) + " F");
            }
        }) ;

        try {
            operations = operationDAO.getAll(7,  DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),Long.valueOf(mParam2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1) continue;
            valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
        }
        final double finalValeur2 = valeur;
        pf.post(new Runnable() {
            @Override
            public void run() {
                pf.setText(Utiles.formatMtn(finalValeur2) + " F");
            }
        }) ;

        try {
            operations = operationDAO.getAll(8,  DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),Long.valueOf(mParam2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1) continue;
            valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
        }
        final double finalValeur3 = valeur;
        cf.post(new Runnable() {
            @Override
            public void run() {
                cf.setText(Utiles.formatMtn(finalValeur3) + " F");
            }
        }) ;

        try {
            operations = operationDAO.getAll(9, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),Long.valueOf(mParam2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1) continue;
            valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
        }
        final double finalValeur4 = valeur;
        pe.post(new Runnable() {
            @Override
            public void run() {
                pe.setText(Utiles.formatMtn(finalValeur4) + " F");
            }
        }) ;

        try {
            operations = operationDAO.getAll(10, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),Long.valueOf(mParam2));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        valeur = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1) continue;
            valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
        }

        final double finalValeur5 = valeur;
        ce.post(new Runnable() {
            @Override
            public void run() {
                ce.setText(Utiles.formatMtn(finalValeur5) + " F");
            }
        }) ;

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



    public class LoadStatTask extends AsyncTask<String, Integer, String> {
        Caisse caisse ;

        public LoadStatTask() {

        }

        @Override
        protected String doInBackground(String... url) {
            Date d = new Date() ;
            refresh();
            return "";
        }


        @Override
        protected void onPostExecute(String result) {
            getActivity().dismissDialog(AccueilActivity.PROGRESS_DIALOG_ID);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            getActivity().showDialog(AccueilActivity.PROGRESS_DIALOG_ID);
        }
    }


}
