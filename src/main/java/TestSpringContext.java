import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestSpringContext {

    public static void main(String[] args) {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("test-context.xml");

        MyBean bean = context.getBean(MyBean.class);
        bean.sayHello();
    }
}
