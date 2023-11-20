import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Function;

public class CodeStatisticsVisitor<T> extends PythonParserBaseVisitor<T> {
    private int totalLines = 0;
    private int totalFunctions = 0;
    private int totalGlobalVariables = 0;
    private int totalIfStatements = 0;
    private int totalForStatements = 0;
    private int totalWhileStatements = 0;
    private int totalVariableNameLength = 0;
    private int totalFunctionNameLength = 0;
    private int totalVariables = 0;
    private int totalFunctionsVisited = 0;
    private Set<String> globalVariables = new HashSet<>();
    private Set<String> externalDependencies = new HashSet<>();
    private Stack<String> scope = new Stack<>();
    private Map<String, Set<String>> functionDependencies = new HashMap<>();
    HashMap<String, String> simbolTable = new HashMap<>();
    private HashMap<String, HashMap<String, String>> localTable = new HashMap<>();
    private ArrayList<FunctionSats> functions=new ArrayList<>();
    private ArrayList<ClassStats> classes=new ArrayList<>();
    private double operants=0, ocurOperants=0, ocurOperators =0;
    private Set<String> operators= new HashSet<>();
    @Override
    public T visitSimple_stmt(PythonParser.Simple_stmtContext ctx) {
        // Increment the node count for each simple statement
        if(!scope.isEmpty() && localTable.get(scope.peek())!=null){
            for(FunctionSats func : functions){
                if(func.getName().equals(scope.peek())){
                    func.getNodes().add(ctx.getText());
                    //func.getEdges().add(String.valueOf(func.getWay()));
                    break;
                }
            }
        }
        return visitChildren(ctx);
    }
    @Override
    public T visitAssignment(PythonParser.AssignmentContext ctx) {
        if(ctx.NAME()!=null){
            if(scope.isEmpty() || simbolTable.get(scope.peek())!=null){
                simbolTable.put(ctx.NAME().getText(),"Simple variable");
            }else{
                localTable.get(scope.peek()).putIfAbsent(ctx.NAME().getText(), "Simple variable");
            }
            operants++;
        }
        return visitChildren(ctx);
    }
    @Override
    public T visitFile_input(PythonParser.File_inputContext ctx) {
        totalLines = ctx.getStop().getLine(); // Total number of lines in the file

        visitChildren(ctx);

        double averageVariableNameLength = calculateAverage(totalVariableNameLength, totalVariables);
        double averageFunctionNameLength = calculateAverage(totalFunctionNameLength, totalFunctionsVisited);
        System.out.println(averageFunctionNameLength);
        System.out.println(averageVariableNameLength);
        // Print the collected statistics
        System.out.println("Total Lines of Code: " + totalLines);
        System.out.println("Total Functions: " + totalFunctions);
        System.out.println("Total Global Variables: " + totalGlobalVariables);
        System.out.println("Global Variables: " + globalVariables);
        System.out.println("Total If Statements: " + totalIfStatements);
        System.out.println("Total For Statements: " + totalForStatements);
        System.out.println("Total While Statements: " + totalWhileStatements);
        System.out.println("Program length: "+ (operators.size()+operants));
        System.out.println("Program volume: "+ ((operators.size()+operants))* (Math.log(operators.size()+operants) / Math.log(2)));
        System.out.println("Program difficult: "+ ((1.0/2.0)*((operators.size()/ocurOperators)+(operants/ocurOperants))));
        System.out.println("Program effort time: "+ (((1.0/2.0)*((operators.size()/ocurOperators)+(operants/ocurOperants)))*(Math.log(operators.size()+operants) / Math.log(2))));

        //for(FunctionSats func : functions){
        //    System.out.println(func.getName());
        //    func.printGraph();
        //}

        return null;
    }

    @Override
    public T visitStar_atom(PythonParser.Star_atomContext ctx) {
        if(ctx.NAME()!=null){
            if(scope.isEmpty() || simbolTable.get(scope.peek())==null){
                simbolTable.put(ctx.NAME().getText(),"Simple variable");
            }else if(localTable.get(scope.peek())!=null){
                localTable.get(scope.peek()).putIfAbsent(ctx.NAME().getText(),"Simple variable");
            }
            totalVariableNameLength += ctx.NAME().getText().length();
            totalVariables++;
        }
        operants++;
        return visitChildren(ctx);
    }
    @Override
    public T visitAtom(PythonParser.AtomContext ctx) {
        // Check if the atom is a function call
        if (ctx.NAME()!=null) {
            String functionName = ctx.NAME().getText();
            if (!scope.isEmpty() && localTable.get(scope.peek())!=null && simbolTable.get(functionName)!=null && simbolTable.get(functionName).equals("function") && !functionName.equals(scope.peek())) {
                // Add the called function to the dependencies
                functionDependencies.get(scope.peek()).add(functionName);
            }else if(!scope.isEmpty() && localTable.get(scope.peek())!=null){
                for(String dependency: externalDependencies){
                    String val = dependency.split("\\.")[dependency.split("\\.").length-1];
                    String val2 = dependency.split("=")[dependency.split("=").length-1];
                    if(val.equals(functionName) || val2.equals(functionName)){
                        functionDependencies.get(scope.peek()).add(dependency);
                    }
                }
            }else if(!scope.isEmpty() && scope.peek().split("-").length>1){
                String var = scope.peek().split("-")[1];
                for(ClassStats clas: classes){
                    if(var.equals(clas.getName())){
                        clas.getInheritance().add(ctx.NAME().getText());
                    }
                }
            }
        }
        ocurOperants++;
        return visitChildren(ctx);
    }

    @Override
    public T visitFunction_def(PythonParser.Function_defContext ctx) {
        totalFunctions++;
        totalFunctionsVisited++;

        // Calculate the length of the function name
        String currentFunction = ctx.function_def_raw().NAME().getText();
        totalFunctionNameLength += currentFunction.length();
        if(scope.isEmpty() || localTable.get(scope.peek())==null){
            simbolTable.put(currentFunction,"function");
            localTable.put(currentFunction,new HashMap<>());
        }else{
            boolean flag = true;
            for(ClassStats clas : classes){
                if(clas.getName().equals(scope.peek())){
                    simbolTable.put(currentFunction,"function");
                    clas.getMethods().add(currentFunction);
                    flag = false;
                    break;
                }
            }
            if(flag){
                localTable.get(scope.peek()).putIfAbsent(currentFunction,"function");
            }
        }
        functions.add(new FunctionSats(currentFunction));
        scope.push(currentFunction);
        functionDependencies.put(currentFunction, new HashSet<>());
        System.out.println("Visited Function Definition: " + currentFunction);

        // You can add more logic here for function-specific statistics
        visitChildren(ctx);
        System.out.println("End Visited Function Definition: " + currentFunction);
        System.out.println("End Visited Function Definition: " + scope.peek());

        scope.pop();

        return null;
    }

    @Override
    public T visitGlobal_stmt(PythonParser.Global_stmtContext ctx) {
        totalGlobalVariables += ctx.NAME().size();
        globalVariables.addAll(getNames(ctx));

        //for (TerminalNode nameNode : ctx.NAME()) {
        //    totalVariableNameLength += nameNode.getText().length();
        //    totalVariables++;
        //}

        return null;
    }

    @Override
    public T visitDotted_as_name(PythonParser.Dotted_as_nameContext ctx) {
        String importName =visitDotted_name(ctx.dotted_name())+"="+ctx.NAME().getText();
        externalDependencies.add(importName);
        return null;
    }

    @Override
    public T visitImport_from(PythonParser.Import_fromContext ctx){
        String importName ="";
        if(ctx.dotted_name()!=null){
            importName+=visitDotted_name(ctx.dotted_name())+".";
        }
        ArrayList<String> val = (ArrayList<String>) visitImport_from_targets(ctx.import_from_targets());
        for(String key : val) {
            externalDependencies.add(importName + key);
        }
        return null;
    }

    @Override
    public T visitDotted_name(PythonParser.Dotted_nameContext ctx){
        String val = "";
        if(ctx.dotted_name()!=null) {
            val+=visitDotted_name(ctx.dotted_name());
            val+="."+ctx.NAME().getText();
        }else{
            val+=ctx.NAME().getText();
        }
        return (T) val;
    }

    @Override
    public T visitImport_from_targets(PythonParser.Import_from_targetsContext ctx){
        return visitImport_from_as_names(ctx.import_from_as_names());
    }

    @Override
    public T visitImport_from_as_names(PythonParser.Import_from_as_namesContext ctx){
        ArrayList<String> val = new ArrayList<>();
        for(PythonParser.Import_from_as_nameContext var :ctx.import_from_as_name()){
            StringBuilder x = new StringBuilder(var.NAME(0).getText());
            try {
                if(var.NAME(1)!=null) {
                    x.append("=").append(var.NAME(1));
                }
            }catch (IndexOutOfBoundsException ignored){
            }
            val.add(x.toString());
        }
        return (T) val;
    }

    @Override
    public T visitIf_stmt(PythonParser.If_stmtContext ctx) {
        totalIfStatements++;
        int prev=0;
        FunctionSats x = new FunctionSats("");
        if(!scope.isEmpty() && localTable.get(scope.peek())!=null){
            for(FunctionSats func: functions){
                if(func.getName().equals(scope.peek())){
                    func.setEdges(func.getEdges()+1);
                    break;
                }
            }
        }
        visitChildren(ctx);
        // You can add more logic here for if statement-specific statistics

        return null;
    }

    @Override
    public T visitElif_stmt(PythonParser.Elif_stmtContext ctx) {
        int prev=0;
        FunctionSats x = new FunctionSats("");
        if(!scope.isEmpty() && localTable.get(scope.peek())!=null){
            for(FunctionSats func: functions){
                if(func.getName().equals(scope.peek())){
                    func.setEdges(func.getEdges()+1);
                    break;
                }
            }
        }
        visitChildren(ctx);
        // You can add more logic here for if statement-specific statistics

        return null;
    }
    @Override
    public T visitElse_block(PythonParser.Else_blockContext ctx) {
        int prev=0;
        FunctionSats x = new FunctionSats("");
        if(!scope.isEmpty() && localTable.get(scope.peek())!=null){
            for(FunctionSats func: functions){
                if(func.getName().equals(scope.peek())){
                    func.setEdges(func.getEdges()+1);
                    break;
                }
            }
        }
        visitChildren(ctx);
        // You can add more logic here for if statement-specific statistics

        return null;
    }

    @Override
    public T visitFor_stmt(PythonParser.For_stmtContext ctx) {
        totalForStatements++;
        visitChildren(ctx);

        // You can add more logic here for statement-specific statistics

        return null;
    }

    @Override
    public T visitWhile_stmt(PythonParser.While_stmtContext ctx) {
        totalWhileStatements++;
        visitChildren(ctx);

        // You can add more logic here for while statement-specific statistics

        return null;
    }

    @Override
    public T visitEq_bitwise_or(PythonParser.Eq_bitwise_orContext ctx) {operators.add("=="); ocurOperators++; return visitChildren(ctx); }
    @Override public T visitNoteq_bitwise_or(PythonParser.Noteq_bitwise_orContext ctx) { operators.add("!="); ocurOperators++; return visitChildren(ctx); }
    @Override public T visitLte_bitwise_or(PythonParser.Lte_bitwise_orContext ctx) { operators.add("<="); ocurOperators++; return visitChildren(ctx); }
    @Override public T visitLt_bitwise_or(PythonParser.Lt_bitwise_orContext ctx) { operators.add("<"); ocurOperators++; return visitChildren(ctx); }
    @Override public T visitGte_bitwise_or(PythonParser.Gte_bitwise_orContext ctx) { operators.add(">="); ocurOperators++; return visitChildren(ctx); }
    @Override public T visitGt_bitwise_or(PythonParser.Gt_bitwise_orContext ctx) { operators.add(">"); ocurOperators++; return visitChildren(ctx); }
    @Override public T visitNotin_bitwise_or(PythonParser.Notin_bitwise_orContext ctx) { operators.add("not in"); ocurOperators++; return visitChildren(ctx); }
    @Override public T visitIn_bitwise_or(PythonParser.In_bitwise_orContext ctx) { operators.add("in"); ocurOperators++; return visitChildren(ctx); }
    @Override public T visitIsnot_bitwise_or(PythonParser.Isnot_bitwise_orContext ctx) { operators.add("is not"); ocurOperators++; return visitChildren(ctx); }
    @Override public T visitIs_bitwise_or(PythonParser.Is_bitwise_orContext ctx) { operators.add("is"); ocurOperators++; return visitChildren(ctx); }
    @Override public T visitSum(PythonParser.SumContext ctx) {
        if(ctx.sum()!=null){
            if(ctx.getText().contains("+")){
                operators.add("+");
            }else{
                operators.add("-");
            }
            ocurOperators++;
        }
        return visitChildren(ctx);
    }
    @Override public T visitTerm(PythonParser.TermContext ctx) {
        if(ctx.term()!=null){
            if(ctx.getText().contains("*")){
                operators.add("*");
            }else if(ctx.getText().contains("/")){
                operators.add("/");
            }else if(ctx.getText().contains("//")){
                operators.add("//");
            }else if(ctx.getText().contains("%")){
                operators.add("%");
            }else{
                operators.add("@");
            }
            ocurOperators++;
        }
        return visitChildren(ctx);
    }
    @Override public T visitFactor(PythonParser.FactorContext ctx) {
        if(ctx.factor()!=null){
            if(ctx.getText().contains("+")){
                operators.add("+");
            }else if(ctx.getText().contains("-")){
                operators.add("-");
            }else{
                operators.add("~");
            }
            ocurOperators++;
        }
        return visitChildren(ctx);
    }
    @Override public T visitPower(PythonParser.PowerContext ctx) {
        if(ctx.factor()!=null){
            operators.add("**");
            ocurOperators++;
        }
        return visitChildren(ctx);
    }

    @Override
    public T visitClass_def_raw(PythonParser.Class_def_rawContext ctx) {
        ClassStats newClass = new ClassStats(ctx.NAME().getText());
        scope.push(ctx.NAME().getText());
        System.out.println("Class Definition: " + ctx.NAME().getText());
        visitChildren(ctx);
        System.out.println("VisitedClass Definition: " + scope.peek());

        scope.pop();
        return null;
    }

    @Override
    public T visitArguments(PythonParser.ArgumentsContext ctx) {
        boolean flag = false;
        if(!scope.isEmpty()){
            for(ClassStats clas: classes){
                if(clas.getName().equals(scope.peek())){
                    scope.push(scope.peek()+"-arguments");
                    flag = true;
                    break;
                }
            }
        }
        visitChildren(ctx);
        if(flag) {
            scope.pop();
        }
        return null;
    }


    // Helper method to calculate the average
    private double calculateAverage(int totalLength, int totalCount) {
        return totalCount > 0 ? ((double) totalLength) / totalCount : 0;
    }

    // Helper method to get the names from a context
    private Set<String> getNames(ParseTree ctx) {
        Set<String> names = new HashSet<>();
        for (int i = 0; i < ctx.getChildCount(); i++) {
            ParseTree child = ctx.getChild(i);
            if (child instanceof TerminalNode) {
                TerminalNode terminalNode = (TerminalNode) child;
                if (terminalNode.getSymbol().getType() == PythonParser.NAME) {
                    names.add(terminalNode.getText());
                }
            } else {
                names.addAll(getNames(child));
            }
        }
        return names;
    }

    public String getFunctions() {
        System.out.println(functions.size());
        StringBuilder func= new StringBuilder("Variables locales\n");
        for(final String key : localTable.keySet()){
            func.append(key+"\n");
            for(final String key1: localTable.get(key).keySet()){
                func.append("\t"+key1+" -> "+localTable.get(key).get(key1)+"\n");
            }
        }
        return func.toString();
    }

    public String getFunctionsDependency() {
        StringBuilder func= new StringBuilder("Llamados externos\n");
        for(final String key : functionDependencies.keySet()){
            func.append(key+"\n");
            for(final String key1: functionDependencies.get(key)){
                func.append("\t"+key1+"\n");
            }
        }
        return func.toString();
    }

    public String getExternalDependency() {
        StringBuilder func= new StringBuilder("Modulos externos\n");
        for(final String key : externalDependencies){
            func.append(key+"\n");
        }
        return func.toString();
    }
}