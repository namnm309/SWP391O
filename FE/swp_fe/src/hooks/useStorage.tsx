import { useEffect, useState } from "react";

export const useStorage = (key, initialValue) => {
  const [storedValue, setStoredValue] = useState(() => {
    try {
      const item = window.localStorage.getItem(key);
      return item ? JSON.parse(item) : initialValue ?? {};
    } catch (error) {
      return initialValue ?? {};
    }
  });

  const saveStoredValue = async (value) => {
    try {
      window.localStorage.setItem(key, JSON.stringify(value));
    } catch (error) {
      console.log(error);
    }
  };

  const loadStoredValue = async () => {
    try {
      const item = window.localStorage.getItem(key);
      if (item) {
        setStoredValue(JSON.parse(item));
      }
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    saveStoredValue(storedValue);
  }, [key, storedValue]);

  return [storedValue, setStoredValue, loadStoredValue];
};
