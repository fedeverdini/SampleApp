package com.example.sampleapp.injection

import android.app.Application
import com.example.sampleapp.datasource.IProductRepo
import com.example.sampleapp.datasource.ProductRepo
import com.example.sampleapp.datasource.local.IProductsCache
import com.example.sampleapp.datasource.local.ProductsCache
import com.example.sampleapp.datasource.remote.ProductsApi
import com.example.sampleapp.db.RateListDao
import com.example.sampleapp.db.TransactionListDao
import com.example.sampleapp.db.SampleDatabase
import com.example.sampleapp.network.retrofit.RetrofitFactory
import com.example.sampleapp.ui.transaction.details.TransactionDetailsViewModel
import com.example.sampleapp.ui.transaction.list.TransactionListViewModel
import com.example.sampleapp.ui.transaction.rates.RateListViewModel
import com.example.sampleapp.usecase.*
import com.example.sampleapp.utils.amount.AmountUtils
import com.example.sampleapp.utils.amount.IAmountUtils
import com.example.sampleapp.utils.date.DateUtils
import com.example.sampleapp.utils.date.IDateUtils
import com.example.sampleapp.utils.constants.NetworkUrl
import com.example.sampleapp.utils.encrypt.EncryptUtils
import com.example.sampleapp.utils.encrypt.IEncryptUtils
import com.example.sampleapp.utils.network.INetworkUtils
import com.example.sampleapp.utils.network.NetworkUtils
import com.example.sampleapp.utils.preferences.IPreferenceUtils
import com.example.sampleapp.utils.preferences.PreferenceUtils
import com.example.sampleapp.utils.strings.IStringUtils
import com.example.sampleapp.utils.strings.StringUtils
import com.example.sampleapp.utils.viewpager.IViewPagerUtils
import com.example.sampleapp.utils.viewpager.ViewPagerUtils
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val utilsModule = module {
    single<INetworkUtils> { NetworkUtils() }
    single<IEncryptUtils> { EncryptUtils() }
    single<IDateUtils> { DateUtils() }
    single<IStringUtils> { StringUtils() }
    single<IViewPagerUtils> { ViewPagerUtils() }
    single<IPreferenceUtils> { PreferenceUtils(get()) }
    single<IAmountUtils> { AmountUtils() }
}

val databaseModule = module {
    fun provideDatabase(application: Application): SampleDatabase {
        return SampleDatabase.buildDatabase(application)
    }

    fun provideRateListDao(database: SampleDatabase): RateListDao {
        return database.rateListDao()
    }

    fun provideTransactionListViewDao(database: SampleDatabase): TransactionListDao {
        return database.transactionListDao()
    }

    single { provideDatabase(androidApplication()) }
    single { provideRateListDao(get()) }
    single { provideTransactionListViewDao(get()) }
}

val apiModule = module {
    factory<ProductsApi> {
        RetrofitFactory().makeRetrofit(NetworkUrl.BASE_URL).create(ProductsApi::class.java)
    }
}

val cacheModule = module {
    factory<IProductsCache> {
        ProductsCache(get(), get())
    }
}

val repoModule = module {
    factory<IProductRepo> {
        ProductRepo(get(), get())
    }
}

val useCaseModule = module {
    factory<IGetRateListUseCase> { GetRateListUseCase(get()) }
    factory<IGetTransactionListUseCase> { GetTransactionListUseCase(get()) }
    factory<IGetTransactionDetailsUseCase> { GetTransactionDetailsUseCase(get()) }
}

val viewModelsModule = module {
    viewModel {
        TransactionListViewModel(get(), Dispatchers)
    }

    viewModel {
        TransactionDetailsViewModel(get(), get(), Dispatchers)
    }

    viewModel {
        RateListViewModel(get(), Dispatchers)
    }
}