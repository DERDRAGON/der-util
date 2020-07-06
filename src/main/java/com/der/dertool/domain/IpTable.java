package com.der.dertool.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @program: der-tool
 * @description: ${description}
 * @author: long
 * @create: 2020-07-03 21:24
 */
@Data
@Entity
public class IpTable implements Serializable {

    @Id
    @GeneratedValue
    private Integer id;

    @Column
    private Integer ip;

}
