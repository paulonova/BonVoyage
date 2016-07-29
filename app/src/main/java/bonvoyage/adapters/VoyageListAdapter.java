package bonvoyage.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import bonvoyage.database.DatabaseHelper;
import bonvoyage.objects.Voyage;
import se.paulo.nackademin.examen.bonvoyage.R;

/**
 * Created by Paulo Vila Nova on 2016-07-18.
 */
public class VoyageListAdapter extends SimpleAdapter{

    private List<? extends Map<String, ?>> data;
    Context context;
    DatabaseHelper helper;


    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */
    public VoyageListAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.data = data;
        this.context = context;
    }

    // Ett sätt att optimera ListView ytterligare
    static class MyViewHolder {
        ImageView voyageType;
        TextView placeToGo, dateToGo, budgetYouHave;
        ProgressBar progressBar;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View itemView;
        MyViewHolder myViewHolder = new MyViewHolder();

        if(convertView == null){
            itemView = layoutInflater.inflate(R.layout.voyage_list_modell, parent, false);
            myViewHolder.voyageType = (ImageView)itemView.findViewById(R.id.img_type_voyage);
            myViewHolder.placeToGo = (TextView)itemView.findViewById(R.id.txtPlaceYouGo);
            myViewHolder.dateToGo = (TextView)itemView.findViewById(R.id.txtDateYouGo);
            myViewHolder.budgetYouHave = (TextView)itemView.findViewById(R.id.txtBudgetYouHave);

            myViewHolder.progressBar = (ProgressBar)itemView.findViewById(R.id.progressBar);
            //itemView.setTag(myViewHolder);

        }else {
            itemView = convertView;
        }


        return itemView;
    }




    public void remove(long position) {
        if (position >= 0 && data.size() < position && data.get((int) position) != null) {
            data.remove(position);
        }
    }

}
