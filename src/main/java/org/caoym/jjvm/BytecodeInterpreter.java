package org.caoym.jjvm;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.tools.classfile.AccessFlags;
import com.sun.tools.classfile.ConstantPool;
import com.sun.tools.classfile.ConstantPoolException;
import org.caoym.jjvm.opcodes.Opcode;
import org.caoym.jjvm.runtime.Env;
import org.caoym.jjvm.runtime.JvmStack;
import org.caoym.jjvm.runtime.StackFrame;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by caoyangmin on 2017/9/5.
 * 字节码解析器
 */
public class BytecodeInterpreter {

    interface OpcodeInterface{
        public void call(Env env, StackFrame frame, byte[] operands) throws Exception;
    }
    public static final OpcodeInterface[] OPCODES = new OpcodeInterface[256];

    //执行字节码
    public static void run(Env env) throws Exception {
        //只需要最外层调用执行栈上操作
        if(env.getStack().isRunning()){
            return;
        }
        StackFrame frame;
        JvmStack stack = env.getStack();
        stack.setRunning(true);

        while ((frame = stack.currentFrame()) != null){
            //如果栈帧被设置为返回，则将其返回值推入上一个栈帧的操作数栈
            if(frame.isReturned()){
                stack.popFrame();
                if(!"void".equals(frame.getReturnType())){
                    frame = stack.currentFrame();
                    if(frame != null){
                        frame.getOperandStack().push(frame.getReturn());
                    }
                }
                continue;
            }
            Opcode[] codes = frame.getOpcodes();
            int pc = frame.increasePC();
            codes[pc].call(env, frame);
        }
    }

    public static Opcode[] parseCodes(byte[] codes){
        ArrayList<Opcode> opcodes = new ArrayList<>();
        for(int i=0; i<codes.length; i++){
            int code = 0xff&codes[i];
            if(OPCODES[code] == null){
                throw new InternalError("The opcode "+Constants.OPCODE_NAMES[code]+" Not Impl");
            }
            short noOfOperands = Constants.NO_OF_OPERANDS[code];
            final OpcodeInterface opcode = OPCODES[code];
            byte[] operands = Arrays.copyOfRange(codes, i + 1, i + 1 + noOfOperands);
            opcodes.add( (Env env, StackFrame frame)->opcode.call(env, frame, operands) );
            i += noOfOperands;
        }
        return Arrays.copyOf(opcodes.toArray(), opcodes.size(), Opcode[].class);
    }
    enum ALOAD_0
    static {
        //aload_0: 将第一个引用类型局部变量推送至栈顶
        OPCODES[Constants.ALOAD_0] = (Env env, StackFrame frame, byte[] operands)->{
            frame.getOperandStack().push(frame.getLocalVariables().get(0), 1);
        };
        //return: 从当前方法返回 void。
        OPCODES[Constants.RETURN] = (Env env, StackFrame frame, byte[] operands)->{
            frame.setReturn(null, "void");
        };
        //getstatic: 获取对象的静态字段值
        OPCODES[Constants.GETSTATIC] = (Env env, StackFrame frame, byte[] operands)->{
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
        };

        //invokespecial: 调用超类构造方法，实例初始化方法，私有方法。
        OPCODES[Constants.INVOKESPECIAL] = (Env env, StackFrame frame, byte[] operands)->{
            throw new InternalError("The opcode invokespecial Not Impl");
        };
        //ldc: 将 int，float 或 String 型常量值从常量池中推送至栈顶
        OPCODES[Constants.LDC] = (Env env, StackFrame frame, byte[] operands)->{
            int arg = operands[0];
            ConstantPool.CPInfo info = frame.getConstantPool().get(arg);
            frame.getOperandStack().push(asObject(info), 1);
        };
        //invokevirtual: 调用实例方法
        OPCODES[Constants.INVOKEVIRTUAL] = (Env env, StackFrame frame, byte[] operands)->{
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

        };
        //dconst_0: 将 double 型 0 推送至栈顶
        OPCODES[Constants.DCONST_0] = (Env env, StackFrame frame, byte[] operands)->{
            frame.getOperandStack().push(0.0,2);
        };
        //dstore_1: 将栈顶 double 型数值存入第二个局部变量。
        OPCODES[Constants.DSTORE_1] = (Env env, StackFrame frame, byte[] operands)->{
            Object var = frame.getOperandStack().pop();
            frame.getLocalVariables().set(1, var, 2);
        };
        //dload_1: 将第二个 double 型局部变量推送至栈顶。
        OPCODES[Constants.DLOAD_1] = (Env env, StackFrame frame, byte[] operands)->{
            Object var =  frame.getLocalVariables().get(1);
            frame.getOperandStack().push(var, 2);
        };
        //dconst_1: 将 double 型 1 推送至栈顶
        OPCODES[Constants.DCONST_1] = (Env env, StackFrame frame, byte[] operands)->{
            frame.getOperandStack().push(1.0,2);
        };
        //dadd: 将栈顶两 double 型数值相加并将结果压入栈顶。
        OPCODES[Constants.DADD] = (Env env, StackFrame frame, byte[] operands)->{
            Double var1 = (Double) frame.getOperandStack().pop();
            Double var2 = (Double) frame.getOperandStack().pop();
            frame.getOperandStack().push(var1+var2,2);
        };
        //iconst_1: 将 int 型 1 推送至栈顶
        OPCODES[Constants.ICONST_1] = (Env env, StackFrame frame, byte[] operands)->{
            frame.getOperandStack().push(1,1);
        };
        //istore_3: 将栈顶 int 型数值存入第四个局部变量。
        OPCODES[Constants.ISTORE_3] = (Env env, StackFrame frame, byte[] operands)->{
            frame.getLocalVariables().set(3,frame.getOperandStack().pop(),1);
        };
        //iinc: 将指定 int 型变量增加指定值。
        OPCODES[Constants.IINC] = (Env env, StackFrame frame, byte[] operands)->{
            Integer var = (Integer) frame.getLocalVariables().get(operands[0]);
            frame.getLocalVariables().set(operands[0], var + operands[1], 1);
        };
        //iload_3: 将第四个 int 型局部变量推送至栈顶。
        OPCODES[Constants.ILOAD_3] = (Env env, StackFrame frame, byte[] operands)->{
            frame.getOperandStack().push(frame.getLocalVariables().get(3),1);
        };
        //i2d: 将栈顶 int 型数值强制转换成 double 型数值并将结果压入栈 顶。
        OPCODES[Constants.I2D] = (Env env, StackFrame frame, byte[] operands)->{
            Integer var = (Integer) frame.getOperandStack().pop();
            frame.getOperandStack().push((double)var, 2);
        };
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

}
