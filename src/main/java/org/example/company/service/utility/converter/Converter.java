package org.example.company.service.utility.converter;

public interface Converter<A, B, C> {

//    B convertToDTO(A a, B b);
//
//    A convertToEntity(C c, A a);

    B convertToDTO(A a);
    A convertToEntity(C c);

}
