/* Soot - a J*va Optimization Framework
 * Copyright (C) 1999 Patrick Lam
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 */

/*
 * Modified by the Sable Research Group and others 1997-1999.
 * See the 'credits' file distributed with Soot for the complete list of
 * contributors.  (Soot is distributed at http://www.sable.mcgill.ca/soot)
 */

package de.upb.swt.soot.core.jimple.common.expr;

import de.upb.swt.soot.core.jimple.Jimple;
import de.upb.swt.soot.core.jimple.basic.Immediate;
import de.upb.swt.soot.core.jimple.visitor.ExprVisitor;
import de.upb.swt.soot.core.jimple.visitor.Visitor;
import de.upb.swt.soot.core.util.Copyable;
import javax.annotation.Nonnull;

public final class JCmpgExpr extends AbstractIntBinopExpr implements Copyable {

  public JCmpgExpr(@Nonnull Immediate op1, @Nonnull Immediate op2) {
    super(op1, op2);
  }

  @Override
  public final String getSymbol() {
    return " " + Jimple.CMPG + " ";
  }

  @Override
  public void accept(@Nonnull Visitor sw) {
    ((ExprVisitor) sw).caseCmpgExpr(this);
  }

  @Nonnull
  public JCmpgExpr withOp1(@Nonnull Immediate op1) {
    return new JCmpgExpr(op1, (Immediate) getOp2());
  }

  @Nonnull
  public JCmpgExpr withOp2(@Nonnull Immediate op2) {
    return new JCmpgExpr((Immediate) getOp1(), op2);
  }
}
