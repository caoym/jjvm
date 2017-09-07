package org.caoym.jjvm;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * 可指定槽位大小的数组
 */
public class Slots {
    private ArrayList buffer;

    public Slots(int size){
        buffer = new ArrayList(size);
    }

    public void set(int pos, Object entity, int size) throws IllegalArgumentException{
        if(pos <=0 || pos+size > buffer.size()){
            throw new IllegalArgumentException("invalid entity size "+size);
        }
        buffer.set(pos, entity);
        for(int i=1; i<size; i++){
            buffer.set(pos+i, null);
        }
    }

    public Object get(int pos) throws NoSuchElementException{
        if(pos<0 || pos >= buffer.size()){
            throw new NoSuchElementException();
        }
        return buffer.get(pos);
    }

}
