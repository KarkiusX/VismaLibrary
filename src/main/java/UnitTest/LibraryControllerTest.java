package UnitTest;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.visma.LaunchApplication;
import com.visma.models.Book;
import com.visma.models.Library;
import com.visma.models.TakenBook;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@ContextConfiguration(classes = LaunchApplication.class)
@SpringBootTest
public class LibraryControllerTest {

    private Library library = new Library();

    private Book book;
    private Book book1;
    private Book book2;

    @BeforeEach
    public void setUp() throws ParseException, IOException {
        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2021-05-21");
        book = new Book("Pasaulis", "Karolis", "History", "Lithuanian",date, "XV324-DF264");
        book1 = new Book("Dvasia", "Benas", "Action", "English",date, "XK324-DF234");
        book2 = new Book("Dangus", "Mykolas", "History", "Lithuanian",date, "XC324-DF234");

        library.AddBook(book);
        library.AddBook(book1);
        library.AddBook(book2);

        library.TakeBook(book, "Karolis", 345123);
        library.TakeBook(book1, "Karolis", 345123);
        library.TakeBook(book2, "Matas", 345123);
    }
    @AfterEach
    public void Finish()
    {

        library.getBookList().clear();
    }
    @Test
    public void TestBookExist()
    {
        Assertions.assertNotNull(library.GetBook(book.getGUID()));
    }
    @Test
    public void TestBookTaken()
    {
        Assertions.assertTrue(library.BookTake(book));
    }
    @ParameterizedTest
    @ValueSource(strings = {"Karolis", "Matas"})
    public void ReachedBookLimit(String user)
    {
        library.TakeBook(book2, "Karolis", 345123);
        Assertions.assertTrue(library.ReachedCountOfTake(user));
    }
}
