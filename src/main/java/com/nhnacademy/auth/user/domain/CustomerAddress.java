package com.nhnacademy.auth.user.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerAddress {
    @EmbeddedId
    private CustomerPk customerPk;

    @Column(name = "alias")
    private String alias;

    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    @Getter
    @Setter
    @Embeddable
    public static class CustomerPk implements Serializable{
        @Column(name = "address_id")
        private Long addressId;
        @Column(name = "customer_id")
        private Long customerId;
    }
}