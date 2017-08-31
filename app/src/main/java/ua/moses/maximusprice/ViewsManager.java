package ua.moses.maximusprice;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
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
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, groupsArray);
        listGroups.setAdapter(adapter);
        //update goods todo custom layout
        List<String> goodsArray = new ArrayList<>();
        List<Good> goods = priceData.getGoods(selectedGroup, selectedSubGroup);
        for (Good good : goods){
            goodsArray.add(good.toString());
        }
        adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_2, goodsArray);
        listGoods.setAdapter(adapter);
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
}
