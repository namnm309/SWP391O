import { create } from "zustand";
import { immer } from "zustand/middleware/immer";
import { devtools, persist } from "zustand/middleware";
import { Draft } from "immer";
import { initialLoading, LoadingState } from "./store/loading";
import {
  initialProfile,
  profileActions,
  ProfileActions,
  ProfileState,
} from "@/store/profile";
import {
  initialNotification,
  NotificationActions,
  notificationActions,
  NotificationState,
} from "@/store/notification";
import { initialManagement, managementActions, ManagementActions, ManagementState } from "./store/management";
import { initialProduct, ProductActions, productActions, ProductState } from "./store/product";

export interface State {
  loading: LoadingState;
  notification: NotificationState;
  profile: ProfileState;
  management: ManagementState;
  product: ProductState;
}

export type StoreSet = {
  <U>(f: (state: Draft<State>) => U, replace?: false, action?: string): void;
  <U>(f: (state: Draft<State>) => U, replace: true, action?: string): void;
};

export type Actions = ProfileActions & NotificationActions & ManagementActions & ProductActions;
export type Store = State & Actions;
export type StoreGet = () => Store;

export const useStore = create<Store>()(
  devtools(
    persist(
      immer((set, get) => ({
        profile: initialProfile,
        ...profileActions(set),
        notification: initialNotification,
        ...notificationActions(set),
        management: initialManagement,
        ...managementActions(set, get),
        product: initialProduct,
        ...productActions(set, get),
        loading: initialLoading,
      })),
      { name: "zustand-store"}
    )
  )
);