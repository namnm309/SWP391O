import apiClient from "./api";


export const getVaccines = async () => {
  const response = await apiClient.get(`/product/products`);
  return response.data;
};


export const getVaccineById = async (id:string) => {
  const response = await apiClient.get(`/product/product/${id}`);
  return response.data;
};
