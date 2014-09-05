package com.usesoft.highcharts4gwt.generator;

import com.sun.codemodel.JClassAlreadyExistsException;
import com.usesoft.highcharts4gwt.generator.codemodel.ClassBuilder;
import com.usesoft.highcharts4gwt.generator.codemodel.InterfaceClassBuilder;
import com.usesoft.highcharts4gwt.generator.codemodel.JsoClassBuilder;
import com.usesoft.highcharts4gwt.generator.codemodel.MockClassBuilder;
import com.usesoft.highcharts4gwt.generator.codemodel.OutputTypeVisitor;

public class ClassWritterVisitor implements OutputTypeVisitor<String, ClassBuilder>
{

    @Override
    public ClassBuilder visitInterface(String in)
    {
        try
        {
            return new InterfaceClassBuilder(in);
        }
        catch (JClassAlreadyExistsException e)
        {
            throw new RuntimeException("Cannot instantiate InterfaceClassBuilder");
        }
    }

    @Override
    public ClassBuilder visitJso(String in)
    {
        try
        {
            return new JsoClassBuilder(in);
        }
        catch (JClassAlreadyExistsException e)
        {
            throw new RuntimeException("Cannot instantiate JsoClassBuilder");
        }
    }

    @Override
    public ClassBuilder visitMock(String in)
    {
        try
        {
            return new MockClassBuilder(in);
        }
        catch (JClassAlreadyExistsException e)
        {
            throw new RuntimeException("Cannot instantiate MockClassBuilder");
        }
    }

}
