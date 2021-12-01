package com.visma;

import com.visma.datasave.JSONDataBook;
import com.visma.models.Book;
import com.visma.models.Library;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class LaunchApplication {

	@Autowired
	Library library;

	public static void main(String[] args){
		SpringApplication.run(LaunchApplication.class, args);

	}
	@PostConstruct
	public void init(){

		try{
			List<Book> books = JSONDataBook.LoadData();
			library.setBookList(books);
		}
		catch (IOException ex)
		{
			System.out.println("JSON file is empty");
		}
	}

}
