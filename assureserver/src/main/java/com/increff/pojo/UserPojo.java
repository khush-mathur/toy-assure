package com.increff.pojo;

import com.increff.enums.UserType;
import lombok.*;

import javax.persistence.*;
import javax.persistence.TableGenerator;

import static com.increff.pojo.TableGenerator.USER_GENERATOR;
import static com.increff.pojo.TableGenerator.USER_INITIAL_VALUE;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "assure_user")
public class UserPojo extends AbstractPojo {

    @Id
    @TableGenerator(name = "USER_GENERATOR",initialValue = USER_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE, generator = USER_GENERATOR)
    private Long id;
    @Column(unique = true)
    private String name;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserType type;
}



