package admin.pv.projects.mediasoft.com.abacus_admin.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mayi on 05/10/2015.
 */
public class ProduitHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "produit";

    public static final String TABLE_KEY = "_id";
    public static final String ID_EXTERNE = "id_externe";
    public static final String LIBELLE = "libelle";
    public static final String CODE = "code";
    public static final String AFFICHABLE = "affichable";
    public static final String ETAT = "etat";
    public static final String MODIFIABLE = "prixmodifiable";
    public static final String PRIXVENTE = "prixVente";
    public static final String PRIXACHAT = "prixAchat";
    public static final String IMAGE = "image";
    public static final String UNITE = "unite";
    public static final String UTILISATEUR_ID = "client_id";
    public static final String QUANTITE = "quantite";
    public static final String CATEGORIE_ID = "categorie_id";
    public static final String CREATED_AT = "created_at";

    public static final String PRODUIT_TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    TABLE_KEY + " INTEGER PRIMARY KEY, " +
                    LIBELLE + " TEXT, " +
                    CODE + " TEXT, " +
                    IMAGE + " TEXT, " +
                    AFFICHABLE + " TEXT, " +
                    ID_EXTERNE + " INTEGER, " +
                    PRIXVENTE + " INTEGER, " +
                    PRIXACHAT + " INTEGER, " +
                    ETAT + " INTEGER, " +
                    QUANTITE + " INTEGER, " +
                    MODIFIABLE + " INTEGER, " +
                    UTILISATEUR_ID + " INTEGER, " +
                    CATEGORIE_ID + " INTEGER, " +
                    UNITE + " TEXT, " +
                    CREATED_AT + " TEXT);";

    public static final String PRODUIT_TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    private static ProduitHelper instance ;

    private ProduitHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized ProduitHelper getHelper(Context context,String name, int version){
        SQLiteDatabase mDb = null;

        if (instance==null) instance = new ProduitHelper(context, name, null, version) ;

        try{
            mDb = instance.getWritableDatabase();
            String sql = "SELECT " + TABLE_KEY + " FROM " + TABLE_NAME;
            Cursor cursor = mDb.rawQuery(sql, null);
            cursor.close();
        }
        catch(SQLiteException s){
            mDb.execSQL(PRODUIT_TABLE_CREATE);
        }

        return instance ;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PRODUIT_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(PRODUIT_TABLE_DROP);
        onCreate(db);
    }
}
