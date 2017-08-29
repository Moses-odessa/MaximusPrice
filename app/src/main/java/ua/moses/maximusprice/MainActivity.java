package ua.moses.maximusprice;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import ua.moses.maximusprice.model.Good;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btnUpdate = (Button) findViewById(R.id.btnUpdate);
        final Button btnToHome = (Button) findViewById(R.id.btnToHome);
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

        btnToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!updator.getSelectedSubGroup().isEmpty()) {
                    updator.setSelectedSubGroup("");
                    v.setClickable(false);
                } else {
                    updator.setSelectedGroup("");
                }
                updator.update();
            }
        });

        listGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                String selectedGroup = ((TextView) itemClicked).getText().toString();
                if (updator.getSelectedGroup().isEmpty()){
                    updator.setSelectedGroup(selectedGroup);
                } else {
                    updator.setSelectedSubGroup(selectedGroup);
                }
                updator.update();
                btnToHome.setClickable(true);
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
