package tetris;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class RandomFunctionTest {

    @Test
    void testRandomFunctionDistribution() {
        double[] fitness = {0.4, 0.2, 0.2, 0.2}; // 총합: 1.0, 각각의 선택 확률 설정
        int[] counts = new int[fitness.length];
        int totalRuns = 10000;
        for (int i = 0; i < totalRuns; i++) {
            int selected = RandomFunction.randomFunction(fitness);
            counts[selected]++;
        }

        // 각 인덱스가 선택될 확률이 fitness에 비례하는지 확인
        for (int i = 0; i < counts.length; i++) {
            double expected = fitness[i] * totalRuns;
            double actual = counts[i];
            double tolerance = totalRuns * 0.05; // 5%의 허용 오차
            assertTrue(Math.abs(expected - actual) < tolerance,
                    "Index " + i + " selected " + actual + " times, but expected " + expected);
        }
    }
}
