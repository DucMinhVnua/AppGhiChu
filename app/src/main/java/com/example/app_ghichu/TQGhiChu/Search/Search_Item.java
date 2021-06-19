package com.example.app_ghichu.TQGhiChu.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.app_ghichu.Database.DBGhiChu;
import com.example.app_ghichu.R;
import com.example.app_ghichu.TQGhiChu.AdapterGhiChu.AdapterGhiChu;
import com.example.app_ghichu.TQGhiChu.ObjGhiChu.ObjGhiChu;
import com.example.app_ghichu.TQGhiChu.TongQuatGhiChu;

import java.util.ArrayList;
import java.util.List;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class Search_Item extends AppCompatActivity {

    DBGhiChu db;
    ArrayList<ObjGhiChu> arrayListGhiChu;
    AdapterGhiChu adapterGhiChu;
    RecyclerView rcv;
    TextView tvHuy;
    EditText searchCongViec;

    String text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_item);

        System.out.println(ObjFlag.getFlag());

        // Khởi tạo UI
        InitUI();

        // Khởi tạo lại database
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
    public void InitUI () {
        rcv = findViewById(R.id.rcv_search);
        tvHuy = findViewById(R.id.tv_huy);
        searchCongViec = findViewById(R.id.edt_search_item);
    }

    // Khởi tạo lại database
    public void DataBaseGhiChu () {
        // Tạo database
        db = new DBGhiChu(this, "GhiChu.sqlite", null, 1);
    }

    // Load toàn bộ ghi chú ra màn hình
    public void LoadGhiChu () {
        // khởi tạo và thêm data cho array list
        InitAndAddArrayList ();

        // Khởi tạo adapter ghi chú
        adapterGhiChu = new AdapterGhiChu(this,null, arrayListGhiChu);

        // Khởi tạo LinearLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);

        // Set adapter cho recyclerview
        rcv.setAdapter(adapterGhiChu);

        // Set LinearLayoutManager cho recyclerview
        rcv.setLayoutManager(linearLayoutManager);
    }

    // khởi tạo và thêm data cho array list
    public void InitAndAddArrayList () {
        arrayListGhiChu = new ArrayList<>();

        Cursor cursor = db.TraVe("SELECT * FROM CongViec");
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String congViecGhiChu = cursor.getString(1);
            String gioGhiChu = cursor.getString(2);

            arrayListGhiChu.add(new ObjGhiChu(congViecGhiChu, gioGhiChu, id));
        }
    }

    // Click vào hủy
    public void ClickCancel () {
        tvHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TatBanPhim();
                new ObjFlag(1);
                Intent intent = new Intent(Search_Item.this, TongQuatGhiChu.class);
                startActivity(intent);
            }
        });
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

    // Tắt bàn phím
    public void TatBanPhim () {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(searchCongViec.getWindowToken(), 0);
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
                            db.KhongTraVe("DELETE FROM CongViec WHERE id = '"+ id +"'");

                            dialogInterface.dismiss();
                            Toast.makeText(Search_Item.this, "Xóa thành công", Toast.LENGTH_SHORT).show();

                            // cập nhật lại dữ liệu sau khi xóa
                            arrayListGhiChu.remove(position);
                            rcv.removeViewAt(position);
                            adapterGhiChu.notifyItemRemoved(position);
                            adapterGhiChu.notifyItemRangeChanged(position, arrayListGhiChu.size());

                            // Set rỗng cho editText (để không auto tìm kiếm)
                            searchCongViec.setText("");

                        } catch (Exception e) {
                            Toast.makeText(Search_Item.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
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
        ArrayList<ObjGhiChu> filterList = new ArrayList<>();

        for (ObjGhiChu item : arrayListGhiChu) {
            if (item.getGhiChu().toLowerCase().contains(text.toLowerCase())) {
                filterList.add(item);
            }
        }

        adapterGhiChu.FilterList(filterList);
    }
}