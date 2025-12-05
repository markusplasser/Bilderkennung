package org.example;

public class Netzwerk
{
    public double[][] output;
    public double[][][] weight;
    public double[][] bias;


    public final int[] NETWORK_LAYER_SIZE;
    public final int INPUT_SIZE;
    public final int OUTPUT_SIZE;
    public final int NETWORK_SIZE;


    public Netzwerk(int... NETWORK_LAYER_SIZE) {
        this.NETWORK_LAYER_SIZE = NETWORK_LAYER_SIZE;
        INPUT_SIZE = NETWORK_LAYER_SIZE[0];
        OUTPUT_SIZE = NETWORK_LAYER_SIZE[NETWORK_LAYER_SIZE.length-1];
        NETWORK_SIZE = NETWORK_LAYER_SIZE.length;

        this.output = new double[NETWORK_SIZE][];
        this.weight = new double[NETWORK_SIZE][][];
        this.bias = new double[NETWORK_SIZE][];



        for(int i = 0; i < NETWORK_SIZE; i++)
        {
            output[i] = new double[NETWORK_LAYER_SIZE[i]];

            bias[i] = Netzwerktools.createRandomArray(NETWORK_LAYER_SIZE[i],0.2,0.6);
            if(i>0)
            {
                weight[i] = Netzwerktools.createRandomArray(NETWORK_LAYER_SIZE[i],NETWORK_LAYER_SIZE[i-1],-0.2,0.6);
            }

        }
    }



    public double[] calculate(double... input)
    {
        if(input.length != INPUT_SIZE){return null;}
        output[0] = input;
        for(int layer = 1; layer < NETWORK_SIZE; layer++)
        {
            for(int neuron = 0; neuron < NETWORK_LAYER_SIZE[layer]; neuron++)
            {
                double sum = bias[layer][neuron];

                for(int prevNeuron = 0; prevNeuron < NETWORK_LAYER_SIZE[layer-1]; prevNeuron++)
                {
                    sum += output[layer-1][prevNeuron] * weight[layer][neuron][prevNeuron];
                }
                output[layer][neuron] = sigmoid(sum);
            }
        }

        return output[NETWORK_SIZE-1];
    }

    private double sigmoid(double input)
    {
        return 1d / (1 + Math.exp(-input));
    }
}
