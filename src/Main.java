// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {
        try{
            PythonLexer lexer=new PythonLexer(CharStreams.fromFileName("input/input.txt"));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            PythonParser parser = new PythonParser(tokens);
            ParseTree newTree = parser.file_input();
            CodeStatisticsVisitor loader1 = new CodeStatisticsVisitor();
            loader1.visit(newTree);
            System.out.println(loader1.getFunctionsLocal());
            System.out.println(loader1.getFunctionsDependency());
            System.out.println(loader1.getExternalDependency());
            System.out.println("----------------------------");
            PythonLexer lexer2=new PythonLexer(CharStreams.fromFileName("input/input.txt"));
            CommonTokenStream tokens2 = new CommonTokenStream(lexer2);
            PythonParser parser2 = new PythonParser(tokens2);
            ExtendedVisitors visitor1 = new ExtendedVisitors();
            visitor1.setFunctions(loader1.getFunctions());
            visitor1.visit(parser2.file_input());


            Java8Lexer javaLexer=new Java8Lexer(CharStreams.fromFileName("input/input2.txt"));
            // Identificar al analizador léxico como fuente de tokens para el sintactico
            CommonTokenStream javaTokens = new CommonTokenStream(javaLexer);
            // Crear el objeto del analizador sintáctico a partir del buffer de tokens
            Java8Parser javaParser = new Java8Parser(javaTokens);
            ParseTree tree = javaParser.compilationUnit();
            CodeSatisticsVisitorJava loader = new CodeSatisticsVisitorJava();
            loader.visit(tree);
            System.out.println(loader.getFunctions());
            System.out.println(loader.getFunctionsDependency());
            System.out.println(loader.getExternalDependency());

        } catch (Exception e){
            System.err.println("Error (Test): " + e);
        }
    }

    public void getStatistics(ArrayList<FunctionSats> functions){

    }
}