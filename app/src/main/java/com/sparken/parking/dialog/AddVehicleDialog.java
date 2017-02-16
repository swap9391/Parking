package com.sparken.parking.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.sparken.parking.MainActivity;
import com.sparken.parking.R;
import com.sparken.parking.common.CommonUtils;
import com.sparken.parking.model.VehicleBean;

import java.util.Calendar;


public class AddVehicleDialog extends DialogFragment implements
		OnClickListener {

	EditText  txtVehiNumber;
	Button btnSave;
	TextView txttimestamp;
	private OnDataChange dataChangeListner;
    int type;


	public static AddVehicleDialog createInstance(int type,
			OnDataChange listner) {
		AddVehicleDialog dlg = new AddVehicleDialog();
		//dlg.product = product;
		dlg.dataChangeListner = listner;
		dlg.type = type;
		// int remoteid=product.getRemoteId();
		return dlg;
	
	}
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
        View view = inflater.inflate(R.layout.add_vehicle_dialog, container);
        view.findViewById(R.id.btnSave).setOnClickListener(this);
        view.setBackgroundColor(Color.WHITE);
        getDialog().setTitle("Add Vehicle");
		txtVehiNumber= (EditText)view.findViewById(R.id.txtVehicleNumber);
		btnSave= (Button) view.findViewById(R.id.btnSave);
        txttimestamp = (TextView) view.findViewById(R.id.txtTimestamp);
		txttimestamp.setText(CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(),"dd MMM yyyy hh:mm:ss") );
        return view;
    }


    
    
    
	
	@Override
	public void onStart() {
		super.onStart();

		bindView();
	}
	

	private void bindView() {/*
		getDialog().setTitle("Add Competitor");
		
		if(productCompetitor!=null){
		
			txtBrand.setText(CommonUtils.emptyIfNull(productCompetitor.getBrand()));
			txtCompany.setText(CommonUtils.emptyIfNull(productCompetitor.getCompany()));
			txtPrice.setText(productCompetitor.getPrice()!=null?productCompetitor.getPrice()+"":"");
			txtQty.setText(productCompetitor.getQty()!=null?productCompetitor.getQty()+"":"");
		
		}
	*/}

	public MainActivity getMyActivity() {
		return (MainActivity) getActivity();
	}

	/*private void save() {

		VehicleBean vehicleBean = new VehicleBean();
		vehicleBean.setVehicle_number(txtVehiNumber.getText().toString());
		vehicleBean.setIn_time_stamp(CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(),"ddMMMyyyyhh:mm:ss") );



			dataChangeListner.refresh();

		getDialog().dismiss();
}*/

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btnSave: 

			break;
		
		}

	}

	
	
	
}
