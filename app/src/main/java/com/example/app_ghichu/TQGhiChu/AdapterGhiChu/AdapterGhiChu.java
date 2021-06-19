package com.example.app_ghichu.TQGhiChu.AdapterGhiChu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.app_ghichu.R;
import com.example.app_ghichu.TQGhiChu.ObjGhiChu.ObjGhiChu;
import com.example.app_ghichu.TQGhiChu.Search.ObjFlag;
import com.example.app_ghichu.TQGhiChu.Search.Search_Item;
import com.example.app_ghichu.TQGhiChu.TongQuatGhiChu;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdapterGhiChu extends RecyclerView.Adapter<AdapterGhiChu.GhiChuViewHolder>{

    Search_Item contextSearch;
    TongQuatGhiChu context;
    List<ObjGhiChu> mList;

    public AdapterGhiChu(Search_Item contextSearch, TongQuatGhiChu context, List<ObjGhiChu> mList) {
        this.contextSearch = contextSearch;
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @NotNull
    @Override
    public GhiChuViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_congviec, parent, false);
        return new GhiChuViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterGhiChu.GhiChuViewHolder holder, int position) {
        ObjGhiChu objGhiChu = mList.get(position);
        if (objGhiChu == null) {
            return;
        }
        holder.tvGhiChu.setText(objGhiChu.getGhiChu());
        holder.tvNgayThang.setText(objGhiChu.getNgayThang());

        // edit công việc
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Sửa" + objGhiChu.getId());
            }
        });

        // delete công việc
        holder.layoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ObjFlag.getFlag() == 1) {
                    context.DiaLogDelete(objGhiChu.getId(), position);
                } else {
                    contextSearch.DiaLogDeleteSearch(objGhiChu.getId(), position);
                }
                System.out.println("xóa" + objGhiChu.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mList != null) {
            return mList.size();
        }
        return 0;
    }

    public class GhiChuViewHolder extends RecyclerView.ViewHolder {

        public TextView tvGhiChu;
        public TextView tvNgayThang;
        public LinearLayout layoutItem;
        public LinearLayout layoutDelete;

        public GhiChuViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            tvGhiChu = itemView.findViewById(R.id.tv_ghichu_item);
            tvNgayThang = itemView.findViewById(R.id.tv_ngaythang_item);
            layoutItem = itemView.findViewById(R.id.layout_item);
            layoutDelete = itemView.findViewById(R.id.linearlayout_delete);
        }
    }

    public void FilterList (ArrayList<ObjGhiChu> fileteredList) {
        mList = fileteredList;
        notifyDataSetChanged();
    }
}
