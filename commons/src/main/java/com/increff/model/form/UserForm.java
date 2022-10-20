package com.increff.model.form;

import com.increff.enums.UserType;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserForm {
    String name;
    String type;
}
