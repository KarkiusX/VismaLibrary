package com.visma.dataSave;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.visma.models.Book;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JSONDataBook{

    final static private ObjectMapper mapper = new ObjectMapper();

    final static private File LibraryDataFile = new File("Books.json");

    public static void SaveData(List<Book> bookList) throws IOException
    {
        mapper.writerWithDefaultPrettyPrinter().writeValue(LibraryDataFile, bookList);
    }
    public static List<Book> LoadData() throws IOException
    {
        List<Book> bookList = mapper.readValue(LibraryDataFile, new TypeReference<>(){});

        return bookList;
    }

}
