package ua.moses.maximusprice;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import ua.moses.maximusprice.model.DataManager;
import ua.moses.maximusprice.model.DataManagerSQLite;
import ua.moses.maximusprice.model.Good;

import java.util.ArrayList;
import java.util.List;

public class UpdateLists {
    private ListView listGroups;
    private ListView listGoods;
    private DataManager priceData;
    private Context context;
    private String selectedGroup = "";
    private String selectedSubGroup = "";


    public void setSelectedGroup(String selectedGroup) {
        this.selectedGroup = selectedGroup;
    }

    public void setSelectedSubGroup(String selectedSubGroup) {
        this.selectedSubGroup = selectedSubGroup;
    }


    public UpdateLists(ListView listGroups, ListView listGoods, Context context) {
        this.listGroups = listGroups;
        this.listGoods = listGoods;
        this.priceData = new DataManagerSQLite(context);
        this.context = context;
    }

    public void update(){
        //update groups
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, priceData.getGroups(selectedGroup));
        listGroups.setAdapter(adapter);
        //update goods todo custom layout
        List<String> goodsArray = new ArrayList<>();
        List<Good> goods = priceData.getGoods(selectedGroup, selectedSubGroup);
        for (Good good : goods){
            goodsArray.add(good.getName() + " - " + good.getPrice());
        }
        adapter = new ArrayAdapter<>(context,
                android.R.layout.simple_list_item_1, goodsArray);
        listGoods.setAdapter(adapter);
    }

    public Context getContext() {
        return context;
    }

    public String getSelectedGroup() {
        return selectedGroup;
    }

    public String getSelectedSubGroup() {
        return selectedSubGroup;
    }
}
