"use client"
import { useEffect, useState } from "react"
import { Calendar, Plus } from "lucide-react"
import { useStore } from "@/store"
import { Button } from "@/components/ui/button"
import { Card, CardContent } from "@/components/ui/card"
import { Tabs, TabsList, TabsTrigger } from "@/components/ui/tabs"
import { cn } from "@/lib/utils"
import { CreateNotificationModal } from "@/components/modals/CreateNotificationModal"

export default function NotificationsPage() {
  const { user } = useStore((state) => state.profile)

  const notifications = useStore((state) => state.notification.notifications) || []
  const sendedNotifications = useStore((state) => state.notification.sendedNotifications) || []
  const fetchNotifications = useStore((state) => state.fetchNotifications)
  const fetchSendedNotifications = useStore((state) => state.fetchSendedNotifications)
  const markAsRead = useStore((state) => state.markAsRead)
  const markAllAsRead = useStore((state) => state.markAllAsRead)

  const [activeTab, setActiveTab] = useState("all")
  const [isCreateModalOpen, setIsCreateModalOpen] = useState(false)

  useEffect(() => {
    if (user) {
      fetchNotifications()
      fetchSendedNotifications()
    }
  }, [user, fetchNotifications, fetchSendedNotifications])

  const filteredNotifications =
    activeTab === "send"
      ? sendedNotifications
      : notifications.filter((notification) => {
          if (activeTab === "unread" && notification.readStatus) return false
          if (activeTab === "read" && !notification.readStatus) return false
          return true
        })

  const unreadCount = notifications.filter((n) => !n.readStatus).length

  const handleCreateNotification = () => {
    setIsCreateModalOpen(true)
  }

  return (
    <div className="space-y-4">
      <div className="flex items-center justify-between">
        <h1 className="text-2xl font-bold">Notifications</h1>
        {user && (user.role === "ADMIN" || user.role === "STAFF") && (
          <Button onClick={handleCreateNotification}>
            <Plus className="mr-2 h-4 w-4" />
            Create Notification
          </Button>
        )}
      </div>

      <div className="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <Tabs value={activeTab} onValueChange={setActiveTab} className="w-full sm:w-auto">
          <TabsList>
            <TabsTrigger value="all">All</TabsTrigger>
            <TabsTrigger value="unread">Unread</TabsTrigger>
            <TabsTrigger value="read">Read</TabsTrigger>
            <TabsTrigger value="send">Send</TabsTrigger>
          </TabsList>
        </Tabs>
      </div>

      <Card>
        <CardContent className="p-0">
          {filteredNotifications.length === 0 ? (
            <div className="flex h-40 items-center justify-center">
              <p className="text-sm text-muted-foreground">No notifications found</p>
            </div>
          ) : (
            <div className="divide-y">
              <div className="flex items-center justify-between border-b p-3">
                <span className="font-semibold">
                  {activeTab === "send" ? "Sent Notifications" : "Notifications"}
                </span>
                {activeTab !== "send" && unreadCount > 0 && (
                  <Button
                    variant="ghost"
                    size="sm"
                    className="h-auto p-0 text-xs text-blue-600"
                    onClick={() => markAllAsRead()}
                  >
                    Read All
                  </Button>
                )}
              </div>
              {filteredNotifications.map((notification) => (
                <div
                  key={notification.id}
                  className={cn(
                    "cursor-pointer p-4 transition-colors hover:bg-gray-50",
                    !notification.readStatus && "bg-blue-50"
                  )}
                  onClick={() => {
                    if (activeTab !== "send") {
                      markAsRead(notification.id)
                    }
                  }}
                >
                  <p className="mt-1 line-clamp-2 text-sm text-muted-foreground">
                    {notification.message}
                  </p>
                  <div className="mt-2 flex items-center justify-between text-xs text-muted-foreground">
                    {notification.sender && <span>From: {notification.sender}</span>}
                    <div className="flex items-center">
                      <Calendar className="mr-1 h-3 w-3" />
                      <span>{new Date(notification.createdAt).toLocaleString()}</span>
                    </div>
                  </div>
                </div>
              ))}
            </div>
          )}
        </CardContent>
      </Card>

      {isCreateModalOpen && (
        <CreateNotificationModal
          onClose={() => {
            setIsCreateModalOpen(false)
            fetchNotifications()
          }}
        />
      )}
    </div>
  )
}
