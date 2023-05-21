package com.example.mybooks.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.mybooks.R
import com.example.mybooks.data.Book
import com.example.mybooks.data.BookRepository
import com.example.mybooks.data.MyBooksDB
import com.example.mybooks.viewmodels.HomeScreenViewModel
import com.example.mybooks.viewmodels.HomeScreenViewModelFactory
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavController
) {
    Scaffold(
        topBar = {
            HomeAppBar(navController = navController)
        }
    ) {
        BookList(navController = navController)
    }
}

@Composable
fun BookList(
    navController: NavController = rememberNavController()
) {
    val db = MyBooksDB.getDatabase(LocalContext.current)
    val repository = BookRepository(bookDao = db.bookDao())
    val factory = HomeScreenViewModelFactory(repository = repository)
    val homeScreenViewModel: HomeScreenViewModel = viewModel(factory = factory)

    val bookList = homeScreenViewModel.bookList.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    if (bookList.value.isEmpty()) {
        Text(text = "You don't have any books saved in this App.")
    } else {
        LazyColumn {
            items (bookList.value) { book ->
                BookRow(
                    book = book,
                    onReadClick = {
                        coroutineScope.launch {
                            homeScreenViewModel.toggleReadState(book)
                        }
                    },
                    onDeleteClick = {
                        coroutineScope.launch {
                            homeScreenViewModel.deleteBook(book)
                        }
                    },
                    onEditClick = { bookId ->
                        navController.navigate("AddEdit/$bookId")
                    }
                )
            }
        }

    }
}

@Composable
fun BookRow(
    book: Book,
    onReadClick: (Int) -> Unit = {},
    onEditClick: (Int) -> Unit = {},
    onDeleteClick: (Int) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    var read by remember { mutableStateOf(book.read) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        shape = RoundedCornerShape(corner = CornerSize(15.dp)),
        elevation = 5.dp
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(175.dp)
            ) {
                Row {
                    if (book.cover.isNotEmpty()) {
                        AsyncImage(
                            model = book.cover,
                            contentDescription = book.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(115.dp)
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.no_image_placeholder),
                            contentDescription = "Prev Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(115.dp))
                    }

                    Column(
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 10.dp, start = 10.dp, end = 25.dp)
                    ) {
                        Text(
                            text = book.title,
                            style = MaterialTheme.typography.h5,
                            modifier = Modifier
                                .padding(5.dp))
                        Text(
                            text = "By ${book.author}",
                            style = MaterialTheme.typography.h6,
                            modifier = Modifier
                                .padding(5.dp))
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    Column {
                        Icon(
                            tint = if (read) MaterialTheme.colors.secondary else Color.LightGray,
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = "Mark book as read",
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                                .clickable {
                                    read = !read
                                    onReadClick(book.bookId)
                                }
                        )
                        Icon(
                            tint = MaterialTheme.colors.secondary,
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit book",
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                                .clickable {
                                    onEditClick(book.bookId)
                                }
                        )
                        Icon(
                            tint = MaterialTheme.colors.secondary,
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete book",
                            modifier = Modifier
                                .padding(vertical = 5.dp)
                                .clickable {
                                    onDeleteClick(book.bookId)
                                }
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(10.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    IconButton(
                        modifier = Modifier.size(24.dp),
                        onClick = { expanded = !expanded },
                    ) {
                        if (expanded) {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowUp,
                                contentDescription = ("Hide details")
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Filled.KeyboardArrowDown,
                                contentDescription = ("Show details")
                            )
                        }
                    }
                }
            }

            if (expanded) {
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Text(text = "First Published: ${book.firstPublished}")
                    Text(text = "ISBN: ${book.isbn}")
                    Divider(
                        modifier = Modifier
                            .padding(5.dp))
                    Text(
                        text = book.plot.ifBlank { "No plot available." },
                        modifier = Modifier
                            .padding(5.dp))
                }
            }
        }
    }
}

@Preview
@Composable
fun BookRowPreview() {
    BookRow(
        book = Book(
            title = "Alice's Adventures in Wonderland",
            author = "Lewis Carroll",
            firstPublished = 1865,
            isbn = "979-8565465747",
            cover = "https://m.media-amazon.com/images/I/71EENzKGNOL.jpg",
            plot = "\"Alice's Adventures in Wonderland\" is a novel written by English author Lewis Carroll (the pseudonym of Charles Lutwidge Dodgson) and published in 1865. \n" +
                    "\n" +
                    "It tells the story of a young girl named Alice who falls down a rabbit hole into a fantastic and absurd world populated by anthropomorphic creatures and talking animals.\n" +
                    "\n" +
                    "In this magical world, Alice encounters a range of eccentric characters, including the Cheshire Cat, the Mad Hatter, and the Queen of Hearts. Each encounter challenges Alice's assumptions about the world and her own identity, and leads her on a series of adventures through this surreal and sometimes frightening world.\n" +
                    "\n" +
                    "Throughout the story, Carroll uses wordplay and nonsensical logic to create a whimsical and dreamlike atmosphere. Despite its fantastical elements, the book also deals with themes of growing up, identity, and the search for meaning in a confusing and chaotic world.",
            read = true
        )
    )
}

@Composable
fun HomeAppBar(navController: NavController) {
    TopAppBar {
        var expanded by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "My Favorite Books",
                style = MaterialTheme.typography.h6)

            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = { expanded = !expanded },
            ) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "DropdownMenu",
                )
            }
        }

        Row {
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                DropdownMenuItem(
                    onClick = {
                        navController.navigate("Add")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Book",
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Add Book")
                }
            }
        }

    }
}
