package Test;

import Network.*;
import Mnist.*;

/**
 * Created by Luecx on 10.08.2017.
 */
public class Mnist {

    public static void main(String[] args) {

        try {
            Network net = Network.loadNetwork("res/save.txt");
            TrainSet testSet = createTrainSet(10000,14999);
            testTrainSet(net, testSet, 100);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static TrainSet createTrainSet(int start, int end) {

        TrainSet set = new TrainSet(28 * 28, 10);

        try {
            MnistImageFile m = new MnistImageFile("C:\\Users\\marku\\OneDrive\\Bilderkennung\\src\\main\\resources\\trainImage.idx3-ubyte", "rw");
            MnistLabelFile l = new MnistLabelFile("C:\\Users\\marku\\OneDrive\\Bilderkennung\\src\\main\\resources\\trainLabel.idx1-ubyte", "rw");

            for(int i = start; i <= end; i++) {
                if(i % 100 ==  0){
                    System.out.println("prepared: " + i);
                }

                double[] input = new double[28 * 28];
                double[] output = new double[10];

                output[l.readLabel()] = 1d;
                for(int j = 0; j < 28*28; j++){
                    input[j] = (double)m.read() / (double)255;
                }

                set.addData(input, output);
                m.next();
                l.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

         return set;
    }

    public static void trainData(Network net, TrainSet set, int epochs, int loops, int batch_size, String outputFile) {
        for(int e = 0; e < epochs;e++) {
            net.train(set, loops, batch_size);
            try {
                net.saveNetwork(outputFile);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public static void testTrainSet(Network net, TrainSet set, int printSteps) {
        int correct = 0;
        for(int i = 0; i < set.size(); i++) {

            double[] netOut = net.calculate(set.getInput(i));
            int highest = NetworkTools.indexOfHighestValue(netOut);
            int actualHighest = NetworkTools.indexOfHighestValue(set.getOutput(i));
            if(highest == actualHighest) {
                correct ++ ;
            }
            if(i % printSteps == 0) {
                //System.out.println(i + ": " + (double)correct / (double) (i + 1));
                System.out.printf("%d: %d (%f); %d (%f)\n", i, highest, netOut[highest], actualHighest, netOut[actualHighest]);
            }
        }
        System.out.println("Testing finished, RESULT: " + correct + " / " + set.size()+ "  -> " + (double)correct / (double)set.size() +" %");
    }
}
