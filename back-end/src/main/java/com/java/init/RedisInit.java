package com.java.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import com.java.utils.RedisClearUtils;
 
@Component
@Order(2)
public class RedisInit implements CommandLineRunner {
	@Autowired RedisClearUtils redisClearUtils;
 
    @Override
    public void run(String... args) throws Exception {
        redisClearUtils.delBarCache();
        redisClearUtils.delLineCache();
        redisClearUtils.delPanelCache();
        redisClearUtils.delPieCache();
        redisClearUtils.delRadaCache();
        redisClearUtils.delCLineCache();
        redisClearUtils.delCRadaCache();
        redisClearUtils.delMListCache();
        redisClearUtils.delMMapCache();
    }
    
}
