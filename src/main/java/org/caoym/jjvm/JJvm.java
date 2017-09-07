package org.caoym.jjvm;

import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Created by caoyangmin on 2017/9/7.
 */
public class JJvm {
    static public void main(String[] args){
        if(args.length == 0){
            System.out.println("usage: JJvm class [args...]");
            return;
        }
        VirtualMachine vm = new VirtualMachine(Paths.get("."), args[0]);
        try {
            args = Arrays.copyOfRange(args, 1, args.length);
            vm.run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
