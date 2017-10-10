package org.caoym.jjvm.opcode;

import com.sun.tools.classfile.*;
import org.caoym.jjvm.lang.*;
import org.caoym.jjvm.runtime.Env;

import java.io.IOException;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * 字节码定义的 Java 类( 区别于 native 类 )
 */
public class JvmOpcodeClass implements JvmClass {

    private final ClassFile classFile;
    private final String className;
    private final JvmClassLoader classLoader;
    private Map<Map.Entry<String, String>, JvmOpcodeMethod> methods = new HashMap<>();
    private Map<String, JvmField> fields = new HashMap<>();
    /**
     *  是否已经初始化
     */
    boolean inited = false;

    static public JvmOpcodeClass read(JvmClassLoader classLoader, Path path) throws ClassNotFoundException {
        try {
            return  new JvmOpcodeClass(classLoader, ClassFile.read(path));
        } catch (IOException e) {
            throw new ClassNotFoundException(e.toString());
        } catch (Exception e) {
            throw new InternalError(e);
        }
    }
    /**
     * @param classLoader
     * @param classFile
     * @throws ConstantPoolException
     */
    private JvmOpcodeClass(JvmClassLoader classLoader, ClassFile classFile) throws ConstantPoolException, Descriptor.InvalidDescriptor {
        this.classFile = classFile;
        this.className = classFile.getName();
        this.classLoader = classLoader;
        for (Method method : classFile.methods) {
            String name = method.getName(classFile.constant_pool);
            String desc = method.descriptor.getValue(classFile.constant_pool);
            methods.put(new AbstractMap.SimpleEntry<>(name, desc), new JvmOpcodeMethod(this, method));
        }
        //准备阶段
        prepare();
    }

    /**
     * 准备阶段（Preparation）
     * 分配静态变量，并初始化为默认值，但不会执行任何字节码，在初始化阶段（clinit) 会有显式的初始化器来初始化这些静态字段，所以准备阶段不做
     * 这些事情。
     * @see `http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-5.html#jvms-5.4.2`
     */
    private void prepare() throws ConstantPoolException, Descriptor.InvalidDescriptor {
        for(Field i : this.classFile.fields){
            if(i.access_flags.is(AccessFlags.ACC_STATIC)){
                fields.put(i.getName(classFile.constant_pool), new JvmOpcodeStaticField(this, i));
            }else{
                fields.put(i.getName(classFile.constant_pool), new JvmOpcodeObjectField(this, i));
            }
        }
    }

    /**
     * 初始化阶段（Initialization）
     * 调用类的<clinit>方法（如果有）
     * @see `http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-5.html#jvms-5.5`
     */
    public void clinit(Env env) throws Exception {
        if(inited){
            return;
        }
        synchronized(this){
            if(inited){
                return;
            }
            inited = true;
            JvmOpcodeMethod method = methods.get(new AbstractMap.SimpleEntry<>("<clinit>", "()V"));
            if(method != null){
                method.call(env, null);
            }
        }
    }

    @Override
    public JvmObject newInstance(Env env) throws InstantiationException, IllegalAccessException {
        try {
            clinit(env);
        } catch (Exception e) {
            throw new InstantiationException(e.getMessage());
        }
        return new JvmOpcodeObject(env, this);
    }

    @Override
    public JvmMethod getMethod(String name, String desc) throws NoSuchMethodException {
        JvmOpcodeMethod method = methods.get(new AbstractMap.SimpleEntry<>(name, desc));
        if(method == null){
            throw new NoSuchMethodException("method "+name+":"+ desc+" not exist");
        }
        return method;
    }

    @Override
    public boolean hasMethod(String name, String desc) {
        JvmOpcodeMethod method = methods.get(new AbstractMap.SimpleEntry<>(name, desc));
        return method != null;
    }

    @Override
    public JvmField getField(String name) throws NoSuchFieldException, IllegalAccessException {
        JvmField field = fields.get(name);
        if(field == null){
            throw new NoSuchFieldException("field "+name+" of "+ className+" not exist");
        }
        return field;
    }

    @Override
    public JvmClassLoader getClassLoader() {
        return classLoader;
    }

    @Override
    public JvmClass getSuperClass() throws ClassNotFoundException {
        try {
            return classLoader.loadClass(classFile.getSuperclassName());
        } catch (ConstantPoolException e) {
            throw new ClassNotFoundException(e.getMessage());
        }
    }

    @Override
    public String getName() {
        return className;
    }

    public ClassFile getClassFile() {
        return classFile;
    }
}
