import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.*;

public class GraphListenerXOXO extends PythonParserBaseListener {

    public class Node {
        private String content;

        private ArrayList<Node> next;

        public Node() {
            content = "";
            next = new ArrayList<>();
        }

        public Node(String content) {
            this.content = content;
            next = new ArrayList<>();
        }

        public ArrayList<Node> getNext() {
            return next;
        }

        public void setNext(ArrayList<Node> next) {
            this.next = next;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return this.content;
        }
    }

    private boolean connectSons = false;
    //Verdadero cuando sale de un ciclo o condicional
    private Set<Node> visited = new HashSet<>();
    //Conjunto para la implementacion del DFS

    private StringBuilder str = new StringBuilder();

    private Map<String,Node> myFunctions = new HashMap<>();
    //Grafo de funciones, key = NombreFuncion , value = grafo de la función

    private Stack<Node> currNode = new Stack<>();
    //Nodo sobre el que se está trabajando.

    private Stack<ArrayList<Node>> sons = new Stack<>();
    //Hijos que van a retornar al flujo principal


    private void DFS (Node node, String indent) {
        if (visited.contains(node)) {
            return;
        }
        //System.out.println(indent + node.toString());
        System.out.println(node.toString() + " : " + node.getNext().toString());
        visited.add(node);
        for (Node n : node.getNext()) {
            if (node.getNext().size() > 1) {
                DFS(n,indent + "\t");
            }
            else {
                DFS(n,indent);
            }
        }
    }

    public void myFunctionsPrint() {
        for (String key : myFunctions.keySet()) {
            DFS(myFunctions.get(key),"");
        }
    }

    public void myFunctionsPrint(String key) {
        DFS(myFunctions.get(key),"");
    }



    @Override
    public void enterFunction_def_raw (PythonParser.Function_def_rawContext ctx) {
        Node nNode = new Node(ctx.NAME().getText() + "(" + ctx.params().getText() + ")");
        myFunctions.put(ctx.NAME().getText(),nNode);
        currNode.push(myFunctions.get(ctx.NAME().getText()));
        sons.push(new ArrayList<>());
        sons.peek().add(nNode);
    }

    public void ConnectSons(Node nNode) {
        for (int i = 0; i < sons.peek().size(); i++) {
            sons.peek().get(i).getNext().add(nNode);
        }
        sons.pop();
        connectSons = false;
    }

    @Override
    public void exitFunction_def_raw (PythonParser.Function_def_rawContext ctx) {
        Node nNode = new Node("End");
        currNode.pop();
        ConnectSons(nNode);
    }


    @Override
    public void enterSimple_stmt (PythonParser.Simple_stmtContext ctx) {
        if (ctx.assignment() != null) {
            Node nNode = new Node(ctx.assignment().getText());
            if (connectSons) {
                ConnectSons(nNode);
            }
            else {
                currNode.peek().getNext().add(nNode);
            }
            sons.peek().set(sons.peek().size() - 1,nNode);
            if (currNode.peek().getContent().charAt(currNode.peek().getContent().length() - 1) != '?'){
                currNode.pop();
            }
            currNode.push(nNode);
        }
        else if (ctx.return_stmt() != null) {
            Node nNode = new Node("return " + ctx.return_stmt().getText().substring(6));
            if (connectSons) {
                ConnectSons(nNode);
            }
            else {
                currNode.peek().getNext().add(nNode);
            }
            sons.peek().set(sons.peek().size() - 1,nNode);
            if (currNode.peek().getContent().charAt(currNode.peek().getContent().length() - 1) != '?'){
                currNode.pop();
            }
            currNode.push(nNode);
        }
        else if (ctx.raise_stmt() != null) {
            Node nNode = new Node("raise " + ctx.raise_stmt().getText().substring(5));
            if (connectSons) {
                ConnectSons(nNode);
            }
            else {
                currNode.peek().getNext().add(nNode);
            }
            sons.peek().set(sons.peek().size() - 1,nNode);
            if (currNode.peek().getContent().charAt(currNode.peek().getContent().length() - 1) != '?'){
                currNode.pop();
            }
            currNode.push(nNode);
        }
        else if (ctx.PASS() != null) {
            Node nNode = new Node(ctx.PASS().getText());
            if (connectSons) {
                ConnectSons(nNode);
            }
            else {
                currNode.peek().getNext().add(nNode);
            }
            sons.peek().set(sons.peek().size() - 1,nNode);
            if (currNode.peek().getContent().charAt(currNode.peek().getContent().length() - 1) != '?'){
                currNode.pop();
            }
            currNode.push(nNode);
        }
        else if (ctx.BREAK() != null) {
            Node nNode = new Node(ctx.BREAK().getText());
            if (connectSons) {
                ConnectSons(nNode);
            }
            else {
                currNode.peek().getNext().add(nNode);
            }
            sons.peek().set(sons.peek().size() - 1,nNode);
            if (currNode.peek().getContent().charAt(currNode.peek().getContent().length() - 1) != '?'){
                currNode.pop();
            }
            currNode.push(nNode);
        }
        else if (ctx.CONTINUE() != null) {
            Node nNode = new Node(ctx.CONTINUE().getText());
            if (connectSons) {
                ConnectSons(nNode);
            }
            else {
                currNode.peek().getNext().add(nNode);
            }
            sons.peek().set(sons.peek().size() - 1,nNode);
            if (currNode.peek().getContent().charAt(currNode.peek().getContent().length() - 1) != '?'){
                currNode.pop();
            }
            currNode.push(nNode);

        }
        else if (ctx.del_stmt() != null) {
            Node nNode = new Node("del " + ctx.del_stmt().getText().substring(3));
            if (connectSons) {
                ConnectSons(nNode);
            }
            else {
                currNode.peek().getNext().add(nNode);
            }
            sons.peek().set(sons.peek().size() - 1,nNode);
            if (currNode.peek().getContent().charAt(currNode.peek().getContent().length() - 1) != '?'){
                currNode.pop();
            }
            currNode.push(nNode);
        }
        else if (ctx.global_stmt() != null) {
            Node nNode = new Node("global " + ctx.global_stmt().getText().substring(6));
            if (connectSons) {
                ConnectSons(nNode);
            }
            else {
                currNode.peek().getNext().add(nNode);
            }
            sons.peek().set(sons.peek().size() - 1,nNode);
            if (currNode.peek().getContent().charAt(currNode.peek().getContent().length() - 1) != '?'){
                currNode.pop();
            }
            currNode.push(nNode);
        }
        else if (ctx.nonlocal_stmt() != null) {
            Node nNode = new Node("nonlocal " + ctx.nonlocal_stmt().getText().substring(8));
            if (connectSons) {
                ConnectSons(nNode);
            }
            else {
                currNode.peek().getNext().add(nNode);
            }
            sons.peek().set(sons.peek().size() - 1,nNode);
            if (currNode.peek().getContent().charAt(currNode.peek().getContent().length() - 1) != '?'){
                currNode.pop();
            }
            currNode.push(nNode);
        }
    }

    @Override
    public void enterCompound_stmt(PythonParser.Compound_stmtContext ctx) {
        if (ctx.if_stmt() != null) {
            Node nNode = new Node(ctx.if_stmt().named_expression().getText() + " ?");
            if (connectSons) {
                ConnectSons(nNode);
            }
            sons.push(new ArrayList<>());
            sons.peek().add(nNode);
            currNode.peek().getNext().add(nNode);
            currNode.push(nNode);
        }
    }

    @Override
    public void enterElif_stmt(PythonParser.Elif_stmtContext ctx) {
        Node nNode = new Node(ctx.named_expression().getText() + " ?");
        currNode.pop();
        currNode.peek().getNext().add(nNode);
        sons.peek().add(nNode);
        currNode.push(nNode);
    }

    @Override
    public void enterElse_block (PythonParser.Else_blockContext ctx) {
        sons.peek().add(new Node("ELSE"));
        currNode.pop();
    }

    @Override
    public void exitElse_block (PythonParser.Else_blockContext ctx) {
    }

    @Override
    public void exitIf_stmt (PythonParser.If_stmtContext ctx) {
        currNode.pop();
        System.out.println("HI" + sons.peek());
        connectSons = true;
    }


    @Override
    public void exitElif_stmt (PythonParser.Elif_stmtContext ctx) {

    }



    public static void main(String[] args) throws Exception {
        PythonLexer lexer;
        if (args.length > 0 ) {
            lexer = new PythonLexer(CharStreams.fromFileName(args[0]));
        }
        else {
            lexer = new PythonLexer(CharStreams.fromStream(System.in));
        }
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PythonParser parser = new PythonParser(tokens);
        ParseTree tree = parser.file_input();
        ParseTreeWalker walker = new ParseTreeWalker();
        GraphListenerXOXO graphListener = new GraphListenerXOXO();
        walker.walk(graphListener, tree);
        graphListener.myFunctionsPrint();
        System.out.println(graphListener.myFunctions.toString());
    }
}
