package com.example.mybooks.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    var isTitleEmpty by remember { mutableStateOf(false) }
    var isAuthorEmpty by remember { mutableStateOf(false) }
    var isFirstPublishedEmpty by remember { mutableStateOf(false) }
    var isIsbnEmpty by remember { mutableStateOf(false) }

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
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(modifier = Modifier.weight(2f)) {
                        OutlinedTextField(
                            value = addEditBookScreenViewModel.bookToAdd.title,
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            onValueChange = {
                                // pass the new title to the VM to update the book state
                                addEditBookScreenViewModel.updateBook(addEditBookScreenViewModel.bookToAdd.copy(title = it))
                                // you can validate inputs with an EventClass - see LD04
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
                    }

                    Column(modifier = Modifier) {
                        Text(
                            text = "Read",
                            modifier = Modifier
                                .padding(start = 10.dp),
                            fontSize = 12.sp)
                        Checkbox(
                            checked = addEditBookScreenViewModel.bookToAdd.read,
                            onCheckedChange = {
                                addEditBookScreenViewModel.updateBook(addEditBookScreenViewModel.bookToAdd.copy(read = it))
                            }
                        )
                    }
                }

                OutlinedTextField(
                    value = addEditBookScreenViewModel.bookToAdd.author,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        addEditBookScreenViewModel.updateBook(addEditBookScreenViewModel.bookToAdd.copy(author = it))
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
                    value = addEditBookScreenViewModel.bookToAdd.firstPublished.toString(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        addEditBookScreenViewModel.updateBook(addEditBookScreenViewModel.bookToAdd.copy(firstPublished = it.toInt()))
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
                    value = addEditBookScreenViewModel.bookToAdd.isbn,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        addEditBookScreenViewModel.updateBook(addEditBookScreenViewModel.bookToAdd.copy(isbn = it))
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
                    value = addEditBookScreenViewModel.bookToAdd.cover,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        addEditBookScreenViewModel.updateBook(addEditBookScreenViewModel.bookToAdd.copy(cover = it))
                    },
                    label = { Text(text = "Cover link") },
                    isError = false
                )

                OutlinedTextField(
                    value = addEditBookScreenViewModel.bookToAdd.plot,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp),
                    onValueChange = {
                        addEditBookScreenViewModel.updateBook(addEditBookScreenViewModel.bookToAdd.copy(plot = it))
                    },
                    label = { Text(text = "Plot") },
                    isError = false
                )

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        coroutineScope.launch {
                            addEditBookScreenViewModel.saveBook()
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