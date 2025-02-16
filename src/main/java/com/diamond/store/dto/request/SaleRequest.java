package com.diamond.store.dto.request;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SaleRequest {

    private String saleName;
    private String description;
    private String saleType;//Loại giảm giá (Tiền hay %)
    private double value;
    private Date timeStart;
    private Date timeEnd;
    private boolean enable;
}
