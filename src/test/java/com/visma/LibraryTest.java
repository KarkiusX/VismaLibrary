package com.visma;

import com.visma.models.Book;
import com.visma.models.Library;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class LibraryTest {

	private Library library;

	private Book book;
	private Book book1;
	private Book book2;

	@BeforeEach
	public void setUp() {
		try{
			library = new Library();
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2021-05-21");
			book = new Book("Pasaulis", "Karolis", "History", "Lithuanian",date, "XV324-DF264");
			book1 = new Book("Dvasia", "Benas", "Action", "English",date, "XK324-DF234");
			book2 = new Book("Dangus", "Mykolas", "History", "Lithuanian",date, "XC324-DF234");

			library.AddBook(book);
			library.AddBook(book1);
			library.AddBook(book2);
		}
		catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
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
		library.TakeBook(book, "Karolis", 345123);
		Assertions.assertTrue(library.BookTake(book));
	}
	@Test
	public void TestBookAdd()
	{
		int bookCount = library.getBookList().size();

		try{
			Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2021-05-21");
			Book bookTest = new Book("Pomidorai", "Antanas", "History", "Lithuanian",date, "XV324-DF264");

			library.AddBook(bookTest);

			Assertions.assertEquals(bookCount + 1, library.getBookList().size());
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}
	@ParameterizedTest
	@ValueSource(strings = {"Karolis", "karolis"})
	public void ReachedBookLimit(String user)
	{

		library.TakeBook(book, "Karolis", 345123);
		library.TakeBook(book1, "Karolis", 345123);
		library.TakeBook(book2, "Karolis", 345123);
		Assertions.assertTrue(library.ReachedCountOfTake(user));
	}

}
