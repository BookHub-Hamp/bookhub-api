package com.hampcode.dto;

import lombok.Data;

import java.util.List;

@Data
public class PurchaseCreateDTO {
    private Float total;
    private Integer customerId;
    private List<PurchaseItemCreateDTO> items;
}
