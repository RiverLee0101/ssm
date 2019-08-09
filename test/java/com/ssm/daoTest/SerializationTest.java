package com.ssm.daoTest;

import java.io.*;

import static javafx.scene.input.KeyCode.O;

public class SerializationTest {
    
    /*
    * 序列化
    */
    public static byte[] toBytes(Serializable obj) throws IOException {
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            ){
            objectOutputStream.writeObject(obj);
            return byteArrayOutputStream.toByteArray();
        }
    }
    
    /*
    * 反序列化
    */
    public static Serializable toObj(byte[] bytes) throws IOException,ClassNotFoundException{
        try(ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        ){
            Object o = objectInputStream.readObject();
            return (Serializable) O;
        }
    }
}
