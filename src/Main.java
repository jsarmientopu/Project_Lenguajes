// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.io.File;

public class Main {
    public static void main(String[] args) throws Exception {
        try{
            // crear un analizador léxico a partir de la entrada (archivo  o consola)
            PythonLexer lexer;
            if (args.length>0)
                lexer = new PythonLexer(CharStreams.fromFileName(args[0]));
            else
                lexer = new PythonLexer(CharStreams.fromStream(System.in));
            // Identificar al analizador léxico como fuente de tokens para el sintactico
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            // Crear el objeto del analizador sintáctico a partir del buffer de tokens
            PythonParser parser = new PythonParser(tokens);
            ParseTree tree = parser.statements(); // Iniciar el analisis sintáctico en la regla inicial: r
            System.out.println(tree.toStringTree(parser)); // imprime el arbol al estilo LISP
        } catch (Exception e){
            System.err.println("Error (Test): " + e);
        }
    }
}