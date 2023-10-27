package com.stickebox.today

import android.app.Application
import com.stickebox.common.Database
import com.stickebox.common.Repository
import com.stickebox.food.AddFoodItemViewModel
import com.stickebox.food.FoodScreenViewModel
import com.stickebox.home.ui.HomeScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

@Module
@ComponentScan
class DatabaseModule

val appModule = module {
    single { Database() }
    single { Repository(get()) }
    viewModel { HomeScreenViewModel(get()) }
    viewModel { AddFoodItemViewModel(get()) }
    viewModel { FoodScreenViewModel(get()) }
}

class TodoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@TodoApplication)
            modules(appModule)
        }
    }
}