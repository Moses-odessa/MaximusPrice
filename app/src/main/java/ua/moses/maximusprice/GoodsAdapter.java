package ua.moses.maximusprice;

import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

public class GoodsAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater layoutInflater;
    private List<Good> goods;
    private ViewsManager viewsManager;

    GoodsAdapter(Context context, ViewsManager viewsManager) {
        this.context = context;
        this.goods = viewsManager.getGoods();
        this.viewsManager = viewsManager;
        layoutInflater = (LayoutInflater) this.context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return goods.size();
    }

    @Override
    public Object getItem(int position) {
        return goods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.good_list_item, parent, false);
        }
        final Good good = getGood(position);
        TextView goodTitle = (TextView) view.findViewById(R.id.goodTitle);
        goodTitle.setText(good.getName());
        ((TextView) view.findViewById(R.id.goodPrice)).setText(good.getPrice() + "");
        ((TextView) view.findViewById(R.id.goodsAvailability)).setText(good.getAvailability());
        TextView goodOrder = (TextView) view.findViewById(R.id.goodOrderQuantity);
        goodOrder.setText(good.getOrder() + "");
        if (good.getOrder() > 0) {
            goodOrder.setTextColor(ContextCompat.getColor(context, R.color.colorAccent));
        } else {
            goodOrder.setTextColor(ContextCompat.getColor(context, R.color.colorPrimaryDark));
        }

        final float[] fromPosition = {0};
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        fromPosition[0] = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        float toPosition = event.getX();
                        if (fromPosition[0] - toPosition > 20)
                            changeOrder(-1, good);
                        else if (fromPosition[0] - toPosition < -20)
                            changeOrder(1, good);
                        else showDescriptionDialogs(good.getDescription());
                    default:
                        break;
                }
                return true;
            }
        });
        
        return view;
    }

    private void showDescriptionDialogs(String description) {
        AlertDialog.Builder builder = new AlertDialog.Builder(viewsManager.getContext());
        builder.setTitle(R.string.DESCRIPTION_TITLE)
                .setMessage(description.replace(". ", ".\n"))
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

    private void changeOrder(int delta, Good good) {
        if (good.getOrder() + delta >= 0) {
            good.setOrder(good.getOrder() + delta);
            viewsManager.setOrder(good);
            viewsManager.updateCart();
            this.notifyDataSetChanged();
        }
    }

    private Good getGood(int position) {
        return ((Good) getItem(position));
    }

}
