package com.visma.rest.book;

import com.visma.dto.BookDto;
import com.visma.models.Book;
import com.visma.models.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LibraryController {

    private final Library library;

    LibraryController(Library library) {
        this.library = library;
    }

    /**
     * Retrieve all books
     * @return books
     */
    @GetMapping("/book")
    public ResponseEntity getAllBooksFromLibrary()
    {
       List<Book> bookList = library.getBookList();

       if(bookList.size() == 0)
           return ResponseEntity.status(HttpStatus.NO_CONTENT).build();

       return ResponseEntity.ok(bookList);
    }
    /**
     * EndPoint to get book
     * @param guid The book id
     * @return Book if one exist, otherwise returns resource not found.
     */
    @GetMapping("/book/{guid}")
    public ResponseEntity getBookFromLibrary(@PathVariable String guid)
    {
        Book book = library.GetBook(guid);

        if(book == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    /**
     * EndPoint to take book from library
     * @param guid The book id
     * @param who Person who wants to borrow
     * @param period For how long, we specify in days
     * @return returns the borrowed book
     */
    @GetMapping("/book/{guid}/{who}/{period}")
    public ResponseEntity takeBookFromLibrary(@PathVariable String guid, @PathVariable String who, @PathVariable int period)
    {
        double periodMonth = period * 86400.0 / 2628000.0;

        long totalPeriodSecond = Instant.now().getEpochSecond() + (long)(period * 86400);


        if(periodMonth > 2)
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Reached maximum term for borrowing MAX 2 MONTHS");
        }
        if(library.ReachedCountOfTake(who))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not eligible to borrow anymore");
        }
        Book book = library.GetBook(guid);

        if(book == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        if(library.BookTake(book))
        {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Someone else borrowed this book");
        }

        library.TakeBook(book, who, totalPeriodSecond);

        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    /**
     * EndPoint to Add book
     * @return Book created
     * @throws IOException problem with saving the book
     */
    @PostMapping("/book")
    public ResponseEntity AddBookToLibrary(@RequestBody BookDto bookdto) throws IOException {
        Book book = new Book(bookdto.getName(), bookdto.getAuthor(), bookdto.getCategory(), bookdto.getLanguage(), bookdto.getPublication_date(), bookdto.getISBN());
        library.AddBook(book);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * EndPoint to remove the book
     * @param guid The book id
     * @return Book removed succesfully
     */
    @DeleteMapping("/book/{guid}")
    public ResponseEntity DeleteBookFromLibrary(@PathVariable String guid)
    {
        Book book = library.GetBook(guid);

        if(book == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        try
        {
            library.RemoveBook(book);
        }
        catch (IOException ioException)
        {
            System.out.println("Error: " + ioException);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * EntPoint to filter the book by parameters
     * @param author Filter by author
     * @param category Filter by category
     * @param language Filter by language
     * @param name Filter by name
     * @param taken Filter by taken or available
     * @return filtered list
     */
    @GetMapping("/book/filter")
    public ResponseEntity FilterBooksBy(@RequestParam(required = false) String author, @RequestParam(required = false) String category,
                                        @RequestParam(required = false) String language, @RequestParam(required = false) String name,
                                        @RequestParam(required = false) Boolean taken)
    {
        List<Book> bookList = library.getBookList();
        List<Book> filteredBooks = bookList.stream().filter(book -> {
            List<Boolean> AllParametersPass = new ArrayList<>();
            if(author != null)
              AllParametersPass.add(book.getAuthor().equals(author));
            if(category != null)
                AllParametersPass.add(book.getCategory().equals(category));
            if(language != null)
                AllParametersPass.add(book.getLanguage().equals(language));
            if(name != null)
                AllParametersPass.add(book.getName().equals(name));
            if(taken != null)
            {
                if(taken)
                {
                    boolean bookTaken = library.BookTake(book);
                    AllParametersPass.add(bookTaken);
                }
                else
                    AllParametersPass.add(library.BookTake(book) == false ? true : false);
            }

            if(AllParametersPass.contains(false))
                return false;
            return true;

        }).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(filteredBooks);
    }

}
