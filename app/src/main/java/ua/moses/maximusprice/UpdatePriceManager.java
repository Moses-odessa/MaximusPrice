package ua.moses.maximusprice;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

//http://developer.alexanderklimov.ru/android/theory/asynctask.php наглядная картинка
public class UpdatePriceManager extends AsyncTask<String, Integer, List<Good>> {
    private ViewsManager viewsManager;
    private Boolean sameDate = false;
    private String errorString;

    UpdatePriceManager(ViewsManager viewsManager) {
        this.viewsManager = viewsManager;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        viewsManager.getTextPriceActual().setText(viewsManager.getContext().getString(R.string.PRICE_LOADING_TITLE));
        viewsManager.getTextPriceActual().setClickable(false);
    }

    @Override
    protected List<Good> doInBackground(String... strings) {
        List<Good> result = null;
        try {
            RemoteXMLManager xml = new RemoteXMLManager(getLink());
            if (!viewsManager.getActualDate().equals(getNewDate())) {
                result = xml.getGoods();
                updateActualDate(getNewDate());
            } else {
                sameDate = true;
            }
        } catch (IOException e) {
            errorString = e.getMessage();
        }
        return result;
    }

    @Override
    protected void onPostExecute(List<Good> goods) {
        if (goods != null && goods.size() > 0) {
            viewsManager.updatePrice(goods);
            Toast.makeText(viewsManager.getContext(),
                    viewsManager.getContext().getText(R.string.PRICE_WAS_UPDATED_MESSAGE) + viewsManager.getFormattedDate(viewsManager.getActualDate()),
                    Toast.LENGTH_SHORT).show();
        } else {
            if (sameDate) {
                Toast.makeText(viewsManager.getContext(), viewsManager.getContext().getText(R.string.SAME_DATE_WARNING), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(viewsManager.getContext(), viewsManager.getContext().getText(R.string.ERROR_CONNECTION) + errorString, Toast.LENGTH_SHORT).show();
            }
            viewsManager.updateText();
            viewsManager.updateCart();
        }
        viewsManager.getTextPriceActual().setClickable(true);
    }

    private void updateActualDate(Date date) {
        SharedPreferences sPref = viewsManager.getContext().getSharedPreferences("sPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putLong(viewsManager.getContext().getString(R.string.ACTUAL_DATE_VARIABLE), date.getTime());
        ed.commit();
    }

    private Date getNewDate() throws IOException {
        URL url = new URL(getLink());
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        return new Date(httpCon.getLastModified());
    }

    private String getLink() {
        return viewsManager.getContext().getString(R.string.PRICE_LINK);
    }
}
