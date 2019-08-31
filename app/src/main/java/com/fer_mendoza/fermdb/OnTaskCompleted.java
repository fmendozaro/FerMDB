package com.fer_mendoza.fermdb;

import java.util.ArrayList;
import java.util.HashMap;

public interface OnTaskCompleted {
    HashMap<String, String> params = new HashMap<>();
    void onTaskCompleted(String response, String type, ArrayList<String> favs);
}
