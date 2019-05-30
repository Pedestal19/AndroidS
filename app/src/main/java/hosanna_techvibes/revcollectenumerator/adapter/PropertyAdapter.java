package hosanna_techvibes.revcollectenumerator.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;


import java.util.ArrayList;

import hosanna_techvibes.revcollectenumerator.R;
import hosanna_techvibes.revcollectenumerator.model.Taxpayer;

/**
 * Created by Hosanna on 02/11/2016.
 */
public class PropertyAdapter extends BaseAdapter {
    private ColorGenerator mColorGenerator = ColorGenerator.MATERIAL;
    private TextDrawable.IBuilder mDrawableBuilder;
    ArrayList<Taxpayer> myList = new ArrayList<Taxpayer>();
    LayoutInflater inflater;
    Context context;

    public PropertyAdapter(Context context, ArrayList<Taxpayer> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Taxpayer getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyViewHolder mViewHolder;
        View view;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.taxpayers_list_item, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        Taxpayer currentListData = getItem(position);
        mViewHolder.tvEmpID.setText(currentListData.getTax_registration_id());
        mViewHolder.tvFullName.setText(currentListData.getSurname());
        mViewHolder.tvViewDate.setText(currentListData.getTemp_tax_id());
        if(currentListData.getTax_id() != null && !currentListData.getTax_id().equalsIgnoreCase("null") && !currentListData.getTax_id().isEmpty())
        {
            mViewHolder.tvTIN.setText("Area Name: " + currentListData.getTax_id());
        }
        else if(currentListData.getTemp_tax_id() != null && !currentListData.getTemp_tax_id().equalsIgnoreCase("null") && !currentListData.getTemp_tax_id().isEmpty())
        {
//            mViewHolder.tvTIN.setText("Area Name: " + currentListData.getTemp_tax_id());
            mViewHolder.tvTIN.setText(currentListData.getFirst_name());


        }
        else {
            mViewHolder.tvTIN.setText("");
        }

        if(currentListData.getPhoto() == null || currentListData.getPhoto().equalsIgnoreCase("null") || currentListData.getPhoto().isEmpty())
        {
            //Log.e("TaxPayerAdapter", "No image");
            char myChar = currentListData.getSurname().charAt(0);
            String str = Character.toString(myChar).toUpperCase();
            int tColor = mColorGenerator.getColor(str);
            TextDrawable drawable = TextDrawable.builder()
                    .buildRound(str, tColor);
            mViewHolder.ivIcon.setImageDrawable(drawable);
        }
        else
        {
            //Log.e("TaxPayerAdapter", "Yes image");
            byte[] decoded64String = Base64.decode(currentListData.getPhoto(), Base64.NO_WRAP);
            Bitmap bmp = BitmapFactory.decodeByteArray(decoded64String, 0, decoded64String.length);
            int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
            int screenHeight = context.getResources().getDisplayMetrics().heightPixels;
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                // Notice that width and height are reversed //screenHeight, screenWidth,
                Bitmap scaled = Bitmap.createScaledBitmap(bmp, 150, 150, true);
                int w = scaled.getWidth();
                int h = scaled.getHeight();
                // Setting post rotate to 90
                Matrix mtx = new Matrix();
                mtx.postRotate(90);
                // Rotating Bitmap
                bmp = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);
            }else{// LANDSCAPE MODE
                //No need to reverse width and height
                Bitmap scaled = Bitmap.createScaledBitmap(bmp, screenWidth, screenHeight, true);
                bmp=scaled;
            }
            mViewHolder.ivIcon.setImageBitmap(bmp);
        }

        return convertView;
    }

    private class MyViewHolder {


        TextView tvEmpID, tvFullName, tvViewDate, tvTIN;
        ImageView ivIcon;

        public MyViewHolder(View item) {
            tvEmpID = (TextView)item.findViewById(R.id.textViewEmp_id);
            tvFullName = (TextView)item.findViewById(R.id.textViewFullName);
            tvViewDate = (TextView)item.findViewById(R.id.textViewDate);
            ivIcon = (ImageView) item.findViewById(R.id.ivIcon);
            tvTIN = (TextView)item.findViewById(R.id.txt_TAXID);
        }
    }

}
