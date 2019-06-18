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
            OpcodeInvoker[] ops = frame.getOps();
            int pc = frame.getPC();
            
            StringBuilder sb = new StringBuilder();
            sb.append("> ");
            sb.append(frame.getCurrentClass().getName());
            sb.append(".");
            sb.append(frame.getCurrentMethod().getName());
            sb.append("@");
            sb.append(pc);
            sb.append(":");
            sb.append(ops[pc]);
            sb.append("[");
            sb.append(ops[pc].getNoOfOperands());
            sb.append("]");
            System.out.println(sb);
            
            //execute opcode
            int ret = ops[pc].invoke(env, frame);
            
            //move forward
            if(ret == 0) {
            	frame.increasePC(1+ops[pc].getNoOfOperands());
            }else {
            	frame.increasePC(pc + ret);
            }
        }
    }

    /**
     * 返回内容以 opcode 为主
     * 
     * @param codes
     * @return
     */
    public static OpcodeInvoker[] parseCodes(byte[] codes){
        ArrayList<OpcodeInvoker> opcodes = new ArrayList<>();
        for(int i=0; i<codes.length; i++){
            short code = (short)(0xff&codes[i]);
            final OpcodeRout route = OpcodeRout.valueOf(code);
            short noOfOperands = Constants.NO_OF_OPERANDS[code];
            byte[] operands = Arrays.copyOfRange(codes, i + 1, i + 1 + noOfOperands);
            opcodes.add(new OpcodeInvoker() {
                @Override
                public int invoke(Env env, StackFrame frame) throws Exception {
                    route.invoke(env, frame, operands);
                    return 0;
                }
                @Override
                public String toString() {
                    return route.name();
                }
				@Override
				public int getNoOfOperands() {
					// TODO Auto-generated method stub
					return 0;
				}
            });
            i += noOfOperands;
        }
        return Arrays.copyOf(opcodes.toArray(), opcodes.size(), OpcodeInvoker[].class);
    }
    
    /**
     * 返回内容以 opcode 和 operands 为主
     * @param codes
     * @return
     */
    public static OpcodeInvoker[] parseOps(byte[] codes){
    	ArrayList<OpcodeInvoker> ops = new ArrayList<>();
    	for(int i=0; i<codes.length; i++){
    		short opcode = (short)(0xff&codes[i]);
            final OpcodeRout route = OpcodeRout.valueOf(opcode);
            short noOfOperands = Constants.NO_OF_OPERANDS[opcode];
            byte[] operands = Arrays.copyOfRange(codes, i + 1, i + 1 + noOfOperands);
            ops.add(new OpcodeInvoker() {
                @Override
                public int invoke(Env env, StackFrame frame) throws Exception {
                    route.invoke(env, frame, operands);
                    return 0;
                }
                @Override
                public String toString() {
                    return route.name();
                }
                public int getNoOfOperands() {
                	return noOfOperands;
                }
            });
            
            for(int si = 0; si < noOfOperands; si++) {
            	ops.add(null);
            }
            i += noOfOperands;
    	}
    	return Arrays.copyOf(ops.toArray(), ops.size(), OpcodeInvoker[].class);
    }
}
