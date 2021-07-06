package com.example.app_ghichu.TQGhiChuDaXoa.SearchGhiChuDaXoa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.app_ghichu.Database.DBGhiChu;
import com.example.app_ghichu.R;
import com.example.app_ghichu.TQGhiChu.ObjGhiChu.ObjGhiChu;
import com.example.app_ghichu.TQGhiChu.Search.Search_Item;
import com.example.app_ghichu.TQGhiChuDaXoa.AdapterGhiChuDaXoa.AdapterGhiChuDaXoa;
import com.example.app_ghichu.TQGhiChuDaXoa.ObjGhiChuDaXoa.ObjGhiChuDaXoa;
import com.example.app_ghichu.TQGhiChuDaXoa.TongQuatGhiChuDaXoa;

import java.util.ArrayList;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class SearchGhiChuDaXoa extends AppCompatActivity {

    DBGhiChu db;
    ArrayList<ObjGhiChuDaXoa> arrayListGhiChuDaXoa;
    AdapterGhiChuDaXoa adapterGhiChuDaXoa;
    RecyclerView rcv;
    EditText searchCongViec;
    TextView tvHuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ghi_chu_da_xoa);

        // Animation
        Animation();

        // Khởi tạo UI
        InitUI();

        // Khởi tạo database
        DataBaseGhiChu() ;

        // Load toàn bộ ghi chú ra màn hình
        LoadGhiChu();

        // Tự hiện bàn phím khi vào search công việc
        AutoHienBanPhim();

        // Click vào hủy
        ClickCancel ();

        // Search Ghi Chú
        SearchGhiChu();
    }

    // Khởi tạo UI
    public void InitUI() {
        rcv = findViewById(R.id.rcv_search);
        searchCongViec = findViewById(R.id.edt_search_item);
        tvHuy = findViewById(R.id.tv_huy);
    }

    // Khởi tạo database
    public void DataBaseGhiChu () {
        // Tạo database
        db = new DBGhiChu(this, "GhiChu.sqlite", null, 1);
    }

    // Load toàn bộ ghi chú ra màn hình
    public void LoadGhiChu () {
        // khởi tạo và thêm data cho array list
        InitAndAddArrayList ();

        // Khởi tạo adapter ghi chú
        adapterGhiChuDaXoa = new AdapterGhiChuDaXoa(this,null, arrayListGhiChuDaXoa);

        // Khởi tạo LinearLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        // Set adapter cho recyclerview
        rcv.setAdapter(adapterGhiChuDaXoa);

        // Set LinearLayoutManager cho recyclerview
        rcv.setLayoutManager(linearLayoutManager);
    }

    // khởi tạo và thêm data cho array list
    public void InitAndAddArrayList () {
        arrayListGhiChuDaXoa = new ArrayList<>();

        Cursor cursor = db.TraVe("SELECT * FROM CongViecDaXoa");
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String congViecGhiChu = cursor.getString(1);
            String gioGhiChu = cursor.getString(2);

            arrayListGhiChuDaXoa.add(new ObjGhiChuDaXoa(congViecGhiChu, gioGhiChu, id));
        }
    }

    // Hiện bàn phím khi vào search công việc
    public void AutoHienBanPhim() {
        // Hiện bàn phím
        BatBanPhim ();
    }

    // Bật bàn phím
    public void BatBanPhim () {
        searchCongViec.setFocusableInTouchMode(true);
        searchCongViec.requestFocus();
    }

    // Click vào hủy
    public void ClickCancel () {
        tvHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TatBanPhim();
                new ObjFlagGhiChuDaXoa(1);
                Intent intent = new Intent(SearchGhiChuDaXoa.this, TongQuatGhiChuDaXoa.class);
                startActivity(intent);
            }
        });
    }

    // Tắt bàn phím
    public void TatBanPhim () {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchCongViec.getWindowToken(), 0);
    }

    // Search ghi chú
    public void SearchGhiChu () {
        searchCongViec.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                System.out.println(s);
                filter (s.toString());
            }
        });

    }

    // Lọc kết quả của search
    private void filter (String text) {
        ArrayList<ObjGhiChuDaXoa> filterList = new ArrayList<>();

        for (ObjGhiChuDaXoa item : arrayListGhiChuDaXoa) {
            if (item.getGhiChu().toLowerCase().contains(text.toLowerCase())) {
                filterList.add(item);
            }
        }

        adapterGhiChuDaXoa.FilterList(filterList);
    }

    // DiaLog delete
    public void DiaLogDeleteSearch(int id, int position) {
        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .setMessage("Are you sure want to delete this file?")
                .setCancelable(false)
                .setPositiveButton("Oke",R.drawable.ic_oke, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        try {

                            // Delete ghi chú
                            db.KhongTraVe("DELETE FROM CongViecDaXoa WHERE id = '"+ id +"'");

                            // Off dialog
                            dialogInterface.dismiss();

                            // Thông báo
                            Toast.makeText(SearchGhiChuDaXoa.this, "Xóa thành công", Toast.LENGTH_SHORT).show();

                            // cập nhật lại dữ liệu sau khi xóa
                            arrayListGhiChuDaXoa.remove(position);
                            rcv.removeViewAt(position);
                            adapterGhiChuDaXoa.notifyItemRemoved(position);
                            adapterGhiChuDaXoa.notifyItemRangeChanged(position, arrayListGhiChuDaXoa.size());

                            // Set rỗng cho editText (để không auto tìm kiếm)
                            searchCongViec.setText("");

                        } catch (Exception e) {
                            Toast.makeText(SearchGhiChuDaXoa.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", R.drawable.ic_delete, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();

        // Show Dialog
        mDialog.show();
    }

    // DiaLog khôi phục
    public void DiaLogRestore(int id, int position, String ghiChu, String gioPhut) {
        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .setMessage("Are you sure want to restore this file?")
                .setCancelable(false)
                .setPositiveButton("Oke",R.drawable.ic_oke, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        try {
                            // Insert lại ghi chú vào bảng CongViec
                            db.KhongTraVe("INSERT INTO CongViec VALUES (null, '"+ ghiChu +"', '"+ gioPhut +"')");

                            // Delete ghi chú
                            db.KhongTraVe("DELETE FROM CongViecDaXoa WHERE id = '"+ id +"'");

                            // Off dialog
                            dialogInterface.dismiss();

                            // Thông báo
                            Toast.makeText(SearchGhiChuDaXoa.this, "Khôi phục thành công", Toast.LENGTH_SHORT).show();

                            // cập nhật lại dữ liệu sau khi xóa
                            arrayListGhiChuDaXoa.remove(position);
                            rcv.removeViewAt(position);
                            adapterGhiChuDaXoa.notifyItemRemoved(position);
                            adapterGhiChuDaXoa.notifyItemRangeChanged(position, arrayListGhiChuDaXoa.size());

                        } catch (Exception e) {
                            Toast.makeText(SearchGhiChuDaXoa.this, "Khôi phục thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", R.drawable.ic_delete, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();

        // Show Dialog
        mDialog.show();
    }

    // Animation
    public void Animation () {
        YoYo.with(Techniques.Pulse)
                .duration(500)
                .repeat(0)
                .playOn(findViewById(R.id.layout_main));
    }
}