package aalPackage;

import java.io.File;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.TreeSet;

public class RasterGenerator {

    public Raster generate(String fileName){
        int n=0,m=0;
        ArrayList<Color> vec= new ArrayList<Color>();

        try{
            File file = new File(fileName);

            Scanner sc= new Scanner(file);

            n = sc.nextInt();
            m = sc.nextInt();

            while(sc.hasNext()){
                String color = sc.next();

                if(color.equals("G"))
                    vec.add(Color.G);
                else if(color.equals("B"))
                    vec.add(Color.B);
                else if(color.equals("W"))
                    vec.add(Color.W);
            }
        }
        catch(Exception e){}


        return new Raster(n,m,vec);
    }
    public Raster generate(int n, int m, int w, int b){
        ArrayList<Color> vec= new ArrayList<Color>();

        TreeSet<Integer> whitePositions =new TreeSet<Integer>();
        TreeSet<Integer> blackPositions =new TreeSet<Integer>();
        Random r = new Random();
        int lowerBound = 0;
        int upperBound = n*m;
        while (whitePositions.size()!=w){
            whitePositions.add(r.nextInt(upperBound-lowerBound) + lowerBound);
        }
        while(blackPositions.size()!=b){
            int result =r.nextInt(upperBound-lowerBound) + lowerBound;
            if(!whitePositions.contains(result))
                blackPositions.add(result);
        }

        for(int i=0;i<n*m;i++) {
            if(whitePositions.contains(i))
                vec.add(Color.W);
            else if(blackPositions.contains(i))
                vec.add(Color.B);
            else vec.add(Color.G);
        }

        return new Raster(n,m,vec);
    }
}
