import com.ecnu.dao.UserMapper;
import com.ecnu.domain.User;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tk.mybatis.mapper.entity.Example;

/**
 * @author onion
 * @date 2019/12/12 -9:24 上午
 */
@SpringBootTest
@Aspect
public class MybatisMapperTest {
    @Autowired
    private UserMapper mapper;
    @Test
    public void testSelectByExample() {
        Example example = new Example(User.class);
        example.orderBy("credit").desc().orderBy("id").asc();
        example.setDistinct(true);
        example.selectProperties("id", "nickname", "credit");
        Example.Criteria criteria1 = example.createCriteria();
        Example.Criteria criteria2 = example.createCriteria();
        criteria1.andGreaterThan("credit", 60).andLessThan("credit", 80);
        criteria2.andLike("nickname", "onion");
        example.or(criteria2);
        mapper.selectByExample(example);
    }


}

