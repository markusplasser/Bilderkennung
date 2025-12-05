package org.example;

import java.util.Arrays;

public class Neuron {
    public double output;
    public double bias;
    public double[] wheights;
    public double errorGrad;
    public final int inputSize;

    public Neuron(int inputSize){
        // sollte norm distr. sein aber egal gerade
        wheights = Netzwerktools.createRandomArray(inputSize, -0.5, 0.5);
        // bias erst pos.
        bias = Netzwerktools.randomValue(0, 1);
        this.inputSize = inputSize;
    }

    public void calculateOutput(double[] inputs){
        double sum = bias;
        for (int i = 0; i < inputSize; i++){
            sum += inputs[i]*wheights[i];
        }
        output = sigmoid(sum);
    }


    // static damit es im global table ist
    public static double sigmoid(double input){
        return  1d / (1 + Math.exp(-input));
    }
}
