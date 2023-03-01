import cn.hutool.crypto.SecureUtil;
import org.junit.Test;

import java.util.UUID;

public class TestCode {

    @Test
    public void testCode() {
        System.out.println(SecureUtil.md5("a.2232281"));
        System.out.println(UUID.randomUUID());

    }
}
