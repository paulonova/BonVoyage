package bonvoyage.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bonvoyage.objects.Spending;
import bonvoyage.objects.Voyage;
import se.paulo.nackademin.examen.bonvoyage.R;

/**
 * Created by Paulo Vila Nova on 2016-06-27.
 */
public class SpendingListAdapter extends RecyclerView.Adapter<SpendingListAdapter.SpendingHolder> {

    private List<Spending> listSpendData;
    private LayoutInflater inflaterSpending;
    private Context context;

    public final int FUEL_COLOR = R.color.category_fuel;
    public final int FOOD_COLOR = R.color.category_feeding;
    public final int TRANSPORTATION_COLOR = R.color.category_transport;
    public final int ACCOMODATION_COLOR = R.color.category_accommodation;
    public final int OTHERS_COLOR = R.color.category_others;

    //Creating an Interface..
    private ItemClickCallBack itemClickCallBack;

    public interface ItemClickCallBack{
        void onItemClick(int p);
        void onStarIconClick(int p);
    }

    public void setItemClickCallBack(ItemClickCallBack itemClickCallBack) {
        this.itemClickCallBack = itemClickCallBack;
    }


    public SpendingListAdapter(List<Spending> listSpendData, Context context) {
        this.listSpendData = listSpendData;
        this.inflaterSpending = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public SpendingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflaterSpending.inflate(R.layout.spend_list_model, parent, false);

        return new SpendingHolder(view);
    }

    @Override
    public void onBindViewHolder(SpendingHolder holder, int position) {
        Spending item = listSpendData.get(position);

        holder.spendDate.setText(item.getDate());
        holder.spendDescription.setText(item.getDescription());
        holder.spendValue.setText(item.getValue().toString());
        Log.d("CATEGORY","" + item.getCategory());

        switch (item.getCategory()){

            case "Fuel":
                holder.imgSpendIcon.setImageResource(R.drawable.ic_logo_fuel);
                break;
            case "Food":
                holder.imgSpendIcon.setImageResource(R.drawable.ic_logo_food);
                break;
            case "Transportation":
                holder.imgSpendIcon.setImageResource(R.drawable.ic_logo_transportation);
                break;
            case "Accommodation":
                holder.imgSpendIcon.setImageResource(R.drawable.ic_logo_accomodation);
                break;
            case "Others":
                holder.imgSpendIcon.setImageResource(R.drawable.ic_logo_others);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return listSpendData.size();
    }

    class SpendingHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView spendDate, spendDescription, spendValue;
        private ImageView imgSpendIcon, imgSpendStar;
        private View container;

        public SpendingHolder(final View itemView) {
            super(itemView);
            spendDate = (TextView)itemView.findViewById(R.id.spendDate);
            spendDescription = (TextView)itemView.findViewById(R.id.spendDescription);
            spendValue = (TextView)itemView.findViewById(R.id.spendValue);
            imgSpendIcon = (ImageView) itemView.findViewById(R.id.imgSpendIcon);

            imgSpendStar = (ImageView) itemView.findViewById(R.id.imgSpendStar);
            imgSpendStar.setOnClickListener(this);

            container = itemView.findViewById(R.id.containerSpendModel);
            container.setOnClickListener(this);



        }

        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.containerSpendModel:
                    itemClickCallBack.onItemClick(getAdapterPosition());
                    break;

                case R.id.imgSpendStar:
                    itemClickCallBack.onStarIconClick(getAdapterPosition());
                    break;
            }

        }
    }
}
