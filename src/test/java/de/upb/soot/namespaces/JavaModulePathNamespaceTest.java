package de.upb.soot.namespaces;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import de.upb.soot.namespaces.classprovider.ClassSource;
import de.upb.soot.signatures.ClassSignature;
import de.upb.soot.signatures.ModuleSignatureFactory;
import de.upb.soot.signatures.SignatureFactory;

import java.util.Collections;
import java.util.Optional;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.powermock.reflect.Whitebox;

import categories.Java9Test;

@Category(Java9Test.class)

public class JavaModulePathNamespaceTest extends AbstractNamespaceTest {

  @Override
  protected SignatureFactory getSignatureFactory() {
    return new ModuleSignatureFactory(){};
  }

  @Test
  public void singleDir() {
    ModuleSignatureFactory factory = (ModuleSignatureFactory) getSignatureFactory();
    final JavaModulePathNamespace javaClassPathNamespace
        = new JavaModulePathNamespace(getClassProvider(), "target/test-classes/de/upb/soot/namespaces/modules");
    final ClassSignature sig = factory.getClassSignature("module-info", "", "fancyMod");
    Optional<ClassSource> classSource = javaClassPathNamespace.getClassSource(sig);
    assertTrue(classSource.isPresent());
  }

  @Test
  public void singleDir2() {
    ModuleSignatureFactory factory = (ModuleSignatureFactory) getSignatureFactory();;
    final JavaModulePathNamespace javaClassPathNamespace
        = new JavaModulePathNamespace(getClassProvider(), "target/test-classes/de/upb/soot/namespaces/modules");
    final ClassSignature sig = factory.getClassSignature("module-info", "", "fancyMod");
    // TODO: check for a better minClassFoundNumber
    // also all JDK classes are loaded
    testClassReceival(javaClassPathNamespace, sig, 50);
  }

  @Test
  public void singleJar() {
    ModuleSignatureFactory factory = (ModuleSignatureFactory) getSignatureFactory();
    final JavaModulePathNamespace javaClassPathNamespace = new JavaModulePathNamespace(getClassProvider(),
        "target/test-classes/de/upb/soot/namespaces/modules/de.upb.mod.jar");
    final ClassSignature sig = factory.getClassSignature("module-info", "", "de.upb.mod");
    Optional<ClassSource> classSource = javaClassPathNamespace.getClassSource(sig);
    assertTrue(classSource.isPresent());

  }

  @Test
  public void testSignatureWrapper() throws Exception {
    ModuleSignatureFactory factory = (ModuleSignatureFactory) getSignatureFactory();
    final JavaModulePathNamespace javaClassPathNamespace = new JavaModulePathNamespace(getClassProvider(),
        "target/test-classes/de/upb/soot/namespaces/modules/de.upb.mod.jar");
    Class signatureCLass = Whitebox.getInnerClassType(JavaModulePathNamespace.class, "SignatureFactoryWrapper");
    // Constructor constructor = Whitebox.getConstructor(signatureCLass, SignatureFactory.class, String.class);
    Object signatureFacotryWrapper = Whitebox.invokeConstructor(signatureCLass,
        new Class[] { JavaModulePathNamespace.class, SignatureFactory.class, String.class },
        new Object[] { javaClassPathNamespace, factory, "myJava.mod" });
    Object res1 = Whitebox.invokeMethod(signatureFacotryWrapper, "getClassSignature", "java.lang.System");
    assertEquals(res1, factory.getClassSignature("java.lang.System"));

    res1 = Whitebox.invokeMethod(signatureFacotryWrapper, "getClassSignature", "java.lang", "System");
    assertEquals(res1, factory.getClassSignature("java.lang", "System"));

    res1 = Whitebox.invokeMethod(signatureFacotryWrapper, "getTypeSignature", "int");
    assertEquals(res1, factory.getTypeSignature("int"));

    res1 = Whitebox.invokeMethod(signatureFacotryWrapper, "getTypeSignature", "int");
    assertEquals(res1, factory.getTypeSignature("int"));

    res1 = Whitebox.invokeMethod(signatureFacotryWrapper, "getMethodSignature", "metho1", "java.lang.System", "void",
        Collections.emptyList());
    assertEquals(res1, factory.getMethodSignature("metho1", "java.lang.System", "void", Collections.emptyList()));

    res1 = Whitebox.invokeMethod(signatureFacotryWrapper, "getMethodSignature", "metho1",
        factory.getClassSignature("java.lang.System"), "void", Collections.emptyList());
    assertEquals(res1, factory.getMethodSignature("metho1", factory.getClassSignature("java.lang.System"), "void",
        Collections.emptyList()));

    res1 = Whitebox.invokeMethod(signatureFacotryWrapper, "getPackageSignature", "java.lang");
    assertEquals(res1, factory.getPackageSignature("java.lang"));
  }
}