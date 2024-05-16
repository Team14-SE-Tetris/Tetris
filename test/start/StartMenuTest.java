package start;

import static org.junit.Assert.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartMenuTest extends ApplicationTest {

    private StartMenu startMenu;
    

    @Override
    public void start(Stage stage) throws Exception {
        startMenu = new StartMenu();
        Scene scene = startMenu.createScene(stage);
        stage.setScene(scene);
        
        stage.show();
    }

    @Before
    public void setUp() throws Exception {
        // StartMenu 인스턴스를 생성하기 위해 start() 메서드가 먼저 호출되도록 설정
    	
        launch(StartMenu.class);
    }

    @Test
    public void testCreateSceneNotNull() {
        assertNotNull(startMenu.scene);
    }
    @Test
    public void testCreateSceneSize() {
    	System.out.print(startMenu.scene.getWidth());
        assertEquals(StartMenu.XSIZE, startMenu.scene.getWidth(), 0);
        assertEquals(StartMenu.YSIZE, startMenu.scene.getHeight(), 0);
    }

    @Test
    public void testNavigateMenu() {
        // 초기 selectedIndex는 0이므로, 1을 전달하여 메뉴를 아래로 이동시켜서 selectedIndex가 1이 되도록 함
        startMenu.navigateMenu(1);
        assertEquals(1, startMenu.selectedIndex);
        
        // 다시 1을 전달하여 selectedIndex가 2가 되도록 함
        startMenu.navigateMenu(1);
        assertEquals(2, startMenu.selectedIndex);
        
        // 음수를 전달하여 메뉴를 위로 이동시켜서 selectedIndex가 1이 되도록 함
        startMenu.navigateMenu(-1);
        assertEquals(1, startMenu.selectedIndex);
    }

    @Test
    public void testUpdateMenu() {
        // 테스트를 위해 selectedIndex를 2로 설정
        startMenu.selectedIndex = 2;
        startMenu.updateMenu();
        
     // selectedIndex에 해당하는 메뉴가 빨간색으로 변경되었는지 확인
        assertEquals("Standard Game", startMenu.menuItems.get(0).getText());
        assertEquals("Item Mode Game", startMenu.menuItems.get(1).getText());
        assertEquals("Battle Mode Game", startMenu.menuItems.get(2).getText());
        assertEquals("Setting", startMenu.menuItems.get(3).getText());
        assertEquals("Score Board", startMenu.menuItems.get(4).getText());
        assertEquals("Exit", startMenu.menuItems.get(5).getText());
        //assertEquals("red", startMenu.menuItems.get(2).getFill().toString());

        // selectedIndex를 0으로 변경하여 해당 메뉴가 검정색인지 확인
        startMenu.selectedIndex = 0;
        startMenu.updateMenu();
        assertEquals("Standard Game", startMenu.menuItems.get(0).getText());
        assertEquals("Item Mode Game", startMenu.menuItems.get(1).getText());
        assertEquals("Battle Mode Game", startMenu.menuItems.get(2).getText());
        assertEquals("Setting", startMenu.menuItems.get(3).getText());
        assertEquals("Score Board", startMenu.menuItems.get(4).getText());
        assertEquals("Exit", startMenu.menuItems.get(5).getText());

    }
}
