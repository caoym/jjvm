package org.caoym.jjvm.opcode;

import com.sun.tools.classfile.ConstantPoolException;
import com.sun.tools.classfile.Descriptor;
import com.sun.tools.classfile.Field;
import org.caoym.jjvm.lang.JvmField;
import org.caoym.jjvm.runtime.Env;

/**
 * 静态成员
 */
public class JvmOpcodeStaticField implements JvmField {
    private final Field field;
    private final JvmOpcodeClass clazz;
    private Object value;
    private final String type;
    public JvmOpcodeStaticField(JvmOpcodeClass clazz, Field field) throws Descriptor.InvalidDescriptor, ConstantPoolException {
        this.field = field;
        this.clazz = clazz;
        type = field.descriptor.getFieldType(this.clazz.getClassFile().constant_pool);
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

    @Override
    public void set(Env env, Object thiz, Object value) throws IllegalAccessException {
        this.value = value;
    }

    @Override
    public Object get(Env env, Object thiz) throws IllegalAccessException {
        return value;
    }
}
