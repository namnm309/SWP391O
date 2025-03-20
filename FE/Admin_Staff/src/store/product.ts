import { StoreGet, StoreSet } from "@/store";
import { Vaccine } from "@/types/vaccine";
import { Category } from "@/types/category";
import axios from "@/utils/axiosConfig";
import { toast } from "@/hooks/use-toast";
import { ApiError } from "@/types/error";

export interface ProductState {
  vaccines: Vaccine[];
  categories: Category[];
}

export interface ProductActions {
  fetchVaccines: () => Promise<Vaccine[]>;
  createVaccine: (formData: FormData) => Promise<void>;
  updateVaccine: (vaccineId: number, formData: FormData) => Promise<void>;
  deleteVaccine: (vaccineId: number) => Promise<void>;
  fetchCategories: () => Promise<Category[]>;
  createCategory: (formData: FormData) => Promise<void>;
  updateCategory: (categoryId: number, formData: FormData) => Promise<void>;
  deleteCategory: (categoryId: number) => Promise<void>;
}

export const initialProduct: ProductState = {
  vaccines: [],
  categories: [],
};

export function productActions(set: StoreSet, get: StoreGet): ProductActions {
  return {
    fetchVaccines: async () => {
      set((state) => { state.loading.isLoading = true; });
      try {
        const token = localStorage.getItem("token")
        const resp = await axios.get("/product/products", {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        const vaccines: Vaccine[] = resp.data || [];
        set((state) => { state.product.vaccines = vaccines; });
        return vaccines;
      } catch (error: unknown) {
        const apiError = error as ApiError;
        const msg = apiError?.response?.data?.message || apiError?.message;
        toast({ variant: "destructive", description: msg });
        return [];
      } finally {
        set((state) => { state.loading.isLoading = false; });
      }
    },

    createVaccine: async (formData: FormData) => {
      set((state) => { state.loading.isLoading = true; });
      try {
        const token = localStorage.getItem("token")
        await axios.post("/product/addProduct", formData, {
          headers: { "Content-Type": "multipart/form-data", "Authorization": `Bearer ${token}` },
        });
        await get().fetchVaccines();
        toast({
          title: "Success",
          description: "Vaccine created successfully",
          variant: "success"
        });
      } catch (error: unknown) {
        const apiError = error as ApiError;
        const msg = apiError?.response?.data?.message || apiError?.message;
        toast({
          title: "Error",
          description: msg,
          variant: "destructive"
        });
      } finally {
        set((state) => { state.loading.isLoading = false; });
      }
    },

    updateVaccine: async (vaccineId: number, formData: FormData) => {
      set((state) => { state.loading.isLoading = true; });
      try {
        const token = localStorage.getItem("token")
        await axios.put(`/product/updateProduct/${vaccineId}`, formData, {
          headers: { "Content-Type": "multipart/form-data", "Authorization": `Bearer ${token}` },
        });
        await get().fetchVaccines();
        toast({
          title: "Success",
          description: "Vaccine updated successfully",
          variant: "success"
        });
      } catch (error: unknown) {
        const apiError = error as ApiError;
        const msg = apiError?.response?.data?.message || apiError?.message;
        toast({
          title: "Error",
          description: msg,
          variant: "destructive"
        });
      } finally {
        set((state) => { state.loading.isLoading = false; });
      }
    },

    deleteVaccine: async (vaccineId: number) => {
      set((state) => { state.loading.isLoading = true; });
      try {
        const token = localStorage.getItem("token")
        await axios.delete(`/product/deleteProduct/${vaccineId}`, {
          headers: {
            "Authorization": `Bearer ${token}`
          }
        });
        await get().fetchVaccines();
        toast({
          title: "Success",
          description: "Vaccine deleted successfully",
          variant: "success"
        });
      } catch (error: unknown) {
        const apiError = error as ApiError;
        const msg = apiError?.response?.data?.message || apiError?.message;
        toast({
          title: "Error",
          description: msg,
          variant: "destructive"
        });
      } finally {
        set((state) => { state.loading.isLoading = false; });
      }
    },

    fetchCategories: async () => {
      set((state) => { state.loading.isLoading = true; });
      try {
        const token = localStorage.getItem("token")
        const resp = await axios.get("/category/showCategory", {
          headers: {
            "Authorization": `Bearer ${token}`
          }
        });
        const data: Category[] = resp.data.result || [];
        set((state) => { state.product.categories = data; });
        return data;
      } catch (error: unknown) {
        const apiError = error as ApiError;
        const msg = apiError?.response?.data?.message || apiError?.message;
        toast({ variant: "destructive", description: msg });
        return [];
      } finally {
        set((state) => { state.loading.isLoading = false; });
      }
    },

    createCategory: async (formData: FormData) => {
      set((state) => { state.loading.isLoading = true; });
      try {
        const token = localStorage.getItem("token")
        await axios.post("/category/createCategory", formData, {
          headers: { "Content-Type": "multipart/form-data", "Authorization": `Bearer ${token}` },
        });
        await get().fetchCategories();
        toast({
          title: "Success",
          description: "Category created successfully",
          variant: "success"
        });
      } catch (error: unknown) {
        const apiError = error as ApiError;
        const msg = apiError?.response?.data?.message || apiError?.message;
        toast({
          title: "Error",
          description: msg,
          variant: "destructive"
        });
      } finally {
        set((state) => { state.loading.isLoading = false; });
      }
    },

    updateCategory: async (categoryId: number, formData: FormData) => {
      set((state) => { state.loading.isLoading = true; });
      try {
        const token = localStorage.getItem("token")
        await axios.put(`/category/updateCategory/${categoryId}`, formData, {
          headers: { "Content-Type": "multipart/form-data", "Authorization": `Bearer ${token}` },
        });
        await get().fetchCategories();
        toast({
          title: "Success",
          description: "Category updated successfully",
          variant: "success"
        });
      } catch (error: unknown) {
        const apiError = error as ApiError;
        const msg = apiError?.response?.data?.message || apiError?.message;
        toast({
          title: "Error",
          description: msg,
          variant: "destructive"
        });
      } finally {
        set((state) => { state.loading.isLoading = false; });
      }
    },

    deleteCategory: async (categoryId: number) => {
      set((state) => { state.loading.isLoading = true; });
      try {
        const token = localStorage.getItem("token")
        await axios.delete(`/category/deleteCategory/${categoryId}`, {
          headers: {
            "Authorization": `Bearer ${token}`
          }
        });
        await get().fetchCategories();
        toast({
          title: "Success",
          description: "Category deleted successfully",
          variant: "success"
        });
      } catch (error: unknown) {
        const apiError = error as ApiError;
        const msg = apiError?.response?.data?.message || apiError?.message;
        toast({
          title: "Error",
          description: msg,
          variant: "destructive"
        });
      } finally {
        set((state) => { state.loading.isLoading = false; });
      }
    },
  };
}
