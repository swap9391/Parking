package com.sparken.parking.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.sparken.parking.LoginActivity;
import com.sparken.parking.ParkingTabActivity;
import com.sparken.parking.R;
import com.sparken.parking.common.CommonUtils;
import com.sparken.parking.common.StringUtils;
import com.sparken.parking.constant.CallWebservice;
import com.sparken.parking.constant.IConstants;
import com.sparken.parking.constant.IJson;
import com.sparken.parking.constant.IUrls;
import com.sparken.parking.constant.VolleyResponseListener;
import com.sparken.parking.model.VehicleBean;
import com.sparken.parking.service.MyService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 6/2/17.
 */

public class VehicleOutFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private AutoCompleteTextView edt_vehi_no;
    ObjectAdapter objectAdapter;

    VehicleBean vehicleBean;
    List<VehicleBean> beanList;
    Button btnSearch;
    View dialogView;

    TextView txtdialogTitle;
    String vehicleNumber;
    LayoutInflater layoutInflater;

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        View view;
        layoutInflater = inflater;
        view = inflater.inflate(R.layout.tab_parking_out, container, false);

        dialogView = inflater.inflate(R.layout.dialog_title, null);


        vehicleBean = new VehicleBean();
        beanList = new ArrayList<>();
        beanList = getMyActivity().getDbInvoker().getVehicleByStatus(IConstants.STATUS_IN);


        edt_vehi_no = (AutoCompleteTextView) view.findViewById(R.id.txtVehinumber);


        btnSearch = (Button) view.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.btnSearch:
                if (edt_vehi_no.getText().toString() != null && !StringUtils.isEmpty(edt_vehi_no.getText().toString())) {
                    SeachVehicle();
                } else {
                    CommonUtils.showToast(getMyActivity(), "Please Enter Vehicle Number");
                }

                break;
        }

    }

    //save

    public void bindModel() {
        String vehino = edt_vehi_no.getText().toString();
        vehicleBean = getMyActivity().getDbInvoker().getVehicle(vehino);
        if (vehicleBean == null) {
            vehicleBean = new VehicleBean();
        }
    }

    private boolean check() {

        if (vehicleBean.getVehicle_no() == null || StringUtils.isEmpty(vehicleBean.getVehicle_no())) {
            CommonUtils.showToast(getMyActivity(), "Please Enter Vehicle Number");
            return false;
        }

        return true;
    }

    public void ParkRegistration() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(IJson.pk_parking_detail_id, "" + vehicleBean.getPk_parking_detail_id());
        hashMap.put(IJson.out_user_id, "" + CommonUtils.getSharedPref(getMyActivity(), IConstants.SH_USER_ID));


        CallWebservice.getWebservice(true, getMyActivity(), Request.Method.POST, IUrls.URL_UPDATE_PARKING, hashMap, new VolleyResponseListener<VehicleBean>() {
            @Override
            public void onResponse(VehicleBean[] object, String message) {

                if (object[0] instanceof VehicleBean) {
                    for (VehicleBean bean : object) {
                        // txtCapacity.setText(bean.getFilled() + "/" + bean.getCapacity());
                        bean.setVehicle_no(vehicleBean.getVehicle_no());
                        getMyActivity().printRecipt(bean);
                        Toast.makeText(getMyActivity(), "Vehicle Released Successfully", Toast.LENGTH_SHORT).show();
                        bean.setPk_parking_location_id(vehicleBean.getPk_parking_location_id());
                        bean.setVehicle_no(vehicleBean.getVehicle_no());
                        bean.setVehicle_type(vehicleBean.getVehicle_type());
                        bean.setMobile_no(vehicleBean.getMobile_no());
                        bean.setStatus(IConstants.STATUS_OUT);
                        bean.setId(vehicleBean.getId());
                        getMyActivity().getDbInvoker().insertUpdateVehicle(bean);


                        refresh();
                    }
                }
            }


            @Override
            public void onError(String message) {

            }
        }, VehicleBean[].class);

    }


    private void refresh() {
        edt_vehi_no.setText("");
        vehicleBean = new VehicleBean();
        getMyActivity().startService(new Intent(getMyActivity(), MyService.class));
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    //custom adapter
    public class ObjectAdapter extends BaseAdapter implements Filterable {

        private Context context;
        private List<String> originalList;
        private List<String> suggestions = new ArrayList<>();
        private Filter filter = new CustomFilter();

        /**
         * @param context      Context
         * @param originalList Original list used to compare in constraints.
         */
        public ObjectAdapter(Context context, List<String> originalList) {
            this.context = context;
            this.originalList = originalList;
        }

        @Override
        public int getCount() {
            return suggestions.size(); // Return the size of the suggestions list.
        }

        @Override
        public Object getItem(int position) {
            return suggestions.get(position);
        }


        @Override
        public long getItemId(int position) {
            return 0;
        }

        /**
         * This is where you inflate the layout and also where you set what you want to display.
         * Here we also implement a View Holder in order to recycle the views.
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = LayoutInflater.from(context);

            ViewHolder holder;

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.simple_list_item,
                        parent,
                        false);
                holder = new ViewHolder();
                holder.autoText = (TextView) convertView.findViewById(R.id.simpletext);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.autoText.setText(suggestions.get(position));

            return convertView;
        }


        @Override
        public Filter getFilter() {
            return filter;
        }

        private class ViewHolder {
            TextView autoText;
        }

        /**
         * Our Custom Filter Class.
         */
        private class CustomFilter extends Filter {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                suggestions.clear();

                if (originalList != null && constraint != null) { // Check if the Original List and Constraint aren't null.
                    for (int i = 0; i < originalList.size(); i++) {
                        if (originalList.get(i).toLowerCase().contains(constraint) || originalList.get(i).toUpperCase().contains(constraint)) { // Compare item in original list if it contains constraints.
                            suggestions.add(originalList.get(i)); // If TRUE add item in Suggestions.
                        }
                    }
                }
                FilterResults results = new FilterResults(); // Create new Filter Results and return this to publishResults;
                results.values = suggestions;
                results.count = suggestions.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        }
    }


    public void SeachVehicle() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(IJson.vehi_no, "" + edt_vehi_no.getText().toString());
        hashMap.put(IJson.parking_location_id, "" + CommonUtils.getSharedPref(getMyActivity(), IConstants.SH_PARKING_ID));
        hashMap.put(IJson.out_user_id, "" + CommonUtils.getSharedPref(getMyActivity(), IConstants.SH_USER_ID));


        CallWebservice.getWebservice(true, getMyActivity(), Request.Method.POST, IUrls.URL_SEARCH_VEHI, hashMap, new VolleyResponseListener<VehicleBean>() {
            @Override
            public void onResponse(VehicleBean[] object, String message) {

                if (object[0] instanceof VehicleBean) {

                    List<VehicleBean> lst = new ArrayList<VehicleBean>();
                    for (VehicleBean bean : object) {

                        lst.add(bean);

                        // txtCapacity.setText(bean.getFilled() + "/" + bean.getCapacity());




                         /*   Toast.makeText(getMyActivity(), "Vehicle Released Successfully", Toast.LENGTH_SHORT).show();
                            bean.setParking_location_id(vehicleBean.getParking_location_id());
                            bean.setVehicle_no(vehicleBean.getVehicle_no());
                            bean.setVehicle_type(vehicleBean.getVehicle_type());
                            bean.setMobile_no(vehicleBean.getMobile_no());
                            bean.setIn_time(vehicleBean.getIn_time());
                            bean.setStatus(IConstants.STATUS_OUT);
                            bean.setId(vehicleBean.getId());
                            getMyActivity().getDbInvoker().insertUpdateVehicle(bean);

                            getMyActivity().printRecipt(bean);

                            refresh();*/
                    }


                    selectVehicle(lst);

                }
            }


            @Override
            public void onError(String message) {

            }
        }, VehicleBean[].class);

    }


    public static AlertDialog.Builder showAlertDialog(Context context, String string) {
        AlertDialog.Builder alertDialog = null;
        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Outgoing Vehicle\n" + string);
        return alertDialog;
    }

    private void OutDialog(String string) {

        try {

            AlertDialog.Builder builder = showAlertDialog(getMyActivity(), string);
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ParkRegistration();

                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            }).show();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }


    private void selectVehicle(final List<VehicleBean> type1) {

        final String[] labels = new String[type1.size()];
        int checked = 0;
        for (int i = 0; i < type1.size(); i++) {


            String string = type1.get(i).getVehicle_no();
            if (string.length() == 10) {
                String str1 = string.substring(0, 2);
                String str2 = string.substring(2, 4);
                String str3 = string.substring(4, 6);
                String str4 = string.substring(6);

                String finalString = str1 + "-" + str2 + "-" + str3 + "-" + str4;
                labels[i] = finalString;
            } else {
                labels[i] = string;
            }


            if (type1.get(i).equals(vehicleNumber)) {
                checked = i;
            }
        }


        AlertDialog dlg = new AlertDialog.Builder(getActivity())
                .setCustomTitle(dialogView)
                .setItems(labels, new DialogInterface.OnClickListener() {

                    // input = new
                    // EditText(getActivity()).setView(input);

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                        vehicleBean.setVehicle_no(type1.get(which).getVehicle_no());
                        vehicleBean.setPk_parking_detail_id(type1.get(which).getPk_parking_detail_id());
                        vehicleNumber = type1.get(which).getVehicle_no();
                        OutDialog(vehicleNumber);

                               /* ddUserType.setText(type1.get(which));
                                mobileLoginRequest.setUserType(type1.get(which));

                                UserTypeChange(type1.get(which));
*/
                    }
                }).create();
        dlg.show();
        dialogView = layoutInflater.inflate(R.layout.dialog_title, null);


    }


    private ParkingTabActivity getMyActivity() {
        return (ParkingTabActivity) getActivity();
    }

}