package admin.pv.projects.mediasoft.com.abacus_admin.utile;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;

/**
 * Created by Mayi on 07/10/2015.
 */

public class Url {
    public static String ipConfig = "ipserver" ;
    //public static String serverIp = "http://1 92.168.0.104/" ;
    //public static String serverIp = "quickprojet.mediasoftmoney.com";
    //public static String serverIp = "mediasofthome.com";
    public static String serverIp = "50.63.13.84/~abacusweb/pvente";
    //local
    //public static String serverIp = "192.168.10.6/pvente_serviceweb" ;


    public static String getCheckAndInitCaisse(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(ipConfig, serverIp) + "/service_web/authentification.php";

        return result ;
    }



    public static String getLoadOperationUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(ipConfig, serverIp) + "/service_web/loadoperation.php";

        return result ;
    }


    public static String getLoadOperationsUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(ipConfig, serverIp) + "/service_web/loadoperations.php";

        return result ;
    }



    public static String getLoadReglementUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(ipConfig, serverIp) + "/service_web/loadpayements.php";

        return result ;
    }



    public static String getPostMouvementUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(ipConfig, serverIp) + "/service_web/addMouvement.php";

        return result ;
    }

    public static String getPostOperationUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(ipConfig, serverIp) + "/service_web/addOperation.php";

        return result ;
    }



    public static String getPostProduitUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(ipConfig, serverIp) + "/service_web/addProduit.php";

        return result ;
    }




    public static String getPostPartenaireUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(ipConfig, serverIp) + "/service_web/addPartenaire.php";

        return result ;
    }




    public static String getPostCommercialUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(ipConfig, serverIp) + "/service_web/addCommercial.php";

        return result ;
    }



    public static String getPostPointVenteUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(ipConfig, serverIp) + "/service_web/addPointvente.php";

        return result ;
    }


    public static String getDeletePointVenteUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(ipConfig, serverIp) + "/service_web/deletePointvente.php";

        return result ;
    }


    public static String getSendProduitPointvente(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(ipConfig, serverIp) + "/service_web/addproduitpointvente.php";

        return result ;
    }



    public static String getSendProduitPredefinie(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(ipConfig, serverIp) + "/service_web/chooseproduit.php";

        return result ;
    }


    public static String getLoadProduitPredefinie(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(ipConfig, serverIp) + "/service_web/loadproduit.php";

        return result ;
    }



    public static String getPostCaisseUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(ipConfig, serverIp) + "/service_web/addCaisse.php";

        return result ;
    }


    public static String getModifProfil(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(ipConfig, serverIp) + "/service_web/modifprofil.php";

        return result ;
    }


    public static String getImageDirectory(Context context, String image) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(ipConfig, serverIp) + "/uploads/produits/"+ image;

        return result ;
    }


    public static String getSendImageUrl(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(ipConfig, serverIp) + "/service_web/addimageproduit.php";
        return result ;
    }



    public static String getDeleteProduit(Context context) {
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        String result = "http://" + preferences.getString(ipConfig, serverIp) + "/service_web/deleteproduit.php";

        return result ;
    }

}
