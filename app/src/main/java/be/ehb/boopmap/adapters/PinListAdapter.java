package be.ehb.boopmap.adapters;

import android.graphics.DashPathEffect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import be.ehb.boopmap.R;
import be.ehb.boopmap.data.Pin;
import be.ehb.boopmap.database.Database;

public class PinListAdapter extends RecyclerView.Adapter<PinListAdapter.viewHolder>{
    private ArrayList<Pin> pinList;
    public PinListAdapter() {
        pinList = new ArrayList<>();
    }

    public void addItems(List<Pin> pinList){
        this.pinList = new ArrayList<>(pinList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_pin_list, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
    Pin currentItem = pinList.get(position);
    holder.pinTitle.setText(currentItem.getTitel());
    holder.pinDescription.setText((currentItem.getDescription()));

    }

    @Override
    public int getItemCount() {
        return pinList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        TextView pinTitle, pinDescription;
        Button btnRemovePin;
        Database db = new Database();
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            pinTitle = itemView.findViewById(R.id.pinTitel);
            pinDescription = itemView.findViewById(R.id.pinDescription);
            btnRemovePin = itemView.findViewById(R.id.btn_removePin);
            btnRemovePin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        Pin pin = pinList.get(position);
                        int pinId = pin.getId();
                        db.removePinById(pinId);
                        pinList.remove(position);
                        notifyItemRemoved(position);

                    }
                }
            });
        }
    }
}
