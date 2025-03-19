import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  /* config options here */
  // tStrictMode: true,

  images: {
    domains: [
      "i.pinimg.com",
      "vnvc.vn",
      "via.placeholder.com",
      "res.cloudinary.com",
      "avatars.githubusercontent.com",
    ],
  },
};

export default nextConfig;
