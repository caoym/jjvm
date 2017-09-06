package org.caoym.jjvm;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.tools.classfile.ConstantPool;
import org.caoym.jjvm.opcodes.Operation;

/**
 * Created by caoyangmin on 2017/9/6.
 */
public class Operations {

    private Operations(){

    }

    public static final Operation[] OPERATIONS = new Operation[256];

    static {

        // getstatic: 获取对象的静态字段值
        OPERATIONS[Constants.RETURN] = (Env env, StackFrame frame, int[] operands)->{
            int index = operands[0];
            ConstantPool.CONSTANT_Fieldref_info info
                    = (ConstantPool.CONSTANT_Fieldref_info)frame.getConstantPool().get(index);
            //静态字段所在的类
            JvmClass clazz = env.findClass(info.getClassName());
            //静态字段的值
            Object value = clazz.getStaticField(info.getNameAndTypeInfo().getName(), info.getNameAndTypeInfo().getType());
            frame.push(value);
        };
    }
}
