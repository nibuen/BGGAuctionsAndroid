package com.boarbeard.bggauctions.io;

import com.boarbeard.bggauctions.model.GeekListsResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by cott on 11/15/16.
 */

public interface BggService {

    @GET("/geeklist/module?ajax=1&domain=boardgame&nosession=1&showcount=12&tradelists=0&version=v2&comments=1")
    Observable<GeekListsResponse> geekLists();
}
