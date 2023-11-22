import java.util.ArrayList;

public class FunctionSats {
    private String name;
    private int ciclomatic;
    private ArrayList<String> nodes;
    private ArrayList<ArrayList<String>> graph;
    private int edges;
    private ArrayList<Integer> way;
    private int length;
    private ArrayList<FunctionSats> dependencies;

    public FunctionSats(String name){
        this.name=name;
        this.ciclomatic=0;
        this.nodes=new ArrayList<>();
        this.edges=0;
        this.way=new ArrayList<>();
        this.graph=new ArrayList<>();
        this.length=0;
        this.dependencies=new ArrayList<>();
    };

    public String getName() {
        return name;
    }

    public int getCiclomatic() {
        return this.edges-this.nodes.size()+2;
    }

    public ArrayList<String> getNodes() {
        return nodes;
    }

    public void setNodes(ArrayList<String> nodes) {
        this.nodes = nodes;
    }

    public int getEdges() {
        return edges;
    }

    public void setEdges(int edges) {
        this.edges = edges;
    }

    public ArrayList<Integer> getWay() {
        return way;
    }

    public void setWay(ArrayList<Integer> way) {
        this.way = way;
    }

    public ArrayList<ArrayList<String>> getGraph() {
        return graph;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public ArrayList<FunctionSats> getDependencies() {
        return dependencies;
    }

    public void printGraph(){
        int count=0;
        for(int i= 0; i< nodes.size();i++){
            while(true) {
            //    if (edges.get(count).split("-").length > 1) {
            //        System.out.print(edges.get(count)+" ");
            //        count++;
            //    }else{
            //        break;
            //    }
            }
            //System.out.println();
            //System.out.print(nodes.get(i)+" "+edges.get(count)+" ");
            //count ++;
        }
    }
}
