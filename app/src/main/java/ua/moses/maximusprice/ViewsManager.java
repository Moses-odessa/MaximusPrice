package ua.moses.maximusprice;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.*;

class ViewsManager {
    private ListView listGroups;
    private ListView listGoods;
    private TextView textPriceActual;
    private TextView textOrderCartInfo;
    private DataBaseManager priceData;
    private Context context;
    private String selectedGroup = "";
    private String selectedSubGroup = "";
    private int previousPosition = 0;


    void setSelectedGroup(String selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    void setSelectedSubGroup(String selectedSubGroup) {
        this.selectedSubGroup = selectedSubGroup;
    }

    ViewsManager(ListView listGroups, ListView listGoods, TextView textPriceActual, TextView textOrderCartInfo, Context context) {
        this.listGroups = listGroups;
        this.listGoods = listGoods;
        this.textPriceActual = textPriceActual;
        this.textOrderCartInfo = textOrderCartInfo;
        this.priceData = new DataBaseManager(context);
        this.context = context;
    }

    void updatePrice(List<Good> goods) {
        priceData.updatePrice(goods);
        this.update();
    }

    void update() {
        updateText();
        updateCart();
        updateDataViews();

    }

    void updateCart() {
        Order cart = priceData.getOrder();
        if (cart.getOrderQuantity() > 0) {
            String text = String.format(context.getString(R.string.ORDER_CART_LABEL), cart.getOrderQuantity(), cart.getOrderSumm());
            textOrderCartInfo.setText(text);
        } else {
            textOrderCartInfo.setText("");
        }
    }

    void updateDataViews() {
        updateGroups();
        updateGoods();
    }

    void updateGoods() {
        listGoods.setAdapter(new GoodsAdapter(context, this));
    }

    private void updateGroups() {
        String[] groupsArray = new String[1];
        if (selectedSubGroup.isEmpty()) {
            groupsArray = priceData.getGroups(selectedGroup);
        } else {
            groupsArray[0] = context.getString(R.string.ROOT_DIR);
        }
        ArrayAdapter<String> groupsAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, groupsArray);
        listGroups.setAdapter(groupsAdapter);
        if (this.previousPosition > 0 && this.selectedGroup.isEmpty()) {
            listGroups.setSelection(this.previousPosition);
            this.previousPosition = 0;
        }
    }

    void updateText() {
        if (getNewDate().getTime() > getActualDate().getTime()) {
            textPriceActual.setText(String.format(context.getString(R.string.NEW_DATE_TITLE), getFormattedDate(getNewDate())));
            textPriceActual.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent));

        } else {
            textPriceActual.setText(String.format(context.getString(R.string.ACTUAL_DATE_TITLE), getFormattedDate(getActualDate())));
            textPriceActual.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }
    }

    Context getContext() {
        return context;
    }

    String getSelectedGroup() {
        return selectedGroup;
    }

    String getSelectedSubGroup() {
        return selectedSubGroup;
    }

    String getGoodsDescription(String selectedGoods) {
        return priceData.getDescription(selectedGoods);
    }

    void setPreviousPosition(int previousPosition) {
        this.previousPosition = previousPosition;
    }

    void updatePrice() {
        new UpdatePriceManager(this).execute(context.getString(R.string.PRICE_LINK));
    }

    Date getActualDate() {
        SharedPreferences sPref = context.getSharedPreferences("sPref", Context.MODE_PRIVATE);
        return new Date(sPref.getLong(context.getString(R.string.ACTUAL_DATE_VARIABLE), 0));
    }

    Date getNewDate() {
        SharedPreferences sPref = context.getSharedPreferences("sPref", Context.MODE_PRIVATE);
        return new Date(sPref.getLong(context.getString(R.string.NEW_DATE_VARIABLE), 0));
    }

    String getFormattedDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return dateFormat.format(date);
    }

    TextView getTextPriceActual() {
        return textPriceActual;
    }

    List<Good> getGoods() {
        return priceData.getGoods(selectedGroup, selectedSubGroup);
    }

    void setOrder(Good good) {
        priceData.setOrder(good);
    }

    Order getOrder() {
        return priceData.getOrder();
    }

    void clearOrder() {
        priceData.clearOrder();
    }
}
