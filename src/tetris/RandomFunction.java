package tetris;
import java.util.Random;

public class RandomFunction {
	public static int randomFunction(double[] fitness) {
        Random rand = new Random();
        
        // 1. 개체를 랜덤하게 하나 선택
        int selectedIndex = rand.nextInt(fitness.length);
        
        // 2. 선택된 개체의 fitness와 최대 fitness 비교
        double maxFitness = getMaxFitness(fitness);
        if (rand.nextDouble() < fitness[selectedIndex] / maxFitness) {
            return selectedIndex;
        } else {
            return randomFunction(fitness); // 재귀 호출
        }
    }
    
    private static double getMaxFitness(double[] fitness) {
        double max = 0;
        for (double f : fitness) {
            if (f > max) {
                max = f;
            }
        }
        return max;
    }
    
    public static void testRandomFunction() {
        // 1과 2가 60%와 40%의 확률로 나오도록 fitness 값 설정
        double[] fitness = {0.14, 0.14,0.14, 0.14,0.14, 0.14,0.14};
        
        int count1 = 0, count2 = 0;
        for (int i = 0; i < 1000; i++) {
            int selected = randomFunction(fitness);
            if (selected == 0) {
                count1++;
            } else {
                count2++;
            }
        }
        
        System.out.println("1 selected: " + count1 + " (60%)");
        System.out.println("2 selected: " + count2 + " (40%)");
    }
}
