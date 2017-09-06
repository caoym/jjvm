package org.caoym.jjvm;

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
    private Slots localVariables;
    /**
     * 操作数栈(Operand Stack）
     * 用于存储操作指令的输入输出
     */
    private Slots operandStack;

    public void StackFrame(Stack stack){

    }

    public Slots getLocalVariables() {
        return localVariables;
    }

    public Slots getOperandStack() {
        return operandStack;
    }



}
