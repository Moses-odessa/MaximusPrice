package ua.moses.maximusprice;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class ViewsManager {
    private ListView listGroups;
    private ListView listGoods;
    private DataManager priceData;
    private Context context;
    private String selectedGroup = "";
    private String selectedSubGroup = "";


    void setSelectedGroup(String selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    void setSelectedSubGroup(String selectedSubGroup) {
        this.selectedSubGroup = selectedSubGroup;
    }


    ViewsManager(ListView listGroups, ListView listGoods, Context context) {
        this.listGroups = listGroups;
        this.listGoods = listGoods;
        this.priceData = new DataManager(context);
        this.context = context;
    }

    void updateData(List<Good> goods){
        priceData.updatePrice(goods);
        this.update();
    }

    void update(){
        //update groups
        String[] groupsArray = new String[1];
        if (selectedSubGroup.isEmpty()){
            groupsArray = priceData.getGroups(selectedGroup);
        } else {
            groupsArray[0] = "..";
        }
        ArrayAdapter<String> groupsAdapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, groupsArray);
        listGroups.setAdapter(groupsAdapter);
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
}
