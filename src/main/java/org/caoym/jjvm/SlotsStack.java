package org.caoym.jjvm;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * 可指定槽位大小的栈
 */
public class SlotsStack<T> {

    private T[] buffer;

    private int end = 0;

    public SlotsStack(int size){
        buffer = (T[]) new Object[size];
    }

    public void push(T entity) throws IllegalArgumentException{
        this.push(entity, 1);
    }

    public void push(T entity, int size) throws IllegalArgumentException{
        if(size <=0 || end+size>buffer.length){
            throw new IllegalArgumentException("invalid entity size "+size);
        }
        buffer[end] = entity;
        for(int i=1; i<size; i++){
            buffer[end+i] = null;
        }
        end += size;

    }

    public T pop() throws NoSuchElementException {
        while (end >0){
            end--;
            T entity = buffer[end];
            if(entity != null){
                buffer[end] = null;
                return entity;
            }

        }
        throw new NoSuchElementException();
    }

    /**
     * 按正常出栈的顺序 pop 出全部元素
     * @return
     */
    public T[] popAll() {
        ArrayList<T> items = new ArrayList<>();
        while (end >0){
            end--;
            T entity = buffer[end];
            if(entity != null){
                buffer[end] = null;
                items.add(entity);
            }
        }
        return (T[]) items.toArray();
    }

    /**
     * 按栈内顺序 pop 出全部元素
     * @return
     */
    public T[] dumpAll() {
        ArrayList<T> items = new ArrayList<>();

        for (int begin = 0; begin < end; begin++){
            T entity = buffer[begin];
            if(entity != null){
                buffer[begin] = null;
                items.add(entity);
            }
        }
        end = 0;
        return (T[]) items.toArray();
    }


    /**
     * 取出最后一个元素，但不出栈
     * @return
     */
    public T pick(){
        int end = this.end;
        while (end > 0)
        {
            end--;
            T entity = buffer[end];
            if(entity != null){
                return entity;
            }
        }
        return null;
    }
}
