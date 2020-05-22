package ie.dbs.moviesapi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ie.dbs.moviesapi.R;
import ie.dbs.moviesapi.ui.activity.MovieDetailActivity;


public class CastAdapter  extends BaseAdapter {
    Context context;
    final ArrayList<Object> casts;

    public CastAdapter(Context context, ArrayList<Object> casts){
        //super(context, R.layout.single_list_app_item, utilsArrayList);
        this.context = context;
        this.casts = casts;
    }

    @Override
    public int getCount() {
        return casts.size() ;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.movie_list_item, parent, false);

            viewHolder.castName = (TextView) convertView.findViewById(R.id.md_castName);
            viewHolder.castPicture = (ImageView) convertView.findViewById(R.id.md_cast_picture);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Map cast = (HashMap)casts.get(position);


        try {
            URL url = new URL(MovieDetailActivity.applicationContext.getResources().getString(R.string.api_url)
                    +cast.get("Cast_Picture").toString());
            Bitmap thumbnail = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            viewHolder.castPicture.setImageBitmap(thumbnail);
        } catch (Exception e) {
            e.printStackTrace();
        }


        viewHolder.castName.setText(cast.get("Cast_Name").toString());


        return convertView;
    }

    private static class ViewHolder {

        TextView castName;
        ImageView castPicture;

    }

}