package com.sparken.parking.printer;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bbpos.simplyprint.SimplyPrintController.BatteryStatus;
import com.bbpos.simplyprint.SimplyPrintController.Error;
import com.bbpos.simplyprint.SimplyPrintController.PrinterResult;
import com.sparken.parking.common.CommonUtils;
import com.sparken.parking.printer.MswipePrinterListner.PRINTER_STATE;

import java.util.ArrayList;


public class PrinterFragmentActivity extends AppCompatActivity implements PrinterListener.OnPrinterConnectionError {
    private ServiceConnection serviceConnection;
    protected PrinterConnectionService printerConnectionService;
    private boolean isBound;

    @Override
    public void onPrinterConnectionError(MswipePrinterListner.PRINTER_CONNECTION_ERROR connection_ERROR,
                                         ArrayList<BluetoothDevice> devices) {
        try {
            String message = null;
            switch (connection_ERROR) {
                case WISEPAD_SWITCHED_OFF:
                    message = "Printer is off";
                    break;

                default:
                    break;
            }
            if (message != null) {
                showDialog(message);
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    @Override
    public void printerDisconnected() {

        Toast.makeText(this,"Printer Disconnected",Toast.LENGTH_LONG).show();
        CommonUtils.showToast(this,"Printer Disconnected");

        printerConnectionService.printerState = PRINTER_STATE.DISCONNECTED;
        printerConnectionService.reconnectToDevice();
    }

    @Override
    public void onBatteryLow(BatteryStatus batteryStatus) {
        showDialog("Printer Batter is low");
    }

    @Override
    public void onPrintResult(PrinterResult printResult) {
        if (printResult == PrinterResult.SUCCESS) {
            Toast.makeText(this,"Printing Success",Toast.LENGTH_LONG).show();
           /* Pref pref=new Pref(this);
            String id=pref.getCustomerIdFromPref();
            APPDatabase db=new APPDatabase(this);
            db.deleteCartContents(id);
            pref.clearPref();
            goToCustomerList();*/
        } else if (printResult == PrinterResult.NO_PAPER) {
            showDialog("No Paper to print");
        }
    }

    @Override
    public void onError(Error errorState) {
        String message = null;
        try {
            switch (errorState) {
                case COMM_LINK_UNINITIALIZED:
                    message = "Unable To Connect";
                    break;
                case DEVICE_BUSY:
                    message = "Device is busy";
                    break;
                default:
                    break;
            }
            if (message != null)
                showDialog(message);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

    @Override
    public void printerConnected() {
        printerConnectionService.printerState = PRINTER_STATE.CONNECTED;
        CommonUtils.showToast(this,"Printer Connected !");
    }

    @Override
    public void unknownState() {
        printerConnectionService.printerState = PRINTER_STATE.UNKNOWN;
        showDialog("Unable To Connect To Printer");
    }

    @Override
    public void onPrinterOperationEnd() {
        printerConnectionService.printerState = PRINTER_STATE.CONNECTED;
    }

    public void startPrinter() {

        serviceConnection = new ServiceConnection() {
            public void onServiceConnected(ComponentName className, IBinder service) {
                try {
                    PrinterConnectionService.LocalBinder localBinder = (PrinterConnectionService.LocalBinder) service;
                    printerConnectionService = localBinder.getService();
                    printerConnectionService.setPrinterListner(new PrinterListener(PrinterFragmentActivity.this));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            public void onServiceDisconnected(ComponentName className) {
                // This is called when the connection with the service has been
                // unexpectedly disconnected - process crashed.
                printerConnectionService = null;
            }
        };
        bindService(new Intent(this, PrinterConnectionService.class), serviceConnection, Context.BIND_AUTO_CREATE);
        isBound = true;
    }

    public void stopPrinter() {
        try {
            if (serviceConnection != null)
                unbindService(serviceConnection);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void printReceipt(final byte[] dataToBePrinted) {
        if (isBound && (printerConnectionService.printerState == PRINTER_STATE.CONNECTED
				|| printerConnectionService.isPrinterOn)) {
			printerConnectionService.printReceipt(dataToBePrinted);

		} else {
/*
			AlertDialog.Builder builder = new AlertDialog.Builder(PrinterFragmentActivity.this);

			builder.setTitle("! Your Printer is off,Start bluetooth and pair printer");
*/
            CommonUtils.showToast(PrinterFragmentActivity.this,"! Your Printer is not connected,Start bluetooth and pair printer");

		/*	final AlertDialog alertDialog = builder.setCancelable(true)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                        }
                    })
                    .setPositiveButton("Print", null).create();
			alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
				@Override
				public void onShow(DialogInterface dialog) {
					Button b = alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL);
					b.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							if (printerConnectionService.printerState == PRINTER_STATE.CONNECTED
									|| printerConnectionService.isPrinterOn) {
								alertDialog.dismiss();
								printReceipt(dataToBePrinted);

							} else {
                                CommonUtils.showToast(PrinterFragmentActivity.this,printerConnectionService.printerState.toString());
								printerConnectionService.reconnectToDevice();
							}
						}
					});
				}
			});
			alertDialog.show();*/
		}


        if (isBound
                && (printerConnectionService.printerState == PRINTER_STATE.CONNECTED || printerConnectionService.isPrinterOn)) {

            printerConnectionService.printReceipt(dataToBePrinted);

        } else
        {
            printerConnectionService.reconnectToDevice();
        }
    }



    protected void showDialog(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Printer");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}