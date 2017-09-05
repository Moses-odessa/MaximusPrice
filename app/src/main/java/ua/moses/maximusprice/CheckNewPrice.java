package ua.moses.maximusprice;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class CheckNewPrice extends AsyncTask <String, Void, Date> {
    private ViewsManager viewsManager;

    CheckNewPrice(ViewsManager viewsManager) {
        this.viewsManager = viewsManager;
    }

    @Override
    protected Date doInBackground(String... strings) {
        try {
            URL url = new URL(strings[0]);
            HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
            return new Date(httpCon.getLastModified());
        } catch (IOException e) {
           return null;
        }
    }

    @Override
    protected void onPostExecute(Date date){
        if (date.getTime() > viewsManager.getNewDate().getTime()) {
            updateNewDate(date);
            viewsManager.updateText();
        }
    }

    private void updateNewDate(Date date) {
        SharedPreferences sPref = viewsManager.getContext().getSharedPreferences("sPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putLong(viewsManager.getContext().getString(R.string.NEW_DATE_VARIABLE), date.getTime());
        ed.commit();
    }
}
