import { StoreSet } from "@/store"
import axios from "@/utils/axiosConfig"
import { Notification } from "@/types/notification"

export interface NotificationState {
  notifications: Notification[]
  loading: boolean
}

export interface NotificationActions {
  fetchNotifications: () => Promise<void>
  markAsRead: (id: number) => Promise<void>
  markAllAsRead: () => Promise<void>
  setNotifications: (notifications: Notification[]) => void
}

export const initialNotification: NotificationState = {
  notifications: [],
  loading: false,
}

export function notificationActions(set: StoreSet): NotificationActions {
  return {
    fetchNotifications: async () => {
      try {
        set((state) => {
          state.notification.loading = true
        })
        const token = localStorage.getItem("token")
        const response = await axios.get("/notification/notifications", {
          headers: { Authorization: `Bearer ${token}` },
        })
        const data: Notification[] = response.data.result || response.data || []
        set((state) => {
          state.notification.notifications = data
        })
      } catch (error) {
        console.error("Failed to fetch notifications", error)
      } finally {
        set((state) => {
          state.notification.loading = false
        })
      }
    },

    markAsRead: async (id: number) => {
      try {
        const token = localStorage.getItem("token")
        await axios.put(`/notification/notifications/${id}/read`, null, {
          headers: { Authorization: `Bearer ${token}` },
        })
        set((state) => {
          state.notification.notifications = state.notification.notifications.map((notif) =>
            notif.id === id ? { ...notif, readStatus: true } : notif
          )
        })
      } catch (error) {
        console.error("Failed to mark notification as read", error)
      }
    },

    markAllAsRead: async () => {
      try {
        const token = localStorage.getItem("token")
        await axios.put("/notification/notifications/read-all", null, {
          headers: { Authorization: `Bearer ${token}` },
        })
        set((state) => {
          state.notification.notifications = state.notification.notifications.map((notif) => ({
            ...notif,
            readStatus: true,
          }))
        })
      } catch (error) {
        console.error("Failed to mark all notifications as read", error)
      }
    },

    setNotifications: (notifications: Notification[]) => {
      set((state) => {
        state.notification.notifications = notifications
      })
    },
  }
}
