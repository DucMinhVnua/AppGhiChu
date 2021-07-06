package com.example.app_ghichu.TQGhiChuDaXoa.AdapterGhiChuDaXoa;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_ghichu.R;
import com.example.app_ghichu.TQGhiChu.ObjGhiChu.ObjGhiChu;
import com.example.app_ghichu.TQGhiChu.Search.ObjFlag;
import com.example.app_ghichu.TQGhiChu.Search.Search_Item;
import com.example.app_ghichu.TQGhiChu.TongQuatGhiChu;
import com.example.app_ghichu.TQGhiChuDaXoa.ObjGhiChuDaXoa.ObjGhiChuDaXoa;
import com.example.app_ghichu.TQGhiChuDaXoa.SearchGhiChuDaXoa.ObjFlagGhiChuDaXoa;
import com.example.app_ghichu.TQGhiChuDaXoa.SearchGhiChuDaXoa.SearchGhiChuDaXoa;
import com.example.app_ghichu.TQGhiChuDaXoa.TongQuatGhiChuDaXoa;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AdapterGhiChuDaXoa extends RecyclerView.Adapter<AdapterGhiChuDaXoa.GhiChuDaXoaViewHolder>{

    SearchGhiChuDaXoa contextSearch;
    TongQuatGhiChuDaXoa context;
    List<ObjGhiChuDaXoa> mList;

    public AdapterGhiChuDaXoa(SearchGhiChuDaXoa contextSearch, TongQuatGhiChuDaXoa context, List<ObjGhiChuDaXoa> mList) {
        this.contextSearch = contextSearch;
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @NotNull
    @Override
    public GhiChuDaXoaViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_congviec, parent, false);
        return new GhiChuDaXoaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull AdapterGhiChuDaXoa.GhiChuDaXoaViewHolder holder, int position) {
        ObjGhiChuDaXoa objGhiChuDaXoa = mList.get(position);
        if (objGhiChuDaXoa == null) {
            return;
        }
        holder.tvGhiChu.setText(objGhiChuDaXoa.getGhiChu());
        holder.tvNgayThang.setText(objGhiChuDaXoa.getNgayThang());

        // delete công việc
        holder.layoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ObjFlagGhiChuDaXoa.getFlag() == 1) {
                    context.DiaLogDelete(objGhiChuDaXoa.getId(), position);
                } else {
                    contextSearch.DiaLogDeleteSearch(objGhiChuDaXoa.getId(), position);
                }
            }
        });

        // Khôi phục công việc
        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ObjFlagGhiChuDaXoa.getFlag() == 1) {
                    context.DiaLogRestore(objGhiChuDaXoa.getId(), position, objGhiChuDaXoa.getGhiChu(), objGhiChuDaXoa.getNgayThang());
                } else {
                    contextSearch.DiaLogRestore(objGhiChuDaXoa.getId(), position, objGhiChuDaXoa.getGhiChu(), objGhiChuDaXoa.getNgayThang());
                }
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

    public class GhiChuDaXoaViewHolder extends RecyclerView.ViewHolder {

        public TextView tvGhiChu;
        public TextView tvNgayThang;
        public LinearLayout layoutItem;
        public LinearLayout layoutDelete;

        public GhiChuDaXoaViewHolder(@NonNull @org.jetbrains.annotations.NotNull View itemView) {
            super(itemView);
            tvGhiChu = itemView.findViewById(R.id.tv_ghichu_item);
            tvNgayThang = itemView.findViewById(R.id.tv_ngaythang_item);
            layoutItem = itemView.findViewById(R.id.layout_item);
            layoutDelete = itemView.findViewById(R.id.linearlayout_delete);
        }
    }

    public void FilterList (ArrayList<ObjGhiChuDaXoa> fileteredList) {
        mList = fileteredList;
        notifyDataSetChanged();
    }
}
