package ua.moses.maximusprice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;

import java.util.Timer;
import java.util.TimerTask;

//todo выход из группы по кнопке "назад"  - не уверен что нужно
//todo формирование и отправка заказа
//todo оповещение о наличии новых остатков таймер и запус асинхронной проги

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Button btnUpdate = (Button) findViewById(R.id.btnUpdate);
        final ListView listGroups = (ListView) findViewById(R.id.listGroups);
        final ListView listGoods = (ListView) findViewById(R.id.listGoods);
        final TextView textPriceActual = (TextView) findViewById(R.id.textPriceActual);

        final ViewsManager viewsManager = new ViewsManager(listGroups, listGoods, textPriceActual, btnUpdate, this);
        viewsManager.update();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewsManager.updatePrice();
            }
        });

        listGroups.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                String selectedGroup = ((TextView) itemClicked).getText().toString();
                if (selectedGroup.equalsIgnoreCase(getString(R.string.ROOT_DIR)) && !viewsManager.getSelectedSubGroup().isEmpty()) {
                    viewsManager.setSelectedSubGroup("");
                } else if (selectedGroup.equalsIgnoreCase(getString(R.string.ROOT_DIR)) && viewsManager.getSelectedSubGroup().isEmpty()) {
                    viewsManager.setSelectedGroup("");
                } else if (viewsManager.getSelectedGroup().isEmpty()) {
                    viewsManager.setPreviousPosition(position);
                    viewsManager.setSelectedGroup(selectedGroup);
                } else if (viewsManager.getSelectedSubGroup().isEmpty()) {
                    viewsManager.setSelectedSubGroup(selectedGroup);
                }
                viewsManager.update();
            }
        });

        listGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                String selectedGoods = ((TextView) itemClicked.findViewById(R.id.goodTitle)).getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.DESCRIPTION_TITLE)
                        .setMessage(viewsManager.getGoodsDescription(selectedGoods).replace(". ", ".\n"))
                        .setCancelable(false)
                        .setNegativeButton(R.string.BTN_OK,
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                AlertDialog infoBox = builder.create();
                infoBox.show();
                TextView textInfo = (TextView) infoBox.findViewById(android.R.id.message);
                textInfo.setTextSize(14);
            }
        });

        //Timer
        final Handler uiHandler = new Handler();
        Timer checkUpdateTimer = new Timer();
        checkUpdateTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        new CheckNewPrice(viewsManager).execute(getString(R.string.PRICE_LINK));
                    }
                });
            }
        }, 0L, 60L * 1000);
    }
}
