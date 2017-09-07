package org.caoym.jjvm.opcodes;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * 可指定槽位大小的栈
 */
public class SlotsStack {

    private ArrayList buffer;

    private int end = 0;

    public SlotsStack(int size){
        buffer = new ArrayList(size);
    }
    public void push(Object entity, int size) throws IllegalArgumentException{
        if(size <=0 || end+size>buffer.size()){
            throw new IllegalArgumentException("invalid entity size "+size);
        }
        buffer.set(end, entity);
        for(int i=1; i<size; i++){
            buffer.set(end+i, null);
        }
        end += size;

    }

    public Object pop() throws NoSuchElementException {
        while (end >0){
            Object entity = buffer.get(end);
            if(entity != null){
                return entity;
            }
            end--;
        }
        throw new NoSuchElementException();
    }
}
