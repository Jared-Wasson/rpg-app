package com.usu.firebasetodosapplication.ui.repositories

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.usu.firebasetodosapplication.ui.models.Todo
import kotlinx.coroutines.tasks.await

object TodosRepository {

    private val todoCache = mutableListOf<Todo>()
    private var cacheInitialize = false


    suspend fun createTodo(
        title: String,
        description: String,
        priority: Int,
        estimatedCompletionTime: Int
    ) {
        val doc = Firebase.firestore.collection("todos").document()
        val todo = Todo(
            title = title,
            description = description,
            priority = priority,
            estimatedCompletionTime = estimatedCompletionTime,
            id = doc.id,
            userId = UserRepository.getCurrentUserId(),
            completed = false
        )

        doc.set(todo).await()
        todoCache.add(todo)

    }

    suspend fun getTodos(): List<Todo> {
        if (cacheInitialize) {
            return todoCache;
        } else {
            val snapshot = Firebase.firestore.collection("todos")
                .whereEqualTo("userId", UserRepository.getCurrentUserId()).get().await()
            todoCache.addAll(snapshot.toObjects())
            cacheInitialize = true
            return todoCache
        }
    }

    suspend fun updateTodo(todo: Todo) {
        Firebase.firestore.collection("todos")
            .document(todo.id!!).set(todo).await()

        val oldTodoIndex = todoCache.indexOfFirst { it.id == todo.id  }
        todoCache[oldTodoIndex] = todo
    }
}
