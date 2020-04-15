package de.upb.swt.soot.javatestsuite.java6;

import de.upb.swt.soot.core.model.SootMethod;
import de.upb.swt.soot.core.signatures.MethodSignature;
import de.upb.swt.soot.javatestsuite.JimpleTestSuiteBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Test;

/** @author Kaustubh Kelkar */
public class SuperClassTest extends JimpleTestSuiteBase {
  public MethodSignature getMethodSignature() {
    return identifierFactory.getMethodSignature(
        "superclassMethod", getDeclaredClassSignature(), "void", Collections.emptyList());
  }

  public List<String> expectedBodyStmts() {
    return Stream.of(
            "r0 := @this: SuperClass",
            "r0.<SuperClass: int a> = 10",
            "r0.<SuperClass: int b> = 20",
            "r0.<SuperClass: int c> = 30",
            "r0.<SuperClass: int d> = 40",
            "return")
        .collect(Collectors.toCollection(ArrayList::new));
  }

  @Test
  public void test() {
    SootMethod method = loadMethod(getMethodSignature());
    assertJimpleStmts(method, expectedBodyStmts());
  }
}
