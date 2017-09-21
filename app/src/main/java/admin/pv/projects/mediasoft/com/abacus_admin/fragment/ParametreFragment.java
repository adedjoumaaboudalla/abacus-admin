package admin.pv.projects.mediasoft.com.abacus_admin.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import admin.pv.projects.mediasoft.com.abacus_admin.MainActivity;
import admin.pv.projects.mediasoft.com.abacus_admin.R;
import admin.pv.projects.mediasoft.com.abacus_admin.activities.ReglageActivity;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ParametreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ParametreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ParametreFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ListView lv;

    public ParametreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ParametreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParametreFragment newInstance(String param1, String param2) {
        ParametreFragment fragment = new ParametreFragment();
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
        View v = inflater.inflate(R.layout.fragment_parametre, container, false);

        lv = (ListView) v.findViewById(R.id.list);

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,new String[]{getString(R.string.changecmt), getString(R.string.changepasseadmin), getString(R.string.reglege)}) ;
        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0 : {
                        Intent intent = new Intent(getActivity(),MainActivity.class) ;
                        intent.putExtra("change",true) ;
                        startActivity(intent);
                    }
                    break;
                    case 1 : {
                        changePassword() ;
                    }
                    break;
                    case 2 : {
                        Intent intent = new Intent(getActivity(),ReglageActivity.class) ;
                        startActivity(intent);
                    }
                    break;
                }
            }
        });

        getActivity().setTitle(R.string.parametres);
        return  v ;
    }

    private void changePassword() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()) ;
        ScrollView scrollView = (ScrollView) getActivity().getLayoutInflater().inflate(R.layout.changepass,null);

        final EditText password = (EditText) scrollView.findViewById(R.id.password);
        final EditText password1 = (EditText) scrollView.findViewById(R.id.password1);
        final EditText confirmed = (EditText) scrollView.findViewById(R.id.confirmed);
        Button valider = (Button) scrollView.findViewById(R.id.valider);
        Button annuler = (Button) scrollView.findViewById(R.id.annuler);


        final SharedPreferences preferences = (SharedPreferences) PreferenceManager.getDefaultSharedPreferences(getActivity());

        builder.setTitle(R.string.app_name) ;
        builder.setView(scrollView) ;
        final AlertDialog alert = builder.show();;

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getText().toString().equals(preferences.getString("adminpass","admin"))) {
                    if (password1.getText().toString().equals(confirmed.getText().toString())){
                        if (confirmed.getText().toString().length()>=6) {
                            SharedPreferences.Editor editor = preferences.edit() ;
                            editor.putString("adminpass",password1.getText().toString()) ;
                            editor.commit() ;
                            Toast.makeText(getActivity(), R.string.passwordmodifier, Toast.LENGTH_SHORT).show();
                            alert.dismiss();
                        }
                        else Toast.makeText(getActivity(), R.string.tropcourt, Toast.LENGTH_SHORT).show();
                    }
                    else Toast.makeText(getActivity(), R.string.passpareil, Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getActivity(), R.string.passincorrect, Toast.LENGTH_SHORT).show();
            }
        });

        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        builder.setTitle(R.string.app_name) ;
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
