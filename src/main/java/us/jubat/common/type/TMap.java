package us.jubat.common.type;

import java.util.HashMap;
import java.util.Map;

import org.msgpack.type.MapValue;
import org.msgpack.type.Value;

import us.jubat.common.TypeMismatch;

public class TMap<K, V> implements TType<Map<K, V>> {
	private final TType<K> keyType;
	private final TType<V> valueType;

	public static <K, V> TMap<K, V> create(TType<K> keyType, TType<V> valueType) {
		return new TMap<K, V>(keyType, valueType);
	}

	public TMap(TType<K> keyType, TType<V> valueType) {
		this.keyType = keyType;
		this.valueType = valueType;
	}

	public void check(Map<K, V> value) {
		if (value == null)
			throw new NullPointerException();
		for (Map.Entry<K, V> e : value.entrySet()) {
			keyType.check(e.getKey());
			valueType.check(e.getValue());
		}
	}

	public Map<K, V> revert(Value value) {
		if (value.isMapValue()) {
			MapValue map = value.asMapValue();
			int initialSize = (int) (map.size() / 0.75) + 1;
			HashMap<K, V> reverted = new HashMap<K, V>(initialSize);
			for (Map.Entry<Value, Value> entry : map.entrySet()) {
				K key = this.keyType.revert(entry.getKey());
				V val = this.valueType.revert(entry.getValue());
				reverted.put(key, val);
			}
			return reverted;
		} else {
			throw new TypeMismatch();
		}
	}
}
