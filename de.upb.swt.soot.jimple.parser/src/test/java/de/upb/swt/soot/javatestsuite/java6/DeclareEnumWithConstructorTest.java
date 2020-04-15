package de.upb.swt.soot.javatestsuite.java6;

import static org.junit.Assert.assertTrue;

import de.upb.swt.soot.categories.Java8Test;
import de.upb.swt.soot.core.model.SootClass;
import de.upb.swt.soot.core.model.SootMethod;
import de.upb.swt.soot.core.signatures.MethodSignature;
import de.upb.swt.soot.javatestsuite.JimpleTestSuiteBase;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.experimental.categories.Category;

/** @author Kaustubh Kelkar */
@Category(Java8Test.class)
public class DeclareEnumWithConstructorTest extends JimpleTestSuiteBase {

  public MethodSignature getInitMethodSignature() {
    return identifierFactory.getMethodSignature(
        "<init>", getDeclaredClassSignature(), "void", Collections.emptyList());
  }

  public MethodSignature getMethodSignature() {
    return identifierFactory.getMethodSignature(
        "getValue", getDeclaredClassSignature(), "int", Collections.emptyList());
  }

  public MethodSignature getMainMethodSignature() {
    return identifierFactory.getMethodSignature(
        "main",
        getDeclaredClassSignature(),
        "void",
        Collections.singletonList("java.lang.String[]"));
  }

  public MethodSignature getEnumConstructorSignature() {
    return identifierFactory.getMethodSignature(
        "<clinit>",
        identifierFactory.getClassType("DeclareEnumWithConstructor$Number"),
        "void",
        Collections.emptyList());
  }

  public MethodSignature getEnumGetValueSignature() {
    return identifierFactory.getMethodSignature(
        "getValue",
        identifierFactory.getClassType("DeclareEnumWithConstructor$Number"),
        "int",
        Collections.emptyList());
  }

  @org.junit.Test
  public void test() {
    SootMethod sootMethod = loadMethod(getInitMethodSignature());
    assertJimpleStmts(sootMethod, expectedBodyStmts());

    sootMethod = loadMethod(getMainMethodSignature());
    assertJimpleStmts(sootMethod, expectedMainBodyStmts());

    sootMethod = loadMethod(getEnumConstructorSignature());
    assertJimpleStmts(sootMethod, expectedEnumConstructorStmts());

    sootMethod = loadMethod(getEnumGetValueSignature());
    assertJimpleStmts(sootMethod, expectedGetValueStmts());

    SootClass sootClass =
        loadClass(identifierFactory.getClassType("DeclareEnumWithConstructor$Number"));
    System.out.println(sootClass.getModifiers());
    assertTrue(sootClass.isEnum());
  }

  public List<String> expectedBodyStmts() {
    return Stream.of(
            "r0 := @this: DeclareEnumWithConstructor",
            "specialinvoke r0.<java.lang.Object: void <init>()>()",
            "return")
        .collect(Collectors.toList());
  }

  public List<String> expectedMainBodyStmts() {
    return Stream.of(
            "$r0 := @parameter0: java.lang.String[]",
            "$r1 = <DeclareEnumWithConstructor$Number: DeclareEnumWithConstructor$Number ONE>",
            "$r2 = <java.lang.System: java.io.PrintStream out>",
            "$i0 = specialinvoke $r1.<DeclareEnumWithConstructor$Number: int getValue()>()",
            "virtualinvoke $r2.<java.io.PrintStream: void println(int)>($i0)",
            "return")
        .collect(Collectors.toList());
  }

  public List<String> expectedEnumConstructorStmts() {
    return Stream.of(
            "$r0 = new DeclareEnumWithConstructor$Number",
            "specialinvoke $r0.<DeclareEnumWithConstructor$Number: void <init>(java.lang.String,int,int)>(\"ZERO\", 0, 0)",
            "<DeclareEnumWithConstructor$Number: DeclareEnumWithConstructor$Number ZERO> = $r0",
            "$r1 = new DeclareEnumWithConstructor$Number",
            "specialinvoke $r1.<DeclareEnumWithConstructor$Number: void <init>(java.lang.String,int,int)>(\"ONE\", 1, 1)",
            "<DeclareEnumWithConstructor$Number: DeclareEnumWithConstructor$Number ONE> = $r1",
            "$r2 = new DeclareEnumWithConstructor$Number",
            "specialinvoke $r2.<DeclareEnumWithConstructor$Number: void <init>(java.lang.String,int,int)>(\"TWO\", 2, 2)",
            "<DeclareEnumWithConstructor$Number: DeclareEnumWithConstructor$Number TWO> = $r2",
            "$r3 = new DeclareEnumWithConstructor$Number",
            "specialinvoke $r3.<DeclareEnumWithConstructor$Number: void <init>(java.lang.String,int,int)>(\"THREE\", 3, 3)",
            "<DeclareEnumWithConstructor$Number: DeclareEnumWithConstructor$Number THREE> = $r3",
            "return")
        .collect(Collectors.toList());
  }

  public List<String> expectedGetValueStmts() {
    return Stream.of(
            "r0 := @this: DeclareEnumWithConstructor$Number",
            "$i0 = r0.<DeclareEnumWithConstructor$Number: int value>",
            "return $i0")
        .collect(Collectors.toList());
  }
}
