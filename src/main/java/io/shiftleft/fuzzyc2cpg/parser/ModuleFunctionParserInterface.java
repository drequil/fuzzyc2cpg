package io.shiftleft.fuzzyc2cpg.parser;

import io.shiftleft.fuzzyc2cpg.ModuleParser.Compound_statementContext;
import io.shiftleft.fuzzyc2cpg.ModuleParser.Function_defContext;
import io.shiftleft.fuzzyc2cpg.ast.logical.statements.CompoundStatement;
import io.shiftleft.fuzzyc2cpg.parser.functions.AntlrCFunctionParserDriver;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.misc.Interval;

public class ModuleFunctionParserInterface {
  // Extracts compound statement from input stream
  // as a string and passes that string to the
  // function parser. The resulting 'CompoundStatement'
  // (an AST node) is returned.

  public static CompoundStatement parseFunctionContents(
      Function_defContext ctx) {
    String text = getCompoundStmtAsString(ctx);

    AntlrCFunctionParserDriver driver = new AntlrCFunctionParserDriver();

    try {
      driver.parseAndWalkString(text);
    } catch (RuntimeException ex) {
      System.err.println("Error parsing function "
          + ctx.function_name().getText() + ". skipping.");

      // ex.printStackTrace();
    }
    CompoundStatement result = driver.getResult();
    Compound_statementContext statementContext = ctx.compound_statement();
    AstNodeFactory.initializeFromContext(result, statementContext);
    return result;
  }

  private static String getCompoundStmtAsString(
      Function_defContext ctx) {
    Compound_statementContext compound_statement = ctx.compound_statement();

    CharStream inputStream = compound_statement.start.getInputStream();
    int startIndex = compound_statement.start.getStopIndex();
    int stopIndex = compound_statement.stop.getStopIndex();
    return inputStream.getText(new Interval(startIndex + 1, stopIndex - 1));
  }

}
