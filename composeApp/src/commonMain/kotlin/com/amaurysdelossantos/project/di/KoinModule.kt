package com.amaurysdelossantos.project.di

import com.amaurysdelossantos.project.database.getRoomDatabase
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module


expect val targetModule: Module

val sharedModule = module {
    single { getRoomDatabase(get()) }
    // single { get<BookDatabase>().bookDao() }
}

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null
) {
    startKoin {
        config?.invoke(this)
        modules(targetModule, sharedModule)
    }
}