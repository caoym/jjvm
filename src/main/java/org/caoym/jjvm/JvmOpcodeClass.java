package org.caoym.jjvm;

import com.sun.tools.classfile.*;
import com.sun.tools.classfile.AccessFlags;
import com.sun.tools.classfile.Method;
import javafx.util.Pair;
import sun.jvm.hotspot.oops.*;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * 字节码定义的 Java 类( 区别于 native 类 )
 */
public class JvmOpcodeClass implements JvmClass{

    private ClassFile classFile;
    private HashMap< Pair<String,String>, JvmOpcodeMethod> methods;

    static public JvmOpcodeClass read(Path path) throws ClassNotFoundException {
        try {
            return  new JvmOpcodeClass(ClassFile.read(path));
        } catch (ConstantPoolException e) {
            throw new InternalError(e);
        } catch (IOException e) {
            throw new ClassNotFoundException(e.toString());
        }
    }
    /**
     *
     * @param classFile
     * @throws ConstantPoolException
     */
    private JvmOpcodeClass(ClassFile classFile) throws ConstantPoolException {
        this.classFile = classFile;
        for (Method method : classFile.methods) {
            String name = method.getName(classFile.constant_pool);
            String desc = method.descriptor.getValue(classFile.constant_pool);
            methods.put(new Pair<>(name, desc), new JvmOpcodeMethod(classFile, method));
        }
        //准备阶段
        prepare();
        //初始化阶段
        init();
    }

    /**
     * 准备阶段（Preparation）
     * 分配静态变量，并初始化为默认值，但不会执行任何字节码，在初始化阶段（init) 会有显式的初始化器来初始化这些静态字段，所以准备阶段不做
     * 这些事情。
     * @see `http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-5.html#jvms-5.4.2`
     */
    private void prepare(){

    }

    /**
     * 初始化阶段（Initialization）
     * 调用类的<clinit>方法（如果有）
     * @see `http://docs.oracle.com/javase/specs/jvms/se7/html/jvms-5.html#jvms-5.5`
     */
    private void init(){

    }
    @Override
    public JvmMethod getMethod(String name, String desc, int flags) throws NoSuchMethodException {
        JvmOpcodeMethod method = methods.get(new Pair<>(name, desc));
        if(method == null){
            throw new NoSuchMethodException("method "+name+"#"+ desc+" not exist");
        }
        if(method.getAccessFlags().is(flags)){
            throw new NoSuchMethodException("method "+name+"#"+ desc+" not a `public static` method");
        }
        return method;
    }

    @Override
    public Object getField(String name, String type, int flags) throws NoSuchFieldException {
        throw new InternalError("Not Impl");
    }
}
