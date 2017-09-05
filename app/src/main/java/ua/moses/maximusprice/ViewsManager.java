package ua.moses.maximusprice;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.widget.*;

import java.text.SimpleDateFormat;
import java.util.*;

class ViewsManager {
    private ListView listGroups;
    private ListView listGoods;
    private TextView textPriceActual;
    private Button btnUpdate;
    private DataManager priceData;
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


    Button getBtnUpdate() {
        return btnUpdate;
    }

    ViewsManager(ListView listGroups, ListView listGoods, TextView textPriceActual, Button btnUpdate, Context context) {
        this.listGroups = listGroups;
        this.listGoods = listGoods;
        this.textPriceActual = textPriceActual;
        this.btnUpdate = btnUpdate;
        this.priceData = new DataManager(context);
        this.context = context;
    }

    void updateData(List<Good> goods) {
        priceData.updatePrice(goods);
        this.update();
    }

    void update() {
        updateText();
        updateGroups();
        updateGoods();

    }

    private void updateGoods() {
        ArrayList<HashMap<String, String>> goodsArray = new ArrayList<>();
        List<Good> goods = priceData.getGoods(selectedGroup, selectedSubGroup);
        for (Good good : goods) {
            HashMap<String, String> map = new HashMap<>();
            map.put("Title", good.getName());
            map.put("Info", good.getInfo());
            goodsArray.add(map);
        }
        SimpleAdapter goodsAdapter = new SimpleAdapter(context, goodsArray, R.layout.good_list_item,
                new String[]{"Title", "Info"},
                new int[]{R.id.goodTitle, R.id.goodInfo});
        listGoods.setAdapter(goodsAdapter);
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
        if (getNewDate().getTime() > getActualDate().getTime()){
            textPriceActual.setText(R.string.NEW_DATE_TITLE);
            textPriceActual.append(getFormattedDate(getNewDate()));
            textPriceActual.setTextColor(Color.RED);
        } else {
            textPriceActual.setText(R.string.ACTUAL_DATE_TITLE);
            textPriceActual.append(getFormattedDate(getActualDate()));
            textPriceActual.setTextColor(Color.BLACK);
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
}
