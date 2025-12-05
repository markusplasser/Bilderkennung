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


    private void instantiateNeurons(){
        for (Neuron neuron: neurons){
            neuron = new Neuron(prevLayerSize);
        }
    }

    public double[] formatOutputs(){
        double[] output = new double[layerSize];
        for (int i = 0; i < layerSize; i++){
            output[i] = neurons[i].output;
        }
        return output;
    }


    public void calcLayer(Layer l){
        for (int i = 0; i < prevLayerSize; i++){
            Neuron neuron = neurons[i];
            neuron.calculateOutput(l.formatOutputs());
        }
    }

}
