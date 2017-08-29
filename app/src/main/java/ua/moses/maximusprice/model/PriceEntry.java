package ua.moses.maximusprice.model;

import android.provider.BaseColumns;

public final class PriceEntry implements BaseColumns {
    final static String TABLE_NAME = "pricelist";
    final static String _ID = BaseColumns._ID;
    final static String COLUMN_GOOD_ID = "good_id";
    final static String COLUMN_NAME = "name";
    final static String COLUMN_PRICE = "price";
    final static String COLUMN_GROUP = "maingroup";
    final static String COLUMN_SUBGROUP = "subgroup";
    final static String COLUMN_DESCRIPTION = "description";
    final static String COLUMN_AVALAIBILITY = "avalaibility";
}