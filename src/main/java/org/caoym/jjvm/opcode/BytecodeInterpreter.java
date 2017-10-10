package org.caoym.jjvm.opcode;

import com.sun.org.apache.bcel.internal.Constants;
import org.caoym.jjvm.runtime.Env;
import org.caoym.jjvm.runtime.JvmStack;
import org.caoym.jjvm.runtime.StackFrame;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 字节码解析器
 */
public class BytecodeInterpreter {

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
                StackFrame oldFrame = frame;
                stack.popFrame();
                frame = stack.currentFrame();
                if(frame != null && !"void".equals(oldFrame.getReturnType())){
                    frame.getOperandStack().push(oldFrame.getReturn());
                }
                continue;
            }
            OpcodeInvoker[] codes = frame.getOpcodes();
            int pc = frame.increasePC();
            StringBuilder sb = new StringBuilder();
            sb.append("> ");
            sb.append(frame.getCurrentClass().getName());
            sb.append(".");
            sb.append(frame.getCurrentMethod().getName());
            sb.append("@");
            sb.append(pc);
            sb.append(":");
            sb.append(codes[pc]);
            System.out.println(sb);
            codes[pc].invoke(env, frame);
        }
    }

    public static OpcodeInvoker[] parseCodes(byte[] codes){
        ArrayList<OpcodeInvoker> opcodes = new ArrayList<>();
        for(int i=0; i<codes.length; i++){
            short code = (short)(0xff&codes[i]);
            final OpcodeRout route = OpcodeRout.valueOf(code);
            short noOfOperands = Constants.NO_OF_OPERANDS[code];
            byte[] operands = Arrays.copyOfRange(codes, i + 1, i + 1 + noOfOperands);
            opcodes.add(new OpcodeInvoker() {
                @Override
                public void invoke(Env env, StackFrame frame) throws Exception {
                    route.invoke(env, frame, operands);
                }
                @Override
                public String toString() {
                    return route.name();
                }
            });
            i += noOfOperands;
        }
        return Arrays.copyOf(opcodes.toArray(), opcodes.size(), OpcodeInvoker[].class);
    }

}
