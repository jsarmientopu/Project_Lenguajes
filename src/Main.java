// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Main {
    public static void main(String[] args) throws Exception {
        try {
            // crear un analizador léxico a partir de la entrada (archivo  o consola)
            PythonLexer lexer = new PythonLexer(CharStreams.fromFileName("input/input.txt"));
            // Identificar al analizador léxico como fuente de tokens para el sintactico
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            // Crear el objeto del analizador sintáctico a partir del buffer de tokens
            PythonParser parser = new PythonParser(tokens);
            //ParseTree tree = parser.statements(); // Iniciar el analisis sintáctico en la regla inicial
            //System.out.println(tree.toStringTree(parser)); // imprime el arbol al estilo LISP
            // Create a generic parse tree walker that can trigger callbacks
            //ExtendedVisitors loader = new ExtendedVisitors();
            //loader.visit(tree);
            ParseTree newTree = parser.file_input();
            CodeStatisticsVisitor loader1 = new CodeStatisticsVisitor();
            loader1.visit(newTree);
            System.out.println(loader1.getFunctions());
            System.out.println(loader1.getFunctionsDependency());
            System.out.println(loader1.getExternalDependency());
        } catch (Exception e) {
            System.err.println("Error (Test): " + e);
        }
    }
}