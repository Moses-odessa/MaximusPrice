package ua.moses.maximusprice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btnUpdate = (Button) findViewById(R.id.btnUpdate);
        final ListView listGroups = (ListView) findViewById(R.id.listGroups);
        final ListView listGoods = (ListView) findViewById(R.id.listGoods);
        final UpdateLists updator = new UpdateLists(listGroups, listGoods, this);
        updator.update();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UpdatePrice(updator, btnUpdate).execute(getString(R.string.PRICE_LINK));
            }
        });

        listGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                String selectedGroup = ((TextView) itemClicked).getText().toString();
                if (selectedGroup.equalsIgnoreCase("..") && !updator.getSelectedSubGroup().isEmpty()){
                    updator.setSelectedSubGroup("");
                } else if (selectedGroup.equalsIgnoreCase("..") && updator.getSelectedSubGroup().isEmpty()){
                    updator.setSelectedGroup("");
                } else if (updator.getSelectedGroup().isEmpty()) {
                    updator.setSelectedGroup(selectedGroup);
                } else if (updator.getSelectedSubGroup().isEmpty()) {
                    updator.setSelectedSubGroup(selectedGroup);
                }
                updator.update();
            }
        });

        listGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                //todo ge toast with description
            }
        });
    }

}
