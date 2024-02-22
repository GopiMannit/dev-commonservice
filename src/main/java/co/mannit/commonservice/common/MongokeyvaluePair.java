package co.mannit.commonservice.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MongokeyvaluePair<T extends Object> {

	private String key;
	private T value;
	
	/*public MongokeyvaluePair(String key, T value) {
		super();
		this.key = key;
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public T getValue() {
		return value;
	}
	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "MongokeyvaluePair [key=" + key + ", value=" + value + "]";
	}*/
}
