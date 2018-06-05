package com.shichuang.sendnar.entify;

import java.io.Serializable;

/**
 * Created by xiedd on 2017/8/1.
 */

public class AMBaseDto<T> implements Serializable {
    //private static final long serialVersionUID = -686453405647539973L;
    public int code;
    public String msg;
    public T data;
}
