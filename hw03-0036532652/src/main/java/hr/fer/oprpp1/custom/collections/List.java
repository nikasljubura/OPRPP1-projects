package hr.fer.oprpp1.custom.collections;

public interface List<E> extends Collection<E>{

    E get(int index);
    void insert(E value, int position);
    int indexOf(E value);
    void remove(int index);

}
