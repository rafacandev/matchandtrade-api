package com.matchandtrade.test;

import java.util.HashMap;
import java.util.Map;

public class IntegrationTestStore {
	public enum StoredObject {
		UserAuthentication
	}
	
	private static Map<StoredObject, Object> storedObjects = new HashMap<>();
	
	public static void add(StoredObject storedObject, Object object) {
		storedObjects.put(storedObject, object);
	}
	
	public static Object get(StoredObject storedObject) {
		return storedObjects.get(storedObject);
	}

}
