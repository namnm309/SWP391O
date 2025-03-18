import { ChildProfile, Patient, User, UserProfile, UserRole } from "../models/user";
import type { StoreGet, StoreSet } from "../store";
import axios from "../utils/axiosConfig";
import { parseJWT } from "../utils/validate";

export interface ProfileState {
  user: User | undefined;
  myInfo: UserProfile | undefined;
  children: Patient[];
  allUsers: Patient[];
  userProfile: UserProfile | undefined;
  error: string | undefined;
}

export interface ProfileActions {
  fetchAllUsers: () => Promise<Patient[]>;
  fetchAllChildren: () => Promise<Patient[]>;
  registerUser: (payload: {
    username: string;
    email: string;
    phone: string;
    password: string;
  }) => Promise<void>;
  verifyUser: (email: string, code: string) => Promise<void>;
  resendVerification: (email: string) => Promise<void>;
  login: (username: string, password: string) => Promise<void>;
  fetchProfile: () => Promise<void>;
  updateProfile: (data: any) => Promise<void>;
  deleteUser: (userId: string) => Promise<void>;
  logout: () => Promise<void>;
  changePassword: (oldPassword: string, newPassword: string) => Promise<void>;
  createChild: (parentId: string, childRequest: {
    fullName: string;
    dob: string;
    gender: string;
  }) => Promise<void>;
  updateMyChild: (childId: string, childRequest: {
    fullName: string;
    dob: string;
    gender: string;
  }) => Promise<void>;
}

export const initialProfile: ProfileState = {
  user: undefined,
  userProfile: undefined,
  children: [],
  error: undefined,
  allUsers: [],
  myInfo: undefined,
};

export function profileActions(set: StoreSet, get: StoreGet): ProfileActions {
  return {
    fetchAllUsers: async () => {
      set((state) => {
        state.loading.isLoading = true;
      }, false, "loading: start");
      try {
        const resp = await axios.get("/manage/parents");
        const data: Patient[] = resp.data || [];
        set((state) => {
          state.profile.allUsers = data;
        }, false, "fetchAllUsers: success");
        return data;
      } catch (error: any) {
        const msg = error?.response?.data?.message || error?.message;
        set((state) => {
          state.notification.data.push({
            status: "ERROR",
            content: msg,
          });
        }, false, "fetchAllUsers: error");
        return [];
      } finally {
        set((state) => {
          state.loading.isLoading = false;
        }, false, "loading: end");
      }
    },

    fetchAllChildren: async () => {
      set((state) => {
        state.loading.isLoading = true;
      }, false, "loading: start");
      try {
        const resp = await axios.get("/manage/children");
        const data: Patient[] = resp.data || [];
        set((state) => {
          state.profile.children = data;
        }, false, "fetchAllChildren: success");
        return data;
      } catch (error: any) {
        const msg = error?.response?.data?.message || error?.message;
        set((state) => {
          state.notification.data.push({
            status: "ERROR",
            content: msg,
          });
        }, false, "fetchAllChildren: error");
        return [];
      } finally {
        set((state) => {
          state.loading.isLoading = false;
        }, false, "loading: end");
      }
    },

    registerUser: async (payload) => {
      set((state) => {
        state.loading.isLoading = true;
      }, false, "loading: start");
      try {
        await axios.post(`/users/createUser`, payload);
        set((state) => {
          state.notification.data.push({
            status: "SUCCESS",
            content: "Tạo tài khoản thành công! Vui lòng kiểm tra email để xác thực.",
          });
        }, false, "registerUser: success");
      } catch (error: any) {
        const msg = error?.response?.data?.message || error?.message;
        set((state) => {
          state.notification.data.push({
            status: "ERROR",
            content: msg,
          });
        }, false, "registerUser: error");
      } finally {
        set((state) => {
          state.loading.isLoading = false;
        }, false, "loading: end");
      }
    },

    verifyUser: async (email, code) => {
      set((state) => {
        state.loading.isLoading = true;
      }, false, "loading: start");
      try {
        const body = { email, code };
        await axios.post(`/users/verify`, body);
        set((state) => {
          state.notification.data.push({
            status: "SUCCESS",
            content: "Xác thực email thành công!",
          });
        }, false, "verifyUser: success");
      } catch (error: any) {
        const msg = error?.response?.data?.message || error?.message;
        set((state) => {
          state.notification.data.push({
            status: "ERROR",
            content: msg,
          });
        }, false, "verifyUser: error");
      } finally {
        set((state) => {
          state.loading.isLoading = false;
        }, false, "loading: end");
      }
    },

    resendVerification: async (email) => {
      set((state) => {
        state.loading.isLoading = true;
      }, false, "loading: start");
      try {
        await axios.post(`/users/resend`, { email });
        set((state) => {
          state.notification.data.push({
            status: "SUCCESS",
            content: "Mã xác thực mới đã được gửi!",
          });
        }, false, "resendVerification: success");
      } catch (error: any) {
        const msg = error?.response?.data?.message || error?.message;
        set((state) => {
          state.notification.data.push({
            status: "ERROR",
            content: msg,
          });
        }, false, "resendVerification: error");
      } finally {
        set((state) => {
          state.loading.isLoading = false;
        }, false, "loading: end");
      }
    },

    login: async (username: string, password: string) => {
      set((state) => {
        state.loading.isLoading = true;
      }, false, "loading: start");
      try {
        const response = await axios.post(`/auth/login`, { username, password });
        const result = response.data?.result;
        const token = result?.token;
        if (token) {
          localStorage.setItem("token", token);
          const parsedToken = parseJWT(token);
          const role = parsedToken.scope || "";
          localStorage.setItem("role", role);
          set((state) => {
            state.profile.user = {
              ...parsedToken,
              token,
              username,
              role,
            };
            state.notification.data.push({
              content: "Login success!",
              status: "SUCCESS",
            });
          }, false, "login: success");
        }
      } catch (error: any) {
        const msg = error?.response?.data?.message || error?.message;
        set((state) => {
          state.notification.data.push({
            status: "ERROR",
            content: msg,
          });
        }, false, "login: error");
      } finally {
        set((state) => {
          state.loading.isLoading = false;
        }, false, "loading: end");
      }
    },

    fetchProfile: async () => {
      set((state) => {
        state.loading.isLoading = true;
      }, false, "loading: start");
      try {
        const response = await axios.post(`/users/profile`);
        const profile = response.data?.data || undefined;
        set((state) => {
          state.profile.userProfile = profile;
        }, false, "fetchProfile: success");
      } catch (error: any) {
        const message = error?.response?.data?.message || error?.message;
        set((state) => {
          state.notification.data.push({
            status: "ERROR",
            content: message,
          });
        }, false, "fetchProfile: error");
      } finally {
        set((state) => {
          state.loading.isLoading = false;
        }, false, "loading: end");
      }
    },

    updateProfile: async (data) => {
      set((state) => {
        state.loading.isLoading = true;
      }, false, "loading: start");
      try {
        const response = await axios.post(`/users/profile/update`, data);
        const profile = response.data?.data || undefined;
        set((state) => {
          state.profile.userProfile = profile;
          state.notification.data.push({
            status: "SUCCESS",
            content: "Update profile successfully",
          });
        }, false, "updateProfile: success");
      } catch (error: any) {
        const message = error?.response?.data?.message || error?.message;
        set((state) => {
          state.notification.data.push({
            status: "ERROR",
            content: message,
          });
        }, false, "updateProfile: error");
      } finally {
        set((state) => {
          state.loading.isLoading = false;
        }, false, "loading: end");
      }
    },

    deleteUser: async (userId: string) => {
      set((state) => {
        state.loading.isLoading = true;
      }, false, "loading: start");
      try {
        await axios.delete(`/users/delete/${userId}`);
      } catch (error: any) {
        set((state) => {
          state.profile.error = error.response?.data?.message || error.message;
        }, false, "deleteUser: error");
      } finally {
        set((state) => {
          state.loading.isLoading = false;
        }, false, "loading: end");
      }
    },

    logout: async () => {
      set((state) => {
        state.loading.isLoading = true;
      }, false, "logout: start");
      try {
        localStorage.removeItem("token");
        localStorage.removeItem("role");
        set((state) => {
          state.profile.user = undefined;
          state.notification.data.push({
            content: "Logout success!",
            status: "SUCCESS",
          });
        }, false, "logout: success");
      } catch (error: any) {
        set((state) => {
          state.notification.data.push({
            status: "ERROR",
            content: error?.message || "Logout failed",
          });
        }, false, "logout: error");
      } finally {
        set((state) => {
          state.loading.isLoading = false;
        }, false, "logout: end");
      }
    },

    changePassword: async (oldPassword: string, newPassword: string) => {
      set((state) => {
        state.loading.isLoading = true;
      }, false, "loading: start");
      try {
        const body = {
          oldPassword,
          newPassword,
          confirmPassword: newPassword,
        };
        await axios.post(`/users/changePassword`, body);
        set((state) => {
          state.notification.data.push({
            status: "SUCCESS",
            content: "Change password successfully",
          });
        }, false, "changePassword: success");
      } catch (error: any) {
        const msg = error?.response?.data?.message || error?.message;
        set((state) => {
          state.notification.data.push({
            status: "ERROR",
            content: msg,
          });
        }, false, "changePassword: error");
      } finally {
        set((state) => {
          state.loading.isLoading = false;
        }, false, "loading: end");
      }
    },

    createChild: async (parentId: string, childRequest) => {
      set((state) => {
        state.loading.isLoading = true;
      }, false, "loading: start");
      try {
        await axios.post(`/staff/children/create/${parentId}`, childRequest);
      } catch (error: any) {
        const msg = error?.response?.data?.message || error?.message;
        set((state) => {
          state.notification.data.push({ status: "ERROR", content: msg });
        }, false, "createChild: error");
      } finally {
        set((state) => {
          state.loading.isLoading = false;
        }, false, "loading: end");
      }
    },

    updateMyChild: async (childId: string, childRequest) => {
      set((state) => {
        state.loading.isLoading = true;
      }, false, "loading: start");
      try {
        await axios.put(`/staff/children/${childId}/update`, childRequest);
      } catch (error: any) {
        const msg = error?.response?.data?.message || error?.message;
        set((state) => {
          state.notification.data.push({ status: "ERROR", content: msg });
        }, false, "updateMyChild: error");
      } finally {
        set((state) => {
          state.loading.isLoading = false;
        }, false, "loading: end");
      }
    },
  };
}
