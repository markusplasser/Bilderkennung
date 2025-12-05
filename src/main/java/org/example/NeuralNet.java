package org.example;

public class NeuralNet{
    private double[] inputLayer;
    private int inputSize;
    private int hiddenSize;
    private Layer[] hiddenLayers;
    private Layer outputLayer;
    private int outPutsize;

    public NeuralNet(int inputSize, int hiddenSize, int outPutsize, int ... hiddenlayerSizes) {
        this.inputSize = inputSize;
        this.hiddenLayers = new Layer[hiddenSize];
        this.outPutsize = outPutsize;
        this.outputLayer = new Layer(hiddenlayerSizes[hiddenlayerSizes.length-1], outPutsize);
    }

    public void setData(double[] input){
        if(input.length != inputSize){
            throw new IllegalArgumentException("Data is not equal in lenght to specified inputsize");
        }
        this.inputLayer = input;
    }
}
