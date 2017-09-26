package org.caoym.jjvm;

import com.sun.org.apache.bcel.internal.Constants;
import com.sun.tools.classfile.AccessFlags;
import com.sun.tools.classfile.ConstantPool;
import com.sun.tools.classfile.ConstantPoolException;
import org.caoym.jjvm.opcodes.Opcode;
import org.caoym.jjvm.runtime.Env;
import org.caoym.jjvm.runtime.JvmStack;
import org.caoym.jjvm.runtime.OpcodeRout;
import org.caoym.jjvm.runtime.StackFrame;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by caoyangmin on 2017/9/5.
 * 字节码解析器
 */
public class BytecodeInterpreter {

    public interface OpcodeInvoker{
        public void invoke(Env env, StackFrame frame) throws Exception ;
    }
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
            OpcodeInvoker[] codes = frame.getOpcodes();
            int pc = frame.increasePC();
            codes[pc].invoke(env, frame);
        }
    }

    public static OpcodeInvoker[] parseCodes(byte[] codes){
        ArrayList<OpcodeInvoker> opcodes = new ArrayList<>();
        for(int i=0; i<codes.length; i++){
            short code = (short)(0xff&codes[i]);
            OpcodeRout route = OpcodeRout.valueOf(code);
            short noOfOperands = Constants.NO_OF_OPERANDS[code];
            byte[] operands = Arrays.copyOfRange(codes, i + 1, i + 1 + noOfOperands);
            opcodes.add( (Env env, StackFrame frame)->route.invoke(env, frame, operands) );
            i += noOfOperands;
        }
        return Arrays.copyOf(opcodes.toArray(), opcodes.size(), OpcodeInvoker[].class);
    }
    static {

    }


}
