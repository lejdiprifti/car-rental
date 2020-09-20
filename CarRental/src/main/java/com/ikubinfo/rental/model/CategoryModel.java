package com.ikubinfo.rental.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CategoryModel {

    private Long id;
    private MultipartFile file;
    private String name;
    private String description;
    private boolean active;
    private byte[] photo;
}
