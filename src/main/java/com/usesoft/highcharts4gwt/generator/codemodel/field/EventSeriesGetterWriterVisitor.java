package com.usesoft.highcharts4gwt.generator.codemodel.field;

import javax.annotation.CheckForNull;

import com.sun.codemodel.JBlock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;
import com.usesoft.highcharts4gwt.generator.codemodel.ClassRegistry;
import com.usesoft.highcharts4gwt.generator.codemodel.OutputType;
import com.usesoft.highcharts4gwt.generator.codemodel.OutputTypeVisitor;
import com.usesoft.highcharts4gwt.generator.codemodel.klass.NativeContentHack;
import com.usesoft.highcharts4gwt.generator.graph.Option;

public class EventSeriesGetterWriterVisitor implements OutputTypeVisitor<Void, Void>
{
    private final Option option;
    private final JDefinedClass jClass;
    private final JCodeModel jCodeModel;

    public EventSeriesGetterWriterVisitor(Option option, JDefinedClass jClass, JCodeModel jCodeModel)
    {
        this.option = option;
        this.jClass = jClass;
        this.jCodeModel = jCodeModel;
    }

    @Override
    @CheckForNull
    public Void visitInterface(Void in)
    {
        JClass series = ClassRegistry.INSTANCE.getRegistry().get(new ClassRegistry.RegistryKey(new Option("series", "", ""), OutputType.Interface));
        jClass.method(JMod.NONE, series, EventHelper.GET_SERIES_METHOD_NAME);
        return null;
    }

    @Override
    @CheckForNull
    public Void visitJso(Void in)
    {
        NativeContentHack getterContentHack = new NativeContentHack(jCodeModel, JsoFieldHelper.getJsniSeriesEventGetter());
        JClass series = ClassRegistry.INSTANCE.getRegistry().get(new ClassRegistry.RegistryKey(new Option("series", "", ""), OutputType.Interface));

        jClass.method(JMod.NATIVE + JMod.FINAL + JMod.PUBLIC, series, EventHelper.GET_SERIES_METHOD_NAME)._throws(getterContentHack);
        return null;
    }

    @Override
    @CheckForNull
    public Void visitMock(Void in)
    {
        String fieldName = "series";
        JClass series = ClassRegistry.INSTANCE.getRegistry().get(new ClassRegistry.RegistryKey(new Option(fieldName, "", ""), OutputType.Interface));

        JFieldVar field = jClass.field(JMod.PRIVATE, series, fieldName);

        JMethod getter = jClass.method(JMod.PUBLIC, series, "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
        JBlock block = getter.body();
        block._return(field);

        JMethod setter = jClass.method(JMod.PUBLIC, jClass, fieldName);
        JVar setterParam = setter.param(series, fieldName);

        setter.body().assign(JExpr._this().ref(field), setterParam)._return(JExpr._this());

        return null;
    }

}
