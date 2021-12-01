package com.visma.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.visma.dto.BookDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor
public class Book extends BookDto {
    private String Name;
    private String Author;
    private String Category;
    private String Language;

    private Date Publication_date;

    private String ISBN;

    private String GUID;

    public Book(String name, String author, String category, String language, Date publication_date, String isbn)
    {
        Name = name;
        Author = author;
        Category = category;
        Language = language;
        Publication_date = publication_date;
        ISBN = isbn;
        GUID = UUID.randomUUID().toString();
    }
    @Override
    public String toString()
    {
        return String.format("Book name is %s, Author is %s, Category is %s, Language is %s, Publicated %s, ISBN is %s, GUID is %s",
                Name, Author, Category, Language, Publication_date.toString(), ISBN, GUID);
    }

}
