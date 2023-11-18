import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.LinkedList;

public class ExtendedVisitors<T> extends  PythonParserBaseVisitor<T>{

    private HashMap<String, String> simbolTable = new HashMap<>();
    private HashMap<String, HashMap<String, String>> localTable = new HashMap<>();
    private int amountFunc = 0;
    private Queue<String> globalScope=new LinkedList<>();

    @Override
    public T visitAssignment(PythonParser.AssignmentContext ctx) {
        if(ctx.NAME()!=null){
            if(globalScope.isEmpty() || simbolTable.get(globalScope.peek())!=null){
                simbolTable.put(ctx.NAME().getText(),"Simple variable");
            }else{
                localTable.get(globalScope).put(ctx.NAME().getText(),"Simple variable");
            }
        }
        return visitChildren(ctx);
    }

    @Override public T visitFunction_def_raw(PythonParser.Function_def_rawContext ctx) {
	    simbolTable.put(ctx.NAME().getText(),"Function");
        amountFunc++;
        globalScope.add(ctx.NAME().getText());
        if(ctx.params()!=null){
            ArrayList<String> parameters = (ArrayList<String>) visitParams(ctx.params());
        }
        int length = (int)visitBlock(ctx.block());
        return visitChildren(ctx);
    }

    @Override
    public T visitParams(PythonParser.ParamsContext ctx) {
        return (T) visitParameters(ctx.parameters());
    }

    @Override
    public T visitParameters(PythonParser.ParametersContext ctx) {
        ArrayList<String> parameters = new ArrayList<>();
        if(!ctx.param_with_default().isEmpty()){
            for(PythonParser.Param_with_defaultContext var : ctx.param_with_default()){
                parameters.add((String) visitParam_with_default(var));
            }
        }
        if(!ctx.param_no_default().isEmpty()){
            for(PythonParser.Param_no_defaultContext var : ctx.param_no_default()){
                parameters.add((String) visitParam_no_default(var));
            }
        }
        return (T) parameters;
    }
    @Override
    public T visitParam_with_default(PythonParser.Param_with_defaultContext ctx) {
        return visitParam(ctx.param());
    }
    @Override
    public T visitParam(PythonParser.ParamContext ctx) {
        return (T)ctx.NAME().getText();
    }
    @Override
    public T visitBlock(PythonParser.BlockContext ctx) {
        return visitChildren(ctx);
    }


}
