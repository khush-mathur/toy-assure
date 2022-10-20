package com.increff.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.TableGenerator;

import static com.increff.pojo.TableGenerator.*;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "assure_bin")
public class BinPojo extends AbstractPojo {
    @Id
    @TableGenerator(name = BIN_GENERATOR,initialValue = BIN_INITIAL_VALUE)
    @GeneratedValue(strategy = GenerationType.TABLE,generator = BIN_GENERATOR)
    private Long binId;
}
