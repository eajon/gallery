package cn.no7player.config.fastjson;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.MediaType;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Wu Yujie
 * @email coffee377@dingtalk.com
 * @time 2018/03/13 15:29
 */
@ConfigurationProperties(prefix = "spring.fastjson")
@ConditionalOnClass({JSON.class})
public class FastJsonProperties {
    private final static String DEFAULT_MEDIA_TYPE = MediaType.APPLICATION_JSON_UTF8_VALUE;

    private boolean enabled;

    private List<MediaType> supportedMediaTypes = MediaType.parseMediaTypes(DEFAULT_MEDIA_TYPE);

    private FastJsonConfig config = new FastJsonConfig();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<MediaType> getSupportedMediaTypes() {
        return supportedMediaTypes;
    }

    public void setSupportedMediaTypes(List<MediaType> supportedMediaTypes) {
        this.supportedMediaTypes = supportedMediaTypes;
    }

    public FastJsonConfig getConfig() {
        return config;
    }

    public void setConfig(FastJsonConfig config) {
        this.config = config;
    }
}
