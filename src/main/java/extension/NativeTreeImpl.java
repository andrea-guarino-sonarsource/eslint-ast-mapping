package extension;

import java.util.List;
import java.util.Iterator;
import org.sonar.javascript.tree.impl.JavaScriptTree;
import org.sonar.plugins.javascript.api.tree.Tree;
import org.sonar.plugins.javascript.api.visitors.DoubleDispatchVisitor;

public class NativeTreeImpl extends JavaScriptTree implements NativeTree {
  private final List<Tree> children;
  private final NativeKind nativeKind;

  public NativeTreeImpl(List<Tree> children, NativeKind nativeKind) {
    this.children = children;
    this.nativeKind = nativeKind;
  }

  @Override
  public NativeKind nativeKind() {
    return nativeKind;
  }

  @Override
  public Kind getKind() {
    return null;
  }

  @Override
  public Iterator<Tree> childrenIterator() {
    return children.iterator();
  }

  @Override
  public void accept(DoubleDispatchVisitor doubleDispatchVisitor) {
    children.forEach(child -> child.accept(doubleDispatchVisitor));
  }
}
