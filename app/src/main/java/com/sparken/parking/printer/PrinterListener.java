package com.sparken.parking.printer;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.bbpos.simplyprint.SimplyPrintController.BatteryStatus;
import com.bbpos.simplyprint.SimplyPrintController.Error;
import com.bbpos.simplyprint.SimplyPrintController.PrinterResult;

import java.util.ArrayList;
import java.util.Hashtable;

public class PrinterListener extends MswipePrinterListner {

	public interface OnPrinterConnectionError {
		void onPrinterConnectionError(PRINTER_CONNECTION_ERROR state, ArrayList<BluetoothDevice> devices);

		void onBatteryLow(BatteryStatus batteryStatus);

		void printerDisconnected();

		void printerConnected();

		void onPrintResult(PrinterResult printResult);

		void onError(Error errorState);

		void unknownState();

		void onPrinterOperationEnd();
	}

	private Context context;
	private OnPrinterConnectionError onPrinterConnectionError;

	public PrinterListener(Context context) {
		super();
		this.context = context;
		onPrinterConnectionError = (OnPrinterConnectionError) context;
	}

	@Override
	public void onRegisterd() {
		super.onRegisterd();

	}

	@Override
	public void onUnRegisterd() {
		super.onUnRegisterd();
	}

	@Override
	public void onPrinterStateChanged(PRINTER_STATE state) {
		super.onPrinterStateChanged(state);
		if (state == PRINTER_STATE.WAITINGFORCONNECTION) {
			//Toast.makeText(context, "Waiting For Connection", Toast.LENGTH_SHORT).show();
		} else if (state == PRINTER_STATE.CONNECTED) {
			onPrinterConnectionError.printerConnected();
			new Thread() {
				public void run() {
					try {
						sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				};
			}.start();
		} else if (state == PRINTER_STATE.DISCONNECTED) {
			//Toast.makeText(context, "Printer Disconnected", Toast.LENGTH_SHORT).show();
		} else if (state == PRINTER_STATE.PRINTING) {
			//Toast.makeText(context, "Printing", Toast.LENGTH_SHORT).show();
		} else if (state == PRINTER_STATE.UNKNOWN) {
			onPrinterConnectionError.unknownState();
		}
	}

	@Override
	public void onPrinterConnectionError(PRINTER_CONNECTION_ERROR state, ArrayList<BluetoothDevice> devices) {
		super.onPrinterConnectionError(state, devices);
		onPrinterConnectionError = (OnPrinterConnectionError) context;
		onPrinterConnectionError.onPrinterConnectionError(state, devices);

	}

	@Override
	public void onReturnDeviceInfo(Hashtable<String, String> deviceInfoTable) {

	}

	@Override
	public void onReturnPrinterResult(PrinterResult printerResult) {
		onPrinterConnectionError.onPrintResult(printerResult);
	}

	@Override
	public void onPrinterOperationEnd() {
		onPrinterConnectionError.onPrinterOperationEnd();
	}

	@Override
	public void onBatteryLow(BatteryStatus batteryStatus) {
		onPrinterConnectionError.onBatteryLow(batteryStatus);
	}

	@Override
	public void onError(Error errorState) {
		onPrinterConnectionError.onError(errorState);
	}

}
