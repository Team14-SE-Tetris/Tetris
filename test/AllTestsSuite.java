import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({"start","blocks","tetris"})
public class AllTestsSuite {
    // 이 클래스는 테스트 스위트의 설정을 위한 것으로, 별도의 구현 코드는 필요하지 않습니다.
}