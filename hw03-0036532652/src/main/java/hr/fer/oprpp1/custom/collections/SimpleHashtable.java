package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Represents a simple HashMap parametrized by K,V.
 * @param <K> keys stored in map
 * @param <V> values stored in map
 */
public class SimpleHashtable<K,V> implements Iterable<SimpleHashtable.TableEntry<K,V>>{

    private int size; //size -> num of pairs stored in table
    private TableEntry<K,V>[] table; //array of table slots
    private int modificationCount;

    /**
     * makes a table of size 16 slots
     */
    public SimpleHashtable() {
        this.table = (TableEntry<K, V>[]) new TableEntry[16];
        this.size = 0;
        this.modificationCount = 0;
    }


    /**
     * a non default constructor
     * @param initialCapacity of the array of slots we want to create
     * @throws IllegalArgumentException
     */
    public SimpleHashtable(int initialCapacity) {

        this.size = 0;

        if(initialCapacity < 1) throw new IllegalArgumentException("Initial capacity cannot be less than 1!");

        if((int)(Math.ceil((Math.log(initialCapacity) / Math.log(2))))
                == (int)(Math.floor(((Math.log(initialCapacity) / Math.log(2)))))){

            this.table = (TableEntry<K, V>[]) new TableEntry[initialCapacity];

        }else{
            int newCapacity = initialCapacity;

            while((int)(Math.ceil((Math.log(newCapacity) / Math.log(2))))
                    != (int)(Math.floor(((Math.log(newCapacity) / Math.log(2)))))){

                newCapacity++;

            }

            this.table = (TableEntry<K, V>[]) new TableEntry[newCapacity];
        }
    }

    /**
     *
     * @return a new intance of our iterator
     */
    @Override
    public Iterator<SimpleHashtable.TableEntry<K,V>> iterator() {
            return new IteratorImpl();
    }


    /**
     * Models our iterator.
     */
    private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K,V>> {

        private int counter; //number of elements returned
        private TableEntry<K,V> lastReturned; //last that was returned with next
        private int i; //index in simplehastable
        private int iterMod;  //if iterator has been changed
        private boolean legalRemoval = false; //we cant call remove twice in a row
        private int b; //size of simplehashtable

        public IteratorImpl(){
            this.counter = 0;
            this.i = 0;
            this.iterMod = modificationCount;
            this.b = size(); // size of simplehastable in the moment of creation, before iterator
        }


        public boolean hasNext() {
            if(modificationCount != iterMod) throw new ConcurrentModificationException();
            return (counter < b);
        }

        public SimpleHashtable.TableEntry next() {

            if(!hasNext()) throw new NoSuchElementException();
            if(modificationCount != iterMod) throw new ConcurrentModificationException();

            if(lastReturned != null && lastReturned.next != null){
                lastReturned = lastReturned.next;
               // i++;
                this.counter++;
                legalRemoval = true;
                return lastReturned;
            }else{
                for(int j = i; j < table.length; j++){
                    if(SimpleHashtable.this.table[j] != null){
                        lastReturned = SimpleHashtable.this.table[j];
                        i = j+1;
                        this.counter++;
                        legalRemoval = true;
                        return lastReturned;
                    }
                }

            }

            return lastReturned;

        }


        public void remove() {

            if(legalRemoval == true) {
                if (modificationCount != iterMod) throw new ConcurrentModificationException();
                legalRemoval = false;
                SimpleHashtable.this.remove(lastReturned.getKey());
                iterMod++;
            }else{
                throw new IllegalStateException();
            }
        }
    }

    /**
     * represents one key-value pair
     * @param <K> key of the pair
     * @param <V> value of the pair
     */
    public static class TableEntry<K,V> {

        private K key;
        private V value;
        private TableEntry<K,V> next; //pointer to the next K,V pair in the SAME slot


        /**
         * Constructs one K,V pair, makes next null
         * @param key
         * @param value
         */
        public TableEntry (K key, V value) {
            if(key == null) throw new IllegalArgumentException("Key cannot be null");
            this.key = key;
            this.value = value;
            this.next = null;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        /**
         * Overrides toString -> writes {key=value}
         * @return
         */
        @Override
        public String toString() {
            return (this.key.toString() + "=" + this.value.toString());
        }
    }


    /**
     *
     * @param key of the pair we want to add into the map
     * @param value of the pair we want to add into the map
     * @return null if previously the pair didnt exits, otherwise the previous value of the key
     */
    public V put(K key, V value) {

        if(this.containsKey(key)) {
            V val = this.get(key);
            int hashCode = Math.abs(key.hashCode() % this.table.length);
            TableEntry<K, V> entry = this.table[hashCode];
            if (entry.getKey().equals(key)) {
                this.table[hashCode].setValue(value);
                return val;
            } else {
                entry = this.table[hashCode].next;
                while (entry != null) {
                    if (entry.getKey().equals(key)) {
                        entry.setValue(value);
                        return val;
                    }else{
                        entry = entry.next;
                    }
                }
            }
        }else{
            if(this.size / table.length >= 0.75){
                //table is full
                this.refill();
            }
            size++;
            modificationCount++;
            int hashCode = Math.abs(key.hashCode() % this.table.length);

            if (this.table[hashCode] == null) {
                this.table[hashCode] = new TableEntry<>(key, value);
                return null;
            } else {
                TableEntry<K,V> entry = this.table[hashCode];
                while (entry.next != null) {
                    entry = entry.next;
                }
                entry.next = new TableEntry<>(key, value);
                return null;
            }

        }

        return null;

    }


    /**
     * Gets the element by key
     * @param key
     * @return
     */
    public V get(Object key) {


        int hashCode = Math.abs(key.hashCode()) % this.table.length;

        if(this.table[hashCode] == null) return null;

        if(this.table[hashCode].getKey().equals(key)){
                return this.table[hashCode].getValue();
        }else if(this.table[hashCode].next != null){
            TableEntry<K,V> pair = this.table[hashCode];
            while(pair != null) {
                if(pair.getKey().equals(key)){
                    return pair.getValue();
                    }
                pair = pair.next;
                }
        }


        return null;


    }




    /**
     *
     * @return size of the hashmap, which increases each time we add a new K,V pair
     */
    public int size() {
        return this.size;
    }


    public boolean containsKey(Object key) {

        int hashCode = Math.abs(key.hashCode()) % this.table.length;

        if(this.table[hashCode] == null) return false;

        if(this.table[hashCode].getKey().equals(key)){
            return true;
        }else if(this.table[hashCode].next != null){
            TableEntry<K,V> pair = this.table[hashCode].next;
            while(pair != null) {
                if(pair.getKey().equals(key)){
                    return true;
                }
                pair = pair.next;
            }
        }

        return false;

    }

    public boolean containsValue(Object value) {
        boolean contains = false;


        for(int i = 0; i < this.table.length; i++){
            //we go round slots
            if(this.table[i] == null) continue;

            if(this.table[i].getValue().equals(value)){
                contains = true;
                break;
            }else if(this.table[i].next != null){
                TableEntry<K,V> pair = this.table[i].next;
                while(pair != null) {
                    if(pair.getValue().equals(value)){
                        contains = true;
                        break;
                    }
                    pair = pair.next;
                }
            }else{
                continue;
            }
        }

        return contains;
    }


    /**
     * Removes the instance of an object if it exists and returns its previous value, otherwise returns null
     * @param key of the unwanted element
     * @return
     */

    public V remove(Object key) {

        //doesnt contain the key or the key is null

        if(!this.containsKey(key) || key==null) return null;

        //contains the key

        int hashCode = Math.abs(key.hashCode()) % this.table.length;
        TableEntry<K,V> element = this.table[hashCode];

        while(!element.getKey().equals(key)){
            element = element.next;
        }

        //element is the wanted element
        V value = element.getValue();

        if(element.next != null){
            element = element.next;
            size--;
            modificationCount++;
            return value;
        }else{
            element = null;
            modificationCount++;
            size--;
            return value;
        }


    }

    /**
     *
     * @return true if size is 0, false otherwise
     */
    public boolean isEmpty() {
        return (this.size == 0);
    }


    /**
     *
     * @return String containing all pairs
     */
    @Override
    public String toString() {

        TableEntry<K,V>[] array = this.toArray();
        String ar = "[";

        for(int i = 0; i < array.length; i++){
            if(i == array.length - 1){
                ar += array[i];
            }else{
                ar += array[i] + ", ";
            }
        }
        ar += "]";

        return ar;

    }

    /**
     * Turns map into an array
     * @return map into an array
     */
    public TableEntry<K,V>[] toArray(){

        TableEntry<K,V>[] array = (TableEntry<K, V>[]) new TableEntry[this.size()];
        int i = 0;
        for(int j = 0; j < this.table.length; j++){
            if(this.table[j] != null){
                array[i] = this.table[j];
                i++;
                if(this.table[j].next != null){
                    TableEntry<K,V> pair = this.table[j].next;
                    while(pair != null){
                        array[i] = pair;
                        pair = pair.next;
                        i++;
                    }
                }
            }

        }

        return array;

    }

    /**
     * clears the map
     */
    public void clear(){

        this.table = (TableEntry<K, V>[]) new TableEntry[table.length];
        this.size = 0;
        modificationCount++;
    }


    /**
     * if table is full, reallocate a new table double the size and fill with all existing entries
     */
    private void refill(){

        TableEntry<K,V>[] entries =  this.toArray();

        this.table = new TableEntry[this.table.length * 2];

        for(TableEntry<K,V> entry: entries){
            //this.put(entry.getKey(), entry.getValue());

            int hashCode = Math.abs(entry.getKey().hashCode() % this.table.length);

            if (this.table[hashCode] == null) {
                this.table[hashCode] = new TableEntry<>(entry.getKey(), entry.getValue());
            } else {
                TableEntry<K,V> entry1 = this.table[hashCode];
                while (entry1.next != null) {
                    entry1 = entry1.next;
                }
                entry1.next = new TableEntry<>(entry.getKey(), entry.getValue());
            }
        }

    }


}
