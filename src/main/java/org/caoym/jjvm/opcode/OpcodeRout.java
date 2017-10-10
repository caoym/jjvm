package org.caoym.jjvm.opcode;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.tools.classfile.ConstantPool;
import com.sun.tools.classfile.ConstantPoolException;
import org.caoym.jjvm.lang.JvmClass;
import org.caoym.jjvm.lang.JvmField;
import org.caoym.jjvm.lang.JvmMethod;
import org.caoym.jjvm.lang.JvmObject;
import org.caoym.jjvm.runtime.Env;
import org.caoym.jjvm.runtime.StackFrame;

import java.util.*;

/**
 * 操作数例程
 */
public enum OpcodeRout {

    /**
     * 将第0个引用类型局部变量推送至栈顶
     */
    ALOAD_0(Constants.ALOAD_0){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            Object object = frame.getLocalVariables().get(0);
            frame.getOperandStack().push(object, 1);
        }
    },
    /**
     * 将第1个引用类型局部变量推送至栈顶
     */
    ALOAD_1(Constants.ALOAD_1){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getOperandStack().push(frame.getLocalVariables().get(1), 1);
        }
    },
    /**
     * 将第2个引用类型局部变量推送至栈顶
     */
    ALOAD_2(Constants.ALOAD_2){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getOperandStack().push(frame.getLocalVariables().get(2), 1);
        }
    },
    /**
     * 将第3个引用类型局部变量推送至栈顶
     */
    ALOAD_3(Constants.ALOAD_3){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getOperandStack().push(frame.getLocalVariables().get(3), 1);
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
            int index = (operands[0]<<8)|operands[1];
            ConstantPool.CONSTANT_Fieldref_info info
                    = (ConstantPool.CONSTANT_Fieldref_info)frame.getConstantPool().get(index);
            //静态字段所在的类
            JvmClass clazz = env.getVm().getClass(info.getClassName());
            JvmField field = clazz.getField(info.getNameAndTypeInfo().getName());
            //静态字段的值
            Object value = field.get(env,null);
            frame.getOperandStack().push(value, 1);
        }
    },
    /**
     * 调用超类构造方法，实例初始化方法，私有方法。
     */
    INVOKESPECIAL(Constants.INVOKESPECIAL){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            int arg = (operands[0]<<8)|operands[1];

            ConstantPool.CONSTANT_Methodref_info info
                    = (ConstantPool.CONSTANT_Methodref_info)frame.getConstantPool().get(arg);

            JvmClass clazz  = env.getVm().getClass(info.getClassName());
            JvmMethod method = clazz.getMethod(
                    info.getNameAndTypeInfo().getName(),
                    info.getNameAndTypeInfo().getType()
            );
            //从操作数栈中推出方法的参数
            ArrayList<Object> args = frame.getOperandStack().multiPop(method.getParameterCount() + 1);
            Collections.reverse(args);
            Object[] argsArr = args.toArray();
            JvmObject thiz = (JvmObject) argsArr[0];

            //根据类名确定是调用父类还是子类
            while (!thiz.getClazz().getName().equals(clazz.getName())){
                thiz = thiz.getSuper();
            }
            method.call(env, thiz, Arrays.copyOfRange(argsArr,1, argsArr.length));
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
            INVOKESPECIAL.invoke(env, frame, operands);
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
    /**
     * 将 int 型 0 推送至栈顶
     */
    ICONST_0(Constants.ICONST_0){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getOperandStack().push(0,1);
        }
    },
    //iconst_1: 将 int 型 1 推送至栈顶
    ICONST_1(Constants.ICONST_1){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getOperandStack().push(1, 1);
        }
    },
    //iconst_2: 将 int 型 2 推送至栈顶
    ICONST_2(Constants.ICONST_2){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getOperandStack().push(2, 1);
        }
    },
    //iconst_3: 将 int 型 3 推送至栈顶
    ICONST_3(Constants.ICONST_3){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getOperandStack().push(3, 1);
        }
    },
    //iconst_4: 将 int 型 4 推送至栈顶
    ICONST_4(Constants.ICONST_4){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getOperandStack().push(4, 1);
        }
    },
    //iconst_5: 将 int 型 5 推送至栈顶
    ICONST_5(Constants.ICONST_5){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getOperandStack().push(5, 1);
        }
    },
    /**
     * 将栈顶 int 型数值存入第0个局部变量
     */
    ISTORE_0(Constants.ISTORE_0){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getLocalVariables().set(0, frame.getOperandStack().pop(), 1);
        }
    },
    /**
     * 将栈顶 int 型数值存入第1个局部变量
     */
    ISTORE_1(Constants.ISTORE_1){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getLocalVariables().set(1, frame.getOperandStack().pop(), 1);
        }
    },
    /**
     * 将栈顶 int 型数值存入第2个局部变量
     */
    ISTORE_2(Constants.ISTORE_2){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getLocalVariables().set(2, frame.getOperandStack().pop(), 1);
        }
    },
    /**
     * 将栈顶 int 型数值存入第3个局部变量
     */
    ISTORE_3(Constants.ISTORE_3){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getLocalVariables().set(3, frame.getOperandStack().pop(), 1);
        }
    },
    /**
     * 将第0个 int 型局部变量推送至栈顶。
     */
    ILOAD_0(Constants.ILOAD_0){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getOperandStack().push(frame.getLocalVariables().get(0), 1);
        }
    },
    /**
     * 将第1个 int 型局部变量推送至栈顶。
     */
    ILOAD_1(Constants.ILOAD_1){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getOperandStack().push(frame.getLocalVariables().get(1), 1);
        }
    },
    /**
     * 将第2个 int 型局部变量推送至栈顶。
     */
    ILOAD_2(Constants.ILOAD_2){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getOperandStack().push(frame.getLocalVariables().get(2), 1);
        }
    },
    /**
     * 将第3个 int 型局部变量推送至栈顶。
     */
    ILOAD_3(Constants.ILOAD_3){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getOperandStack().push(frame.getLocalVariables().get(3), 1);
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
     * 将栈顶 int 型数值强制转换成 double 型数值并将结果压入栈顶
     */
    I2D(Constants.I2D){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            Integer var = (Integer) frame.getOperandStack().pop();
            frame.getOperandStack().push((double) var, 2);
        }
    },
    /**
     * 从数组中加载一个 reference 类型数据到操作数栈
     */
    AALOAD(Constants.AALOAD){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            int index = (int) frame.getOperandStack().pop();
            Object[] arrayRef = (Object[]) frame.getOperandStack().pop();
            frame.getOperandStack().push(arrayRef[index]);
        }
    },
    /**
     * 创建一个对象，并将其引用值压入栈顶。
     */
    NEW(Constants.NEW){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            int index = (operands[0] << 8)| operands[1];
            ConstantPool.CONSTANT_Class_info info
                    = (ConstantPool.CONSTANT_Class_info)frame.getConstantPool().get(index);
            JvmClass clazz = env.getVm().getClass(info.getName());
            frame.getOperandStack().push(clazz.newInstance(env));
        }
    },
    /**
     * 复制栈顶数值并将复制值压入栈顶。
     */
    DUP(Constants.DUP){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            frame.getOperandStack().push(frame.getOperandStack().pick(), frame.getOperandStack().getEndSize());
        }
    },
    /**
     * 为指定的类的静态域赋值。
     */
    PUTSTATIC(Constants.PUTSTATIC){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            Object var = frame.getOperandStack().pop();
            int index = (operands[0] << 8)| operands[1];
            ConstantPool.CONSTANT_Fieldref_info info
                    = (ConstantPool.CONSTANT_Fieldref_info)frame.getConstantPool().get(index);

            JvmClass clazz = env.getVm().getClass(info.getClassName());
            JvmField field = clazz.getField(info.getNameAndTypeInfo().getName());
            field.set(env, null, var);
        }
    },
    /**
     * 为指定的类的实例域赋值
     */
    PUTFIELD(Constants.PUTFIELD){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            Object value = frame.getOperandStack().pop();
            Object objectref = frame.getOperandStack().pop();

            int index = (operands[0] << 8)| operands[1];
            ConstantPool.CONSTANT_Fieldref_info info
                    = (ConstantPool.CONSTANT_Fieldref_info)frame.getConstantPool().get(index);
            JvmClass clazz = env.getVm().getClass(info.getClassName());
            JvmField field = clazz.getField(info.getNameAndTypeInfo().getName());
            assert field != null;
            field.set(env,objectref, value);
        }
    },
    /**
     * 获取指定类的实例域，并将其值压入栈顶
     */
    GETFIELD(Constants.GETFIELD){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {
            Object objectref = frame.getOperandStack().pop();

            int index = (operands[0] << 8)| operands[1];
            ConstantPool.CONSTANT_Fieldref_info info
                    = (ConstantPool.CONSTANT_Fieldref_info)frame.getConstantPool().get(index);
            JvmClass clazz = env.getVm().getClass(info.getClassName());
            JvmField field = clazz.getField(info.getNameAndTypeInfo().getName());
            frame.getOperandStack().push(field.get(env, objectref));
        }
    },
    /**
     * 调用接口方法
     */
    INVOKEINTERFACE(Constants.INVOKEINTERFACE){
        @Override
        public void invoke(Env env, StackFrame frame, byte[] operands) throws Exception {

            // 获取接口和方法信息
            int arg = (operands[0]<<8)|operands[1];

            ConstantPool.CONSTANT_InterfaceMethodref_info info
                    = (ConstantPool.CONSTANT_InterfaceMethodref_info)frame.getConstantPool().get(arg);

            String interfaceName = info.getClassName();
            String name = info.getNameAndTypeInfo().getName();
            String type = info.getNameAndTypeInfo().getType();

            // 获取接口的参数数量
            int count = 0xff&operands[2]; //TODO count代表参数个数，还是参数所占的槽位数？
            //从操作数栈中推出方法的参数
            ArrayList<Object> args = frame.getOperandStack().multiPop(count + 1);
            Collections.reverse(args);
            Object[] argsArr = args.toArray();

            JvmObject thiz = (JvmObject)argsArr[0];
            JvmMethod method = null;
            //递归搜索接口方法
            while(thiz != null){
                if(thiz.getClazz().hasMethod(name, type)){
                    method = thiz.getClazz().getMethod(name, type);
                    break;
                }else{
                    thiz = thiz.getSuper();
                }
            }
            if(method == null){
                throw new AbstractMethodError(info.toString());
            }
            // 执行接口方法
            method.call(env, thiz, Arrays.copyOfRange(argsArr,1, argsArr.length));
        }
    }
    ;
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
        Object res = null;
        switch (info.getTag()){
            case ConstantPool.CONSTANT_Integer:
                res = ((ConstantPool.CONSTANT_Integer_info) info).value;
                break;
            case ConstantPool.CONSTANT_Float:
                res = ((ConstantPool.CONSTANT_Float_info) info).value;
                break;
            case ConstantPool.CONSTANT_String:
                res = ((ConstantPool.CONSTANT_String_info)info).getString();
                break;
            default:
                throw new InternalError("unknown type: "+info.getTag());
        }
        return res;
    }

    static {
        for (OpcodeRout op: values()) {
            codeMapping.put(op.getCode(), op);
        }
    }

}
