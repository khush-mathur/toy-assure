package com.increff.model.form;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChannelForm {
    private String name;
    private String invoiceType;
}
