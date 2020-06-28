package com.example.movieapp.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.example.movieapp.service.MovieDataService;

public class MovieDataSourceFactory extends DataSource.Factory {

    private Application application;
    private MovieDataSource movieDataSource;
    private MovieDataService movieDataService;
    private MutableLiveData<MovieDataSource> mutableLiveData;

    public MovieDataSourceFactory(Application application, MovieDataService movieDataService) {
        this.application = application;
        this.movieDataService = movieDataService;

        mutableLiveData = new MutableLiveData<>();
    }



    @NonNull
    @Override
    public DataSource create() {

        movieDataSource = new MovieDataSource(movieDataService,application);
        mutableLiveData.postValue(movieDataSource);
        return movieDataSource;
    }

    public MutableLiveData<MovieDataSource> getMutableLiveData() {
        return mutableLiveData;
    }
}
