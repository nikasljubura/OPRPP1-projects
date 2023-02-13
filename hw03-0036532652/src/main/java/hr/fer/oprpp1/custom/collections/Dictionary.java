package hr.fer.oprpp1.custom.collections;

import java.util.Objects;

/**
 * Models a dictionary(map)
 * @param <K> type of keys of the stored pairs
 * @param <V> type of values of the stored pairs
 */
public class Dictionary<K,V> {

    private ArrayIndexedCollection<Pair<K,V>> colDict;

    public Dictionary(){
        this.colDict = new ArrayIndexedCollection<>();
    }
    /**
     * Models a pair.
     * @param <K> type of key
     * @param <V> type of value
     */
    private static class Pair<K, V>{

        K key;
        V value;

        Pair(K key, V value){
            if(key == null) throw new RuntimeException("Key cannot be null");
            this.key = key;
            this.value = value;
        }

        public K getKey(){
            return this.key;
        }

        public void setValue(V value){
            this.value = value;
        }

        public V getValue(){
            return this.value;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> that = (Pair<?, ?>) o;
            return Objects.equals(key, that.key) && Objects.equals(value, that.value);
        }
    }


    /**
     *
     * @return true if dictionary is empty, false otherwise
     */
    public boolean isEmpty(){
        return colDict.isEmpty();
    }


    /**
     *
     * @return size of the dictionary
     */
    public int size(){
        return colDict.size();
    }

    /**
     * clears the dictionary
     */
    public void clear(){

        colDict.clear();
    }


    /**
     * puts the given key value pair into the dictionary
     * @param key of the pair we want to put in dictionary
     * @param value we want to put in dictionary
     * @return previous value of the pair at the given key if it existed, null if it didn't exist
     */
    public V put(K key, V value){

        V previousValue = get(key);

        if(previousValue == null){
            Pair<K,V> newPair = new Pair<>(key, value);
            colDict.add(newPair);
            return null;
        }else{
            remove(key);
            Pair<K,V> newPair = new Pair<>(key, value);
            colDict.add(newPair);
        }
        return previousValue;
    }


    /**
     * searches the collection for the given key and returns its value if it exits
     * @param key of the object we want to get
     * @return value of the wanted object, null if it doesnt exits
     */
   public V get(Object key){

        Object[] arr = this.colDict.toArray();

        for(Object pair: arr){
            Pair<K,V> newPair = (Pair<K,V>) pair;
            if(newPair.getKey().equals(key)){
                return newPair.getValue();
            }
        }
        return null;
   }

    /**
     * removes a pair from dictionary
     * @param key of the Pair we want to remove
     * @return value of the removed pair
     */
   public V remove(K key){

       V value = get(key);
       Pair<K,V> unwantedPair;
       if(value == null){
           unwantedPair = new Pair<>(key, null);
       }else{
           unwantedPair = new Pair<>(key, value);
       }

       this.colDict.remove(unwantedPair);
       return value;


   }



}
