/*-
 * #%L
 * Soot
 * %%
 * Copyright (C) 15.11.2018 Markus Schmidt
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Lesser Public License for more details.
 *
 * You should have received a copy of the GNU General Lesser Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/lgpl-2.1.html>.
 * #L%
 */

package de.upb.soot.jimple.common.stmt;

import categories.Java8Test;
import de.upb.soot.DefaultIdentifierFactory;
import de.upb.soot.jimple.basic.Local;
import de.upb.soot.jimple.basic.PositionInfo;
import de.upb.soot.jimple.common.ref.JCaughtExceptionRef;
import de.upb.soot.jimple.common.ref.JParameterRef;
import de.upb.soot.jimple.common.ref.JThisRef;
import de.upb.soot.types.PrimitiveType;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

/** @author Markus Schmidt & Linghui Luo */
@Category(Java8Test.class)
public class JIdentityStmtTest {

  @Test
  public void test() {
    PositionInfo nop = PositionInfo.createNoPositionInfo();
    DefaultIdentifierFactory typeFactory = DefaultIdentifierFactory.getInstance();

    Local thiz = new Local("$r0", typeFactory.getType("somepackage.dummy.MyClass"));
    Stmt thisIdStmt =
        new JIdentityStmt(
            thiz, new JThisRef(typeFactory.getClassType("somepackage.dummy.MyClass")), nop);

    Local param = new Local("$i0", PrimitiveType.getInt());
    Stmt paramIdStmt =
        new JIdentityStmt(param, new JParameterRef(PrimitiveType.getInt(), 123), nop);

    Local exception = new Local("$r1", typeFactory.getType("java.lang.Exception"));
    Stmt exceptionIdStmt = new JIdentityStmt(exception, new JCaughtExceptionRef(), nop);

    // toString
    Assert.assertEquals("$r0 := @this: somepackage.dummy.MyClass", thisIdStmt.toString());
    Assert.assertEquals("$i0 := @parameter123: int", paramIdStmt.toString());
    Assert.assertEquals("$r1 := @caughtexception", exceptionIdStmt.toString());

    // equivTo
    Assert.assertFalse(
        thisIdStmt.equivTo(
            new JIdentityStmt(
                new Local("$r5", typeFactory.getType("somepackage.NotMyClass")),
                new JThisRef(typeFactory.getClassType("somepackage.NotMyClass")),
                nop)));
    Assert.assertFalse(
        thisIdStmt.equivTo(
            new JIdentityStmt(
                new Local("$r42", typeFactory.getType("somepackage.dummy.MyClass")),
                new JThisRef(typeFactory.getClassType("somepackage.dummy.MyClass")),
                nop)));
    Assert.assertTrue(thisIdStmt.equivTo(thisIdStmt));
    Assert.assertFalse(thisIdStmt.equivTo(exceptionIdStmt));
    Assert.assertFalse(thisIdStmt.equivTo(paramIdStmt));

    Assert.assertFalse(
        thisIdStmt.equivTo(
            new JIdentityStmt(
                new Local("$i1", PrimitiveType.getInt()),
                new JParameterRef(PrimitiveType.getInt(), 123),
                nop)));
    Assert.assertFalse(
        thisIdStmt.equivTo(
            new JIdentityStmt(
                new Local("$i0", PrimitiveType.getInt()),
                new JParameterRef(PrimitiveType.getInt(), 42),
                nop)));
    Assert.assertFalse(exceptionIdStmt.equivTo(thisIdStmt));
    Assert.assertTrue(exceptionIdStmt.equivTo(exceptionIdStmt));
    Assert.assertFalse(exceptionIdStmt.equivTo(paramIdStmt));

    Assert.assertFalse(
        thisIdStmt.equivTo(
            new JIdentityStmt(
                new Local("$r1", typeFactory.getType("somepckg.NotMyException")),
                new JCaughtExceptionRef(),
                nop)));
    Assert.assertFalse(paramIdStmt.equivTo(thisIdStmt));
    Assert.assertFalse(paramIdStmt.equivTo(exceptionIdStmt));
    Assert.assertTrue(paramIdStmt.equivTo(paramIdStmt));
  }
}
