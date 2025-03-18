import { UserRole } from "../models/user";
import {
  Person,
  Event,
  History,
  Feedback,
  Dashboard,
  ChildCare,
  Vaccines,
  ManageAccounts,
  Group,
  Badge,
  People,
  PostAdd,
  Category,
  Notifications,
  Assessment,
} from "@mui/icons-material";

export interface SubMenuItem {
  icon: JSX.Element;
  label: string;
  key: string;
}

export interface MenuItem {
  icon: JSX.Element;
  label: string;
  key: string;
  subItems?: SubMenuItem[];
}

export const menuItemsByRole: Record<UserRole, MenuItem[]> = {
  ROLE_CHILD: [],
  ROLE_CUSTOMER: [],
  ROLE_STAFF: [
    {
      label: "Dashboard",
      icon: <Dashboard />,
      key: "dashboard",
    },
    {
      label: "Patients",
      icon: <People />,
      key: "patients",
      subItems: [
        { label: "Parents", icon: <Person />, key: UserRole.CUSTOMER },
        { label: "Children", icon: <ChildCare />, key: "children" },
      ],
    },
    {
      label: "Bookings",
      icon: <Event />,
      key: "bookings",
    },
    {
      label: "Product Management",
      icon: <Vaccines />,
      key: "product-management",
      subItems: [
        { label: "Add Product", icon: <Vaccines />, key: "add-product" },
        { label: "List Products", icon: <Vaccines />, key: "list-products" },
      ],
    },
    {
      label: "Category Management",
      icon: <Category />,
      key: "category-management",
      subItems: [
        { label: "Create Category", icon: <Category />, key: "create-category" },
        { label: "List Categories", icon: <Category />, key: "list-categories" },
      ],
    },
    {
      label: "Post Management",
      icon: <PostAdd />,
      key: "post-management",
      subItems: [
        { label: "Add Post", icon: <PostAdd />, key: "add-post" },
        { label: "List Posts", icon: <PostAdd />, key: "list-posts" },
      ],
    },
    {
      label: "Feedback",
      icon: <Feedback />,
      key: "feedback",
    },
    {
      label: "Notifications",
      icon: <Notifications />,
      key: "notifications",
    },
  ],
  ROLE_ADMIN: [
    {
      label: "Dashboard",
      icon: <Dashboard />,
      key: "dashboard",
    },
    {
      label: "Management",
      icon: <ManageAccounts />,
      key: "management",
      subItems: [
        { label: "Users", icon: <Group />, key: "users-management" },
        { label: "Staff", icon: <Badge />, key: "staff-management" },
        { label: "Roles & Permissions", icon: <ManageAccounts />, key: "roles-management" },
      ],
    },
    {
      label: "Vaccines",
      icon: <Vaccines />,
      key: "vaccines",
    },
    {
      label: "Feedback",
      icon: <Feedback />,
      key: "feedback",
    },
    {
      label: "Reports",
      icon: <Assessment />,
      key: "reports",
      subItems: [
        { label: "Daily Orders", icon: <History />, key: "daily-orders" },
        { label: "Top Vaccine", icon: <Vaccines />, key: "top-vaccine" },
        { label: "Revenue", icon: <History />, key: "revenue" },
        { label: "Vaccinated Age", icon: <ChildCare />, key: "vaccinated-age" },
      ],
    },
    {
      label: "Notifications",
      icon: <Notifications />,
      key: "notifications",
    },
  ],
};
