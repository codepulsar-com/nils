package com.codepulsar.nils.core.handler;

import com.codepulsar.nils.core.NLS;

/**
 * Interface for resolving the prefix of a key for a translation from a <code>Class
 * </code>.
 *
 * <p>The interface is used calling {@link NLS#get(Class, String)} or {@link NLS#get(Class, String,
 * Object...)} to resolve the prefix for the translation key.
 */
@FunctionalInterface
public interface ClassPrefixResolver {
  /**
   * Default implementation resolving the simple name from a class, i.e. xyz.dummy.DummyClass -&gt;
   * "DummyClass".
   */
  public static final ClassPrefixResolver SIMPLE_CLASSNAME = k -> k.getSimpleName();

  /**
   * Default implementation resolving the full qualified name from a class, i.e.
   * xyz.dummy.DummyClass -&gt; "xyz.dummy.DummyClass".
   */
  public static final ClassPrefixResolver FQN_CLASSNAME = k -> k.getName();

  /**
   * Get the resolved String representation of a <code>Class</code>.
   *
   * @param clazz The <code>Class</code> to resolve
   * @return The String representation of the <code>Class</code>
   */
  String resolve(Class<?> clazz);
}
