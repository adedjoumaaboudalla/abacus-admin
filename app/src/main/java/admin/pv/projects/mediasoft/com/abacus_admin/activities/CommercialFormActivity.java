package admin.pv.projects.mediasoft.com.abacus_admin.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Date;

import admin.pv.projects.mediasoft.com.abacus_admin.R;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CommercialDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.PointVenteDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Commercial;
import admin.pv.projects.mediasoft.com.abacus_admin.model.PointVente;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Utiles;

public class CommercialFormActivity extends AppCompatActivity {

    private static final String FILEPATH = "filepath";
    private static final String PP = "PP";
    private static final String PM = "PM";
    EditText libelle = null ;
    EditText code = null ;
    EditText unite = null ;
    EditText prixV = null ;
    EditText prixA = null ;
    CheckBox switchCompat = null ;

    Button valider = null ;
    ImageButton canBtn = null ;
    ImageButton venteBtn = null ;
    ImageButton achatBtn = null ;

    boolean checked = false ;
    CommercialDAO commercialDAO = null ;
    AddCommercialTask addCommercialTask = null ;
    private ProgressDialog mProgressBar;
    final static int PROGRESS_DIALOG_ID = 0 ;
    private int MAX_SIZE;


    ImageView imageV = null ;

    private static final int CAMERA_CODE = 101, GALLERY_CODE = 201, CROPING_CODE = 301;

    private Uri mImageCaptureUri;
    private File outPutFile = null;
    private String filePath = null;
    private Commercial commercial;
    private String dossier;
    private boolean achat = false;
    private ScrollView sc = null ;
    private AlertDialog.Builder builder;
    private AlertDialog alert;
    private Button total =null ;
    private Button init =null ;
    private Button close = null;
    private RelativeLayout rv = null ;
    private Bitmap photo = null ;
    private EditText nom;
    private EditText prenom;
    private EditText email;
    private RadioGroup sexe;
    private EditText contact;
    private EditText adresse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commercial_form);

        setupToolbar() ;
        initialisation() ;

        if (savedInstanceState!=null) {
            filePath = savedInstanceState.getString(FILEPATH) ;
            if (filePath!=null)outPutFile = new File(filePath) ;
            processImage();
        }

        Intent intent = null ;
        intent = getIntent() ;
        if (intent!=null){
            long id = intent.getLongExtra("ID", 0) ;
            //int type = intent.getIntExtra("TYPE",0) ;
            dossier = Utiles.PV_PRODUIT_IMAGE_DIR ;
            //if (type==0) dossier = Utiles.FLUFF_PHOTO_PROFILS ;
            Log.e("DEBUG", String.valueOf(id)) ;
            if (id != 0){
                commercial = commercialDAO.getOne(id);
                if (commercial != null) {
                    init(commercial);
                    //if (produit.getImage() != null && !produit.getImage().equals("null") && !produit.getImage().equals("")) loadLocalImage(produit.getImage(),imageV);
                    //imageV.setImageBitmap(Utiles.loadImageFromExternalStorage(this,produit.getImage(),dossier));
                }
                else Toast.makeText(getApplicationContext(),  getString(R.string.echec), Toast.LENGTH_SHORT).show();
            }
        }
    }


    private void initialisation() {
        nom = (EditText) findViewById(R.id.nom);
        prenom = (EditText) findViewById(R.id.prenom);
        sexe = (RadioGroup) findViewById(R.id.sexegroup);
        email = (EditText) findViewById(R.id.email);
        contact = (EditText) findViewById(R.id.contact);
        adresse = (EditText) findViewById(R.id.adresse);
        commercialDAO = new CommercialDAO(this) ;


        valider = (Button) findViewById(R.id.valider);
        canBtn = (ImageButton) findViewById(R.id.cam);
        //venteBtn = (ImageButton) findViewById(R.id.btnvente);
        //achatBtn = (ImageButton) findViewById(R.id.btnachat);
        imageV = (ImageView) findViewById(R.id.image);

        imageV.setImageResource(R.mipmap.ic_product);

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCommercial();
            }
        });



        imageV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    private void init(Commercial commercial) {
        nom.setText(commercial.getNom());
        prenom.setText(commercial.getPrenom());
        contact.setText(commercial.getContact());
        adresse.setText(commercial.getAdresse());
        email.setText(commercial.getEmail());
        if (commercial.getSexe().equals("PM")) sexe.check(R.id.pm);
        else if (commercial.getSexe().equals("Masculin")) sexe.check(R.id.m);
        else  sexe.check(R.id.f);
    }




    private void selectImageOption() {
        final CharSequence[] items = {getString(R.string.camera), getString(R.string.gallery), getString(R.string.delete), getString(R.string.annuler)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.addfoto));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals(getString(R.string.camera))) {

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    mImageCaptureUri = Uri.fromFile(f);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                    startActivityForResult(intent, CAMERA_CODE);

                } else if (items[item].equals(getString(R.string.gallery))) {

                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, GALLERY_CODE);

                }else if (items[item].equals(getString(R.string.delete))) {

                    if(filePath!=null){
                        filePath = null;
                        imageV.setImageDrawable(null);
                    }


                } else {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        if (requestCode == GALLERY_CODE && resultCode == RESULT_OK && null != imageReturnedIntent) {

            Uri selectedImage = imageReturnedIntent.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filePath = cursor.getString(columnIndex);
            cursor.close();

            outPutFile = new File(filePath) ;
            Log.e("PATH", filePath) ;

            //Bitmap img = BitmapFactory.decodeFile(filePath);
            //img = Utiles.getResizedBitmap(img, 750, 1200);
            //imageView.setImageBitmap(img);

            processImage();

        } else if (requestCode == CAMERA_CODE && resultCode == RESULT_OK) {

            Log.e("Camera Image URI : ", String.valueOf(mImageCaptureUri));
            //cropingIMG();
            outPutFile = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
            processImage();
        } else if (requestCode == CROPING_CODE  && resultCode == RESULT_OK) {

            try {
                if(outPutFile.exists()){
                    photo = Utiles.saveScaledPhotoToFile(Utiles.decodeFile(outPutFile));
                    filePath = outPutFile.getAbsolutePath();
                    Log.e("PATH", filePath) ;
                    imageV.setImageBitmap(photo);
                }
                else {
                    Toast.makeText(getApplicationContext(),  getString(R.string.echec), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    public void processImage(){
        try {
            if(outPutFile.exists()){
                photo = Utiles.saveScaledPhotoToFile(Utiles.decodeFile(outPutFile));
                filePath = outPutFile.getAbsolutePath();
                Log.e("PATH", filePath) ;
                imageV.setImageBitmap(photo);
            }
            else {
                Toast.makeText(getApplicationContext(),  getString(R.string.echec), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void setupToolbar(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Show menu icon
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_back);
        ab.setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_commercial_form, menu);
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putString(FILEPATH, filePath);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(FILEPATH,filePath);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) finish();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        super.onCreateDialog(id);
        if (mProgressBar ==null){
            mProgressBar = new ProgressDialog(this) ;
            mProgressBar.setCancelable(true);
            mProgressBar.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    try {
                        addCommercialTask.cancel(true);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            mProgressBar.setTitle(getString(R.string.send_loding));
            mProgressBar.setMessage(getString(R.string.wait));
            mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressBar.setMax(MAX_SIZE);
        }

        return mProgressBar ;
    }

    private void addCommercial() {
        if (isValid()){
            showDialog(PROGRESS_DIALOG_ID);
            if (commercial==null)commercial = new Commercial() ;
            RadioButton rb = ((RadioButton)findViewById(sexe.getCheckedRadioButtonId())) ;
            commercial.setNom(nom.getText().toString());
            PointVente pv = new PointVenteDAO(CommercialFormActivity.this).getLast() ;
            commercial.setPointvente_id(pv.getId());
            commercial.setPrenom(prenom.getText().toString());
            commercial.setContact(contact.getText().toString());
            commercial.setEmail(email.getText().toString());
            commercial.setAdresse(adresse.getText().toString());
            commercial.setSexe(rb.getText().toString());
            commercial.setUtilisateur_id(pv.getUtilisateur());

            addCommercialTask = new AddCommercialTask(commercial) ;
            addCommercialTask.execute() ;
        }
        else {
            Toast.makeText(getApplicationContext(), getString(R.string.data_errors1), Toast.LENGTH_LONG).show();
        }
    }

    private boolean isValid() {
        if (sexe.getCheckedRadioButtonId()==R.id.m && nom.getText().length() <= 0) return false ;
        if (sexe.getCheckedRadioButtonId()==R.id.f && prenom.getText().length() <= 0) return false ;
        if (contact.getText().length() <= 0) return false ;

        return true;
    }

    public class AddCommercialTask extends AsyncTask<Void, Integer, String> {

        String lib = null ;
        String code = null ;
        String unite = null ;
        String prixVente = null ;
        String prixAchat = null ;
        Commercial commercial ;

        public AddCommercialTask(Commercial p) {
            commercial = p ;
        }

        @Override
        protected String doInBackground(Void... params) {
            Date d = new Date() ;

            commercial.setEtat(0);
            long result = 0 ;

            if (commercial.getId()==0)  result =  commercialDAO.add(commercial) ;
            else   result =  commercialDAO.update(commercial) ;

            return String.valueOf(result);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            valider.setEnabled(true);
            commercial = commercialDAO.getLast() ;
            if (Long.valueOf(result)>0) {
                if (filePath!=null) {
                    /*commercial = commercialDAO.getLast() ;
                    try {
                        Uri path = Utiles.saveImageExternalStorage(photo, CommercialFormActivity.this, commercial.getId()+".jpg", Utiles.PV_COMMERCIAL_IMAGE_DIR) ;
                        commercial.setImage(path.getPath());
                        commercialDAO.update(commercial) ;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    */
                }


                if (commercial.getId_externe()==0)Toast.makeText(getApplicationContext(),getString(R.string.commercial_success),Toast.LENGTH_LONG).show();
                else Toast.makeText(getApplicationContext(),getString(R.string.commercial_update),Toast.LENGTH_LONG).show();

                clean();
            }
            else
                Toast.makeText(getApplicationContext(),getString(R.string.echec_ajout),Toast.LENGTH_LONG).show();
            dismissDialog(PROGRESS_DIALOG_ID);
        }





        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (commercialDAO==null)commercialDAO = new CommercialDAO(getApplicationContext()) ;
        }
    }

    private void clean() {
        nom.setText("");
        prenom.setText("");
        contact.setText("");
        email.setText("");
        adresse.setText("");
    }


    public void diminuer(int v, TextView tv){
        int val = 0 ;
        int prix = 0 ;
        if (tv.getVisibility()!=View.GONE) val = Integer.parseInt(tv.getText().toString());
        String p = prixA.getText().toString() ;
        if (p.length()==0) prixA.setText("0");
        p = prixV.getText().toString() ;
        if (p.length()==0) prixV.setText("0");

        // Si le total afficher est superieur à zero
        if (!total.getText().equals("0")){

            prix = Integer.parseInt(total.getText().toString()) ;
            if (prix>v) prix -= v ;

            // Si c'est achat donc on rempli la case prix achat
            if (achat){
                prixA.setText(String.valueOf(prix));
            }
            // Sinon on rempli la case prix vente
            else {
                prixV.setText(String.valueOf(prix));
            }
            // S'il ya une etiquette sur les billets on les met à jour
            if (val>0){
                val-- ;
                tv.setText(String.valueOf(val));
                tv.setVisibility(View.VISIBLE);
            }

            total.setText(String.valueOf(prix));
        }

        if (val==0)tv.setVisibility(View.GONE);
    }



    public void augmenter(int v,TextView tv){
        int val = 0 ;
        int prix = 0 ;
        if (tv.getVisibility()!=View.GONE) val = Integer.parseInt(tv.getText().toString());

        String p = prixA.getText().toString() ;
        if (p.length()==0) prixA.setText("0");
        p = prixV.getText().toString() ;
        if (p.length()==0) prixV.setText("0");
        val++ ;
        // Si c'est achat donc on rempli la case prix achat
        if (achat){
            prix += v + Integer.parseInt(prixA.getText().toString()) ;
            prixA.setText(String.valueOf(prix));
        }
        // Sinon on rempli la case prix vente
        else {
            prix += v + Integer.parseInt(prixV.getText().toString()) ;
            prixV.setText(String.valueOf(prix));
        }
        // On met à jour les etiquettes
        tv.setText(String.valueOf(val));
        tv.setVisibility(View.VISIBLE);

        total.setText(String.valueOf(prix));
    }


}
