package org.adso.minimarket.mappers;

public interface BaseMapper<D, E> {
    D toDto(E model);
}
