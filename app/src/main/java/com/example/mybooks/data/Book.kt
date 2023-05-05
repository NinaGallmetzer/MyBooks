package com.example.mybooks.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Book(
    @PrimaryKey
    val bookId: String = "",
    var title: String = "",
    var author: String = "",
    var firstPublished: Int = 0,
    var isbn: String = "",
    var cover: String = "",
    var plot: String = "",
    var read: Boolean = false
)

fun getInitialBooks(): List<Book> {
    return listOf(
        Book(
            bookId = "1",
            title = "Victory City",
            author = "Salman Rushdie",
            firstPublished = 2023,
            isbn = "978-0593597217",
            cover = "https://m.media-amazon.com/images/I/814ZqpcytvL.jpg",
            plot = "In the wake of an unimportant battle between two long-forgotten kingdoms in fourteenth-century southern India, a nine-year-old girl has a divine encounter that will change the course of history. After witnessing the death of her mother, the grief-stricken Pampa Kampana becomes a vessel for a goddess, who begins to speak out of the girl’s mouth. Granting her powers beyond Pampa Kampana’s comprehension, the goddess tells her that she will be instrumental in the rise of a great city called Bisnaga—“victory city”—the wonder of the world.\n" +
                    "\n" +
                    "Over the next 250 years, Pampa Kampana’s life becomes deeply interwoven with Bisnaga’s, from its literal sowing from a bag of magic seeds to its tragic ruination in the most human of ways: the hubris of those in power. Whispering Bisnaga and its citizens into existence, Pampa Kampana attempts to make good on the task that the goddess set for her: to give women equal agency in a patriarchal world. But all stories have a way of getting away from their creator, and Bisnaga is no exception. As years pass, rulers come and go, battles are won and lost, and allegiances shift, the very fabric of Bisnaga becomes an ever more complex tapestry—with Pampa Kampana at its center.\n" +
                    "\n" +
                    "Brilliantly styled as a translation of an ancient epic, Victory City is a saga of love, adventure, and myth that is in itself a testament to the power of storytelling.",
            read = false
        ),
        Book(
            bookId = "2",
            title = "Alice's Adventures in Wonderland",
            author = "Lewis Carroll",
            firstPublished = 1865,
            isbn = "979-8565465747",
            cover = "https://m.media-amazon.com/images/I/71EENzKGNOL.jpg",
            plot = "\"Alice's Adventures in Wonderland\" is a novel written by English author Lewis Carroll (the pseudonym of Charles Lutwidge Dodgson) and published in 1865." +
                    "\n" +
                    "It tells the story of a young girl named Alice who falls down a rabbit hole into a fantastic and absurd world populated by anthropomorphic creatures and talking animals.\n" +
                    "\n" +
                    "In this magical world, Alice encounters a range of eccentric characters, including the Cheshire Cat, the Mad Hatter, and the Queen of Hearts. Each encounter challenges Alice's assumptions about the world and her own identity, and leads her on a series of adventures through this surreal and sometimes frightening world.\n" +
                    "\n" +
                    "Throughout the story, Carroll uses wordplay and nonsensical logic to create a whimsical and dreamlike atmosphere. Despite its fantastical elements, the book also deals with themes of growing up, identity, and the search for meaning in a confusing and chaotic world.",
            read = true
        ),
        Book(
            bookId = "3",
            title = "Le Petit Prince",
            author = "Antoine de Saint-Exupéry",
            firstPublished = 1943,
            isbn = "978-2070408504",
            cover = "https://m.media-amazon.com/images/I/71mRmbfNNlL.jpg",
            plot = "\"The Little Prince\" is a novella written by French author and aviator Antoine de Saint-Exupéry. It was first published in 1943 and has since become a beloved classic of children's literature.\n" +
                    "\n" +
                    "The story follows a pilot who has crashed in the Sahara desert and meets a young boy who claims to be a prince from a small planet called Asteroid B-612. As the pilot and the little prince spend time together, the prince shares his experiences traveling to other planets and meeting various strange and unique characters, including a conceited man, a drunkard, and a geographer.\n" +
                    "\n" +
                    "Through these encounters, the little prince learns important lessons about love, friendship, and the meaning of life. The story is both whimsical and profound, and has been praised for its simple yet powerful messages about the importance of human connections and the value of innocence and imagination.",
            read = true
        )
    )
}