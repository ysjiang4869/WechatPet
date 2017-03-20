import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by jiangyuesong on 2017/3/17 0017.
 */
public class test {
    public static void main(String[] args) throws Exception {
        JSONObject inputText=new JSONObject();
        JSONObject perception=new JSONObject();
        JSONObject userInfo=new JSONObject();
        JSONObject selfInfo=new JSONObject();
        inputText.put("text", "你开心吗");
        userInfo.put("apiKey", "74ca22e4b15a1fe5822adaeec945ca99");
        userInfo.put("userId",5);
        perception.put("inputText",inputText);
        perception.put("selfInfo",selfInfo);
        JSONObject postbody=new JSONObject();
        postbody.put("perception",perception);
        postbody.put("userInfo",userInfo);
        RestTemplate restTemplate=new RestTemplate();
        Charset charset=Charset.forName("UTF-8");
        StringHttpMessageConverter stringHttpMessageConverter=new StringHttpMessageConverter(charset);
        List<HttpMessageConverter<?>> messageConverters=restTemplate.getMessageConverters();
        messageConverters.set(1,stringHttpMessageConverter);
        restTemplate.setMessageConverters(messageConverters);
        HttpHeaders header =new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request= new HttpEntity<>(postbody.toString(), header);
        String ret= restTemplate.postForObject("http://openapi.tuling123.com/openapi/api/v2", request, String.class);
        System.out.println(ret);
    }
}
