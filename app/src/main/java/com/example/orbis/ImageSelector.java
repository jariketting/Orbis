package com.example.orbis;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ImageSelector extends Fragment {
    private int GALLERY = 1, CAMERA = 2;

    RecyclerView rv;
    ImageGridAdapter iga;

    String currentPhotoPath;

    API api;

    View view; //stores view
    MainActivity main; //stores our main activity
    Toolbar toolbar; //store view

    Bundle arguments;

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //assign all variables
        view = inflater.inflate(R.layout.fragment_image_selector, container, false);
        toolbar = view.findViewById(R.id.toolbar); //assign toolbar (green bar on top)
        main = ((MainActivity) getActivity());

        api = new API(main);

        rv = view.findViewById(R.id.gallery);

        StaggeredGridLayoutManager sglm = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(sglm);

        List<ImageItem> imageItemList = new ArrayList<>();

        //get bundle arguments to extract id
        arguments = getArguments();

        if(arguments != null && arguments.containsKey("images")) {
            imageItemList = (ArrayList<ImageItem>)arguments.getSerializable("images");
        }

        iga = new ImageGridAdapter(imageItemList);
        rv.setAdapter(iga);

        setupToolbar();
        onPickButtonClicked();

        // Inflate the layout for this fragment
        return view;
    }

    /**
     * Setup toolbar
     */
    @SuppressLint("PrivateResource")
    public void setupToolbar() {
        //set title based on edit or new
        toolbar.setTitle(R.string.image_selector_title);

        //set back button
        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);

        //set onclick listener to go back when back button pressed
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButtonPressed();
            }
        });
    }

    /**
     *
     */
    public void backButtonPressed() {
        Fragment fragment = new NewFragment();

        ArrayList<ImageItem> imageItemsArray = new ArrayList<>(iga.getItemCount());
        imageItemsArray.addAll(iga.getImageItemList());

        arguments.putSerializable("images", imageItemsArray);

        fragment.setArguments(arguments);

        main.getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit(); //start and commit transaction to new fragment
    }

    /**
     *
     */
    public void onPickButtonClicked() {
        Button pickButton = view.findViewById(R.id.pickButton);

        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });
    }

    /**
     *
     */
    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(main);
        pictureDialog.setTitle("Select action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera"
        };

        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                }
        );

        pictureDialog.show();
    }

    /**
     *
     */
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    /**
     *
     */
    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

        //TODO fix orientation issue on some devices

        if (intent.resolveActivity(main.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(main,
                        "com.example.android.FileProvider",
                        photoFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(intent, CAMERA);
            }
        }

    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                Bitmap bitmap;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(main.getContentResolver(), contentURI);
                    addImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if(requestCode == CAMERA) {
            Bitmap rotatedBitmap = BitmapFactory.decodeFile(currentPhotoPath);

            ExifInterface ei = null;
            try {
                ei = new ExifInterface(currentPhotoPath);
            } catch (IOException e) {
                e.printStackTrace();
            }

            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            Bitmap bitmap = null;
            switch(orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(rotatedBitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(rotatedBitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(rotatedBitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    bitmap = rotatedBitmap;
            }

            addImage(bitmap);
        }
    }

    /**
     * Add new image to server
     *
     * @param bitmap
     */
    private void addImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 45, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);

        String url = "add_image/";

        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("bitmap", imageEncoded);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        api.request(url, jsonBody, new APICallback() {
            @Override
            public void onSuccessResponse(JSONObject response) {
                try {
                    updateImageList(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Update list with new added or removed image
     *
     * @param object
     * @throws JSONException
     */
    private void updateImageList(JSONObject object) throws JSONException {
        JSONObject error = object.getJSONObject("error");
        JSONObject data = object.getJSONObject("data");

        if(!error.getBoolean("error")) {
            List<ImageItem> newList = iga.getImageItemList();
            newList.add(new ImageItem(data.getInt("id"), data.getString("uri")));

            iga = new ImageGridAdapter(newList);
            rv.setAdapter(iga);
        }
    }

    /**
     * Create image file from bitmap on disk
     *
     * @return
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = main.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * Rotate bitmap image
     *
     * @param source
     * @param angle
     * @return
     */
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
