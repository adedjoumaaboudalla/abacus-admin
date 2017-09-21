package admin.pv.projects.mediasoft.com.abacus_admin.utile;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.serialport.api.PrintService;
import android.serialport.api.PrinterClass;
import android.serialport.api.PrinterClassSerialPort;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import admin.pv.projects.mediasoft.com.abacus_admin.R;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.MouvementDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.OperationDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.PointVenteDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Mouvement;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Operation;


/**
 * Created by Mayi on 12/01/2016.
 */
public class PrintPDA {

    PrintService printservice = new PrintService();
    protected static final String TAG = "Print Utils";
    private Handler mHandler;
    MouvementDAO mouvementDAO = null ;
    PointVenteDAO pointVenteDAO = null ;
    private String msgFin = null;
    private String societeNom = null;
    private String societeAdresse = null;
    String msg;
    Context context;
    PrinterClassSerialPort printerClass = null ;
    public static SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy H:m:s") ;
    String macAddress = null ;
    WifiManager wifiManager ;
    private OperationDAO operationDAO;


    public PrintPDA(Context context){
        SharedPreferences preferences =  PreferenceManager.getDefaultSharedPreferences(context);
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        macAddress = wInfo.getMacAddress();pointVenteDAO = new PointVenteDAO(context) ;
        operationDAO = new OperationDAO(context) ;
        mouvementDAO = new MouvementDAO(context) ;
        pointVenteDAO = new PointVenteDAO(context) ;
        this.context = context ;
        societeNom = pointVenteDAO.getLast().getLibelle() ;
        societeAdresse = context.getString(R.string.tel) + pointVenteDAO.getLast().getTel() + context.getString(R.string.adresse) + pointVenteDAO.getLast().getVille()+ ", "+ pointVenteDAO.getLast().getPays();
        msgFin = preferences.getString("messagefinal", "PointOperation  Copyright Media Soft") ;
        initPrintPDA();
    }


    
    public void printTicket(long id){
        Operation operation = operationDAO.getOne(id) ;
        if (operation==null) return;

        ArrayList<Mouvement> mouvements = mouvementDAO.getMany(operation.getId()) ;

        Calendar cal = Calendar.getInstance() ;
        msg = "##############################";
        msg+= "\n";
        msg +=  societeNom ;
        msg+= "\n";
        msg +=  societeAdresse ;
        msg+= "\n";
        msg += "################################";
        msg+= "\n";
        msg+= "Date      : "+ formatter.format(new Date());
        msg+= "\n";
        msg+= "Ticket No : "+ operation.getId()+ "/" + cal.get(Calendar.YEAR);
        msg+= "\n";
        msg+= "Client    : "+ operation.getClient();
        msg+= "\n";
        msg += "--------------------------------";
        msg += "\n";
        msg+= "DESIGNATION  Qte   Prix   Total";
        msg += "\n";
        msg += "--------------------------------";
        msg+= "\n";
        int n = mouvements.size() ;
        for (int i = 0; i < n; i++){
            Mouvement mv = mouvements.get(i) ;
            msg+= name(mv.getProduit()) + " " + quantite(String.valueOf(mv.getQuantite())) + " " + mv.getPrixV()   + " " + String.valueOf(mv.getPrixV()*mv.getQuantite())  ;
            msg+= "\n";
        }
        msg += "--------------------------------";
        msg+= "\n";
        msg+= context.getString(R.string.total);
        msg+= totaux(String.valueOf(operation.getMontant())) ;
        msg+= "\n";
        msg+= context.getString(R.string.print_remise);
        msg+= totaux(String.valueOf(operation.getRemise())) ;
        msg+= "\n";
        msg+= context.getString(R.string.print_net_a_payer);
        msg+= totaux(String.valueOf(operation.getMontant()-operation.getRemise())) ;
        msg+= "\n";
        msg += "--------------------------------";
        msg+= "\n";
        msg+= context.getString(R.string.print_recu);
        msg+= totaux(String.valueOf(operation.getRecu()))  ;
        msg+= "\n";
        msg+= context.getString(R.string.print_rendu);
        msg+= totaux(String.valueOf(operation.getRecu()-operation.getMontant()+operation.getRemise()))  ;
        msg+= "\n";
        msg += "--------------------------------";
        msg += "\n";
        msg += msgFin;
        msg += "\n";
        msg += "################################";
        msg += "\n";
        msg += context.getString(R.string.copyright);
        msg += "\n";
        msg += "\n";
        msg += "\n";
        msg += "\n";

        new Thread(
                new Runnable(){

                    @Override
                    public void run() {
                        try {
                            printTicket(msg);
                        } catch (Exception e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                    }
                }).start();
    }

    public String name(String name){
        int n = 11 ;
        int siz = 0 ;
        Log.e("NAME1",name) ;
        name = (name.length()>9)?(name.substring(0,8)+".."):(name) ;
        siz = n-name.length() ;
        for (int i =0 ; i < siz; ++i){
            //name += " " ;
        }
        Log.e("NAME2",name) ;
        return name ;
    }


    public String depenselibelle(String name){
        int n = 17 ;
        int siz = 0 ;
        Log.e("NAME1",name) ;
        name = (name.length()>17)?(name.substring(0,16)):(name) ;
        siz = n-name.length() ;
        for (int i =0 ; i < siz; ++i){
            name += " " ;
        }
        Log.e("NAME2",name) ;
        return name ;
    }

    public String prix(String prix){
        int n = 6 ;
        int siz = 0 ;
        prix = (prix.length()>6)?(prix.substring(0,5)):(prix) ;
        siz = n-prix.length() ;
        for (int i =0 ; i < siz; ++i){
            prix = " " + prix ;
        }
        return prix ;
    }

    public String montant(String prix){
        int n = 6 ;
        int siz = 0 ;
        prix = (prix.length()>6)?(prix.substring(0,5)):(prix) ;
        siz = n-prix.length() ;
        for (int i =0 ; i < siz; ++i){
            prix = " " + prix ;
        }
        return prix ;
    }

    public String quantite(String prix){
        int n = 5 ;
        int siz = 0 ;
        prix = (prix.length()>5)?(prix.substring(0,4)):(prix) ;
        siz = n-prix.length() ;
        for (int i =0 ; i < siz; ++i){
            prix = " " + prix ;
        }
        return prix ;
    }

    public String totaux(String totaux){
        int n = 18 ;
        int siz = 0 ;
        totaux = (totaux.length()>18)?(totaux.substring(0,17)):(totaux) ;
        siz = n-totaux.length() ;
        for (int i =0 ; i < siz; ++i){
            totaux = " " + totaux ;
        }
        return totaux ;
    }

    public void printTicket(String msg) {

        try {
            printerClass.printText(msg);
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }


    private Context getApplicationContext(){
        return this.context;
    }

    private void initPrintPDA(){
        printerClass = new PrinterClassSerialPort(getHandler());
        //printerClass.open(this);
        if(!printerClass.IsOpen())
            printerClass.open(context);

    }

    private Handler getHandler(){
        Handler mhandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case PrinterClass.MESSAGE_READ:
                        byte[] readBuf = (byte[]) msg.obj;
                        Log.i(TAG, "readBuf:" + readBuf[0]);
                        if (readBuf[0] == 0x13) {
                            //PrintService.isFUll = true;
                            //ShowMsg(getResources().getString(R.string.str_printer_state)+":"+getResources().getString(R.string.str_printer_bufferfull));
                        } else if (readBuf[0] == 0x11) {
                            //PrintService.isFUll = false;
                            //ShowMsg(getResources().getString(R.string.str_printer_state)+":"+getResources().getString(R.string.str_printer_buffernull));
                        } else if (readBuf[0] == 0x08) {
                            //	ShowMsg(getResources().getString(R.string.str_printer_state)+":"+getResources().getString(R.string.str_printer_nopaper));
                            ShowMsg("No Paper !!");
                        } else if (readBuf[0] == 0x01) {
                            //ShowMsg(getResources().getString(R.string.str_printer_state)+":"+getResources().getString(R.string.str_printer_printing));
                        }  else if (readBuf[0] == 0x04) {
                            //ShowMsg(getResources().getString(R.string.str_printer_state)+":"+getResources().getString(R.string.str_printer_hightemperature));
                            ShowMsg("High Temperature !!");
                        } else if (readBuf[0] == 0x02) {
                            //ShowMsg(getResources().getString(R.string.str_printer_state)+":"+getResources().getString(R.string.str_printer_lowpower));
                            ShowMsg("Low Power !!");

                        } else {
                            String readMessage = new String(readBuf, 0, msg.arg1);
                            if (readMessage.contains("800"))// 80mm paper
                            {
                                PrintService.imageWidth = 72;
                                Toast.makeText(getApplicationContext(), "80mm",
                                        Toast.LENGTH_SHORT).show();
                            } else if (readMessage.contains("580"))// 58mm paper
                            {
                                PrintService.imageWidth = 48;
                                Toast.makeText(getApplicationContext(), "58mm",
                                        Toast.LENGTH_SHORT).show();
                            } else {

                            }
                        }
                        break;
                    case PrinterClass.MESSAGE_STATE_CHANGE:// 6��l��״
                        switch (msg.arg1) {
                            case PrinterClass.STATE_CONNECTED:// �Ѿ�l��
                                break;
                            case PrinterClass.STATE_CONNECTING:// ����l��
                                Toast.makeText(getApplicationContext(),
                                        "STATE_CONNECTING", Toast.LENGTH_SHORT).show();
                                break;
                            case PrinterClass.STATE_LISTEN:
                            case PrinterClass.STATE_NONE:
                                break;
                            case PrinterClass.SUCCESS_CONNECT:
                                printerClass.write(new byte[] { 0x1b, 0x2b });// ����ӡ���ͺ�
                                Toast.makeText(getApplicationContext(),
                                        "SUCCESS_CONNECT", Toast.LENGTH_SHORT).show();
                                break;
                            case PrinterClass.FAILED_CONNECT:
                                Toast.makeText(getApplicationContext(),
                                        "FAILED_CONNECT", Toast.LENGTH_SHORT).show();

                                break;
                            case PrinterClass.LOSE_CONNECT:
                                Toast.makeText(getApplicationContext(), "LOSE_CONNECT",
                                        Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case PrinterClass.MESSAGE_WRITE:

                        break;
                }
                super.handleMessage(msg);
            }
        };
        return mhandler;
    }

    static byte[] string2Unicode(String s) {
        try {
            byte[] bytes = s.getBytes("unicode");
            byte[] bt = new byte[bytes.length - 2];
            for (int i = 2, j = 0; i < bytes.length - 1; i += 2, j += 2) {
                bt[j] = (byte) (bytes[i + 1] & 0xff);
                bt[j + 1] = (byte) (bytes[i] & 0xff);
            }
            return bt;
        } catch (Exception e) {
            try {
                byte[] bt = s.getBytes("GBK");
                return bt;
            } catch (UnsupportedEncodingException e1) {
                Log.e(TAG, e.getMessage());
                return null;
            }
        }
    }



    private void ShowMsg(String msg){
        Toast.makeText(this.context, msg,
                Toast.LENGTH_SHORT).show();
    }

}
