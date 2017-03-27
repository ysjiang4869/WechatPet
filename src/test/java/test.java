import org.apache.log4j.spi.LoggerFactory;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by jiangyuesong on 2017/3/17 0017.
 */
public class test {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(test.class);
    public static void main(String[] args) throws Exception {
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
        String date="20356432";
        try {
            Date day=format.parse(date);
            log.info(day.toString());
        }catch (ParseException e){
            log.error(e.getMessage());
            log.info("parse error",e);
        }

    }
}
