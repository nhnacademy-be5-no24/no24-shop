package com.nhnacademy.shop.wrap;


import com.nhnacademy.shop.config.RedisConfig;
import com.nhnacademy.shop.wrap.domain.Wrap;
import com.nhnacademy.shop.wrap.repository.WrapRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@WebAppConfiguration
@Import(
        {RedisConfig.class}
)
class WrapRepositoryTest {

    @Autowired
    private WrapRepository wrapRepository;

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private EntityManager executeUpdate;
    @AfterEach
    public void teardown() {
        this.executeUpdate.createNativeQuery("ALTER TABLE wrap ALTER COLUMN `wrap_id` RESTART WITH 1").executeUpdate();
    }
    @Test
    void testFindWrapByWrapName(){
        Wrap wrap1 = Wrap.builder()
                .wrapId(null)
                .wrapName("name1")
                .wrapCost(1L)
                .build();
        Wrap wrap2 = Wrap.builder()
                .wrapId(null)
                .wrapName("name2")
                .wrapCost(2L)
                .build();

        entityManager.persist(wrap1);
        entityManager.persist(wrap2);

        Optional<Wrap> optionalWrap = wrapRepository.findWrapByWrapName("name1");
        assertTrue(optionalWrap.isPresent());
        assertEquals("name1", optionalWrap.get().getWrapName());

    }
}
