package aalPackage;

import java.util.*;
import java.util.ArrayList;

public class Solver {

    private WGraph graph;
    private Raster raster;
    private ArrayList<ArrayList<Cell>> matrix;
    private int m;
    private int n;
    private long bruteForceTime;
    private long bronKerboschTime;
    private long heuristicsTime;

    public long getBruteForceTime() {
        return bruteForceTime;
    }

    public long getBronKerboschTime() {
        return bronKerboschTime;
    }

    public long getHeuristicsTime() {
        return heuristicsTime;
    }

    public Solver(WGraph graph, Raster raster){
        this.graph=graph;
        this.raster=raster;
        this.matrix = raster.getMatrix();
        this.m= raster.getDimM();
        this.n= raster.getDimN();
    }

    public ArrayList<Integer> maxCliqueBruteForce() {
        long start= System.nanoTime();

        ArrayList<Integer> solution = new ArrayList<Integer>();

        ArrayList<Integer> whiteCellsLocation= new ArrayList<Integer>();
        for(int i=0;i<n;i++)
            for(int j=0;j<m;j++)
            {
                if(matrix.get(i).get(j).isWhite())
                    whiteCellsLocation.add(new Integer(i*m+j));
            }

        int size= whiteCellsLocation.size();
        //  i- number of pawns
        ArrayList<Boolean> vector= new ArrayList<Boolean>(size);
        ArrayList<Boolean> vectorSolution= new ArrayList<Boolean>();
        for(int i=1;i<=size;i++)
        {
            for(int j=0;j<size;j++)
            {
                if(j<i)
                    vector.add(new Boolean(true));
                else vector.add(new Boolean(false));
            }//to for
            ArrayList<Boolean> sol=checkPermutationsValid(vector, whiteCellsLocation);
            if(!sol.isEmpty()) {
                vectorSolution=sol;
                vector= new ArrayList<Boolean>(size);
            }
            else break;
        }//to for
        for(int i=0;i<vectorSolution.size();i++)
        {
            if(vectorSolution.get(i).equals(true))
                solution.add(whiteCellsLocation.get(i));
        }
        bruteForceTime=System.nanoTime()-start;
        return solution;
    }

    public ArrayList<Node> maxCliqueBronKerbosch() {
        long start = System.nanoTime();

        ArrayList<Node> solution = new ArrayList<Node>();

        Stack stack = new Stack();
        stack.push(new ArrayList<Node>());
        stack.push(new ArrayList<Node>(graph.getNodes()));
        stack.push(new ArrayList<Node>());

        while(!stack.empty())
        {
            ArrayList<Node> x= (ArrayList<Node>) stack.pop();
            ArrayList<Node> p= (ArrayList<Node>) stack.pop();
            ArrayList<Node> r= (ArrayList<Node>) stack.pop();

            if(p.isEmpty() && x.isEmpty())
            {

                if(r.size()>solution.size())
                    solution=r;
            }
            if(!p.isEmpty())
            {
                /*******************************/
                Node v = p.get(0);
                stack.push(new ArrayList<Node>(r));
                stack.push(new ArrayList<Node>(p.subList(1,p.size())));
                ArrayList<Node> a= new ArrayList<Node>(x);
                a.add(p.get(0));
                stack.push(a);
                /*******************************/
                ArrayList<Node> b= new ArrayList<Node>(r);
                b.add(v);
                stack.push(b);
                ArrayList<Node> neighbors= new ArrayList<Node>(v.getNeighbors());

                ArrayList<Node> c= new ArrayList<Node>();
                for(Node n: p)
                    if(neighbors.contains(n))
                        c.add(n);
                stack.push(c);

                ArrayList<Node> d= new ArrayList<Node>();
                for(Node n: x)
                    if(neighbors.contains(n))
                        c.add(n);
                stack.push(c);
                /*******************************/

            }
        }//to while
        bronKerboschTime=System.nanoTime()-start;
        return solution;
    }//to maxCliqueBronKerbosch()

    public ArrayList<Integer> maxCliqueHeuristics() {
        long start= System.nanoTime();

        ArrayList<Integer> solution = new ArrayList<Integer>();


        ArrayList<ArrayList<Integer>> neighbourNumber = new ArrayList<ArrayList<Integer>>();
        for(int i=0;i<n;i++)
            neighbourNumber.add(new ArrayList<Integer>());

        for(int i=0;i<n;i++)
            for(int j=0;j<m;j++)
            {
                if(matrix.get(i).get(j).isWhite())
                    neighbourNumber.get(i).add(new Integer(countNeighbours(i,j)+1));
                else
                    neighbourNumber.get(i).add(new Integer(0));
            }

        int max=-1;
        int maxI=-1;
        int maxJ=-1;
        while(true) {
            max=0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < m; j++) {
                    if (max < neighbourNumber.get(i).get(j)) {
                        max = neighbourNumber.get(i).get(j);
                        maxI = i;
                        maxJ = j;
                    }
                }//to for
            }//to for
            if (max==1)
                break;

            erasePawn(maxI,maxJ,neighbourNumber);

        }//to while

        for(int i=0;i<n;i++) {
            for (int j = 0; j < m; j++) {
                if(neighbourNumber.get(i).get(j)==1)
                    solution.add(i*m+j);
            }
        }
        heuristicsTime=System.nanoTime()-start;
        return solution;
    }

    /*********************************************************************************************************/
    private int countNeighbours(int i, int j) {

        int neigh = 0;

        for (int a = j-1; a>=0; a--) {
            if (matrix.get(i).get(a).isWhite())
                neigh++;
            else if (matrix.get(i).get(a).isBlack())
                break;
        }

        for (int a = j+1; a < m; a++) {
            if (matrix.get(i).get(a).isWhite())
                neigh++;
            else if (matrix.get(i).get(a).isBlack())
                break;
        }

        for (int b = i-1; b>=0; b--){
            if(matrix.get(b).get(j).isWhite())
                neigh++;
            else if (matrix.get(b).get(j).isBlack())
                break;
        }

        for (int b = i+1; b<n; b++) {
            if (matrix.get(b).get(j).isWhite())
                neigh++;
            else if (matrix.get(b).get(j).isBlack())
                break;
        }

        return neigh;
    }
    private void erasePawn(int i, int j, ArrayList<ArrayList<Integer>> neighbourNumber)
    {
        for (int a = j-1; a>=0; a--) {
            if (matrix.get(i).get(a).isWhite()){
                int k=neighbourNumber.get(i).get(a);
                if(k!=0) {
                    k--;
                    neighbourNumber.get(i).set(a, new Integer(k));
                }
            }
            else if (matrix.get(i).get(a).isBlack())
                break;
        }

        for (int a = j+1; a < m; a++) {
            if (matrix.get(i).get(a).isWhite()){
                int k=neighbourNumber.get(i).get(a);
                if(k!=0) {
                    k--;
                    neighbourNumber.get(i).set(a, new Integer(k));
                }
            }
            else if (matrix.get(i).get(a).isBlack())
                break;
        }

        for (int b = i-1; b>=0; b--){
            if(matrix.get(b).get(j).isWhite()){
                int k=neighbourNumber.get(b).get(j);
                if(k!=0){
                    k--;
                    neighbourNumber.get(b).set(j,new Integer(k));
                }
            }
            else if (matrix.get(b).get(j).isBlack())
                break;
        }

        for (int b = i+1; b<n; b++) {
            if (matrix.get(b).get(j).isWhite()){
                int k=neighbourNumber.get(b).get(j);
                if(k!=0) {
                    k--;
                    neighbourNumber.get(b).set(j, new Integer(k));
                }
            }
            else if (matrix.get(b).get(j).isBlack())
                break;
        }

        neighbourNumber.get(i).set(j,new Integer(0));
    }
    /*********************************************************************************************************/
    /*checkPermutationsValid- check if permutation of vector and positions make pawns don't see*/

    private ArrayList<Boolean> checkPermutationsValid(ArrayList<Boolean> vector, ArrayList<Integer> cellsIndexes){

        ArrayList<ArrayList<Boolean>> vecSet = new ArrayList<ArrayList<Boolean>>();
        permute(vecSet,vector,cellsIndexes,0);

        for(ArrayList<Boolean> vec: vecSet)
        {
            if(validComposition(vec,cellsIndexes))
                return vec;
        }
       return new ArrayList<Boolean>();

    }
    public void permute(ArrayList<ArrayList<Boolean>> vecSet,ArrayList<Boolean> vec,ArrayList<Integer> cellIndexes, int k){
                for(int i = k; i < vec.size(); i++){
                    Collections.swap(vec, i, k);
                    permute(vecSet,new ArrayList<Boolean>(vec), cellIndexes,k+1);
                    Collections.swap(vec, k, i);
                }
                if (k == vec.size() -1){
                    try{
                    vecSet.add(vec);}
                    catch(Exception e){}
                    int a=0;
                }
    }
    private boolean validComposition(ArrayList<Boolean> vec, ArrayList<Integer> cellIndexes){
        int vecSize= vec.size();
        ArrayList<Integer> pawnIndexes = new ArrayList<Integer> ();
        for(int i=0;i<vecSize;i++)
        {
            if(vec.get(i).equals(true))
                pawnIndexes.add(cellIndexes.get(i));
        }

        for(int i=0;i<pawnIndexes.size();i++)
        {
            for(int j=i+1;j<pawnIndexes.size();j++)
            {
                int a=pawnIndexes.get(i);
                int b= pawnIndexes.get(j);
                //this same row or column
                if(((a%m)==(b%m))|| ((a/m)==(b/m))) {
                    if(isBlackBetween((a/m),(a%m),(b/m),(b%m),raster)==false)
                        return false;
                }
            }

        }
        return true;
    }
    private boolean isBlackBetween(int x1, int y1, int x2, int y2, Raster raster){
        if(x1==x2){
            int row=x1;
            for(int i=y1+1;i<y2;i++){
                if(matrix.get(x1).get(i).isBlack())
                    return true;
            }//to for
        }//to if
        else{
            int column=y1;
            for(int i=x1+1;i<x2;i++){
                if(matrix.get(i).get(column).isBlack())
                    return true;
            }//to for
        }
        return false;
    }

}
 /**********************************************
    * BronKerbosch(P):
        S := empty stack
        S.push({}, P, {})
        while S is not empty:
            R, P, X := S.pop()
            if P and X are both empty:
                 report R as a maximal clique
            if P is not empty:
                v := some vertex in P
                S.push(R, P \ {v}, X ⋃ {v})
                S.push(R ⋃ {v}, P ⋂ N(v), X ⋂ N(v))
        p-sets of nodes which are candidates for solution
        R-set of nodes, which are predicted to be solution
        X-set of nodes which are ommitted
  ***********************************************/