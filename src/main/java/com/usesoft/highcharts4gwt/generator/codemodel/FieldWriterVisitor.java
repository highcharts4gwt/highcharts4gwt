package com.usesoft.highcharts4gwt.generator.codemodel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.usesoft.highcharts4gwt.generator.codemodel.field.FieldArrayJsonObjectWriter;
import com.usesoft.highcharts4gwt.generator.codemodel.field.FieldArrayNumberWriter;
import com.usesoft.highcharts4gwt.generator.codemodel.field.FieldArrayObjectWriter;
import com.usesoft.highcharts4gwt.generator.codemodel.field.FieldArrayStringWriter;
import com.usesoft.highcharts4gwt.generator.codemodel.field.FieldBooleanWriter;
import com.usesoft.highcharts4gwt.generator.codemodel.field.FieldDataWriter;
import com.usesoft.highcharts4gwt.generator.codemodel.field.FieldJsonObjectWriter;
import com.usesoft.highcharts4gwt.generator.codemodel.field.FieldNumberWriter;
import com.usesoft.highcharts4gwt.generator.codemodel.field.FieldObjectWriter;
import com.usesoft.highcharts4gwt.generator.codemodel.field.FieldStringWriter;
import com.usesoft.highcharts4gwt.generator.codemodel.field.FieldTypeVisitor;
import com.usesoft.highcharts4gwt.generator.graph.Option;

public class FieldWriterVisitor implements FieldTypeVisitor<OutputType, Void>
{
    private static final Logger logger = LoggerFactory.getLogger(FieldWriterVisitor.class);

    private final String fieldName;
    private final JCodeModel codeModel;
    private final JDefinedClass jClass;
    private final String className;
    private final Option option;

    private final boolean pipe;

    public FieldWriterVisitor(Option optionSpec, JCodeModel codeModel, JDefinedClass jClass, String className, boolean pipe)
    {
        this.option = optionSpec;
        this.pipe = pipe;
        this.fieldName = optionSpec.getTitle();
        this.codeModel = codeModel;
        this.jClass = jClass;
        this.className = className;
    }

    @Override
    public Void visitNumber(OutputType in)
    {
        String defaultValue = option.getDefaults();

        return in.accept(new FieldNumberWriter(codeModel, jClass, className, defaultValue, pipe, fieldName), null);
    }

    @Override
    public Void visitBoolean(OutputType in)
    {
        return in.accept(new FieldBooleanWriter(codeModel, jClass, className, Boolean.parseBoolean(option.getDefaults()), pipe, fieldName), null);
    }

    @Override
    public Void visitString(OutputType in)
    {
        return in.accept(new FieldStringWriter(codeModel, jClass, className, option.getDefaults(), pipe, fieldName), null);
    }

    @Override
    public Void visitOther(OutputType in)
    {
        return null;
    }

    @Override
    public Void visitArrayString(OutputType in)
    {
        return in.accept(new FieldArrayStringWriter(codeModel, jClass, className, option.getDefaults(), pipe, fieldName), null);
    }

    @Override
    public Void visitArrayNumber(OutputType in)
    {
        return in.accept(new FieldArrayNumberWriter(codeModel, jClass, className, option.getDefaults(), pipe, fieldName), null);
    }

    @Override
    public Void visitArrayObject(OutputType in)
    {
        return in.accept(new FieldArrayObjectWriter(codeModel, jClass, className, option, pipe, fieldName), null);
    }

    @Override
    public Void visitClass(OutputType in)
    {
        return in.accept(new FieldObjectWriter(codeModel, jClass, className, option, pipe, fieldName), null);
    }

    @Override
    public Void visitData(OutputType in)
    {
        return in.accept(new FieldDataWriter(codeModel, jClass, className, option.getDefaults(), pipe, fieldName), null);
    }

    @Override
    public Void visitJsonObject(OutputType in)
    {
        // TODO should precise that this is an "object" not a string value
        return in.accept(new FieldJsonObjectWriter(codeModel, jClass, className, option.getDefaults(), pipe, fieldName), null);
    }

    @Override
    public Void visitCssObject(OutputType in)
    {
        // TODO should precise that this is an "object" not a string value
        return in.accept(new FieldJsonObjectWriter(codeModel, jClass, className, option.getDefaults(), pipe, fieldName), null);
    }

    @Override
    public Void visitArrayJsonObject(OutputType in)
    {
        return in.accept(new FieldArrayJsonObjectWriter(codeModel, className, jClass, option, pipe, fieldName), null);
    }
}
