import apiClient from "./api";

export const getPosts = async () => {
  const response = await apiClient.get(`/post/posts`);
  return response.data;
};

export const getPostById = async (id: string) => {
  const response = await apiClient.get(`/post/posts/search?id=${id}`);
  return response.data;
};
