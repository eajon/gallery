package cn.no7player;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@EnableSwagger2Doc
@SpringBootApplication
@MapperScan(basePackages = "cn.no7player.mapper")
public class Application {
    private static Logger logger = Logger.getLogger(Application.class);


    /**
     * Start
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        logger.info("SpringBoot Start Success");
    }

}
