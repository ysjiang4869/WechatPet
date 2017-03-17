import org.springframework.web.client.RestTemplate;

/**
 * Created by jiangyuesong on 2017/3/17 0017.
 */
public class test {
    public static void main(String[] args) throws Exception {
        RestTemplate restTemplate=new RestTemplate();
        String ans=restTemplate.getForObject("https://www.zhihu.com/question/57125982/answer/151942234", String.class);
        System.out.print(ans);
    }
}
