package org.caoym.jjvm.lang;

import com.sun.tools.classfile.ClassFile;
import com.sun.tools.classfile.ConstantPoolException;
import com.sun.tools.classfile.Descriptor;
import com.sun.tools.classfile.Field;

/**
 * 静态成员
 */
public class JvmOpcodeStaticField {
    private final Field field;
    private final ClassFile classFile;
    private Object value;
    private final String type;
    public JvmOpcodeStaticField(ClassFile classFile, Field field) throws Descriptor.InvalidDescriptor, ConstantPoolException {
        this.field = field;
        this.classFile = classFile;
        type = field.descriptor.getFieldType(this.classFile.constant_pool);
        //初始化为默认值
        switch (type){
            case "byte":
                value = (byte)0;
                break;
            case "char":
                value = '\u0000';
                break;
            case "double":
                value = 0.;
                break;
            case "float":
                value = 0.f;
                break;
            case "int":
                value = 0;
                break;
            case "long":
                value = 0L;
                break;
            case "short":
                value = (short)0;
                break;
            case "value":
                value = false;
                break;
            default:
                value = null;
                break;
        }
    }

    public Object getValue() {
        return value;
    }

    public void putValue(Object value) {
        this.value = value;
    }
}
