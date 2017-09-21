package admin.pv.projects.mediasoft.com.abacus_admin.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Mayi on 05/10/2015.
 */
public class LigneHelper extends SQLiteOpenHelper{

    public static final String TABLE_NAME = "ligne";

    public static final String TABLE_KEY = "_id";
    public static final String CODE = "code";
    public static final String NATURE = "nature";
    public static final String LIBELLE = "libelle";
    public static final String ORDRE = "ordre";
    public static final String CREATED_AT = "created_at";
    public static final String UPDATED_AT = "updated_at";

    public static final String LIGNE_TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    TABLE_KEY + " INTEGER PRIMARY KEY , " +
                    CODE + " TEXT, " +
                    NATURE + " TEXT, " +
                    LIBELLE + " TEXT, " +
                    ORDRE + " INTEGER, " +
                    CREATED_AT + " TEXT, " +
                    UPDATED_AT + " TEXT);";

    public static final String LIGNE_TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    private static LigneHelper instance ;

    private LigneHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized LigneHelper getHelper(Context context, String name, int version){
        SQLiteDatabase mDb = null;

        if (instance==null) instance = new LigneHelper(context, name, null, version) ;

        try{
            mDb = instance.getWritableDatabase();
            String sql = "SELECT " + TABLE_KEY + " FROM " + TABLE_NAME;
            Cursor cursor = mDb.rawQuery(sql, null);
            cursor.close();
        }
        catch(SQLiteException s){
            mDb.execSQL(LIGNE_TABLE_CREATE);
        }

        return instance ;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LIGNE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(LIGNE_TABLE_DROP);
        onCreate(db);
    }

}
