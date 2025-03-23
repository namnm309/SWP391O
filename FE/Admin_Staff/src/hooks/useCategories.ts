import { useEffect, useState } from "react";
import { useStore } from "@/store";
import type { Category } from "@/types/category";

export function useCategories(): { categories: Category[]; loading: boolean } {
  const fetchCategories = useStore((state) => state.fetchCategories);
  const [categories, setCategories] = useState<Category[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const load = async () => {
      setLoading(true);
      const data = await fetchCategories();
      setCategories(data);
      setLoading(false);
    };
    load();
  }, [fetchCategories]);

  return { categories, loading };
}