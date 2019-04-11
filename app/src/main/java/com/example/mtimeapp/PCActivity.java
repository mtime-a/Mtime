package com.example.mtimeapp;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PCActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mIcon;
    private TextView mUsername;
    private TextView mId;
    private TextView mEmil;
    private ImageView back;

    private String user_id;
    private String username;
    private String nickName;
    private String headImage;
    private String statu;
    private Uri imageUri;
    private String email;
    private String icon;
    private String cookie;
    private File outputImage;
    private AlertDialog.Builder builder_username;
    private View view;
    private File postFile;

    private AlertDialog.Builder buider;
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pc_homepage);

        initUI();

        mIcon.setOnClickListener(this);
        mUsername.setOnClickListener(this);
        mId.setOnClickListener(this);
        mEmil.setOnClickListener(this);
        back.setOnClickListener(this);

        // initThread();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
        cookie = sharedPreferences.getString("cookie", "");
        username = sharedPreferences.getString("theName", "");
        nickName = sharedPreferences.getString("theNickname", "");
        email = sharedPreferences.getString("theEmail", "");
        headImage = "http://132.232.78.106:8001/media/" + sharedPreferences.getString("theHeadImage", "");
        imageUri = Uri.parse(headImage);
        mUsername.setText(nickName);
        mEmil.setText(email);
        mId.setText(username);
        Glide.with(this).load(headImage).into(mIcon);
    }

    private void initUI() {
        mIcon = findViewById(R.id.pc_homepage_icon);
        mUsername = findViewById(R.id.pc_homepage_username);
        mId = findViewById(R.id.pc_homepage_id);
        mEmil = findViewById(R.id.pc_homepage_emil);
        back = findViewById(R.id.pc_homepage_back);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pc_homepage_back:
                finish();
                break;
            case R.id.pc_homepage_icon:
                //这里写拍照和从相册选择
                buider = new AlertDialog.Builder(PCActivity.this);
                final String arrItem[] = getResources().getStringArray(R.array.oem);
                buider.setItems(arrItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (arrItem[which].equals("拍照")) {
                            outputImage = new File(getExternalCacheDir(), "output_image.jpg");
                            try {
                                if (outputImage.exists()) {
                                    outputImage.delete();
                                }
                                outputImage.createNewFile();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (Build.VERSION.SDK_INT >= 24) {
                                imageUri = FileProvider.getUriForFile(PCActivity.this, "com.example.cameraAlbumTest.fileProvider", outputImage);
                            } else {
                                imageUri = Uri.fromFile(outputImage);
                            }
                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                            startActivityForResult(intent, TAKE_PHOTO);

                        } else if (arrItem[which].equals("从相册选择")) {
                            if (ContextCompat.checkSelfPermission(PCActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(PCActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                            } else {
                                openAlbum();
                            }
                        }
                    }
                });
                buider.create().show();
                break;
            case R.id.pc_homepage_username:
                initBuilder_username();
                break;
        }
    }

    private void initBuilder_username() {
        builder_username = new AlertDialog.Builder(PCActivity.this);
        builder_username.setTitle("请输入新昵称");
        view = LayoutInflater.from(PCActivity.this).inflate(R.layout.dialog, null);
        final EditText editText = view.findViewById(R.id.text);
        builder_username.setPositiveButton("修改", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                nickName = editText.getText().toString();
                int i = nickName.length();
                if (i > 16) {
                    Toast.makeText(getApplicationContext(), "昵称不能超过12位", Toast.LENGTH_LONG).show();
                } else {
                    boolean judge = checkNickname(nickName);
                    if(judge){
                        Log.e("Nickname","可以");
                        changNickname(nickName);
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"昵称不能为空白字符",Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
        builder_username.setView(view).create().show();
    }

    private void changNickname(final String nick) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        FormBody formBody = new FormBody.Builder()
                .add("nickName", nick)
                .add("session", cookie)
                .build();

        Log.e("PC ", cookie + nick);
        Request request = new Request.Builder()
                .addHeader("Connection", "close")
                .url("http://132.232.78.106:8001/api/changeNickName/")
                .post(formBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                Log.e("TAG", "获取数据失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        statu = jsonObject.getString("state");
                        String msg = jsonObject.getString("msg");
                        judgeState(msg);
                        if (statu.equals("1")) {
                            String nick = jsonObject.getString("nickName");
                            SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("theNickname", nick);
                            editor.apply();
                            mUsername.setText(nick);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void judgeState(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "拒绝权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                        Log.e("TAG", "选择照片");
                    } else {
                        handleImageBeforeKitKat(data);
                        Log.e("TAG", "拍照");
                    }
                }
                break;
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    bitmap = getBitmapSmall(bitmap);
                    OkHttpClient okHttpClient = new OkHttpClient.Builder()
                            .retryOnConnectionFailure(true)
                            .connectTimeout(20, TimeUnit.SECONDS)
                            .writeTimeout(20, TimeUnit.SECONDS)
                            .readTimeout(20, TimeUnit.SECONDS)
                            .build();

                    RequestBody image = RequestBody.create(MediaType.parse("image/png"), convertBitmapToFile(bitmap));
                    RequestBody requestBody = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("headImage", "output_image.jpg", image)
                            .addFormDataPart("session", cookie)
                            .build();
                    Request request = new Request.Builder()
                            .url("http://132.232.78.106:8001/api/changeHeadImage/")
                            .post(requestBody)
                            .build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        //请求错误回调方法
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.d("mlj", "获取数据失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.isSuccessful()) {
                                try {
                                    String responseData = response.body().string();
                                    JSONObject jsonObject = new JSONObject(responseData);
                                    statu = jsonObject.getString("state");
                                    String msg = jsonObject.getString("msg");
                                    final String ImageUrl = "http://132.232.78.106:8001/media/" + jsonObject.getString("imageHead");
                                    String HeadImageUrl = jsonObject.getString("imageHead");
                                    Log.e("PCActivity", statu + msg + ImageUrl);
                                    SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString("theHeadImage", HeadImageUrl);
                                    editor.apply();

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Glide.with(getApplicationContext()).load(ImageUrl).into(mIcon);
                                        }
                                    });
                                    judgeState(msg);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });

                }
                break;
            default:
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }
        postImage(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        postImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex((MediaStore.Images.Media.DATA)));
            }
            cursor.close();
        }
        return path;
    }

    private void postImage(String imagePath) {
        if (imagePath != null) {
            //这里可以上服务器;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .connectTimeout(20, TimeUnit.SECONDS)
                    .writeTimeout(20, TimeUnit.SECONDS)
                    .readTimeout(20, TimeUnit.SECONDS)
                    .build();
            //File file = new File(imagePath);
            //File file = convertBitmapToFile(bitmap);
            Log.e("PCActivity", "ok1");
            RequestBody image = RequestBody.create(MediaType.parse("image/png"), convertBitmapToFile(bitmap));
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    // .addFormDataPart("headImage", imagePath, image)
                    .addFormDataPart("headImage", imagePath, image)
                    .addFormDataPart("session", cookie)
                    .build();
            Log.e("PCActivity", "为啥传不上去");
            final Request request = new Request.Builder()
                    .url("http://132.232.78.106:8001/api/changeHeadImage/")
                    .post(requestBody)
                    .build();
            Log.e("PCActivity", "ok2");
            okHttpClient.newCall(request).enqueue(new Callback() {
                //请求错误回调方法
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("mlj", "获取数据失败");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.isSuccessful()) {
                        try {
                            Log.e("PCActivity", "ok3");
                            String responseData = response.body().string();
                            JSONObject jsonObject = new JSONObject(responseData);
                            statu = jsonObject.getString("state");
                            String msg = jsonObject.getString("msg");
                            final String ImageUrl = "http://132.232.78.106:8001/media/" + jsonObject.getString("imageHead");
                            String HeadImageUrl = jsonObject.getString("imageHead");
                            Log.e("PCActivity", statu + msg + ImageUrl);
                            SharedPreferences sharedPreferences = getSharedPreferences("theUser", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("theHeadImage", HeadImageUrl);
                            editor.apply();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Glide.with(getApplicationContext()).load(ImageUrl).into(mIcon);
                                }
                            });
                            judgeState(msg);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("PCActivity", response.body().string());
                        }

                    }
                }
            });
        } else {
            Toast.makeText(this, "选择失败", Toast.LENGTH_LONG).show();
        }
    }

    public Bitmap getBitmapSmall(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, out);                                     //压缩图片
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        bitmap = BitmapFactory.decodeByteArray(out.toByteArray(), 0, out.toByteArray().length);
        return bitmap;
    }

    private File convertBitmapToFile(Bitmap bitmap) {
        try {
            // create a file to write bitmap data
            postFile = new File(this.getCacheDir(), "output_image.jpg");
            postFile.createNewFile();
            // convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int num = getBitmapSize(bitmap);
            if (num >= 20000000 && num <= 40000000) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 5, bos);
            } else if (num < 20000000) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            } else if (num > 40000000) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 1, bos);
            }
            byte[] bitmapdata = bos.toByteArray();
            // write the bytes in file
            FileOutputStream fos = new FileOutputStream(postFile);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception e) {

        }
        return postFile;
    }

    public static int getBitmapSize(Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {    //API 19
            return bitmap.getAllocationByteCount();
        }
        // 在低版本中用一行的字节x高度
        return bitmap.getRowBytes() * bitmap.getHeight();                //earlier version
    }

    public static boolean checkNickname(String nickname) {
        Pattern pattern = Pattern.compile("^[\\S\u4e00-\u9fa5]{1,12}$");
        Matcher matcher = pattern.matcher(nickname);
        return matcher.matches();
    }
}


