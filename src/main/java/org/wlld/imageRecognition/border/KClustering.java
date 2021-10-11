package org.wlld.imageRecognition.border;

import org.wlld.MatrixTools.Matrix;
import org.wlld.MatrixTools.MatrixOperation;
import org.wlld.tools.ArithUtil;

import java.util.*;

/**
 * @author lidapeng
 * @description //k均值聚类工具
 * @date 10:14 上午 2020/2/4
 */
public class KClustering {
    private List<Box> matrixList = new ArrayList<>();//聚类集合
    private int length;//向量长度
    private int speciesQuantity;//种类数量
    private Matrix[] matrices;//均值K
    private Map<Integer, List<Box>> clusterMap = new HashMap<>();//簇

    public Matrix[] getMatrices() {
        return matrices;
    }

    public Map<Integer, List<Box>> getClusterMap() {
        return clusterMap;
    }

    public KClustering(int speciesQuantity) {
        this.speciesQuantity = speciesQuantity;
        matrices = new Matrix[speciesQuantity];
        for (int i = 0; i < speciesQuantity; i++) {
            clusterMap.put(i, new ArrayList<>());
        }
    }

    public void setMatrixList(Box matrixBody) throws Exception {
        if (matrixBody.getMatrix().isVector() && matrixBody.getMatrix().isRowVector()) {
            Matrix matrix = matrixBody.getMatrix();
            if (matrixList.size() == 0) {
                matrixList.add(matrixBody);
                length = matrix.getY();
            } else {
                if (length == matrix.getY()) {
                    matrixList.add(matrixBody);
                } else {
                    throw new Exception("vector length is different");
                }
            }
        } else {
            throw new Exception("this matrix is not vector or rowVector");
        }
    }

    private Matrix[] averageMatrix() throws Exception {
        Matrix[] matrices2 = new Matrix[speciesQuantity];//待比较均值K
        for (Box matrixBody : matrixList) {//遍历当前集合
            Matrix matrix = matrixBody.getMatrix();
            double min = 0;
            int id = 0;
            for (int i = 0; i < matrices.length; i++) {
                double dist = MatrixOperation.getEDist(matrix, matrices[i]);
                if (min == 0 || dist < min) {
                    min = dist;
                    id = i;
                }
            }
            List<Box> matrixList1 = clusterMap.get(id);
            matrixList1.add(matrixBody);
        }
        //重新计算均值
        for (Map.Entry<Integer, List<Box>> entry : clusterMap.entrySet()) {
            Matrix matrix = average(entry.getValue());
            matrices2[entry.getKey()] = matrix;
        }
        return matrices2;
    }

    private void clear() {
        for (Map.Entry<Integer, List<Box>> entry : clusterMap.entrySet()) {
            entry.getValue().clear();
        }
    }

    private Matrix average(List<Box> matrixList) throws Exception {//进行矩阵均值计算
        double nub = ArithUtil.div(1, matrixList.size());
        Matrix matrix = new Matrix(1, length);
        for (Box matrixBody1 : matrixList) {
            matrix = MatrixOperation.add(matrix, matrixBody1.getMatrix());
        }
        MatrixOperation.mathMul(matrix, nub);
        return matrix;
    }


    public void start() throws Exception {//开始聚类
        if (matrixList.size() > 1) {
            Random random = new Random();
            for (int i = 0; i < matrices.length; i++) {//初始化均值向量
                int index = random.nextInt(matrixList.size());
                //要进行深度克隆
                matrices[i] = matrixList.get(index).getMatrix();
            }
            //进行两者的比较
            boolean isEqual = false;
            int nub = 0;
            do {
                Matrix[] matrices2 = averageMatrix();
                isEqual = equals(matrices, matrices2);
                if (!isEqual) {
                    matrices = matrices2;
                    clear();
                }
                nub++;
            }
            while (!isEqual);
            //聚类结束，进行坐标均值矩阵计算
            System.out.println("聚类循环次数：" + nub);
            
        } else {
            throw new Exception("matrixList number less than 2");
        }
    }

    public boolean equals(Matrix[] matrices1, Matrix[] matrices2) throws Exception {
        boolean isEquals = true;
        for (int i = 0; i < matrices1.length; i++) {
            Matrix matrix1 = matrices1[i];
            Matrix matrix2 = matrices2[i];
            for (int j = 0; j < length; j++) {
                if (matrix1.getNumber(0, j) != matrix2.getNumber(0, j)) {
                    isEquals = false;
                    break;
                }
            }
            if (!isEquals) {
                break;
            }
        }
        return isEquals;
    }
}