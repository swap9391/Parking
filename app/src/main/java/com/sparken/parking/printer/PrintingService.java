package com.sparken.parking.printer;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.sparken.parking.common.CommonUtils;
import com.sparken.parking.constant.IConstants;
import com.sparken.parking.model.VehicleBean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PrintingService {

    public static PrintingService printingService = new PrintingService();

    private PrintingService() {
    }

    private static byte[] INIT = {0x1B, 0x40};
    private static byte[] POWER_ON = {0x1B, 0x3D, 0x01};
    // private static byte[] POWER_OFF = { 0x1B, 0x3D, 0x02 };
    private static byte[] NEW_LINE = {0x0A};
    private static byte[] ALIGN_LEFT = {0x1B, 0x61, 0x00};
    private static byte[] ALIGN_CENTER = {0x1B, 0x61, 0x01};
    // private static byte[] ALIGN_RIGHT = { 0x1B, 0x61, 0x02 };
    private static byte[] EMPHASIZE_ON = {0x1B, 0x45, 0x01};
    private static byte[] EMPHASIZE_OFF = {0x1B, 0x45, 0x00};
    // private static byte[] FONT_5X8 = { 0x1B, 0x4D, 0x00 };
    private static byte[] FONT_5X12 = {0x1B, 0x4D, 0x01};
    private static byte[] FONT_8X12 = {0x1B, 0x4D, 0x02};
    private static byte[] FONT_10X18 = {0x1B, 0x4D, 0x03};
    private static byte[] FONT_SIZE_0 = {0x1D, 0x21, 0x00};
    private static byte[] FONT_SIZE_1 = {0x1D, 0x21, 0x11};
    private static byte[] CHAR_SPACING_0 = {0x1B, 0x20, 0x00};

    // private static byte[] CHAR_SPACING_1 = { 0x1B, 0x20, 0x01 };


    public void writeFooter(ByteArrayOutputStream baos) throws IOException {
        baos.write(NEW_LINE);
        baos.write(ALIGN_CENTER);
        baos.write(FONT_8X12);
        baos.write("Manual tampering of the receipt not allowed.".getBytes());
        baos.write(NEW_LINE);
        baos.write(NEW_LINE);
        baos.write(NEW_LINE);
        baos.write(NEW_LINE);
        baos.write(NEW_LINE);
        baos.write(NEW_LINE);
        baos.write(NEW_LINE);
        baos.write(NEW_LINE);
    }

    private void writeLine(ByteArrayOutputStream baos, StringBuffer singleLine)
            throws IOException {
        baos.write(FONT_8X12);
        baos.write(singleLine.toString().getBytes());
        baos.write(NEW_LINE);
    }

    private StringBuffer getLine(int size0NoEmphasizeLineWidth, String character) {
        StringBuffer singleLine = new StringBuffer(size0NoEmphasizeLineWidth);
        for (int i = 0; i < size0NoEmphasizeLineWidth; ++i) {
            singleLine.append(character);
        }
        return singleLine;
    }

    private void writeHeader(ByteArrayOutputStream baos, Context context) throws IOException {
        baos.write(INIT);
        baos.write(POWER_ON);
        baos.write(NEW_LINE);
        baos.write(ALIGN_CENTER);
        baos.write(FONT_SIZE_1);
        baos.write(EMPHASIZE_ON);
        baos.write(FONT_5X12);
        baos.write(CHAR_SPACING_0);
        String header = CommonUtils.getSharedPref(IConstants.SH_PARKING_LOC, context) + " Parking";
        baos.write(header.getBytes());
        baos.write(EMPHASIZE_OFF);
        baos.write(NEW_LINE);
        baos.write(NEW_LINE);
    }

    private void writeRow(ByteArrayOutputStream baos, String tag, String value)
            throws IOException {
        if (!TextUtils.isEmpty(value)) {
            baos.write(FONT_10X18);
            baos.write(tag.getBytes());
            baos.write(EMPHASIZE_ON);
            baos.write(value.getBytes());
            baos.write(NEW_LINE);
            baos.write(EMPHASIZE_OFF);
        }

    }

    private static byte[] hexToByteArray(String s) {
        if (s == null) {
            s = "";
        }
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        for (int i = 0; i < s.length() - 1; i += 2) {
            String data = s.substring(i, i + 2);
            bout.write(Integer.parseInt(data, 16));
        }
        return bout.toByteArray();
    }

    private String getSpace(int numberOfSpaces) {
        int MaxLength = 26;
        int spaceNo = 0;
        if (numberOfSpaces < MaxLength) {
            spaceNo = MaxLength - numberOfSpaces;
        }
        String space = String.format("%" + spaceNo + "s", " ");
        return space;
    }


    public byte[] getTotalCollectionReport(VehicleBean bean, Context context) {
        ByteArrayOutputStream baos = null;
        byte[] dataToPrint = null;
        int lineWidth = 384;
        int size0NoEmphasizeLineWidth =
                lineWidth / 8;
        StringBuffer singleLine = null;
        StringBuffer doubleLine =
                null;
        try {
            baos = new ByteArrayOutputStream();
            singleLine = new
                    StringBuffer();
            doubleLine = new StringBuffer();
            singleLine =
                    getLine(size0NoEmphasizeLineWidth, "-");
            doubleLine =
                    getLine(size0NoEmphasizeLineWidth, "=");
            writeHeader(baos, context);
            String paystatus = null;
            paystatus = "Paid";


            baos.write(FONT_SIZE_0);
            writeSmallLine(baos, singleLine);
            baos.write(ALIGN_LEFT);
            // Receipt Number
            writeRow(baos, "Reciept No.  : ",
                    "PARK" + bean.getPk_parking_detail_id());
            writeRow(baos,
                    "IN TIME       : ", "" + bean.getIn_time());
            writeRow(baos,
                    "OUT TIME      : ", "" + bean.getOut_time());

            writeSmallLine(baos, singleLine);
            writeRow(baos, "Vehicle No.      : ",
                    "" + bean.getVehicle_no());


            writeRow(baos, "Parking Location : ", "" + CommonUtils.getSharedPref(IConstants.SH_PARKING_LOC, context));

            //Location header
            int fontSize = 1;
            int fontWidth = 10 *
                    (fontSize + 1) + (fontSize + 1);

            int numOfCharacterPerLine = lineWidth /
                    fontWidth;


            writeSmallLine(baos, singleLine);
            baos.write(FONT_10X18);
            String offenceNameHeading = "PAYMENT DETAIL";
            String offenceAmountHeading = "             AMOUNT";
            StringBuffer heading = new
                    StringBuffer(offenceNameHeading);

            for (int i = 0; i < numOfCharacterPerLine - offenceNameHeading.length() -
                    offenceAmountHeading.length(); ++i) {
                heading.append(" ");
            }
            heading.append(offenceAmountHeading);
            baos.write(heading.toString().getBytes());
            baos.write(NEW_LINE);
            writeSmallLine(baos, singleLine);
            baos.write(NEW_LINE);

            fontSize = 0;
            fontWidth = 10 * (fontSize + 1);
            numOfCharacterPerLine =
                    lineWidth / fontWidth;
            baos.write(FONT_10X18);


            //TowingCharges
            String TowingCharges = "" + bean.getParking_charges();
            ;
            StringBuffer row1 = new StringBuffer("Parking Charges");
            for (int i = 0; i < numOfCharacterPerLine - "Parking Charges".length() - TowingCharges.length(); ++i) {
                row1.append(" ");
            }
            row1.append(TowingCharges);

            baos.write(row1.toString().getBytes());
            baos.write(NEW_LINE);






			/*//Total
            String totalAmountHeading = "Total";
			String formatedAmount ="200.0";
			StringBuffer totalAmountRow = new StringBuffer(totalAmountHeading);
			for (int i = 0; i
					< numOfCharacterPerLine - totalAmountHeading.length() -
					formatedAmount.length(); ++i) {
				totalAmountRow.append(" ");
			}
			totalAmountRow.append(formatedAmount);
			baos.write(totalAmountRow.toString().getBytes());
			baos.write(NEW_LINE);*/
            writeSmallLine(baos, doubleLine);
            baos.write(NEW_LINE);


            writeSmallLine(baos, doubleLine);
            writeFooter(baos);
            dataToPrint = baos.toByteArray();
        } catch (Throwable throwable) {
            throwable.printStackTrace();

        }
        return dataToPrint;
    }


    private void writeSmallLine(ByteArrayOutputStream baos, StringBuffer singleLine) throws IOException {
        baos.write(FONT_8X12);
        baos.write(singleLine.toString().getBytes());
        baos.write(NEW_LINE);
    }

    private static byte[] convertBitmap(Bitmap bitmap, int targetWidth,
                                        int threshold) {
        int targetHeight = (int) Math.round((double) targetWidth
                / (double) bitmap.getWidth() * (double) bitmap.getHeight());

        byte[] pixels = new byte[targetWidth * targetHeight];
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth,
                targetHeight, false);
        for (int j = 0; j < scaledBitmap.getHeight(); ++j) {
            for (int i = 0; i < scaledBitmap.getWidth(); ++i) {
                int pixel = scaledBitmap.getPixel(i, j);
                int alpha = (pixel >> 24) & 0xFF;
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;
                if (alpha < 50) {
                    pixels[i + j * scaledBitmap.getWidth()] = 0;
                } else if ((r + g + b) / 3 >= threshold) {
                    pixels[i + j * scaledBitmap.getWidth()] = 0;
                } else {
                    pixels[i + j * scaledBitmap.getWidth()] = 1;
                }
            }
        }

        byte[] output = new byte[scaledBitmap.getWidth()
                * (int) Math.ceil((double) scaledBitmap.getHeight()
                / (double) 8)];

        for (int i = 0; i < scaledBitmap.getWidth(); ++i) {
            for (int j = 0; j < (int) Math.ceil((double) scaledBitmap
                    .getHeight() / (double) 8); ++j) {
                for (int n = 0; n < 8; ++n) {
                    if (j * 8 + n < scaledBitmap.getHeight()) {
                        output[i + j * scaledBitmap.getWidth()] |= pixels[i
                                + (j * 8 + n) * scaledBitmap.getWidth()] << (7 - n);
                    }
                }
            }
        }

        return output;
    }

    public static PrintingService getInstance(Context context) {
		/*printingService.userPreference = new Preference(context,
				PrefName.USER_PREFERENCE.toString());*/
        return printingService;
    }
}
