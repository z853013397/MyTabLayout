package com.zeyuan.kyq.utils;


import com.zeyuan.kyq.http.bean.UserStepBean;

import java.util.List;

public class ListArrayUtils {

    public static UserStepBean[] listToArray(List<UserStepBean> list) {
        int size = list.size();
        UserStepBean[] result = new UserStepBean[size];
        for (int i = 0; i < size; i++) {
            result[i] = list.get(i);
        }
        return result;
    }
//    public <T> T[] listToArray(T t, List list) {
//        int size = list.size();
//        Object[] temp = new Object[size];
//
//
//        for (int i = 0; i < size; i++) {
//            result[i] = (T) list.get(i);
//        }
//
//        return result;
//    }
}

//    public class TestGenericArray<E> {
//        private Object[] elements;
//        private int size = 0;
//        private static final int DEFAULT_INITIAL_CAPACITY = 16;
//
//        public TestGenericArray() {
//            elements = new Object[DEFAULT_INITIAL_CAPACITY];
//        }
//
//        public E pop() {
//            if (size == 0) {
//                throw new EmptyStackException();
//            }
//            E reslut = (E) elements[--size];
//            elements[size] = null;
//            return reslut;
//        }
//    }
//}