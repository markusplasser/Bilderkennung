package org.example;

import java.util.Arrays;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        NeuralNet net = new NeuralNet(10, 3, 1, 5, 3, 2);
        net.setData(new double[]{0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 1.1, 1.2, 1.3, 1.4});
        System.out.println(Arrays.toString(net.calculateOutput()));
    }
}