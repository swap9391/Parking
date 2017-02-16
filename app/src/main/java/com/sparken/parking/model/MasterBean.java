package com.sparken.parking.model;

import com.sparken.parking.database.ContentHolder;

/**
 * Created by root on 6/2/17.
 */

public class MasterBean extends BasicBean {
    private String value;
    private String type;

    public MasterBean( String value,String type){
        this.value = value;
        this.type = type;
    }
    public MasterBean(){

    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public void dbBinding(ContentHolder holder) {
        super.dbBinding(holder);
        holder.setTable("state_code");
        holder.getValues().put("_id", getId());
        holder.getValues().put("code", getValue());
        holder.getValues().put("type", getType());

    }
}
