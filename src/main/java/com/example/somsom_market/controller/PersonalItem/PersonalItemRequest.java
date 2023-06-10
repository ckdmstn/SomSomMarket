package com.example.somsom_market.controller.PersonalItem;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class PersonalItemRequest {
    private Long itemId;
    private String sellerId;
    private String imgName;
    private String imgPath;

    private String title;
    private int price;
    private String description;
    // private List<String> imageUrl;
    private MultipartFile imgFile;
    private String status; // 거래가능 / 거래중 / 거래완료
}
