package br.com.codinglab.myapp.web;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.codinglab.myapp.R;
import br.com.codinglab.myapp.model.Contact;

public class WebTaskContacts extends WebTaskBase {

    private static final String URL_SERVICE = "contacts";
    private static final String FIELD_TOKEN = "token";

    private String token;

    public WebTaskContacts(Context context, String token) {
        super(context, URL_SERVICE);
        this.token = token;
    }

    @Override
    void handleResponse(String response) {
        Gson gson = new Gson();
        List<Contact> contactList;
        Type listType = new TypeToken<List<Contact>>() {}.getType();

        try {
            contactList  = gson.fromJson(response, listType);
            EventBus.getDefault().post(contactList);
        } catch (JsonSyntaxException x) {
            Error error = new Error(getContext().getString(R.string.error_json));
            EventBus.getDefault().post(error);
        }
    }

    @Override
    String getRequestBody() {
        Map<String,String> requestMap = new HashMap<>();
        requestMap.put(FIELD_TOKEN, token);

        JSONObject json = new JSONObject(requestMap);
        String jsonString = json.toString();

        return  jsonString;
    }
}
