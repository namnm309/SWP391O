import { useEffect, useState, Dispatch, SetStateAction } from "react";

export function useStorage<T>(
  key: string,
  initialValue: T
): [T, Dispatch<SetStateAction<T>>, () => Promise<void>] {
  const [storedValue, setStoredValue] = useState<T>(() => {
    try {
      const item = localStorage.getItem(key);
      return item ? (JSON.parse(item) as T) : initialValue;
    } catch (error) {
      console.error(error);
      return initialValue;
    }
  });

  const saveStoredValue = async (value: T) => {
    try {
      localStorage.setItem(key, JSON.stringify(value));
    } catch (error) {
      console.error(error);
    }
  };

  const loadStoredValue = async () => {
    try {
      const item = localStorage.getItem(key);
      if (item) {
        setStoredValue(JSON.parse(item) as T);
      }
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    saveStoredValue(storedValue);
  }, [key, storedValue]);

  return [storedValue, setStoredValue, loadStoredValue];
}
