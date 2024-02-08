package com.sejong.sejongpeer.domain.college.repository;

import static org.assertj.core.api.Assertions.*;

import com.sejong.sejongpeer.TestQuerydslConfig;
import com.sejong.sejongpeer.domain.college.entity.CollegeMajor;
import com.sejong.sejongpeer.global.config.JpaAuditingConfig;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({JpaAuditingConfig.class, TestQuerydslConfig.class})
class CollegeMajorRepositoryTest {

    @Autowired private CollegeMajorRepository collegeMajorRepository;

    @BeforeEach
    void setUp() {
        List<CollegeMajor> collegeMajors =
                Arrays.asList(
                        CollegeMajor.builder().college("인문과학대학").major("국어국문학과").build(),
                        CollegeMajor.builder().college("인문과학대학").major("국제학부").build(),
                        CollegeMajor.builder().college("인문과학대학").major("국제학부 영어영문학전공").build(),
                        CollegeMajor.builder().college("인문과학대학").major("국제학부 일어일문학전공").build(),
                        CollegeMajor.builder().college("인문과학대학").major("국제학부 중국통상학전공").build(),
                        CollegeMajor.builder().college("인문과학대학").major("역사학과").build(),
                        CollegeMajor.builder().college("인문과학대학").major("교육학과").build(),
                        CollegeMajor.builder().college("사회과학대학").major("행정학과").build(),
                        CollegeMajor.builder().college("사회과학대학").major("미디어커뮤니케이션학과").build(),
                        CollegeMajor.builder().college("경영경제대학").major("경영학부").build());

        collegeMajorRepository.saveAll(collegeMajors);
    }

    @Test
    void findAllColleges() {
        List<String> colleges = collegeMajorRepository.findAllColleges();

        assertThat(colleges).isNotEmpty();
    }

    @Test
    void findAllByCollege() {
        List<CollegeMajor> majors = collegeMajorRepository.findAllByCollege("인문과학대학");

        assertThat(majors).isNotEmpty();
    }
}
