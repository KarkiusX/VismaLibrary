package com.visma.models;

import com.visma.datasave.JSONDataBook;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
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
     * Take book from library
     * @param book Taken book
     * @param user Who took it
     * @param period For how long
     */
    public void TakeBook(Book book, String user, long period)
    {
        booksTaken.put(book, new TakenBook(user, period));
    }

    /**
     * Remove book from library
     * @param book
     * @throws IOException
     */
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
        {
            System.out.println("Taken");
            return true;
        }
        return false;
    }

    /**
     * Check if User reached borrow limit
     * @param user User to check
     * @return true if reached otherwise false
     */
    public boolean ReachedCountOfTake(String user)
    {
       List<TakenBook> books = booksTaken.values().stream().filter(book -> book.getWho().equals(user)).collect(Collectors.toList());
       if(books.size() == 3)
       {
           return true;
       }
       return false;
    }
}
