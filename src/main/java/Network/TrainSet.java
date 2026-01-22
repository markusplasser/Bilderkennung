package Network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class TrainSet
{
    public final int INPUT_SIZE;
    public final int OUTPUT_SIZE;

    private ArrayList<double[][]> data = new  ArrayList<>();
    public TrainSet(int INPUT_SIZE, int OUTPUT_SIZE)
    {
        this.INPUT_SIZE = INPUT_SIZE;
        this.OUTPUT_SIZE = OUTPUT_SIZE;
    }

    public void addData(double[] input, double[] expected){
        if(input.length != INPUT_SIZE || expected.length != OUTPUT_SIZE){return;}
        data.add(new double[][]{input,expected});
    }

    public int size(){return data.size();}

    public double[] getInput(int index){return data.get(index)[0];}

    public double[] getOutput(int index){return data.get(index)[1];}

    public TrainSet extractBatch(int ammount){
        TrainSet set = new TrainSet(INPUT_SIZE, OUTPUT_SIZE);
        ArrayList<Integer> check = new ArrayList<Integer>();
        Random rand = new Random();
        for(int i = 0; i < ammount; i++){
            int ran = rand.nextInt(data.size());
            if(check.contains(ran)){
                i--;
            }
            else {
                set.addData(this.data.get(ran)[0], this.data.get(ran)[1]);
                check.add(ran);
            }
        }
        return set;
    }
    
    public static void main(String[] args){
        TrainSet set = new TrainSet(3,2);
        for(int i = 0; i < 8; i++){
            double[] a = new double[3];
            double[] b = new double[2];

            for(int j = 0; j < 3; j++){
                a[j] = Math.random()*10;
                if(j<2){
                    b[j] = Math.random()*10;
                }
            }
            set.addData(a,b);
        }

        System.out.println(set);
        System.out.println(set.extractBatch(3));
    }

    public String toString(){
        String ret = "";
        for(int i = 0; i < data.size(); i++){
            ret += (i+":"+ Arrays.toString(data.get(i)[0]) +"    >-||-<    "+ Arrays.toString(data.get(i)[1]) + "\n");
        }
        return ret;
    }
}
