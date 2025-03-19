import apiClient from "./api";


export const getNotifications = async () => {
  const response = await apiClient.get(`/notification/notifications`);
  return response.data;
};

 