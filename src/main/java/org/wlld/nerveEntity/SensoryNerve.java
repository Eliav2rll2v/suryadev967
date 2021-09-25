package org.wlld.nerveEntity;

import org.wlld.MatrixTools.Matrix;
import org.wlld.i.OutBack;
import org.wlld.imageRecognition.border.Border;

import java.util.List;
import java.util.Map;

/**
 * @author lidapeng
 * 感知神经元输入层
 * @date 9:29 上午 2019/12/21
 */
public class SensoryNerve extends Nerve {

    public SensoryNerve(int id, int upNub) throws Exception {
        super(id, upNub, "SensoryNerve", 0, 0.1, false, null, false);
    }

    public void postMessage(long eventId, double parameter, boolean isStudy, Map<Integer, Double> E
            , OutBack outBack) throws Exception {//感知神经元输出

        sendMessage(eventId, parameter, isStudy, E, outBack);
    }

    public void postMatrixMessage(long eventId, Matrix parameter, boolean isKernelStudy
            , int E, OutBack outBack) throws Exception {
        sendMatrix(eventId, parameter, isKernelStudy, E, outBack);
    }

    @Override
    public void connect(List<Nerve> nerveList) {//连接第一层隐层神经元
        super.connect(nerveList);
    }
}
