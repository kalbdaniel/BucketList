package nl.hva.dka.bucketlist;

import android.content.Intent;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class BucketListItemAdapter extends RecyclerView.Adapter<BucketListItemAdapter.ViewHolder> {
    private List<BucketListItem> bucketListItems;
    private IUpdateCallback updateCallback;

    public BucketListItemAdapter(List<BucketListItem> bucketListItems, IUpdateCallback updateCallback) {
        this.bucketListItems = bucketListItems;
        this.updateCallback = updateCallback;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, null);
        return new BucketListItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final BucketListItem bucketListItem = bucketListItems.get(i);
        viewHolder.bucketItemName.setText(bucketListItem.getName());

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    viewHolder.bucketItemName.setPaintFlags(viewHolder.bucketItemName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    viewHolder.bucketItemDescription.setPaintFlags(viewHolder.bucketItemDescription.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    viewHolder.bucketItemName.setPaintFlags(viewHolder.bucketItemName.getPaintFlags()& ~Paint.STRIKE_THRU_TEXT_FLAG);
                    viewHolder.bucketItemDescription.setPaintFlags(viewHolder.bucketItemDescription.getPaintFlags()& ~Paint.STRIKE_THRU_TEXT_FLAG);
                }
                bucketListItem.setChecked(isChecked);
                updateCallback.updateItem(bucketListItem);
            }
        });
        viewHolder.checkBox.setChecked(bucketListItem.getChecked());
    }

    @Override
    public int getItemCount() {
        return bucketListItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView bucketItemName;
        TextView bucketItemDescription;
        CheckBox checkBox;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            bucketItemName = itemView.findViewById(R.id.bucketItemName);
            bucketItemDescription = itemView.findViewById(R.id.bucketItemDescription);
            checkBox = itemView.findViewById(R.id.checkBox);
            linearLayout = itemView.findViewById(R.id.parentLayout);
            linearLayout.setClickable(true);
        }
    }



}
