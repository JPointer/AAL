package aalPackage;

import java.util.ArrayList;

public class GraphGenerator {
    private ArrayList<Node> nodes;

    public GraphGenerator(){
        nodes = new ArrayList<Node>();
    }
    public WGraph generate(Raster raster) {
        /*Loop for Create Nodes (if Cell is WHITE)*/
        int n= raster.getDimN();
        int m= raster.getDimM();
        ArrayList<ArrayList<Cell>> matrix = raster.getMatrix();

        for(int i=0;i<n;i++)
            for(int j=0;j<m;j++)
            {
                if(matrix.get(i).get(j).isWhite())
                    nodes.add(new Node(i,j));
            }
        /*Create Edges*/
        int length= nodes.size();
        for(int i=0;i<length;i++)
        {
            Node node= nodes.get(i);
            for(int j=i+1;j<length;j++)
            {
                Node node2 = nodes.get(j);

                if(node.getPositionX()==node2.getPositionX()){
                    int a= node.getPositionY();
                    int b= node2.getPositionY();
                    int c= node. getPositionX();

                    if(isBlackBetween(c,a,c,b,raster)){
                        node.addNeighbor(node2);
                        node2.addNeighbor(node);
                    }

                }
                else if(node.getPositionY()==node2.getPositionY()){
                    int a= node.getPositionX();
                    int b= node2.getPositionX();
                    int c= node. getPositionY();

                    if(isBlackBetween(a,c,b,c, raster)){
                        node.addNeighbor(node2);
                        node2.addNeighbor(node);
                    }
                }
                else {
                    node.addNeighbor(node2);
                    node2.addNeighbor(node);
                }//to else
            }//to for
        }//to for


        WGraph graph = new WGraph(nodes);
        return graph;
    }
    private boolean isBlackBetween(int x1, int y1, int x2, int y2, Raster raster){
       if(x1==x2){
           int row=x1;
           for(int i=y1+1;i<y2;i++){
               if(raster.getMatrix().get(x1).get(i).isBlack())
                   return true;
           }//to for
       }//to if
       else{
           int column=y1;
           for(int i=x1+1;i<x2;i++){
               if(raster.getMatrix().get(i).get(column).isBlack())
                   return true;
           }//to for
       }
        return false;
    }
}
