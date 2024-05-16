package start;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.Before;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicBoolean;

public class ScoreBoardTest extends ApplicationTest {
    private Stage stage;
    private ScoreBoard scoreBoard;
    private int score;
    private String name;
    private String mode;

    @Before
    public void setUp() {
        scoreBoard = new ScoreBoard();
    }

    //@Override
    //public void start(Stage stage) throws Exception {
        //this.stage = stage;
    //}

    @Test
    public void testCreateScene() {
        // Given
        String expectedMode1 = "Standard Mode";
        String expectedMode2 = "Item Mode";

        // When
        Scene scene = scoreBoard.createScene(stage,2);
        GridPane root = (GridPane) scene.getRoot();
        VBox scoreBox1 = null;
        VBox scoreBox2 = null;
        Button backButton = null;
        for (Node node : root.getChildren()) {
            if (GridPane.getColumnIndex(node) == 0 && node instanceof VBox) {
                scoreBox1 = (VBox) node;
            } else if (GridPane.getColumnIndex(node) == 1 && node instanceof VBox) {
                scoreBox2 = (VBox) node;
            } else if (GridPane.getColumnIndex(node) == 0 && node instanceof Button) {
                backButton = (Button) node;
            }
        }

        // Then
        assertNotNull(scoreBox1);
        assertNotNull(scoreBox2);
        assertNotNull(backButton);
        assertTrue(scoreBox1.getChildren().stream()
                .anyMatch(child -> child instanceof Text && ((Text) child).getText().equals(expectedMode1 + " (Normal)")));
        assertTrue(scoreBox2.getChildren().stream()
                .anyMatch(child -> child instanceof Text && ((Text) child).getText().equals(expectedMode2+ " (Normal)")));
    }
    
    @Test
    public void testCreateScene_2() {
        // Given
        String expectedMode1 = "Standard Mode";
        String expectedMode2 = "Item Mode";
        
        mode=expectedMode1;
        name="JuHyeok";
        score=1000000;
        
        //주의 테스트를 하면 실제로 test.txt에 반영이 됨
        AtomicBoolean flag = new AtomicBoolean(false); // flag 변수를 AtomicBoolean으로 선언
        scoreBoard.addScoreToFile(name,score,mode,2);//테스트로 위에 주어진 이름과 기록점수를 게임 종료시 입력받았을 때 처럼 호출
        
        // When
        Scene scene = scoreBoard.createScene(stage,score,name,mode,2);
        GridPane root = (GridPane) scene.getRoot();
        VBox scoreBox1 = null;
        VBox scoreBox2 = null;
        Button backButton = null;
        Button exitButton = null;
        
        
        for (Node node : root.getChildren()) {
            if (GridPane.getColumnIndex(node) == 0 && node instanceof VBox) {
                scoreBox1 = (VBox) node;
            } else if (GridPane.getColumnIndex(node) == 1 && node instanceof VBox) {
                scoreBox2 = (VBox) node;
            } else if (GridPane.getColumnIndex(node) == 0 && node instanceof Button) {
                backButton = (Button) node;
            } else if (GridPane.getColumnIndex(node) == 1 && node instanceof Button) {
                exitButton = (Button) node;
            }
        }
        
        // Then
        assertNotNull(scoreBox1);
        assertNotNull(scoreBox2);
        assertNotNull(backButton);
        assertNotNull(exitButton);
        assertTrue(scoreBox1.getChildren().stream()
                .anyMatch(child -> child instanceof Text && ((Text) child).getText().equals(expectedMode1+ " (Normal)")));
        assertTrue(scoreBox2.getChildren().stream()
                .anyMatch(child -> child instanceof Text && ((Text) child).getText().equals(expectedMode2+ " (Normal)")));
        

        // scoreBox1 내의 모든 요소를 테스트하는 코드 -> 새로운 신기록 빨간색으로 나타나는지 확인
        scoreBox1.getChildren().forEach(child -> {//테스트 입력에서 standard모드였음 ->scoreBox1
            if (child instanceof Text) {
            	Text text = (Text) child;
                Paint fill = text.getFill(); // Paint 객체 가져오기
                if (fill instanceof Color) {
                    Color color = (Color) fill; // Paint 객체를 Color로 캐스트
                    if(color.equals(Color.RED)) {
                    	flag.set(true); // 텍스트의 색상이 하나라도 빨간색인 경우(신기록 세운 게 빨간색으로 알맞게 되었다면) flag를 true로 설정
                    } 
                }
            } 
        });
        assertTrue(flag.get());
    }
}
