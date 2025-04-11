import {
  Home,
  Trophy,
  //   UserIcon as Male,
  //   UserIcon as Female,
  // History,
  // BookmarkCheck,
  MessageSquare,
  Facebook,
  ListFilter,
  type LucideIcon,
} from "lucide-react";

export interface MenuItem {
  header?: {
    title: string;
    href: string;
    icon: LucideIcon;
    children?: {
      title: string;
      href: string;
    }[];
  }[];
}

export const routs_vn: MenuItem = {
  header: [
    {
      title: "Trang Chủ",
      href: "/",
      icon: Home,
    },
    {
      title: "Thể Loại",
      href: "/genres",
      icon: ListFilter,
      children: [
        { title: "Hành Động", href: "/genres?hanh-dong" },
        { title: "Phiêu Lưu", href: "/genres?phieu-luu" },
        { title: "Hài Hước", href: "/genres?hai-huoc" },
        { title: "Drama", href: "/genres?drama" },
        { title: "Fantasy", href: "/genres?fantasy" },
        { title: "Kinh Dị", href: "/genres?kinh-di" },
        { title: "Romance", href: "/genres?romance" },
        { title: "Học Đường", href: "/genres?hoc-duong" },
        { title: "Thể Thao", href: "/genres?the-thao" },
      ],
    },
    {
      title: "Xếp Hạng",
      href: "/rankings",
      icon: Trophy,
      children: [
        { title: "Ngày", href: "/ranking/daily" },
        { title: "Tuần", href: "/ranking/weekly" },
        { title: "Tháng", href: "/ranking/monthly" },
      ],
    },
    //   {
    //     title: "Con Trai",
    //     href: "/boy",
    //     icon: Male,
    //   },
    //   {
    //     title: "Con Gái",
    //     href: "/girl",
    //     icon: Female,
    //   },
    // {
    //   title: "Lịch Sử",
    //   href: "/history",
    //   icon: History,
    // },
    // {
    //   title: "Theo Dõi",
    //   href: "/following",
    //   icon: BookmarkCheck,
    // },
    {
      title: "Thảo Luận",
      href: "/discussion",
      icon: MessageSquare,
    },
    {
      title: "Fanpage",
      href: "/fanpage",
      icon: Facebook,
    },
  ],
};

export const routs_en: MenuItem = {
  header: [
    {
      title: "Home",
      href: "/",
      icon: Home,
    },
    {
      title: "Genres",
      href: "/genres",
      icon: ListFilter,
      children: [
        { title: "Action", href: "/genres?hanh-dong" },
        { title: "Adventure", href: "/genres?phieu-luu" },
        { title: "Comedy", href: "/genres?hai-huoc" },
        { title: "Drama", href: "/genres?drama" },
        { title: "Fantasy", href: "/genres?fantasy" },
        { title: "Horror", href: "/genres?kinh-di" },
        { title: "Romance", href: "/genres?romance" },
        { title: "School Life", href: "/genres?hoc-duong" },
        { title: "Sports", href: "/genres?the-thao" },
      ],
    },
    {
      title: "Rankings",
      href: "/rankings",
      icon: Trophy,
      children: [
        { title: "Daily", href: "/ranking/daily" },
        { title: "Weekly", href: "/ranking/weekly" },
        { title: "Monthly", href: "/ranking/monthly" },
      ],
    },
    // {
    //     title: "Boys",
    //     href: "/boy",
    //     icon: Male,
    // },
    // {
    //     title: "Girls",
    //     href: "/girl",
    //     icon: Female,
    // },
    // {
    //   title: "History",
    //   href: "/history",
    //   icon: History,
    // },
    // {
    //   title: "Following",
    //   href: "/following",
    //   icon: BookmarkCheck,
    // },
    {
      title: "Discussion",
      href: "/discussion",
      icon: MessageSquare,
    },
    {
      title: "Fanpage",
      href: "/fanpage",
      icon: Facebook,
    },
  ],
};
