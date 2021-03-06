package com.example.app_ghichu.TQGhiChuDaXoa;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.app_ghichu.Database.DBGhiChu;
import com.example.app_ghichu.MainActivity;
import com.example.app_ghichu.R;
import com.example.app_ghichu.TQGhiChu.AdapterGhiChu.AdapterGhiChu;
import com.example.app_ghichu.TQGhiChu.ObjGhiChu.ObjGhiChu;
import com.example.app_ghichu.TQGhiChu.Search.ObjFlag;
import com.example.app_ghichu.TQGhiChu.Search.Search_Item;
import com.example.app_ghichu.TQGhiChu.TongQuatGhiChu;
import com.example.app_ghichu.TQGhiChuDaXoa.AdapterGhiChuDaXoa.AdapterGhiChuDaXoa;
import com.example.app_ghichu.TQGhiChuDaXoa.ObjGhiChuDaXoa.ObjGhiChuDaXoa;
import com.example.app_ghichu.TQGhiChuDaXoa.SearchGhiChuDaXoa.ObjFlagGhiChuDaXoa;
import com.example.app_ghichu.TQGhiChuDaXoa.SearchGhiChuDaXoa.SearchGhiChuDaXoa;

import java.util.ArrayList;

import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface;

public class TongQuatGhiChuDaXoa extends AppCompatActivity {

    AdapterGhiChuDaXoa adapterGhiChuDaXoa;
    ArrayList<ObjGhiChuDaXoa> arrayListGhiChuDaXoa;
    RecyclerView rcv;
    DBGhiChu db;
    EditText Search;
    TextView demGhiChu, tvThuMuc;
    FrameLayout Back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tong_quat_ghi_chu_da_xoa);

        // Animation
        Animation();

        // Khởi tạo database
        DataBaseGhiChu();

        // Khởi tạo bên UI
        initUI();

        // Load toàn bộ ghi chú ra màn hình
        LoadGhiChu();

        // Sự kiện khi click vào search sẽ sang trang activity search
        ClickSearch();

        // Đếm xem có bao nhiêu ghi chú
        DemGhiChu();

        // Sự kiện khi click vào button back
        SuKienOnclickBtnBack();
    }

    // Khởi tạo database
    public void DataBaseGhiChu () {
        // Tạo database
        db = new DBGhiChu(this, "GhiChu.sqlite", null, 1);
    }

    // Khởi tạo bên UI
    public void initUI () {
        rcv = findViewById(R.id.rcv);
        Search = findViewById(R.id.edt_search);
        demGhiChu = findViewById(R.id.tv_dem_ghi_chu);
        Back = findViewById(R.id.frame_back);
        tvThuMuc = findViewById(R.id.tv_thumuc);
    }

    // Load toàn bộ ghi chú ra màn hình
    public void LoadGhiChu () {
        // khởi tạo và thêm data cho array list
        InitAndAddArrayList ();

        // Khởi tạo adapter ghi chú
        adapterGhiChuDaXoa = new AdapterGhiChuDaXoa(null, this, arrayListGhiChuDaXoa);

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

    // Sự kiện khi click vào search sẽ sang trang activity search
    public void ClickSearch () {
        Search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                new ObjFlagGhiChuDaXoa(0);
                Intent intent = new Intent(TongQuatGhiChuDaXoa.this, SearchGhiChuDaXoa.class);
                startActivity(intent);
            }
        });
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

                            // Delete ghi chú
                            db.KhongTraVe("DELETE FROM CongViecDaXoa WHERE id = '"+ id +"'");

                            // Off dialog
                            dialogInterface.dismiss();

                            // Thông báo
                            Toast.makeText(TongQuatGhiChuDaXoa.this, "Xóa thành công", Toast.LENGTH_SHORT).show();

                            // cập nhật lại dữ liệu sau khi xóa
                            arrayListGhiChuDaXoa.remove(position);
                            rcv.removeViewAt(position);
                            adapterGhiChuDaXoa.notifyItemRemoved(position);
                            adapterGhiChuDaXoa.notifyItemRangeChanged(position, arrayListGhiChuDaXoa.size());

                            // Đếm lại ghi chú
                            DemGhiChu ();
                        } catch (Exception e) {
                            Toast.makeText(TongQuatGhiChuDaXoa.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
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

    // Đếm xem có bao nhiêu ghi chú (công việc)
    public void DemGhiChu () {
        demGhiChu.setText(String.valueOf(arrayListGhiChuDaXoa.size() + " ghi chú"));
    }

    // Sự kiện khi click vào button back
    public void SuKienOnclickBtnBack () {
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TongQuatGhiChuDaXoa.this, MainActivity.class);
                startActivity(intent);
            }
        });
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
                            Toast.makeText(TongQuatGhiChuDaXoa.this, "Khôi phục thành công", Toast.LENGTH_SHORT).show();

                            // cập nhật lại dữ liệu sau khi xóa
                            arrayListGhiChuDaXoa.remove(position);
                            rcv.removeViewAt(position);
                            adapterGhiChuDaXoa.notifyItemRemoved(position);
                            adapterGhiChuDaXoa.notifyItemRangeChanged(position, arrayListGhiChuDaXoa.size());

                            // Đếm lại ghi chú
                            DemGhiChu ();
                        } catch (Exception e) {
                            Toast.makeText(TongQuatGhiChuDaXoa.this, "Khôi phục thất bại", Toast.LENGTH_SHORT).show();
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
                .duration(1000)
                .repeat(0)
                .playOn(findViewById(R.id.layout_main));
    }
}