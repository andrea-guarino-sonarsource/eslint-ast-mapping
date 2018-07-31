package extension;

import api.Node;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NativeKindImpl implements NativeKind {
  private final Node element;
  private final List<Object> differentiators;

  public NativeKindImpl(Node element) {
    this.element = element;
    differentiators = Collections.emptyList();
  }

  public NativeKindImpl(Node element, Object... differentiators) {
    this.element = element;
    this.differentiators = Arrays.asList(differentiators);
  }
}
