package com.github.highcharts4gwt.generator.option.field;

import javax.annotation.CheckForNull;

import com.github.highcharts4gwt.generator.common.OutputType;
import com.github.highcharts4gwt.generator.common.OutputTypeVisitor;
import com.sun.codemodel.JDefinedClass;

public class GenericFieldWriter implements OutputTypeVisitor<JDefinedClass, Void>
{

    public void writeGenericJsonObject(JDefinedClass jClass, OutputType outputType)
    {
        outputType.accept(this, jClass);
    }

    @Override
    @CheckForNull
    public Void visitInterface(JDefinedClass in)
    {
        InterfaceFieldHelper.addGenericJsonObjectGetterSetterDeclaration(in);
        return null;
    }

    @Override
    @CheckForNull
    public Void visitJso(JDefinedClass in)
    {
        JsoFieldHelper.addGenericJsonObjectGetterSetterDeclaration(in);
        return null;
    }

    @Override
    @CheckForNull
    public Void visitMock(JDefinedClass in)
    {
        MockFieldHelper.addGenericJsonObjectGetterSetterDeclaration(in);
        return null;
    }

}
