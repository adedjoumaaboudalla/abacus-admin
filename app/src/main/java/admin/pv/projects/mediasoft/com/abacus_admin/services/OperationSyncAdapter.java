package admin.pv.projects.mediasoft.com.abacus_admin.services;

import android.accounts.Account;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import admin.pv.projects.mediasoft.com.abacus_admin.R;
import admin.pv.projects.mediasoft.com.abacus_admin.activities.AccueilActivity;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CaisseDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.ClientDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CommercialDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.DAOBase;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.MouvementDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.OperationDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.PartenaireDAO;

import admin.pv.projects.mediasoft.com.abacus_admin.dao.PointVenteDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.ProduitDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Caisse;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Mouvement;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Operation;

import admin.pv.projects.mediasoft.com.abacus_admin.utile.Url;
import admin.pv.projects.mediasoft.com.abacus_admin.utile.Utiles;
import okhttp3.FormBody;

/**
 * Created by mediasoft on 17/11/2016.
 */
public class OperationSyncAdapter extends AbstractThreadedSyncAdapter {

    private static final int ID_NOTIFICATION = 1;
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;
    Context context = null ;
    private long id;
    Calendar calendar = Calendar.getInstance() ;
    String login = null ;
    String password = null ;
    private ClientDAO clientDAO;
    private String res;
    private static final String MAUVAIS = "KO";
    private static final String BON = "OK";

    private int etape = 1;
    private ProduitDAO produitDAO;
    private OperationDAO operationDAO;
    private MouvementDAO mouvementDAO;
    private PartenaireDAO partenaireDAO;
    private CommercialDAO commercialDAO;
    private int PROGRESS = 0 ;
    private PointVenteDAO pointVenteDAO;
    private CaisseDAO caisseDAO;
    NotificationCompat.InboxStyle inboxStyle ;
    private ArrayList<Operation> operations;

    public OperationSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.context = context ;
        initialisation();
    }

    private void initialisation() {
        caisseDAO = new CaisseDAO(context);
        clientDAO = new ClientDAO(context);
        operationDAO = new OperationDAO(context) ;
        produitDAO = new ProduitDAO(context) ;
        mouvementDAO = new MouvementDAO(context) ;
        partenaireDAO = new PartenaireDAO(context) ;
        commercialDAO = new CommercialDAO(context) ;
        inboxStyle = new NotificationCompat.InboxStyle();
    }



    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {


        if (clientDAO.getLast()==null) return ;

        String result = loadoperation(Url.getLoadOperationsUrl(context));;

        if (result.equals(BON)) {
            showNotif(null) ;
        }

    }

    private void showNotif(String tickerText) {
        int icon = 0 ;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) icon = R.drawable.notif_icon;
        else  icon = R.mipmap.ic_launcher ;

        if (tickerText==null)tickerText = context.getString(R.string.newop);
        long when = System.currentTimeMillis();

        Intent notificationIntent = null;


        notificationIntent = new Intent(context, AccueilActivity.class);
        notificationIntent.putExtra("POS", 1);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setPriority(Notification.PRIORITY_HIGH);
        inboxStyle.setBigContentTitle(context.getString(R.string.app_name))
                .setSummaryText(tickerText);

// Moves the expanded layout object into the notification object.
        builder.setStyle(inboxStyle);
        Notification notification = builder.setContentIntent(contentIntent)
                .setSmallIcon(icon)
                .setTicker(tickerText)
                .setWhen(when)
                .setContentText(tickerText)
                .setContentTitle(context.getString(R.string.app_name))
                .build();

        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        // Récupération du Notification Manager
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(ID_NOTIFICATION, notification);
    }

    private String loadoperation(String... url) {

        FormBody.Builder formBuilder = new FormBody.Builder() ;
        formBuilder.add("client", String.valueOf(clientDAO.getLast().getId()));
        if (operationDAO.getLast()!=null)formBuilder.add("operation", String.valueOf(operationDAO.getLast().getId()));
        else  formBuilder.add("operation", "0");
        Log.e("client",String.valueOf(clientDAO.getLast().getId())) ;
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
                    mouvement.setQuantite(mouvementObj.getDouble("quantite"));
                    mouvement.setRestant(mouvementObj.getDouble("restant"));
                    if(!mouvementObj.getString("cmup").equals("null"))mouvement.setCmup(mouvementObj.getDouble("cmup"));
                    mouvement.setProduit_id(mouvementObj.getLong("produit_id"));
                    mouvement.setOperation_id(mouvementObj.getLong("operation_id"));
                    mouvement.setProduit(mouvementObj.getString("produit"));
                    try {
                        mouvement.setCreated_at(DAOBase.formatter.parse(mouvementObj.getString("created_at")));
                        Log.e("DATE " + mouvement.getId(), DAOBase.formatter.format(mouvement.getCreated_at())) ;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mouvementDAO.add(mouvement) ;
                }




                JSONArray caisseArr = obj.getJSONArray("caisses");
                size = caisseArr.length() ;
                JSONObject caisseObj = null ;
                Caisse caisse = null ;


                for (int i = 0; i < size; i++) {
                    caisseObj = caisseArr.getJSONObject(i);
                    caisse = new Caisse();
                    caisse.setId(caisseObj.getLong("id"));
                    caisse = caisseDAO.getOne(caisse.getId()) ;
                    caisse.setSolde(caisseObj.getDouble("solde"));

                    caisseDAO.update(caisse) ;
                }


                JSONArray operationArr = obj.getJSONArray("operations");
                size = operationArr.length() ;
                JSONObject operationObj = null ;
                Operation operation = null ;
// Moves events into the expanded layout
                for (int i = 0; i < size; i++) {
                    operationObj = operationArr.getJSONObject(i);
                    operation = new Operation();
                    operation.setId_externe(operationObj.getLong("id"));
                    operation.setId(operationObj.getLong("id"));
                    operation.setAnnuler(operationObj.getInt("annuler"));
                    operation.setAttente(0);
                    operation.setToken(operationObj.getString("token"));
                    operation.setCaisse(operationObj.getLong("caisse_id"));
                    if (!operationObj.getString("operation_id").equals("null"))operation.setOperation_id(operationObj.getLong("operation_id"));
                    if (!operationObj.getString("compte_banque_id").equals("null"))operation.setComptebanque_id(operationObj.getLong("compte_banque_id"));
                    if (!operationObj.getString("commercial_id").equals("null"))operation.setCommercialid(operationObj.getLong("commercial_id"));
                    operation.setEntree(operationObj.getInt("entree"));
                    if (!operationObj.getString("partenaire_id").equals("null"))operation.setPartenaire_id(operationObj.getLong("partenaire_id"));
                    operation.setPayer(operationObj.getInt("payer"));
                    operation.setNbreproduit(operationObj.getInt("nbreproduit"));
                    operation.setTypeOperation_id(operationObj.getString("typeoperation"));
                    operation.setDescription(operationObj.getString("description"));
                    operation.setClient(operationObj.getString("client"));
                    operation.setMontant(operationObj.getDouble("montant"));
                    operation.setModepayement(operationObj.getString("modepayement"));
                    operation.setRecu(operationObj.getDouble("recu"));
                    operation.setRemise(operationObj.getDouble("remise"));
                    operation.setMontant_ammor(operationObj.getDouble("montant_amorti"));
                    try {
                        operation.setDateoperation(DAOBase.formatter.parse(operationObj.getString("created_at")));
                        operation.setCreated_at(DAOBase.formatter.parse(operationObj.getString("created_at")));
                        operation.setDateecheance(DAOBase.formatter2.parse(operationObj.getString("date_echeance")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        operation.setDateannulation(DAOBase.formatter.parse(operationObj.getString("date_annulation")));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String text = "" ;
                    if (operation.getTypeOperation_id().equals(OperationDAO.VENTE)) text = context.getString(R.string.vente) + " : " + Utiles.formatMtn(operation.getMontant()) ;
                    if (operation.getTypeOperation_id().equals(OperationDAO.DEPENSE)) text = context.getString(R.string.depense) +  " : "  + Utiles.formatMtn(operation.getMontant()) ;
                    if (operation.getTypeOperation_id().equals(OperationDAO.ACHAT)) text = context.getString(R.string.achat) +  " : "  + Utiles.formatMtn(operation.getMontant()) ;
                    if (operation.getTypeOperation_id().equals(OperationDAO.CH_EXC)) text = context.getString(R.string.chargeexp) +  " : "  + Utiles.formatMtn(operation.getMontant()) ;
                    if (operation.getTypeOperation_id().equals(OperationDAO.CH_FN)) text = context.getString(R.string.chargefin) +  " : "  + Utiles.formatMtn(operation.getMontant()) ;
                    if (operation.getTypeOperation_id().equals(OperationDAO.CMDCLNT)) text = context.getString(R.string.cmdclt) +  " : "  + Utiles.formatMtn(operation.getMontant()) ;
                    if (operation.getTypeOperation_id().equals(OperationDAO.CMDFRNSS)) text = context.getString(R.string.cmdfr) +  " : "  + Utiles.formatMtn(operation.getMontant()) ;
                    if (operation.getTypeOperation_id().equals(OperationDAO.DIVERS)) text = context.getString(R.string.divers) +  " : "  + Utiles.formatMtn(operation.getMontant()) ;
                    if (operation.getTypeOperation_id().equals(OperationDAO.CMDFRNSS)) text = context.getString(R.string.cmdfr) +  " : "  + Utiles.formatMtn(operation.getMontant()) ;
                    inboxStyle.addLine(text);
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

}
