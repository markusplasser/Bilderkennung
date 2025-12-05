package org.example;

public class NeuralNet{
    private Layer inputLayer;
    private final int inputSize;
    private final int hiddenSize;
    private final Layer[] hiddenLayers;
    private final Layer outputLayer;
    private final int outPutsize;

    public NeuralNet(int inputSize, int hiddenSize, int outPutsize, int ... hiddenlayerSizes) {
        if(hiddenSize != hiddenlayerSizes.length){
            throw new IllegalArgumentException("the ammount of hidden layers specified and the hiddenlayer sizes passed aren't equal in lenght");
        }
        this.inputSize = inputSize;
        this.hiddenSize = hiddenSize;
        this.hiddenLayers = new Layer[hiddenSize];
        this.outPutsize = outPutsize;
        this.outputLayer = new Layer(hiddenlayerSizes[hiddenlayerSizes.length-1], outPutsize);
        instantiateHiddenLayers(hiddenlayerSizes);
    }

    private void instantiateHiddenLayers(int[] sizes){
        hiddenLayers[0] = new Layer(inputSize, sizes[0]);
        for (int i = 1; i < hiddenSize; i++){
            hiddenLayers[i] = new Layer(hiddenLayers[i-1].layerSize, sizes[i]);
        }
    }

    public double[] calculateOutput(){
        if(inputLayer == null){
            throw new IllegalArgumentException("No data was set wit setData");
        }

        hiddenLayers[0].calcLayer(inputLayer);

        for (int i = 1; i < hiddenSize; i ++){
            Layer activeLayer = hiddenLayers[i];
            activeLayer.calcLayer(hiddenLayers[i-1]);
        }

        outputLayer.calcLayer(hiddenLayers[hiddenSize-1]);
        return outputLayer.formatOutputs();
    }

    public void setData(double[] input){
        if(input.length != inputSize){
            throw new IllegalArgumentException("Data is not equal in lenght to specified inputsize");
        }
        this.inputLayer = new Layer(input, inputSize);
    }


}
