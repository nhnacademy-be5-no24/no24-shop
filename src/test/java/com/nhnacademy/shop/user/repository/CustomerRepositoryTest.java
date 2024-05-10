package com.nhnacademy.shop.user.repository;

import com.nhnacademy.shop.config.RedisConfig;
import com.nhnacademy.shop.customer.entity.Customer;
import com.nhnacademy.shop.customer.repository.CustomerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@ActiveProfiles("dev")
@WebAppConfiguration
@Import(
        {RedisConfig.class}
)
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    @DisplayName("고유Id로 고객 찾기")
    void findByCustomerNo() {
        customerRepository.deleteAll();
        //given
        Customer customer = new Customer().builder()
                .customerId("customer")
                .customerPassword("1234")
                .customerName("김고객")
                .customerBirthday(LocalDate.parse("2024-03-28"))
                .customerEmail("kim@gmail.com")
                .customerPhoneNumber("01012345678")
                .customerRole("ROLE_CUSTOMER")
                .build();
        Customer savedCustomer = customerRepository.save(customer);

        //when
        Optional<Customer> optionalCustomer = customerRepository.findById(savedCustomer.getCustomerNo());
        Customer result = optionalCustomer.get();

        //then
        assertThat(result).isEqualTo(savedCustomer);
    }

    @Test
    @DisplayName("아이디로 고객 찾기")
    void findByCustomerId() {
        //given
        Customer customer = new Customer().builder()
                .customerId("customer")
                .customerPassword("1234")
                .customerName("김고객")
                .customerBirthday(LocalDate.parse("2024-03-28"))
                .customerEmail("kim@gmail.com")
                .customerPhoneNumber("01012345678")
                .customerRole("ROLE_CUSTOMER")
                .build();
        Customer savedCustomer = customerRepository.save(customer);

        //when
        Customer result = customerRepository.findByCustomerId(savedCustomer.getCustomerId());

        //then
        assertThat(result).isEqualTo(savedCustomer);
    }

}