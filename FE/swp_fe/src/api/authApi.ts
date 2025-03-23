import { UserLoginForm } from "@/lib/users";
import apiClient from "./api";

export const authLogin = async (data: UserLoginForm) => {
  const response = await apiClient.post(`/auth/login`, data);
  return response.data;
};
