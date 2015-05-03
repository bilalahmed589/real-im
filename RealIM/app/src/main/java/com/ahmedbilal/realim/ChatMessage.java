package com.ahmedbilal.realim;

import java.util.HashMap;

/**
 * Created by Ashi_3 on 5/2/2015.
 */
public class ChatMessage {
    private HashMap<String,String> where;
    private HashMap<String,String> data;

    public HashMap<String, String> getWhere() {
        return where;
    }

    public void setWhere(HashMap<String, String> where) {
        this.where = where;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }

}
