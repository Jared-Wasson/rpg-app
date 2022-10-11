package com.usu.firebasetodosapplication.ui.viewmodels

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.usu.firebasetodosapplication.ui.models.Todo
import com.usu.firebasetodosapplication.ui.repositories.TodosRepository

class TodosScreenState {
    val _todos = mutableStateListOf<Todo>()
    val todos: List<Todo> get() = _todos
    var loadingState by mutableStateOf(false)
}
class TodosViewModel(application: Application): AndroidViewModel(application) {
    val uiState = TodosScreenState()
    suspend fun getTodos() {
        val todos = TodosRepository.getTodos()
        uiState._todos.clear()
        uiState._todos.addAll(todos)
        uiState.loadingState = true
    }

    suspend fun toggleTodoCompletion(todo: Todo) {
        var opposite = false;
        if(todo.completed == false){
            opposite = true
        }
        val copyTodo = todo.copy(completed = opposite)
        uiState._todos[uiState._todos.indexOf(todo)] = copyTodo

        TodosRepository.updateTodo(copyTodo)
    }
}