import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class ExtendedListeners extends PythonParserBaseListener{

    //All the variables
    // {Name,Type(Var, Func, I/O)}
    HashMap<String, String> simbolTable = new HashMap<>();

    private HashMap<String, HashMap<String, String>> localTable = new HashMap<>();
    private int amountFunc = 0;
    private Queue<String> globalScope=new LinkedList<>();

    private int statistic=0;

    //{Name, [Amount, ]}
    HashMap<String, ArrayList<Integer>> statisticsTable = new HashMap<>();

    @Override
    public void enterAssignment(PythonParser.AssignmentContext ctx) {
        if(ctx.NAME()!=null){
            if(globalScope.isEmpty() || simbolTable.get(globalScope.peek())!=null){
                simbolTable.put(ctx.NAME().getText(),"Simple variable");
            }else{
                localTable.get(globalScope.peek()).putIfAbsent(ctx.NAME().getText(), "Simple variable");
            }
        }
        System.out.println("Enter to assign");
    }

    @Override
    public void enterStar_atom(PythonParser.Star_atomContext ctx){
        if(ctx.NAME()!=null){
            if(globalScope.isEmpty() || simbolTable.get(globalScope.peek())!=null){
                simbolTable.put(ctx.NAME().getText(),"Simple variable");
            }else{
                localTable.get(globalScope.peek()).putIfAbsent(ctx.NAME().getText(),"Simple variable");
            }
        }
    }

    @Override
    public void enterSingle_target(PythonParser.Single_targetContext ctx){
        System.out.println("Loko");
    }

    @Override
    public void enterFunction_def_raw(PythonParser.Function_def_rawContext ctx) {
        simbolTable.put(ctx.NAME().getText(),"Function");
        globalScope.add(ctx.NAME().getText());
    }
    @Override
    public void exitFunction_def_raw(PythonParser.Function_def_rawContext ctx) {
        globalScope.poll();
    }

    @Override
    public void enterIf_stmt(PythonParser.If_stmtContext ctx) {
        globalScope.add("if");
    }
    @Override
    public void exitIf_stmt(PythonParser.If_stmtContext ctx) {
        globalScope.poll();
    }

    @Override
    public void enterWhile_stmt(PythonParser.While_stmtContext ctx) {
        globalScope.add("while");
    }
    @Override
    public void exitWhile_stmt(PythonParser.While_stmtContext ctx) {
        globalScope.poll();
    }

    @Override
    public void enterFor_stmt(PythonParser.For_stmtContext ctx) {
        globalScope.add("for");
    }
    @Override
    public void exitFor_stmt(PythonParser.For_stmtContext ctx) {
        globalScope.poll();
    }

    @Override
    public void enterWith_stmt(PythonParser.With_stmtContext ctx) {
        globalScope.add("with");
    }
    @Override
    public void exitWith_stmt(PythonParser.With_stmtContext ctx) {
        globalScope.poll();
    }




}
