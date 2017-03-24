package com.rakuishi.postalcode.di;

import android.app.Application;
import android.content.Context;

import com.rakuishi.postalcode.R;
import com.rakuishi.postalcode.model.OrmaDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private Context context;

    public AppModule(Application app) {
        context = app;
    }

    @Provides
    public Context provideContext() {
        return context;
    }

    @Singleton
    @Provides
    public OrmaDatabase provideOrmaDatabase(Context context) {
        String name = context.getString(R.string.database_name);
        return OrmaDatabase.builder(context).name(name).build();
    }
}
