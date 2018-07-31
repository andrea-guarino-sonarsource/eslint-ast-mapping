package parser;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.Test;
import org.sonar.javascript.parser.JavaScriptParserBuilder;
import org.sonar.javascript.tree.SyntacticEquivalence;
import org.sonar.plugins.javascript.api.tree.ScriptTree;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class ESLintASTTest {

  @Test
  public void parseTest() throws Exception {
    URL url = Resources.getResource("small.js.json");
    String jsonText = Resources.toString(url, Charsets.UTF_8);
    URL urlJS = Resources.getResource("snippets/small.js");
    String jsText = Resources.toString(urlJS, Charsets.UTF_8);
    ScriptTree tree = (ScriptTree) new ESLintAST().parse(jsonText);
    ScriptTree sonarJSTree = (ScriptTree) JavaScriptParserBuilder.createParser().parse(jsText);
    assertThat(SyntacticEquivalence.areEquivalent(tree.items(), sonarJSTree.items())).isTrue();
  }
}