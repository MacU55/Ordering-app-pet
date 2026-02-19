package org.example.company.service.converter;

public abstract class ConverterImpl<A, B, C> implements Converter<A, B, C> {


//    @Override
//    public B convertToDTO(A a, B b) {
//        throw new UnsupportedOperationException("Not supported operation.");
//    }
//
//    @Override
//    public A convertToEntity(C c, A a) {
//        throw new UnsupportedOperationException("Not supported operation.");
//    }

    @Override
    public B convertToDTO(A a) {
        throw new UnsupportedOperationException("Not supported operation.");
    }

    @Override
    public A convertToEntity(C c) {
        throw new UnsupportedOperationException("Not supported operation.");
    }
}
