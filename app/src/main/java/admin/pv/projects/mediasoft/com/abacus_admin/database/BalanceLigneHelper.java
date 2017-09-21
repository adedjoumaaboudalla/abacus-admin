package admin.pv.projects.mediasoft.com.abacus_admin.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mayi on 05/10/2015.
 */
public class BalanceLigneHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "balance_ligne";

    public static final String TABLE_KEY = "_id";
    public static final String BALANCE_ID = "balance_id";
    public static final String LIGNE_ID = "ligne_id";
    public static final String SOLDE_REPORT = "solde_report";
    public static final String SOLDE_CLOTURE = "solde_cloture";
    public static final String MOUVEMENT = "mouvement";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";

    public static final String _TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    TABLE_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    BALANCE_ID + " INTEGER, " +
                    SOLDE_CLOTURE + " REAL, " +
                    SOLDE_REPORT + " REAL, " +
                    MOUVEMENT + " REAL, " +
                    LIGNE_ID + " INTEGER, " +
                    CREATED_AT + " TEXT, " +
                    UPDATED_AT + " TEXT );";

    public static final String _TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    private static BalanceLigneHelper instance ;

    private BalanceLigneHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized BalanceLigneHelper getHelper(Context context, String name, int version){
        SQLiteDatabase mDb = null;

        if (instance==null) instance = new BalanceLigneHelper(context, name, null, version) ;

        try{
            mDb = instance.getWritableDatabase();
            String sql = "SELECT " + TABLE_KEY + " FROM " + TABLE_NAME;
            Cursor cursor = mDb.rawQuery(sql, null);
            cursor.close();
        }
        catch(SQLiteException s){
            mDb.execSQL(_TABLE_CREATE);
        }

        return instance ;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(_TABLE_DROP);
        onCreate(db);
    }

}
