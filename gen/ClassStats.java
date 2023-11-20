import java.util.ArrayList;

public class ClassStats {

    private String name;

    private ArrayList<String> inheritance;

    private ArrayList<String> methods;

    public ClassStats(String name){
        this.name=name;
        this.inheritance=new ArrayList<>();
        this.methods=new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getInheritance() {
        return inheritance;
    }

    public ArrayList<String> getMethods() {
        return methods;
    }
}
