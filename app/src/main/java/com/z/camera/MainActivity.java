package com.z.camera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.z.camera.adapter.ShowSelectedPicsAdapter;

import java.io.File;
import java.util.Random;

import static android.os.Build.VERSION_CODES.N;
import static com.z.camera.CallSystemCameraOrAlbum.createImageFile;

public class MainActivity extends AppCompatActivity implements PopupWindow.OnDismissListener, View.OnClickListener {
    private RecyclerView recyclerView;
    private ShowSelectedPicsAdapter adapter;
    private PopupWindow popupWindow;
    private static final int REQUEST_PERMISSION_CAMERA = 0x001;//相机
    private static final int REQUEST_PERMISSION_WRITE = 0x002;//相册
    private static final int CROP_REQUEST_CODE = 0x003;//最终返回结果

    private final String TAG="tag";

    //文件相关
    private File captureFile;
    private File rootFile;
    private File cropFile;
    private File cameraFile;
    private Uri uritempFile;
    private Intent intent;
    private static final int N = 99;
    private Random rand = new Random();
    private int navigationHeight;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        recyclerView=findViewById(R.id.glide_photo_select);
        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        navigationHeight = getResources().getDimensionPixelSize(resourceId);
        adapter = new ShowSelectedPicsAdapter(R.layout.show_selected_pic_item);
        GridLayoutManager manager=new GridLayoutManager(this,4);
        recyclerView.setLayoutManager(manager);
        adapter.setOnItemClickListener(new ShowSelectedPicsAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {

            }


        });
    }
    private void openPopupWindow(View v) {
        //防止重复按按钮
        if (popupWindow != null && popupWindow.isShowing()) {
            return;
        }
        //设置PopupWindow的View
        View view = LayoutInflater.from(this).inflate(R.layout.selecter_photo_way, null);
        popupWindow = new PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        //设置背景,这个没什么效果，不添加会报错
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        //设置点击弹窗外隐藏自身
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        //设置动画
        popupWindow.setAnimationStyle(R.style.BootomAnimStyle);
        //设置位置
        popupWindow.showAtLocation(v, Gravity.BOTTOM, 0, navigationHeight);
        //设置消失监听
        popupWindow.setOnDismissListener(this);
        //设置PopupWindow的View点击事件
        setOnPopupViewClick(view);
        //设置背景色
        setBackgroundAlpha(0.5f);
    }

    private void setOnPopupViewClick(View view) {
    }

    //设置屏幕背景透明效果
    public void setBackgroundAlpha(float alpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = alpha;
        this.getWindow().setAttributes(lp);
    }

    @Override
    public void onDismiss() {
        setBackgroundAlpha(1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
        case R.id.tv_photo_albm_choose:
            popupWindow.dismiss();
            choosePhoto();

        break;
        case R.id.tv_camera_choose:
            popupWindow.dismiss();
            takePhoto();
        break;
        case R.id.tv_cancel:
        popupWindow.dismiss();
        break;
        }
    }

    /**
     * 从相册选择
     */
    private void choosePhoto() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_PERMISSION_WRITE);
    }

    /**
     * 拍照
     */
    private void takePhoto() {
        //用于保存调用相机拍照后所生成的文件
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return;
        }
        captureFile = new File(rootFile, rand.nextInt(N) + "temp.png");
        //跳转到调用系统相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断版本 如果在Android7.0以上,使用FileProvider获取Uri
        if (Build.VERSION.SDK_INT >= N) {
            cameraFile = createImageFile();
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(this, this.getPackageName() + ".fileprovider", cameraFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, apkUri);
            Log.e(TAG,"" + apkUri.toString());
        } else {
            //否则使用Uri.fromFile(file)方法获取Uri
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(captureFile));
        }
        startActivityForResult(intent, REQUEST_PERMISSION_CAMERA);
    }
}
