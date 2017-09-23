package admin.pv.projects.mediasoft.com.abacus_admin.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import admin.pv.projects.mediasoft.com.abacus_admin.R;
import admin.pv.projects.mediasoft.com.abacus_admin.activities.DashboardActivity;
import admin.pv.projects.mediasoft.com.abacus_admin.activities.WebViewActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AccueilFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AccueilFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccueilFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    public static final String PROFIL_URI = "profiluri" ;
    CardView profilCard = null ;

    public static final String PV_URI = "pvuri" ;
    CardView pvCard = null ;

    public static final String OPERATION_URI = "operationuri" ;
    CardView operationCard = null ;

    public static final String PRODUIT_URI = "produituri" ;
    CardView produitCard = null ;

    public static final String COMMERCIAU_URI = "comerciauxuri" ;
    CardView commerciauxCard = null ;

    public static final String PARTENAIRE_URI = "partenaireuri" ;
    CardView partenaireCard = null ;

    public static final String PARAMETRE_URI = "parametreuri" ;
    CardView parametreCard = null ;

    public static final String ETAT_URI = "etaturi" ;
    CardView etatCard = null ;

    CardView pointventeCard = null ;
    CardView caisseCard = null ;

    public static final String EXIT_URI = "exituri" ;
    CardView exitCard = null ;
    private CardView dashCard;

    private CardView predefiniProduitCard;
    public static final String PREDEF_URI = "predefiniProduitCarduri" ;

    public AccueilFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccueilFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccueilFragment newInstance(String param1, String param2) {
        AccueilFragment fragment = new AccueilFragment();
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
        View v =  inflater.inflate(R.layout.fragment_accueil, container, false);
        pvCard = (CardView) v.findViewById(R.id.pvCard);
        pvCard.setOnClickListener(this);

        dashCard = (CardView) v.findViewById(R.id.dashCard);
        dashCard.setOnClickListener(this);

        profilCard = (CardView) v.findViewById(R.id.profilCard);
        profilCard.setOnClickListener(this);

        operationCard = (CardView) v.findViewById(R.id.operationCard);
        operationCard.setOnClickListener(this);

        produitCard = (CardView) v.findViewById(R.id.produitCard);
        produitCard.setOnClickListener(this);

        commerciauxCard = (CardView) v.findViewById(R.id.commerciauxCard);
        commerciauxCard.setOnClickListener(this);

        partenaireCard = (CardView) v.findViewById(R.id.partenaireCard);
        partenaireCard.setOnClickListener(this);

        parametreCard = (CardView) v.findViewById(R.id.parametreCard);
        parametreCard.setOnClickListener(this);

        exitCard = (CardView) v.findViewById(R.id.exitCard);
        exitCard.setOnClickListener(this);

        etatCard = (CardView) v.findViewById(R.id.etatCard);
        etatCard.setOnClickListener(this);

        pointventeCard = (CardView) v.findViewById(R.id.pointventeCard);
        pointventeCard.setOnClickListener(this);

        caisseCard = (CardView) v.findViewById(R.id.caisseCard);
        caisseCard.setOnClickListener(this);

        predefiniProduitCard = (CardView) v.findViewById(R.id.predefiniProduitCard);
        predefiniProduitCard.setOnClickListener(this);

        return  v ;
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.pvCard : mListener.onFragmentInteraction(Uri.parse(PV_URI)); break;
            case R.id.operationCard : mListener.onFragmentInteraction(Uri.parse(OPERATION_URI)); break;
            case R.id.produitCard : mListener.onFragmentInteraction(Uri.parse(PRODUIT_URI)); break;
            case R.id.commerciauxCard : mListener.onFragmentInteraction(Uri.parse(COMMERCIAU_URI)); break;
            case R.id.partenaireCard : mListener.onFragmentInteraction(Uri.parse(PARTENAIRE_URI)); break;
            case R.id.profilCard : mListener.onFragmentInteraction(Uri.parse(PROFIL_URI)); break;
            case R.id.etatCard : mListener.onFragmentInteraction(Uri.parse(ETAT_URI)); break;
            case R.id.predefiniProduitCard : mListener.onFragmentInteraction(Uri.parse(PREDEF_URI)); break;
            case R.id.parametreCard : mListener.onFragmentInteraction(Uri.parse(PARAMETRE_URI)); break;
            case R.id.exitCard : mListener.onFragmentInteraction(Uri.parse(EXIT_URI)); break;
            case R.id.dashCard : {
                Intent intent = new Intent(getActivity(), DashboardActivity.class) ;
                startActivity(intent);
            } break;
            case R.id.pointventeCard : {
                Intent intent = new Intent(getActivity(), WebViewActivity.class) ;
                intent.putExtra("url","pointventes") ;
                startActivity(intent);
            } break;
            case R.id.caisseCard : {
                Intent intent = new Intent(getActivity(), WebViewActivity.class) ;
                intent.putExtra("url","caisse") ;
                startActivity(intent);
            } break;
        }
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

    @Override
    public void onResume() {
        super.onResume();
    }
}
