package com.chengzi.app.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.chengzi.app.R;

import java.util.ArrayList;
import java.util.List;

public class EstimateTypeView extends RecyclerView {
    public EstimateTypeView(@NonNull Context context) {
        super(context);
        init();
    }

    public EstimateTypeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EstimateTypeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setNestedScrollingEnabled(false);
        setLayoutManager(new FlexboxLayoutManager(getContext(), FlexDirection.ROW, FlexWrap.WRAP));


    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        post(() -> {

            List<Item> list = new ArrayList<>();

            String[] stringArray = getContext().getResources().getStringArray(R.array.estimate_type);
            for (int i = 0; i < stringArray.length; i++) {
                list.add(new Item(stringArray[i], i + 1));
            }
            list.get(selectPos).selected = true;
            setAdapter(new EstimateAdapter(list));
        });
    }

    private int selectPos = 0;

    private class EstimateAdapter extends Adapter<EstimateViewHolder> {
        private List<Item> data;

        public EstimateAdapter(List<Item> data) {
            this.data = data;
        }

        @NonNull
        @Override
        public EstimateViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            Context context = viewGroup.getContext();
            View view = LayoutInflater.from(context).inflate(R.layout.item_estimate_type_layout, viewGroup, false);
            EstimateViewHolder viewHolder = new EstimateViewHolder(view);

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull EstimateViewHolder viewHolder, int i) {
            Item item = data.get(i);
            viewHolder.textView.setText(item.text);
            viewHolder.textView.setSelected(item.selected);
            viewHolder.textView.setOnClickListener(v -> {
                if (listener != null) {
                    int layoutPosition = viewHolder.getLayoutPosition();
                    Item selectItem = data.get(layoutPosition);
                    if (listener.onTypeSelected(selectItem.type)) {
                        return;
                    }

                    if (selectPos >= 0) {
                        data.get(selectPos).selected = false;
                        notifyItemChanged(selectPos);
                    }

                    int childAdapterPosition = getChildAdapterPosition(v);
                    data.get(childAdapterPosition).selected = true;
                    notifyItemChanged(childAdapterPosition);
                    selectPos = childAdapterPosition;
                }
            });
        }

        @Override
        public int getItemCount() {
            return data == null ? 0 : data.size();
        }

    }

    private static class EstimateViewHolder extends ViewHolder {

        private TextView textView;

        public EstimateViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }

    public static class Item {
        public String text;
        public int type;
        public boolean selected;

        public Item(String text, int type) {
            this.text = text;
            this.type = type;
        }
    }

    private OnTypeSelectListener listener;

    public void setListener(OnTypeSelectListener listener) {
        this.listener = listener;
    }

    public interface OnTypeSelectListener {
        /**
         * @param type (1 全部评价| 2 好评| 3 有图| 4 效果显著| 5 优质服务)
         */
        boolean onTypeSelected(int type);
    }
}
