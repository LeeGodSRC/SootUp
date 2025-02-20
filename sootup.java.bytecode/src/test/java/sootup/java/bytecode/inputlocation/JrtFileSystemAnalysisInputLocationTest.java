package sootup.java.bytecode.inputlocation;

import static org.junit.Assert.*;

import categories.Java9Test;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import sootup.core.Project;
import sootup.core.frontend.AbstractClassSource;
import sootup.core.inputlocation.DefaultSourceTypeSpecifier;
import sootup.core.types.ClassType;
import sootup.java.core.JavaModuleIdentifierFactory;
import sootup.java.core.JavaModuleProject;
import sootup.java.core.JavaSootClass;
import sootup.java.core.language.JavaLanguage;
import sootup.java.core.signatures.ModuleSignature;
import sootup.java.core.views.JavaView;

/** @author Andreas Dann, Markus Schmidt */
@Category(Java9Test.class)
public class JrtFileSystemAnalysisInputLocationTest {

  @Test
  public void getClassSource() {
    JrtFileSystemAnalysisInputLocation inputLocation = new JrtFileSystemAnalysisInputLocation();
    Project<JavaSootClass, JavaView> project =
        new JavaModuleProject(
            new JavaLanguage(9),
            Collections.emptyList(),
            Collections.singletonList(inputLocation),
            DefaultSourceTypeSpecifier.getInstance());
    final ClassType sig =
        JavaModuleIdentifierFactory.getInstance().getClassType("String", "java.lang", "java.base");

    final Optional<? extends AbstractClassSource<JavaSootClass>> clazz =
        inputLocation.getClassSource(sig, project.createView());
    assertTrue(clazz.isPresent());
    assertEquals(sig, clazz.get().getClassType());
  }

  @Test
  public void getClassSources() {
    // hint: quite expensive as it loads **all** Runtime modules!
    JrtFileSystemAnalysisInputLocation inputLocation = new JrtFileSystemAnalysisInputLocation();
    Project<JavaSootClass, JavaView> project =
        new JavaModuleProject(
            new JavaLanguage(9),
            Collections.emptyList(),
            Collections.singletonList(inputLocation),
            DefaultSourceTypeSpecifier.getInstance());
    final ClassType sig1 =
        JavaModuleIdentifierFactory.getInstance().getClassType("String", "java.lang", "java.base");
    final ClassType sig2 =
        JavaModuleIdentifierFactory.getInstance().getClassType("System", "java.lang", "java.base");

    final JavaView view = project.createView();
    final Collection<? extends AbstractClassSource<?>> classSources =
        inputLocation.getClassSources(view);
    assertTrue(
        classSources.size()
            > 20000); // not precise as this amount can differ depending on the included runtime
    // library
    assertTrue(classSources.stream().anyMatch(cs -> cs.getClassType().equals(sig1)));
    assertTrue(view.getClass(sig1).isPresent());
    assertTrue(classSources.stream().anyMatch(cs -> cs.getClassType().equals(sig2)));
    assertTrue(view.getClass(sig2).isPresent());
  }

  @Test
  public void discoverModules() {
    JrtFileSystemAnalysisInputLocation inputLocation = new JrtFileSystemAnalysisInputLocation();
    Collection<ModuleSignature> modules = inputLocation.discoverModules();
    assertTrue(modules.size() > 65);
    System.out.println(modules);
    assertTrue(modules.contains(JavaModuleIdentifierFactory.getModuleSignature("java.base")));
    assertTrue(modules.contains(JavaModuleIdentifierFactory.getModuleSignature("java.se")));
    assertTrue(modules.contains(JavaModuleIdentifierFactory.getModuleSignature("jdk.javadoc")));
    assertTrue(modules.contains(JavaModuleIdentifierFactory.getModuleSignature("jdk.charsets")));
  }
}
