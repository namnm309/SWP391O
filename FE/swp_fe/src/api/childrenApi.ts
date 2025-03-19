import apiClient from "./api";
import { Children } from "@/lib/children";

export const getMyChildren = async () => {
  const response = await apiClient.get(`/user/my-children`);
  return response.data;
};
export const getMyChildrenId = async (id: string) => {
  const response = await apiClient.get(`/user/child/${id}`);
  return response.data;
};
export const getMyChildrenHistoryId = async (id: string) => {
  const response = await apiClient.get(`user/history/${id}`);
  return response.data;
};
export const getMyChildrenUpcomingId = async (id: string) => {
  const response = await apiClient.get(`user/upcoming/${id}`);
  return response.data;
};
export const postMyChildren = async (data: Children) => {
  const response = await apiClient.post(`/user/child/create`, data);
  return response.data;
};

export const putMyChildren = async (id: number, data: Children) => {
  const response = await apiClient.put(`/user/children/${id}/update`, data);
  return response.data;
};
