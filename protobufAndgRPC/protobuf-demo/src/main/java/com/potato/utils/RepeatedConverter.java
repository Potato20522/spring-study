package com.potato.utils;

/**
 * 适用类型：repeated
 * 第一个字节: 最高bit空置不用，proto属性序号(15以内占用4bit)，最后3bit表示proto类型(大类，proto共有5个大类)
 * 第二个字节：表示后面有多少个字节是当前这个数组元素的
 * 数组有好多元素，后面的元素以此类推
 */
public class RepeatedConverter {
    /**
     * 在二进制数据中修改proto的属性下标
     * @param bytes 输入的数据
     * @param targetFiledIndex 想要转换成下标是多少
     */
    public static void map(byte[] bytes,int targetFiledIndex) {
        for (int i = 0; i < bytes.length; i++) {
            //一个byte和 0111进行与运算，就取该byte的后3位
            //一个byte和 0111进行与运算，就取该byte的后3位
            byte filedType = (byte) (bytes[i] & 7);//后3位
            //targetFiledIndex << 3 左移动3位，就在后补3个0,再把filedType放到后3位，用或运算来实现
            //最高位proto没用到，就不管他它，让它默认为0吧
            //java的整数默认会转换成int，这里让它再转回来
            byte tag = (byte) ((targetFiledIndex << 3) | filedType);//?
            bytes[i] = tag;
            i += bytes[i + 1] + 1;
        }
    }
}
