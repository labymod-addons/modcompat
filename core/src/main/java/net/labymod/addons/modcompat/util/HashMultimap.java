package net.labymod.addons.modcompat.util;

import net.labymod.api.util.collection.map.Multimap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

// TODO (Christian) Replace it with ConcurrentHashMultimap
public class HashMultimap<K, V> implements Multimap<K, V> {

  private final Map<K, Collection<V>> map;

  public HashMultimap() {
    this.map = new HashMap<>();
  }

  public HashMultimap(int initialCapacity) {
    this.map = new HashMap<>(initialCapacity);
  }

  public HashMultimap(int initialCapacity, float loadFactor) {
    this.map = new HashMap<>(initialCapacity, loadFactor);
  }

  public HashMultimap(Map<K, Collection<V>> entries) {
    this.map = entries;
  }

  @Override
  public int size() {
    return this.map.size();
  }

  @Override
  public boolean isEmpty() {
    return this.map.isEmpty();
  }

  @Override
  public void put(K key, V value) {
    Collection<V> values = this.map.computeIfAbsent(key, k -> new ArrayList<>());
    values.add(value);
  }

  @Override
  public boolean remove(K key) {
    Collection<V> values = this.map.remove(key);
    return values != null;
  }

  @Override
  public boolean remove(K key, V value) {
    Collection<V> values = this.map.get(key);
    return values != null && values.remove(value);

  }

  @Override
  public void clear() {
    this.map.clear();
  }

  @Override
  public Collection<V> get(K key) {
    return this.map.computeIfAbsent(key, k -> new ArrayList<>());
  }

  @Override
  public Collection<V> values() {
    Collection<V> values = new ArrayList<>();

    for (Collection<V> value : this.map.values()) {
      values.addAll(value);
    }

    return values;
  }

  @Override
  public Map<K, Collection<V>> asMap() {
    return Map.copyOf(this.map);
  }

  @Override
  public void forEach(Consumer<Entry<K, V>> entryConsumer) {
    for (Entry<K, Collection<V>> entry : this.map.entrySet()) {
      K key = entry.getKey();

      for (V value : entry.getValue()) {
        entryConsumer.accept(new SimpleEntry<>(key, value));
      }
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || this.getClass() != o.getClass()) {
      return false;
    }

    HashMultimap<?, ?> that = (HashMultimap<?, ?>) o;
    return this.map.equals(that.map);
  }

  @Override
  public int hashCode() {
    return this.map.hashCode();
  }

  @Override
  public String toString() {
    return this.map.toString();
  }

  private static class SimpleEntry<K, V> implements Entry<K, V> {

    private final K key;
    private final V value;

    public SimpleEntry(K key, V value) {
      this.key = key;
      this.value = value;
    }

    @Override
    public K getKey() {
      return this.key;
    }

    @Override
    public V getValue() {
      return this.value;
    }

    @Override
    public V setValue(V value) {
      throw new UnsupportedOperationException();
    }
  }

}
