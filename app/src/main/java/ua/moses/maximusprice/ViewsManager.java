package ua.moses.maximusprice;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

class ViewsManager {
    private ListView listGroups;
    private ListView listGoods;
    private TextView textPriceActual;
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


    ViewsManager(ListView listGroups, ListView listGoods, TextView textPriceActual, Context context) {
        this.listGroups = listGroups;
        this.listGoods = listGoods;
        this.textPriceActual = textPriceActual;
        this.priceData = new DataManager(context);
        this.context = context;
    }

    void updateData(List<Good> goods){
        priceData.updatePrice(goods);
        this.update();
    }

    void update(){
        //updates text
        textPriceActual.setText(R.string.ACTUAL_DATE_TITLE);
        textPriceActual.append(getActualPriceDate());

        //update groups
        String[] groupsArray = new String[1];
        if (selectedSubGroup.isEmpty()){
            groupsArray = priceData.getGroups(selectedGroup);
        } else {
            groupsArray[0] = context.getString(R.string.ROOT_DIR);
        }
        ArrayAdapter<String> groupsAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, groupsArray);
        listGroups.setAdapter(groupsAdapter);
        if (this.previousPosition > 0 && this.selectedGroup.isEmpty()){
            listGroups.setSelection(this.previousPosition);
            this.previousPosition = 0;
        }

        //update goods
        ArrayList<HashMap<String, String>> goodsArray = new ArrayList<>();
        List<Good> goods = priceData.getGoods(selectedGroup, selectedSubGroup);
        for (Good good : goods){
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

    private String getActualPriceDate() {
        String result;
        SharedPreferences sPref = context.getSharedPreferences("sPref", MODE_PRIVATE);
        result = sPref.getString(context.getString(R.string.ACTUAL_DATE_VARIABLE), "");
        return result;
    }

}
