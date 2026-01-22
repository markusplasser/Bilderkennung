package Test;

import Network.Network;

import java.util.Arrays;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Network net = new Network(4,5,7,4);

        double[] input = new double[]{0.2,0.4,0.9,0.1};
        double[] target = new double[]{0,1,0,0};
        for (int j = 0; j < 100; j++) {
            for (int i = 0; i < 10000; i++) {
                net.train(input, target, 3);
            }

            double[] o = net.calculate(input);
            System.out.println(Arrays.toString(o));
        }



    }
}