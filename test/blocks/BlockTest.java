package blocks;

import org.junit.jupiter.api.Test;
import java.awt.Color;
import static org.junit.jupiter.api.Assertions.*;

class BlockTest {
    
    @Test
    void testRotate() {
        Block block = new IBlock() {};
        int[][] originalShape = block.getShapeDetail().clone();
        block.rotate();
        block.rotate();
        block.rotate();
        block.rotate();
        int[][] rotatedShape = block.getShapeDetail();
        
        assertArrayEquals(originalShape, rotatedShape, "블록을 4번 회전시킨 후의 모양이 원래의 모양과 동일해야 합니다.");
    }
    
    @Test
    void testChangeItem() {
        Block block = new JBlock() {};
        block.changeItem(2); // 폭탄 아이템으로 변경
        assertEquals(2, block.getItem(), "아이템이 올바르게 변경되어야 합니다.");
    }

    @Test
    void testWhereOBlock() {
        Block block = new OBlock();
        int[] expectedPosition = {1, 1}; // 네 번째 블록의 위치
        assertArrayEquals(expectedPosition, block.whereBlock(3), "첫 번째 비어있지 않은 블록의 위치가 올바르게 반환되어야 합니다.");
    }
    
  
    @Test
    void testGetShape() {
        Block block = new Block() {}; // Block은 추상 클래스이므로 인스턴스화를 위해 익명 클래스를 사용
        assertEquals(1, block.getShape(0, 0), "0,0 위치의 값이 1이어야 합니다.");
        assertEquals(1, block.getShape(1, 1), "1,1 위치의 값이 1이어야 합니다.");
    }



    @Test
    void testChangeShape() {
        Block block = new ZBlock() {}; // Block은 추상 클래스이므로 인스턴스화를 위해 익명 클래스를 사용
        int[][] newShape = {
                {0, 1},
                {1, 1}
        };
        block.changeShape(newShape);
        assertArrayEquals(newShape, block.getShapeDetail(), "변경된 모양이 일치해야 합니다.");
        assertEquals(0, block.getShape(0, 0), "0,0 위치의 값이 0이어야 합니다.");
        assertEquals(1, block.getShape(1, 0), "1,0 위치의 값이 1이어야 합니다.");
    }
    
    @Test
    public void testChangeShapeDetail() {
        Block block = new Block() {}; // 익명 클래스를 이용한 인스턴스 생성

        // shape의 초기 상태 검증
        assertArrayEquals(new int[][]{{1, 1}, {1, 1}}, block.getShapeDetail());

        // shape 변경 테스트
        block.changeShapeDetail(0, 0, 2);
        assertEquals(2, block.getShape(0, 0), "Shape의 (0,0) 위치 값이 2로 변경되어야 합니다.");

        // 변경된 shape 전체 상태 검증
        assertArrayEquals(new int[][]{{2, 1}, {1, 1}}, block.getShapeDetail());
    }

    @Test
    public void testChangeColor() {
        Block block = new Block() {}; // 익명 클래스를 이용한 인스턴스 생성

        // color와 color_num의 초기 상태 검증
        assertEquals(Color.YELLOW, block.getColor(), "초기 색상은 YELLOW이어야 합니다.");
        assertEquals(4, block.getColorNum(), "초기 color_num은 4여야 합니다.");

        // 색상 변경 테스트
        block.changeColor(Color.RED, 1);
        assertEquals(Color.RED, block.getColor(), "색상이 RED로 변경되어야 합니다.");
        assertEquals(1, block.getColorNum(), "color_num이 1로 변경되어야 합니다.");
    }
    
    @Test
    public void testWidth() {
        // Block 인스턴스 생성을 위한 익명 클래스 사용
        Block block = new Block() {
            // Block은 추상 클래스이므로, 직접 인스턴스화할 수 없습니다.
            // 따라서 익명 클래스를 사용하여 테스트에 필요한 메소드를 구현합니다.
        };

        // 기본 생성자에 의해 설정된 shape의 너비 검증
        assertEquals(2, block.width(), "기본 shape의 너비가 올바르지 않습니다.");

        // shape 변경 후 너비 검증
        int[][] newShape = {
                {1, 0, 1},
                {1, 1, 1}
        };
        block.changeShape(newShape); // shape 변경
        assertEquals(3, block.width(), "변경된 shape의 너비가 올바르지 않습니다.");

        // 빈 shape 설정 후 너비 검증
        int[][] emptyShape = {};
        block.changeShape(emptyShape); // 빈 shape로 변경
        assertEquals(0, block.width(), "빈 shape의 너비 검증이 올바르지 않습니다.");
    }
}
