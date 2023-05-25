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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mybooks.data.BookRepository
import com.example.mybooks.data.MyBooksDB
import com.example.mybooks.ui.navigation.Screen
import com.example.mybooks.viewmodels.AddEditBookScreenViewModel
import com.example.mybooks.viewmodels.AddEditBookScreenViewModelFactory
import kotlinx.coroutines.launch
import java.util.Calendar

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

    var isValidTitle by remember { mutableStateOf(true) }
    var isValidAuthor by remember { mutableStateOf(true) }
    var isValidFirstPublished by remember { mutableStateOf(true) }
    var isValidISBN by remember { mutableStateOf(true) }

    val labelTextStyle = MaterialTheme.typography.caption.copy(
        color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.medium))

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
                        navController.navigate(Screen.HomeScreen.route) {
                            popUpTo(Screen.HomeScreen.route)
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
                            value = addEditBookScreenViewModel.book.title,
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            onValueChange = {
                                addEditBookScreenViewModel.updateBook(addEditBookScreenViewModel.book.copy(title = it))
                                // you can validate inputs with an EventClass - see LD04
                                isValidTitle = addEditBookScreenViewModel.isValidTitle(it)
                            },
                            label = { Text(text = "Title") },
                            isError = !isValidTitle
                        )
                        if(!isValidTitle) {
                            Text(
                                text = "Title must not be empty",
                                color = Color.Red
                            )
                        }
                    }

                    Column(modifier = Modifier) {
                        Text(
                            text = "Read",
                            modifier = Modifier
                                .padding(start = 10.dp),
                            style = labelTextStyle)
                        Checkbox(
                            checked = addEditBookScreenViewModel.book.read,
                            onCheckedChange = {
                                addEditBookScreenViewModel.updateBook(addEditBookScreenViewModel.book.copy(read = it))
                            }
                        )
                    }
                }

                OutlinedTextField(
                    value = addEditBookScreenViewModel.book.author,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        addEditBookScreenViewModel.updateBook(addEditBookScreenViewModel.book.copy(author = it))
                        isValidAuthor = addEditBookScreenViewModel.isValidAuthor(it)
                    },
                    label = { Text(text = "Author") },
                    isError = !isValidAuthor
                )
                if(!isValidAuthor) {
                    Text(
                        text = "Author must not be empty",
                        color = Color.Red
                    )
                }

                OutlinedTextField(
                    value = addEditBookScreenViewModel.book.firstPublished.toString(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        addEditBookScreenViewModel.updateBook(addEditBookScreenViewModel.book.copy(firstPublished = it.toIntOrNull() ?: 0))
                        isValidFirstPublished = addEditBookScreenViewModel.isValidFirstPublished(it.toIntOrNull() ?: 0)
                    },
                    label = { Text(text = "First published in") },
                    isError = !isValidFirstPublished
                )
                if(!isValidFirstPublished) {
                    Text(
                        text = "Year of first publication must be between 0 and ${Calendar.getInstance().get(Calendar.YEAR)}",
                        color = Color.Red
                    )
                }

                OutlinedTextField(
                    value = addEditBookScreenViewModel.book.isbn,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        addEditBookScreenViewModel.updateBook(addEditBookScreenViewModel.book.copy(isbn = it))
                        isValidISBN = addEditBookScreenViewModel.isValidISBN(it)
                    },
                    label = {  Text(text = "ISBN") },
                    isError = !isValidISBN,
                )
                if(!isValidISBN) {
                    Text(
                        text = "Invalid ISBN.",
                        color = Color.Red
                    )
                }

                OutlinedTextField(
                    value = addEditBookScreenViewModel.book.cover,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        addEditBookScreenViewModel.updateBook(addEditBookScreenViewModel.book.copy(cover = it))
                    },
                    label = { Text(text = "Cover link") },
                    isError = false
                )

                OutlinedTextField(
                    value = addEditBookScreenViewModel.book.plot,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    onValueChange = {
                        addEditBookScreenViewModel.updateBook(addEditBookScreenViewModel.book.copy(plot = it))
                    },
                    label = { Text(text = "Plot") },
                    isError = false
                )

                isEnabledSaveButton = addEditBookScreenViewModel.isValidBook(
                    title = addEditBookScreenViewModel.book.title,
                    author = addEditBookScreenViewModel.book.author,
                    firstPublished = addEditBookScreenViewModel.book.firstPublished,
                    isbn = addEditBookScreenViewModel.book.isbn
                )

                Button(
                    enabled = isEnabledSaveButton,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        coroutineScope.launch {
                            addEditBookScreenViewModel.saveBook()
                        }
                        navController.navigate(Screen.HomeScreen.route) {
                            popUpTo(Screen.HomeScreen.route)
                        }
                    }
                ) {
                    Text(if (bookId == "0") "Add" else "Save")
                }
            }
        }
    }
}