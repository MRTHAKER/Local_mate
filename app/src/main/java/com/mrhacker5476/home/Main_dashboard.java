package com.mrhacker5476.home;

import android.R.layout;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Main_dashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Main_dashboard extends Fragment implements AsyncResponse{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView usersList;
    LocationBean lb;
    String file="home";
    ProgressDialog pd;


    public Main_dashboard() {
        // Required empty public constructor
    }

    public static Main_dashboard newInstance(String param1, String param2) {
        Main_dashboard fragment = new Main_dashboard();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_main_dashboard, container, false);
        usersList=v.findViewById(R.id.DashboardList);
        pd=new ProgressDialog(getContext());
        pd.setTitle("Processing, please wait...");
        pd.setCancelable(false);
        pd.show();
        new SqlCall(file,lb,this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        return v;
    }

    @Override
    public void processFinish(JSONObject jsonObject) throws JSONException {
        pd.dismiss();
        if(jsonObject.get("done").equals(true)) {
            Toast.makeText(getActivity().getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), layout.simple_list_item_1, getArray(jsonObject));
            usersList.setAdapter(adapter);
        }
        else Toast.makeText(getActivity().getApplicationContext(),"None is around you at the moment.",Toast.LENGTH_SHORT).show();
    }

    private ArrayList<String> getArray(JSONObject jsonObject) throws JSONException {
        ArrayList<String> arrayList = new ArrayList<>();
        JSONArray jsonArray=jsonObject.getJSONArray("array");
        for(int i=0;i<jsonArray.length();i++){
            arrayList.add(jsonArray.getString(i));
        }
        return arrayList;
    }
}
