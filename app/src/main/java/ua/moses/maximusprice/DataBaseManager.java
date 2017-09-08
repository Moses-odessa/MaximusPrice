package ua.moses.maximusprice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataBaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "price.db";
    private static final int DATABASE_VERSION = 2;
    private Context context;

    DataBaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }


    String[] getGroups(String parentGroup) {
        parentGroup = parentGroup.replaceAll("\'", "\'\'");
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql;
        String columnName;
        if (parentGroup.isEmpty()) {
            sql = "SELECT DISTINCT "
                    + DataPriceEntry.COLUMN_GROUP
                    + " FROM "
                    + DataPriceEntry.TABLE_NAME
                    + " ORDER BY "
                    + DataPriceEntry._ID;
            columnName = DataPriceEntry.COLUMN_GROUP;
        } else {
            sql = "SELECT '" + context.getString(R.string.ROOT_DIR) + "' as " + DataPriceEntry.COLUMN_SUBGROUP
                    + " UNION SELECT DISTINCT "
                    + DataPriceEntry.COLUMN_SUBGROUP
                    + " FROM "
                    + DataPriceEntry.TABLE_NAME
                    + " WHERE "
                    + DataPriceEntry.COLUMN_GROUP + "='" + parentGroup +"' AND "
                    + DataPriceEntry.COLUMN_SUBGROUP + "<>''"
                    + " ORDER BY 1";
            columnName = DataPriceEntry.COLUMN_SUBGROUP;
        }
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex(columnName)));
        }
        cursor.close();
        db.close();
        String[] result = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            result[i] = list.get(i);
        }

        return result;
    }

    List<Good> getGoods(String group, String subGroup) {
        List<Good> result = new ArrayList<>();
        group = group.replaceAll("\'", "\'\'");
        subGroup = subGroup.replaceAll("\'", "\'\'");
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * "
                + " FROM "
                + DataPriceEntry.TABLE_NAME
                + " WHERE "
                + DataPriceEntry.COLUMN_GROUP + "='" + group + "' and "
                + DataPriceEntry.COLUMN_SUBGROUP + "='" + subGroup + "'"
                + " ORDER BY "
                + DataPriceEntry._ID;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Good good = new Good();
            good.setId(cursor.getInt(cursor.getColumnIndex(DataPriceEntry.COLUMN_GOOD_ID)));
            good.setName(cursor.getString(cursor.getColumnIndex(DataPriceEntry.COLUMN_NAME)));
            good.setPrice(cursor.getInt(cursor.getColumnIndex(DataPriceEntry.COLUMN_PRICE)));
            good.setGroup(cursor.getString(cursor.getColumnIndex(DataPriceEntry.COLUMN_GROUP)));
            good.setSubGroup(cursor.getString(cursor.getColumnIndex(DataPriceEntry.COLUMN_SUBGROUP)));
            good.setDescription(cursor.getString(cursor.getColumnIndex(DataPriceEntry.COLUMN_DESCRIPTION)));
            good.setOrder(cursor.getInt(cursor.getColumnIndex(DataPriceEntry.COLUMN_ORDER)));
            good.setAvailability(cursor.getString(cursor.getColumnIndex(DataPriceEntry.COLUMN_AVALAIBILITY)));
            result.add(good);
        }
        cursor.close();
        db.close();
        return result;
    }

    void updatePrice(List<Good> goods) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DataPriceEntry.TABLE_NAME, null, null);
        for (Good good : goods){
            addGood(db, good);
        }
        db.close();
    }

    String getDescription(String goodName) {
        String result = "";
        goodName = goodName.replaceAll("\'", "\'\'");
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT " + DataPriceEntry.COLUMN_DESCRIPTION
                + " FROM "
                + DataPriceEntry.TABLE_NAME
                + " WHERE "
                + DataPriceEntry.COLUMN_NAME + "='" + goodName + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            result = cursor.getString(cursor.getColumnIndex(DataPriceEntry.COLUMN_DESCRIPTION));
        }
        cursor.close();
        db.close();
        return result;

    }

    private void addGood( SQLiteDatabase db, Good good) {
        ContentValues values = new ContentValues();
        values.put(DataPriceEntry.COLUMN_GOOD_ID, good.getId());
        values.put(DataPriceEntry.COLUMN_NAME, good.getName());
        values.put(DataPriceEntry.COLUMN_PRICE, good.getPrice());
        values.put(DataPriceEntry.COLUMN_GROUP, good.getGroup());
        values.put(DataPriceEntry.COLUMN_SUBGROUP, good.getSubGroup());
        values.put(DataPriceEntry.COLUMN_DESCRIPTION, good.getDescription());
        values.put(DataPriceEntry.COLUMN_AVALAIBILITY, good.getAvailability());
        values.put(DataPriceEntry.COLUMN_ORDER, good.getOrder());

        db.insert(DataPriceEntry.TABLE_NAME, null, values);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PRICE_TABLE = "CREATE TABLE " + DataPriceEntry.TABLE_NAME + " ("
                + DataPriceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DataPriceEntry.COLUMN_GOOD_ID + " INTEGER NOT NULL DEFAULT 0, "
                + DataPriceEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + DataPriceEntry.COLUMN_PRICE + " REAL NOT NULL, "
                + DataPriceEntry.COLUMN_GROUP + " TEXT NOT NULL, "
                + DataPriceEntry.COLUMN_SUBGROUP + " TEXT NOT NULL, "
                + DataPriceEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, "
                + DataPriceEntry.COLUMN_ORDER + " INTEGER, "
                + DataPriceEntry.COLUMN_AVALAIBILITY + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_PRICE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + DataPriceEntry.TABLE_NAME);
        onCreate(db);
    }
}
