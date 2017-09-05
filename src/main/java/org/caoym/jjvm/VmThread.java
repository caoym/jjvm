package org.caoym.jjvm;


/**
 * Created by caoyangmin on 2017/9/5.
 */
public class VmThread{
    Env env;
    BytecodeInterpreter interpreter;

    public void VmThread(){

    }
    public void run(){
        interpreter.run(env);
    }
}
