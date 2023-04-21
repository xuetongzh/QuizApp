package com.google.quizapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import com.kongzue.dialogx.dialogs.CustomDialog;
import com.kongzue.dialogx.interfaces.OnBindView;
import com.permissionx.guolindev.PermissionX;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private Button start, tools;
    private EditText userName;
    private PreviewView textPreview;
    private ExecutorService cameraExecutor;
    private ProcessCameraProvider cameraProvider;
    private TextView tips;
    private String username;
    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermissions();
        userName = findViewById(R.id.username);
        start = findViewById(R.id.start);
        tools = findViewById(R.id.tools);

        customDialog = CustomDialog.build().setCustomView(new OnBindView<CustomDialog>(R.layout.item_dialog) {
            @Override
            public void onBind(CustomDialog dialog, View v) {
                textPreview = v.findViewById(R.id.face_preview);
                tips = v.findViewById(R.id.tips);
                startCamera();
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = userName.getText().toString();
                if (username.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                intent.putExtra("userName", username);
                startActivity(intent);
                //customDialog.show();
            }
        });

        tools.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, TextActivity.class)));
    }

    private void closeCamera() {
        cameraExecutor.shutdown();
        cameraProvider.unbindAll();
    }

    private void startCamera() {
        //Before starting, make sure there are no other camera instances are running
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(getApplicationContext());

        cameraProviderFuture.addListener(() -> {
            try {
                cameraExecutor = Executors.newSingleThreadExecutor();
                cameraProvider = cameraProviderFuture.get();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(textPreview.getSurfaceProvider());

                // Select camera by requesting the orientation of the lens
                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                        .build();

                cameraProvider.unbindAll();

                ImageAnalysis imageAnalyzer = new ImageAnalysis.Builder()
                        .setTargetAspectRatio(AspectRatio.RATIO_4_3)
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();
                imageAnalyzer.setAnalyzer(cameraExecutor, new FaceAnalyzer());
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer);
            } catch (Exception exc) {
                Log.e("", "Use case binding failed", exc);
            }
        }, ContextCompat.getMainExecutor(getApplicationContext()));
    }

    private int i = 0;

    private class FaceAnalyzer implements ImageAnalysis.Analyzer {
        private final FaceDetector detector;

        FaceAnalyzer() {
            FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                    .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
                    .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                    .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                    .build();

            detector = FaceDetection.getClient(options);
        }

        @Override
        public void analyze(ImageProxy imageProxy) {
            @SuppressLint("UnsafeOptInUsageError")
            Image mediaImage = imageProxy.getImage();
            if (mediaImage != null) {
                InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());

                detector.process(image)
                        .addOnSuccessListener(faces -> {
                            if (faces.size() != 0) {
                                Face face = faces.get(0);
                                if (face.getHeadEulerAngleY() < 10 && face.getHeadEulerAngleY() > -10) {
                                    i++;
                                    tips.setText("Recognizing...");
                                    if (i == 20) {
                                        customDialog.dismiss();
                                        i = 0;
                                        Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                                        intent.putExtra("userName", username);
                                        startActivity(intent);
                                    }
                                } else {
                                    tips.setText("Please keep facing the camera");
                                    i = 0;
                                }
                            }
                            imageProxy.close();
                        })
                        .addOnFailureListener(e -> imageProxy.close());
            }
        }
    }

    private void initPermissions() {
        PermissionX.init(this)
                .permissions(Manifest.permission.CAMERA)
                .request((allGranted, grantedList, deniedList) -> {
                    if (!allGranted) {
                        Toast.makeText(this, "The following permissions are prohibited from running" + grantedList + "Please enable and restart the app", Toast.LENGTH_LONG).show();
                    }
                });
    }
}