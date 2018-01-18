package aalPackage;


import java.util.ArrayList;

public class Main {

    public static void main(String[] strings){



        if(strings.length==0)
        {
            System.out.println("Wrong input");
        }

        if(strings[0].equals("1")) {
            if(strings.length!=3)
                System.out.println("Wrong input");
            int funNumber= Integer.parseInt(strings[1]);
            String file = strings[2];
            mode1(funNumber,file);
        }
        else if(strings[0].equals("2")) {
            if (strings.length != 6)
                System.out.println("Wrong input");
            int funNumber = Integer.parseInt(strings[1]);
            int nDimension= Integer.parseInt(strings[2]);
            int mDimension= Integer.parseInt(strings[3]);
            int numberOfWhiteCells= Integer.parseInt(strings[4]);
            int numberOfBlackCells= Integer.parseInt(strings[5]);
            mode2(funNumber,nDimension,mDimension,numberOfWhiteCells,numberOfBlackCells);
        }
        else if(strings[0].equals("3")) {
            mode3();
        }
        else System.out.println("Wrong input");


        return;
    }
    private static void mode1(int funNumber, String file){

        RasterGenerator generator = new RasterGenerator();
        Raster raster = generator.generate(file);
        solve(funNumber,raster);

    }
    private static void mode2(int funNumber, int nDimension, int mDimension, int numberOfWhiteCells, int numberofBlackCells){
        RasterGenerator generator = new RasterGenerator();
        Raster raster = generator.generate(nDimension,mDimension,numberOfWhiteCells,numberofBlackCells);
        solve(funNumber,raster);
    }
    private static void mode3(){
        ArrayList<Long> times1 = new ArrayList<Long>();
        ArrayList<Long> times2 = new ArrayList<Long>();
        ArrayList<Long> times3 = new ArrayList<Long>();
        int n=4;
        int m=5;
        /*4,6,8,9,10,12,15,16,20*/
        /*a1-array of NDims*/
        /*a2- array of MDims*/
        int[] a1={2,2,2,2,2,2,2,2,2};
        int[] a2={2,3,4,5,6,7,8,9,10};

        int[] w ={2,3,4,5,6,7,8,9,10};//number of white
        int[] b ={1,2,2,3,3,4,4,5,5};//number of black
        int numberOfTest =9;
        for(int i=0;i<numberOfTest;i++) {

            RasterGenerator generator = new RasterGenerator();
            Raster raster = generator.generate(a1[i],a2[i],w[i],b[i]);

            GraphGenerator graphGenerator = new GraphGenerator();
            WGraph wGraph= graphGenerator.generate(raster);

            Solver solver = new Solver(wGraph, raster);
            ArrayList<Integer> solution1= new  ArrayList<Integer>();
            ArrayList<Node> solution2= new  ArrayList<Node>();
            ArrayList<Integer> solution3= new  ArrayList<Integer>();

            solution1 = solver.maxCliqueBruteForce();
            solution2 = solver.maxCliqueBronKerbosch();
            solution3 = solver.maxCliqueHeuristics();

            times1.add(solver.getBruteForceTime());
            times2.add(solver.getBronKerboschTime());
            times3.add(solver.getHeuristicsTime());

        }

        String leftAlignFormat = "| %-5d | %-20s | %-5f |%n";

        System.out.format("+-------+----------------------+-------+%n");
        System.out.format("|   n   |      time brute      |   q   |%n");
        System.out.format("+-------+----------------------+-------+%n");
        for(int i=0;i<numberOfTest;i++) {
            float q=(float) ((times1.get(i) * Math.pow(2, w[4])) / (times1.get(4) * Math.pow(2, w[i])));
            System.out.format(leftAlignFormat,a1[i]*a2[i],Long.toString(times1.get(i)),q);
        }
        System.out.format("+-------+----------------------+-------+%n");


        System.out.format("+-------+----------------------+-------+%n");
        System.out.format("|   n   |     time kerbosh     |   q   |%n");
        System.out.format("+-------+----------------------+-------+%n");
        for(int i=0;i<numberOfTest;i++) {

            System.out.format(leftAlignFormat,a1[i]*a2[i],Long.toString(times2.get(i)),0.f);
        }
        System.out.format("+-------+----------------------+-------+%n");


        System.out.format("+-------+----------------------+-------+%n");
        System.out.format("|   n   |    time heuristics   |   q   |%n");
        System.out.format("+-------+----------------------+-------+%n");
        for(int i=0;i<numberOfTest;i++) {
            float q=(float) ((times1.get(i) * Math.pow(a1[4]*a2[4], 2) / (times1.get(4) * Math.pow(a1[i]*a2[i], 2))));
            System.out.format(leftAlignFormat,a1[i]*a2[i],Long.toString(times3.get(i)),q);
        }
        System.out.format("+-------+----------------------+-------+%n");
    }

    private static void solve(int funNumber, Raster raster){
        GraphGenerator graphGenerator = new GraphGenerator();
        WGraph wGraph= graphGenerator.generate(raster);

        Solver solver = new Solver(wGraph, raster);
        ArrayList<Integer> solution1= new  ArrayList<Integer>();
        ArrayList<Node> solution2= new  ArrayList<Node>();
        ArrayList<Integer> solution3= new  ArrayList<Integer>();

        if(funNumber==1 || funNumber==4)
            solution1 = solver.maxCliqueBruteForce();
        if(funNumber==2 || funNumber==4)
            solution2 = solver.maxCliqueBronKerbosch();
        if(funNumber==3 || funNumber==4)
            solution3 = solver.maxCliqueHeuristics();

        raster.print();

        if(funNumber==1 || funNumber==4) {
            String raport1 = "Solution1(Brute) length=" + Integer.toString(solution1.size()) + " and examples nodes:";
            for (int n : solution1)
                raport1 += n + "  ";
            System.out.println(raport1);
            System.out.println(solver.getBruteForceTime());
        }
        if(funNumber==2 || funNumber==4) {
            String raport2 = "Solution2(B-K) length=" + Integer.toString(solution2.size()) + " and examples nodes:";
            for (Node n : solution2)
                raport2 += "(" + n.getPositionX() + "," + n.getPositionY() + ")";
            System.out.println(raport2);
            System.out.println(solver.getBronKerboschTime());
        }
        if(funNumber==3 || funNumber==4) {
            String raport3 = "Solution3(Heuristics) length=" + Integer.toString(solution3.size()) + " and examples nodes:";
            for (int n : solution3)
                raport3 += n + "  ";
            System.out.println(raport3);
            System.out.println(solver.getHeuristicsTime());
        }
    }//to solve
}//to Main
