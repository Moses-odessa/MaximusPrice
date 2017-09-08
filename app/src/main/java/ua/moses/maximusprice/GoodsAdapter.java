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

    GoodsAdapter(Context context, List<Good> goods) {
        this.context = context;
        this.goods = goods;
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
        ((TextView) view.findViewById(R.id.goodInfo)).setText(good.getInfo());
        NumberPicker orderQuantity = (NumberPicker) view.findViewById(R.id.orderQuantity);
        orderQuantity.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                //todo запрос к базе данных на изменение количества
            }
        });
        //orderQuantity.setTag(position);
        orderQuantity.setMinValue(0);
        orderQuantity.setMaxValue(10);
        orderQuantity.setValue(good.getOrder());
        orderQuantity.setWrapSelectorWheel(false);


        return view;
    }

    Good getGood(int position) {
        return ((Good) getItem(position));
    }

}
