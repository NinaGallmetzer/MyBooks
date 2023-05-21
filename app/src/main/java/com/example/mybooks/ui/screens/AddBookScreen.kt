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
import com.example.mybooks.viewmodels.AddBookScreenViewModel
import com.example.mybooks.viewmodels.AddBookScreenViewModelFactory
import kotlinx.coroutines.launch
import java.util.Calendar

@Composable
fun AddBookScreen (
    navController: NavHostController = rememberNavController()
) {
    val db = MyBooksDB.getDatabase(LocalContext.current)
    val repository = BookRepository(bookDao = db.bookDao())
    val factory = AddBookScreenViewModelFactory(repository = repository)
    val addBookScreenViewModel: AddBookScreenViewModel = viewModel(factory = factory)

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
                    text = "Add New Book",
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
                            value = addBookScreenViewModel.book.title,
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            onValueChange = {
                                addBookScreenViewModel.updateBook(addBookScreenViewModel.book.copy(title = it))
                                // you can validate inputs with an EventClass - see LD04
                                isValidTitle = addBookScreenViewModel.isValidTitle(it)
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
                            checked = addBookScreenViewModel.book.read,
                            onCheckedChange = {
                                addBookScreenViewModel.updateBook(addBookScreenViewModel.book.copy(read = it))
                            }
                        )
                    }
                }

                OutlinedTextField(
                    value = addBookScreenViewModel.book.author,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        addBookScreenViewModel.updateBook(addBookScreenViewModel.book.copy(author = it))
                        isValidAuthor = addBookScreenViewModel.isValidAuthor(it)
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
                    value = addBookScreenViewModel.book.firstPublished.toString(),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        addBookScreenViewModel.updateBook(addBookScreenViewModel.book.copy(firstPublished = it.toInt()))
                        isValidFirstPublished = addBookScreenViewModel.isValidFirstPublished(it.toInt())
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
                    value = addBookScreenViewModel.book.isbn,
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        addBookScreenViewModel.updateBook(addBookScreenViewModel.book.copy(isbn = it))
                        isValidISBN = addBookScreenViewModel.isValidISBN(it)
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
                    value = addBookScreenViewModel.book.cover,
                    modifier = Modifier.fillMaxWidth(),
                    onValueChange = {
                        addBookScreenViewModel.updateBook(addBookScreenViewModel.book.copy(cover = it))
                    },
                    label = { Text(text = "Cover link") },
                    isError = false
                )

                OutlinedTextField(
                    value = addBookScreenViewModel.book.plot,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    onValueChange = {
                        addBookScreenViewModel.updateBook(addBookScreenViewModel.book.copy(plot = it))
                    },
                    label = { Text(text = "Plot") },
                    isError = false
                )

                isEnabledSaveButton = addBookScreenViewModel.isValidBook(
                    title = addBookScreenViewModel.book.title,
                    author = addBookScreenViewModel.book.author,
                    firstPublished = addBookScreenViewModel.book.firstPublished,
                    isbn = addBookScreenViewModel.book.isbn
                )

                Button(
                    enabled = isEnabledSaveButton,
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        coroutineScope.launch {
                            addBookScreenViewModel.saveBook()
                        }
                        navController.navigate("home") {
                            popUpTo("home")
                        }
                    }
                ) {
                    Text("Add")
                }
            }
        }
    }
}