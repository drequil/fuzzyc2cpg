package io.shiftleft.fuzzyc2cpg.parser;

import org.antlr.v4.runtime.ParserRuleContext;

import io.shiftleft.fuzzyc2cpg.FunctionParser.InitDeclWithAssignContext;
import io.shiftleft.fuzzyc2cpg.FunctionParser.StatementContext;
import io.shiftleft.fuzzyc2cpg.ModuleParser.Parameter_declContext;
import io.shiftleft.fuzzyc2cpg.ModuleParser.Parameter_idContext;
import io.shiftleft.fuzzyc2cpg.ModuleParser.Parameter_nameContext;
import io.shiftleft.fuzzyc2cpg.ModuleParser.Template_nameContext;
import io.shiftleft.fuzzyc2cpg.ast.AstNode;
import io.shiftleft.fuzzyc2cpg.ast.expressions.AssignmentExpression;
import io.shiftleft.fuzzyc2cpg.ast.expressions.BinaryExpression;
import io.shiftleft.fuzzyc2cpg.ast.expressions.Expression;
import io.shiftleft.fuzzyc2cpg.ast.expressions.Identifier;
import io.shiftleft.fuzzyc2cpg.ast.functionDef.Template;
import io.shiftleft.fuzzyc2cpg.ast.functionDef.TemplateTypeName;
import io.shiftleft.fuzzyc2cpg.ast.langc.functiondef.Parameter;
import io.shiftleft.fuzzyc2cpg.ast.langc.functiondef.ParameterType;
import io.shiftleft.fuzzyc2cpg.ast.logical.statements.Statement;

public class AstNodeFactory {

  public static void initializeFromContext(AstNode node,
      ParserRuleContext ctx) {
    if (ctx == null) {
      return;
    }

    node.setLocation(CodeLocationExtractor.extractFromContext(ctx));
    node.setCodeStr(escapeCodeStr(getOriginalCodeFragment(ctx)));
  }

  private static String getOriginalCodeFragment(ParserRuleContext ctx) {

    int startIdx = ctx.start.getStartIndex();
    int stopIdx = ctx.stop != null ? ctx.stop.getStopIndex() : -1;

    String ret;
    // Check for size > 0 is required because toString() method in
    // ANTLR crashes on empty input streams.
    if (ctx.getStart().getInputStream().size() > 0) {
      ret = ctx.getStart().getInputStream().toString();
      if(startIdx <= stopIdx) {
        ret = ret.substring(startIdx, stopIdx + 1);
      } else {
        ret = "";
      }
    } else {
      ret = "";
    }

    return ret;
  }

  public static void initializeFromContext(Expression node,
      ParserRuleContext ctx) {
    initializeFromContext((AstNode) node, ctx);
    if (node instanceof BinaryExpression && ctx.getChildCount() == 3) {
      node.setOperator(ctx.getChild(1).getText());
    }
  }

  public static AstNode create(StatementContext ctx) {
    AstNode node = new Statement();
    initializeFromContext(node, ctx);
    return node;
  }

  private static String escapeCodeStr(String codeStr) {
    String retval = codeStr;
    retval = retval.replace("\n", "\\n");
    retval = retval.replace("\t", "\\t");
    return retval;
  }

  public static AssignmentExpression create(InitDeclWithAssignContext ctx) {
    AssignmentExpression assign = new AssignmentExpression();
    initializeFromContext(assign, ctx);
    if (ctx.getChildCount() == 3) {
      assign.setOperator(ctx.getChild(1).getText());
    }

    return assign;
  }

  public static Parameter create(Parameter_declContext ctx) {
    Parameter param = new Parameter();

    Parameter_declContext paramCtx = ctx;
    Parameter_nameContext paramName = getNameOfParameter(paramCtx);

    Identifier name = new Identifier();
    ParameterType type = new ParameterType();
    initializeFromContext(type, ctx);
    initializeFromContext(name, paramName);
    initializeFromContext(param, ctx);

    param.addChild(type);
    param.addChild(name);

    return param;
  }

  private static Parameter_nameContext getNameOfParameter(
      Parameter_declContext paramCtx) {
    Parameter_idContext parameterId = paramCtx.parameter_id();

    while (parameterId.parameter_name() == null) {
      parameterId = parameterId.parameter_id();
    }
    return parameterId.parameter_name();
  }

  public static Template create(Template_nameContext ctx) {

    Template template = new Template();

    String typeName = ParseTreeUtils.childTokenString(ctx);
    TemplateTypeName templateType = new TemplateTypeName(typeName);

    initializeFromContext(templateType, ctx);
    initializeFromContext(template, ctx);

    template.addChild(templateType);
    return template;
  }
}
