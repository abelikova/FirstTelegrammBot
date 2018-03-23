package com.company;

public interface DocumentMarshaller {

    <T> String marshal(T document);

    <T> T unmarshal(String str);

    <T> T unmarshal(String str, Class clazz);
    <T> T unmarshal(String str, String clazz);
}
