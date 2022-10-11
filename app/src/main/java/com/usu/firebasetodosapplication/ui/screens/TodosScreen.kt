package com.usu.firebasetodosapplication.ui.screens

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.usu.firebasetodosapplication.ui.components.TodoListItem
import com.usu.firebasetodosapplication.ui.viewmodels.TodosViewModel
import kotlinx.coroutines.launch

@Composable
fun TodosScreen(navHostController: NavHostController) {
    val viewModel: TodosViewModel = viewModel()
    val state = viewModel.uiState
    val scope = rememberCoroutineScope()

    LaunchedEffect(true) {
        viewModel.getTodos()
    }
    if (!state.loadingState) return
    LazyColumn (
        Modifier
            .fillMaxHeight()
            .padding(16.dp)){
        items(state.todos, key = {it.id!!}) { todo ->
            TodoListItem(todo = todo, toggle = {
                scope.launch {
                    viewModel.toggleTodoCompletion(todo)
                }
            }, onEditPressed = {navHostController.navigate("edittodo?id=${todo.id}")})
        }
    }
}