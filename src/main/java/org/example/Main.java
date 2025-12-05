package org.example;

import java.util.Arrays;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Netzwerk net = new Netzwerk(4,5,7,4);
        double[] output = net.calculate(0.2,0.8,0.9,0.1);
        System.out.println(Arrays.toString(output));
    }
}