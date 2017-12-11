package org.home.geonamesdemo.api;

import org.home.geonamesdemo.direction_model.DirectionsServiceResponse;
import org.home.geonamesdemo.direction_model.GeoLatLng;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Kelvin on 10/12/2017.
 */

public interface DirectionsService {

    @GET("json?sensor=false&units=metric")
    Call<DirectionsServiceResponse> buscarRuta(@Query("origin") GeoLatLng geoLatLngOrigin,
                                               @Query("destination") GeoLatLng geoLatLngDestiny,
                                               @Query("mode") String mode,
                                               @Query("key") String key);
}
