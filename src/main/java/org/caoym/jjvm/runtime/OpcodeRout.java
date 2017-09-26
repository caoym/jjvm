package org.caoym.jjvm.runtime;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.tools.classfile.AccessFlags;
import com.sun.tools.classfile.ConstantPool;
import com.sun.tools.classfile.ConstantPoolException;
import org.caoym.jjvm.JvmClass;
import org.caoym.jjvm.JvmMethod;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by caoyangmin on 2017/9/26.
 */
public enum OpcodeRout {

    /**
     * 将第一个引用类型局部变量推送至栈顶
     */
    ALOAD_0(Constants.ALOAD_0){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getOperandStack().push(frame.getLocalVariables().get(0), 1);
        }
    },
    /**
     * 从当前方法返回 void
     */
    RETURN(Constants.RETURN){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.setReturn(null, "void");
        }
    },
    /**
     * 获取对象的静态字段值
     */
    GETSTATIC(Constants.GETSTATIC){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            int arg = (operands[0]<<4)|operands[1];
            ConstantPool.CONSTANT_Fieldref_info info
                    = (ConstantPool.CONSTANT_Fieldref_info)frame.getConstantPool().get(arg);
            //静态字段所在的类
            JvmClass clazz = env.getVm().findClass(info.getClassName());
            //静态字段的值
            Object value = clazz.getField(
                    info.getNameAndTypeInfo().getName(),
                    info.getNameAndTypeInfo().getType(),
                    AccessFlags.ACC_STATIC
            );

            frame.getOperandStack().push(value, 1);
        }
    },
    /**
     * 调用超类构造方法，实例初始化方法，私有方法。
     */
    INVOKESPECIAL(Constants.INVOKESPECIAL){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            throw new InternalError("The opcode invokespecial Not Impl");
        }
    },
    /**
     *  将 int，float 或 String 型常量值从常量池中推送至栈顶
     */
    LDC(Constants.LDC){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            int arg = operands[0];
            ConstantPool.CPInfo info = frame.getConstantPool().get(arg);
            frame.getOperandStack().push(asObject(info), 1);
        }
    },
    /**
     * 调用实例方法
     */
    INVOKEVIRTUAL(Constants.INVOKEVIRTUAL){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            int arg = (operands[0]<<4)|operands[1];

            ConstantPool.CONSTANT_Methodref_info info
                    = (ConstantPool.CONSTANT_Methodref_info)frame.getConstantPool().get(arg);

            String className = info.getClassName();
            String name = info.getNameAndTypeInfo().getName();
            String type = info.getNameAndTypeInfo().getType();

            JvmClass clazz  = env.getVm().findClass(className);
            JvmMethod method = clazz.getMethod(name, type, 0);

            //从操作数栈中推出方法的参数
            Object args[] = frame.getOperandStack().dumpAll();
            method.call(env, args[0], Arrays.copyOfRange(args,1, args.length));
        }
    },
    /**
     * 将 double 型 0 推送至栈顶
     */
    DCONST_0(Constants.DCONST_0){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getOperandStack().push(0.0,2);
        }
    },
    /**
     * 将栈顶 double 型数值存入第二个局部变量。
     */
    DSTORE_1(Constants.DSTORE_1){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            Object var = frame.getOperandStack().pop();
            frame.getLocalVariables().set(1, var, 2);
        }
    },
    /**
     * 将第二个 double 型局部变量推送至栈顶。
     */
    DLOAD_1(Constants.DLOAD_1){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            Object var = frame.getLocalVariables().get(1);
            frame.getOperandStack().push(var, 2);
        }
    },

    //dconst_1: 将 double 型 1 推送至栈顶
    DCONST_1(Constants.DCONST_1){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getOperandStack().push(1.0, 2);
        }
    },
    /**
     * 将栈顶两 double 型数值相加并将结果压入栈顶
     */
    DADD(Constants.DADD){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            Double var1 = (Double) frame.getOperandStack().pop();
            Double var2 = (Double) frame.getOperandStack().pop();
            frame.getOperandStack().push(var1 + var2, 2);
        }
    },
    //iconst_1: 将 int 型 1 推送至栈顶
    ICONST_1(Constants.ICONST_1){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getOperandStack().push(1, 1);
        }
    },
    /**
     * 将栈顶 int 型数值存入第四个局部变量
     */
    ISTORE_3(Constants.ISTORE_3){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getLocalVariables().set(3, frame.getOperandStack().pop(), 1);
        }
    },
    /**
     * 将指定 int 型变量增加指定值。
     */
    IINC(Constants.IINC){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            Integer var = (Integer) frame.getLocalVariables().get(operands[0]);
            frame.getLocalVariables().set(operands[0], var + operands[1], 1);
        }
    },
    /**
     * 将第四个 int 型局部变量推送至栈顶。
     */
    ILOAD_3(Constants.ILOAD_3){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getOperandStack().push(frame.getLocalVariables().get(3), 1);
        }
    },
    /**
     * 将栈顶 int 型数值强制转换成 double 型数值并将结果压入栈顶
     */
    I2D(Constants.I2D){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            Integer var = (Integer) frame.getOperandStack().pop();
            frame.getOperandStack().push((double) var, 2);
        }
    };
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static Map<Short, OpcodeRout> codeMapping =  new HashMap<>();
    private final short code;

    OpcodeRout(short code){
        this.code = code;
    }

    public abstract void invoke(Env env, StackFrame frame, byte[] operands) throws Exception;

    public short getCode() {
        return code;
    }

    public static OpcodeRout valueOf(short code){
        OpcodeRout op = codeMapping.get(code);
        if(op == null){
            throw new InternalError("The opcode "+Constants.OPCODE_NAMES[code]+" Not Impl");
        }
        return op;
    }

    static private Object asObject(ConstantPool.CPInfo info) throws ConstantPoolException {
        switch (info.getTag()){
            case ConstantPool.CONSTANT_Integer:
                return ((ConstantPool.CONSTANT_Integer_info) info).value;
            case ConstantPool.CONSTANT_Float:
                return ((ConstantPool.CONSTANT_Float_info) info).value;
            case ConstantPool.CONSTANT_String:
                return ((ConstantPool.CONSTANT_String_info)info).getString();
            default:
                throw new InternalError("unknown type: "+info.getTag());
        }
    }

    static {
        for (OpcodeRout op: values()) {
            codeMapping.put(op.getCode(), op);
        }
    }

}
