import { Vaccine } from "../models/vaccine";
import { StoreGet, StoreSet } from "../store";
import axios from "../utils/axiosConfig";

export interface VaccineState {
  vaccines: Vaccine[];
}

export interface VaccineActions {
  fetchAllVaccine: () => Promise<void>;
  createVaccine: (formData: FormData) => Promise<void>;
  updateVaccine: (vaccineId: string, formData: FormData) => Promise<void>;
  deleteVaccine: (vaccineId: string) => Promise<void>;
}

export const initialVaccine: VaccineState = {
  vaccines: [],
};

export function vaccineActions(set: StoreSet, get: StoreGet): VaccineActions {
  return {
    fetchAllVaccine: async () => {
      set((state) => {
        state.loading.isLoading = true;
      });
      try {
        const resp = await axios.get("/staff/products");
        set((state) => {
          state.vaccine.vaccines = resp.data || [];
        });
      } catch (error: any) {
        set((state) => {
          const msg = error?.response?.data?.message || error?.message;
          state.notification.data.push({
            status: "ERROR",
            content: msg,
          });
        });
      } finally {
        set((state) => {
          state.loading.isLoading = false;
        });
      }
    },

    createVaccine: async (formData: FormData) => {
      set((state) => {
        state.loading.isLoading = true;
      });
      try {
        await axios.post("/staff/addProduct", formData, {
          headers: { "Content-Type": "multipart/form-data" },
        });
        get().fetchAllVaccine();
        set((state) => {
          state.notification.data.push({
            status: "SUCCESS",
            content: "Vaccine created successfully",
          });
        });
      } catch (error: any) {
        set((state) => {
          const msg = error?.response?.data?.message || error?.message;
          state.notification.data.push({
            status: "ERROR",
            content: msg,
          });
        });
      } finally {
        set((state) => {
          state.loading.isLoading = false;
        });
      }
    },

    updateVaccine: async (vaccineId: string, formData: FormData) => {
      set((state) => {
        state.loading.isLoading = true;
      });
      try {
        await axios.put(`/staff/updateProduct/${vaccineId}`, formData, {
          headers: { "Content-Type": "multipart/form-data" },
        });
        get().fetchAllVaccine();
        set((state) => {
          state.notification.data.push({
            status: "SUCCESS",
            content: "Vaccine updated successfully",
          });
        });
      } catch (error: any) {
        set((state) => {
          const msg = error?.response?.data?.message || error?.message;
          state.notification.data.push({
            status: "ERROR",
            content: msg,
          });
        });
      } finally {
        set((state) => {
          state.loading.isLoading = false;
        });
      }
    },

    deleteVaccine: async (vaccineId: string) => {
      set((state) => {
        state.loading.isLoading = true;
      });
      try {
        await axios.delete(`/staff/deleteProduct/${vaccineId}`);
        get().fetchAllVaccine();
        set((state) => {
          state.notification.data.push({
            status: "SUCCESS",
            content: "Vaccine deleted successfully",
          });
        });
      } catch (error: any) {
        set((state) => {
          const msg = error?.response?.data?.message || error?.message;
          state.notification.data.push({
            status: "ERROR",
            content: msg,
          });
        });
      } finally {
        set((state) => {
          state.loading.isLoading = false;
        });
      }
    },
  };
}
