package com.example.mybooks.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mybooks.data.BookRepository
import com.example.mybooks.data.MyBooksDB
import com.example.mybooks.viewmodels.AddEditBookScreenViewModel
import com.example.mybooks.viewmodels.AddEditBookScreenViewModelFactory

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AddEditBookScreen (
    navController: NavHostController = rememberNavController(),
    bookId: String?
) {
    val db = MyBooksDB.getDatabase(LocalContext.current)
    val repository = BookRepository(bookDao = db.bookDao())
    val factory = bookId?.let { AddEditBookScreenViewModelFactory(repository = repository, bookId = it) }
    val addEditBookScreenViewModel: AddEditBookScreenViewModel = viewModel(factory = factory)

    val book = addEditBookScreenViewModel.book.value
    val coroutineScope = rememberCoroutineScope()

    Column {
        TopAppBar {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
            ) {
                IconButton(
                    modifier = Modifier.size(24.dp),
                    onClick = {
                        navController.navigate("home") {
                            popUpTo("home")
                        }
                    },
                ) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "GoBack"
                    )
                }

                Text(
                    text = "Current book: ${book.bookId}, ${book.title}",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(horizontal = 20.dp))
            }
        }
        
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(10.dp)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {

                var title by remember {
                    mutableStateOf("")
                }

            }

        }
    }

}