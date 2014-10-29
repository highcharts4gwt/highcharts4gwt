package com.usesoft.highcharts4gwt.generator.codemodel.field;

import javax.annotation.CheckForNull;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.usesoft.highcharts4gwt.generator.codemodel.OutputTypeVisitor;

public class FieldBooleanWriter extends FieldWriter implements OutputTypeVisitor<Void, Void>
{

    private final boolean defaultValue;

    public FieldBooleanWriter(JCodeModel codeModel, JDefinedClass jClass, String className, boolean defaultValue, boolean pipe, String fieldName)
    {
        super(codeModel, className, jClass, pipe, fieldName);
        this.defaultValue = defaultValue;
    }

    @Override
    @CheckForNull
    public Void visitInterface(Void in)
    {
        InterfaceFieldHelper.addGetterSetterDeclaration(getNames(), boolean.class, getJclass());
        return null;
    }

    @Override
    @CheckForNull
    public Void visitJso(Void in)
    {
        JsoFieldHelper.writeGetterNativeCodeBoolean(fieldName, boolean.class, getJclass(), getCodeModel(), defaultValue);
        JsoFieldHelper.writeSetterNativeCode(fieldName, boolean.class, getJclass(), getCodeModel());
        return null;
    }

    @Override
    @CheckForNull
    public Void visitMock(Void in)
    {
        MockFieldHelper.addGetterSetterDeclaration(fieldName, computeFieldName(fieldName), fieldName, boolean.class, getJclass());
        return null;
    }

    private String computeFieldName(String fieldName)
    {
        return fieldName + "AsBoolean";
    }

    @Override
    protected String getNameExtension()
    {
        return "AsBoolean";
    }

}
