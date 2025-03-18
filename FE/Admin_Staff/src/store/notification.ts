import type { StoreGet, StoreSet } from "../store";

export interface Notification {
  content: string;
  status: "ERROR" | "SUCCESS";
}

export interface NotificationState {
  data: Notification[];
}

export interface NotificationActions {
  addNotification: (notification: Notification) => void;
  clearNotification: () => void;
}

export const initialNotification: NotificationState = {
  data: [],
};

export function notificationActions(
  set: StoreSet,
  get: StoreGet
): NotificationActions {
  return {
    addNotification: (notification) => {
      set((state) => {
        state.notification.data.push(notification);
      });
    },
    clearNotification: () => {
      set((state) => {
        state.notification.data = [];
      });
    },
  };
}
