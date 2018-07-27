package com.githubusers.githubusers.api;

import com.githubusers.githubusers.model.ItemResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {
    @GET("/search/users?q=language:java+location:Armenia")
    Call<ItemResponse> getItems();

}
