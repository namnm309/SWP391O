import { Patient, User, UserProfile } from "@/models/user";
import type { StoreSet } from "@/store";
import axios from "@/utils/axiosConfig";
import { parseJWT } from "@/utils/validate";
import { toast } from "@/hooks/use-toast";
import { ApiError } from "@/types/error";
import { UserRole } from "@/types/menu";

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
  updateProfile: (data: Partial<UserProfile>) => Promise<void>;
  deleteUser: (userId: number) => Promise<void>;
  logout: () => Promise<void>;
  changePassword: (oldPassword: string, newPassword: string) => Promise<void>;
  createChild: (parentId: string, childRequest: {
    fullname: string;
    dob: string;
    gender: string;
  }) => Promise<void>;
  updateMyChild: (childId: string, childRequest: {
    fullname: string;
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

interface JWTPayload {
  scope: string;
  [key: string]: unknown;
}

export function profileActions(set: StoreSet): ProfileActions {
  return {
    fetchAllUsers: async () => {
      set((state) => {
        state.loading.isLoading = true;
      }, false, "loading: start");
      try {
        const token = localStorage.getItem('token')
        const resp = await axios.get("/manage/parents", {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        const data: Patient[] = resp.data || [];
        set((state) => {
          state.profile.allUsers = data;
        }, false, "fetchAllUsers: success");
        return data;
      } catch (error: unknown) {
        const err = error as ApiError;
        const msg = err?.response?.data?.message || err?.message;
        toast({ variant: "destructive", description: msg });
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
      } catch (error: unknown) {
        const err = error as ApiError;
        const msg = err?.response?.data?.message || err?.message;
        toast({ variant: "destructive", description: msg });
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
        toast({
          title: "Success",
          description: "Account created successfully! Please check your email for verification.",
          variant: "success"
        });
      } catch (error: unknown) {
        const err = error as ApiError;
        const msg = err?.response?.data?.message || err?.message;
        toast({
          title: "Error",
          description: msg,
          variant: "destructive"
        });
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
        toast({
          title: "Success",
          description: "Email verified successfully!",
          variant: "success"
        });
      } catch (error: unknown) {
        const err = error as ApiError;
        const msg = err?.response?.data?.message || err?.message;
        toast({
          title: "Error",
          description: msg,
          variant: "destructive"
        });
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
        toast({
          title: "Success",
          description: "New verification code has been sent!",
          variant: "success"
        });
      } catch (error: unknown) {
        const err = error as ApiError;
        const msg = err?.response?.data?.message || err?.message;
        toast({
          title: "Error",
          description: msg,
          variant: "destructive"
        });
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
          const rawToken = parseJWT(token);
          const parsedToken = (typeof rawToken === 'object' && rawToken !== null ? rawToken : {}) as JWTPayload;
          const roleParsedToken: string = parsedToken.scope || "";
          const role = roleParsedToken.replace(/ROLE_/g, "") as UserRole;
          localStorage.setItem("role", role);
          set((state) => {
            state.profile.user = {
              id: result.id || 0,
              name: result.name || username,
              username,
              fullname: result.fullname || username,
              email: result.email,
              token,
              role
            };
          }, false, "login: success");
          toast({
            title: "Success",
            description: "Login successful!",
            variant: "success"
          });
        }
      } catch (error: unknown) {
        const err = error as ApiError;
        const msg = err?.response?.data?.message || err?.message;
        toast({
          title: "Error",
          description: msg,
          variant: "destructive"
        });
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
      } catch (error: unknown) {
        const err = error as ApiError;
        const message = err?.response?.data?.message || err?.message;
        toast({ variant: "destructive", description: message });
      } finally {
        set((state) => {
          state.loading.isLoading = false;
        }, false, "loading: end");
      }
    },

    updateProfile: async (data: Partial<UserProfile>) => {
      set((state) => {
        state.loading.isLoading = true;
      }, false, "loading: start");
      try {
        const response = await axios.post(`/users/profile/update`, data);
        const profile = response.data?.data || undefined;
        set((state) => {
          state.profile.userProfile = profile;
        }, false, "updateProfile: success");
        toast({
          title: "Success",
          description: "Profile updated successfully",
          variant: "success"
        });
      } catch (error: unknown) {
        const err = error as ApiError;
        const message = err?.response?.data?.message || err?.message;
        toast({
          title: "Error",
          description: message,
          variant: "destructive"
        });
      } finally {
        set((state) => {
          state.loading.isLoading = false;
        }, false, "loading: end");
      }
    },

    deleteUser: async (userId: number) => {
      set((state) => {
        state.loading.isLoading = true;
      }, false, "loading: start");
      try {
        await axios.delete(`/users/delete/${userId}`);
      } catch (error: unknown) {
        const err = error as ApiError;
        set((state) => {
          state.profile.error = err.response?.data?.message || err.message;
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
        }, false, "logout: success");
        toast({
          title: "Success",
          description: "Logout successful!",
          variant: "success"
        });
      } catch (error: unknown) {
        const err = error as ApiError;
        toast({
          title: "Error",
          description: err?.message || "Logout failed",
          variant: "destructive"
        });
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
        toast({
          title: "Success",
          description: "Password changed successfully",
          variant: "success"
        });
      } catch (error: unknown) {
        const err = error as ApiError;
        const msg = err?.response?.data?.message || err?.message;
        toast({
          title: "Error",
          description: msg,
          variant: "destructive"
        });
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
        toast({
          title: "Success",
          description: "Child added successfully",
          variant: "success"
        });
      } catch (error: unknown) {
        const err = error as ApiError;
        const msg = err?.response?.data?.message || err?.message;
        toast({
          title: "Error",
          description: msg,
          variant: "destructive"
        });
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
        toast({
          title: "Success",
          description: "Child information updated successfully",
          variant: "success"
        });
      } catch (error: unknown) {
        const err = error as ApiError;
        const msg = err?.response?.data?.message || err?.message;
        toast({
          title: "Error",
          description: msg,
          variant: "destructive"
        });
      } finally {
        set((state) => {
          state.loading.isLoading = false;
        }, false, "loading: end");
      }
    },
  };
}