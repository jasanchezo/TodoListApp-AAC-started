package com.mtw.juancarlos.todolistapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.PersistableBundle
import android.view.View
import kotlinx.android.synthetic.main.activity_add_task.*
import android.widget.RadioGroup
import com.mtw.juancarlos.todolistapp.database.AppDatabase
import com.mtw.juancarlos.todolistapp.database.TaskEntry
import com.mtw.juancarlos.todolistapp.helper.doAsync
import java.util.*


class AddTaskActivity : AppCompatActivity() {

    companion object {
        // Extra for the task ID to be received in the intent
        val EXTRA_TASK_ID = "extraTaskId"
        // Extra for the task ID to be received after rotation
        val INSTANCE_TASK_ID = "instanceTaskId"
        // Constants for priority
        val PRIORITY_HIGH = 1
        val PRIORITY_MEDIUM = 2
        val PRIORITY_LOW = 3
        // Constant for default task id to be used when not in update mode
        private val DEFAULT_TASK_ID = -1
        // Constant for logging
        private val TAG = AddTaskActivity::class.java.simpleName
    }

    private var mTaskId = DEFAULT_TASK_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        saveButton.setOnClickListener {
            onSaveButtonClicked()
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(INSTANCE_TASK_ID)) {
            mTaskId = savedInstanceState.getInt(INSTANCE_TASK_ID, DEFAULT_TASK_ID)
        }

        val intent = intent
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            saveButton.text = getString(R.string.update_button).toString()
            if (mTaskId === DEFAULT_TASK_ID) {
                // populate the UI
                // RECUPERAMOS EL OBJETO A PARTIR DEL ID EN EL Intent
                mTaskId = intent.getLongExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID.toLong()).toInt()

                // SE RECUPERAN LOS DATOS DE LA DB SQLITE
                doAsync {
                    val task = AppDatabase.getInstance(this@AddTaskActivity)?.taskDao()?.loadTaskById(mTaskId.toLong())
                    // PARA QUE SE SINCRONICE CON EL HILO DE LA INTERFASE GRAFICA
                    runOnUiThread {
                        populateUI(task!!)
                    }
                }.execute()
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        outState?.putInt(INSTANCE_TASK_ID, mTaskId)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    /**
     * populateUI would be called to populate the UI when in update mode
     *
     * @param task the taskEntry to populate the UI
     */
    private fun populateUI(task: TaskEntry) {
        // NOS ASEGURAMOS QUE LA TAREA TENGA DATOS
        if (task == null) return

        // SI TIENE DATOS ENTONCES SE ACTUALIZA LA INFORMACIÓN EN LOS VIEWS
        editTextTaskDescription.setText(task.description)
        setPriorityInViews(task.priority)
    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    fun onSaveButtonClicked() {
        // TODO (8) Save Task
        val descripcion = editTextTaskDescription.text.toString()
        val prioridad = getPriorityFromViews()

        // SE CONSTRUYE EL OBJETO DE LA CLASE A INSERTAR
        val taskEntry = TaskEntry(description = descripcion, priority = prioridad, updatedAt = Date())

        // ESTO ES PARA QUE SE EJECUTE DE MANERA ASÍNCRONA EN OTRO HILO TODO EL CODIGO CONTENIDO
        doAsync {
            if (mTaskId == DEFAULT_TASK_ID) {
                // ES UNA INSERCION
                AppDatabase.getInstance(this)!!.taskDao().insertTask(taskEntry)
            } else {
                // ES UNA ACTUALIZACION A PARTIR DEL ID DE LA TASK
                taskEntry.id = mTaskId.toLong()
                AppDatabase.getInstance(this)!!.taskDao().updateTask(taskEntry)
            }
            finish() // DE ESTA MANERA SE SALDRÁ DEL ACTIVITY SOLO CUANDO SE HAYA REALIZADO LA TRANSACCION
        }.execute()
    }

    /**
     * getPriority is called whenever the selected priority needs to be retrieved
     */
    fun getPriorityFromViews(): Int {
        var priority = 1
        val checkedId = (findViewById<View>(R.id.radioGroup) as RadioGroup).checkedRadioButtonId
        when (checkedId) {
            R.id.radButton1 -> priority = PRIORITY_HIGH
            R.id.radButton2 -> priority = PRIORITY_MEDIUM
            R.id.radButton3 -> priority = PRIORITY_LOW
        }
        return priority
    }

    /**
     * setPriority is called when we receive a task from MainActivity
     *
     * @param priority the priority value
     */
    fun setPriorityInViews(priority: Int) {
        when (priority) {
            PRIORITY_HIGH -> (findViewById<View>(R.id.radioGroup) as RadioGroup).check(R.id.radButton1)
            PRIORITY_MEDIUM -> (findViewById<View>(R.id.radioGroup) as RadioGroup).check(R.id.radButton2)
            PRIORITY_LOW -> (findViewById<View>(R.id.radioGroup) as RadioGroup).check(R.id.radButton3)
        }
    }


}
