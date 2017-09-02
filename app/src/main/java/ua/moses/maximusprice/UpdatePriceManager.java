package ua.moses.maximusprice;

import android.os.AsyncTask;
import android.widget.Button;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//http://developer.alexanderklimov.ru/android/theory/asynctask.php
public class UpdatePriceManager extends AsyncTask<String, Integer, List<Good>> {
    private ViewsManager viewsManager;
    private Button btnUpdate;

    UpdatePriceManager(ViewsManager viewsManager, Button btnUpdate) {
        this.viewsManager = viewsManager;
        this.btnUpdate = btnUpdate;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        btnUpdate.setText(viewsManager.getContext().getString(R.string.BTN_UPDATE_LOADING_TITLE));
        btnUpdate.setClickable(false);
    }

    @Override
    protected List<Good> doInBackground(String... strings) {
        List<Good> result = new ArrayList<>();
        String link = strings[0];
        XMLManager xml = new XMLManager(link, viewsManager.getContext());
        try {
            result = xml.getGoods();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(List<Good> goods) {
        viewsManager.updateData(goods);
        btnUpdate.setText(viewsManager.getContext().getString(R.string.BTN_UPDATE_TITLE));
        btnUpdate.setClickable(true);
    }
}
