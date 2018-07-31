package extension;

import org.sonar.plugins.javascript.api.tree.Tree;

public interface NativeTree extends Tree {

  NativeKind nativeKind();

}
