import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

public class ExtraStatisticsVisitor<T> extends PythonParserBaseVisitor<T> {

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
    private Queue<String> scope = new LinkedList<>();
    private Map<String, Set<String>> functionDependencies = new HashMap<>();
    HashMap<String, String> simbolTable = new HashMap<>();
    private HashMap<String, HashMap<String, String>> localTable = new HashMap<>();

    private Map<String, List<Integer>> variableCounter = new HashMap<>();

    @Override
    public T visitAssignment(PythonParser.AssignmentContext ctx) {
        if(ctx.NAME()!=null){
            //System.out.println("Assignment "+ ctx.NAME().getText());
            if(scope.isEmpty() || simbolTable.get(scope.peek())!=null){
                simbolTable.put(ctx.NAME().getText(),"Simple variable");
            }else{
                localTable.get(scope.peek()).putIfAbsent(ctx.NAME().getText(), "Simple variable");
            }
        }
        return visitChildren(ctx);
    }
    @Override
    public T visitFile_input(PythonParser.File_inputContext ctx) {
        totalLines = ctx.getStop().getLine(); // Total number of lines in the file

        // Visit each statement in the file
        //for (PythonParser.StatementContext statementContext : ctx.statements().statement()) {
        //    System.out.println("Visiting statement: " + statementContext.getText());
        //    visit(statementContext);
        //}

        visitChildren(ctx);

        double averageVariableNameLength = calculateAverage(totalVariableNameLength, totalVariables);
        double averageFunctionNameLength = calculateAverage(totalFunctionNameLength, totalFunctionsVisited);
        /*System.out.println(averageFunctionNameLength);
        System.out.println(averageVariableNameLength);
        // Print the collected statistics
        System.out.println("Total Lines of Code: " + totalLines);
        System.out.println("Total Functions: " + totalFunctions);
        System.out.println("Total Global Variables: " + totalGlobalVariables);
        System.out.println("Global Variables: " + globalVariables);
        System.out.println("Total If Statements: " + totalIfStatements);
        System.out.println("Total For Statements: " + totalForStatements);
        System.out.println("Total While Statements: " + totalWhileStatements);
        System.out.println("Total variables: "+ totalVariables);*/

        return null;
    }

    @Override
    public T visitStar_atom(PythonParser.Star_atomContext ctx) {
        if(ctx.NAME()!=null){
            boolean exists = false;
            //System.out.println("Variable: "+ctx.NAME().getText());
            if(scope.isEmpty() || simbolTable.get(scope.peek())==null){

                if(simbolTable.get(ctx.NAME().getText()) == null){
                    //System.out.println("En symbol table");
                    simbolTable.put(ctx.NAME().getText(),"Simple variable");
                }else{
                    exists = true;
                }

            }else if(localTable.get(scope.peek())!=null){
                if(localTable.get(ctx.NAME().getText()) == null){
                    //System.out.println("En local table");
                    localTable.get(scope.peek()).putIfAbsent(ctx.NAME().getText(),"Simple variable");
                }else{
                    exists = true;
                }

            }
            if(!exists){
                List<Integer> aux = new ArrayList<>();
                aux.add(1);
                aux.add(ctx.getStop().getLine());

                variableCounter.put(ctx.NAME().getText(), aux);
                totalVariableNameLength += ctx.NAME().getText().length();
                totalVariables++;
            }else{
                List<Integer> modified = variableCounter.get(ctx.NAME().getText());
                modified.set(0,modified.get(0)+1);
                variableCounter.put(ctx.NAME().getText(), modified);
            }


        }
        return visitChildren(ctx);
    }

    @Override
    public T visitAtom(PythonParser.AtomContext ctx) {
        // Check if the atom is a function call
        if (ctx.NAME()!=null) {
            String functionName = ctx.NAME().getText();
            //is a function call
            if (!scope.isEmpty() && localTable.get(scope.peek())!=null && simbolTable.get(functionName)!=null && simbolTable.get(functionName).equals("function") && !functionName.equals(scope.peek())) {
                // Add the called function to the dependencies
                functionDependencies.get(scope.peek()).add(functionName);
            }else if(variableCounter.containsKey(functionName)){
                // check if used as argument
                // helps in checking unused of variables
                List<Integer> modified = variableCounter.get(ctx.NAME().getText());
                modified.set(0,modified.get(0)+1);
                variableCounter.put(ctx.NAME().getText(), modified);
            }else if(!scope.isEmpty() && localTable.get(scope.peek())!=null){
                //Check use of imports
                for(String dependency: externalDependencies){
                    String val = dependency.split("\\.")[dependency.split("\\.").length-1];
                    String val2 = dependency.split("=")[dependency.split("=").length-1];
                    if(val.equals(functionName) || val2.equals(functionName)){
                        functionDependencies.get(scope.peek()).add(dependency);
                    }
                }



            }
        }

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
            localTable.get(scope.peek()).putIfAbsent(currentFunction,"function");
        }
        scope.add(currentFunction);
        functionDependencies.put(currentFunction, new HashSet<>());
        //System.out.println("Visited Function Definition: " + currentFunction);

        // You can add more logic here for function-specific statistics
        visitChildren(ctx);

        scope.poll();

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
        scope.add("if");
        visitChildren(ctx);
        scope.poll();
        // You can add more logic here for if statement-specific statistics

        return null;
    }

    @Override
    public T visitFor_stmt(PythonParser.For_stmtContext ctx) {
        totalForStatements++;
        scope.add("for");
        visitChildren(ctx);
        scope.poll();

        // You can add more logic here for statement-specific statistics

        return null;
    }

    @Override
    public T visitWhile_stmt(PythonParser.While_stmtContext ctx) {
        totalWhileStatements++;
        scope.add("while");
        visitChildren(ctx);
        scope.poll();

        // You can add more logic here for while statement-specific statistics

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

    public void getUnusedVariables(){
        /*
        * Function that builds an ordered map (by line number) with the variables that
        * where used only one time (when declared), so its value is never used after in the
        * program's logic.
        *
        * */
        Map<String, Integer> unused = new HashMap<>();

        //get map from variableCounter
        for(String key: variableCounter.keySet()){
            if(variableCounter.get(key).get(0) == 1){
                unused.put(key, variableCounter.get(key).get(1) );
            }
        }

        //order map
        Map<String, Integer> ordered = unused.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue,
                    (e1, e2) -> e1, // Si hay duplicados, mantener el valor existente
                    LinkedHashMap::new // Mantener el orden de inserción
                ));


        if(!ordered.isEmpty()) printUnused(ordered);
    }

    private void printUnused(Map<String, Integer> unused){
        System.out.println("The following variables are not being used after declaration:");
        for(String key: unused.keySet()){
            System.out.println("Variable "+key+" declared in line "+ unused.get(key));
        }
    }
}
