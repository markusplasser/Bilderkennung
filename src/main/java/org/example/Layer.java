package org.example;

public class Layer {
    public int prevLayerSize;
    public int layerSize;
    public Neuron[] neurons;

    public Layer(int prevLayerSize, int LayerSize){
        this.prevLayerSize = prevLayerSize;
        this.layerSize = LayerSize;
        neurons = new Neuron[layerSize];
        instantiateNeurons();
    }

    public Layer(double[] inputs, int layerSize){
        this.prevLayerSize = inputs.length;
        this.layerSize = layerSize;
        neurons = new Neuron[layerSize];
        instantiateNeurons();
        openPrivLayer(inputs);
    }


    private void instantiateNeurons(){
        for (int i = 0; i< layerSize; i ++){
            neurons[i] = new Neuron(prevLayerSize);
        }
    }

    public double[] formatOutputs(){
        double[] output = new double[layerSize];
        for (int i = 0; i < layerSize; i++){
            output[i] = neurons[i].output;
        }
        return output;
    }

    private void openPrivLayer(double[] inputs){
        for (Neuron neuron: neurons){
            neuron.calculateOutput(inputs);
        }
    }


    public void calcLayer(Layer l){
        for (int i = 0; i < layerSize; i++){
            Neuron neuron = neurons[i];
            neuron.calculateOutput(l.formatOutputs());
        }
    }

}
