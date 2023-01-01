package coverTest;

import org.wlld.regressionForest.RegressionForest;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @param
 * @DATA
 * @Author LiDaPeng
 * @Description
 */
public class ForestTest {
    public static void main(String[] args) throws Exception {
        test();
    }

    public static void test() throws Exception {//对分段回归进行测试
        int size = 2000;
        RegressionForest regressionForest = new RegressionForest(size, 3, 0.2);
        List<double[]> a = fun(0.1, 0.2, 0.3, size);
        List<double[]> b = fun(0.3, 0.2, 0.1, size);
        for (int i = 0; i < 1000; i++) {
            double[] featureA = a.get(i);
            double[] featureB = b.get(i);
            double[] testA = new double[]{featureA[0], featureA[1]};
            double[] testB = new double[]{featureB[0], featureB[1]};
            regressionForest.insertFeature(testA, featureA[2]);
            regressionForest.insertFeature(testB, featureB[2]);
        }
        regressionForest.startStudy();
        regressionForest.regression();//这里进行回归

        double sigma = 0;
        for (int i = 0; i < 1000; i++) {
            double[] feature = a.get(i);
            double[] test = new double[]{feature[0], feature[1]};
            double dist = regressionForest.getDist(test, feature[2]);
            sigma = sigma + Math.pow(dist, 2);
        }
        double avs = sigma / size;
        System.out.println("a误差：" + avs);
        sigma = 0;
        for (int i = 0; i < 1000; i++) {
            double[] feature = b.get(i);
            double[] test = new double[]{feature[0], feature[1]};
            double dist = regressionForest.getDist(test, feature[2]);
            sigma = sigma + Math.pow(dist, 2);
        }
        double avs2 = sigma / size;
        System.out.println("b误差：" + avs2);

    }

    public static List<double[]> fun(double w1, double w2, double w3, int size) {//生成假数据
        List<double[]> list = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            double a = random.nextDouble();
            double b = random.nextDouble();
            double c = w1 * a + w2 * b + w3;
            double[] data = new double[]{a, b, c};
            list.add(data);
        }
        return list;
    }
}
