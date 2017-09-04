package ua.moses.maximusprice;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//http://developer.alexanderklimov.ru/android/theory/asynctask.php
public class UpdatePriceManager extends AsyncTask<String, Integer, List<Good>> {
    private ViewsManager viewsManager;
    private Boolean sameDate = false;

    UpdatePriceManager(ViewsManager viewsManager) {
        this.viewsManager = viewsManager;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        viewsManager.getBtnUpdate().setText(viewsManager.getContext().getString(R.string.BTN_UPDATE_LOADING_TITLE));
        viewsManager.getBtnUpdate().setClickable(false);
    }

    @Override
    protected List<Good> doInBackground(String... strings) {
        List<Good> result = null;
        RemoteXMLManager xml = new RemoteXMLManager(getLink());
        try {
            if (!viewsManager.getSavedDate().equals(getNewDate())) {
                result = xml.getGoods();
                updateActualDate(getNewDate());
            } else {
                sameDate = true;
            }
        } catch (IOException e) {
            //do nothing
        }
        return result;
    }

    @Override
    protected void onPostExecute(List<Good> goods) {
        if (goods != null && goods.size() > 0) {
            viewsManager.updateData(goods);
            Toast.makeText(viewsManager.getContext(),
                    viewsManager.getContext().getText(R.string.PRICE_WAS_UPDATED_MESSAGE) + viewsManager.getFormattedDate(viewsManager.getSavedDate()),
                    Toast.LENGTH_SHORT).show();
        } else {
            if (sameDate) {
                Toast.makeText(viewsManager.getContext(), viewsManager.getContext().getText(R.string.SAME_DATE_WARNING), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(viewsManager.getContext(), viewsManager.getContext().getText(R.string.ERROR_CONNECTION), Toast.LENGTH_SHORT).show();
            }
        }
        viewsManager.getBtnUpdate().setText(viewsManager.getContext().getString(R.string.BTN_UPDATE_TITLE));
        viewsManager.getBtnUpdate().setClickable(true);
    }

    private void updateActualDate(Date date) {
        SharedPreferences sPref = viewsManager.getContext().getSharedPreferences("sPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putLong(viewsManager.getContext().getString(R.string.ACTUAL_DATE_VARIABLE), date.getTime());
        ed.commit();
    }

    private Date getNewDate() throws IOException {
        URL obj = new URL(getLink());
        URLConnection conn = obj.openConnection();
        Map<String, List<String>> map = conn.getHeaderFields();
        List<String> lastModified = map.get("Last-Modified");
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.getDefault());
        try {
            return dateFormat.parse(lastModified.get(0));
        } catch (ParseException | IndexOutOfBoundsException e) {
            throw new IOException(e.getMessage());
        }
    }

    private String getLink() {
        return viewsManager.getContext().getString(R.string.PRICE_LINK);
    }
}
