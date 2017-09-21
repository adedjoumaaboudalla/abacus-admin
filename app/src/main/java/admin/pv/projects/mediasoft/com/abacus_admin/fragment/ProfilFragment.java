package admin.pv.projects.mediasoft.com.abacus_admin.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import admin.pv.projects.mediasoft.com.abacus_admin.R;
import admin.pv.projects.mediasoft.com.abacus_admin.activities.AccueilActivity;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CaisseDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.ClientDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.DAOBase;
import admin.pv.projects.mediasoft.com.abacus_admin.database.CaisseHelper;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Caisse;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Client;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Url;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Utiles;
import okhttp3.FormBody;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfilFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfilFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText nom, prenom, tel, adresse, raisonsocial, email, password, confirmation, passactuel ;
    Button valider, annuler ;

    private OnFragmentInteractionListener mListener;
    private ClientDAO clientDAO;

    public ProfilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfilFragment newInstance(String param1, String param2) {
        ProfilFragment fragment = new ProfilFragment();
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
        View v = inflater.inflate(R.layout.fragment_profil, container, false);

        nom = (EditText) v.findViewById(R.id.nom);
        prenom = (EditText) v.findViewById(R.id.prenom);
        tel = (EditText) v.findViewById(R.id.tel);
        adresse = (EditText) v.findViewById(R.id.adresse);
        raisonsocial = (EditText) v.findViewById(R.id.raisonsocial);
        email = (EditText) v.findViewById(R.id.mail);
        passactuel = (EditText) v.findViewById(R.id.password);
        password = (EditText) v.findViewById(R.id.newpass);
        confirmation = (EditText) v.findViewById(R.id.confirmation);
        valider = (Button) v.findViewById(R.id.valider);
        annuler = (Button) v.findViewById(R.id.annuler);

        getActivity().setTitle(R.string.profil);
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modification() ;
            }
        });
        clientDAO = new ClientDAO(getActivity()) ;

        initialisation(clientDAO.getLast()) ;
        return v ;
    }

    private void modification() {
        if (!Utiles.isConnected(getActivity())) {
            Toast.makeText(getActivity(),R.string.noconnexion , Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isCorrecte()) {
            Toast.makeText(getActivity(), R.string.data_errors1 , Toast.LENGTH_SHORT).show();
        }
        else{
            ModificationTask modificationTask = new ModificationTask(passactuel.getText().toString(),nom.getText().toString(),prenom.getText().toString(),raisonsocial.getText().toString(),tel.getText().toString(),adresse.getText().toString(),email.getText().toString(),confirmation.getText().toString()) ;
            modificationTask.execute() ;
        }
    }

    private boolean isCorrecte() {
        if (confirmation.getText().length()>0){
            if (!confirmation.getText().toString().equals(password.getText().toString())) return false ;
            if (confirmation.getText().toString().length()<6) return false ;
        }
        return true;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initialisation(Client client) {
        nom.setText(client.getNom());
        prenom.setText(client.getPrenom());
        raisonsocial.setText(client.getRaisonsocial());
        adresse.setText(client.getAdresse());
        tel.setText(client.getTel());
        email.setText(client.getEmail());
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }



    public class ModificationTask extends AsyncTask<Void, Integer, String> {
        String nom, prenom,raisonsocial, tel, adresse, email, passactuel, confirmation ;
        Caisse caisse ;

        public ModificationTask(String passactuel,String nom, String prenom, String raison, String tel, String adress, String email, String confirm) {
            this.nom = nom ;
            this.prenom = prenom ;
            this.raisonsocial = raison ;
            this.tel = tel ;
            this.adresse = adress ;
            this.email = email ;
            this.confirmation = confirm ;
            this.passactuel = passactuel ;
        }

        @Override
        protected String doInBackground(Void... params) {
            Date d = new Date() ;


            FormBody.Builder formBuilder = new FormBody.Builder() ;
            formBuilder.add("nom", nom);
            formBuilder.add("prenom", prenom);
            formBuilder.add("raisonsocial", raisonsocial);
            formBuilder.add("tel", tel);
            formBuilder.add("adresse", adresse);
            formBuilder.add("email", email);
            formBuilder.add("passactuel", passactuel);
            formBuilder.add("confirmation", confirmation);
            formBuilder.add("client_id", String.valueOf(clientDAO.getLast().getId()));

            String result = " ";
            try {
                result = Utiles.POST(Url.getModifProfil(getActivity()), formBuilder.build());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.e("URL", Url.getModifProfil(getActivity()));

            Log.e("Reponse",result) ;

            if (result.split(":").length == 2 && result.contains("OK")) {
                // Si le caisse est un caisse qui ne se trouve pas sur le serveur
            }
            else result = "KO" ;

            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("KO")) Toast.makeText(getActivity().getApplicationContext(),getString(R.string.echec_modif),Toast.LENGTH_LONG).show();
            else{
                Client client = clientDAO.getLast() ;
                client.setNom(nom);
                client.setPrenom(prenom);
                client.setTel(tel);
                client.setRaisonsocial(raisonsocial);
                client.setAdresse(adresse);
                client.setEmail(email);

                clientDAO.update(client) ;
                clean();
                initialisation(client);
                Toast.makeText(getActivity(), R.string.modifreussi, Toast.LENGTH_SHORT).show();
            }

            getActivity().dismissDialog(AccueilActivity.PROGRESS_DIALOG_ID);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            clientDAO = new ClientDAO(getActivity()) ;
            getActivity().showDialog(AccueilActivity.PROGRESS_DIALOG_ID);
        }
    }

    private void clean() {
        nom.setText("");
        prenom.setText("");
        password.setText("");
        raisonsocial.setText("");
        adresse.setText("");
        tel.setText("");
        confirmation.setText("");
        passactuel.setText("");
    }


}
