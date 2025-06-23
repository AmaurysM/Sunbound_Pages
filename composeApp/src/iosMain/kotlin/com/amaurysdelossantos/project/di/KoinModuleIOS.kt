package com.amaurysdelossantos.project.di

import com.amaurysdelossantos.project.database.getDatabaseBuilder
import org.koin.dsl.module

actual val targetModule = module {
    single { getDatabaseBuilder() }
}