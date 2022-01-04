package com.firoz.mahmud.bauet;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Pair;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

public class FaceRecogniger {
    int intValues[];
    Interpreter tflite;
    private static final int OUTPUT_SIZE = 192;
    private static final float IMAGE_MEAN = 128.0f;
    private static final float IMAGE_STD = 128.0f;
    private static final int inputSize = 112;



    public static Bitmap cropbit(Bitmap bit, Rect r){
        Bitmap resultBmp = Bitmap.createBitmap(r.right-r.left, r.bottom-r.top, Bitmap.Config.ARGB_8888);
        new Canvas(resultBmp).drawBitmap(bit,-r.left,-r.top,new Paint());
        return resultBmp;
    }

    private MappedByteBuffer loadModelFile(AssetManager assets)
            throws Exception {
        AssetFileDescriptor fileDescriptor = assets.openFd("mobile_face_net.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }
    public float caldis(float[] emb,float[] emb1) {
            float distance = 0;
            for (int i = 0; i < emb.length; i++) {
                float diff = emb[i] - emb1[i];
                distance += diff*diff;
            }
            distance = (float) Math.sqrt(distance);
            return distance;
    }
    public FaceRecogniger(AssetManager assets) throws Exception {
        tflite=new Interpreter(loadModelFile(assets));
        tflite.setNumThreads(4);
    }

    public float[] getimagedata(Bitmap bitmap) {
        ByteBuffer imgData=ByteBuffer.allocateDirect(1 * inputSize * inputSize * 3 * 4);
        imgData.order(ByteOrder.nativeOrder());
        intValues=new int[inputSize * inputSize];
        bitmap=Bitmap.createScaledBitmap(bitmap,inputSize,inputSize,false);
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        imgData.rewind();
        for (int i = 0; i < inputSize; ++i) {
            for (int j = 0; j < inputSize; ++j) {
                int pixelValue = intValues[i * inputSize + j];
                    imgData.putFloat((((pixelValue >> 16) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                    imgData.putFloat((((pixelValue >> 8) & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                    imgData.putFloat(((pixelValue & 0xFF) - IMAGE_MEAN) / IMAGE_STD);
                }
            }




        Object[] inputArray = {imgData};


        Map<Integer, Object> outputMap = new HashMap<>();

        float embeedings[][] = new float[1][OUTPUT_SIZE];
        outputMap.put(0, embeedings);


        tflite.runForMultipleInputsOutputs(inputArray, outputMap);

        return embeedings[0];

    }

}
