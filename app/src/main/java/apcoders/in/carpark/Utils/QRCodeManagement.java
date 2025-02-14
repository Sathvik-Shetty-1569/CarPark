package apcoders.in.carpark.Utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class QRCodeManagement {

    public QRCodeManagement(){
}

public static void generateQRCode(String data, ImageView qrImageView) {
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    try {
        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
        Bitmap bitmap = barcodeEncoder.createBitmap(qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, 300, 300));
        qrImageView.setImageBitmap(bitmap);
    } catch (WriterException e) {
        e.printStackTrace();
    }
}
}
