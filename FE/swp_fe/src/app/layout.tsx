import type { Metadata } from "next";
import "./globals.css";
import { Toaster } from "@/components/ui/toaster";
import Header from "@/components/Header"; 
import { Footer } from "@/components/Footer";

export const metadata: Metadata = {
  title: "Vaxchild",
  description: "Chăm sóc sức khoẻ con trẻ bạn",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" suppressHydrationWarning>
      <body className="flex flex-col flex-1">
          <Header />
          {children}
          <Footer />
          <Toaster />
      </body>
    </html>
  );
}
