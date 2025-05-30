package com.example;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class for saving and loading objects to and from JSON files. Uses the
 * Jackson library.
 */
public class JsonMapper {

    /**
     * The object mapper which will be used for saving and loading.
     */
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Saves the given object to the given file.
     * 
     * @param file       where to save the object to
     * @param fileObject the object to save
     * @throws IOException if the file could not be saved
     */
    public static void save(File file, Object fileObject) throws IOException {
        JsonMapper.objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, fileObject);
    }

    /**
     * Loads an object from the given file.
     * 
     * @param <T>       the type of the object which will be loaded
     * @param file      where to load the object from
     * @param fileClass the class of the object which will be loaded
     * @return the loaded object
     * @throws IOException if the file could not be loaded
     */
    public static <T> T load(File file, Class<T> fileClass) throws IOException {
        return JsonMapper.objectMapper.readValue(file, fileClass);
    }
}