package com.github.highcharts4gwt.generator.option.field;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.highcharts4gwt.generator.common.ClassRegistry;
import com.github.highcharts4gwt.generator.common.OutputType;
import com.github.highcharts4gwt.generator.option.Option;
import com.sun.codemodel.ClassType;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JDocComment;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;

public class InterfaceFieldHelper
{
    private static final Logger logger = LoggerFactory.getLogger(InterfaceFieldHelper.class);

    private InterfaceFieldHelper()
    {
    }
    
    public static void addGetterSetterDeclaration(Names names, Class<?> type, JDefinedClass jDefinedClass)
    {
        addGetterDeclaration(names, type, jDefinedClass);

        addSetterDeclaration(names, type, jDefinedClass);
    }

    private static void addSetterDeclaration(Names names, Class<?> paramType, JDefinedClass setterReturnType)
    {
        JMethod method = setterReturnType.method(JMod.NONE, setterReturnType, names.getSetterName());
        method.param(paramType, names.getParamName());
        JDocComment javadoc = method.javadoc();
        javadoc.append(names.getJavadoc());
    }
   
    private static void addSetterDeclaration(Names names, JDefinedClass paramType, JDefinedClass setterReturnType)
    {
        JMethod method = setterReturnType.method(JMod.NONE, setterReturnType, names.getSetterName());
        method.param(paramType, names.getParamName());
        JDocComment javadoc = method.javadoc();
        javadoc.append(names.getJavadoc());
    }

    public static void addGetterDeclaration(Names names, Class<?> type, JDefinedClass jDefinedClass)
    {
        JMethod method = jDefinedClass.method(JMod.NONE, type, names.getGetterName());
        JDocComment javadoc = method.javadoc();
        javadoc.append(names.getJavadoc());
    }

    public static void addGetterSetterDeclaration(Names names, JClass type, JDefinedClass jDefinedClass)
    {
        addGetterDeclaration(names, type, jDefinedClass);

        JMethod method = jDefinedClass.method(JMod.NONE, jDefinedClass, names.getSetterName());
        method.param(type, names.getParamName());
        
        JDocComment javadoc = method.javadoc();
        javadoc.append(names.getJavadoc());
    }

    public static void addGetterDeclaration(Names names, JClass type, JDefinedClass jDefinedClass)
    {
        JMethod method = jDefinedClass.method(JMod.NONE, type, names.getGetterName());
        JDocComment javadoc = method.javadoc();
        javadoc.append(names.getJavadoc());
    }

    public static void createEventInterface(Option option, String packageName, String rootDirectoryPathName)
    {
        JCodeModel model = new JCodeModel();

        try
        {
            JDefinedClass jClass = model._class(packageName + "." + EventHelper.getEventNamePrefix(option) + EventHelper.EVENT_SUFFIX, ClassType.INTERFACE);
            // jClass._extends(NativeEvent.class);

            // TODO this logic should not be in an helper, pb it is duplicated
            // inside all helper, should have a common algo
            // write getter for Series / Point / Chart / Axis (context) inside
            // event
            if (option.getContext() != null)
                EventHelper.getType(option).accept(new EventGetterWriterVisitor(option, jClass, model), OutputType.Interface);

            ClassRegistry.INSTANCE.put(option, OutputType.Interface, jClass);

        }
        catch (JClassAlreadyExistsException e)
        {
            throw new RuntimeException(e);
        }

        try
        {
            model.build(new File(rootDirectoryPathName));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

    }

    public static JDefinedClass createEventHandlerInterface(Option option, String packageName, String rootDirectoryPathName)
    {
        JCodeModel model = new JCodeModel();

        JDefinedClass jClass = null;

        try
        {
            String eventName = EventHelper.getEventNamePrefix(option);
            String handlerName = eventName + EventHelper.HANDLER_SUFFIX;
            String fullyqualifiedName = packageName + "." + handlerName;
            jClass = model._class(fullyqualifiedName, ClassType.INTERFACE);

            JClass eventClass = ClassRegistry.INSTANCE.getRegistry().get(new ClassRegistry.RegistryKey(option, OutputType.Interface));

            jClass.method(JMod.NONE, void.class, EventHelper.ON_PREFIX + eventName).param(eventClass, EventHelper.paramName(eventName));

            EventRegistry.INSTANCE.put(EventHelper.getRegistryKey(option), jClass);
            logger.info("Handler created;" + handlerName);

        }
        catch (JClassAlreadyExistsException e)
        {
            throw new RuntimeException(e);
        }

        try
        {
            model.build(new File(rootDirectoryPathName));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        return jClass;

    }

    public static void addEventHandlerRegistrationMethods(Option option, JDefinedClass jClass)
    {

        List<JClass> list = EventRegistry.INSTANCE.getRegistry().get(option.getFullname());
        if (list != null)
        {
            for (JClass handlerClass : list)
            {
                String handlerClassName = handlerClass.name();
                String paramName = handlerClassName.substring(0, 1).toLowerCase() + handlerClassName.substring(1);
                jClass.method(JMod.NONE, void.class, EventHelper.ADD_HANDLER_METHOD_PREFIX + handlerClassName).param(handlerClass, paramName);
            }
        }

    }

    public static void addJsonObjectGetterSetterDeclaration(JDefinedClass jDefinedClass)
    {
        //getFieldAsJsonObject(String fieldName);
        jDefinedClass.method(JMod.NONE, String.class, "getFieldAsJsonObject").param(String.class, "fieldName");
        
        //setFieldAsJsonObject(String fieldName, String fieldValueAsJonObject);
        JMethod setter = jDefinedClass.method(JMod.NONE, jDefinedClass, "setFieldAsJsonObject");
        setter.param(String.class, "fieldName");
        setter.param(String.class, "fieldValueAsJonObject");
    }

    public static void addFunctionAsStringGetterSetterDeclaration(JDefinedClass jDefinedClass)
    {
        jDefinedClass.method(JMod.NONE, String.class, "getFunctionAsString").param(String.class, "fieldName");
        
        JMethod setter = jDefinedClass.method(JMod.NONE, jDefinedClass, "setFunctionAsString");
        setter.param(String.class, "fieldName");
        setter.param(String.class, "functionAsString");
    }
    
    public static void addFunctionSetterDeclaration(JDefinedClass jClass, JDefinedClass callbackClass, Names names)
    {
        addSetterDeclaration(names, callbackClass, jClass);
    }

    public static JDefinedClass createFunctionCallbackInterface(Option option, String packageName, String rootDirectoryPathName)
    {
        JCodeModel model = new JCodeModel();

        JDefinedClass jClass = null;

        try
        {
            String fqn = FunctionHelper.getFunctionCallbackFqn(option, packageName);
            
            JClass contextObject = FunctionHelper.getContextObject(option, OutputType.Interface);
            
            jClass = model._class(fqn, ClassType.INTERFACE);

            
            JMethod method = jClass.method(JMod.NONE, Object.class, EventHelper.ON_PREFIX + "Callback");
            
            if (contextObject != null)
                method.param(contextObject, option.getContext().substring(0, 1).toLowerCase() + option.getContext().substring(1));
            
            ClassRegistry.INSTANCE.getRegistry().put(new ClassRegistry.RegistryKey(option, OutputType.Interface), jClass);
            
            logger.info("Function callback created;" + option.getTitle());

        }
        catch (JClassAlreadyExistsException e)
        {
            throw new RuntimeException(e);
        }

        try
        {
            model.build(new File(rootDirectoryPathName));
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        return jClass;

    }
}
