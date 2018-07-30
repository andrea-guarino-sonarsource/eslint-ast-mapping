package parser;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import impl.ExpressionStatementImpl;
import impl.IdentifierImpl;
import impl.NodeImpl;
import impl.ProgramImpl;
import impl.TokenImpl;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;
import org.sonar.javascript.tree.impl.JavaScriptTree;
import org.sonar.javascript.tree.impl.declaration.ModuleTreeImpl;
import org.sonar.javascript.tree.impl.expression.IdentifierTreeImpl;
import org.sonar.javascript.tree.impl.lexical.InternalSyntaxToken;
import org.sonar.javascript.tree.impl.lexical.InternalSyntaxTrivia;
import org.sonar.javascript.tree.impl.statement.ExpressionStatementTreeImpl;
import org.sonar.plugins.javascript.api.tree.Tree;
import org.sonar.plugins.javascript.api.tree.expression.ExpressionTree;
import org.sonar.plugins.javascript.api.tree.lexical.SyntaxTrivia;

public class ESLintAST {

  private List<TokenImpl> tokens;

  public Tree parse() throws Exception {
    URL url = Resources.getResource("small.js.json");
    String jsonText = Resources.toString(url, Charsets.UTF_8);
    Gson gson = new Gson();
    ProgramImpl program = gson.fromJson(jsonText, ProgramImpl.class);
    this.tokens = program.tokens();
    return convert(program);
  }

  public Tree convert(NodeImpl node) {
    JavaScriptTree root = null;
    if (node instanceof ProgramImpl) {
      List<Tree> items = ((ProgramImpl) node).body().stream()
        .map(this::convert)
        .collect(Collectors.toList());
      root = new ModuleTreeImpl(items);
    } else if (node instanceof IdentifierImpl) {
      return new IdentifierTreeImpl(Tree.Kind.IDENTIFIER_REFERENCE, toJSToken(node));
    } else if (node instanceof ExpressionStatementImpl) {
      ExpressionTree expression = (ExpressionTree) convert(((ExpressionStatementImpl) node).expression());
      return new ExpressionStatementTreeImpl(expression, null);
    }
    return root;
  }

  private InternalSyntaxToken toJSToken(NodeImpl node) {
    List<SyntaxTrivia> trivias = tokens.stream()
      .filter(token -> {
        int start = token.range()[0];
        int end = token.range()[1];
        return token.range()[0] >= start && token.range()[1] <= end;
      })
      .map(token -> new InternalSyntaxTrivia(token.value(), token.loc().start().line(), token.loc().start().column()))
      .collect(Collectors.toList());
    return new InternalSyntaxToken(
      node.loc().start().line(),
      node.loc().start().column(),
      ((IdentifierImpl) node).name(),
      trivias,
      node.range()[0],
      false
    );
  }
}
