package com.example.app_ghichu.TQGhiChu;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.app_ghichu.Database.DBGhiChu;
import com.example.app_ghichu.MainActivity;
import com.example.app_ghichu.R;
import com.example.app_ghichu.TQGhiChu.AdapterGhiChu.AdapterGhiChu;
import com.example.app_ghichu.TQGhiChu.ObjGhiChu.ObjGhiChu;
import com.example.app_ghichu.TQGhiChu.Search.ObjFlag;
import com.example.app_ghichu.TQGhiChu.Search.Search_Item;
import com.example.app_ghichu.ThemCV.ThemCongViec;

import java.util.ArrayList;

import dev.shreyaspatil.MaterialDialog.AbstractDialog;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class TongQuatGhiChu extends AppCompatActivity {

    FrameLayout Back;
    AdapterGhiChu adapterGhiChu;
    ArrayList<ObjGhiChu> arrayListGhiChu;
    RecyclerView rcv;
    ImageView themCongViec;
    DBGhiChu db;
    EditText Search;
    TextView demGhiChu, tvThuMuc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tong_quat_ghi_chu);

        // Animation
        Animation();

        System.out.println("Flag: " + ObjFlag.getFlag());

        initUI ();

        // Tạo database và tạo bảng cho database
        DataBaseGhiChu() ;

        // Sự kiện khi click vào button back
        SuKienOnclickBtnBack();

        // Load toàn bộ ghi chú ra màn hình
        LoadGhiChu();

        // Sự kiện khi click vào thêm công việc
        ThemCongViec();

        // Sự kiện khi click vào search sẽ sang trang activity search
        ClickSearch();

        // Đếm xem có bao nhiêu ghi chú
        DemGhiChu();
    }

    // Khởi tạo
    public void initUI () {
        Back = findViewById(R.id.frame_back);
        rcv = findViewById(R.id.rcv);
        themCongViec = findViewById(R.id.imgv_them_cong_viec);
        Search = findViewById(R.id.edt_search);
        demGhiChu = findViewById(R.id.tv_dem_ghi_chu);
        tvThuMuc = findViewById(R.id.tv_thumuc);
    }

    // Khởi tạo database
    public void DataBaseGhiChu () {
        // Tạo database
        db = new DBGhiChu(this, "GhiChu.sqlite", null, 1);
    }

    // Sự kiện khi click vào button back
    public void SuKienOnclickBtnBack () {
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TongQuatGhiChu.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    // Load toàn bộ ghi chú ra màn hình
    public void LoadGhiChu () {
        // khởi tạo và thêm data cho array list
        InitAndAddArrayList ();

        // Khởi tạo adapter ghi chú
        adapterGhiChu = new AdapterGhiChu(null, null,this, arrayListGhiChu);

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

    // Sự kiện khi click vào thêm công việc
    public void ThemCongViec () {
        themCongViec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TongQuatGhiChu.this, ThemCongViec.class);
                intent.putExtra("ten_tieu_de", "Ghi chú");
                startActivity(intent);
            }
        });
    }

    // Sự kiện khi click vào search sẽ sang trang activity search
    public void ClickSearch () {
        Search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                new ObjFlag(0);
                Intent intent = new Intent(TongQuatGhiChu.this, Search_Item.class);
                startActivity(intent);
            }
        });
    }

    // Đếm xem có bao nhiêu ghi chú (công việc)
    public void DemGhiChu () {
        demGhiChu.setText(String.valueOf(arrayListGhiChu.size() + " ghi chú"));
    }

    // DiaLog delete
    public void DiaLogDelete(int id, int position) {
        MaterialDialog mDialog = new MaterialDialog.Builder(this)
                .setMessage("Are you sure want to delete this file?")
                .setCancelable(false)
                .setPositiveButton("Oke",R.drawable.ic_oke, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        try {

                            // Insert ghi chú xóa vào bảng CongViecDaXoa
                            InsertGhiChuDaXoa(id);

                            // Delete ghi chú
                            db.KhongTraVe("DELETE FROM CongViec WHERE id = '"+ id +"'");

                            // Off dialog
                            dialogInterface.dismiss();

                            // Thông báo
                            Toast.makeText(TongQuatGhiChu.this, "Xóa thành công", Toast.LENGTH_SHORT).show();

                            // cập nhật lại dữ liệu sau khi xóa
                            arrayListGhiChu.remove(position);
                            rcv.removeViewAt(position);
                            adapterGhiChu.notifyItemRemoved(position);
                            adapterGhiChu.notifyItemRangeChanged(position, arrayListGhiChu.size());

                            // Đếm lại ghi chú
                            DemGhiChu ();
                        } catch (Exception e) {
                            Toast.makeText(TongQuatGhiChu.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
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

    // Insert ghi chú xóa vào bảng CongViecDaXoa
    public void InsertGhiChuDaXoa(int id) {
        Cursor cursor = db.TraVe("SELECT * FROM CongViec WHERE id = '"+ id +"'");
        while (cursor.moveToNext()) {
            String ghiChu = cursor.getString(1);
            String gioPhut = cursor.getString(2);

            db.KhongTraVe("INSERT INTO CongViecDaXoa VALUES (null,'"+ ghiChu +"', '"+ gioPhut +"')");
        }
    }

    // Animation
    public void Animation () {
        YoYo.with(Techniques.Pulse)
                .duration(500)
                .repeat(0)
                .playOn(findViewById(R.id.layout_main));
    }
}