package Network;

import parser.*;

import java.io.*;
import java.util.Arrays;

public class Network
{
    public double[][] output;
    public double[][][] weight;
    public double[][] bias;
    public double[][] err_signal;
    public double[][] output_derivetive;

    public final int[] NETWORK_LAYER_SIZE;
    public final int INPUT_SIZE;
    public final int OUTPUT_SIZE;
    public final int NETWORK_SIZE;


    public Network(int... NETWORK_LAYER_SIZE) {
        this.NETWORK_LAYER_SIZE = NETWORK_LAYER_SIZE;
        INPUT_SIZE = NETWORK_LAYER_SIZE[0];
        OUTPUT_SIZE = NETWORK_LAYER_SIZE[NETWORK_LAYER_SIZE.length-1];
        NETWORK_SIZE = NETWORK_LAYER_SIZE.length;

        this.output = new double[NETWORK_SIZE][];
        this.weight = new double[NETWORK_SIZE][][];
        this.bias = new double[NETWORK_SIZE][];
        this.err_signal = new double[NETWORK_SIZE][];
        this.output_derivetive = new double[NETWORK_SIZE][];


        for(int i = 0; i < NETWORK_SIZE; i++)
        {
            output[i] = new double[NETWORK_LAYER_SIZE[i]];
            err_signal[i] = new double[NETWORK_LAYER_SIZE[i]];
            output_derivetive[i] = new double[NETWORK_LAYER_SIZE[i]];
            bias[i] = NetworkTools.createRandomArray(NETWORK_LAYER_SIZE[i],-2.5,2.7);
            if(i>0)
            {
                weight[i] = NetworkTools.createRandomArray(NETWORK_LAYER_SIZE[i],NETWORK_LAYER_SIZE[i-1],-0.5,0.5);
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
                output_derivetive[layer][neuron] = output[layer][neuron] * (1-output[layer][neuron]);
            }
        }

        return output[NETWORK_SIZE-1];
    }

    public void train(TrainSet set, int loops, int batch_size){
        if(set.INPUT_SIZE != INPUT_SIZE || set.OUTPUT_SIZE != OUTPUT_SIZE){return;}
        for(int i = 0; i<loops; i++){
            TrainSet batch = set.extractBatch(batch_size);
            for(int b = 0; b < batch_size; b++){
                train(batch.getInput(b), batch.getOutput(b), 0.3);
            }
            //System.out.println(MSE(batch));
        }
    }

    public double MSE(double[] input,double[] target){
        if(input.length != INPUT_SIZE || target.length != OUTPUT_SIZE){return 0;}
        calculate(input);
        double v = 0;
        for(int i = 0; i<target.length; i++){
            v += (target[i] - output[NETWORK_SIZE-1][i]) * (target[i] - output[NETWORK_SIZE-1][i]);
        }
        return v / (2d * target.length);
    }

    public double MSE(TrainSet set){
        double v = 0;
        for(int i = 0; i < set.size(); i++){
            v += MSE(set.getInput(i), set.getOutput(i));
        }
        return v / set.size();
    }

    public void train(double[] input, double[] target, double eta)
    {
        if(input.length != INPUT_SIZE || target.length != OUTPUT_SIZE){return;}
        calculate(input);
        backpropagation(target);
        update(eta);
    }

    public void backpropagation(double[] target){
        for(int neuron = 0; neuron < NETWORK_LAYER_SIZE[NETWORK_SIZE-1]; neuron++){
            err_signal[NETWORK_SIZE-1][neuron] = (output[NETWORK_SIZE-1][neuron] - target[neuron])
                    * output_derivetive[NETWORK_SIZE-1][neuron];
        }
        for(int layer = NETWORK_SIZE-2;layer > 0;layer--){
            for(int neuron = 0; neuron < NETWORK_LAYER_SIZE[layer]; neuron++){
                double sum = 0;
                for(int nextneuron = 0; nextneuron < NETWORK_LAYER_SIZE[layer+1];nextneuron++){
                    sum += weight[layer+1][nextneuron][neuron] * err_signal[layer+1][nextneuron];
                }
                this.err_signal[layer][neuron] = sum * output_derivetive[layer][neuron];
            }
        }
    }

    public void update(double eta){
        for(int layer = 1; layer < NETWORK_SIZE; layer++){
            for(int neuron = 0; neuron < NETWORK_LAYER_SIZE[layer]; neuron++){
                for(int prevneuron = 0; prevneuron < NETWORK_LAYER_SIZE[layer-1]; prevneuron++){
                    double delta = - eta * output[layer-1][prevneuron] * err_signal[layer][neuron];
                    weight[layer][neuron][prevneuron] += delta;
                }
                double delta = -eta * err_signal[layer][neuron];
                bias[layer][neuron] += delta;
            }
        }
    }

    private double sigmoid(double input)
    {
        return 1d / (1 + Math.exp(-input));
    }

    public static void main(String[] args){

        try {
            Network net = Network.loadNetwork("res/save.txt");
            System.out.println(Arrays.toString(net.NETWORK_LAYER_SIZE));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void saveNetwork(String fileName) throws Exception {
        Parser p = new Parser();
        p.create(fileName);
        Node root = p.getContent();
        Node netw = new Node("Network");
        Node ly = new Node("Layers");
        netw.addAttribute(new Attribute("sizes", Arrays.toString(this.NETWORK_LAYER_SIZE)));
        netw.addChild(ly);
        root.addChild(netw);
        for (int layer = 1; layer < this.NETWORK_SIZE; layer++) {

            Node c = new Node("" + layer);
            ly.addChild(c);
            Node w = new Node("weights");
            Node b = new Node("biases");
            c.addChild(w);
            c.addChild(b);

            b.addAttribute("values", Arrays.toString(this.bias[layer]));

            for (int we = 0; we < this.weight[layer].length; we++) {

                w.addAttribute("" + we, Arrays.toString(weight[layer][we]));
            }
        }
        p.close();
    }

    public static Network loadNetwork(String fileName) throws Exception {

        Parser p = new Parser();

        p.load(fileName);
        String sizes = p.getValue(new String[] { "Network" }, "sizes");
        int[] si = ParserTools.parseIntArray(sizes);
        Network ne = new Network(si);

        for (int i = 1; i < ne.NETWORK_SIZE; i++) {
            String biases = p.getValue(new String[] { "Network", "Layers", new String(i + ""), "biases" }, "values");
            double[] bias = ParserTools.parseDoubleArray(biases);
            ne.bias[i] = bias;

            for(int n = 0; n < ne.NETWORK_LAYER_SIZE[i]; n++){

                String current = p.getValue(new String[] { "Network", "Layers", new String(i + ""), "weights" }, ""+n);
                double[] val = ParserTools.parseDoubleArray(current);

                ne.weight[i][n] = val;
            }
        }
        p.close();
        return ne;

    }
}
