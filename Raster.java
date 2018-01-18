package aalPackage;

import java.util.ArrayList;

public class Raster {

    private int nDim;
    private int mDim;
    private ArrayList<ArrayList<Cell>> matrix;

    public Raster(int n, int m, ArrayList<Color> vec){
        this.nDim=n;
        this.mDim=m;
        this.matrix= new ArrayList<ArrayList<Cell>>();

        int length= vec.size();

        for(int i=0;i<n;i++)
            matrix.add(new ArrayList<Cell>());
        for(int i=0, k=0; i<length; i++)
        {
            if(i%m==0 && i!=0)
                k++;
            matrix.get(k).add(new Cell(vec.get(i)));
        }
    }
    public int getDimN() {
        return nDim;
    }

    public int getDimM() {
        return mDim;
    }

    public ArrayList<ArrayList<Cell>> getMatrix() {
        return matrix;
    }
    public void print(){
        for(int i=0;i<nDim;i++) {
            String result="";
            for (int j=0; j < mDim; j++){
                if(matrix.get(i).get(j).isWhite())
                    result+="W ";
                else if (matrix.get(i).get(j).isBlack())
                    result+="B ";
                else result+="G ";
            }
            System.out.println(result);
        }
        return;
    }
}
