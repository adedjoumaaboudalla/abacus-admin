package admin.pv.projects.mediasoft.com.abacus_admin.utile;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import admin.pv.projects.mediasoft.com.abacus_admin.R;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.BalanceDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.BalanceLigneDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CaisseDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.CompteBanqueDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.DAOBase;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.LigneDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.MouvementDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.OperationDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.PartenaireDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.PointVenteDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.dao.ProduitDAO;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Caisse;
import admin.pv.projects.mediasoft.com.abacus_admin.model.CompteBanque;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Mouvement;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Operation;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Partenaire;

import admin.pv.projects.mediasoft.com.abacus_admin.model.PointVente;
import admin.pv.projects.mediasoft.com.abacus_admin.model.Produit;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import static admin.pv.projects.mediasoft.com.abacus_admin.R.id.table;

/**
 * Created by mediasoft on 11/11/2016.
 */
public class EtatsUtils {


    public static final String PV_PRODUIT_IMAGE_DIR = "PV_PRODUIT";
    public static final SimpleDateFormat formatter = new SimpleDateFormat("EEE dd MM yyyy");
    public static final SimpleDateFormat mysql_format = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat french_format = new SimpleDateFormat("dd-MM-yyyy");
    private static final String PV = "ABACUS_ETATS";
    //public static String serverIp = "http://1 92.168.0.104/" ;
    public static String serverIp = "http://192.168.43.78/";
    private static String name = "ABACUS";
    private static ArrayList<Operation> operations;


    public static void createandDisplayPdfEtat(int i, String name, Context context, String datedebut, String datefin,long pv) {
        switch (i){
            case 0: resultatExploitationPDF(context,name,datedebut,datefin,pv) ;break;
            case 1: chiffreAffairePDF(context,name,datedebut,datefin,pv) ;break;
            case 2:
                try {
                    Utiles.createandDisplayOperationPdf(new OperationDAO(context).getAll(0,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv),name, context,false,pv);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ;break;
            case 3: listeAchatPDF(context,name,datedebut,datefin,pv) ;break;
            case 4: ficheDeStockPDF(context,name,datedebut,datefin); ;break;
            case 5: ficheDeStockRegroupePDF(context,name,datedebut,datefin,pv);break;
            case 6: bilanPDF(context,name,datedebut,datefin, pv);break;
            case 7: balancePDF(context,name,datedebut,datefin,pv);break;
            case 8: listePartenairePDF(context,name,datedebut,datefin);break;
            case 9: tresoreriePDF(context,name,datedebut,datefin);break;
        }
    }

    public static void createandDisplayExcelEtat(int i,String name, Context context, String datedebut, String datefin, long pv) {
        switch (i){
            case 0: resultatExploitationEXCEL(context,name,datedebut,datefin,pv) ;break;
            case 1: chiffreAffaireExcel(context,name,datedebut,datefin,pv); break;
            case 2:
                try {
                    Utiles.createandDisplayOperationExcel(new OperationDAO(context).getAll(0,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv),name, context,false);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ;break;
            case 3: listeAchatExcel(context,name,datedebut,datefin,pv); break;
            case 4: ficheDeStockExcel(context,name,datedebut,datefin); break;
            case 5: ficheDeStockRegroupeExcel(context,name,datedebut,datefin,pv); break;
            case 6: bilanEXCEL(context,name,datedebut,datefin); break;
            case 7: balanceExcel(context,name,datedebut,datefin,pv);break;
            case 8: listePartenaireExcel(context,name,datedebut,datefin); break;
        }
    }




    public static void ficheDeStockExcel(Context context, String name, String datedebut, String datefin) {

        Document doc = new Document();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ets = preferences.getString("nomSociete", null);

        try {
            String path = null ;
            if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
            path = Environment.getExternalStorageDirectory().getPath() + "/" + PV + "/" ;
        }
        else if (!preferences.getBoolean("stockage",true)){
            path = context.getFilesDir().getAbsolutePath()  + "/" + PV ;
        }
        else{
            Toast.makeText(context,R.string.sdabsent,Toast.LENGTH_LONG).show();
            return;
        }


            Log.e("DATA",path) ;
            File dir = new File(path);
            if (!dir.exists())  {
                try {
                    dir.mkdirs();
                    Log.e("CRETAED",path) ;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            name = name + "_" + formatter.format(new Date()).replace(' ', '_') + ".xlsx";

            //file path
            File file = new File(path, name);

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;



            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("Fiche de stock", 0);
            int ligne = 0 ;
            sheet.addCell(new Label(0, ligne, "Date"));
            sheet.addCell(new Label(1, ligne, "No"));
            sheet.addCell(new Label(2, ligne, "Libelle"));
            sheet.addCell(new Label(3, ligne, "SI"));
            sheet.addCell(new Label(4, ligne, "Entrée"));
            sheet.addCell(new Label(5, ligne, "Sortie"));
            sheet.addCell(new Label(6, ligne, "SF"));
            ligne++ ;


            //just some random data to fill
            float entree = 0 ;
            float sortie = 0 ;
            OperationDAO operationDAO = new OperationDAO(context) ;
            MouvementDAO mouvementDAO = new MouvementDAO(context) ;
            ProduitDAO produitDAO = new ProduitDAO(context) ;

            ArrayList<Mouvement> mouvements = null;

            double valeur = 0 ;
            double total = 0 ;
            double quantite = 0 ;

            try {
                mouvements = mouvementDAO.getManyByInterval(DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin)) ;
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (mouvements != null){
                valeur = 0 ;
                quantite = 0 ;
                for (int j = 0; j < mouvements.size(); j++) {
                    Mouvement mouvement = mouvements.get(j) ;
                    Operation operation = operationDAO.getOne(mouvement.getOperation_id());;
                    if (operation==null || (operation.getAnnuler()==1 && operation.getDateannulation().before(new Date())) || operation.getTypeOperation_id().startsWith(OperationDAO.CMD)) continue;


                    sheet.addCell(new Label(0, ligne, french_format.format(operation.getCreated_at())));
                    sheet.addCell(new Label(1, ligne, String.valueOf(j+1)));

                    if (produitDAO.getOne(mouvement.getProduit_id())!=null)sheet.addCell(new Label(2, ligne, produitDAO.getOne(mouvement.getProduit_id()).getLibelle()));
                    else sheet.addCell(new Label(2, ligne, mouvement.getProduit()));

                    if (mouvement.getEntree()==1)
                        sheet.addCell(new Label(3, ligne, String.valueOf(mouvement.getRestant()+mouvement.getQuantite())));
                    else
                        sheet.addCell(new Label(3, ligne, String.valueOf(mouvement.getRestant()-mouvement.getQuantite()) ));

                    if (mouvement.getEntree()==1) {
                        sheet.addCell(new Label(4, ligne,String.valueOf(0) ));
                        sheet.addCell(new Label(5, ligne, String.valueOf(mouvement.getQuantite())));
                    }
                    else {
                        sheet.addCell(new Label(5, ligne,String.valueOf(0) ));
                        sheet.addCell(new Label(4, ligne, String.valueOf(mouvement.getQuantite())));
                    }


                    sheet.addCell(new Label(6, ligne,  String.valueOf(mouvement.getRestant())));
                    ligne++ ;
                }

            }

            workbook.write();

            Toast.makeText(context, "Sauvegarder en " + path, Toast.LENGTH_LONG).show();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        } finally {
            doc.close();
        }

        viewEXCEL(name, PV, context);
    }








    public static void ficheDeStockPDF(Context context, String name, String datedebut, String datefin) {

        Document doc = new Document();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ets = preferences.getString("nomSociete", null);
        String path = null ;
        try {
            if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
                path = Environment.getExternalStorageDirectory().getPath() + "/" + PV;
            }
            else if (!preferences.getBoolean("stockage",true)){
                path = context.getFilesDir().getAbsolutePath() + "/" + PV;
            }
            else{
                Toast.makeText(context,R.string.sdabsent,Toast.LENGTH_LONG).show();
                return;
            }

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            name = name + "_" + french_format.format(new Date()).replace(' ', '_') + ".pdf";
            name = name.replace('-', '_') ;
            File file = new File(dir, name);
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter writer = PdfWriter.getInstance(doc, fOut);
            writer.setPageEvent(new HeaderFooterPageEvent(context));

            //special font sizes
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);

            //document header attributes
            doc.addAuthor(context.getResources().getString(R.string.app_name));
            doc.addCreationDate();
            doc.addProducer();
            //doc.addCreator("MySampleCode.com");
            doc.addTitle(context.getString(R.string.resultat));
            doc.setPageSize(PageSize.LETTER);
            //specify column widths

            //open the document
            doc.open();

            PointVenteDAO pointVenteDAO = new PointVenteDAO(context) ;
            PointVente pointVente = pointVenteDAO.getLast() ;

            Paragraph paragraph = new Paragraph(pointVente.getLibelle());;
            paragraph.setAlignment(Element.ALIGN_LEFT);
            doc.add(paragraph);
            paragraph = new Paragraph(context.getString(R.string.teleph)  + pointVente.getTel()) ;
            paragraph.setAlignment(Element.ALIGN_LEFT);
            doc.add(paragraph);
            paragraph = new Paragraph(context.getString(R.string.adress)  + pointVente.getQuartier() + " , " +  pointVente.getVille()) ;
            paragraph.setAlignment(Element.ALIGN_LEFT);
            doc.add(paragraph);
            doc.add(new Paragraph(" "));

            // Largeur des colonnes
            float[] columnWidths = {3f, 2f, 5f, 2f, 2f, 2f, 2f};
            //create PDF table with the given widths
            PdfPTable table = new PdfPTable(columnWidths);
            // set table width a percentage of the page width
            table.setWidthPercentage(90f);


            //create a paragraph
            paragraph = null;
            paragraph = new Paragraph("FICHE DE STOCK SUR " + datedebut + " - " + datefin);

            /*
            if (ets == null)
            else
            //    paragraph = new Paragraph("Commande DE " + ets + " DU " + formatter.format(operations.get(0).getDateoperation()) + " AU " + formatter.format(operations.get(operations.size() - 1).getDateoperation()));

            */
            paragraph.setAlignment(Element.ALIGN_CENTER);

            //insert column headings
            insertCell(table, "Date", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "No", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Libelle", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, "SI", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Entrée", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Sortie", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "SF", Element.ALIGN_CENTER, 1, bfBold12);
            table.setHeaderRows(1);


            //insert an empty row
            //create section heading by cell merging
            //insertCell(table, "New York Orders ...", Element.ALIGN_LEFT, 4, bfBold12);
            //double orderTotal, total = 0;

            //just some random data to fill
            float entree = 0 ;
            float sortie = 0 ;
            OperationDAO operationDAO = new OperationDAO(context) ;
            MouvementDAO mouvementDAO = new MouvementDAO(context) ;
            ProduitDAO produitDAO = new ProduitDAO(context) ;


            ArrayList<Mouvement> mouvements = null;

            double valeur = 0 ;
            double total = 0 ;
            double quantite = 0 ;

            try {
                mouvements = mouvementDAO.getManyByInterval(DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin)) ;
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (mouvements != null){
                valeur = 0 ;
                quantite = 0 ;
                for (int j = 0; j < mouvements.size(); j++) {
                    Mouvement mouvement = mouvements.get(j) ;
                    Operation operation = operationDAO.getOne(mouvement.getOperation_id());
                    if (operation==null || (operation.getAnnuler()==1 && operation.getDateannulation().before(new Date())) || operation.getTypeOperation_id().startsWith(OperationDAO.CMD)) continue;


                    insertCell(table, french_format.format(operation.getCreated_at()), Element.ALIGN_CENTER, 1, bf12);
                    insertCell(table, String.valueOf(j+1), Element.ALIGN_CENTER, 1, bf12);
                    if (produitDAO.getOne(mouvement.getProduit_id())!=null) insertCell(table,produitDAO.getOne(mouvement.getProduit_id()).getLibelle(), Element.ALIGN_LEFT, 1, bf12);
                    else insertCell(table,mouvement.getProduit(), Element.ALIGN_LEFT, 1, bf12);

                    if (mouvement.getEntree()==1) insertCell(table, String.valueOf(mouvement.getRestant()+mouvement.getQuantite()), Element.ALIGN_CENTER, 1, bf12);
                    else insertCell(table, String.valueOf(mouvement.getRestant()-mouvement.getQuantite()), Element.ALIGN_CENTER, 1, bf12);

                    if (mouvement.getEntree()==1) {
                        insertCell(table, String.valueOf(0), Element.ALIGN_CENTER, 1, bf12);
                        insertCell(table, String.valueOf(mouvement.getQuantite()), Element.ALIGN_CENTER, 1, bf12);
                    }
                    else {
                        insertCell(table, String.valueOf(mouvement.getQuantite()), Element.ALIGN_CENTER, 1, bf12);
                        insertCell(table, String.valueOf(0), Element.ALIGN_CENTER, 1, bf12);
                    }

                    Log.e(mouvement.getProduit(), mouvement.getQuantite() + " | " + mouvement.getRestant()) ;

                    insertCell(table, String.valueOf(mouvement.getRestant()), Element.ALIGN_CENTER, 1, bf12);
                }

            }

            //add the PDF table to the paragraph
            paragraph.add(table);
            // add the paragraph to the document
            doc.add(paragraph);


            Toast.makeText(context, "Sauvegarder en " + path, Toast.LENGTH_LONG).show();

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }

        viewPdf(name, PV, context);
    }





    public static void balancePDF(Context context, String name, String datedebut, String datefin, long pv) {

        Document doc = new Document();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ets = preferences.getString("nomSociete", null);

        try {

            String path = null ;
            if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
                path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + PV;
            }
            else if (!preferences.getBoolean("stockage",true)){
                path = context.getFilesDir().getAbsolutePath() + "/" + PV;
            }
            else{
                Toast.makeText(context,R.string.sdabsent,Toast.LENGTH_LONG).show();
                return;
            }


            File dir = new File(path);
            if (!dir.exists())    dir.mkdirs();

            name = name + "_" + french_format.format(new Date()).replace(' ', '_') + ".pdf";
            name = name.replace('-', '_') ;
            File file = new File(dir, name);
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter writer = PdfWriter.getInstance(doc, fOut);
            writer.setPageEvent(new HeaderFooterPageEvent(context));

            //special font sizes
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);

            //document header attributes
            doc.addAuthor(context.getResources().getString(R.string.app_name));
            doc.addCreationDate();
            doc.addProducer();
            //doc.addCreator("MySampleCode.com");
            doc.addTitle(context.getString(R.string.resultat));
            doc.setPageSize(PageSize.LETTER);
            //specify column widths

            //open the document
            doc.open();


            PointVenteDAO pointVenteDAO = new PointVenteDAO(context) ;
            PointVente pointVente = pointVenteDAO.getLast() ;

            Paragraph paragraph = new Paragraph(pointVente.getLibelle()) ;
            paragraph.setAlignment(Element.ALIGN_LEFT);
            doc.add(paragraph);
            doc.add(new Paragraph(" "));

            // Largeur des colonnes
            float[] columnWidths = {5f,3f,3f,3f,3f,3f,3f};
            //create PDF table with the given widths
            PdfPTable table = new PdfPTable(columnWidths);
            // set table width a percentage of the page width
            table.setWidthPercentage(90f);


            //create a paragraph
            paragraph = null;
            paragraph = new Paragraph("BALANCE " + datedebut + " - " + datefin);
            EtatsFonctions etatsFonctions = new EtatsFonctions(context, datedebut, datefin) ;

            /*
            if (ets == null)
            else
            //    paragraph = new Paragraph("Commande DE " + ets + " DU " + formatter.format(operations.get(0).getDateoperation()) + " AU " + formatter.format(operations.get(operations.size() - 1).getDateoperation()));

            */
            paragraph.setAlignment(Element.ALIGN_CENTER);

            //insert column headings
            insertCell(table, "Libelle", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, "Solde report", Element.ALIGN_CENTER, 2, bfBold12);
            insertCell(table, "Mouvement periode", Element.ALIGN_CENTER, 2, bfBold12);
            insertCell(table, "Solde cloture", Element.ALIGN_CENTER, 2, bfBold12);


            insertCell(table, "", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, "Débit", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, "Crédit", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, "Débit", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, "Crédit", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, "Débit", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, "Crédit", Element.ALIGN_LEFT, 1, bfBold12);

            table.setHeaderRows(2);

            double debit = 0 ;
            double credit = 0 ;
            double r_debit = 0 ;
            double r_credit = 0 ;
            double s_debit = 0 ;
            double s_credit = 0 ;

            double tdebit = 0 ;
            double tcredit = 0 ;
            double tr_debit = 0 ;
            double tr_credit = 0 ;
            double ts_debit = 0 ;
            double ts_credit = 0 ;

            OperationDAO operationDAO = new OperationDAO(context) ;
            MouvementDAO mouvementDAO = new MouvementDAO(context) ;
            ProduitDAO produitDAO = new ProduitDAO(context) ;
            LigneDAO ligneDAO = new LigneDAO(context) ;
            BalanceLigneDAO balanceLigneDAO = new BalanceLigneDAO(context) ;
            double valeur = 0 ;
            double r_valeur = 0 ;

            // Liste des charges immobilisées
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getChargeImmo(pv, null, null) ;
            r_debit = etatsFonctions.getReportChargeImmo(pv) ;


            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.chargeimmo), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }


            // Liste des immobilisations corporelle
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getImmoCorp(pv,null,null) ;
            r_debit = etatsFonctions.getReportImmoCorp(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;

            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.immocorp), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }





            // Liste des immobilisations incorcorporelle
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getImmoIncorp(pv,null,null) ;
            r_debit = etatsFonctions.getReportImmoInCorp(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.immobilisationincorporelle), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }




            // Liste des immobilisations financieres
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getImmoFinanciere(pv,null,null) ;
            r_debit = etatsFonctions.getReportImmoFin(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }
            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.immofin), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }





            // Ammortissement corporel
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            credit = etatsFonctions.getAmmortCorp(pv,null,null) ;
            r_credit = etatsFonctions.getReportAmmoCorp(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }
            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.ammocorp), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }







            // Ammortissement incorporel
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            credit = etatsFonctions.getAmmortInCorp(pv,null,null) ;
            r_credit = etatsFonctions.getReportAmmoInCorp(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.ammoincorp), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }






            // Achat
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getAchat(pv,null,null) ;
            r_debit = etatsFonctions.getReportAchat(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.achatmse), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }







             //Fournisseur avances versées

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getFournisseurAvanceVersees(pv) ;
            r_debit = etatsFonctions.getReportFournAvanceVerse(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.fornisseuravance), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }


            // Clients

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getVenteCredit(pv,null,null) ;
            r_debit = etatsFonctions.getReportVenteCredit(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.client), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }








            // Autre creance
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getAutreCreance(pv,null,null) ;
            r_debit = etatsFonctions.getReportAutreCreance(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.autrescreance), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }








            // Caisse
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getCaisseDebit(pv,null,null) ;
            r_debit = etatsFonctions.getReportCaisse(pv) ;
            credit = etatsFonctions.getCaisseCredit(pv,null,null) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.caisse), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }




            // Banque ou SFD
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getBanqueOuSfdDebit(pv,null,null) ;
            r_debit = etatsFonctions.getReportBQSFD(pv) ;
            credit = etatsFonctions.getBanqueOuSfdCredit(pv,null,null) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }


            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.banquesfd), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }




            // Dépot de garantie
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getDepotGarantie(pv,null,null) ;
            r_debit = etatsFonctions.getReportDepotGarantie(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.depotgarantie), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }






            // Dépot à terme
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getDepotTerme(pv,null,null) ;
            r_debit = etatsFonctions.getReportDepotAterme(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.depotaterme), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }






            // Autre placement
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getDepotAutre(pv,null,null) ;
            r_debit = etatsFonctions.getReportAutrePlacement(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.autreplacement), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }




            // Capital ou avoir personnel
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            credit = etatsFonctions.getAvoirPerso(pv) ;
            r_credit = etatsFonctions.getReportCapital(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.capitalouavoir), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }





            // Reserve
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            r_credit = etatsFonctions.getReportReserve(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.reserve), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }



            // Report nouveau
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            r_credit = etatsFonctions.getReportNouveau(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.reportnouveau), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }


            // Résultat de l'exercice
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            r_credit = etatsFonctions.getReportResultat(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.resultatexercice), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }






            // Emprunt
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            credit = etatsFonctions.getEmprunt(pv,null,null) ;
            r_credit = etatsFonctions.getReportEmprunt(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }
            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.emprunt), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }



            /*

             Client avance recu
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            credit = etatsFonctions.getClientAvanceVersées(pv) ;
            r_credit = etatsFonctions.getReportAvanceVerse(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }
            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.clientavancerecu), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }

*/


            // Achat  credit
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            credit = etatsFonctions.getAchatCredit(pv,null,null) ;
            r_credit = etatsFonctions.getReportAchatCredit(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }
            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.achatcredit), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }



            // Autre dettes
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            credit = etatsFonctions.getAutreDette(pv,null,null) ;
            r_credit = etatsFonctions.getReportAutreDette(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }
            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.autredette), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }



            // Variation de stock
            /*debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getVariationStock(pv) ;
            r_debit = etatsFonctions.getReportVariationStock(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }
            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.variationdestock), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }
            */




            // transport
            try {
                operations = operationDAO.getAll(2,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),0);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;
            valeur = 0 ;

            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.TRANSPORT))valeur += operations.get(i).getMontant() ;
            }

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = valeur ;
            r_debit = etatsFonctions.getReportTransport(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }
            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){

                insertCell(table, context.getString(R.string.tranport), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.TEL))valeur += operations.get(i).getMontant() ;
            }

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = valeur ;
            r_debit = etatsFonctions.getReportTransport(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                // Telephone
                insertCell(table, context.getString(R.string.tele), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }


            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.LOYER))valeur += operations.get(i).getMontant() ;
            }

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;
            debit = valeur ;

            r_debit = etatsFonctions.getReportLoyer(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                // loyer
                insertCell(table, context.getString(R.string.loyer), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }


            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.EAU))valeur += operations.get(i).getMontant() ;
            }

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = valeur ;
            r_debit = etatsFonctions.getReportEau(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                // Eau
                insertCell(table, context.getString(R.string.eau), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }


            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.ELECTRICITE))valeur += operations.get(i).getMontant() ;
            }

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = valeur ;
            r_debit = etatsFonctions.getReportElectricite(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                // Electricite
                insertCell(table, context.getString(R.string.electricite), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }

            double d = valeur ;
            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.PUB))valeur += operations.get(i).getMontant() ;
            }


            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;
            debit = valeur ;

            r_debit = etatsFonctions.getReportPub(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                // Publicite
                insertCell(table, context.getString(R.string.pub), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }

            d = valeur ;
            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.MISSION))valeur += operations.get(i).getMontant() ;
            }

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = valeur ;
            r_debit = etatsFonctions.getReportMission(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                // Mission et reception
                insertCell(table, context.getString(R.string.mission), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }


            d = valeur ;
            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.ENTRETIEN_REPARATION))valeur += operations.get(i).getMontant() ;
            }

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = valeur ;
            r_debit = etatsFonctions.getReportEntretient(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                // Entretient et reparation
                insertCell(table, context.getString(R.string.entretient), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }


            d = valeur ;
            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.FRAIS_PERSO))valeur += operations.get(i).getMontant() ;
            }

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = valeur ;
            r_debit = etatsFonctions.getReportFraisPersonnel(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                // Frais personnel
                insertCell(table, context.getString(R.string.fraispersonnel), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            d = valeur ;
            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.DOTATION_AMOR))valeur += operations.get(i).getMontant() ;
            }

            debit = valeur ;
            r_debit = etatsFonctions.getReportDotationAmort(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                // Dotation ammortissement
                insertCell(table, context.getString(R.string.dotationamor), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }



            d = valeur ;
            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.AUTRES_DEP))valeur += operations.get(i).getMontant() ;
            }


            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;
            debit = valeur ;
            r_debit = etatsFonctions.getReportAutreDep(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                // Autre depense
                insertCell(table, context.getString(R.string.autredepense), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }


            d = valeur ;
            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.IMPOT))valeur += operations.get(i).getMontant() ;
            }

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = valeur ;
            r_debit = etatsFonctions.getReportImpotTaxe(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                // Impot et taxe
                insertCell(table, context.getString(R.string.impot), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }


            d = valeur ;

            // Charge financiere

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            d = valeur ;
            debit = etatsFonctions.getChargeFin(pv,null,null) ;
            r_debit = etatsFonctions.getReportChargeFin(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){

                insertCell(table, context.getString(R.string.chargefin), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }


            // Charge exceptionnelle

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getChargeExcep(pv,null,null) ;
            r_debit = etatsFonctions.getReportChargeExcep(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){

                insertCell(table, context.getString(R.string.chargeexp), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }



            // Vente

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            credit = etatsFonctions.getVente(pv,null,null) ;
            r_credit = etatsFonctions.getReportVente(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){

                insertCell(table, context.getString(R.string.vente), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }



            // Produit financiere

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            credit = etatsFonctions.getProduitFinancier(pv,null,null) ;
            r_credit = etatsFonctions.getReportProdFin(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.prdtfin), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }


            // Produit Except

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            credit = etatsFonctions.getProduitExcep(pv,null,null) ;
            r_credit = etatsFonctions.getReportProdExcept(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.prdtexcept), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }




            insertCell(table, context.getString(R.string.total), Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(tr_debit), Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(tr_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(tdebit), Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(tcredit), Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(ts_debit), Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(ts_credit), Element.ALIGN_RIGHT, 1, bfBold12);


            //add the PDF table to the paragraph
            paragraph.add(table);
            // add the paragraph to the document
            doc.add(paragraph);


            Toast.makeText(context, "Sauvegarder en " + path, Toast.LENGTH_LONG).show();

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }

        viewPdf(name, PV, context);
    }

    public static void balanceExcel(Context context, String name, String datedebut, String datefin, long pv) {


        PointVenteDAO pointVenteDAO = new PointVenteDAO(context) ;
        PointVente pointVente = pointVenteDAO.getLast() ;

        try {


            String path = null ;
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
                path = Environment.getExternalStorageDirectory().getPath() + "/" + PV;
            }
            else if (!preferences.getBoolean("stockage",true)){
                path = context.getFilesDir().getAbsolutePath() + "/" + PV;
            }
            else{
                Toast.makeText(context,R.string.sdabsent,Toast.LENGTH_LONG).show();
                return;
            }

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();
            name = name + "_" + french_format.format(new Date()).replace(' ', '_') + ".xlsx";
            name = name.replace('-', '_') ;

            //file path
            File file = new File(path, name);

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;



            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("balance", 0);
            int ligne = 0 ;


            //insert column headings
            sheet.addCell(new Label(0, ligne, "Libelle"));
            sheet.addCell(new Label(1, ligne, "Solde report"));
            sheet.addCell(new Label(3, ligne, "Mouvement periode"));
            sheet.addCell(new Label(5, ligne, "Solde cloture"));
            ligne++ ;

            sheet.addCell(new Label(0, ligne, ""));
            sheet.addCell(new Label(1, ligne, "Debit"));
            sheet.addCell(new Label(2, ligne, "Crédit"));
            sheet.addCell(new Label(3, ligne, "Débit"));
            sheet.addCell(new Label(4, ligne, "Crédit"));
            sheet.addCell(new Label(5, ligne, "Débit"));
            sheet.addCell(new Label(6, ligne, "Crédit"));
            ligne++ ;

            double debit = 0 ;
            double credit = 0 ;
            double r_debit = 0 ;
            double r_credit = 0 ;
            double s_debit = 0 ;
            double s_credit = 0 ;

            double tdebit = 0 ;
            double tcredit = 0 ;
            double tr_debit = 0 ;
            double tr_credit = 0 ;
            double ts_debit = 0 ;
            double ts_credit = 0 ;

            OperationDAO operationDAO = new OperationDAO(context) ;
            MouvementDAO mouvementDAO = new MouvementDAO(context) ;
            ProduitDAO produitDAO = new ProduitDAO(context) ;
            LigneDAO ligneDAO = new LigneDAO(context) ;
            BalanceLigneDAO balanceLigneDAO = new BalanceLigneDAO(context) ;
            double valeur = 0 ;
            double r_valeur = 0 ;

            // Liste des charges immobilisées

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            EtatsFonctions etatsFonctions = new EtatsFonctions(context,datedebut,datefin) ;

            debit = etatsFonctions.getChargeImmo(pv,null,null) ;
            r_debit = etatsFonctions.getReportChargeImmo(pv) ;


            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.chargeimmo)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }


            // Liste des immobilisations corporelle
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getImmoCorp(pv,null,null) ;
            r_debit = etatsFonctions.getReportImmoCorp(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;

            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.immocorp)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }





            // Liste des immobilisations incorcorporelle
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getImmoIncorp(pv,null,null) ;
            r_debit = etatsFonctions.getReportImmoInCorp(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.immobilisationincorporelle)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }




            // Liste des immobilisations financieres
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getImmoFinanciere(pv,null,null) ;
            r_debit = etatsFonctions.getReportImmoFin(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }
            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.immofin)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }





            // Ammortissement corporel
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            credit = etatsFonctions.getAmmortCorp(pv,null,null) ;
            r_credit = etatsFonctions.getReportAmmoCorp(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }
            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.ammocorp)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }







            // Ammortissement incorporel
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            credit = etatsFonctions.getAmmortInCorp(pv,null,null) ;
            r_credit = etatsFonctions.getReportAmmoInCorp(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.ammoincorp)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }



            // Achat
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getAchat(pv,null,null) ;
            r_debit = etatsFonctions.getReportAchat(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.achatmse)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }






/*

            // Fournisseur avances versées

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getFournisseurAvanceVersees(pv) ;
            r_debit = etatsFonctions.getReportFournAvanceVerse(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.fornisseuravance)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }


*/



            // Clients

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getVenteCredit(pv,null,null) ;
            r_debit = etatsFonctions.getReportVenteCredit(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.client)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }








            // Autre creance
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getAutreCreance(pv, null, null) ;
            r_debit = etatsFonctions.getReportAutreCreance(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.autrescreance)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }








            // Caisse
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getCaisseDebit(pv,null,null) ;
            r_debit = etatsFonctions.getReportCaisse(pv) ;
            credit = etatsFonctions.getCaisseCredit(pv,null,null) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.caisse)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }




            // Banque ou SFD
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getBanqueOuSfdDebit(pv,null,null) ;
            r_debit = etatsFonctions.getReportBQSFD(pv) ;
            credit = etatsFonctions.getBanqueOuSfdCredit(pv,null,null) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }


            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.banquesfd)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }




            // Dépot de garantie
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getDepotGarantie(pv,null,null) ;
            r_debit = etatsFonctions.getReportDepotGarantie(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.depotgarantie)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }






            // Dépot à terme
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getDepotTerme(pv,null,null) ;
            r_debit = etatsFonctions.getReportDepotAterme(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.depotaterme)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }






            // Autre placement
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getDepotAutre(pv,null,null) ;
            r_debit = etatsFonctions.getReportAutrePlacement(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.autreplacement)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }




            // Capital ou avoir personnel
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            credit = etatsFonctions.getAvoirPerso(pv) ;
            r_credit = etatsFonctions.getReportCapital(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.capitalouavoir)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }





            // Reserve
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            r_credit = etatsFonctions.getReportReserve(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.reserve)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }



            // Report nouveau
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            r_credit = etatsFonctions.getReportNouveau(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.reportnouveau)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }


            // Résultat de l'exercice
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            r_credit = etatsFonctions.getReportResultat(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.resultatexercice)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }






            // Emprunt
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            credit = etatsFonctions.getEmprunt(pv,null,null) ;
            r_credit = etatsFonctions.getReportEmprunt(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }
            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.emprunt)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }



            /*
             Client avance recu
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            credit = etatsFonctions.getClientAvanceVersées(pv) ;
            r_credit = etatsFonctions.getReportAvanceVerse(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }
            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.clientavancerecu)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }
            */


            // Achat  credit
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            credit = etatsFonctions.getAchatCredit(pv,null,null) ;
            r_credit = etatsFonctions.getReportAchatCredit(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }
            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.achatcredit)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }



            // Autre dettes
            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            credit = etatsFonctions.getAutreDette(pv,null,null) ;
            r_credit = etatsFonctions.getReportAutreDette(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }
            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.autredette)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }



            // Variation de stock
            /*debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getVariationStock(pv) ;
            r_debit = etatsFonctions.getReportVariationStock(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }
            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                insertCell(table, context.getString(R.string.variationdestock), Element.ALIGN_LEFT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(r_credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(credit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_debit), Element.ALIGN_RIGHT, 1, bfBold12);
                insertCell(table, Utiles.formatMtn(s_credit), Element.ALIGN_RIGHT, 1, bfBold12);
            }
            */




            // transport
            try {
                operations = operationDAO.getAll(2,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),0);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;
            valeur = 0 ;

            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.TRANSPORT))valeur += operations.get(i).getMontant() ;
            }

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = valeur ;
            r_debit = etatsFonctions.getReportTransport(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }
            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.tranport)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.TEL))valeur += operations.get(i).getMontant() ;
            }

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = valeur ;
            r_debit = etatsFonctions.getReportTransport(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                // Telephone
                sheet.addCell(new Label(0, ligne, context.getString(R.string.tele)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }


            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.LOYER))valeur += operations.get(i).getMontant() ;
            }

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;
            debit = valeur ;

            r_debit = etatsFonctions.getReportLoyer(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                // loyer
                sheet.addCell(new Label(0, ligne, context.getString(R.string.loyer)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }


            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.EAU))valeur += operations.get(i).getMontant() ;
            }

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = valeur ;
            r_debit = etatsFonctions.getReportEau(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                // Eau
                sheet.addCell(new Label(0, ligne, context.getString(R.string.eau)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }


            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.ELECTRICITE))valeur += operations.get(i).getMontant() ;
            }

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = valeur ;
            r_debit = etatsFonctions.getReportElectricite(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                // Electricite
                sheet.addCell(new Label(0, ligne, context.getString(R.string.electricite)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }

            double d = valeur ;
            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.PUB))valeur += operations.get(i).getMontant() ;
            }


            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;
            debit = valeur ;

            r_debit = etatsFonctions.getReportPub(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                // Publicite
                sheet.addCell(new Label(0, ligne, context.getString(R.string.pub)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }

            d = valeur ;
            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.MISSION))valeur += operations.get(i).getMontant() ;
            }

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = valeur ;
            r_debit = etatsFonctions.getReportMission(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                // Mission et reception
                sheet.addCell(new Label(0, ligne, context.getString(R.string.mission)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }


            d = valeur ;
            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.ENTRETIEN_REPARATION))valeur += operations.get(i).getMontant() ;
            }

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = valeur ;
            r_debit = etatsFonctions.getReportEntretient(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                // Entretient et reparation
                sheet.addCell(new Label(0, ligne, context.getString(R.string.entretient)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }


            d = valeur ;
            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.FRAIS_PERSO))valeur += operations.get(i).getMontant() ;
            }

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = valeur ;
            r_debit = etatsFonctions.getReportFraisPersonnel(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                // Frais personnel
                sheet.addCell(new Label(0, ligne, context.getString(R.string.fraispersonnel)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            d = valeur ;
            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.DOTATION_AMOR))valeur += operations.get(i).getMontant() ;
            }

            debit = valeur ;
            r_debit = etatsFonctions.getReportDotationAmort(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                // Dotation ammortissement
                sheet.addCell(new Label(0, ligne, context.getString(R.string.dotationamor)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }



            d = valeur ;
            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.AUTRES_DEP))valeur += operations.get(i).getMontant() ;
            }


            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;
            debit = valeur ;
            r_debit = etatsFonctions.getReportAutreDep(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                // Autre depense
                sheet.addCell(new Label(0, ligne, context.getString(R.string.autredepense)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }


            d = valeur ;
            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.IMPOT))valeur += operations.get(i).getMontant() ;
            }

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = valeur ;
            r_debit = etatsFonctions.getReportImpotTaxe(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                // Impot et taxe
                sheet.addCell(new Label(0, ligne, context.getString(R.string.impot)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }


            d = valeur ;

            // Charge financiere

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            d = valeur ;
            debit = etatsFonctions.getChargeFin(pv,null,null) ;
            r_debit = etatsFonctions.getReportChargeFin(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.chargefin)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }


            // Charge exceptionnelle

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            debit = etatsFonctions.getChargeExcep(pv,null,null) ;
            r_debit = etatsFonctions.getReportChargeExcep(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.chargeexp)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }



            // Vente

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            credit = etatsFonctions.getVente(pv,null,null) ;
            r_credit = etatsFonctions.getReportVente(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.vente)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }



            // Produit financiere

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            credit = etatsFonctions.getProduitFinancier(pv,null,null) ;
            r_credit = etatsFonctions.getReportProdFin(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.prdtfin)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }


            // Produit Except

            debit = 0 ;
            credit = 0 ;
            r_debit = 0 ;
            r_credit = 0 ;
            s_debit = 0 ;
            s_credit = 0 ;

            credit = etatsFonctions.getProduitExcep(pv,null,null) ;
            r_credit = etatsFonctions.getReportProdExcept(pv) ;

            s_debit = debit + r_debit ;
            s_credit = credit + r_credit ;
            if (s_debit>s_credit) {
                s_debit = s_debit - s_credit ;
                s_credit = 0 ;
            }
            else {
                s_credit = s_credit - s_debit ;
                s_debit = 0 ;
            }

            tdebit += debit ;
            tcredit += credit ;
            tr_credit += r_credit ;
            tr_debit += r_debit ;
            ts_credit += s_credit ;
            ts_debit += s_debit ;

            if (s_debit != 0 || s_credit != 0){
                sheet.addCell(new Label(0, ligne, context.getString(R.string.prdtexcept)));
                sheet.addCell(new Label(1, ligne, Utiles.formatMtn(r_debit)));
                sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(r_credit)));
                sheet.addCell(new Label(3, ligne, Utiles.formatMtn(debit)));
                sheet.addCell(new Label(4, ligne, Utiles.formatMtn(credit)));
                sheet.addCell(new Label(5, ligne, Utiles.formatMtn(s_debit)));
                sheet.addCell(new Label(6, ligne, Utiles.formatMtn(s_credit)));
                ligne++ ;
            }


            sheet.addCell(new Label(0, ligne, context.getString(R.string.total)));
            sheet.addCell(new Label(1, ligne, Utiles.formatMtn(tr_debit)));
            sheet.addCell(new Label(2, ligne,  Utiles.formatMtn(tr_credit)));
            sheet.addCell(new Label(3, ligne, Utiles.formatMtn(tdebit)));
            sheet.addCell(new Label(4, ligne, Utiles.formatMtn(tcredit)));
            sheet.addCell(new Label(5, ligne, Utiles.formatMtn(ts_debit)));
            sheet.addCell(new Label(6, ligne, Utiles.formatMtn(ts_credit)));


            workbook.write();

            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }

            Toast.makeText(context, "Sauvegarder en " + path, Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }

        viewEXCEL(name, PV, context);
    }






    public static void listePartenaireExcel(Context context, String name, String datedebut, String datefin) {

        Document doc = new Document();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ets = preferences.getString("nomSociete", null);

        try {

            String path = null ;
            if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
                path = Environment.getExternalStorageDirectory().getPath() + "/" + PV;
            }
            else if (!preferences.getBoolean("stockage",true)){
                path = context.getFilesDir().getAbsolutePath() + "/" + PV;
            }
            else{
                Toast.makeText(context,R.string.sdabsent,Toast.LENGTH_LONG).show();
                return;
            }

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();
            name = name + "_" + french_format.format(new Date()).replace(' ', '_') + ".xlsx";
            name = name.replace('-', '_') ;

            //file path
            File file = new File(path, name);

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;



            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("Partenaire", 0);
            int ligne = 0 ;
            sheet.addCell(new Label(0, ligne, "Désignation"));
            sheet.addCell(new Label(1, ligne, "Genre"));
            sheet.addCell(new Label(2, ligne, "Contact"));
            sheet.addCell(new Label(3, ligne, "E-mail"));
            sheet.addCell(new Label(4, ligne, "Adresse"));
            ligne++ ;
            //insertCell(table, "New York Orders ...", Element.ALIGN_LEFT, 4, bfBold12);
            //double orderTotal, total = 0;

            //just some random data to fill
            float entree = 0 ;
            float sortie = 0 ;
            OperationDAO operationDAO = new OperationDAO(context) ;
            MouvementDAO mouvementDAO = new MouvementDAO(context) ;
            PartenaireDAO partenaireDAO = new PartenaireDAO(context) ;

            ArrayList<Partenaire> partenaires = partenaireDAO.getAll();

            ArrayList<Mouvement> mouvements = null;

            double valeur = 0 ;
            double total = 0 ;
            double quantite = 0 ;
            // Vente par produit
            Partenaire partenaire = null ;
            for (int i = 0; i < partenaires.size(); i++) {
                partenaire = partenaires.get(i) ;
                sheet.addCell(new Label(0, ligne, partenaire.getNom() + " " + partenaire.getPrenom() + " " + partenaire.getRaisonsocial()));
                sheet.addCell(new Label(1, ligne, partenaire.getSexe()));
                sheet.addCell(new Label(2, ligne, partenaire.getContact()));
                sheet.addCell(new Label(3, ligne, partenaire.getEmail()));
                sheet.addCell(new Label(4, ligne, partenaire.getAdresse()));
                ligne++ ;
            }


            workbook.write();

            Toast.makeText(context, "Sauvegarder en " + path, Toast.LENGTH_LONG).show();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        } finally {
            doc.close();
        }

        viewPdf(name, PV, context);
    }




    public static double resultatExploitationPDF(Context context, String name, String datedebut, String datefin, long pv) {

        Document doc = new Document();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ets = preferences.getString("nomSociete", null);

        try {

            String path = null ;
            if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
                path = Environment.getExternalStorageDirectory().getPath() + "/" + PV;
            }
            else if (!preferences.getBoolean("stockage",true)){
                path = context.getFilesDir().getAbsolutePath() + "/" + PV;
            }
            else{
                Toast.makeText(context,R.string.sdabsent,Toast.LENGTH_LONG).show();
                return 0;
            }

            File dir = new File(path);
            if (!dir.exists())    dir.mkdirs();

            name = name + "_" + french_format.format(new Date()).replace(' ', '_') + ".pdf";
            name = name.replace('-', '_') ;
            File file = new File(dir, name);
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter writer = PdfWriter.getInstance(doc, fOut);
            writer.setPageEvent(new HeaderFooterPageEvent(context));

            //special font sizes
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);

            //document header attributes
            doc.addAuthor(context.getResources().getString(R.string.app_name));
            doc.addCreationDate();
            doc.addProducer();
            //doc.addCreator("MySampleCode.com");
            doc.addTitle(context.getString(R.string.resultat));
            doc.setPageSize(PageSize.LETTER);
            //specify column widths

            //open the document
            doc.open();



            PointVenteDAO pointVenteDAO = new PointVenteDAO(context) ;
            PointVente pointVente = null ;
            Paragraph paragraph = null ;
            if (pv!=0){
                pointVente = pointVenteDAO.getOne(pv) ;
                paragraph = new Paragraph(pointVente.getLibelle()) ;
                paragraph.setAlignment(Element.ALIGN_LEFT);
                doc.add(paragraph);
                paragraph = new Paragraph(context.getString(R.string.teleph)  + pointVente.getTel()) ;
                paragraph.setAlignment(Element.ALIGN_LEFT);
                doc.add(paragraph);
                paragraph = new Paragraph(context.getString(R.string.adress)  + pointVente.getQuartier() + " , " +  pointVente.getVille()) ;
                paragraph.setAlignment(Element.ALIGN_LEFT);
                doc.add(paragraph);
            }



            doc.add(new Paragraph(" "));

            // Largeur des colonnes
            float[] columnWidths = {7f, 3f};
            //create PDF table with the given widths
            PdfPTable table = new PdfPTable(columnWidths);
            // set table width a percentage of the page width
            table.setWidthPercentage(90f);


            //create a paragraph
            paragraph = null;
            paragraph = new Paragraph("RESULTAT D'EXPLOITATION " + datedebut + " - " + datefin);

            /*
            if (ets == null)
            else
            //    paragraph = new Paragraph("Commande DE " + ets + " DU " + formatter.format(operations.get(0).getDateoperation()) + " AU " + formatter.format(operations.get(operations.size() - 1).getDateoperation()));

            */
            paragraph.setAlignment(Element.ALIGN_CENTER);

            //insert column headings
            insertCell(table, " ", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Valeur", Element.ALIGN_CENTER, 1, bfBold12);
            table.setHeaderRows(1);


            //insert an empty row
            insertCell(table, "", Element.ALIGN_LEFT,5, bfBold12);
            //create section heading by cell merging
            //insertCell(table, "New York Orders ...", Element.ALIGN_LEFT, 4, bfBold12);
            //double orderTotal, total = 0;

            //just some random data to fill
            float entree = 0 ;
            float sortie = 0 ;
            OperationDAO operationDAO = new OperationDAO(context) ;
            MouvementDAO mouvementDAO = new MouvementDAO(context) ;
            ProduitDAO produitDAO = new ProduitDAO(context) ;

            ArrayList<Operation> operations = null ;
            try {
                operations = operationDAO.getAll(1, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            double valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }

            double a = valeur ;
            // Chiffre d'affaire
            insertCell(table, context.getString(R.string.chiffreaffairee), Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bfBold12);

            /*
            ArrayList<Produit> produits = produitDAO.getAll();

            ArrayList<Mouvement> mouvements = null;

            CaisseDAO caisseDAO = new CaisseDAO(context);
            // Vente par produit
            for (int i = 0; i < produits.size(); i++) {
                try {
                    mouvements = mouvementDAO.getManyByProductInterval(produits.get(i).getId_externe(),DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin)) ;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (mouvements != null){
                    valeur = 0 ;
                    for (int j = 0; j < mouvements.size(); j++) {
                        Mouvement mouvement = mouvements.get(j);;
                        Operation operation = operationDAO.getOne(mouvement.getOperation_id());;
                        if (operation==null || operation.getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                        if (caisseDAO.getOne(operation.getCaisse_id()).getPointVente_id()!=pv) continue;
                        if (mouvements.get(j).getEntree()==1  && operationDAO.getOne(mouvements.get(j).getOperation_id()).getAttente()==0) valeur += mouvements.get(j).getPrixV()*mouvements.get(j).getQuantite() ;
                    }

                    if (valeur>0){
                        insertCell(table, context.getString(R.string.ventede)+ " " + produits.get(i).getLibelle(), Element.ALIGN_LEFT, 1, bf12);
                        insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);
                    }
                }
            }

            */

            // Achat et  autres depense
            try {
                operations = operationDAO.getAll(2,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }


            try {
                operations = operationDAO.getAll(5,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }


            double b = valeur ;
            insertCell(table, context.getString(R.string.achatdepense), Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bfBold12);


            // Achat

            try {
                operations = operationDAO.getAll(5,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }

            insertCell(table, context.getString(R.string.achatmse), Element.ALIGN_LEFT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);



            try {
                operations = operationDAO.getAll(2, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.TRANSPORT))valeur += operations.get(i).getMontant() ;
            }

            // transport
            insertCell(table, context.getString(R.string.tranport), Element.ALIGN_LEFT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.TEL))valeur += operations.get(i).getMontant() ;
            }

            // telephone
            insertCell(table, context.getString(R.string.tele), Element.ALIGN_LEFT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.LOYER))valeur += operations.get(i).getMontant() ;
            }

            // loyer
            insertCell(table, context.getString(R.string.loyer), Element.ALIGN_LEFT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.EAU))valeur += operations.get(i).getMontant() ;
            }

            // eau
            insertCell(table, context.getString(R.string.eau), Element.ALIGN_LEFT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.ELECTRICITE))valeur += operations.get(i).getMontant() ;
            }

            // electricité
            insertCell(table, context.getString(R.string.electricite), Element.ALIGN_LEFT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.FRAIS_PERSO))valeur += operations.get(i).getMontant() ;
            }

            // frais
            insertCell(table, context.getString(R.string.frais), Element.ALIGN_LEFT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.AUTRES_DEP))valeur += operations.get(i).getMontant() ;
            }

            // Autres dépense
            insertCell(table, context.getString(R.string.autre), Element.ALIGN_LEFT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);


            double c = a-b ;

            // Marche beneficiaire
            insertCell(table, context.getString(R.string.margebenef), Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(c), Element.ALIGN_RIGHT, 1, bfBold12);



            // Produit financiere
            try {
                operations = operationDAO.getAll(7,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }

            double d = valeur ;

            insertCell(table, context.getString(R.string.prdtfin), Element.ALIGN_LEFT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);

            // Charge financiere
            try {
                operations = operationDAO.getAll(8,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }

            d -= valeur ;

            insertCell(table, context.getString(R.string.chargefin), Element.ALIGN_LEFT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);

            insertCell(table, context.getString(R.string.margefin), Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(d), Element.ALIGN_RIGHT, 1, bfBold12);

            // Produit Except
            try {
                operations = operationDAO.getAll(9,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }


            double f = valeur ;
            insertCell(table, context.getString(R.string.prdtexcept), Element.ALIGN_LEFT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);

            // Charge Except
            try {
                operations = operationDAO.getAll(10,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }

            f -= valeur ;
            insertCell(table, context.getString(R.string.chargeexp), Element.ALIGN_LEFT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);

            insertCell(table, context.getString(R.string.margeexp), Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(f), Element.ALIGN_RIGHT, 1, bfBold12);




            // Impot et taxe
            try {
                operations = operationDAO.getAll(2,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.IMPOT))valeur += operations.get(i).getMontant() ;
            }

            insertCell(table, context.getString(R.string.impot), Element.ALIGN_LEFT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);

            double g = c+d+f-valeur ;

            insertCell(table, context.getString(R.string.benefnet), Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(g), Element.ALIGN_RIGHT, 1, bfBold12);

            if (name!=""){
                //add the PDF table to the paragraph
                paragraph.add(table);
                // add the paragraph to the document
                doc.add(paragraph);
                Toast.makeText(context, "Sauvegarder en " + path, Toast.LENGTH_LONG).show();
                viewPdf(name, PV, context);
            }

            return g ;
        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }

        return 0;
    }




    public static   double chiffreAffaire(Context context, long pv){
        MouvementDAO mouvementDAO = new MouvementDAO(context) ;
        OperationDAO operationDAO = new OperationDAO(context) ;
        ArrayList<Operation> operations = operationDAO.getAllByPv(pv);;

        double result = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
            if (operations.get(i).getEntree() == 1) result += (operations.get(i).getMontant() - operations.get(i).getRemise()) ;
        }

        return result ;
    }


    public static   double chiffreAffaireCaisse(Context context, long caisseid){
        OperationDAO operationDAO = new OperationDAO(context) ;
        ArrayList<Operation> operations = operationDAO.getAllByCaisse(caisseid);

        double result = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
            if (operations.get(i).getEntree() == 1) result += (operations.get(i).getMontant() - operations.get(i).getRemise()) ;
        }
        return result ;
    }

    public static   double chiffreAffaireCommercial(Context context, long commercial){
        OperationDAO operationDAO = new OperationDAO(context) ;
        ArrayList<Operation> operations = operationDAO.getAllByCommercial(commercial);

        double result = 0 ;
        for (int i = 0; i < operations.size(); i++) {
            if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
            if (operations.get(i).getEntree() == 1) result += (operations.get(i).getMontant() - operations.get(i).getRemise()) ;
        }
        return result ;
    }

    public static void chiffreAffairePDF(Context context, String name, String datedebut, String datefin, long pv) {

        Document doc = new Document();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ets = preferences.getString("nomSociete", null);

        try {

            String path = null ;
            if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
                path = Environment.getExternalStorageDirectory().getPath() + "/" + PV;
            }
            else if (!preferences.getBoolean("stockage",true)){
                path = context.getFilesDir().getAbsolutePath() + "/" + PV;
            }
            else{
                Toast.makeText(context,R.string.sdabsent,Toast.LENGTH_LONG).show();
                return;
            }

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            name = name + "_" + french_format.format(new Date()).replace(' ', '_') + ".pdf";
            name = name.replace('-', '_') ;
            File file = new File(dir, name);
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter writer = PdfWriter.getInstance(doc, fOut);
            writer.setPageEvent(new HeaderFooterPageEvent(context));

            //special font sizes
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);

            //document header attributes
            doc.addAuthor(context.getResources().getString(R.string.app_name));
            doc.addCreationDate();
            doc.addProducer();
            //doc.addCreator("MySampleCode.com");
            doc.addTitle(context.getString(R.string.chiffreaffaire));
            doc.setPageSize(PageSize.LETTER);
            //specify column widths

            //open the document
            doc.open();



            PointVenteDAO pointVenteDAO = new PointVenteDAO(context) ;
            PointVente pointVente = null ;
            Paragraph paragraph = null ;
            if (pv!=0){
                pointVente = pointVenteDAO.getOne(pv) ;
                paragraph = new Paragraph(pointVente.getLibelle()) ;
                paragraph.setAlignment(Element.ALIGN_LEFT);
                doc.add(paragraph);
                paragraph = new Paragraph(context.getString(R.string.teleph)  + pointVente.getTel()) ;
                paragraph.setAlignment(Element.ALIGN_LEFT);
                doc.add(paragraph);
                paragraph = new Paragraph(context.getString(R.string.adress)  + pointVente.getQuartier() + " , " +  pointVente.getVille()) ;
                paragraph.setAlignment(Element.ALIGN_LEFT);
                doc.add(paragraph);
            }

            doc.add(new Paragraph(" "));

            // Largeur des colonnes
            float[] columnWidths = {3f, 5f, 2f, 5f, 7f};
            //create PDF table with the given widths
            PdfPTable table = new PdfPTable(columnWidths);
            // set table width a percentage of the page width
            table.setWidthPercentage(90f);


            //create a paragraph
            paragraph = null;
            paragraph = new Paragraph("CHIFFRE D'AFFAIRE SUR " +datedebut + " - " + datefin);

            /*
            if (ets == null)
            else
            //    paragraph = new Paragraph("Commande DE " + ets + " DU " + formatter.format(operations.get(0).getDateoperation()) + " AU " + formatter.format(operations.get(operations.size() - 1).getDateoperation()));

            */
            paragraph.setAlignment(Element.ALIGN_CENTER);

            //insert column headings
            insertCell(table, "No", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Produit", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Qte", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Prix vente", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Montant", Element.ALIGN_CENTER, 1, bfBold12);
            table.setHeaderRows(1);


            //insert an empty row
            insertCell(table, "", Element.ALIGN_LEFT,5, bfBold12);
            //create section heading by cell merging
            //insertCell(table, "New York Orders ...", Element.ALIGN_LEFT, 4, bfBold12);
            //double orderTotal, total = 0;

            //just some random data to fill
            float entree = 0 ;
            float sortie = 0 ;
            OperationDAO operationDAO = new OperationDAO(context) ;
            MouvementDAO mouvementDAO = new MouvementDAO(context) ;
            ProduitDAO produitDAO = new ProduitDAO(context) ;

            ArrayList<Produit> produits = null ;
            if (pv==0) produits = produitDAO.getAll();
            else produits = produitDAO.getAllByPv(pv) ;

            ArrayList<Mouvement> mouvements = null;

            double valeur = 0 ;
            double total = 0 ;
            double quantite = 0 ;

            CaisseDAO caisseDAO = new CaisseDAO(context) ;
            Mouvement mouvement = null ;
            Operation operation = null ;
            Log.e("PRODUIT", String.valueOf(produits.size())) ;
            // Vente par produit
            for (int i = 0; i < produits.size(); i++) {
                try {
                    mouvements = mouvementDAO.getManyByProductInterval(produits.get(i).getId_externe(),DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin)) ;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (mouvements != null){
                    valeur = 0 ;
                    quantite = 0 ;
                    for (int j = 0; j < mouvements.size(); j++) {
                        mouvement = mouvements.get(j) ;
                        operation = operationDAO.getOne(mouvement.getOperation_id()) ;
                        if (operation==null || (operation.getAnnuler()==1 && operation.getDateannulation().before(new Date()))) continue;
                        //if (caisseDAO.getOne(operation.getCaisse_id()).getPointVente_id()!=pv) continue;
                        if (mouvement.getEntree()==1) {
                            quantite += mouvement.getQuantite() ;
                            valeur += mouvement.getPrixV() * mouvement.getQuantite() ;
                        }
                    }

                    if (valeur>0){
                        insertCell(table, String.valueOf(i+1), Element.ALIGN_LEFT, 1, bf12);
                        insertCell(table, produits.get(i).getLibelle(), Element.ALIGN_LEFT, 1, bf12);
                        insertCell(table, String.valueOf(quantite), Element.ALIGN_LEFT, 1, bf12);
                        insertCell(table, String.valueOf(produits.get(i).getPrixV()), Element.ALIGN_LEFT, 1, bf12);
                        insertCell(table, String.valueOf(valeur), Element.ALIGN_RIGHT, 1, bf12);
                        total+=valeur ;
                    }
                }
            }

            insertCell(table, context.getString(R.string.total), Element.ALIGN_CENTER, 2, bfBold12);
            insertCell(table, "", Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, "", Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(total), Element.ALIGN_RIGHT, 1, bfBold12);

            //add the PDF table to the paragraph
            paragraph.add(table);
            // add the paragraph to the document
            doc.add(paragraph);


            Toast.makeText(context, "Sauvegarder en " + path, Toast.LENGTH_LONG).show();

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }

        viewPdf(name, PV, context);
    }







    public static void listeAchatPDF(Context context, String name, String datedebut, String datefin, long pv) {

        Document doc = new Document();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ets = preferences.getString("nomSociete", null);

        try {

            String path = null ;
            if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
                path = Environment.getExternalStorageDirectory().getPath() + "/" + PV;
            }
            else if (!preferences.getBoolean("stockage",true)){
                path = context.getFilesDir().getAbsolutePath() + "/" + PV;
            }
            else{
                Toast.makeText(context,R.string.sdabsent,Toast.LENGTH_LONG).show();
                return;
            }

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            name = name + "_" + french_format.format(new Date()).replace(' ', '_') + ".pdf";
            name = name.replace('-', '_') ;
            File file = new File(dir, name);
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter writer = PdfWriter.getInstance(doc, fOut);
            writer.setPageEvent(new HeaderFooterPageEvent(context));

            //special font sizes
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);

            //document header attributes
            doc.addAuthor(context.getResources().getString(R.string.app_name));
            doc.addCreationDate();
            doc.addProducer();
            //doc.addCreator("MySampleCode.com");
            doc.addTitle(context.getString(R.string.resultat));
            doc.setPageSize(PageSize.LETTER);
            //specify column widths

            //open the document
            doc.open();


            PointVenteDAO pointVenteDAO = new PointVenteDAO(context) ;
            PointVente pointVente = null ;
            Paragraph paragraph = null ;
            if (pv!=0){
                pointVente = pointVenteDAO.getOne(pv) ;
                paragraph = new Paragraph(pointVente.getLibelle()) ;
                paragraph.setAlignment(Element.ALIGN_LEFT);
                doc.add(paragraph);
                paragraph = new Paragraph(context.getString(R.string.teleph)  + pointVente.getTel()) ;
                paragraph.setAlignment(Element.ALIGN_LEFT);
                doc.add(paragraph);
                paragraph = new Paragraph(context.getString(R.string.adress)  + pointVente.getQuartier() + " , " +  pointVente.getVille()) ;
                paragraph.setAlignment(Element.ALIGN_LEFT);
                doc.add(paragraph);
            }

            doc.add(new Paragraph(" "));

            // Largeur des colonnes
            float[] columnWidths = {3f, 5f, 2f, 5f, 7f};
            //create PDF table with the given widths
            PdfPTable table = new PdfPTable(columnWidths);
            // set table width a percentage of the page width
            table.setWidthPercentage(90f);


            //create a paragraph
            paragraph = null;
            paragraph = new Paragraph("LISTE DES ACHATS SUR " + datedebut + " - " + datefin);

            /*
            if (ets == null)
            else
            //    paragraph = new Paragraph("Commande DE " + ets + " DU " + formatter.format(operations.get(0).getDateoperation()) + " AU " + formatter.format(operations.get(operations.size() - 1).getDateoperation()));

            */
            paragraph.setAlignment(Element.ALIGN_CENTER);

            //insert column headings
            insertCell(table, "No", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Produit", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Qte", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Prix achat", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Montant", Element.ALIGN_CENTER, 1, bfBold12);
            table.setHeaderRows(1);


            //insert an empty row
            insertCell(table, "", Element.ALIGN_LEFT,5, bfBold12);
            //create section heading by cell merging
            //insertCell(table, "New York Orders ...", Element.ALIGN_LEFT, 4, bfBold12);
            //double orderTotal, total = 0;

            //just some random data to fill
            float entree = 0 ;
            float sortie = 0 ;
            OperationDAO operationDAO = new OperationDAO(context) ;
            MouvementDAO mouvementDAO = new MouvementDAO(context) ;
            ProduitDAO produitDAO = new ProduitDAO(context) ;

            ArrayList<Produit> produits = null ;
            if (pv==0) produits = produitDAO.getAll();
            else produits = produitDAO.getAllByPv(pv) ;

            ArrayList<Mouvement> mouvements = null;

            double valeur = 0 ;
            double total = 0 ;
            double quantite = 0 ;
            // Vente par produit
            CaisseDAO caisseDAO = new CaisseDAO(context) ;
            Mouvement mouvement = null ;
            Operation operation = null ;
            for (int i = 0; i < produits.size(); i++) {
                try {
                    mouvements = mouvementDAO.getManyByProductInterval(produits.get(i).getId_externe(),DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin)) ;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (mouvements != null){
                    valeur = 0 ;
                    quantite = 0 ;
                    for (int j = 0; j < mouvements.size(); j++) {
                        mouvement = mouvements.get(j) ;
                        operation = operationDAO.getOne(mouvement.getOperation_id()) ;
                        if (operation==null || (operation.getAnnuler()==1 && operation.getDateannulation().before(new Date())) || operation.getTypeOperation_id().startsWith(OperationDAO.CMD)) continue;

                        //if (caisseDAO.getOne(operation.getCaisse_id()).getPointVente_id()!=pv) continue;
                        if (mouvement.getEntree()==0) {
                            quantite += mouvement.getQuantite() ;
                            valeur += mouvement.getPrixA() * mouvement.getQuantite() ;
                        }
                    }

                    if (valeur>0){
                        insertCell(table, String.valueOf(i+1), Element.ALIGN_LEFT, 1, bf12);
                        insertCell(table, produits.get(i).getLibelle(), Element.ALIGN_LEFT, 1, bf12);
                        insertCell(table, String.valueOf(quantite), Element.ALIGN_LEFT, 1, bf12);
                        insertCell(table, Utiles.formatMtn(produits.get(i).getPrixA()), Element.ALIGN_LEFT, 1, bf12);
                        insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);
                        total+=valeur ;
                    }
                }
            }

            insertCell(table, context.getString(R.string.total), Element.ALIGN_CENTER, 2, bfBold12);
            insertCell(table, "", Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, "", Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(total), Element.ALIGN_RIGHT, 1, bfBold12);

            //add the PDF table to the paragraph
            paragraph.add(table);
            // add the paragraph to the document
            doc.add(paragraph);


            Toast.makeText(context, "Sauvegarder en " + path, Toast.LENGTH_LONG).show();

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }

        viewPdf(name, PV, context);
    }








    public static void etatDesProduitPDF(Context context, String name, String datedebut, String datefin, long pv) {

        Document doc = new Document();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ets = preferences.getString("nomSociete", null);

        try {

            String path = null ;
            if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
                path = Environment.getExternalStorageDirectory().getPath() + "/" + PV;
            }
            else if (!preferences.getBoolean("stockage",true)){
                path = context.getFilesDir().getAbsolutePath() + "/" + PV;
            }
            else{
                Toast.makeText(context,R.string.sdabsent,Toast.LENGTH_LONG).show();
                return;
            }

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            name = name + "_" + french_format.format(new Date()).replace(' ', '_') + ".pdf";
            name = name.replace('-', '_') ;
            File file = new File(dir, name);
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter writer = PdfWriter.getInstance(doc, fOut);
            writer.setPageEvent(new HeaderFooterPageEvent(context));

            //special font sizes
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);

            //document header attributes
            doc.addAuthor(context.getResources().getString(R.string.app_name));
            doc.addCreationDate();
            doc.addProducer();
            //doc.addCreator("MySampleCode.com");
            doc.addTitle(context.getString(R.string.resultat));
            doc.setPageSize(PageSize.LETTER);
            //specify column widths

            //open the document
            doc.open();


            PointVenteDAO pointVenteDAO = new PointVenteDAO(context) ;
            PointVente pointVente = null ;
            Paragraph paragraph = null ;
            if (pv!=0){
                pointVente = pointVenteDAO.getOne(pv) ;
                paragraph = new Paragraph(pointVente.getLibelle()) ;
                paragraph.setAlignment(Element.ALIGN_LEFT);
                doc.add(paragraph);
                paragraph = new Paragraph(context.getString(R.string.teleph)  + pointVente.getTel()) ;
                paragraph.setAlignment(Element.ALIGN_LEFT);
                doc.add(paragraph);
                paragraph = new Paragraph(context.getString(R.string.adress)  + pointVente.getQuartier() + " , " +  pointVente.getVille()) ;
                paragraph.setAlignment(Element.ALIGN_LEFT);
                doc.add(paragraph);
            }

            doc.add(new Paragraph(" "));

            // Largeur des colonnes
            float[] columnWidths = {3f, 5f, 4f, 4f, 4f, 4f, 4f, 4f};
            //create PDF table with the given widths
            PdfPTable table = new PdfPTable(columnWidths);
            // set table width a percentage of the page width
            table.setWidthPercentage(90f);


            //create a paragraph
            paragraph = null;
            paragraph = new Paragraph("ETAT DES PRODUITS AU " + datefin);

            /*
            if (ets == null)
            else
            //    paragraph = new Paragraph("Commande DE " + ets + " DU " + formatter.format(operations.get(0).getDateoperation()) + " AU " + formatter.format(operations.get(operations.size() - 1).getDateoperation()));

            */
            paragraph.setAlignment(Element.ALIGN_CENTER);

            //insert column headings
            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Libelle", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Qte initial", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Qte sortie", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Qte entrée", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Qte final", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "CMUP", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Valeur", Element.ALIGN_CENTER, 1, bfBold12);
            table.setHeaderRows(1);


            //insert an empty row
            insertCell(table, "", Element.ALIGN_LEFT,8, bfBold12);
            //create section heading by cell merging
            //insertCell(table, "New York Orders ...", Element.ALIGN_LEFT, 4, bfBold12);
            //double orderTotal, total = 0;

            //just some random data to fill
            float entree = 0 ;
            float sortie = 0 ;
            OperationDAO operationDAO = new OperationDAO(context) ;
            MouvementDAO mouvementDAO = new MouvementDAO(context) ;
            ProduitDAO produitDAO = new ProduitDAO(context) ;

            ArrayList<Produit> produits = null ;
            if (pv==0) produits = produitDAO.getAll();
            else produits = produitDAO.getAllByPv(pv) ;

            ArrayList<Mouvement> mouvements = null;

            double valeur = 0 ;
            double total = 0 ;
            double quantite = 0 ;
            double cmpu = 0 ;
            double restant = 0 ;
            double qsortie = 0 ;
            double qentree = 0 ;
            // Vente par produit
            CaisseDAO caisseDAO = new CaisseDAO(context) ;
            Mouvement mouvement = null ;
            Operation operation = null ;

            for (int i = 0; i < produits.size(); i++) {
                try {
                    mouvements = mouvementDAO.getLastMouvOfProduit(produits.get(i).getId_externe(),DAOBase.formatter2.parse(datefin)) ;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (mouvements != null){
                    valeur = 0 ;
                    quantite = 0 ;
                    restant = 0 ;
                    qentree = 0 ;
                    cmpu = 0 ;
                    qsortie = 0 ;

                    for (int j = 0; j < mouvements.size(); j++) {
                        mouvement = mouvements.get(j) ;
                        operation = operationDAO.getOne(mouvement.getOperation_id()) ;
                        if (operation==null || (operation.getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) || operation.getTypeOperation_id().startsWith(OperationDAO.CMD)) continue;

                        //if (pv!=0 && caisseDAO.getOne(operation.getCaisse_id()).getPointVente_id()!=pv) continue;
                        if (j==0)
                            if (mouvement.getRestant()==mouvement.getQuantite() && mouvement.getEntree()==0){
                                restant = 0 ;
                            }
                            else restant = mouvement.getRestant() ;

                        if (mouvement.getEntree()==0) {
                            valeur -= mouvement.getPrixA() * mouvement.getQuantite() ;
                            qentree += mouvement.getQuantite() ;
                        }
                        else {
                            valeur += mouvement.getCmup() * mouvement.getQuantite() ;
                            qsortie += mouvement.getQuantite() ;
                        }
                        if (restant!=0)cmpu = valeur / restant ;
                        else cmpu = mouvement.getCmup() ;
                    }

                    quantite = restant + qentree - qsortie ;
                    total+=Math.abs(valeur) ;
                    insertCell(table, String.valueOf(i+1), Element.ALIGN_LEFT, 1, bf12);
                    insertCell(table, produits.get(i).getLibelle(), Element.ALIGN_LEFT, 1, bf12);
                    insertCell(table, String.valueOf(restant), Element.ALIGN_LEFT, 1, bf12);
                    insertCell(table, Utiles.formatMtn(qsortie), Element.ALIGN_LEFT, 1, bf12);
                    insertCell(table, Utiles.formatMtn(qentree), Element.ALIGN_LEFT, 1, bf12);
                    insertCell(table, Utiles.formatMtn(quantite), Element.ALIGN_LEFT, 1, bf12);
                    insertCell(table, Utiles.formatMtn(cmpu), Element.ALIGN_LEFT, 1, bf12);
                    insertCell(table, Utiles.formatMtn(Math.abs(valeur)), Element.ALIGN_LEFT, 1, bf12);
                }
            }



            insertCell(table, "Total ", Element.ALIGN_LEFT, 7, bf12);
            insertCell(table, Utiles.formatMtn(total), Element.ALIGN_LEFT, 1, bf12);

            //add the PDF table to the paragraph
            paragraph.add(table);
            // add the paragraph to the document
            doc.add(paragraph);


            Toast.makeText(context, "Sauvegarder en " + path, Toast.LENGTH_LONG).show();

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }

        viewPdf(name, PV, context);
    }









    public static void ficheDeStockRegroupePDF(Context context, String name, String datedebut, String datefin, long pv) {

        Document doc = new Document();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ets = preferences.getString("nomSociete", null);

        try {

            String path = null ;
            if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
                path = Environment.getExternalStorageDirectory().getPath() + "/" + PV;
            }
            else if (!preferences.getBoolean("stockage",true)){
                path = context.getFilesDir().getAbsolutePath() + "/" + PV;
            }
            else{
                Toast.makeText(context,R.string.sdabsent,Toast.LENGTH_LONG).show();
                return;
            }

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            name = name + "_" + french_format.format(new Date()).replace(' ', '_') + ".pdf";
            name = name.replace('-', '_') ;
            File file = new File(dir, name);
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter writer = PdfWriter.getInstance(doc, fOut);
            writer.setPageEvent(new HeaderFooterPageEvent(context));

            //special font sizes
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);

            //document header attributes
            doc.addAuthor(context.getResources().getString(R.string.app_name));
            doc.addCreationDate();
            doc.addProducer();
            //doc.addCreator("MySampleCode.com");
            doc.addTitle(context.getString(R.string.resultat));
            doc.setPageSize(PageSize.LETTER);
            //specify column widths

            //open the document
            doc.open();


            PointVenteDAO pointVenteDAO = new PointVenteDAO(context) ;
            PointVente pointVente = null ;
            Paragraph paragraph = null ;
            if (pv!=0){
                pointVente = pointVenteDAO.getOne(pv) ;
                paragraph = new Paragraph(pointVente.getLibelle()) ;
                paragraph.setAlignment(Element.ALIGN_LEFT);
                doc.add(paragraph);
                paragraph = new Paragraph(context.getString(R.string.teleph)  + pointVente.getTel()) ;
                paragraph.setAlignment(Element.ALIGN_LEFT);
                doc.add(paragraph);
                paragraph = new Paragraph(context.getString(R.string.adress)  + pointVente.getQuartier() + " , " +  pointVente.getVille()) ;
                paragraph.setAlignment(Element.ALIGN_LEFT);
                doc.add(paragraph);
            }

            doc.add(new Paragraph(" "));

            // Largeur des colonnes
            float[] columnWidths = {3f, 5f, 4f, 4f, 4f, 4f, 4f, 4f};
            //create PDF table with the given widths
            PdfPTable table = new PdfPTable(columnWidths);
            // set table width a percentage of the page width
            table.setWidthPercentage(90f);


            //create a paragraph
            paragraph = null;
            paragraph = new Paragraph("FICHE DE STOCK SUR " + datedebut + " - " + datefin);

            /*
            if (ets == null)
            else
            //    paragraph = new Paragraph("Commande DE " + ets + " DU " + formatter.format(operations.get(0).getDateoperation()) + " AU " + formatter.format(operations.get(operations.size() - 1).getDateoperation()));

            */
            paragraph.setAlignment(Element.ALIGN_CENTER);

            //insert column headings
            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Libelle", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Qte initial", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Qte sortie", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Qte entrée", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Qte final", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Prix achat", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Valeur", Element.ALIGN_CENTER, 1, bfBold12);
            table.setHeaderRows(1);


            //insert an empty row
            insertCell(table, "", Element.ALIGN_LEFT,8, bfBold12);
            //create section heading by cell merging
            //insertCell(table, "New York Orders ...", Element.ALIGN_LEFT, 4, bfBold12);
            //double orderTotal, total = 0;

            //just some random data to fill
            float entree = 0 ;
            float sortie = 0 ;
            OperationDAO operationDAO = new OperationDAO(context) ;
            MouvementDAO mouvementDAO = new MouvementDAO(context) ;
            ProduitDAO produitDAO = new ProduitDAO(context) ;

            ArrayList<Produit> produits = null ;
            if (pv==0) produits = produitDAO.getAll();
            else produits = produitDAO.getAllByPv(pv) ;

            ArrayList<Mouvement> mouvements = null;

            double valeur = 0 ;
            double total = 0 ;
            double quantite = 0 ;
            double cmpu = 0 ;
            double restant = 0 ;
            double qsortie = 0 ;
            double qentree = 0 ;
            // Vente par produit
            CaisseDAO caisseDAO = new CaisseDAO(context) ;
            Mouvement mouvement = null ;
            Operation operation = null ;

            for (int i = 0; i < produits.size(); i++) {
                try {
                    mouvements = mouvementDAO.getManyByProductInterval(produits.get(i).getId_externe(),DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin)) ;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (mouvements != null){
                    valeur = 0 ;
                    quantite = 0 ;
                    restant = 0 ;
                    qentree = 0 ;
                    cmpu = 0 ;
                    qsortie = 0 ;

                    for (int j = 0; j < mouvements.size(); j++) {
                        mouvement = mouvements.get(j) ;
                        operation = operationDAO.getOne(mouvement.getOperation_id()) ;
                        if (operation==null || (operation.getAnnuler()==1 && operation.getDateannulation().before(new Date())) || operation.getTypeOperation_id().startsWith(OperationDAO.CMD)) continue;

                        //if (pv!=0 && caisseDAO.getOne(operation.getCaisse_id()).getPointVente_id()!=pv) continue;
                        if (j==0)
                            if (mouvement.getRestant()==mouvement.getQuantite() && mouvement.getEntree()==0){
                                restant = 0 ;
                            }
                            else restant = mouvement.getRestant() ;

                        if (mouvement.getEntree()==0) {
                            valeur -= mouvement.getPrixA() * mouvement.getQuantite() ;
                            qentree += mouvement.getQuantite() ;
                            cmpu = mouvement.getPrixA() ;
                        }
                        else {
                            valeur += mouvement.getCmup() * mouvement.getQuantite() ;
                            qsortie += mouvement.getQuantite() ;
                        }
                        /*
                        if (restant!=0) cmpu = valeur / restant ;
                        else cmpu = mouvement.getCmup() ;
                        */
                    }

                    quantite = restant + qentree - qsortie ;
                    total+=Math.abs(valeur) ;
                    insertCell(table, String.valueOf(i+1), Element.ALIGN_LEFT, 1, bf12);
                    insertCell(table, produits.get(i).getLibelle(), Element.ALIGN_LEFT, 1, bf12);
                    insertCell(table, String.valueOf(restant), Element.ALIGN_LEFT, 1, bf12);
                    insertCell(table, Utiles.formatMtn(qsortie), Element.ALIGN_LEFT, 1, bf12);
                    insertCell(table, Utiles.formatMtn(qentree), Element.ALIGN_LEFT, 1, bf12);
                    insertCell(table, Utiles.formatMtn(quantite), Element.ALIGN_LEFT, 1, bf12);
                    insertCell(table, Utiles.formatMtn(cmpu), Element.ALIGN_LEFT, 1, bf12);
                    insertCell(table, Utiles.formatMtn(Math.abs(valeur)), Element.ALIGN_LEFT, 1, bf12);
                }
            }


            insertCell(table, "Total ", Element.ALIGN_LEFT, 7, bf12);
            insertCell(table, Utiles.formatMtn(total), Element.ALIGN_LEFT, 1, bf12);


            //add the PDF table to the paragraph
            paragraph.add(table);
            // add the paragraph to the document
            doc.add(paragraph);


            Toast.makeText(context, "Sauvegarder en " + path, Toast.LENGTH_LONG).show();

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }

        viewPdf(name, PV, context);
    }





    public static void listePartenairePDF(Context context, String name, String datedebut, String datefin) {

        Document doc = new Document();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ets = preferences.getString("nomSociete", null);

        try {

            String path = null ;
            if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
                path = Environment.getExternalStorageDirectory().getPath() + "/" + PV;
            }
            else if (!preferences.getBoolean("stockage",true)){
                path = context.getFilesDir().getAbsolutePath() + "/" + PV;
            }
            else{
                Toast.makeText(context,R.string.sdabsent,Toast.LENGTH_LONG).show();
                return;
            }

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            name = name + "_" + french_format.format(new Date()).replace(' ', '_') + ".pdf";
            name = name.replace('-', '_') ;
            File file = new File(dir, name);
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter writer = PdfWriter.getInstance(doc, fOut);
            writer.setPageEvent(new HeaderFooterPageEvent(context));

            //special font sizes
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);

            //document header attributes
            doc.addAuthor(context.getResources().getString(R.string.app_name));
            doc.addCreationDate();
            doc.addProducer();
            //doc.addCreator("MySampleCode.com");
            doc.addTitle(context.getString(R.string.resultat));
            doc.setPageSize(PageSize.LETTER);
            //specify column widths

            //open the document
            doc.open();

            PointVenteDAO pointVenteDAO = new PointVenteDAO(context) ;
            PointVente pointVente = pointVenteDAO.getLast() ;

            Paragraph paragraph = new Paragraph(pointVente.getLibelle());;
            paragraph.setAlignment(Element.ALIGN_LEFT);
            doc.add(paragraph);
            paragraph = new Paragraph(context.getString(R.string.teleph)  + pointVente.getTel()) ;
            paragraph.setAlignment(Element.ALIGN_LEFT);
            doc.add(paragraph);
            paragraph = new Paragraph(context.getString(R.string.adress)  + pointVente.getQuartier() + " , " +  pointVente.getVille()) ;
            paragraph.setAlignment(Element.ALIGN_LEFT);
            doc.add(paragraph);

            doc.add(new Paragraph(" "));

            // Largeur des colonnes
            float[] columnWidths = {5f, 3f, 5f, 5f, 4f};
            //create PDF table with the given widths
            PdfPTable table = new PdfPTable(columnWidths);
            // set table width a percentage of the page width
            table.setWidthPercentage(90f);


            //create a paragraph
            paragraph = null;
            paragraph = new Paragraph("LISTE DES PARTENAIRES SUR " + datedebut + " - " + datefin);

            /*
            if (ets == null)
            else
            //    paragraph = new Paragraph("Commande DE " + ets + " DU " + formatter.format(operations.get(0).getDateoperation()) + " AU " + formatter.format(operations.get(operations.size() - 1).getDateoperation()));

            */
            paragraph.setAlignment(Element.ALIGN_CENTER);

            //insert column headings
            insertCell(table, "Désignation", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Genre", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Contact", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "E-mail", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Adresse", Element.ALIGN_CENTER, 1, bfBold12);

            table.setHeaderRows(1);


            //insert an empty row
            insertCell(table, "", Element.ALIGN_LEFT,5, bfBold12);
            //create section heading by cell merging
            //insertCell(table, "New York Orders ...", Element.ALIGN_LEFT, 4, bfBold12);
            //double orderTotal, total = 0;

            //just some random data to fill
            float entree = 0 ;
            float sortie = 0 ;
            OperationDAO operationDAO = new OperationDAO(context) ;
            MouvementDAO mouvementDAO = new MouvementDAO(context) ;
            PartenaireDAO partenaireDAO = new PartenaireDAO(context) ;

            ArrayList<Partenaire> partenaires = partenaireDAO.getAll();

            ArrayList<Mouvement> mouvements = null;

            double valeur = 0 ;
            double total = 0 ;
            double quantite = 0 ;
            // Vente par produit
            Partenaire partenaire = null ;
            for (int i = 0; i < partenaires.size(); i++) {
                partenaire = partenaires.get(i) ;
                insertCell(table, partenaire.getNom() + " " + partenaire.getPrenom() + " " + partenaire.getRaisonsocial(), Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table, partenaire.getSexe(), Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table, partenaire.getContact(), Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table, partenaire.getEmail(), Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table, partenaire.getAdresse(), Element.ALIGN_CENTER, 1, bfBold12);
                total+=valeur ;
            }

            //add the PDF table to the paragraph
            paragraph.add(table);
            // add the paragraph to the document
            doc.add(paragraph);


            Toast.makeText(context, "Sauvegarder en " + path, Toast.LENGTH_LONG).show();

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }

        viewPdf(name, PV, context);
    }







    public static void tresoreriePDF(Context context, String name, String datedebut, String datefin) {

        Document doc = new Document();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ets = preferences.getString("nomSociete", null);

        try {

            String path = null ;
            if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
                path = Environment.getExternalStorageDirectory().getPath() + "/" + PV;
            }
            else if (!preferences.getBoolean("stockage",true)){
                path = context.getFilesDir().getAbsolutePath() + "/" + PV;
            }
            else{
                Toast.makeText(context,R.string.sdabsent,Toast.LENGTH_LONG).show();
                return;
            }

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            name = name + "_" + french_format.format(new Date()).replace(' ', '_') + ".pdf";
            name = name.replace('-', '_') ;
            File file = new File(dir, name);
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter writer = PdfWriter.getInstance(doc, fOut);
            writer.setPageEvent(new HeaderFooterPageEvent(context));

            //special font sizes
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);

            //document header attributes
            doc.addAuthor(context.getResources().getString(R.string.app_name));
            doc.addCreationDate();
            doc.addProducer();
            //doc.addCreator("MySampleCode.com");
            doc.addTitle(context.getString(R.string.resultat));
            doc.setPageSize(PageSize.LETTER);
            //specify column widths

            //open the document
            doc.open();

            PointVenteDAO pointVenteDAO = new PointVenteDAO(context) ;
            PointVente pointVente = pointVenteDAO.getLast() ;

            Paragraph paragraph = new Paragraph(pointVente.getLibelle());

            doc.add(new Paragraph(" "));

            // Largeur des colonnes
            float[] columnWidths = {5f, 5f};
            //create PDF table with the given widths
            PdfPTable table = new PdfPTable(columnWidths);
            // set table width a percentage of the page width
            table.setWidthPercentage(90f);


            //create a paragraph
            paragraph = null;
            paragraph = new Paragraph("Tresorerie à " + datefin);

            /*
            if (ets == null)
            else
            //    paragraph = new Paragraph("Commande DE " + ets + " DU " + formatter.format(operations.get(0).getDateoperation()) + " AU " + formatter.format(operations.get(operations.size() - 1).getDateoperation()));

            */
            paragraph.setAlignment(Element.ALIGN_CENTER);

            //insert column headings
            insertCell(table, "Libelle", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Montant", Element.ALIGN_CENTER, 1, bfBold12);

            table.setHeaderRows(1);


            //insert an empty row
            insertCell(table, "", Element.ALIGN_LEFT,2, bfBold12);
            //create section heading by cell merging
            //insertCell(table, "New York Orders ...", Element.ALIGN_LEFT, 4, bfBold12);
            //double orderTotal, total = 0;

            //just some random data to fill
            float entree = 0 ;
            float sortie = 0 ;
            OperationDAO operationDAO = new OperationDAO(context) ;
            MouvementDAO mouvementDAO = new MouvementDAO(context) ;
            CaisseDAO caisseDAO = new CaisseDAO(context) ;
            CompteBanqueDAO compteBanqueDAO = new CompteBanqueDAO(context) ;

            ArrayList<CompteBanque> compteBanques = compteBanqueDAO.getAll();
            ArrayList<Caisse> caisses = caisseDAO.getAll();

            ArrayList<Mouvement> mouvements = null;
            EtatsFonctions etatsFonctions = new EtatsFonctions(context,datedebut,datefin) ;

            double valeur = 0 ;
            double total = 0 ;
            double totaux = 0 ;
            double quantite = 0 ;
            // Vente par produit
            Caisse caisse = null ;
            for (int k = 0; k < caisses.size(); k++) {
                total = 0 ;
                caisse = caisses.get(k) ;
                pointVente = pointVenteDAO.getOne(caisse.getPointVente_id()) ;
                insertCell(table, pointVente.getLibelle() + "(" + caisse.getCode() + ")", Element.ALIGN_LEFT, 1, bfBold12);

                try {
                    operations = operationDAO.getAll(0,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pointVente.getId());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                valeur = 0 ;
                for (int i = 0; i < operations.size(); i++) {
                    if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                    if (operations.get(i).getCaisse_id()!=caisse.getId()) continue;
                    if (operations.get(i).getComptebanque_id()>0  && !operations.get(i).getTypeOperation_id().equals(OperationDAO.BQ)) continue;
                    if (operations.get(i).getEntree()==1)valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
                }
                total += valeur ;
                Log.e("Nbre " + operations.size(),String.valueOf(valeur)) ;

                valeur = 0 ;
                for (int i = 0; i < operations.size(); i++) {
                    if (operations.get(i).getAnnuler()==1) continue;
                    if (operations.get(i).getCaisse_id()!=caisse.getId()) continue;
                    if (operations.get(i).getComptebanque_id()>0  && !operations.get(i).getTypeOperation_id().equals(OperationDAO.BQ)) continue;
                    if (operations.get(i).getEntree()==0)valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
                }

                total = total - valeur ;
                Log.e("Nbre " + operations.size(),String.valueOf(valeur)) ;
                insertCell(table, String.valueOf(total), Element.ALIGN_RIGHT, 1, bfBold12);
                totaux += total ;
            }


            insertCell(table, "Total", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, String.valueOf(total), Element.ALIGN_RIGHT, 1, bfBold12);

            CompteBanque compteBanque = null ;
            for (int k = 0; k < compteBanques.size(); k++) {
                total = 0 ;
                compteBanque = compteBanques.get(k) ;
                pointVente = pointVenteDAO.getOne(caisse.getPointVente_id()) ;
                insertCell(table, compteBanque.getLibelle() + "(" + compteBanque.getCode() + ")", Element.ALIGN_LEFT, 1, bfBold12);

                try {
                    operations = operationDAO.getAll(0,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pointVente.getId());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                valeur = 0 ;
                for (int i = 0; i < operations.size(); i++) {
                    if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                    if (operations.get(i).getComptebanque_id()!=compteBanque.getId()) continue;
                    if (operations.get(i).getTypeOperation_id().equals(OperationDAO.BQ)){
                        if (operations.get(i).getEntree()==0)valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
                    }
                    else if (operations.get(i).getEntree()==1)valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
                }
                total += valeur ;
                Log.e("Nbre " + operations.size(),String.valueOf(valeur)) ;

                valeur = 0 ;
                for (int i = 0; i < operations.size(); i++) {
                    if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                    if (operations.get(i).getCaisse_id()!=pointVente.getId()) continue;
                    if (operations.get(i).getTypeOperation_id().equals(OperationDAO.BQ)){
                        if (operations.get(i).getEntree()==1)valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
                    }
                    else if (operations.get(i).getEntree()==0) valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;

                }

                total = total - valeur ;
                Log.e("Nbre " + operations.size(),String.valueOf(valeur)) ;
                insertCell(table, String.valueOf(total), Element.ALIGN_RIGHT, 1, bfBold12);
                totaux += total ;
            }
            insertCell(table, "Total", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, String.valueOf(total), Element.ALIGN_RIGHT, 1, bfBold12);

            //add the PDF table to the paragraph
            paragraph.add(table);
            // add the paragraph to the document
            doc.add(paragraph);


            Toast.makeText(context, "Sauvegarder en " + path, Toast.LENGTH_LONG).show();

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }

        viewPdf(name, PV, context);
    }






    public static void bilanPDF(Context context, String name, String datedebut, String datefin, long pv) {

        Document doc = new Document();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ets = preferences.getString("nomSociete", null);

        try {

            String path = null ;
            if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
                path = Environment.getExternalStorageDirectory().getPath() + "/" + PV;
            }
            else if (!preferences.getBoolean("stockage",true)){
                path = context.getFilesDir().getAbsolutePath() + "/" + PV;
            }
            else{
                Toast.makeText(context,R.string.sdabsent,Toast.LENGTH_LONG).show();
                return;
            }

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            name = name + "_" + french_format.format(new Date()).replace(' ', '_') + ".pdf";
            name = name.replace('-', '_') ;
            File file = new File(dir, name);
            FileOutputStream fOut = new FileOutputStream(file);

            PdfWriter writer = PdfWriter.getInstance(doc, fOut);
            writer.setPageEvent(new HeaderFooterPageEvent(context));

            //special font sizes
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);

            //document header attributes
            doc.addAuthor(context.getResources().getString(R.string.app_name));
            doc.addCreationDate();
            doc.addProducer();
            //doc.addCreator("MySampleCode.com");
            doc.addTitle(context.getString(R.string.resultat));
            doc.setPageSize(PageSize.LETTER);
            //specify column widths

            //open the document
            doc.open();

            PointVenteDAO pointVenteDAO = new PointVenteDAO(context) ;
            PointVente pointVente = pointVenteDAO.getLast() ;

            Paragraph paragraph = new Paragraph(pointVente.getLibelle());;
            paragraph.setAlignment(Element.ALIGN_LEFT);
            doc.add(paragraph);
            paragraph = new Paragraph(context.getString(R.string.teleph)  + pointVente.getTel()) ;
            paragraph.setAlignment(Element.ALIGN_LEFT);
            doc.add(paragraph);
            paragraph = new Paragraph(context.getString(R.string.adress)  + pointVente.getQuartier() + " , " +  pointVente.getVille()) ;
            paragraph.setAlignment(Element.ALIGN_LEFT);
            doc.add(paragraph);

            doc.add(new Paragraph(" "));

            // Largeur des colonnes
            float[] columnWidths = {5f, 3f, 3f, 3f};
            //create PDF table with the given widths
            PdfPTable table = new PdfPTable(columnWidths);
            // set table width a percentage of the page width
            table.setWidthPercentage(90f);


            //create a paragraph
            paragraph = null;
            paragraph = new Paragraph("BILAN AU " + datefin);

            /*
            if (ets == null)
            else
            //    paragraph = new Paragraph("Commande DE " + ets + " DU " + formatter.format(operations.get(0).getDateoperation()) + " AU " + formatter.format(operations.get(operations.size() - 1).getDateoperation()));

            */
            paragraph.setAlignment(Element.ALIGN_CENTER);

            //insert column headings
            insertCell(table, "ACTIF", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "BRUT ", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "AMORTISSEMENT/APPROVISION", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "NET", Element.ALIGN_CENTER, 1, bfBold12);

            table.setHeaderRows(0);


            //insert an empty row
            //insertCell(table, "", Element.ALIGN_LEFT,4, bfBold12);
            //create section heading by cell merging
            //insertCell(table, "New York Orders ...", Element.ALIGN_LEFT, 4, bfBold12);
            //double orderTotal, total = 0;

            //just some random data to fill
            float entree = 0 ;
            float sortie = 0 ;
            OperationDAO operationDAO = new OperationDAO(context) ;
            MouvementDAO mouvementDAO = new MouvementDAO(context) ;
            PartenaireDAO partenaireDAO = new PartenaireDAO(context) ;

            ArrayList<Partenaire> partenaires = partenaireDAO.getAll();

            ArrayList<Mouvement> mouvements = null;

            double valeur = 0 ;
            double total = 0 ;
            double quantite = 0 ;

            ProduitDAO produitDAO = new ProduitDAO(context) ;

            ArrayList<Produit> produits = produitDAO.getAll();

            EtatsFonctions etatsFonctions = new EtatsFonctions(context, datedebut, datefin) ;

            double ammo = 0 ;
            double net = 0 ;
            double actif = 0 ;
            double passif = 0 ;

            // Vente par produit
            CaisseDAO caisseDAO = new CaisseDAO(context) ;
            Mouvement mouvement = null ;
            Operation operation = null ;

            ArrayList<Operation> operations = null ;

            // TOtal actif immobilisé
            insertCell(table, "ACTIF IMMOBILISE", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);


            // Immobilisation
            actif = etatsFonctions.getImmo(pv) ;
            insertCell(table, "Immobilisations", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(actif), Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, "0", Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(actif), Element.ALIGN_RIGHT, 1, bfBold12);

            // Charge immobilisées
            valeur = etatsFonctions.getChargeImmo(pv,null,null) ;
            total += valeur ;
            insertCell(table, "Charge immobilisé", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, "0", Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);

            // Immobilisation corporel
            valeur = etatsFonctions.getImmoCorp(pv,null,null) ;
            actif = etatsFonctions.getAmmortCorp(pv,null,null) ;
            total += valeur ;
            ammo += actif ;
            net += valeur-actif ;
            insertCell(table, "Corporelles", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(actif), Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(valeur-actif), Element.ALIGN_RIGHT, 1, bfBold12);

            // Immobilisation incorporel
            valeur = etatsFonctions.getImmoIncorp(pv,null,null) ;
            actif = etatsFonctions.getAmmortInCorp(pv,null,null) ;
            total += valeur ;
            ammo += actif ;
            net += valeur-actif ;
            insertCell(table, "Incorporelles", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, Utiles.formatMtn(actif), Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(valeur-actif), Element.ALIGN_RIGHT, 1, bfBold12);

            // Immobilisation financiere
            valeur = etatsFonctions.getImmoFinanciere(pv,null,null) ;
            total += valeur ;
            insertCell(table, "Financieres", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, "0", Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);

            // Actif circulant
            insertCell(table, "ACTIF CIRCULANT", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, "0", Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);


            // Stock
            actif = etatsFonctions.getStockFinal(pv)  ;
            insertCell(table, "Stocks", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(actif), Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, "0", Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);




            // Marchandises
            valeur = etatsFonctions.getStockFinal(pv) ;
            total += valeur ;
            insertCell(table, "Marchandises", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, "0", Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);


            /*
            // Matiere
            valeur = 0 ;
            total += valeur ;
            insertCell(table, "Matieres et autres approvisionnements", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, "0", Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);
            */


            // Créances
            actif = etatsFonctions.getFournisseurAvanceVersees(pv) +  etatsFonctions.getVenteCredit(pv,null,null) + etatsFonctions.getAutreCreance(pv,null,null) ;
            insertCell(table, "Créances", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(actif), Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, "0", Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(actif), Element.ALIGN_RIGHT, 1, bf12);



            /*
            // Fournisseurs avances versées
            valeur = etatsFonctions.getFournisseurAvanceVersees(pv) ;
            total += valeur ;
            insertCell(table, "Fournisseurs avances versées", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, "0", Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);

            */


            // Client
            valeur = etatsFonctions.getVenteCredit(pv,null,null) ;
            total += valeur ;
            insertCell(table, "Client", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, "0", Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);



            // Autres créances
            valeur = etatsFonctions.getAutreCreance(pv,null,null) ;
            total += valeur ;
            insertCell(table, "Autres créances", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, "0", Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);


            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);


            // TRESORERIE ACTIF
            insertCell(table, "TRESORERIE ACTIF", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, "0", Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);


            // En caisse
            valeur = etatsFonctions.getCaisseDebit(pv,null,null) - etatsFonctions.getCaisseCredit(pv,null,null) ;
            total += valeur ;
            insertCell(table, "En caisse", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, "0", Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);

            // Banque ou SFD
            valeur = etatsFonctions.getBanqueOuSfdDebit(pv,null,null) - etatsFonctions.getBanqueOuSfdCredit(pv,null,null) ;
            total += valeur ;
            insertCell(table, "Banque ou SFD", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, "0", Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);


            // Dépot de garantie
            valeur = etatsFonctions.getDepotGarantie(pv,null,null) ;
            total += valeur ;
            insertCell(table, "Dépot de garantie", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, "0", Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);



            // Dépot à terme
            valeur = etatsFonctions.getDepotTerme(pv,null,null) ;
            total += valeur ;
            insertCell(table, "Dépot à terme", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, "0", Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);


            // Autres placements
            valeur = etatsFonctions.getDepotAutre(pv,null,null) ;
            total += valeur ;
            insertCell(table, "Autres placements", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, "0", Element.ALIGN_RIGHT, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);

            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);


            // Total des biens ou actif
            valeur = total ;
            insertCell(table, "Total des biens ou Actif", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(total), Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(ammo), Element.ALIGN_RIGHT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(total-ammo), Element.ALIGN_RIGHT, 1, bfBold12);


            //add the PDF table to the paragraph
            paragraph.add(table);
            // add the paragraph to the document
            doc.add(paragraph) ;
            doc.add(new Paragraph(" "));doc.add(new Paragraph(" "));doc.add(new Paragraph(" "));doc.add(new Paragraph(" "));doc.add(new Paragraph(" "));doc.add(new Paragraph(" "));doc.add(new Paragraph(" "));doc.add(new Paragraph(" "));doc.add(new Paragraph(" "));doc.add(new Paragraph(" "));doc.add(new Paragraph(" "));doc.add(new Paragraph(" "));doc.add(new Paragraph(" "));doc.add(new Paragraph(" "));


            paragraph = new Paragraph() ;
            float[] columnWidths1 = {5f, 5f};
            table = new PdfPTable(columnWidths1);
            table.setWidthPercentage(90f);


            //insert column headings
            insertCell(table, "PASSIF", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "NET ", Element.ALIGN_CENTER, 1, bfBold12);

            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);



            // Capitaux propres
            valeur = 0 ;
            passif = etatsFonctions.getAchatCredit(pv,null,null) + etatsFonctions.getEmprunt(pv,null,null) + resultatExploitationPDF(context,"",datedebut,datefin,pv) ;;
            passif = total-ammo - passif ;
            insertCell(table, "Capitaux propres", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(passif), Element.ALIGN_RIGHT, 1, bfBold12);


            total = 0 ;


            // Capital ou avoir personnel
            valeur = passif ;
            total += valeur ;
            insertCell(table, "Capital ou avoir personnel", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table, Utiles.formatMtn(passif), Element.ALIGN_RIGHT, 1, bf12);


            // Reserves
            valeur = 0 ;
            total += valeur ;
            insertCell(table, "Réserves", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);



            // Report à nouveau
            valeur = 0 ;
            total += valeur ;
            insertCell(table, "Report à nouveau", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);



            // Résultat de l'exercice
            valeur = resultatExploitationPDF(context,"",datedebut,datefin,pv)  ;
            total += valeur ;
            insertCell(table, "Résultat de l'exercice (1)", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);



            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);


            // Dettes financieres et assimilées
            valeur = 0 ;
            passif =  etatsFonctions.getEmprunt(pv,null,null) ;
            insertCell(table, "Dettes financieres et assimilées", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(passif), Element.ALIGN_RIGHT, 1, bfBold12);




            // Emprunts aupres des  SFD et Banques
            valeur = etatsFonctions.getEmprunt(pv,null,null) ;
            total += valeur ;
            insertCell(table, "Emprunts aupres des  SFD et Banques", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);



            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);


            // Passif circulant
            valeur = 0 ;
            passif = etatsFonctions.getAchatCredit(pv,null,null) + etatsFonctions.getAutreDette(pv,null,null) ;
            insertCell(table, "Passif circulant", Element.ALIGN_LEFT, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(passif), Element.ALIGN_RIGHT, 1, bfBold12);



            /*

            // Client avances recues
            valeur = etatsFonctions.getClientAvanceVersées(pv) ;
            total += valeur ;
            insertCell(table, "Client avances recues", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);
            */


            // Fournisseurs
            valeur = etatsFonctions.getAchatCredit(pv,null,null) ;
            total += valeur ;
            insertCell(table, "Fournisseurs", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);


            // Autres dettes
            valeur = etatsFonctions.getAutreDette(pv,null,null) ;
            total += valeur ;
            insertCell(table, "Autres dettes", Element.ALIGN_CENTER, 1, bf12);
            insertCell(table, Utiles.formatMtn(valeur), Element.ALIGN_RIGHT, 1, bf12);


            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);


            // Autres dettes
            valeur = etatsFonctions.getAutreDette(pv,null,null) ;
            insertCell(table, "Total Passif", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, Utiles.formatMtn(total), Element.ALIGN_RIGHT, 1, bfBold12);


            //add the PDF table to the paragraph
            paragraph.add(table);
            // add the paragraph to the document
            doc.add(paragraph);


            Toast.makeText(context, "Sauvegarder en " + path, Toast.LENGTH_LONG).show();

        } catch (DocumentException de) {
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } finally {
            doc.close();
        }

        viewPdf(name, PV, context);
    }








    public static void chiffreAffaireExcel(Context context, String name, String datedebut, String datefin,long pv) {

        Document doc = new Document();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ets = preferences.getString("nomSociete", null);

        try {

            String path = null ;
            if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
                path = Environment.getExternalStorageDirectory().getPath() + "/" + PV;
            }
            else if (!preferences.getBoolean("stockage",true)){
                path = context.getFilesDir().getAbsolutePath() + "/" + PV;
            }
            else{
                Toast.makeText(context,R.string.sdabsent,Toast.LENGTH_LONG).show();
                return;
            }

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            name = name + "_" + french_format.format(new Date()).replace(' ', '_') + ".xlsx";
            name = name.replace('-', '_') ;

            //file path
            File file = new File(path, name);

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;



            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("Chiffre d'affaire", 0);
            int ligne = 0 ;
            sheet.addCell(new Label(0, ligne, "No"));
            sheet.addCell(new Label(1, ligne, "Libelle"));
            sheet.addCell(new Label(2, ligne, "Qte"));
            sheet.addCell(new Label(3, ligne, "Prix vente"));
            sheet.addCell(new Label(4, ligne, "Montant"));
            ligne++ ;


            //just some random data to fill
            float entree = 0 ;
            float sortie = 0 ;
            OperationDAO operationDAO = new OperationDAO(context) ;
            MouvementDAO mouvementDAO = new MouvementDAO(context) ;
            ProduitDAO produitDAO = new ProduitDAO(context) ;

            ArrayList<Produit> produits = null ;
            if (pv==0) produits = produitDAO.getAll();
            else produits = produitDAO.getAllByPv(pv) ;

            ArrayList<Mouvement> mouvements = null;

            double valeur = 0 ;
            double total = 0 ;
            double quantite = 0 ;
            // Vente par produit
            CaisseDAO caisseDAO = new CaisseDAO(context) ;
            Mouvement mouvement = null ;
            Operation operation = null ;
            for (int i = 0; i < produits.size(); i++) {
                try {
                    mouvements = mouvementDAO.getManyByProductInterval(produits.get(i).getId_externe(),DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin)) ;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (mouvements != null){
                    valeur = 0 ;
                    quantite = 0 ;
                    for (int j = 0; j < mouvements.size(); j++) {
                        mouvement = mouvements.get(j) ;
                        operation = operationDAO.getOne(mouvement.getOperation_id()) ;
                        if (operation==null || operation.getAnnuler()==1 && operation.getDateannulation().before(new Date())) continue;
                        //if (caisseDAO.getOne(operation.getCaisse_id()).getPointVente_id()!=pv) continue;
                        if (mouvement.getEntree()==1) {
                            quantite += mouvement.getQuantite() ;
                            valeur += mouvement.getPrixV() * mouvement.getQuantite() ;
                        }
                    }

                    if (valeur>0){
                        sheet.addCell(new Label(0, ligne, String.valueOf(i+1)));
                        sheet.addCell(new Label(1, ligne, produits.get(i).getLibelle()));
                        sheet.addCell(new Label(2, ligne, String.valueOf(quantite)));
                        sheet.addCell(new Label(3, ligne, Utiles.formatMtn(produits.get(i).getPrixV())));
                        sheet.addCell(new Label(4, ligne, Utiles.formatMtn(valeur)));
                        ligne++ ;
                        total+=valeur ;
                    }
                }
            }

            sheet.addCell(new Label(0, ligne, context.getString(R.string.total)));
            sheet.addCell(new Label(1, ligne, ""));
            sheet.addCell(new Label(2, ligne, ""));
            sheet.addCell(new Label(3, ligne, ""));
            sheet.addCell(new Label(4, ligne, Utiles.formatMtn(total)));
            ligne++ ;

            workbook.write();

            Toast.makeText(context, "Sauvegarder en " + path, Toast.LENGTH_LONG).show();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        } finally {
            doc.close();
        }

        viewEXCEL(name, PV, context);
    }





    public static void listeAchatExcel(Context context, String name, String datedebut, String datefin, long pv) {

        Document doc = new Document();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ets = preferences.getString("nomSociete", null);

        try {

            String path = null ;
            if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
                path = Environment.getExternalStorageDirectory().getPath() + "/" + PV;
            }
            else if (!preferences.getBoolean("stockage",true)){
                path = context.getFilesDir().getAbsolutePath() + "/" + PV;
            }
            else{
                Toast.makeText(context,R.string.sdabsent,Toast.LENGTH_LONG).show();
                return;
            }

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            name = name + "_" + french_format.format(new Date()).replace(' ', '_') + ".xlsx";
            name = name.replace('-', '_') ;

            //file path
            File file = new File(path, name);

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;



            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("Liste des achats", 0);
            int ligne = 0 ;
            sheet.addCell(new Label(0, ligne, "No"));
            sheet.addCell(new Label(1, ligne, "Libelle"));
            sheet.addCell(new Label(2, ligne, "Qte"));
            sheet.addCell(new Label(3, ligne, "Prix d'achat"));
            sheet.addCell(new Label(4, ligne, "Montant"));
            ligne++ ;


            //just some random data to fill
            float entree = 0 ;
            float sortie = 0 ;
            OperationDAO operationDAO = new OperationDAO(context) ;
            MouvementDAO mouvementDAO = new MouvementDAO(context) ;
            ProduitDAO produitDAO = new ProduitDAO(context) ;

            ArrayList<Produit> produits = null ;
            if (pv==0) produits = produitDAO.getAll();
            else produits = produitDAO.getAllByPv(pv) ;

            ArrayList<Mouvement> mouvements = null;

            double valeur = 0 ;
            double total = 0 ;
            double quantite = 0 ;
            // Vente par produit
            CaisseDAO caisseDAO = new CaisseDAO(context) ;
            Mouvement mouvement = null ;
            Operation operation = null ;
            for (int i = 0; i < produits.size(); i++) {
                try {
                    mouvements = mouvementDAO.getManyByProductInterval(produits.get(i).getId_externe(),DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin)) ;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (mouvements != null){
                    valeur = 0 ;
                    quantite = 0 ;
                    for (int j = 0; j < mouvements.size(); j++) {
                        mouvement = mouvements.get(j) ;
                        operation = operationDAO.getOne(mouvement.getOperation_id()) ;
                        if (operation==null || (operation.getAnnuler()==1 && operation.getDateannulation().before(new Date())) || operation.getTypeOperation_id().startsWith(OperationDAO.CMD)) continue;

                        //if (caisseDAO.getOne(operation.getCaisse_id()).getPointVente_id()!=pv) continue;
                        if (mouvement.getEntree()==0) {
                            quantite += mouvement.getQuantite() ;
                            valeur += mouvement.getPrixA() * mouvement.getQuantite() ;
                        }
                    }

                    if (valeur>0){
                        sheet.addCell(new Label(0, ligne, String.valueOf(i+1)));
                        sheet.addCell(new Label(1, ligne, produits.get(i).getLibelle()));
                        sheet.addCell(new Label(2, ligne, String.valueOf(quantite)));
                        sheet.addCell(new Label(3, ligne, Utiles.formatMtn(produits.get(i).getPrixA())));
                        sheet.addCell(new Label(4, ligne, Utiles.formatMtn(valeur)));
                        ligne++ ;
                        total+=valeur ;
                    }
                }
            }

            sheet.addCell(new Label(0, ligne, context.getString(R.string.total)));
            sheet.addCell(new Label(1, ligne, ""));
            sheet.addCell(new Label(2, ligne, ""));
            sheet.addCell(new Label(3, ligne, ""));
            sheet.addCell(new Label(4, ligne, Utiles.formatMtn(total)));
            ligne++ ;

            workbook.write();

            Toast.makeText(context, "Sauvegarder en " + path, Toast.LENGTH_LONG).show();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        } finally {
            doc.close();
        }

        viewEXCEL(name, PV, context);
    }






    public static void ficheDeStockRegroupeExcel(Context context, String name, String datedebut, String datefin, long pv) {

        Document doc = new Document();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ets = preferences.getString("nomSociete", null);

        try {

            String path = null ;
            if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
                path = Environment.getExternalStorageDirectory().getPath() + "/" + PV;
            }
            else if (!preferences.getBoolean("stockage",true)){
                path = context.getFilesDir().getAbsolutePath() + "/" + PV;
            }
            else{
                Toast.makeText(context,R.string.sdabsent,Toast.LENGTH_LONG).show();
                return;
            }

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            name = name + "_" + french_format.format(new Date()).replace(' ', '_') + ".xlsx";
            name = name.replace('-', '_') ;

            //file path
            File file = new File(path, name);

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;



            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("Fiche de stock", 0);
            int ligne = 0 ;
            sheet.addCell(new Label(0, ligne, "No"));
            sheet.addCell(new Label(1, ligne, "Libelle"));
            sheet.addCell(new Label(2, ligne, "Qte initial"));
            sheet.addCell(new Label(3, ligne, "Qte entree"));
            sheet.addCell(new Label(4, ligne, "Qte sortie"));
            sheet.addCell(new Label(5, ligne, "Qte final"));
            sheet.addCell(new Label(6, ligne, "Prix achat"));
            sheet.addCell(new Label(7, ligne, "Valeur"));
            ligne++ ;


            //just some random data to fill
            float entree = 0 ;
            float sortie = 0 ;
            OperationDAO operationDAO = new OperationDAO(context) ;
            MouvementDAO mouvementDAO = new MouvementDAO(context) ;
            ProduitDAO produitDAO = new ProduitDAO(context) ;

            ArrayList<Produit> produits = null ;
            if (pv==0) produits = produitDAO.getAll();
            else produits = produitDAO.getAllByPv(pv) ;

            ArrayList<Mouvement> mouvements = null;
            double valeur = 0 ;
            double total = 0 ;
            double cmpu = 0 ;
            double quantite = 0 ;
            double restant = 0 ;
            double qsortie = 0 ;
            double qentree = 0 ;
            // Vente par produit
            CaisseDAO caisseDAO = new CaisseDAO(context) ;
            Mouvement mouvement = null ;
            Operation operation = null ;
            for (int i = 0; i < produits.size(); i++) {
                try {
                    mouvements = mouvementDAO.getManyByProduit(produits.get(i).getId_externe(),DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin)) ;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (mouvements != null){
                    valeur = 0 ;
                    quantite = 0 ;
                    cmpu = 0 ;
                    restant = 0 ;
                    qentree = 0 ;
                    qsortie = 0 ;
                    Log.e("SIZE", String.valueOf(mouvements.size())) ;
                    for (int j = 0; j < mouvements.size(); j++) {
                        mouvement = mouvements.get(j) ;
                        operation = operationDAO.getOne(mouvement.getOperation_id()) ;
                        Log.e("OP", String.valueOf(mouvement.getOperation_id())) ;
                        if (operation!=null)Log.e("OP", String.valueOf(operation.getId())) ;
                        if (operation==null || (operation.getAnnuler()==1 && operation.getDateannulation().before(new Date())) || operation.getTypeOperation_id().startsWith(OperationDAO.CMD)) continue;

                        //if (pv!=0 && caisseDAO.getOne(operation.getCaisse_id()).getPointVente_id()!=pv) continue;

                        if (j==0)
                            if (mouvement.getRestant()==mouvement.getQuantite() && mouvement.getEntree()==0){
                                restant = 0 ;
                            }
                            else restant = mouvement.getRestant() ;

                        if (mouvement.getEntree()==0) {
                            valeur -= mouvement.getPrixA() * mouvement.getQuantite() ;
                            qentree += mouvement.getQuantite() ;
                            cmpu = mouvement.getPrixA() ;
                        }
                        else {
                            valeur += mouvement.getCmup() * mouvement.getQuantite() ;
                            qsortie += mouvement.getQuantite() ;
                        }
                        /*
                        if (restant!=0)cmpu = valeur / restant ;
                        else cmpu = mouvement.getCmup() ;
                        */
                    }

                    quantite = restant + qentree - qsortie ;
                    total+=Math.abs(valeur) ;
                    Log.e("L" + ligne + ": " + produits.get(i).getLibelle(),"Qte = " + quantite + "; Qent = " + qentree + "; QSt" + qsortie + "; Valeur = " + valeur) ;

                    sheet.addCell(new Label(0, ligne, String.valueOf(ligne+1)));
                    sheet.addCell(new Label(1, ligne, produits.get(i).getLibelle()));
                    sheet.addCell(new Label(2, ligne, String.valueOf(restant)));
                    sheet.addCell(new Label(3, ligne, Utiles.formatMtn(qsortie)));
                    sheet.addCell(new Label(4, ligne, Utiles.formatMtn(qentree)));
                    sheet.addCell(new Label(5, ligne, Utiles.formatMtn(quantite)));
                    sheet.addCell(new Label(6, ligne, Utiles.formatMtn(cmpu)));
                    sheet.addCell(new Label(7, ligne, Utiles.formatMtn(Math.abs(valeur))));
                    ligne++ ;
                }
            }
            ligne++ ;

            sheet.addCell(new Label(0, ligne, String.valueOf(ligne+1)));
            sheet.addCell(new Label(1, ligne, "Total"));
            sheet.addCell(new Label(7, ligne, Utiles.formatMtn(total)));

            workbook.write();

            Toast.makeText(context, "Sauvegarder en " + path, Toast.LENGTH_LONG).show();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        } finally {
            doc.close();
        }

        viewEXCEL(name, PV, context);
    }








    public static void etatDesProduitExcel(Context context, String name, String datedebut, String datefin, long pv) {

        Document doc = new Document();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ets = preferences.getString("nomSociete", null);

        try {

            String path = null ;
            if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
                path = Environment.getExternalStorageDirectory().getPath() + "/" + PV;
            }
            else if (!preferences.getBoolean("stockage",true)){
                path = context.getFilesDir().getAbsolutePath() + "/" + PV;
            }
            else{
                Toast.makeText(context,R.string.sdabsent,Toast.LENGTH_LONG).show();
                return;
            }

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            name = name + "_" + french_format.format(new Date()).replace(' ', '_') + ".xlsx";
            name = name.replace('-', '_') ;

            //file path
            File file = new File(path, name);

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;



            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("rapport", 0);
            int ligne = 0 ;
            sheet.addCell(new Label(0, ligne, "No"));
            sheet.addCell(new Label(1, ligne, "Libelle"));
            sheet.addCell(new Label(2, ligne, "Qte initial"));
            sheet.addCell(new Label(3, ligne, "Qte entree"));
            sheet.addCell(new Label(4, ligne, "Qte sortie"));
            sheet.addCell(new Label(5, ligne, "Qte final"));
            sheet.addCell(new Label(6, ligne, "CMUP(Cout Moyent Unitaire Pondéré)"));
            sheet.addCell(new Label(7, ligne, "Valeur"));
            ligne++ ;


            //just some random data to fill
            float entree = 0 ;
            float sortie = 0 ;
            OperationDAO operationDAO = new OperationDAO(context) ;
            MouvementDAO mouvementDAO = new MouvementDAO(context) ;
            ProduitDAO produitDAO = new ProduitDAO(context) ;

            ArrayList<Produit> produits = null ;
            if (pv==0) produits = produitDAO.getAll();
            else produits = produitDAO.getAllByPv(pv) ;

            ArrayList<Mouvement> mouvements = null;
            double valeur = 0 ;
            double total = 0 ;
            double cmpu = 0 ;
            double quantite = 0 ;
            double restant = 0 ;
            double qsortie = 0 ;
            double qentree = 0 ;
            // Vente par produit
            CaisseDAO caisseDAO = new CaisseDAO(context) ;
            Mouvement mouvement = null ;
            Operation operation = null ;
            for (int i = 0; i < produits.size(); i++) {
                try {
                    mouvements = mouvementDAO.getLastMouvOfProduit(produits.get(i).getId_externe(),DAOBase.formatter2.parse(datefin)) ;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (mouvements != null){
                    valeur = 0 ;
                    quantite = 0 ;
                    cmpu = 0 ;
                    restant = 0 ;
                    qentree = 0 ;
                    qsortie = 0 ;
                    Log.e("SIZE", String.valueOf(mouvements.size())) ;
                    for (int j = 0; j < mouvements.size(); j++) {
                        mouvement = mouvements.get(j) ;
                        operation = operationDAO.getOne(mouvement.getOperation_id()) ;
                        Log.e("OP", String.valueOf(mouvement.getOperation_id())) ;
                        if (operation!=null)Log.e("OP", String.valueOf(operation.getId())) ;
                        if (operation==null || (operation.getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) || operation.getTypeOperation_id().startsWith(OperationDAO.CMD)) continue;

                        //if (pv!=0 && caisseDAO.getOne(operation.getCaisse_id()).getPointVente_id()!=pv) continue;

                        if (j==0)
                            if (mouvement.getRestant()==mouvement.getQuantite() && mouvement.getEntree()==0){
                                restant = 0 ;
                            }
                            else restant = mouvement.getRestant() ;

                        if (mouvement.getEntree()==0) {
                            valeur -= mouvement.getPrixA() * mouvement.getQuantite() ;
                            qentree += mouvement.getQuantite() ;
                        }
                        else {
                            valeur += mouvement.getCmup() * mouvement.getQuantite() ;
                            qsortie += mouvement.getQuantite() ;
                        }
                        if (restant!=0)cmpu = valeur / restant ;
                        else cmpu = mouvement.getCmup() ;
                    }

                    quantite = restant + qentree - qsortie ;
                    total+=Math.abs(valeur) ;
                    Log.e("L" + ligne + ": " + produits.get(i).getLibelle(),"Qte = " + quantite + "; Qent = " + qentree + "; QSt" + qsortie + "; Valeur = " + valeur) ;

                    sheet.addCell(new Label(0, ligne, String.valueOf(ligne+1)));
                    sheet.addCell(new Label(1, ligne, produits.get(i).getLibelle()));
                    sheet.addCell(new Label(2, ligne, String.valueOf(restant)));
                    sheet.addCell(new Label(3, ligne, Utiles.formatMtn(qsortie)));
                    sheet.addCell(new Label(4, ligne, Utiles.formatMtn(qentree)));
                    sheet.addCell(new Label(5, ligne, Utiles.formatMtn(quantite)));
                    sheet.addCell(new Label(6, ligne, Utiles.formatMtn(cmpu)));
                    sheet.addCell(new Label(7, ligne, Utiles.formatMtn(Math.abs(valeur))));
                    ligne++ ;
                }
            }
            ligne++ ;


            sheet.addCell(new Label(0, ligne, String.valueOf(ligne+1)));
            sheet.addCell(new Label(1, ligne, "Total"));
            sheet.addCell(new Label(7, ligne, Utiles.formatMtn(total)));

            workbook.write();

            Toast.makeText(context, "Sauvegarder en " + path, Toast.LENGTH_LONG).show();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        } finally {
            doc.close();
        }

        viewEXCEL(name, PV, context);
    }










    public static void bilanEXCEL(Context context, String name, String datedebut, String datefin) {

        Document doc = new Document();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ets = preferences.getString("nomSociete", null);

        try {

            String path = null ;
            if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
                path = Environment.getExternalStorageDirectory().getPath() + "/" + PV;
            }
            else if (!preferences.getBoolean("stockage",true)){
                path = context.getFilesDir().getAbsolutePath() + "/" + PV;
            }
            else{
                Toast.makeText(context,R.string.sdabsent,Toast.LENGTH_LONG).show();
                return;
            }

            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            name = name + "_" + french_format.format(new Date()).replace(' ', '_') + ".xlsx";
            name = name.replace('-', '_') ;

            //file path
            File file = new File(path, name);

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;



            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("Bilan", 0);
            int ligne = 0 ;
            sheet.addCell(new Label(0, ligne, "ACTIF"));
            sheet.addCell(new Label(1, ligne, "MONTANT"));
            sheet.addCell(new Label(2, ligne, "PASSIF"));
            sheet.addCell(new Label(3, ligne, "MONTANT"));
            ligne++ ;


            //just some random data to fill
            float entree = 0 ;
            float sortie = 0 ;
            OperationDAO operationDAO = new OperationDAO(context) ;
            MouvementDAO mouvementDAO = new MouvementDAO(context) ;
            ProduitDAO produitDAO = new ProduitDAO(context) ;
            ligne++ ;

            sheet.addCell(new Label(0, ligne, String.valueOf(ligne+1)));

            PartenaireDAO partenaireDAO = new PartenaireDAO(context) ;

            ArrayList<Partenaire> partenaires = partenaireDAO.getAll();

            ArrayList<Mouvement> mouvements = null;

            double valeur = 0 ;
            double total = 0 ;
            double quantite = 0 ;


            ArrayList<Produit> produits = produitDAO.getAll();

            ArrayList<Operation> operations = null ;

            double cmpu = 0 ;
            double restant = 0 ;
            double qsortie = 0 ;
            double qentree = 0 ;
            double actif = 0 ;
            double passif = 0 ;
            // Vente par produit
            CaisseDAO caisseDAO = new CaisseDAO(context) ;
            Mouvement mouvement = null ;
            Operation operation = null ;


            // TOtal actif immobilisé
            sheet.addCell(new Label(0, ligne, "Total actif immobilisé"));
            sheet.addCell(new Label(1, ligne, ""));

            // Capital
            sheet.addCell(new Label(2, ligne, "Capital"));
            sheet.addCell(new Label(3, ligne, ""));
            ligne++ ;

            // immobilisation
            sheet.addCell(new Label(0, ligne, "Immobilisation"));
            sheet.addCell(new Label(1, ligne, ""));

            // Passif
            sheet.addCell(new Label(2, ligne, "Passif"));
            sheet.addCell(new Label(3, ligne, ""));
            ligne++ ;

            // Fond Commercial
            sheet.addCell(new Label(0, ligne, "Fond commercial"));
            sheet.addCell(new Label(1, ligne, ""));

            // Emprunt
            sheet.addCell(new Label(2, ligne, "Emprunt"));
            sheet.addCell(new Label(3, ligne, ""));
            ligne++ ;

            // total actif


            for (int i = 0; i < produits.size(); i++) {
                try {
                    mouvements = mouvementDAO.getLastMouvOfProduit(produits.get(i).getId_externe(),DAOBase.formatter2.parse(datefin)) ;
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (mouvements != null){
                    valeur = 0 ;
                    quantite = 0 ;
                    restant = 0 ;
                    qentree = 0 ;
                    cmpu = 0 ;
                    qsortie = 0 ;

                    for (int j = 0; j < mouvements.size(); j++) {
                        mouvement = mouvements.get(j) ;
                        operation = operationDAO.getOne(mouvement.getOperation_id()) ;
                        if (operation==null || (operation.getAnnuler()==1 && operation.getDateannulation().before(new Date())) || operation.getTypeOperation_id().startsWith(OperationDAO.CMD)) continue;
                        if (j==0)
                            if (mouvement.getRestant()==mouvement.getQuantite() && mouvement.getEntree()==0){
                                restant = 0 ;
                            }
                            else restant = mouvement.getRestant() ;

                        if (mouvement.getEntree()==0) {
                            valeur -= mouvement.getPrixA() * mouvement.getQuantite() ;
                            qentree += mouvement.getQuantite() ;
                        }
                        else {
                            valeur += mouvement.getCmup() * mouvement.getQuantite() ;
                            qsortie += mouvement.getQuantite() ;
                        }
                        if (restant!=0)cmpu = valeur / restant ;
                        else cmpu = mouvement.getCmup() ;
                    }

                    quantite = restant + qentree - qsortie ;
                    total+=Math.abs(valeur) ;
                }
            }
            double stock = total ;


            try {
                operations = operationDAO.getAll(1,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),0);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getPayer()==0){
                    valeur += operations.get(i).getMontant() ;
                    ArrayList<Operation> payements = operationDAO.getMany(operations.get(i).getId());
                    for (int j = 0; j < payements.size(); j++) {
                        valeur -= (payements.get(j).getMontant()- payements.get(j).getRemise()) ;
                    }
                }
            }

            double creance = valeur ;


            sheet.addCell(new Label(0, ligne, "Total actif circulant"));
            sheet.addCell(new Label(1, ligne, String.valueOf(stock+creance)));


            // Dettes
            sheet.addCell(new Label(2, ligne, "Dettes(Fournisseurs)"));
            sheet.addCell(new Label(3, ligne, ""));
            ligne++ ;

            // Stock
            sheet.addCell(new Label(0, ligne, "Stock"));
            sheet.addCell(new Label(1, ligne, Utiles.formatMtn(total)));
            ligne++ ;
            actif += total ;

            try {
                operations = operationDAO.getAll(1, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),0);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }

            double a = valeur ;
            try {
                operations = operationDAO.getAll(2,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),0);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }


            try {
                operations = operationDAO.getAll(5,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),0);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }


            double b = valeur ;


            // Achat

            try {
                operations = operationDAO.getAll(5,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),0);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }




            try {
                operations = operationDAO.getAll(2, DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),0);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.TRANSPORT))valeur += operations.get(i).getMontant() ;
            }


            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.TEL))valeur += operations.get(i).getMontant() ;
            }


            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.LOYER))valeur += operations.get(i).getMontant() ;
            }


            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.EAU))valeur += operations.get(i).getMontant() ;
            }


            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.ELECTRICITE))valeur += operations.get(i).getMontant() ;
            }


            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.FRAIS_PERSO))valeur += operations.get(i).getMontant() ;
            }


            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.AUTRES_DEP))valeur += operations.get(i).getMontant() ;
            }


            double c = a-b ;




            // Produit financiere
            try {
                operations = operationDAO.getAll(7,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),0);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }

            double d = valeur ;

            // Charge financiere
            try {
                operations = operationDAO.getAll(8,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),0);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }

            d -= valeur ;


            // Produit Except
            try {
                operations = operationDAO.getAll(9,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),0);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }


            double f = valeur ;

            // Charge Except
            try {
                operations = operationDAO.getAll(10,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),0);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }

            f -= valeur ;




            // Impot et taxe
            try {
                operations = operationDAO.getAll(2,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),0);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.IMPOT))valeur += operations.get(i).getMontant() ;
            }



            double g = c+d+f-valeur ;
            // Résultat
            sheet.addCell(new Label(2, ligne, "Résultat"));
            sheet.addCell(new Label(3, ligne, String.valueOf(g)));
            ligne++ ;

            passif += g ;



            // Creance

            sheet.addCell(new Label(0, ligne, "Créances"));
            sheet.addCell(new Label(1, ligne, String.valueOf(creance)));
            sheet.addCell(new Label(2, ligne, ""));
            sheet.addCell(new Label(3, ligne, ""));
            ligne++ ;


            // Total trésorerie
            try {
                operations = operationDAO.getAll(13,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),0);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getEntree()==0)valeur += operations.get(i).getMontant() ;
            }
            double banque = valeur ;
            double tta = valeur ;
            sheet.addCell(new Label(0, ligne, "Total trésorerie actif"));
            sheet.addCell(new Label(1, ligne, String.valueOf(tta)));
            sheet.addCell(new Label(2, ligne, ""));
            sheet.addCell(new Label(3, ligne, ""));
            ligne++ ;


            // Banque
            sheet.addCell(new Label(0, ligne, "Banques"));
            sheet.addCell(new Label(1, ligne, String.valueOf(banque)));
            sheet.addCell(new Label(2, ligne, ""));
            sheet.addCell(new Label(3, ligne, ""));
            ligne++ ;

            actif += stock+creance + tta;

            // Caisse
            sheet.addCell(new Label(0, ligne, "Caisse"));
            sheet.addCell(new Label(1, ligne, String.valueOf(tta)));
            sheet.addCell(new Label(2, ligne, ""));
            sheet.addCell(new Label(3, ligne, ""));
            ligne++ ;

            // Total actif
            sheet.addCell(new Label(0, ligne, "Total actif"));
            sheet.addCell(new Label(1, ligne, String.valueOf(actif)));

            // Total Passif
            sheet.addCell(new Label(2, ligne, "Total Passif"));
            sheet.addCell(new Label(3, ligne, String.valueOf(passif)));
            ligne++ ;



            workbook.write();

            Toast.makeText(context, "Sauvegarder en " + path, Toast.LENGTH_LONG).show();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.e("PDFCreator", "ioException:" + e);
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        } finally {
            doc.close();
        }

        viewEXCEL(name, PV, context);
    }








    public static void resultatExploitationEXCEL(Context context, String name, String datedebut, String datefin, long pv) {

        Document doc = new Document();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String ets = preferences.getString("nomSociete", null);

        try {

            String path = null ;
            if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
                path = Environment.getExternalStorageDirectory().getPath() + "/" + PV;
            }
            else if (!preferences.getBoolean("stockage",true)){
                path = context.getFilesDir().getPath() + "/" + PV;
            }
            else{
                Toast.makeText(context,R.string.sdabsent,Toast.LENGTH_LONG).show();
                return;
            }



            File dir = new File(path);
            if (!dir.exists())
                dir.mkdirs();

            name = name + "_" + french_format.format(new Date()).replace(' ', '_') + ".xlsx";
            name = name.replace('-', '_') ;

            //file path
            File file = new File(path, name);

            WorkbookSettings wbSettings = new WorkbookSettings();
            wbSettings.setLocale(new Locale("en", "EN"));
            WritableWorkbook workbook;



            workbook = Workbook.createWorkbook(file, wbSettings);
            //Excel sheet name. 0 represents first sheet
            WritableSheet sheet = workbook.createSheet("Resultat d'exploitation", 0);
            int ligne = 0 ;
            sheet.addCell(new Label(0, ligne, " "));
            sheet.addCell(new Label(1, ligne, "Valeur"));
            ligne++ ;


            float entree = 0 ;
            float sortie = 0 ;
            OperationDAO operationDAO = new OperationDAO(context) ;
            MouvementDAO mouvementDAO = new MouvementDAO(context) ;
            ProduitDAO produitDAO = new ProduitDAO(context) ;

            ArrayList<Operation> operations = null ;
            try {
                operations = operationDAO.getAll(1,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            double valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }

            double a = valeur ;
            // Chiffre d'affaire
            sheet.addCell(new Label(0, ligne, context.getString(R.string.chiffreaffairee)));
            sheet.addCell(new Label(1, ligne, Utiles.formatMtn(valeur)));
            ligne++ ;

            /*

            ArrayList<Produit> produits = null ;
            if (pv==0) produits = produitDAO.getAll();
            else produits = produitDAO.getAllByPv(pv) ;

            ArrayList<Mouvement> mouvements = null;

            CaisseDAO caisseDAO = new CaisseDAO(context);
            // Vente par produit
            for (int i = 0; i < produits.size(); i++) {
                try {
                    mouvements = mouvementDAO.getManyByProductInterval(produits.get(i).getId_externe(),DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin)) ;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (mouvements != null){
                    valeur = 0 ;
                    for (int j = 0; j < mouvements.size(); j++) {
                        Mouvement mouvement = mouvements.get(j);;
                        Operation operation = operationDAO.getOne(mouvement.getOperation_id());;
                        if (operation==null || operation.getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                        //if (caisseDAO.getOne(operation.getCaisse_id()).getPointVente_id()!=pv) continue;
                        if (mouvements.get(j).getEntree()==1  && operationDAO.getOne(mouvements.get(j).getOperation_id()).getAttente()==0) valeur += mouvements.get(j).getPrixV()*mouvements.get(j).getQuantite() ;
                    }

                    if (valeur>0){
                        sheet.addCell(new Label(0, ligne, context.getString(R.string.ventede) + " " + produits.get(i).getLibelle()));
                        sheet.addCell(new Label(1, ligne, Utiles.formatMtn(valeur)));
                        ligne++ ;
                    }
                }
            }
            */

            // Achat et  autres depense
            try {
                operations = operationDAO.getAll(2,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }


            try {
                operations = operationDAO.getAll(5,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }


            double b = valeur ;

            sheet.addCell(new Label(0, ligne, context.getString(R.string.achatdepense)));
            sheet.addCell(new Label(1, ligne, Utiles.formatMtn(valeur)));
            ligne++ ;


            // Achat

            try {
                operations = operationDAO.getAll(5,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }

            sheet.addCell(new Label(0, ligne, context.getString(R.string.achatmse)));
            sheet.addCell(new Label(1, ligne, Utiles.formatMtn(valeur)));
            ligne++ ;



            try {
                operations = operationDAO.getAll(2,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.TRANSPORT))valeur += operations.get(i).getMontant() ;
            }

            // transport
            sheet.addCell(new Label(0, ligne, context.getString(R.string.tranport)));
            sheet.addCell(new Label(1, ligne, Utiles.formatMtn(valeur)));
            ligne++ ;

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.TEL))valeur += operations.get(i).getMontant() ;
            }

            // telephone
            sheet.addCell(new Label(0, ligne, context.getString(R.string.tele)));
            sheet.addCell(new Label(1, ligne, Utiles.formatMtn(valeur)));
            ligne++ ;

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.LOYER))valeur += operations.get(i).getMontant() ;
            }

            // loyer
            sheet.addCell(new Label(0, ligne, context.getString(R.string.loyer)));
            sheet.addCell(new Label(1, ligne, Utiles.formatMtn(valeur)));
            ligne++ ;

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.EAU))valeur += operations.get(i).getMontant() ;
            }

            // eau
            sheet.addCell(new Label(0, ligne, context.getString(R.string.eau)));
            sheet.addCell(new Label(1, ligne, Utiles.formatMtn(valeur)));
            ligne++ ;

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.ELECTRICITE))valeur += operations.get(i).getMontant() ;
            }

            // electricité
            sheet.addCell(new Label(0, ligne, context.getString(R.string.electricite)));
            sheet.addCell(new Label(1, ligne, Utiles.formatMtn(valeur)));
            ligne++ ;

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.FRAIS_PERSO))valeur += operations.get(i).getMontant() ;
            }

            // frais
            sheet.addCell(new Label(0, ligne, context.getString(R.string.frais)));
            sheet.addCell(new Label(1, ligne, Utiles.formatMtn(valeur)));
            ligne++ ;

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.AUTRES_DEP))valeur += operations.get(i).getMontant() ;
            }

            // Autres dépense
            sheet.addCell(new Label(0, ligne, context.getString(R.string.autre)));
            sheet.addCell(new Label(1, ligne, Utiles.formatMtn(valeur)));
            ligne++ ;


            double c = a-b ;

            // Marche beneficiaire
            sheet.addCell(new Label(0, ligne, context.getString(R.string.margebenef)));
            sheet.addCell(new Label(1, ligne, Utiles.formatMtn(c)));
            ligne++ ;



            // Produit financiere
            try {
                operations = operationDAO.getAll(7,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }

            double d = valeur ;

            sheet.addCell(new Label(0, ligne, context.getString(R.string.prdtfin)));
            sheet.addCell(new Label(1, ligne, Utiles.formatMtn(valeur)));
            ligne++ ;

            // Charge financiere
            try {
                operations = operationDAO.getAll(8,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }

            d -= valeur ;

            sheet.addCell(new Label(0, ligne, context.getString(R.string.chargefin)));
            sheet.addCell(new Label(1, ligne, Utiles.formatMtn(valeur)));
            ligne++ ;

            sheet.addCell(new Label(0, ligne, context.getString(R.string.margefin)));
            sheet.addCell(new Label(1, ligne, Utiles.formatMtn(d)));
            ligne++ ;

            // Produit Except
            try {
                operations = operationDAO.getAll(9,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }


            double f = valeur ;
            sheet.addCell(new Label(0, ligne, context.getString(R.string.prdtexcept)));
            sheet.addCell(new Label(1, ligne, Utiles.formatMtn(valeur)));
            ligne++ ;

            // Charge Except
            try {
                operations = operationDAO.getAll(10,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                valeur += operations.get(i).getMontant()-operations.get(i).getRemise() ;
            }

            f -= valeur ;
            sheet.addCell(new Label(0, ligne, context.getString(R.string.chargeexp)));
            sheet.addCell(new Label(1, ligne, Utiles.formatMtn(valeur)));
            ligne++ ;

            sheet.addCell(new Label(0, ligne, context.getString(R.string.margeexp)));
            sheet.addCell(new Label(1, ligne, Utiles.formatMtn(f)));
            ligne++ ;




            // Impot et taxe
            try {
                operations = operationDAO.getAll(2,DAOBase.formatter2.parse(datedebut),DAOBase.formatter2.parse(datefin),pv);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            valeur = 0 ;
            for (int i = 0; i < operations.size(); i++) {
                if (operations.get(i).getAnnuler()==1 && operations.get(i).getDateannulation().before(new Date())) continue;
                if (operations.get(i).getTypeOperation_id().equals(OperationDAO.IMPOT))valeur += operations.get(i).getMontant() ;
            }

            sheet.addCell(new Label(0, ligne, context.getString(R.string.impot)));
            sheet.addCell(new Label(1, ligne, Utiles.formatMtn(valeur)));
            ligne++ ;

            double g = c+d+f-valeur ;

            sheet.addCell(new Label(0, ligne, context.getString(R.string.benefnet)));
            sheet.addCell(new Label(1, ligne, Utiles.formatMtn(g)));
            ligne++ ;

            workbook.write();

            Toast.makeText(context, "Sauvegarder en " + path, Toast.LENGTH_LONG).show();
            try {
                workbook.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            doc.close();
        }

        viewEXCEL(name, PV, context);
    }




    private static void insertCell(PdfPTable table, String text, int align, int colspan, Font font) {

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if (text.trim().equalsIgnoreCase("")) {
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);

    }

    // Method for opening a pdf file
    private static void viewPdf(String file, String directory, Context context) {

        File pdfFile = null ;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context) ;

        Uri path = null ;
        if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
            pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        }
        else if (!preferences.getBoolean("stockage",true)){
            pdfFile = new File(context.getFilesDir() + "/" + directory + "/" + file);
        }
        else{
            Toast.makeText(context,R.string.sdabsent,Toast.LENGTH_LONG).show();
            return;
        }

        path = Uri.fromFile(pdfFile);

        //Toast.makeText(context,pdfFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();

        // Setting the intent for pdf reader
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, getMimeType(file));
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            context.startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, R.string.noapp, Toast.LENGTH_LONG).show();
        }
    }

    // Method for opening a pdf file
    private static void viewWord(String file, String directory, Context context) {

        File pdfFile = null ;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context) ;

        Uri path = null ;
        if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
            pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        }
        else if (!preferences.getBoolean("stockage",true)){
            pdfFile = new File(context.getFilesDir() + "/" + directory + "/" + file);
        }
        else{
            Toast.makeText(context,R.string.sdabsent,Toast.LENGTH_LONG).show();
            return;
        }

        path = Uri.fromFile(pdfFile);

        // Setting the intent for pdf reader
        Intent wordIntent = new Intent(Intent.ACTION_VIEW);
        wordIntent.setDataAndType(path, getMimeType(file));
        wordIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            context.startActivity(wordIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Vous ne posseder pas d'App pour lire les fichiers Word", Toast.LENGTH_LONG).show();
        }
    }

    // Method for opening a excel file
    private static void viewEXCEL(String file, String directory, Context context) {


        File excelFile = null ;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context) ;

        Uri path = null ;
        if (Utiles.isSdReadable() && preferences.getBoolean("stockage",true)){
            excelFile = new File(Environment.getExternalStorageDirectory() + "/" + directory + "/" + file);
        }
        else if (!preferences.getBoolean("stockage",true)){
            excelFile = new File(context.getFilesDir() + "/" + directory + "/" + file);
        }
        else{
            Toast.makeText(context,R.string.sdabsent,Toast.LENGTH_LONG).show();
            return;
        }

        path = Uri.fromFile(excelFile);

        // Setting the intent for pdf reader
        Intent excelIntent = new Intent(Intent.ACTION_VIEW);
        excelIntent.setDataAndType(path, getMimeType(file));
        excelIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            context.startActivity(excelIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, R.string.noappexel, Toast.LENGTH_LONG).show();
        }
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static class HeaderFooterPageEvent extends PdfPageEventHelper {

        Context c = null;
        SharedPreferences preferences = null;
        String ets = null;
        String adress = null;

        public HeaderFooterPageEvent(Context context) {
            c = context;
            preferences = PreferenceManager.getDefaultSharedPreferences(context);
            ets = preferences.getString("nomSociete", "");
            adress = preferences.getString("adresse", "");
        }

        public void onStartPage(PdfWriter writer, Document document) {
            Font ffont = new Font(Font.FontFamily.UNDEFINED, 10, Font.ITALIC);
            PdfContentByte cb = writer.getDirectContent();
            Phrase header = new Phrase(ets, ffont);
            ColumnText.showTextAligned(cb, Element.ALIGN_LEFT, header, document.left() + document.leftMargin(), document.top() + 10, 0);
            header = new Phrase(adress, ffont);
            ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT, header, document.right() - document.rightMargin(), document.top() + 10, 0);

            //ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("LEFT" +ets), 30, 800, 0);
            //ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(adress), 550, 800, 0);
        }

        /*
        public void onEndPage(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("page " + document.getPageNumber()), 550, 30, 0);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Copyright Media Soft"), 110, 30, 0);
        }
        */

        public void onEndPage(PdfWriter writer, Document document) {
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("page " + document.getPageNumber()), 550, 30, 0);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(french_format.format(new Date())), 110, 30, 0);
            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(name), 310, 30, 0);
        }

    }


}
