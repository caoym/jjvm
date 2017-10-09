package org.caoym.jjvm.runtime;

import com.sun.tools.classfile.ConstantPool;
import org.caoym.jjvm.lang.JvmClass;
import org.caoym.jjvm.lang.JvmMethod;
import org.caoym.jjvm.opcode.OpcodeInvoker;

/**
 * 栈帧
 *
 * 对应 JVM 规范中的栈帧的概念，用于表示一次方法调用的上下文
 */
public class StackFrame {

    /**
     * 局部变量表(Local Variables）
     * 用于存储方法的局部变量
     */
    private Slots<Object> localVariables;

    /**
     * 操作数栈(Operand Stack）
     * 用于存储操作指令的输入输出
     */
    private SlotsStack<Object> operandStack;

    /**
     * 字节码
     */
    private OpcodeInvoker[] opcodes;

    /**
     * 程序计数器
     */
    private int pc=0;
    /**
     * 常量池（Constant Pool）
     */
    private ConstantPool constantPool;
    private Object returnVal;
    private String returnType;
    private boolean isReturned = false;
    private final JvmClass clazz;
    private final JvmMethod method;

    StackFrame(JvmClass clazz,JvmMethod method, ConstantPool constantPool,
                      OpcodeInvoker[] opcodes,
                      int variables,
                      int stackSize) {
        this.constantPool = constantPool;
        this.opcodes = opcodes;
        this.operandStack = new SlotsStack<>(stackSize);
        this.localVariables = new Slots<>(variables);
        this.clazz = clazz;
        this.method = method;
    }

    public Slots<Object> getLocalVariables() {
        return localVariables;
    }

    public SlotsStack<Object> getOperandStack() {
        return operandStack;
    }

    public ConstantPool getConstantPool() {
        return constantPool;
    }

    public void setPC(int pc) {
        this.pc = pc;
    }

    public void setReturn(Object returnVal, String returnType) {
        this.isReturned = true;
        this.returnVal = returnVal;
        this.returnType = returnType;
    }

    public Object getReturn() {
        return returnVal;
    }
    public String getReturnType() {
        return returnType;
    }

    public boolean isReturned() {
        return isReturned;
    }

    public int getPC() {
        return pc;
    }
    public int increasePC(){
        return pc++;
    }
    public OpcodeInvoker[] getOpcodes() {
        return opcodes;
    }

    public JvmClass getCurrentClass() {
        return clazz;
    }

    public JvmMethod getCurrentMethod() {
        return method;
    }
}
