package com.mtw.juancarlos.todolistapp.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

// TODO (2) Annotate the class with Entity. Use "task" for the table name
@Entity (tableName = "task") // ANOTACION PRINCIPAL DE Room, @Entity
data class TaskEntry (
        // TODO (3) Annotate the id as PrimaryKey. Set autoGenerate to true.
        @PrimaryKey (autoGenerate = true)
        var id : Long = 0 ,
        var description : String,
        var priority : Int,

        @ColumnInfo (name = "updated_at") // ANOTACION PARA CAMBIAR EL NOMBRE DEL CAMPO, DE updatedAt A updated_at
        var updatedAt : Date
)
