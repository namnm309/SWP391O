import type { ComponentType } from "react"

export type UserRole = "ADMIN" | "STAFF" | "ROLE_CUSTOMER" | "ROLE_CHILD"

export interface MenuItem {
  key: string
  label: string
  icon: ComponentType<{ className?: string }>
  path?: string
  onClick?: () => void
  subItems?: MenuItem[]
  roles?: UserRole[]
}

