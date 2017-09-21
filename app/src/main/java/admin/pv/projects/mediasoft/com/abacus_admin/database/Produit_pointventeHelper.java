package admin.pv.projects.mediasoft.com.abacus_admin.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mayi on 05/10/2015.
 */
public class Produit_pointventeHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "produit_pointvente";

    public static final String TABLE_KEY = "_id";
    public static final String PRODUIT_ID = "produit_id";
    public static final String POINTVENTE_ID = "pointvente_id";

    public static final String _TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    TABLE_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PRODUIT_ID + " INTEGER, " +
                    POINTVENTE_ID + " INTEGER );";

    public static final String _TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    private static Produit_pointventeHelper instance ;

    private Produit_pointventeHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized Produit_pointventeHelper getHelper(Context context, String name, int version){
        SQLiteDatabase mDb = null;

        if (instance==null) instance = new Produit_pointventeHelper(context, name, null, version) ;

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
