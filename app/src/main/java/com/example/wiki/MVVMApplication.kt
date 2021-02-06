package com.example.wiki

import androidx.multidex.MultiDexApplication
import com.example.wiki.data.db.AppDatabase
import com.example.wiki.data.network.WikiApi
import com.example.wiki.data.repositories.ArticlesRepository
import com.example.wiki.ui.ArticlesViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MVVMApplication : MultiDexApplication(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@MVVMApplication))

        bind() from singleton { WikiApi() } //For Retrofit Interface that contains all API endpoints
        bind() from singleton { AppDatabase(instance()) } //For Room Database class

        //for the whole article module (MainActivity)
        bind() from singleton { ArticlesRepository(instance(),instance()) } // For ArticlesRepository
        bind() from provider { ArticlesViewModelFactory(instance()) } //For AriclesViewModelFactory



    }
}