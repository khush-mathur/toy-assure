package com.increff.model.data;

import com.increff.model.form.BinSkuForm;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BinSkuData extends BinSkuForm {
    private Long id;
    private String productName;
    private String clientName;
}
