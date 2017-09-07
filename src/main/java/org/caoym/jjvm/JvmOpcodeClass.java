package org.caoym.jjvm;

import com.sun.tools.classfile.*;
import javafx.util.Pair;

import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * Created by caoyangmin on 2017/9/6.
 */
public class JvmClass {

    private ClassFile classFile;
    private HashMap< Pair<String,String>, JvmMethod> methods;

    static public JvmClass read(Path path) throws IOException, ConstantPoolException {
        return  new JvmClass(ClassFile.read(path));
    }

    /**
     *
     * @param classFile
     * @throws ConstantPoolException
     */
    private JvmClass(ClassFile classFile) throws ConstantPoolException {
        this.classFile = classFile;
        for (Method method : classFile.methods) {
            String name = method.getName(classFile.constant_pool);
            String desc = method.descriptor.getValue(classFile.constant_pool);
            methods.put(new Pair<>(name, desc), new JvmMethod(classFile, method));
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

    public JvmMethod getStaticMethod(String name, String desc) throws NoSuchMethodException, ConstantPoolException {
        JvmMethod method = methods.get(new Pair<>(name, desc));
        if(method == null){
            throw new NoSuchMethodException("method "+name+"#"+ desc+" not exist");
        }
        if(method.getAccessFlags().is(AccessFlags.ACC_STATIC|AccessFlags.ACC_PUBLIC)){
            throw new NoSuchMethodException("method "+name+"#"+ desc+" not a `public static` method");
        }
        return method;
    }

    public Object getStaticField(String name, String type) {

    }
}
