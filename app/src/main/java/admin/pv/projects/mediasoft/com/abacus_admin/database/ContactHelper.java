package admin.pv.projects.mediasoft.com.abacus_admin.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mayi on 30/10/2015.
 */
public class ContactHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "contact";

    public static final String TABLE_KEY = "_id";
    public static final String ID_EXTERNE = "id_externe";
    public static final String ETAT = "etat";
    public static final String NOM = "nom";
    public static final String PRENOM = "prenom";
    public static final String SEXE = "sexe";
    public static final String CONTACT = "contact";
    public static final String EMAIL = "email";
    public static final String ADRESSE = "adresse";
    public static final String PARTENAIRE_iD = "partenaire_id";
    public static final String CREATED_AT = "created_at";

    public static final String JOURNAL_TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    TABLE_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ID_EXTERNE + " INTEGER, " +
                    NOM + " TEXT, " +
                    PRENOM + " TEXT, " +
                    EMAIL + " TEXT, " +
                    SEXE + " TEXT, " +
                    CONTACT + " TEXT, " +
                    ADRESSE + " TEXT, " +
                    ETAT + " INTEGER, " +
                    PARTENAIRE_iD + " INTEGER, " +
                    CREATED_AT + " TEXT);";

    public static final String JOURNAL_TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    private static ContactHelper instance ;

    private ContactHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized ContactHelper getHelper(Context context, String name, int version){
        SQLiteDatabase mDb = null;

        if (instance==null) instance = new ContactHelper(context, name, null, version) ;

        try{
            mDb = instance.getWritableDatabase();
            String sql = "SELECT " + TABLE_KEY + " FROM " + TABLE_NAME;
            Cursor cursor = mDb.rawQuery(sql, null);
            cursor.close();
        }
        catch(SQLiteException s){
            mDb.execSQL(JOURNAL_TABLE_CREATE);
        }

        return instance ;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(JOURNAL_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(JOURNAL_TABLE_DROP);
        onCreate(db);
    }

}