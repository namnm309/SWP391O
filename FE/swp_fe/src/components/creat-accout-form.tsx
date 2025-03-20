"use client";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { cn } from "@/lib/utils";
import { useForm, FormProvider } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { Accout } from "@/app/yup/accout";
import Image from "next/image";
import Link from "next/link";
import { useToast } from "@/hooks/use-toast";
import { useState } from "react";
import { FormData } from "@/lib/users";
import { createUser, verifyUser } from "@/api/usersApi";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
export function CreatAccoutForm({
  className,
  ...props
}: React.ComponentProps<"div">) {
  const { toast } = useToast();
  const [loading, setLoading] = useState<boolean>(false);
  const [email, setEmail] = useState<string>("");
  const [open, setOpen] = useState<boolean>(false);
  const [verificationCode, setVerificationCode] = useState<string>("");
  const methods = useForm({
    resolver: yupResolver(Accout),
  });
  const {
    handleSubmit,
    register,
    formState: { errors },
  } = methods;

  const onSubmit = async (data: FormData) => {
    setLoading(true);
    try {
      // Add default values for `address` and `image`

      const response = await createUser(data);
      console.log(response);
      console.log(response.code);
      setEmail(data.email);
      if (response.code === 0) {
      console.log(response);

        toast({
          title: "Đăng ký thành công!",
          description: "Vui lòng kiểm tra email để xác thực tài khoản.",
        });
        setOpen(true);
      }
    } catch (error) {
      console.log(error);
      toast({
        variant: "destructive",
        title: "Lỗi!",
        description:
          (error as { response?: { data?: { message?: string } } }).response
            ?.data?.message || "An unexpected error occurred.",
      });
    } finally {
      setLoading(false);
    }
  };
  const onVerify = async () => {
    setLoading(true);
    try {
      // Add default values for `address` and `image`
      const response = await verifyUser({ email, verificationCode });
      console.log(response);
      if (response) {
        toast({
          title: "Xác thực thành công!",
          description: "Vui lòng đăng nhập để sử dụng tài khoản.",
        });
        setOpen(false);
      }
    } catch (error) {
      console.log(error);
      toast({
        variant: "destructive",
        title: "Lỗi!",
        description:
          (error as { response?: { data?: { message?: string } } }).response
            ?.data?.message || "Vui lòng kiểm tra lại.",
      });
    } finally {
      setLoading(false);
    }
  };
  return (
    <div className={cn("flex flex-col gap-6", className)} {...props}>
      <Dialog open={open}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Xác thực địa chỉ mail</DialogTitle>
            <DialogDescription>
              <div className="flex flex-col gap-4">
                <Label htmlFor="code">Mã xác thực</Label>

                <Input
                  id="code"
                  type="text"
                  onChange={(e) => setVerificationCode(e.target.value)}
                  placeholder="Mã xác thực"
                />
                <Button onClick={onVerify} className="w-full">
                  {loading ? "Processing..." : "Xác thực"}
                </Button>
                <Button  onClick={() => setOpen(false)} className="w-full bg-red-500">
                  Hủy bỏ
                </Button>
              </div>
            </DialogDescription>
          </DialogHeader>
        </DialogContent>
      </Dialog>
      <Card className="overflow-hidden">
        <CardContent className="grid p-0 md:grid-cols-2">
          <FormProvider {...methods}>
            <form onSubmit={handleSubmit(onSubmit)} className="p-6 md:p-8">
              <div className="flex flex-col gap-6">
                <div className="flex flex-col items-center text-center">
                  <h1 className="text-2xl font-bold">
                    Welcome to our platform
                  </h1>
                  <p className="text-balance text-muted-foreground">
                    Create a new account
                  </p>
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="username">Tên đăng nhập</Label>
                  <Input
                    id="username"
                    type="text"
                    placeholder="m"
                    {...register("username")}
                  />
                  {errors.username && (
                    <p className="text-red-500">{errors.username.message}</p>
                  )}
                </div>

                <div className="grid gap-2">
                  <Label htmlFor="last_name">Họ và tên</Label>
                  <Input
                    id="last_name"
                    type="text"
                    placeholder="Doe"
                    {...register("fullname")}
                  />
                  {errors.fullname && (
                    <p className="text-red-500">{errors.fullname.message}</p>
                  )}
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="email">Email</Label>
                  <Input
                    id="email"
                    type="email"
                    placeholder="m@example.com"
                    {...register("email")}
                  />
                  {errors.email && (
                    <p className="text-red-500">{errors.email.message}</p>
                  )}
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="phone">Phone Number</Label>
                  <Input
                    id="phone"
                    type="text"
                    placeholder="0123456789"
                    {...register("phone")}
                  />
                  {errors.phone && (
                    <p className="text-red-500">{errors.phone.message}</p>
                  )}
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="bod">Ngày sinh</Label>
                  <Input
                    id="bod"
                    type="date"
                    placeholder="2022-02-02"
                    {...register("bod")}
                  />
                  {errors.bod && (
                    <p className="text-red-500">{errors.bod.message}</p>
                  )}
                </div>
                <div className="grid gap-2">
                  <Label htmlFor="gender">Giới tính</Label>
                  <select
                    id="gender"
                    {...register("gender")}
                    className="p-2 border rounded"
                  >
                    <option value="">Chọn giới tính</option>
                    <option value="male">Nam</option>
                    <option value="female">Nữ</option>
                    <option value="other">Khác</option>
                  </select>
                </div>
                {errors.gender && (
                  <p className="text-red-500">{errors.gender.message}</p>
                )}

                <div className="grid gap-2">
                  <Label htmlFor="password">Password</Label>
                  <Input
                    id="password"
                    type="password"
                    placeholder="********"
                    {...register("password")}
                  />
                  {errors.password && (
                    <p className="text-red-500">{errors.password.message}</p>
                  )}
                </div>
                <Button type="submit" disabled={loading} className="w-full">
                  {loading ? "Processing..." : "Create Account"}
                </Button>

                <div className="text-center text-sm">
                  Already have an account?{" "}
                  <Link href="/login" className="underline underline-offset-4">
                    Log in now
                  </Link>
                </div>
              </div>
            </form>
          </FormProvider>
          <div className="relative hidden bg-muted md:block">
            <Image
              src="https://vnvc.vn/wp-content/uploads/2023/02/tiem-ngua-cho-tre-so-sinh-1.jpg"
              alt="Image"
              width={400}
              height={400}
              className="absolute inset-0 h-full w-full object-cover dark:brightness-[0.2] dark:grayscale"
            />
          </div>
        </CardContent>
      </Card>
    </div>
  );
}
