package com.github.highcharts4gwt.generator.option.klass;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.annotation.CheckForNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.highcharts4gwt.generator.common.ClassRegistry;
import com.github.highcharts4gwt.generator.common.OutputType;
import com.github.highcharts4gwt.generator.option.Option;
import com.github.highcharts4gwt.generator.option.OptionTree;
import com.github.highcharts4gwt.generator.option.OptionUtils;
import com.github.highcharts4gwt.generator.option.field.BaseFieldWriter;
import com.github.highcharts4gwt.generator.option.field.EventHelper;
import com.github.highcharts4gwt.generator.option.field.GenericFieldWriter;
import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;

public abstract class BaseClassWriter implements OptionClassWriter
{
    private static final Logger logger = LoggerFactory.getLogger(BaseClassWriter.class);

    @CheckForNull
    private JCodeModel codeModel;

    private final String rootDirectory;

    @CheckForNull
    private String packageName;

    @CheckForNull
    private JDefinedClass jClass;

    private String shortClassName;

    @CheckForNull
    private Option option;

    @CheckForNull
    private OptionTree tree;

    private final BaseFieldWriter fieldWriter;

    private GenericFieldWriter genericFieldWritter;

    public BaseClassWriter(String rootDirectory) throws JClassAlreadyExistsException
    {
        this.rootDirectory = rootDirectory;
        fieldWriter = new BaseFieldWriter();
        genericFieldWritter = new GenericFieldWriter();
    }

    @Override
    public void write() throws IOException, JClassAlreadyExistsException
    {
        JClass jclass = ClassRegistry.INSTANCE.getRegistry().get(new ClassRegistry.RegistryKey(option, getOutputType()));
        if (jclass != null)
        {
            logger.trace("Class already written;" + option);
            return;
        }

        codeModel = new JCodeModel();

        jClass = createJClass();

        if (tree == null)
            throw new RuntimeException("Need to set the tree to build a class");

        JDocComment javadoc = jClass.javadoc();
        javadoc.append(option.getDescription());

        addFieldsToJClass();

        addGenericSetterGetters();

        writeJClassToFileSystem();

        ClassRegistry.INSTANCE.put(option, getOutputType(), jClass);
    }

    private void addGenericSetterGetters()
    {
        genericFieldWritter.writeGenericGettersSetters(jClass, getOutputType());
    }

    private void writeJClassToFileSystem() throws IOException
    {
        if (codeModel != null) // CS
            codeModel.build(new File(rootDirectory));
    }

    private void addFieldsToJClass()
    {
        initFieldBuilder();

        if (tree == null || option == null)
            throw new RuntimeException("tree/option should not be null");

        List<Option> fieldToAdd = tree.getChildren(option);
        
        //TODO #series hack
        if (fieldToAdd != null) //series case
        {
            for (Option option : fieldToAdd)
            {
                //TODO #series hack
                if (option.getFullname().matches("series<\\w+>")) // do not write the series<area> etc. inside the chartOption class
                    continue;
                fieldWriter.writeField(option, getOutputType(), rootDirectory);
            }
        }
    }

    private void initFieldBuilder()
    {
        fieldWriter.setJclass(jClass);
        fieldWriter.setCodeModel(codeModel);
        fieldWriter.setShortClassNameWithPrefix(getPrefix() + shortClassName);
    }

    @Override
    public OptionClassWriter setPackageName(String packageName)
    {
        this.packageName = packageName;
        return this;
    }

    @Override
    public OptionClassWriter setOption(Option option)
    {
        this.option = option;
        this.shortClassName = OptionUtils.getShortClassName(option);
        return this;
    }

    @Override
    public OptionClassWriter setTree(OptionTree tree)
    {
        this.tree = tree;
        return this;
    }

    protected String getFullyQualifiedName()
    {
        String p = getPackageName();
        p = EventHelper.removeLtGt(p, false);

        String s = getShortClassName();
        s = EventHelper.removeLtGt(s, true);

        String fullyqualifiedName = p + "." + getPrefix() + s;

        return fullyqualifiedName;
    }

    protected abstract String getPrefix();

    protected abstract OutputType getOutputType();

    protected abstract JDefinedClass createJClass() throws JClassAlreadyExistsException;

    protected abstract ClassType getClassType();

    protected JCodeModel getCodeModel()
    {
        return codeModel;
    }

    protected Option getOption()
    {
        return option;
    }

    protected String getPackageName()
    {
        return packageName;
    }

    protected String getShortClassName()
    {
        return shortClassName;
    }
}
