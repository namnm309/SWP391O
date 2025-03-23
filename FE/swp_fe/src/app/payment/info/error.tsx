"use client";

import { useEffect } from "react";
import Link from "next/link";
import { AlertTriangle, Home, ArrowLeft } from "lucide-react";

import { Button } from "@/components/ui/button";
import {
  Card,
  CardContent,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";

export default function Error({
  error,
  reset,
}: {
  error: Error & { digest?: string };
  reset: () => void;
}) {
  useEffect(() => {
    console.error(error);
  }, [error]);

  return (
    <div className="flex min-h-screen flex-col items-center justify-center bg-gray-50 p-4">
      <Card className="w-full max-w-md shadow-lg">
        <CardHeader className="bg-red-50 rounded-t-lg">
          <CardTitle className="flex items-center justify-center gap-2 text-xl">
            <AlertTriangle className="h-8 w-8 text-red-500" />
            <span className="text-red-700">Đã xảy ra lỗi</span>
          </CardTitle>
        </CardHeader>

        <CardContent className="mt-4 space-y-4">
          <div className="rounded-lg bg-red-50 p-4 text-center text-red-700">
            <p>
              Không thể xử lý thông tin thanh toán. Vui lòng thử lại hoặc liên
              hệ hỗ trợ.
            </p>
          </div>
        </CardContent>

        <CardFooter className="flex flex-col gap-3 sm:flex-row">
          <Button variant="outline" className="w-full" asChild>
            <Link href="/">
              <Home className="mr-2 h-4 w-4" />
              Trang chủ
            </Link>
          </Button>
          <Button
            className="w-full bg-blue-600 hover:bg-blue-700"
            onClick={reset}
          >
            <ArrowLeft className="mr-2 h-4 w-4" />
            Thử lại
          </Button>
        </CardFooter>
      </Card>
    </div>
  );
}
