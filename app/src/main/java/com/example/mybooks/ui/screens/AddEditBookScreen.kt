package com.example.mybooks.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mybooks.data.BookRepository
import com.example.mybooks.data.MyBooksDB
import com.example.mybooks.viewmodels.AddEditBookScreenViewModel
import com.example.mybooks.viewmodels.AddEditBookScreenViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun AddEditBookScreen (
    navController: NavHostController = rememberNavController(),
    bookId: String?
) {
    val db = MyBooksDB.getDatabase(LocalContext.current)
    val repository = BookRepository(bookDao = db.bookDao())
    val factory = bookId?.let { AddEditBookScreenViewModelFactory(repository = repository, bookId = it.toInt()) }
    val addEditBookScreenViewModel: AddEditBookScreenViewModel = viewModel(factory = factory)

    val coroutineScope = rememberCoroutineScope()
    val book = addEditBookScreenViewModel.getBook()

    var title by rememberSaveable { mutableStateOf(book.title) }
    var isTitleEmpty by remember { mutableStateOf(false) }

    var author by remember { mutableStateOf("") }
    var isAuthorEmpty by remember { mutableStateOf(false) }

    var firstPublished by remember { mutableStateOf("") }
    var isFirstPublishedEmpty by remember { mutableStateOf(false) }

    var isbn by remember { mutableStateOf("") }
    var isIsbnEmpty by remember { mutableStateOf(false) }

    var cover by remember { mutableStateOf("") }

    var plot by remember { mutableStateOf("") }

    var isEnabledSaveButton by remember { mutableStateOf(false) }

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
                    text = if (bookId == "0") "Add New Book" else "Edit Book Details",
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


                if (bookId == "0") {
                    Text(text = "book.title: new book")
                    Text(text = "title: new book")
                } else {
                    Text(text = "var book.title = ${book.title}")
                    Text(text = "var title = $title")
                }

                OutlinedTextField(
                    value = title,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        title = it
                        isTitleEmpty = it.isEmpty()
                    },
                    label = { Text(text = "Title") },
                    isError = isTitleEmpty
                )
                if(isTitleEmpty) {
                    Text(
                        text = "Field must not be empty",
                        color = Color.Red
                    )
                }

                OutlinedTextField(
                    value = author,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        author = it
                        isAuthorEmpty = it.isEmpty()
                    },
                    label = { Text(text = "Author") },
                    isError = isAuthorEmpty
                )
                if(isAuthorEmpty) {
                    Text(
                        text = "Field must not be empty",
                        color = Color.Red
                    )
                }

                OutlinedTextField(
                    value = firstPublished,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        firstPublished = it
                        isFirstPublishedEmpty = it.isEmpty()
                    },
                    label = { Text(text = "First published in") },
                    isError = isFirstPublishedEmpty
                )
                if(isFirstPublishedEmpty) {
                    Text(
                        text = "Field must not be empty",
                        color = Color.Red
                    )
                }

                OutlinedTextField(
                    value = isbn,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        isbn = it
                        isIsbnEmpty = it.isEmpty()
                    },
                    label = {  Text(text = "ISBN") },
                    isError = isIsbnEmpty,
                )
                if(isIsbnEmpty) {
                    Text(
                        text = "Field must not be empty",
                        color = Color.Red
                    )
                }

                OutlinedTextField(
                    value = cover,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        cover = it
                    },
                    label = { Text(text = "Cover link") },
                    isError = false
                )

                OutlinedTextField(
                    value = plot,
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    onValueChange = {
                        plot = it
                    },
                    label = { Text(text = "Plot") },
                    isError = false
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        coroutineScope.launch {
                            addEditBookScreenViewModel.saveBook(
                                title = title,
                                author = book.author,
                                firstPublished = book.firstPublished,
                                isbn = book.isbn,
                                cover = book.cover,
                                plot = book.plot,
                                read = book.read
                            )
                        }
                        navController.navigate("home") {
                            popUpTo("home")
                        }
                    }
                ) {
                    Text(if (bookId == "0") "Add" else "Save")
                }
            }
        }
    }
}
