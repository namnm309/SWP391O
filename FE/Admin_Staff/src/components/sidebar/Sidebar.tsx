"use client"

import { useState, useEffect } from "react"
import Link from "next/link"
import { usePathname, useRouter } from "next/navigation"
import { ChevronLeft, ChevronRight } from "lucide-react"
import { cn } from "@/lib/utils"
import { useStore } from "@/store"
import { menuItemsByRole } from "@/config/menuConfig"
import type { MenuItem } from "@/types/menu"
import { Button } from "@/components/ui/button"
import { Tooltip, TooltipContent, TooltipProvider, TooltipTrigger } from "@/components/ui/tooltip"
import Image from "next/image"

export function Sidebar() {
  const pathname = usePathname()
  const router = useRouter()
  const profile = useStore.getState().profile
  const user = profile ? profile.user : undefined

  const [sidebarCollapsed, setSidebarCollapsed] = useState<boolean>(false)

  useEffect(() => {
    if (!user || user.role === "ROLE_CUSTOMER" || user.role === "ROLE_CHILD") {
      router.push("/")
    } 
  }, [user, router])

  if (!user) {
    return null
  }

  const [openMenus, setOpenMenus] = useState<Record<string, boolean>>({})

  useEffect(() => {
    if (sidebarCollapsed) {
      setOpenMenus({})
    }
  }, [sidebarCollapsed])

  const toggleMenu = (key: string) => {
    setOpenMenus((prev) => ({
      ...prev,
      [key]: !prev[key],
    }))
  }

  const toggleSidebar = () => {
    setSidebarCollapsed((prev) => !prev)
  }

  const renderMenuItem = (item: MenuItem, level = 0) => {
    const isActive = pathname === item.path
    const hasSubItems = item.subItems && item.subItems.length > 0

    if (hasSubItems) {
      return (
        <div key={item.key} className="relative w-full">
          <Button
            variant="ghost"
            className={cn(
              "flex w-full items-center justify-start gap-2 p-2 hover:bg-blue-50",
              level > 0 && "pl-8",
              sidebarCollapsed && "justify-center",
            )}
            onClick={() => toggleMenu(item.key)}
          >
            <item.icon className="h-5! w-5! text-black" />
            {!sidebarCollapsed && (
              <>
                <span className="flex-1 text-left">{item.label}</span>
                <ChevronRight
                  className={cn("h-4 w-4 transition-transform", openMenus[item.key] && "rotate-90")}
                />
              </>
            )}
          </Button>

          {openMenus[item.key] && (
            <div
              className={cn(
                "space-y-1 pl-2",
                sidebarCollapsed &&
                  "absolute left-full top-0 z-50 ml-2 w-48 rounded-md border bg-white p-2 shadow-md",
              )}
            >
              {item.subItems?.map((subItem) => renderMenuItem(subItem, level + 1))}
            </div>
          )}
        </div>
      )
    }

    const menuItemContent = (
      <div
        className={cn(
          "flex w-full items-center gap-2 rounded-md p-2",
          isActive ? "bg-blue-100 text-blue-700" : "hover:bg-blue-50 hover:text-blue-600",
          level > 0 && "pl-8",
          sidebarCollapsed && "justify-center",
        )}
      >
        <item.icon className="h-5! w-5! text-black" />
        {!sidebarCollapsed && <span>{item.label}</span>}
      </div>
    )

    if (item.onClick) {
      return (
        <Button key={item.key} variant="ghost" className="w-full p-0" onClick={item.onClick}>
          {sidebarCollapsed ? (
            <TooltipProvider>
              <Tooltip>
                <TooltipTrigger asChild>{menuItemContent}</TooltipTrigger>
                <TooltipContent side="right">{item.label}</TooltipContent>
              </Tooltip>
            </TooltipProvider>
          ) : (
            menuItemContent
          )}
        </Button>
      )
    }

    return (
      <Link key={item.key} href={item.path || "#"} className="block w-full">
        {sidebarCollapsed ? (
          <TooltipProvider>
            <Tooltip>
              <TooltipTrigger asChild>{menuItemContent}</TooltipTrigger>
              <TooltipContent side="right">{item.label}</TooltipContent>
            </Tooltip>
          </TooltipProvider>
        ) : (
          menuItemContent
        )}
      </Link>
    )
  }

  const menuItems = menuItemsByRole[user.role] || []

  return (
    <div
      className={cn(
        "flex h-screen flex-col border-r bg-white transition-all duration-300",
        sidebarCollapsed ? "w-16" : "w-64",
      )}
    >
      <div className="flex h-auto items-center justify-between px-4">
        {!sidebarCollapsed && 
          <div className="text-xl font-bold text-blue-600">
            <Image src={"/images/logo_demo.webp"} alt={"VNVC logo"} width={240} height={240} priority/>  
          </div>}
        <Button variant="ghost" size="icon" onClick={toggleSidebar} className={cn("ml-auto")}>
          {sidebarCollapsed ? <ChevronRight className="h-5 w-5" /> : <ChevronLeft className="h-5 w-5" />}
        </Button>
      </div>
      <div className="flex-1 overflow-y-auto p-2">
        <nav className="space-y-1">{menuItems.map((item) => renderMenuItem(item))}</nav>
      </div>
    </div>
  )
}
