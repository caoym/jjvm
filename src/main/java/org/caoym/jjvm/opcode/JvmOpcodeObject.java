package org.caoym.jjvm.opcode;

import org.caoym.jjvm.lang.JvmClass;
import org.caoym.jjvm.lang.JvmObject;
import org.caoym.jjvm.runtime.Env;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class JvmOpcodeObject implements JvmObject{

    private final JvmOpcodeClass clazz;
    private final Map<String, Object> fields = new HashMap<>();
    /**
     * 父类的实例，即通过 super 访问到的实例
     */
    private final JvmObject superObject;
    public JvmOpcodeObject(Env env, JvmOpcodeClass clazz) throws IllegalAccessException, InstantiationException {
        this.clazz = clazz;
        JvmClass superClass = null;
        try {
            superClass = clazz.getSuperClass();
        } catch (ClassNotFoundException e) {
            throw new InstantiationException(e.getMessage());
        }
        superObject = superClass.newInstance(env);
        //初始化成员变量
        for (Field field : superClass.getClass().getFields()) {
            Object value;
            //初始化为默认值
            switch (field.getType().getName()){
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
                case "boolean":
                    value = false;
                    break;
                default:
                    value = null;
                    break;
            }
            fields.put(field.getName(), value);
        }
    }
    public void setField(String name, Object value) {
        fields.put(name, value);
    }

    public Object getField(String name) {
        return fields.get(name);
    }

    @Override
    public JvmObject getSuper() {
        return superObject;
    }

    @Override
    public JvmClass getClazz() {
        return clazz;
    }
}
