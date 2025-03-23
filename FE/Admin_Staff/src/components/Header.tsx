"use client"
import { useEffect } from "react"
import { useRouter } from "next/navigation"
import { Bell, ChevronDown, LogOut } from "lucide-react"
import { useStore } from "@/store"
import { Button } from "@/components/ui/button"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import { Badge } from "@/components/ui/badge"

export function Header() {
  const router = useRouter()
  const { user } = useStore((state) => state.profile)
  const logout = useStore((state) => state.logout)

  const notifications = useStore((state) => state.notification.notifications) || [];
  const loadingNotifications = useStore((state) => state.notification.loading)
  const fetchNotifications = useStore((state) => state.fetchNotifications)
  const markAsRead = useStore((state) => state.markAsRead)
  const markAllAsRead = useStore((state) => state.markAllAsRead)

  useEffect(() => {
    fetchNotifications()
  }, [fetchNotifications])

  const unreadCount = notifications.filter((n) => !n.readStatus).length

  const handleLogout = () => {
    logout()
    router.push("/")
  }

  return (
    <header className="flex h-16 items-center justify-end border-b bg-white px-6">
      <div className="flex items-center gap-4">
        {/* Notifications Dropdown */}
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="ghost" size="icon" className="relative">
              <Bell className="h-5 w-5 text-black" />
              {unreadCount > 0 && (
                <Badge
                  className="absolute -right-1 -top-1 flex h-5 w-5 items-center justify-center rounded-full bg-red-500 p-0 text-xs text-white"
                  variant="destructive"
                >
                  {unreadCount}
                </Badge>
              )}
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent className="w-80 p-0" align="end">
            <div className="flex items-center justify-between border-b p-3">
              <DropdownMenuLabel className="font-semibold">Notifications</DropdownMenuLabel>
              {unreadCount > 0 && (
                <Button
                  variant="ghost"
                  size="sm"
                  className="h-auto p-0 text-xs text-blue-600"
                  onClick={() => markAllAsRead()}
                >
                  Mark all as read
                </Button>
              )}
            </div>
            <div className="max-h-[300px] overflow-y-auto">
              {notifications.length > 0 ? (
                notifications.map((notification) => (
                  <DropdownMenuItem
                    key={notification.id}
                    className={`flex cursor-pointer flex-col p-3 hover:bg-gray-50 ${
                      !notification.readStatus ? "bg-blue-50" : ""
                    }`}
                    onClick={() => markAsRead(notification.id)}
                  >
                    <div className="flex gap-1">
                      {!notification.readStatus && (
                        <div className="h-2 w-2 rounded-full bg-blue-600"></div>
                      )}
                      <p className="text-sm text-gray-600">{notification.message}</p>
                    </div>
                    <span className="ml-6 w-full text-xs text-gray-500">
                      {new Date(notification.createdAt).toLocaleString()}
                    </span>
                  </DropdownMenuItem>
                ))
              ) : (
                <div className="p-4 text-center text-gray-500">
                  {loadingNotifications ? "Loading..." : "No notifications"}
                </div>
              )}
            </div>
            <DropdownMenuSeparator />
            <div className="p-2">
              <Button
                variant="ghost"
                className="w-full justify-center text-sm text-blue-600"
                onClick={() => router.push("/notifications")}
              >
                View all notifications
              </Button>
            </div>
          </DropdownMenuContent>
        </DropdownMenu>

        {/* User Avatar Dropdown */}
        <DropdownMenu>
          <DropdownMenuTrigger asChild>
            <Button variant="ghost" className="flex items-center gap-2 hover:bg-gray-100">
              <Avatar className="h-8 w-8">
                <AvatarImage src="/images/placeholder-user.webp" alt="User" />
                <AvatarFallback>AD</AvatarFallback>
              </Avatar>
              <div className="flex flex-col items-start text-sm">
                <span className="font-medium">{user?.name}</span>
                <span className="text-xs text-gray-500">{user?.email || ""}</span>
              </div>
              <ChevronDown className="h-4 w-4" />
            </Button>
          </DropdownMenuTrigger>
          <DropdownMenuContent align="end" className="w-56">
            <DropdownMenuLabel>My Account</DropdownMenuLabel>
            <DropdownMenuSeparator />
            {/* <DropdownMenuItem>
              <User className="mr-2 h-4 w-4" />
              <span>Profile</span>
            </DropdownMenuItem>
            <DropdownMenuItem>
              <Settings className="mr-2 h-4 w-4" />
              <span>Settings</span>
            </DropdownMenuItem>
            <DropdownMenuSeparator /> */}
            <DropdownMenuItem onClick={handleLogout}>
              <LogOut className="mr-2 h-4 w-4" />
              <span>Log out</span>
            </DropdownMenuItem>
          </DropdownMenuContent>
        </DropdownMenu>
      </div>
    </header>
  )
}
