package com.githubusers.githubusers;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.githubusers.githubusers.api.Client;
import com.githubusers.githubusers.api.Service;
import com.githubusers.githubusers.model.Item;
import com.githubusers.githubusers.model.ItemResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    TextView Disconnecterd;
    private Item item;
    ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeContainer;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setColorSchemeColors(android.R.color.holo_orange_dark);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadJSON();
                Toast.makeText(MainActivity.this,"Github Users refreshed",Toast.LENGTH_SHORT).show();
            }
        });
    }

   private void initViews(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting GitHub Users . . . ");
        progressDialog.setCancelable(false);
        progressDialog.show();

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.smoothScrollToPosition(0);
        loadJSON();

    }

    private void loadJSON(){
     Disconnecterd = (TextView) findViewById(R.id.disconnected);
        try{
            Client Client = new Client();

        Service apiService = Client.getClient().create(Service.class);
            Call<ItemResponse> call = apiService.getItems();
            call.enqueue(new Callback<ItemResponse>() {
                @Override
                public void onResponse(@NonNull Call<ItemResponse> call,@NonNull Response<ItemResponse> response) {
                    List<Item> items = response.body().getItems();
                    recyclerView.setAdapter(new ItemAdapter(getApplicationContext(), items));
                    recyclerView.smoothScrollToPosition(0);
                    swipeContainer.setRefreshing(false);
                    progressDialog.hide();
                }

                @Override
                public void onFailure(Call<ItemResponse> call,Throwable t) {
                    Log.d("Error", t.getMessage());
                    Toast.makeText(MainActivity.this, "Loading failed", Toast.LENGTH_SHORT).show();
                    Disconnecterd.setVisibility(View.VISIBLE);
                    progressDialog.hide();

                }
            });
        } catch (Exception e){
            Log.d("Error exp",e.getMessage());
        Toast.makeText(this, e.toString(),Toast.LENGTH_SHORT).show();
        }

    }
}