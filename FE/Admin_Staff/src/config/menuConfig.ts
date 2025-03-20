import { UserRole } from "@/types/menu";
import {
  LayoutDashboard,
  Users,
  CalendarDays,
  Baby,
  Syringe,
  ListChecks,
  Layers,
  BadgeCheck,
  Bell,
  FileText,
  MessageSquare
} from "lucide-react";

export interface SubMenuItem {
  icon: React.ComponentType<{ className?: string }>;
  label: string;
  key: string;
  path?: string;
}

export interface MenuItem {
  icon: React.ComponentType<{ className?: string }>;
  label: string;
  key: string;
  subItems?: SubMenuItem[];
  path?: string;
  onClick?: () => void;
}

export const menuItemsByRole: Record<UserRole,
  MenuItem[]> = {
  ROLE_CHILD: [],
  ROLE_CUSTOMER: [],
  STAFF: [
    {
      label: "Dashboard",
      icon: LayoutDashboard,
      key: "dashboard",
      path: "/dashboard",
    },
    {
      label: "Patients",
      icon: Users,
      key: "patients",
      subItems: [
        {
          label: "Parents",
          icon: Users,
          key: "parents",
          path: "/parents"
        },
        {
          label: "Children",
          icon: Baby,
          key: "children",
          path: "/children"
        },
      ],
    },
    {
      label: "Bookings",
      icon: CalendarDays,
      key: "bookings",
      path: "/orders",
    },
    {
      label: "Products",
      icon: Layers,
      key: "category",
      subItems: [
        {
          label: "Categories",
          icon: ListChecks,
          key: "categories",
          path: "/categories"
        },
        {
          label: "Vaccines",
          icon: Syringe,
          key: "vaccines",
          path: "/vaccines",
        },
      ],
    },
    {
      label: "Post Management",
      icon: FileText,
      key: "post",
      path: "/posts",
    },
    {
      label: "Feedback",
      icon: MessageSquare,
      key: "feedback",
      path: "/feedback",
    },
    {
      label: "Notifications",
      icon: Bell,
      key: "notifications",
      path: "/notifications",
    },
  ],
  ADMIN: [
    {
      label: "Dashboard",
      icon: LayoutDashboard,
      key: "dashboard",
      path: "/dashboard",
    },
    {
      label: "Management",
      icon: BadgeCheck,
      key: "management",
      subItems: [
        {
          label: "Users",
          icon: Users,
          key: "users",
          path: "/users-management"
        },
        {
          label: "Staff",
          icon: BadgeCheck,
          key: "staff",
          path: "/staff-management"
        },
        {
          label: "Roles & Permissions",
          icon: BadgeCheck,
          key: "roles",
          path: "/roles-premission"
        },
      ],
    },
    {
      label: "Products",
      icon: Layers,
      key: "category",
      subItems: [
        {
          label: "Categories",
          icon: ListChecks,
          key: "categories",
          path: "/categories"
        },
        {
          label: "Vaccines",
          icon: Syringe,
          key: "vaccines",
          path: "/vaccines",
        },
      ],
    },
    {
      label: "Feedback",
      icon: MessageSquare,
      key: "feedback",
      path: "/feedback",
    },
    // {
    //   label: "Reports",
    //   icon: FileText,
    //   key: "reports",
    //   subItems: [
    //     { label: "Daily Orders", 
    // icon: History, 
    // key: "daily-orders" },
    //     { label: "Top Vaccine", 
    // icon: Syringe, 
    // key: "top-vaccine" },
    //     { label: "Revenue", 
    // icon: TrendingUp, 
    // key: "revenue" },
    //     { label: "Vaccinated Age", 
    // icon: Baby, 
    // key: "vaccinated-age" },
    //   ],
    // },
    {
      label: "Notifications",
      icon: Bell,
      key: "notifications",
      path: "/notifications",
    },
  ],
};
