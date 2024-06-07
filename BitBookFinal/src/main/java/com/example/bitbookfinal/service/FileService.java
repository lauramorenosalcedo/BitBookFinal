package com.example.bitbookfinal.service;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class FileService {

    private static final Path PDF_FOLDER = Paths.get(System.getProperty("user.dir"), "files");

    public String createPDF(MultipartFile multipartFile) { // Función para crear un archivo PDF.
        String originalName = StringUtils.cleanPath(multipartFile.getOriginalFilename());

        if (!originalName.toLowerCase().endsWith(".pdf")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El archivo no es un PDF.");
        }
        

        Path pdfPath = PDF_FOLDER.resolve(originalName);
        Path canonicalPath;
        try {
            canonicalPath = pdfPath.toRealPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!canonicalPath.startsWith(PDF_FOLDER)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No intentes guarradas");
        } else {
            try {
                multipartFile.transferTo(pdfPath);
            } catch (IOException ex) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo guardar el PDF localmente.", ex);
            }
        }

        return originalName;
    }

    public File getPDF(String pdfName) throws IOException { // Función para recuperar un archivo PDF del directorio de PDF.
        Path pdfPath = PDF_FOLDER.resolve(pdfName);
        Path canonicalPath = pdfPath.toRealPath();

        if (!canonicalPath.startsWith(PDF_FOLDER)){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No intentes guarradas");
        }else {
            return pdfPath.toFile();
        }
    }

    public void deletePDF(String pdfName) { // Función para eliminar un archivo PDF del directorio de PDF.
        Path pdfPath = PDF_FOLDER.resolve(pdfName);
        try {
            pdfPath.toFile().delete();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No se pudo eliminar el PDF localmente.");
        }
    }


    public String getPath(String filename){
        return PDF_FOLDER+"\\"+filename;
    }
}
