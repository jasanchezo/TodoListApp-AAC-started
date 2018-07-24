package com.mtw.juancarlos.todolistapp.database

import android.arch.persistence.room.*

// TODO (4) Crear interface TaskDao

@Dao // ANOTACION PRINCIPAL DE Room, @Entity
interface TaskDAO {

    @Query ("SELECT * FROM task ORDER BY priority")
    fun loadAllTask() : List<TaskEntry>

    @Insert
    fun insertTask(taskEntry: TaskEntry)

    @Update (onConflict = OnConflictStrategy.REPLACE)
    fun updateTask(taskEntry: TaskEntry)

    @Delete
    fun deleteTask(taskEntry: TaskEntry)

    // METODO GENERICO PARA ELIMINAR TODA LA DB
    @Query ("DELETE FROM task")
    fun deleteAll()

    // CONSULTA PARAMETRIZADA QUE RECIBE EL ID DE LA TAREA A CONSULTAR Y REGRESA UN OBJETO DE TIPO TaskEntry
    @Query("SELECT * FROM task WHERE id = :id")
    fun loadTaskById(id : Long) : TaskEntry
}