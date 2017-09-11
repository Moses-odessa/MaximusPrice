package ua.moses.maximusprice;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
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
        ((TextView) view.findViewById(R.id.goodTitle)).setText(good.getName());
        ((TextView) view.findViewById(R.id.goodPrice)).setText(good.getPrice() + "");
        ((TextView) view.findViewById(R.id.goodsAvailability)).setText(good.getAvailability());
        ((TextView) view.findViewById(R.id.goodOrderQuantity)).setText(good.getOrder() + "");
        TextView orderPlus = (TextView) view.findViewById(R.id.goodOrderPlus);
        TextView orderMinus = (TextView) view.findViewById(R.id.goodOrderMinus);
        orderPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeOrder(1, good);
            }
        });
        orderMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeOrder(-1, good);
            }
        });
        
        return view;
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
