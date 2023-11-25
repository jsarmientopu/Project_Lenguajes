// Generated from C:/Users/Sebastian Sarmiento/IdeaProjects/Len_Poriect/grammar/PythonParser.g4 by ANTLR 4.13.1
import org.antlr.v4.runtime.tree.AbstractParseTreeVisitor;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.apache.commons.text.similarity.CosineSimilarity;
import org.apache.commons.text.similarity.JaccardSimilarity;

import java.util.ArrayList;
import java.util.Stack;

/**
 * This class provides an empty implementation of {@link PythonParserVisitor},
 * which can be extended to create a visitor which only needs to handle a subset
 * of the available methods.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
@SuppressWarnings("CheckReturnValue")
public class ExtendedVisitors<T> extends PythonParserBaseVisitor<T> implements PythonParserVisitor<T> {
    private ArrayList<FunctionSats> functions;
    private StringBuffer code;
    private Stack<FunctionSats> scope = new Stack<>();
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitFile_input(PythonParser.File_inputContext ctx) {
        visitChildren(ctx);
        getDuplicityInd();
        return null;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitInteractive(PythonParser.InteractiveContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitEval(PythonParser.EvalContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitFunc_type(PythonParser.Func_typeContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitFstring_input(PythonParser.Fstring_inputContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitStatements(PythonParser.StatementsContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        for(PythonParser.StatementContext x: ctx.statement()){
            codeAux.append(((String) visitStatement(x)));
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitStatement(PythonParser.StatementContext ctx) {;
        return (T) (((String) visitChildren(ctx)));
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitStatement_newline(PythonParser.Statement_newlineContext ctx) {
        if(ctx.compound_stmt()!=null){
            return (T) (visitCompound_stmt(ctx.compound_stmt())+"\n");
        }else if(ctx.NEWLINE()!=null){
            return null;
        }
        return visitChildren(ctx);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitSimple_stmts(PythonParser.Simple_stmtsContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        for(PythonParser.Simple_stmtContext x : ctx.simple_stmt()){
            codeAux.append(((String)visitSimple_stmt(x))+"\n");
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitSimple_stmt(PythonParser.Simple_stmtContext ctx) {
        if(ctx.getText().contains("pass")){
            return (T) "pass";
        }else if(ctx.getText().contains("break")){
            return (T) "break";
        }else if(ctx.getText().contains("continue")){
            return (T) "continue";
        }
        return visitChildren(ctx);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitCompound_stmt(PythonParser.Compound_stmtContext ctx) {
        return (T) (visitChildren(ctx));
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitAssignment(PythonParser.AssignmentContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        if(ctx.NAME()!=null) {
            codeAux.append(((String) ctx.NAME().getText()) + " : " + ((String) visitExpression((ctx.expression()))) + " ");
            if (ctx.annotated_rhs() != null) {
                codeAux.append( "= " + ((String) visitAnnotated_rhs(ctx.annotated_rhs())));
            }
        }else if(ctx.expression()!=null){
            if (ctx.single_target() != null) {
                codeAux.append("( "+((String) visitSingle_target(ctx.single_target()))+" ) ");
            }else{
                codeAux.append((String) visitSingle_subscript_attribute_target(ctx.single_subscript_attribute_target()));
            }
            codeAux.append(" : "+((String) visitExpression(ctx.expression()))+" ");
            if (ctx.annotated_rhs() != null) {
                codeAux.append( "= " + ((String) visitAnnotated_rhs(ctx.annotated_rhs())));
            }
        }else if(ctx.augassign()!=null){
            codeAux.append(((String) visitSingle_target(ctx.single_target()))+" "+((String) visitAugassign(ctx.augassign()))+" ");
            if(ctx.yield_expr()!=null){
                codeAux.append(((String) visitYield_expr(ctx.yield_expr()))+" ");
            }else{
                codeAux.append(((String) visitStar_expressions(ctx.star_expressions()))+" ");
            }
        }else{
            for(PythonParser.Star_targetsContext x : ctx.star_targets()){
                codeAux.append(((String) visitStar_targets(x))+" = ");
            }
            if(ctx.yield_expr()!=null){
                codeAux.append(((String) visitYield_expr(ctx.yield_expr()))+" ");
            }else{
                codeAux.append(((String) visitStar_expressions(ctx.star_expressions()))+" ");
            }
            if(ctx.TYPE_COMMENT()!=null){
                codeAux.append(ctx.TYPE_COMMENT().getText());
            }
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitAnnotated_rhs(PythonParser.Annotated_rhsContext ctx) {
        if(ctx.yield_expr()!=null){
           return (T) (visitYield_expr(ctx.yield_expr())+" ");
        }else{
            return (T) (visitStar_expressions(ctx.star_expressions())+" ");
        }
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitAugassign(PythonParser.AugassignContext ctx) {
        visitChildren(ctx);
        return (T) ctx.getText();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitReturn_stmt(PythonParser.Return_stmtContext ctx) {
        if(ctx.star_expressions()==null){
            return (T) ("retun ");
        }
        return (T) ("return "+visitStar_expressions(ctx.star_expressions())+" ");
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitRaise_stmt(PythonParser.Raise_stmtContext ctx) {
        if(ctx.expression().isEmpty()){
            return (T) ("raise ");
        }
        StringBuffer codeAux = new StringBuffer();
        codeAux.append("raise "+((String) visitExpression(ctx.expression(0)))+" ");
        if(ctx.expression().size()>1){
            codeAux.append("from "+((String) visitExpression(ctx.expression(1)))+" ");
        }
        return (T) (codeAux.toString());
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitGlobal_stmt(PythonParser.Global_stmtContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append("global "+ctx.NAME().get(0).getText()+" ");
        if(ctx.NAME().size()>1){
            for(int i=1;i<ctx.NAME().size();i++){
                codeAux.append(", " + ctx.NAME(i).getText());
            }
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitNonlocal_stmt(PythonParser.Nonlocal_stmtContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append((String) "nonlocal "+ctx.NAME().get(0).getText()+" ");
        if(ctx.NAME().size()>1){
            for(int i=1;i<ctx.NAME().size();i++){
                codeAux.append(", " + ctx.NAME(i).getText());
            }
        }
        return (T) codeAux.toString();}
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitDel_stmt(PythonParser.Del_stmtContext ctx) {
        return (T) ("del "+visitDel_targets(ctx.del_targets())+" ");
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitYield_stmt(PythonParser.Yield_stmtContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitAssert_stmt(PythonParser.Assert_stmtContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append("assert  "+((String) visitExpression(ctx.expression(0)))+" ");
        if(ctx.expression().size()>1){
            for(int i =1; i<ctx.expression().size();i++){
                codeAux.append(", " + ((String) visitExpression(ctx.expression(i))));
            }
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitImport_stmt(PythonParser.Import_stmtContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitImport_name(PythonParser.Import_nameContext ctx) { return (T) ("import " +visitChildren(ctx)); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitImport_from(PythonParser.Import_fromContext ctx) {
        visitChildren(ctx);
        return (T) ctx.getText();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitImport_from_targets(PythonParser.Import_from_targetsContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitImport_from_as_names(PythonParser.Import_from_as_namesContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitImport_from_as_name(PythonParser.Import_from_as_nameContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitDotted_as_names(PythonParser.Dotted_as_namesContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitDotted_as_name(PythonParser.Dotted_as_nameContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitDotted_name(PythonParser.Dotted_nameContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitBlock(PythonParser.BlockContext ctx) {
        if(ctx.simple_stmts()==null){
            return (T) ("\n"+((String) visitStatements(ctx.statements())));
        }
        return visitChildren(ctx);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitDecorators(PythonParser.DecoratorsContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        for(PythonParser.Named_expressionContext x : ctx.named_expression()){
            codeAux.append("@ "+((String) visitNamed_expression(x))+"\n");
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitClass_def(PythonParser.Class_defContext ctx) {
        if(ctx.decorators()!=null){
            return (T) (visitDecorators(ctx.decorators())+" "+visitClass_def_raw(ctx.class_def_raw()));
        }
        return visitChildren(ctx);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitClass_def_raw(PythonParser.Class_def_rawContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append("class "+ ctx.NAME().getText()+" ");
        if(ctx.type_params()!=null){
            codeAux.append(((String) visitType_params(ctx.type_params()))+" ");
        }
        if(ctx.arguments()!=null){
            codeAux.append(" ( "+((String) visitArguments(ctx.arguments()))+" ) ");
        }
        codeAux.append(" : "+((String) visitBlock(ctx.block()))+" ");
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitFunction_def(PythonParser.Function_defContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        String currentName = ctx.function_def_raw().NAME().getText();
        for(FunctionSats func : functions) {
            if (func.getName().equals(currentName)){
                scope.push(func);
                break;
            }
        }
        if(ctx.decorators()!=null){
            codeAux.append((String) visitDecorators(ctx.decorators())+" ");
        }
        codeAux.append((String) visitFunction_def_raw(ctx.function_def_raw())+" ");
        scope.peek().getRawCode().put(codeAux.toString(),"complete");
        scope.pop();
        return null;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitFunction_def_raw(PythonParser.Function_def_rawContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        if(ctx.ASYNC()!=null){
            codeAux.append(ctx.ASYNC().getText()+" ");
        }
        codeAux.append("def "+ctx.NAME().getText()+" ");
        if(ctx.type_params()!=null){
            codeAux.append((String) visitType_params(ctx.type_params()));
        }
        codeAux.append("( ");
        if(ctx.params()!=null){
            codeAux.append((String) visitParams(ctx.params()));
        }
        codeAux.append(") ");
        if(ctx.expression()!=null){
            codeAux.append("-> "+((String) visitParams(ctx.params())));
        }
        codeAux.append(": ");
        if(ctx.func_type_comment()!=null){
            codeAux.append("-> "+((String) visitFunc_type_comment(ctx.func_type_comment())));
        }
        codeAux.append((String) visitBlock(ctx.block()));
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitParams(PythonParser.ParamsContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitParameters(PythonParser.ParametersContext ctx) {
        visitChildren(ctx);
        return (T) (ctx.getText());
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitSlash_no_default(PythonParser.Slash_no_defaultContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitSlash_with_default(PythonParser.Slash_with_defaultContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitStar_etc(PythonParser.Star_etcContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitKwds(PythonParser.KwdsContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitParam_no_default(PythonParser.Param_no_defaultContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitParam_no_default_star_annotation(PythonParser.Param_no_default_star_annotationContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitParam_with_default(PythonParser.Param_with_defaultContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitParam_maybe_default(PythonParser.Param_maybe_defaultContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitParam(PythonParser.ParamContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append(ctx.NAME().getText()+" ");
        if(ctx.annotation()!=null){
            codeAux.append(((String) visitAnnotation(ctx.annotation()))+" ");
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitParam_star_annotation(PythonParser.Param_star_annotationContext ctx) { return (T) (ctx.NAME().getText()+" "+visitChildren(ctx)); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitAnnotation(PythonParser.AnnotationContext ctx) { return (T) (": "+visitChildren(ctx)); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitStar_annotation(PythonParser.Star_annotationContext ctx) { return (T) (": "+visitChildren(ctx)); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitDefault_assignment(PythonParser.Default_assignmentContext ctx) { return (T) ("= "+visitChildren(ctx)); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitIf_stmt(PythonParser.If_stmtContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append("if "+((String) visitNamed_expression(ctx.named_expression()))+" : "+((String) visitBlock(ctx.block()))+" ");
        if(ctx.elif_stmt()!=null){
            codeAux.append((String) visitElif_stmt(ctx.elif_stmt()));
        }else if(ctx.else_block()!=null){
            codeAux.append((String) visitElse_block(ctx.else_block()));
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitElif_stmt(PythonParser.Elif_stmtContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append("if "+((String) visitNamed_expression(ctx.named_expression()))+" : "+((String) visitBlock(ctx.block()))+" ");
        if(ctx.elif_stmt()!=null){
            codeAux.append((String) visitElif_stmt(ctx.elif_stmt()));
        }else if(ctx.else_block()!=null){
            codeAux.append((String) visitElse_block(ctx.else_block()));
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitElse_block(PythonParser.Else_blockContext ctx) {
        return (T) ("else : "+((String) visitBlock(ctx.block())));
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitWhile_stmt(PythonParser.While_stmtContext ctx) {
        StringBuffer currentCode = new StringBuffer();
        currentCode.append("while "+((String) visitNamed_expression(ctx.named_expression()))+" : "+((String) visitBlock(ctx.block()))+" ");
        if(ctx.else_block()!=null){
            currentCode.append((String) visitElse_block(ctx.else_block()));
        }
        if(!scope.isEmpty()){
            scope.peek().getRawCode().put(currentCode.toString(), "while");
        }
        return (T) currentCode.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitFor_stmt(PythonParser.For_stmtContext ctx) {
        StringBuffer currentCode = new StringBuffer();
        if(ctx.ASYNC()!=null){
            currentCode.append(ctx.ASYNC().getText()+" ");
        }
        currentCode.append("for "+((String) visitStar_targets(ctx.star_targets()))+" in "+((String) visitStar_expressions(ctx.star_expressions()))+" : ");
        if(ctx.TYPE_COMMENT()!=null){
            currentCode.append(ctx.TYPE_COMMENT().getText()+" ");
        }
        currentCode.append((String) visitBlock(ctx.block()));
        if(ctx.else_block()!=null){
            currentCode.append(((String) visitElse_block(ctx.else_block()))+" ");
        }
        if(!scope.isEmpty()){
            scope.peek().getRawCode().put(currentCode.toString(), "for");
        }
        //System.out.println(currentCode.toString());
        return (T) currentCode.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitWith_stmt(PythonParser.With_stmtContext ctx) {
        StringBuffer currentCode = new StringBuffer();
        if(ctx.ASYNC()!=null){
            currentCode.append(ctx.ASYNC().getText()+" ");
        }
        currentCode.append("with ");
        if(ctx.getText().contains("(")){
            currentCode.append("( ");
            for(PythonParser.With_itemContext x :ctx.with_item()){
                currentCode.append(((String) visitWith_item(x))+" , ");
            }
            currentCode.append(" ) : ");
        }else{
            for(PythonParser.With_itemContext x :ctx.with_item()){
                currentCode.append(((String) visitWith_item(x))+" , ");
            }
            if(!ctx.with_item().isEmpty()){
                currentCode.delete(currentCode.length()-3 ,currentCode.length());
            }
            if(ctx.TYPE_COMMENT()!=null){
                currentCode.append(ctx.TYPE_COMMENT().getText()+" ");
            }
        }
        currentCode.append((String) visitBlock(ctx.block()));
        if(!scope.isEmpty()){
            scope.peek().getRawCode().put(currentCode.toString(), "for");
        }
        return (T) currentCode.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitWith_item(PythonParser.With_itemContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append(((String) visitExpression(ctx.expression()))+" ");
        if(ctx.star_target()!=null){
            codeAux.append("as "+((String) visitStar_target(ctx.star_target()))+" ");
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitTry_stmt(PythonParser.Try_stmtContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append("try : "+((String) visitBlock(ctx.block())));
        if(ctx.finally_block()!=null){
            codeAux.append((String) visitFinally_block(ctx.finally_block()));
        }else {
            if(!ctx.except_block().isEmpty()){
                for(PythonParser.Except_blockContext x : ctx.except_block()){
                    codeAux.append(((String) visitExcept_block(x))+" ");
                }
            }else{
                for(PythonParser.Except_star_blockContext x : ctx.except_star_block()){
                    codeAux.append(((String) visitExcept_star_block(x))+" ");
                }
            }
            if(ctx.else_block()!=null){
                codeAux.append(((String) visitElse_block(ctx.else_block()))+" ");
            }
            if(ctx.finally_block()!=null){
                codeAux.append(((String) visitFinally_block(ctx.finally_block()))+" ");
            }
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitExcept_block(PythonParser.Except_blockContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append("except ");
        if(ctx.expression()!=null){
            codeAux.append(((String) visitExpression(ctx.expression()))+" ");
            if(ctx.NAME()!=null){
                codeAux.append("as "+ctx.NAME().getText()+" ");
            }
        }
        codeAux.append(" : "+((String) visitBlock(ctx.block())));
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitExcept_star_block(PythonParser.Except_star_blockContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append("except "+((String) visitExpression(ctx.expression()))+" ");
        if(ctx.NAME()!=null){
            codeAux.append("as "+ctx.NAME().getText()+" ");
        }
        codeAux.append( " : "+((String) visitBlock(ctx.block())));
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitFinally_block(PythonParser.Finally_blockContext ctx) {
        return (T) ("finally : "+((String) visitBlock(ctx.block())));
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitMatch_stmt(PythonParser.Match_stmtContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append(((String) visitSoft_kw_match(ctx.soft_kw_match()))+" "+((String) visitSubject_expr(ctx.subject_expr()))+" : \n");
        for(PythonParser.Case_blockContext x : ctx.case_block()){
            codeAux.append(((String) visitCase_block(x))+" ");
        }
        if(!scope.isEmpty()){
            scope.peek().getRawCode().put(codeAux.toString(),"match");
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitSubject_expr(PythonParser.Subject_exprContext ctx) {
        if(ctx.named_expression()==null){
            StringBuffer codeAux = new StringBuffer();
            codeAux.append(((String) visitStar_named_expression(ctx.star_named_expression()))+" , ");
            if(ctx.star_named_expressions()!=null){
                codeAux.append(((String) visitStar_named_expressions(ctx.star_named_expressions()))+" ");
            }
            return (T) codeAux.toString();
        }
        return visitNamed_expression(ctx.named_expression());
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitCase_block(PythonParser.Case_blockContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append(((String) visitSoft_kw_case(ctx.soft_kw_case()))+" ");
        if(ctx.guard()!=null){
            codeAux.append(((String) visitGuard(ctx.guard()))+" ");
        }
        codeAux.append(" : "+((String) visitBlock(ctx.block())));
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitGuard(PythonParser.GuardContext ctx) {
        return (T) ("if "+((String) visitNamed_expression(ctx.named_expression()))+" ");
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitPatterns(PythonParser.PatternsContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitPattern(PythonParser.PatternContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitAs_pattern(PythonParser.As_patternContext ctx) {
        return (T) (((String) visitOr_pattern(ctx.or_pattern()))+" as "+((String) visitPattern_capture_target(ctx.pattern_capture_target()))+" ");
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitOr_pattern(PythonParser.Or_patternContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append((String) visitClosed_pattern(ctx.closed_pattern(0)));
        if(ctx.closed_pattern().size()>1){
            for(int i = 1; i<ctx.closed_pattern().size();i++){
                codeAux.append( " | "+((String) visitClosed_pattern(ctx.closed_pattern(i)))+" ");
            }
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitClosed_pattern(PythonParser.Closed_patternContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitLiteral_pattern(PythonParser.Literal_patternContext ctx) {
        if(ctx.getText().contains("None")){
            return (T) "None";
        }else if(ctx.getText().contains("True")){
            return (T) "True";
        }else if(ctx.getText().contains("False")){
            return (T) "False";
        }
        return visitChildren(ctx);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitLiteral_expr(PythonParser.Literal_exprContext ctx) {
        if(ctx.getText().contains("None")){
            return (T) "None";
        }else if(ctx.getText().contains("True")){
            return (T) "True";
        }else if(ctx.getText().contains("False")){
            return (T) "False";
        }
        return visitChildren(ctx);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitComplex_number(PythonParser.Complex_numberContext ctx) {
        if(ctx.getText().contains("+")){
            return (T) (((String) visitSigned_real_number(ctx.signed_real_number()))+" + "+((String) visitImaginary_number(ctx.imaginary_number())));
        }else{
            return (T) (((String) visitSigned_real_number(ctx.signed_real_number()))+" - "+((String) visitImaginary_number(ctx.imaginary_number())));
        }
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitSigned_number(PythonParser.Signed_numberContext ctx) {
        if(ctx.getText().contains("-")){
            return (T) (ctx.NUMBER().getText()+" ");
        }
        return (T) (" - "+ctx.NUMBER().getText()+" ");
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitSigned_real_number(PythonParser.Signed_real_numberContext ctx) {
        if(ctx.getText().contains("-")){
            return (T) (((String) visitReal_number(ctx.real_number()))+" ");
        }
        return (T) (" - "+((String) visitReal_number(ctx.real_number()))+" ");
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitReal_number(PythonParser.Real_numberContext ctx) { return (T) ctx.NUMBER().getText(); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitImaginary_number(PythonParser.Imaginary_numberContext ctx) { return (T) ctx.NUMBER().getText(); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitCapture_pattern(PythonParser.Capture_patternContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitPattern_capture_target(PythonParser.Pattern_capture_targetContext ctx) {
        visitChildren(ctx);
        return (T) ctx.getText();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitWildcard_pattern(PythonParser.Wildcard_patternContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitValue_pattern(PythonParser.Value_patternContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitAttr(PythonParser.AttrContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append(ctx.NAME(0).getText()+" ");
        for(int i =1; i< ctx.NAME().size();i++){
            codeAux.append(" . "+ctx.NAME(i).getText()+" ");
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitName_or_attr(PythonParser.Name_or_attrContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append(ctx.NAME(0).getText()+" ");
        for(int i =1; i< ctx.NAME().size();i++){
            codeAux.append(" . "+ctx.NAME(i).getText()+" ");
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitGroup_pattern(PythonParser.Group_patternContext ctx) { return (T) (" ( "+visitChildren(ctx)+" ) "); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitSequence_pattern(PythonParser.Sequence_patternContext ctx) {
        if(ctx.getText().contains("[")){
            if(ctx.maybe_sequence_pattern()!=null){
                return (T) (" [ "+((String) visitMaybe_sequence_pattern(ctx.maybe_sequence_pattern()))+" ] ");
            }
            return (T) (" [ ] ");
        }else{
            if(ctx.open_sequence_pattern()!=null){
                return (T) (" ( "+((String) visitOpen_sequence_pattern(ctx.open_sequence_pattern()))+" ) ");
            }
            return (T) (" ( ) ");
        }
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitOpen_sequence_pattern(PythonParser.Open_sequence_patternContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append(((String) visitMaybe_star_pattern(ctx.maybe_star_pattern()))+" , ");
        if(ctx.maybe_sequence_pattern()!=null){
            codeAux.append(((String) visitMaybe_sequence_pattern(ctx.maybe_sequence_pattern()))+" ");
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitMaybe_sequence_pattern(PythonParser.Maybe_sequence_patternContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append(((String) visitMaybe_star_pattern(ctx.maybe_star_pattern(0)))+" , ");
        if(ctx.maybe_star_pattern().size()>1){
            for(int i=1;i<ctx.maybe_star_pattern().size();i++){
                codeAux.append(" , "+((String) visitMaybe_star_pattern(ctx.maybe_star_pattern(i)))+" ");
            }
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitMaybe_star_pattern(PythonParser.Maybe_star_patternContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitStar_pattern(PythonParser.Star_patternContext ctx) { return (T) (" * "+((String) visitChildren(ctx))); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitMapping_pattern(PythonParser.Mapping_patternContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append(ctx.LBRACE().getText()+" ");
        if(ctx.double_star_pattern()!=null){
            codeAux.append(((String) visitDouble_star_pattern(ctx.double_star_pattern()))+" ");
        }else if(ctx.items_pattern()!=null){
            codeAux.append(((String) visitItems_pattern(ctx.items_pattern()))+" ");
            if(ctx.double_star_pattern()!=null){
                codeAux.append(", "+((String) visitDouble_star_pattern(ctx.double_star_pattern()))+" ");
            }
        }
        codeAux.append(ctx.RBRACE().getText()+" ");
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitItems_pattern(PythonParser.Items_patternContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append(((String) visitKey_value_pattern(ctx.key_value_pattern(0)))+" ");
        for(int i=1;i<ctx.key_value_pattern().size();i++){
            codeAux.append(", "+((String) visitKey_value_pattern(ctx.key_value_pattern(i)))+" ");
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitKey_value_pattern(PythonParser.Key_value_patternContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        if(ctx.literal_expr()!=null){
            codeAux.append(((String) visitLiteral_expr(ctx.literal_expr()))+" ");
        }else{
            codeAux.append(((String) visitAttr(ctx.attr()))+" ");
        }
        codeAux.append(": "+((String) visitPattern(ctx.pattern()))+" ");
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitDouble_star_pattern(PythonParser.Double_star_patternContext ctx) { return (T) ("** "+((String) visitChildren(ctx))); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitClass_pattern(PythonParser.Class_patternContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append(((String) visitName_or_attr(ctx.name_or_attr()))+" ( ");
        if(ctx.positional_patterns()!=null){
            codeAux.append(((String) visitPositional_patterns(ctx.positional_patterns()))+" , ");
        }
        if(ctx.keyword_patterns()!=null){
            codeAux.append(((String) visitKeyword_patterns(ctx.keyword_patterns()))+" ");
        }
        codeAux.append(") ");
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitPositional_patterns(PythonParser.Positional_patternsContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append(((String) visitPattern(ctx.pattern(0)))+" ");
        for(int i = 1; i<ctx.pattern().size();i++){
            codeAux.append(((String) ", "+visitPattern(ctx.pattern(i)))+" ");
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitKeyword_patterns(PythonParser.Keyword_patternsContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append(((String) visitKeyword_pattern(ctx.keyword_pattern(0)))+" ");
        for(int i = 1; i<ctx.keyword_pattern().size();i++){
            codeAux.append(((String) visitKeyword_pattern(ctx.keyword_pattern(i)))+" ");
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitKeyword_pattern(PythonParser.Keyword_patternContext ctx) { return (T) (ctx.NAME().getText()+" = "+((String) visitChildren(ctx))); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitType_alias(PythonParser.Type_aliasContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append(((String) visitSoft_kw_type(ctx.soft_kw_type()))+" "+ctx.NAME().getText()+" ");
        if(ctx.type_params()!=null){
            codeAux.append(((String) visitType_params(ctx.type_params()))+" ");
        }
        codeAux.append("= "+((String) visitExpression(ctx.expression())));
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitType_params(PythonParser.Type_paramsContext ctx) {
        return (T )("[ "+((String) visitType_param_seq(ctx.type_param_seq()))+"] ");
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitType_param_seq(PythonParser.Type_param_seqContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        for(PythonParser.Type_paramContext x :ctx.type_param()){
            codeAux.append((String) visitType_param(x));
            codeAux.append(", ");
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitType_param(PythonParser.Type_paramContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        if(ctx.expression()==null){
            codeAux.append(ctx.NAME().getText()+" "+((String) visitType_param_bound(ctx.type_param_bound())));
        }else{
            if(ctx.getText().contains("**")){
                codeAux.append("** "+ctx.NAME().getText()+" ");
            }else{
                codeAux.append("* "+ctx.NAME().getText()+" ");
            }
            if(ctx.expression()!=null){
                codeAux.append(": "+((String) visitExpression(ctx.expression())));
            }
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitType_param_bound(PythonParser.Type_param_boundContext ctx) {
        return (T) (": "+((String) visitExpression(ctx.expression())));
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitExpressions(PythonParser.ExpressionsContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitExpression(PythonParser.ExpressionContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        if(ctx.lambdef()==null){
            codeAux.append(((String) visitDisjunction(ctx.disjunction(0)))+" ");
            if(ctx.expression()!=null){
                codeAux.append("if "+((String) visitDisjunction(ctx.disjunction(1)))+" else "+((String) visitExpression(ctx.expression())));
            }
        }else{
            codeAux.append((String) visitLambdef(ctx.lambdef()));
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitYield_expr(PythonParser.Yield_exprContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitStar_expressions(PythonParser.Star_expressionsContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitStar_expression(PythonParser.Star_expressionContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitStar_named_expressions(PythonParser.Star_named_expressionsContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitStar_named_expression(PythonParser.Star_named_expressionContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitAssignment_expression(PythonParser.Assignment_expressionContext ctx) { return (T) (ctx.NAME().getText()+" : = "+((String) visitExpression(ctx.expression()))); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitNamed_expression(PythonParser.Named_expressionContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitDisjunction(PythonParser.DisjunctionContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append(((String) visitConjunction(ctx.conjunction(0)))+" ");
        for(int i =1; i<ctx.conjunction().size();i++){
            codeAux.append("or "+((String) visitConjunction(ctx.conjunction(i))));
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitConjunction(PythonParser.ConjunctionContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append(((String) visitInversion(ctx.inversion(0)))+" ");
        for(int i =1; i<ctx.inversion().size();i++){
            codeAux.append("and "+((String) visitInversion(ctx.inversion(i))));
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitInversion(PythonParser.InversionContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        if(ctx.inversion()!=null){
            codeAux.append("not "+((String) visitInversion(ctx.inversion())));
        }else{
            codeAux.append((String) visitComparison(ctx.comparison()));
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitComparison(PythonParser.ComparisonContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append((String) visitBitwise_or(ctx.bitwise_or()));
        if(!ctx.compare_op_bitwise_or_pair().isEmpty()){
            for(PythonParser.Compare_op_bitwise_or_pairContext x : ctx.compare_op_bitwise_or_pair()){
                codeAux.append(((String) visitCompare_op_bitwise_or_pair(x))+" ");
            }
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitCompare_op_bitwise_or_pair(PythonParser.Compare_op_bitwise_or_pairContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitEq_bitwise_or(PythonParser.Eq_bitwise_orContext ctx) { return (T) ("== "+((String) visitChildren(ctx))); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitNoteq_bitwise_or(PythonParser.Noteq_bitwise_orContext ctx) { return (T) ("!= "+((String) visitChildren(ctx))); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitLte_bitwise_or(PythonParser.Lte_bitwise_orContext ctx) { return (T) ("<= "+((String) visitChildren(ctx))); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitLt_bitwise_or(PythonParser.Lt_bitwise_orContext ctx) { return (T) ("< "+((String) visitChildren(ctx))); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitGte_bitwise_or(PythonParser.Gte_bitwise_orContext ctx) { return (T) (">= "+((String) visitChildren(ctx))); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitGt_bitwise_or(PythonParser.Gt_bitwise_orContext ctx) { return (T) ("> "+((String) visitChildren(ctx))); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitNotin_bitwise_or(PythonParser.Notin_bitwise_orContext ctx) { return (T) ("not in "+((String) visitChildren(ctx))); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitIn_bitwise_or(PythonParser.In_bitwise_orContext ctx) { return (T) ("in "+((String) visitChildren(ctx))); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitIsnot_bitwise_or(PythonParser.Isnot_bitwise_orContext ctx) { return (T) ("is not "+((String) visitChildren(ctx))); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitIs_bitwise_or(PythonParser.Is_bitwise_orContext ctx) { return (T) ("is "+((String) visitChildren(ctx))); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitBitwise_or(PythonParser.Bitwise_orContext ctx) {
        if(ctx.bitwise_or()!=null){
            return (T) (((String) visitBitwise_or(ctx.bitwise_or()))+" | "+((String) visitBitwise_xor(ctx.bitwise_xor())));
        }
        return visitBitwise_xor(ctx.bitwise_xor()) ;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitBitwise_xor(PythonParser.Bitwise_xorContext ctx) {
        if(ctx.bitwise_xor()!=null){
            return (T) (((String) visitBitwise_xor(ctx.bitwise_xor()))+" ^ "+((String) visitBitwise_and(ctx.bitwise_and())));
        }
        return visitBitwise_and(ctx.bitwise_and()) ;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitBitwise_and(PythonParser.Bitwise_andContext ctx) {
        if(ctx.bitwise_and()!=null){
            return (T) (((String) visitBitwise_and(ctx.bitwise_and()))+" & "+((String) visitShift_expr(ctx.shift_expr())));
        }
        return visitShift_expr(ctx.shift_expr()) ;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitShift_expr(PythonParser.Shift_exprContext ctx) {
        if(ctx.shift_expr()!=null){
            if(ctx.getText().contains("<<")){
                return (T) (((String) visitShift_expr(ctx.shift_expr()))+" << "+((String) visitSum(ctx.sum())));
            }else{
                return (T) (((String) visitShift_expr(ctx.shift_expr()))+" >> "+((String) visitSum(ctx.sum())));
            }
        }
        return visitSum(ctx.sum()) ;
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitSum(PythonParser.SumContext ctx) {
        if(ctx.sum()!=null){
            if(ctx.getText().contains("+")){
                return (T) (((String) visitSum(ctx.sum()))+" + "+((String) visitTerm(ctx.term())));
            }else{
                return (T) (((String) visitSum(ctx.sum()))+" - "+((String) visitTerm(ctx.term())));
            }
        }
        return visitTerm(ctx.term());
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitTerm(PythonParser.TermContext ctx) {
        if(ctx.term()!=null){
            if(ctx.getText().contains("*")){
                return (T) (((String) visitTerm(ctx.term()))+" * "+((String) visitFactor(ctx.factor())));
            }else if(ctx.getText().contains("/")){
                return (T) (((String) visitTerm(ctx.term()))+" / "+((String) visitFactor(ctx.factor())));
            }else if(ctx.getText().contains("//")){
                return (T) (((String) visitTerm(ctx.term()))+" // "+((String) visitFactor(ctx.factor())));
            }else if(ctx.getText().contains("%")){
                return (T) (((String) visitTerm(ctx.term()))+" % "+((String) visitFactor(ctx.factor())));
            }

            return (T) (((String) visitTerm(ctx.term()))+" @ "+((String) visitFactor(ctx.factor())));
        }
        return visitFactor(ctx.factor());
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitFactor(PythonParser.FactorContext ctx) {
        if(ctx.factor()!=null){
            if(ctx.getText().contains("+")){
                return (T) ("+ "+((String) visitFactor(ctx.factor())));
            }else if(ctx.getText().contains("-")){
                return (T) ("- "+((String) visitFactor(ctx.factor())));
            }
            return (T) ("~ "+((String) visitFactor(ctx.factor())));
        }
        return visitPower(ctx.power());
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitPower(PythonParser.PowerContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append((String) visitAwait_primary(ctx.await_primary()));
        if(ctx.factor()!=null){
            codeAux.append(" ** "+((String) visitFactor(ctx.factor())));
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitAwait_primary(PythonParser.Await_primaryContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        if(ctx.AWAIT()!=null){
            codeAux.append(ctx.AWAIT().getText()+" ");
        }
        codeAux.append((String) visitPrimary(ctx.primary()));
        return (T) codeAux.toString();}
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitPrimary(PythonParser.PrimaryContext ctx) {
        if(ctx.atom()==null){
            StringBuffer codeAux = new StringBuffer();
            codeAux.append((String) visitPrimary(ctx.primary()));
            if(ctx.NAME()!=null){
                codeAux.append("."+ctx.NAME().getText()+" ");
            } else if (ctx.genexp()!=null) {
                codeAux.append((String) visitGenexp(ctx.genexp()));
            } else if (ctx.slices()!=null) {
                codeAux.append("[ "+((String) visitSlices(ctx.slices()))+" ]");
            }else{
                codeAux.append("( ");
                if(ctx.arguments()!=null){
                    codeAux.append((String) visitArguments(ctx.arguments()));
                }
                codeAux.append(" )");
            }
            return (T) codeAux.toString();
        }
        return visitAtom(ctx.atom());
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitSlices(PythonParser.SlicesContext ctx) {
        visitChildren(ctx);
        return (T) ctx.getText();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitSlice(PythonParser.SliceContext ctx) {
        if(ctx.named_expression()==null){
            StringBuffer codeAux = new StringBuffer();
            for(PythonParser.ExpressionContext x : ctx.expression()){
                codeAux.append(((String) visitExpression(x))+" : ");
            }
            if(ctx.expression().isEmpty()){
                codeAux.append(" : ");
            }else if(ctx.expression().size()>=2){
                codeAux.delete(codeAux.length()-3, codeAux.length());
            }
            return (T) codeAux.toString();
        }
        return visitNamed_expression(ctx.named_expression());
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitAtom(PythonParser.AtomContext ctx) {
        if(ctx.NAME()!=null){
            return (T) (ctx.NAME().getText()+" ");
        }else if(ctx.NUMBER()!=null){
            return (T) (ctx.NUMBER().getText()+" ");
        }else if(ctx.getText().contains("True")){
            return (T) ("True");
        }else if(ctx.getText().contains("False")){
            return (T) ("False");
        }else if(ctx.getText().contains("None")){
            return (T) ("None");
        }
        return visitChildren(ctx);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitGroup(PythonParser.GroupContext ctx) {
        if(ctx.yield_expr()==null){
            return (T) ("( "+((String) visitNamed_expression(ctx.named_expression()))+" ) ");
        }
        return (T) ("( "+((String) visitYield_expr(ctx.yield_expr()))+" ) ");
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitLambdef(PythonParser.LambdefContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append("lambda ");
        if(ctx.lambda_params()!=null){
            codeAux.append((String) visitLambda_params(ctx.lambda_params()));
        }
        codeAux.append(" : "+((String) visitExpression(ctx.expression()))+" ");
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitLambda_params(PythonParser.Lambda_paramsContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitLambda_parameters(PythonParser.Lambda_parametersContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitLambda_slash_no_default(PythonParser.Lambda_slash_no_defaultContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        for(PythonParser.Lambda_param_no_defaultContext x : ctx.lambda_param_no_default()){
            codeAux.append((String) visitLambda_param_no_default(x));
        }
        codeAux.append(" / ");
        if(ctx.getText().contains(",")){
            codeAux.append(" , ");
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitLambda_slash_with_default(PythonParser.Lambda_slash_with_defaultContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitLambda_star_etc(PythonParser.Lambda_star_etcContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitLambda_kwds(PythonParser.Lambda_kwdsContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitLambda_param_no_default(PythonParser.Lambda_param_no_defaultContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitLambda_param_with_default(PythonParser.Lambda_param_with_defaultContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitLambda_param_maybe_default(PythonParser.Lambda_param_maybe_defaultContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitLambda_param(PythonParser.Lambda_paramContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitFstring_middle(PythonParser.Fstring_middleContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitFstring_replacement_field(PythonParser.Fstring_replacement_fieldContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitFstring_conversion(PythonParser.Fstring_conversionContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitFstring_full_format_spec(PythonParser.Fstring_full_format_specContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitFstring_format_spec(PythonParser.Fstring_format_specContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitFstring(PythonParser.FstringContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitString(PythonParser.StringContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitStrings(PythonParser.StringsContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitList(PythonParser.ListContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitTuple(PythonParser.TupleContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitSet(PythonParser.SetContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitDict(PythonParser.DictContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitDouble_starred_kvpairs(PythonParser.Double_starred_kvpairsContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitDouble_starred_kvpair(PythonParser.Double_starred_kvpairContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitKvpair(PythonParser.KvpairContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitFor_if_clauses(PythonParser.For_if_clausesContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitFor_if_clause(PythonParser.For_if_clauseContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitListcomp(PythonParser.ListcompContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitSetcomp(PythonParser.SetcompContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitGenexp(PythonParser.GenexpContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitDictcomp(PythonParser.DictcompContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitArguments(PythonParser.ArgumentsContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitArgs(PythonParser.ArgsContext ctx) { return (T) ctx.getText(); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitKwargs(PythonParser.KwargsContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitStarred_expression(PythonParser.Starred_expressionContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitKwarg_or_starred(PythonParser.Kwarg_or_starredContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitKwarg_or_double_starred(PythonParser.Kwarg_or_double_starredContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitStar_targets(PythonParser.Star_targetsContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append(((String) visitStar_target(ctx.star_target(0)))+" ");
        for(int i =1; i< ctx.star_target().size();i++){
            codeAux.append(((String) ", "+visitStar_target(ctx.star_target(0)))+" ");
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitStar_targets_list_seq(PythonParser.Star_targets_list_seqContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitStar_targets_tuple_seq(PythonParser.Star_targets_tuple_seqContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitStar_target(PythonParser.Star_targetContext ctx) {
        if(ctx.target_with_star_atom()==null){
            return (T) ("* "+((String) visitStar_target(ctx.star_target())));
        }
        return visitChildren(ctx);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitTarget_with_star_atom(PythonParser.Target_with_star_atomContext ctx) {
        if(ctx.star_atom()==null){
            StringBuffer codeAux = new StringBuffer();
            codeAux.append(((String) visitT_primary(ctx.t_primary()))+" ");
            if(ctx.NAME()!=null){
                codeAux.append(". "+ctx.NAME().getText()+" ");
            }else{
                codeAux.append("[ "+((String) visitSlices(ctx.slices()))+"] ");
            }
            return (T) codeAux.toString();
        }
        return visitChildren(ctx);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitStar_atom(PythonParser.Star_atomContext ctx) {
        if(ctx.NAME()!=null){
            return (T) (ctx.NAME().getText()+" ");
        }else if(ctx.target_with_star_atom()!=null){
            return (T) ("( "+((String) visitTarget_with_star_atom(ctx.target_with_star_atom()))+" ) ");
        }else if(ctx.getText().contains("(")){
            if(ctx.star_targets_tuple_seq()!=null){
                return (T) ("( "+((String) visitStar_targets_tuple_seq(ctx.star_targets_tuple_seq()))+" ) ");
            }
            return (T) ("(  ) ");
        }else{
            if(ctx.star_targets_list_seq()!=null){
                return (T) ("[ "+((String) visitStar_targets_list_seq(ctx.star_targets_list_seq()))+" ] ");
            }
            return (T) ("[  ] ");
        }
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitSingle_target(PythonParser.Single_targetContext ctx) {
        if(ctx.NAME()!=null){
            return (T) (ctx.NAME().getText()+" ");
        }else if(ctx.single_target()!=null){
            return (T) ("( "+((String) visitSingle_target(ctx.single_target()))+" ) ");
        }
        return visitChildren(ctx);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitSingle_subscript_attribute_target(PythonParser.Single_subscript_attribute_targetContext ctx) {
        StringBuffer codeAux = new StringBuffer();
        codeAux.append(((String) visitT_primary(ctx.t_primary()))+" ");
        if(ctx.NAME()!=null){
            codeAux.append(". "+ctx.NAME().getText()+" ");
        }else{
            codeAux.append("[ "+((String) visitSlices(ctx.slices()))+" ] ");
        }
        return (T) codeAux.toString();
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitT_primary(PythonParser.T_primaryContext ctx) {
        if(ctx.atom()==null){
            StringBuffer codeAux = new StringBuffer();
            codeAux.append(((String) visitT_primary(ctx.t_primary()))+" ");
            if(ctx.NAME()!=null){
                codeAux.append(". "+ctx.NAME().getText()+" ");
            }else if(ctx.slices()!=null){
                codeAux.append("[ "+((String) visitSlices(ctx.slices()))+" ] ");
            }else if(ctx.genexp()!=null){
                codeAux.append(((String) visitGenexp(ctx.genexp()))+" ");
            }else{
                codeAux.append("( ");
                if(ctx.arguments()!=null){
                    codeAux.append(((String) visitArguments(ctx.arguments()))+" ");
                }
                codeAux.append(" ) ");
            }
            return (T) codeAux.toString();
        }
        return visitChildren(ctx);
    }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitDel_targets(PythonParser.Del_targetsContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitDel_target(PythonParser.Del_targetContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitDel_t_atom(PythonParser.Del_t_atomContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitType_expressions(PythonParser.Type_expressionsContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitFunc_type_comment(PythonParser.Func_type_commentContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitSoft_kw_match(PythonParser.Soft_kw_matchContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitSoft_kw_case(PythonParser.Soft_kw_caseContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitSoft_kw_wildcard(PythonParser.Soft_kw_wildcardContext ctx) { return visitChildren(ctx); }
    /**
     * {@inheritDoc}
     *
     * <p>The default implementation returns the result of calling
     * {@link #visitChildren} on {@code ctx}.</p>
     */
    @Override public T visitSoft_kw_type(PythonParser.Soft_kw_typeContext ctx) { return visitChildren(ctx); }

    private void getDuplicityInd(){
        for(int i = 0; i < functions.size(); i++){
            for(int j = i; j < functions.size(); j++){
                if(i==j){
                    for(String keys : functions.get(i).getRawCode().keySet()){
                        functions.get(i).getDuplicity().put(keys,new ArrayList<>());
                        if(functions.get(i).getRawCode().get(keys).equals("complete")){break;}
                        for(String keys2 : functions.get(i).getRawCode().keySet()){
                            if(!keys.equals(keys2) && functions.get(i).getRawCode().get(keys).equals(functions.get(i).getRawCode().get(keys2))){
                                double val = calculateJaccardSimilarity(keys,keys2);
                                functions.get(i).getDuplicity().get(keys).add(val);
                            }else{
                                functions.get(i).getDuplicity().get(keys).add(0.0);
                            }
                            //System.out.println(functions.get(i).getDuplicity().get(keys).toString());
                        }
                    }
                }else{
                    String val1 = "";
                    String val2 = "";
                    for(String keys : functions.get(i).getRawCode().keySet()){
                        if(functions.get(i).getRawCode().get(keys).equals("complete")){
                            val1 = keys;
                            break;
                        }
                    }
                    for(String keys : functions.get(j).getRawCode().keySet()){
                        if(functions.get(j).getRawCode().get(keys).equals("complete")){
                            val2 = keys;
                            break;
                        }
                    }
                    //System.out.println("Fun1: "+val1);
                    //System.out.println("Fun2: "+val2);
                    double val = calculateJaccardSimilarity(val1,val2);
                    System.out.println("Function "+functions.get(i).getName()+" and function "+functions.get(j).getName()+":");
                    System.out.println("\t"+val);
                }
            }
        }
    }

    private static double calculateJaccardSimilarity(String str1, String str2) {
        CharSequence charSeq1 = new StringBuilder(str1);
        CharSequence charSeq2 = new StringBuilder(str2);

        CosineSimilarity cosineSimilarity = new CosineSimilarity();
        JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
        return jaccardSimilarity.apply(charSeq1, charSeq2);
    }

    public ArrayList<FunctionSats> getFunc() {
        return functions;
    }

    public void setFunctions(ArrayList<FunctionSats> func) {
        this.functions = func;
    }
}