package hosanna_techvibes.revcollectenumerator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import hosanna_techvibes.revcollectenumerator.R;
import hosanna_techvibes.revcollectenumerator.SearchTaxpayerWidget;

public class TaxpayerA extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private ArrayList<TaxpayerSearch> arrayList;

    public TaxpayerA(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
        this.arrayList = new ArrayList<TaxpayerSearch>();
        this.arrayList.addAll(SearchTaxpayerWidget.taxpayerArrayList);
    }

    public class ViewHolder{
        TextView name;
        TextView id;
    }

    @Override
    public int getCount() {
        return SearchTaxpayerWidget.taxpayerArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return SearchTaxpayerWidget.taxpayerArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent){
        final ViewHolder holder;

            if(view == null){
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.listview_item, null);
                holder.name = (TextView) view.findViewById(R.id.name);
                holder.id = (TextView) view.findViewById(R.id.rin);
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }
            holder.name.setText(SearchTaxpayerWidget.taxpayerArrayList.get(position).getName());
            holder.id.setText(SearchTaxpayerWidget.taxpayerArrayList.get(position).getT_id());
            return view;
    }

    public void filter(String charText){
        charText = charText.toLowerCase(Locale.getDefault());
        SearchTaxpayerWidget.taxpayerArrayList.clear();
        if(charText.length() == 0){
            SearchTaxpayerWidget.taxpayerArrayList.addAll(arrayList);
        }else{
            for(TaxpayerSearch t: arrayList){
                if(t.getName().toLowerCase(Locale.getDefault()).contains(charText)){
                    SearchTaxpayerWidget.taxpayerArrayList.add(t);
                }
            }
        }
        notifyDataSetChanged();
    }
}
