package com.mtw.juancarlos.todolistapp.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context

// TODO (5) Crear clase AppDatabase : RoomDatabase

// ANOTACION PRINCIPAL DE Room, @Database
@Database (entities = arrayOf(TaskEntry::class), version = 1, exportSchema = false)

// SE DECLARA PARA QUE LA @Database TENGO IDENTIFICADO A SU CONVERTERS
@TypeConverters(DateConverter::class)

abstract class AppDatabase : RoomDatabase() {

    // TIENE QUE SER DE TIPO singleton (ESTATICO) POR CUESTIONES DE QUE SÓLO SEA UNA FORMA DE ACCEDER A UNA DB
    companion object {
        private var Instance : AppDatabase? = null

        fun getInstance(context : Context) : AppDatabase? {
            if (Instance == null) {
                synchronized(AppDatabase::class) {
                    Instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            "todoList.db"   // NOMBRE DE LA BASE DE DATOS, QUE EN REALIDAD ES UN ARCHIVO
                    ).build() // .allowMainThreadQueries() // TODO (7) Habilitar AllowMainThreadQuery
                }
            }
            return Instance
        }
    }

    // ES NECESARIO ESTE MAPEO PARA QUE LO TENGA IDENTIFICADO EN EL CONTEXTO DE LA APP
    abstract fun taskDao() : TaskDAO
}