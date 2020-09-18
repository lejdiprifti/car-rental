package com.ikubinfo.rental.car.util;

import com.ikubinfo.rental.category.util.CategoryUtil;
import com.ikubinfo.rental.entity.CarEntity;
import com.ikubinfo.rental.entity.StatusEnum;
import com.ikubinfo.rental.model.CarModel;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CarUtils {

    public static CarEntity createCarEntityWithStatus(StatusEnum statusEnum) {
        CarEntity carEntity = new CarEntity();
        carEntity.setName("some name");
        carEntity.setPlate("00000");
        carEntity.setActive(true);
        carEntity.setPhoto(new byte[0]);
        carEntity.setCategory(CategoryUtil.createCategoryEntity());
        carEntity.setDiesel("some diesel");
        carEntity.setPrice(10.0);
        carEntity.setAvailability(statusEnum);
        carEntity.setType("some type");
        carEntity.setYear(2020);
        return carEntity;
    }

    public static CarModel createCarModelWithStatus(StatusEnum statusEnum) {
        CarModel carModel = new CarModel();
        carModel.setName("some name");
        carModel.setPlate("00000");
        carModel.setActive(true);
        carModel.setPhoto(new byte[0]);
        carModel.setCategoryId((long) 1);
        carModel.setDiesel("some diesel");
        carModel.setPrice(10.0);
        carModel.setAvailability(statusEnum);
        carModel.setType("some type");
        carModel.setYear(2020);
        carModel.setDescription("some description");
        return carModel;
    }

    public static MultipartFile createMultipartFile() {
        return new MultipartFile() {
            @Override
            public String getName() {
                return "some name";
            }

            @Override
            public String getOriginalFilename() {
                return "some name";
            }

            @Override
            public String getContentType() {
                return "image/png";
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 10;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[10];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return new InputStream() {
                    @Override
                    public int read() throws IOException {
                        return 10;
                    }
                };
            }

            @Override
            public void transferTo(File file) throws IOException, IllegalStateException {

            }
        };
    }

    private CarUtils(){}
}
