package com.chengzi.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chengzi.app.R;
import com.chengzi.app.ui.account.bean.SelectListBean;

import java.util.List;


public class SelectListDialog extends Dialog {
    private RecyclerView mRecyclerView;
    private TextView tv_cancel, tv_title;
    private View view;
    private SelectListDialogAdapter adapter;
    private int type = 0; //1SelectListBean

    public SelectListDialog(Context context, List<SelectListBean> list, String cause) {
        super(context);
        setView(context);

        adapter.setNewData(list);
        adapter.setChooseString(cause);
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            String str = list.get(position).getName();
            int id = list.get(position).getId();
            adapter.setChooseString(str);
            adapter.notifyDataSetChanged();
            if (listener != null) {
                listener.onSelect(str + "", id, position);
            }

            dismiss();
        });

        tv_cancel.setOnClickListener(v -> dismiss());
    }

    //区分上边的正确SelectListDialog
    public SelectListDialog(Context context, List<String> list, String cause, int i) {
        super(context);
        setView(context);

        type = i;
        adapter.setNewData(list);
        adapter.setChooseString(cause);
        adapter.setOnItemChildClickListener((adapter1, view, position) -> {
            String str = list.get(position);
            adapter.setChooseString(str);
            adapter.notifyDataSetChanged();
            if (listener != null) {
                listener.onSelect(str + "", 1, position);
            }
            dismiss();
        });
        tv_cancel.setOnClickListener(v -> dismiss());
    }


    private void setView(Context context) {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#20000000")));
        setContentView(R.layout.dialog_select_list);

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;

        getWindow().setGravity(Gravity.BOTTOM);


        mRecyclerView = findViewById(R.id.recyclerView);
        tv_title = findViewById(R.id.tv_title);
        tv_cancel = findViewById(R.id.tv_cancel);
        view = findViewById(R.id.view);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        adapter = new SelectListDialogAdapter();
        mRecyclerView.setAdapter(adapter);
    }

    private OnSelectListener listener;

    public interface OnSelectListener {
        void onSelect(String cause, int causeid, int position);
    }

    public SelectListDialog setSureListener(OnSelectListener listener) {
        this.listener = listener;
        return this;
    }

    public SelectListDialog setTvCancle(String cancle) {
        tv_cancel.setText(cancle);
        tv_cancel.setVisibility(View.VISIBLE);
        return this;
    }

    //是否显示 默认显示  false隐藏
    public SelectListDialog setShowCancle(boolean isHint) {
        tv_cancel.setVisibility(!isHint ? View.GONE : View.VISIBLE);
        return this;
    }


    //是否显示 title
    public SelectListDialog setTitle(String title) {
        tv_title.setText(title);
        tv_title.setVisibility(View.VISIBLE);
        view.setVisibility(View.VISIBLE);
        return this;
    }


    public class SelectListDialogAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

        public SelectListDialogAdapter() {
            super(R.layout.item_dialog_selectlist_layout);
        }

        private String chooseString;

        public void setChooseString(String chooseString) {
            this.chooseString = chooseString;
        }

        @Override
        protected void convert(BaseViewHolder helper, T item) {
            String name = "";
            if (type == 1) {
                name = (String) item;
            } else {
                SelectListBean bean = (SelectListBean) item;
                name = bean.getName();
            }
            helper.setText(R.id.tv_name, name);
            helper.addOnClickListener(R.id.tv_name);
            TextView view = helper.getView(R.id.tv_name);
            if (name.equals(chooseString)) {
                view.setTextColor(Color.parseColor("#FF3A2D"));
            } else {
                view.setTextColor(Color.parseColor("#FF3D3D3D"));
            }
        }
    }
}