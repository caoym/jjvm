package org.caoym.jjvm.lang;

import com.sun.tools.classfile.ClassFile;
import com.sun.tools.classfile.Field;

/**
 * 类的成员
 */
public class JvmOpcodeField {

    private Field field;
    private ClassFile classFile;

    public JvmOpcodeField(ClassFile classFile, Field field) {
        this.field = field;
        this.classFile = classFile;
        field.descriptor
    }
}
