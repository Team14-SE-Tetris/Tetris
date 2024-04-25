package tetris;
import java.util.Random;

public class RandomFunction {
	public static int randomFunction(double[] fitness) {
		//fitness 배열의 index중 하나를 랜덤 반환
        Random rand = new Random();
        
        // 1. 개체를 랜덤하게 하나 선택
        int selectedIndex = rand.nextInt(fitness.length);
        
        
        // 2. 선택된 개체의 fitness와 최대 fitness 비교
        double maxFitness = 0;
        for (double f : fitness) {
            if (f > maxFitness) {
            	maxFitness = f;
            }
        }
        if (rand.nextDouble() < fitness[selectedIndex] / maxFitness) {
            return selectedIndex;
        } else {
            return randomFunction(fitness); // 재귀 호출
        }
    }
    
}
