package ua.moses.maximusprice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "price.db";
    private static final int DATABASE_VERSION = 1;

    DataManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    String[] getGroups(String parentGroup) {
        parentGroup = parentGroup.replaceAll("\'", "\'\'");
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql;
        String columnName;
        if (parentGroup.isEmpty()) {
            sql = "SELECT DISTINCT "
                    + PriceEntry.COLUMN_GROUP
                    + " FROM "
                    + PriceEntry.TABLE_NAME
                    + " ORDER BY "
                    + PriceEntry._ID;
            columnName = PriceEntry.COLUMN_GROUP;
        } else {
            sql = "SELECT '..' as " + PriceEntry.COLUMN_SUBGROUP
                    + " UNION SELECT DISTINCT "
                    + PriceEntry.COLUMN_SUBGROUP
                    + " FROM "
                    + PriceEntry.TABLE_NAME
                    + " WHERE "
                    + PriceEntry.COLUMN_GROUP + "='" + parentGroup +"' AND "
                    + PriceEntry.COLUMN_SUBGROUP + "<>''"
                    + " ORDER BY 1";
            columnName = PriceEntry.COLUMN_SUBGROUP;
        }
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            list.add(cursor.getString(cursor.getColumnIndex(columnName)));
        }
        cursor.close();
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
                + PriceEntry.TABLE_NAME
                + " WHERE "
                + PriceEntry.COLUMN_GROUP + "='" + group + "' and "
                + PriceEntry.COLUMN_SUBGROUP + "='" + subGroup + "'"
                + " ORDER BY "
                + PriceEntry._ID;
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()) {
            Good good = new Good();
            good.setId(cursor.getInt(cursor.getColumnIndex(PriceEntry.COLUMN_GOOD_ID)));
            good.setName(cursor.getString(cursor.getColumnIndex(PriceEntry.COLUMN_NAME)));
            good.setPrice(cursor.getInt(cursor.getColumnIndex(PriceEntry.COLUMN_PRICE)));
            good.setGroup(cursor.getString(cursor.getColumnIndex(PriceEntry.COLUMN_GROUP)));
            good.setSubGroup(cursor.getString(cursor.getColumnIndex(PriceEntry.COLUMN_SUBGROUP)));
            good.setDescription(cursor.getString(cursor.getColumnIndex(PriceEntry.COLUMN_DESCRIPTION)));
            good.setAvailability(cursor.getString(cursor.getColumnIndex(PriceEntry.COLUMN_AVALAIBILITY)));
            result.add(good);
        }
        cursor.close();

        return result;
    }

    void updatePrice(List<Good> goods) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PriceEntry.TABLE_NAME, null, null);
        for (Good good : goods){
            addGood(db, good);
        }
    }

    public String getDescription(int goodID) {
        return null;  //todo
    }

    private void addGood( SQLiteDatabase db, Good good) {
        ContentValues values = new ContentValues();
        values.put(PriceEntry.COLUMN_GOOD_ID, good.getId());
        values.put(PriceEntry.COLUMN_NAME, good.getName());
        values.put(PriceEntry.COLUMN_PRICE, good.getPrice());
        values.put(PriceEntry.COLUMN_GROUP, good.getGroup());
        values.put(PriceEntry.COLUMN_SUBGROUP, good.getSubGroup());
        values.put(PriceEntry.COLUMN_DESCRIPTION, good.getDescription());
        values.put(PriceEntry.COLUMN_AVALAIBILITY, good.getAvailability());

        db.insert(PriceEntry.TABLE_NAME, null, values);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_PRICE_TABLE = "CREATE TABLE " + PriceEntry.TABLE_NAME + " ("
                + PriceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PriceEntry.COLUMN_GOOD_ID + " INTEGER NOT NULL DEFAULT 0, "
                + PriceEntry.COLUMN_NAME + " TEXT NOT NULL, "
                + PriceEntry.COLUMN_PRICE + " REAL NOT NULL, "
                + PriceEntry.COLUMN_GROUP + " TEXT NOT NULL, "
                + PriceEntry.COLUMN_SUBGROUP + " TEXT NOT NULL, "
                + PriceEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, "
                + PriceEntry.COLUMN_AVALAIBILITY + " TEXT NOT NULL);";
        db.execSQL(SQL_CREATE_PRICE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);
        db.execSQL("DROP TABLE IF IT EXISTS " + PriceEntry.TABLE_NAME);
        onCreate(db);

    }
}
