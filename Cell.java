package aalPackage;

public class Cell {

    Color color;

    public Cell(Color color){
        this.color=color;
    }

    public boolean isWhite(){
        if(color.equals(Color.W))
            return true;
        else return false;
    }
    public boolean isBlack(){
        if(color.equals(Color.B))
            return true;
        else return false;
    }

}
