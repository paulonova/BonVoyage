package bonvoyage.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import bonvoyage.database.DatabaseHelper;
import bonvoyage.objects.Voyage;
import se.paulo.nackademin.examen.bonvoyage.R;
import se.paulo.nackademin.examen.bonvoyage.SpendListActivity;
import se.paulo.nackademin.examen.bonvoyage.VoyageListActivity;

/**
 * Created by Paulo Vila Nova on 2016-07-18.
 */
public class VoyageListAdapter extends RecyclerView.Adapter<VoyageListAdapter.VoyageHolder> {

    private List<Voyage> listData;
    private LayoutInflater inflater;
    private Context context;

    //Creating an Interface..
    private ItemClickCallBack itemClickCallBack;

    public interface ItemClickCallBack{
        void onItemClick(int p);
    }

    public void setItemClickCallBack(ItemClickCallBack itemClickCallBack) {
        this.itemClickCallBack = itemClickCallBack;
    }

    public final int IMAGE_VACATION = R.drawable.photo_vacation;
    public final int IMAGE_BUSINESS = R.drawable.photo_business;

    public VoyageListAdapter(List<Voyage> listData, Context c) {
        this.listData = listData;
        this.inflater = LayoutInflater.from(c);
        this.context = c;
    }

    @Override
    public VoyageHolder onCreateViewHolder(ViewGroup parent, final int viewType) {

        View view = inflater.inflate(R.layout.voyage_list_modell, parent, false);

        return new VoyageHolder(view);
    }


    @Override
    public void onBindViewHolder(VoyageHolder holder, final int position) {
        Voyage item = listData.get(position);


        if(item.getTypeVoyage().contains("Vacation")){
            holder.voyageType.setImageResource(IMAGE_VACATION);
        }else{
            holder.voyageType.setImageResource(IMAGE_BUSINESS);
        }
        holder.txtVoyageType.setText(item.getTypeVoyage());
        holder.placeToGo.setText(item.getDestiny());
        holder.dateToGo.setText(item.getArrivalDate() + " to " + item.getExitDate());
        holder.budgetYouHave.setText("Budget: " + item.getBudget().toString());
        holder.txtTotalSpend.setText("Total Spend: " + item.getTotalSpend());


        //value = item.getBudget().intValue() / (int)item.getTotalSpend();
        holder.progressBar.setMax(item.getBudget().intValue());
        holder.progressBar.setSecondaryProgress((int) item.getAlertSpend());  //limit (week color)
        holder.progressBar.setProgress((int)item.getTotalSpend());  //spend (strong color)



    }

    @Override
    public int getItemCount() {
        return listData.size();
    }



    class VoyageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView voyageType;
        private TextView placeToGo, dateToGo, budgetYouHave, txtVoyageType, txtTotalSpend;
        private ProgressBar progressBar;
        private View container;

        public VoyageHolder(final View itemView) {
            super(itemView);

            voyageType = (ImageView) itemView.findViewById(R.id.img_type_voyage);
            placeToGo = (TextView) itemView.findViewById(R.id.txtPlaceYouGo);
            txtVoyageType = (TextView) itemView.findViewById(R.id.txtVoyageType);
            txtTotalSpend = (TextView) itemView.findViewById(R.id.txtTotalSpend);
            dateToGo = (TextView) itemView.findViewById(R.id.txtDateYouGo);
            budgetYouHave = (TextView) itemView.findViewById(R.id.txtBudgetYouHave);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);

            container = itemView.findViewById(R.id.container_model);
            container.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.container_model:
                    itemClickCallBack.onItemClick(getAdapterPosition());
                    break;
            }

        }
    }

}
