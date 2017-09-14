package ua.moses.maximusprice;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {
    private ViewsManager viewsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView listGroups = (ListView) findViewById(R.id.listGroups);
        final ListView listGoods = (ListView) findViewById(R.id.listGoods);
        final TextView textPriceActual = (TextView) findViewById(R.id.textPriceActual);
        final TextView textOrderCartInfo = (TextView) findViewById(R.id.textOrderCartInfo);

        viewsManager = new ViewsManager(listGroups, listGoods, textPriceActual, textOrderCartInfo, this);
        viewsManager.update();

        textPriceActual.setOnClickListener(new View.OnClickListener() {
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
                viewsManager.updateDataViews();
            }
        });

        textOrderCartInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog orderDialog = new AlertDialog.Builder(MainActivity.this).create();
                orderDialog.setTitle(R.string.ORDER_CART_TITLE);
                final String orderString = String.format(viewsManager.getOrder().toString(), getString(R.string.ORDER_TOTAL_TITLE));
                orderDialog.setMessage(orderString);
                orderDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.BTN_SEND), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.putExtra(Intent.EXTRA_TEXT, orderString);
                        startActivity(Intent.createChooser(share, getString(R.string.ORDER_SEND_TILE)));
                        viewsManager.clearOrder();
                        viewsManager.updateCart();
                        viewsManager.updateGoods();
                    }
                });
                orderDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.BTN_CANCEL), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //nothing
                    }
                });

                orderDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.BTN_CLEAR), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        viewsManager.clearOrder();
                        viewsManager.updateCart();
                        viewsManager.updateGoods();
                    }
                });
                orderDialog.show();
                TextView textInfo = (TextView) orderDialog.findViewById(android.R.id.message);
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
        }, 0L, 60L * 10000);
    }

    @Override
    public void onBackPressed() {
        if (!viewsManager.getSelectedSubGroup().isEmpty()) {
            viewsManager.setSelectedSubGroup("");
            viewsManager.updateDataViews();
        } else if (!viewsManager.getSelectedGroup().isEmpty() && viewsManager.getSelectedSubGroup().isEmpty()) {
            viewsManager.setSelectedGroup("");
            viewsManager.updateDataViews();
        } else {
            new AlertDialog.Builder(this)
                    .setMessage(R.string.EXIT_MESSAGE)
                    .setPositiveButton(R.string.BTN_OK, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.BTN_CANCEL, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //nothing
                        }
                    })
                    .show();

        }
    }
}
