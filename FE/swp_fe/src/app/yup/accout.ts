import * as yup from "yup";

export const Accout = yup.object().shape({
  email: yup
    .string()
    .email("Email không hợp lệ")
    .required("Vui lòng nhập email"),
  username: yup.string().required("Vui lòng nhập username"),
  bod: yup.string().required("Vui lòng nhập ngày sinh"),
  gender: yup.string().required("Vui lòng chọn giới tính"),
  fullname: yup
    .string()

    .required("Vui lòng nhập Họ và tên"),
  phone: yup
    .string()
    .matches(/^\d{10,11}$/, "Số điện thoại không hợp lệ")
    .required("Vui lòng nhập số điện thoại"),
  password: yup
    .string()
    .min(6, "Mật khẩu phải có ít nhất 6 ký tự")
    .required("Vui lòng nhập mật khẩu"),
});


export const LoginFormYup = yup.object().shape({
  username: yup.string().required("Vui lòng nhập username"),
  password: yup
    .string()
    .min(3, "Mật khẩu phải có ít nhất 6 ký tự")
    .required("Vui lòng nhập mật khẩu"),
});
