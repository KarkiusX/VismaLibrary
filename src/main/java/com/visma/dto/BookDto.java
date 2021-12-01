package com.visma.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class BookDto {
    private String Name;
    private String Author;
    private String Category;
    private String Language;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date publication_date;

    private String ISBN;

    @Override
    public String toString()
    {
        return String.format("Book name is %s, Author is %s, Category is %s, Language is %s, Publicated %s, ISBN is %s",
                Name, Author, Category, Language, publication_date.toString(), ISBN);
    }
}
