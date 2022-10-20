package com.increff;

import com.increff.spring.SpringConfig;
import org.springframework.context.annotation.*;

@Configuration
@ComponentScan(
        basePackages = { "com.increff" },
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = { SpringConfig.class }))
@PropertySources({
        @PropertySource(value = "classpath:./com/increff/test.properties")
})
public class TestConfig {
}

