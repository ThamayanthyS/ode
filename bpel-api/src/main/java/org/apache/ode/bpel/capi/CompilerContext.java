/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.ode.bpel.capi;

import org.apache.ode.bom.api.Activity;
import org.apache.ode.bom.api.Expression;
import org.apache.ode.bpel.o.OActivity;
import org.apache.ode.bpel.o.OExpression;
import org.apache.ode.bpel.o.OLValueExpression;
import org.apache.ode.bpel.o.OLink;
import org.apache.ode.bpel.o.OMessageVarType;
import org.apache.ode.bpel.o.OPartnerLink;
import org.apache.ode.bpel.o.OProcess;
import org.apache.ode.bpel.o.OScope;
import org.apache.ode.bpel.o.OXsdTypeVarType;
import org.apache.ode.bpel.o.OXslSheet;

import javax.wsdl.Operation;
import javax.xml.namespace.QName;
import java.util.List;


/**
 * Interface providing access to the compiler .
 */
public interface CompilerContext {

    OExpression constantExpr(boolean value);

    OExpression compileJoinCondition(Expression expr)
            throws CompilationException;

    OExpression compileExpr(Expression expr)
            throws CompilationException;

    OLValueExpression compileLValueExpr(Expression expr)
            throws CompilationException;

    OXslSheet compileXslt(String docStrUri)
            throws CompilationException;

    OXsdTypeVarType resolveXsdType(QName typeName)
            throws CompilationException;

    OProcess.OProperty resolveProperty(QName name)
            throws CompilationException;

    OScope.Variable resolveVariable(String name)
            throws CompilationException;

    List<OScope.Variable> getAccessibleVariables();

    OScope.Variable resolveMessageVariable(String inputVar)
            throws CompilationException;

    OScope.Variable resolveMessageVariable(String inputVar, QName messageType)
            throws CompilationException;

    OMessageVarType.Part resolvePart(OScope.Variable variable, String partname)
            throws CompilationException;

    OActivity compile(Activity child)
            throws CompilationException;

    OActivity compileSLC(final Activity source)
            throws CompilationException;

    OPartnerLink resolvePartnerLink(String name)
            throws CompilationException;

    Operation resolvePartnerRoleOperation(OPartnerLink partnerLink, String operationName)
            throws CompilationException;

    Operation resolveMyRoleOperation(OPartnerLink partnerLink, String operationName)
            throws CompilationException;

    OProcess.OPropertyAlias resolvePropertyAlias(OScope.Variable variable, QName property)
            throws CompilationException;

    void recoveredFromError(Object where, CompilationException bce)
            throws CompilationException;

    OLink resolveLink(String linkName)
            throws CompilationException;

    OScope resolveCompensatableScope(String scopeToCompensate)
            throws CompilationException;

    OProcess getOProcess()
            throws CompilationException;

    OScope.CorrelationSet resolveCorrelationSet(String csetName)
            throws CompilationException;

    String getSourceLocation();

    void compile(OActivity context, Runnable run);

    public boolean isPartnerLinkAssigned(String plink);
}
