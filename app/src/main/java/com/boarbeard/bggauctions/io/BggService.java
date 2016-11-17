package com.boarbeard.bggauctions.io;

import com.boarbeard.bggauctions.model.GeekList;
import com.boarbeard.bggauctions.model.GeekListsResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by cott on 11/15/16.
 */
public interface BggService {

    @GET("/geeklist/module?ajax=1&domain=boardgame&nosession=1&showcount=12&tradelists=1&version=v2")
    Observable<GeekListsResponse> geekListsFrontPage();

    @GET("/xmlapi/geeklist/{id}?comments=1")
    Observable<GeekList> geekList(@Path("id") int geeklistId);
}
