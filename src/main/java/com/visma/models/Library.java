package com.visma.models;

import com.visma.dataSave.JSONDataBook;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Getter
@Setter
@NoArgsConstructor
public class Library {

    private List<Book> bookList = new ArrayList<>();

    private Map<Book, TakenBook> booksTaken = new HashMap<>();

    /**
     * Get book from library
     * @param guid book guid
     * @return book if such one exist otherwise null
     */
    public Book GetBook(String guid)
    {
        Optional<Book> bookFirst = bookList.stream().filter(book -> book.getGUID().equals(guid)).findFirst();

        if(bookFirst.isPresent())
            return bookFirst.get();

        return null;
    }

    /**
     * Add book to library
     * @param book Book that we want to add
     * @throws IOException Problem with saving
     */
    public void AddBook(Book book) throws IOException {
        bookList.add(book);

        JSONDataBook.SaveData(bookList);
    }
    /**
     *
     * @param book Taken book
     * @param user Who took it
     * @param period For how long
     */
    public void TakeBook(Book book, String user, long period)
    {
        booksTaken.put(book, new TakenBook(user, period));
    }
    public void RemoveBook(Book book) throws IOException
    {
        bookList.remove(book);

        JSONDataBook.SaveData(bookList);
    }

    /**
     * Check if book is taken
     * @param book Book to take
     * @return If book is taken true otherwise false
     */
    public boolean BookTake(Book book)
    {
        if(booksTaken.containsKey(book))
            return true;
        return false;
    }

    /**
     * User reached borrow limit
     * @param user User to check
     * @return true if reached otherwise false
     */
    public boolean ReachedCountOfTake(String user)
    {
       List<TakenBook> books = booksTaken.values().stream().filter(book -> book.getWho().equals(user)).collect(Collectors.toList());
       System.out.println("Book" + books.size());
       if(books.size() == 3)
       {
           return true;
       }
       return false;
    }
}
