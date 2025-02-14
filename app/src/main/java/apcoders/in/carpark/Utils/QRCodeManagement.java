package apcoders.in.carpark.Utils;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.QRCodeWriter;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.squareup.picasso.Picasso;

import apcoders.in.carpark.models.BookingDetailsModel;

public class QRCodeManagement {

    public QRCodeManagement() {
    }

    public static Bitmap generateQRCodeBitmap(String jsonData) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            return barcodeEncoder.createBitmap(qrCodeWriter.encode(jsonData, BarcodeFormat.QR_CODE, 300, 300));
        } catch (WriterException e) {
            e.printStackTrace();
            Log.e("QR_ERROR", "Error generating QR Code: " + e.getMessage());
            return null;
        }
    }

    public static void displayQRCode(String url, ImageView qrImageView) {
        Picasso.get().load(url).into(qrImageView);
    }

    public static String convertBookingToJson(BookingDetailsModel booking) {
        Gson gson = new Gson();
        return gson.toJson(booking);

    }
}
